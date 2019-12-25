package com.third.aliyun.oss;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.STSAssumeRoleSessionCredentialsProvider;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.AlibabaCloudCredentials;
import com.aliyuncs.auth.BasicCredentials;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.dodo.common.framework.destroyer.Destroyable;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
@Configuration
@PropertySource("classpath:/aliyunoss.properties")
public class OSSService implements Destroyable {
    private static final Logger           LOGGER                     = LoggerFactory.getLogger(OSSService.class);
    public static final String            OSS_ACCESSKEYID_KEY        = "dodo.aliyun.oss.AccessKeyID";
    public static final String            OSS_ACCESSKEYSECRET_KEY    = "dodo.aliyun.oss.AccessKeySecret";
    public static final String            OSS_ROLEARN_KEY            = "dodo.aliyun.oss.RoleArn";
    public static final String            OSS_BUCKETS_KEY            = "dodo.aliyun.oss.Buckets";
    public static final String            OSS_REGIONID_KEY           = "dodo.aliyun.oss.RegionId";
    public static final String            OSS_ENDPOINT_KEY           = "dodo.aliyun.oss.Endpoint";
    public static final String            OSS_DOMAIN_KEY             = "dodo.aliyun.oss.Bucket.{0}.domain";
    public static final String            OSS_ACCESSROLE_KEY         = "dodo.aliyun.oss.Bucket.{0}.accessRole";

    // 目前只有"cn-hangzhou"这个region可用, 不要使用填写其他region的值
    public static final String            REGION_CN_HANGZHOU         = "cn-hangzhou";
    public static final String            STS_API_VERSION            = "2015-04-01";
    public static final Integer           PRESIGNEDURL_EXPIRESECONDS = 5 * 60;

    private static final Integer          readWriteExpireTimeSeconds = 15 * 60;
    private static Map<String, OSSBucket> buckets                    = new HashMap<String, OSSBucket>();
    private static String                 accessKeyID;
    private static String                 accessKeySecret;
    private static String                 roleArn;
    private static String                 regionId;
    private static String                 endpoint;
    private static OSSClient              client;
    @Autowired
    private Environment                   env;

    @PostConstruct
    public void init() {
        String[] bucketsArr = env.getProperty(OSS_BUCKETS_KEY).split(",");
        for (String b : bucketsArr) {
            OSSBucket bucket = new OSSBucket();
            bucket.setName(b);
            bucket.setDomain(env.getProperty(MessageFormat.format(OSS_DOMAIN_KEY, b)));
            bucket.setAccessRole(BucketAccessRole.valueOf(env.getProperty(MessageFormat.format(OSS_ACCESSROLE_KEY, b))));
            buckets.put(b, bucket);
        }
        accessKeyID = env.getProperty(OSS_ACCESSKEYID_KEY);
        accessKeySecret = env.getProperty(OSS_ACCESSKEYSECRET_KEY);
        roleArn = env.getProperty(OSS_ROLEARN_KEY);
        regionId = env.getProperty(OSS_REGIONID_KEY);
        endpoint = env.getProperty(OSS_ENDPOINT_KEY);
        AlibabaCloudCredentials longLivedCredentials = new BasicCredentials(accessKeyID, accessKeySecret);
        DefaultProfile profile = DefaultProfile.getProfile(regionId);
        CredentialsProvider credentialsProvider = new STSAssumeRoleSessionCredentialsProvider(longLivedCredentials,
                roleArn, profile).withExpiredDuration(readWriteExpireTimeSeconds);
        client = new OSSClient(endpoint, credentialsProvider, new ClientConfiguration());
        LOGGER.info("**********OSSTokenService****************");
        LOGGER.info("Init.........OK");
        LOGGER.info("**********OSSTokenService****************");
    }

    protected static AssumeRoleResponse assumeRole(String accessKeyId, String accessKeySecret, String roleArn,
            String roleSessionName, String policy, ProtocolType protocolType, long durationSeconds)
            throws ClientException {
        try {
            // 创建一个 Aliyun Acs Client, 用于发起 OpenAPI 请求
            IClientProfile profile = DefaultProfile.getProfile(REGION_CN_HANGZHOU, accessKeyId, accessKeySecret);
            DefaultAcsClient client = new DefaultAcsClient(profile);
            // 创建一个 AssumeRoleRequest 并设置请求参数
            final AssumeRoleRequest request = new AssumeRoleRequest();
            request.setVersion(STS_API_VERSION);
            request.setMethod(MethodType.POST);
            request.setProtocol(protocolType);
            request.setRoleArn(roleArn);
            request.setRoleSessionName(roleSessionName);
            request.setPolicy(policy);
            request.setDurationSeconds(durationSeconds);
            // 发起请求，并得到response
            final AssumeRoleResponse response = client.getAcsResponse(request);
            return response;
        } catch (ClientException e) {
            throw e;
        }
    }

    public static Map<String, String> ossAuth(String bucket) {
        if (!buckets.keySet().contains(bucket)) {
            Map<String, String> respMap = new LinkedHashMap<String, String>();
            respMap.put("StatusCode", "500");
            respMap.put("ErrorCode", "ERROR BUCKET");
            respMap.put("ErrorMessage", "Opps...");
            return respMap;
        }
        String policy = OSSPolicys.readWritePolicy(bucket);
        // RoleSessionName 是临时Token的会话名称，自己指定用于标识你的用户，主要用于审计，或者用于区分Token颁发给谁
        // 但是注意RoleSessionName的长度和规则，不要有空格，只能有'-' '_' 字母和数字等字符
        // 具体规则请参考API文档中的格式要求
        String roleSessionName = "dodo-framework";
        // 此处必须为 HTTPS
        ProtocolType protocolType = ProtocolType.HTTPS;
        try {
            final AssumeRoleResponse stsResponse = assumeRole(accessKeyID, accessKeySecret, roleArn, roleSessionName,
                    policy, protocolType, readWriteExpireTimeSeconds);

            Map<String, String> respMap = new LinkedHashMap<String, String>();
            respMap.put("StatusCode", "200");
            respMap.put("AccessKeyId", stsResponse.getCredentials().getAccessKeyId());
            respMap.put("AccessKeySecret", stsResponse.getCredentials().getAccessKeySecret());
            respMap.put("SecurityToken", stsResponse.getCredentials().getSecurityToken());
            respMap.put("Expiration", stsResponse.getCredentials().getExpiration());
            return respMap;
        } catch (ClientException e) {
            Map<String, String> respMap = new LinkedHashMap<String, String>();
            respMap.put("StatusCode", "500");
            respMap.put("ErrorCode", e.getErrCode());
            respMap.put("ErrorMessage", e.getErrMsg());
            return respMap;
        }

    }

    private static String getKey(String normalUrl) throws MalformedURLException {
        String key = normalUrl;
        if (key.startsWith("http")) {
            URL url = new URL(normalUrl);
            key = url.getPath();
        }
        if (key.startsWith("/")) {
            key = key.substring(1);
        }
        return key;
    }

    /**
     * 私有库生成临时URL
     * **/
    public static String generatePresignedUrlFromKey(String bucket, String key, int expireSeconds) {
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucket, key);
        request.setExpiration(DateUtils.addSeconds(new Date(), expireSeconds));
        URL url = client.generatePresignedUrl(request);
        return String.format("%s%s?%s", getDomain(bucket), url.getPath(), url.getQuery());
    }

    /**
     * 私有库生成临时URL
     * **/
    public static String generatePresignedUrlFromUrl(String bucket, String normalUrl) {
        if (StringUtils.isBlank(normalUrl) || gettAccessRole(bucket) != BucketAccessRole.PRIVATE) {
            return normalUrl;
        }
        try {
            return generatePresignedUrlFromKey(bucket, getKey(normalUrl), PRESIGNEDURL_EXPIRESECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return normalUrl + "?Exception=Occ";
    }

    public static String upload(File file, String ossKey, String bucketName) {
        OSSBucket bucket = buckets.get(bucketName);
        if (bucket == null) {
            LOGGER.error("upload to oss error: bucket does't exits.");
            return null;
        }
        try {
            client.putObject(bucket.getName(), getKey(ossKey), file);
            return MessageFormat.format("{0}{1}", bucket.getDomain(), ossKey);
        } catch (Exception e) {
            LOGGER.error("upload to oss error.", e);
            return null;
        }
    }

    public static String upload(byte[] fileBytes, String ossKey, String bucketName) {
        OSSBucket bucket = buckets.get(bucketName);
        if (bucket == null) {
            LOGGER.error("upload to oss error: bucket does't exits.");
            return null;
        }
        try {
            client.putObject(bucket.getName(), getKey(ossKey), new ByteArrayInputStream(fileBytes));
            return MessageFormat.format("{0}{1}", bucket.getDomain(), ossKey);
        } catch (Exception e) {
            LOGGER.error("upload to oss error.", e);
            return null;
        }
    }

    public static Boolean delete(String ossKey, String bucketName) {
        OSSBucket bucket = buckets.get(bucketName);
        if (bucket == null) {
            LOGGER.error("delete oss file error: bucket does't exits.");
            return null;
        }
        try {
            client.deleteObject(bucket.getName(), getKey(ossKey));
            return Boolean.TRUE;
        } catch (Exception e) {
            LOGGER.error("delete oss file error.", e);
            return Boolean.FALSE;
        }
    }

    public static byte[] downloadAsByteArray(String bucketName, String normalUrl) {
        OSSBucket bucket = buckets.get(bucketName);
        if (bucket == null) {
            LOGGER.error("downloadAsByteArray to oss error: bucket does't exits.");
            return null;
        }
        OSSObject object = null;
        try {
            object = client.getObject(bucketName, getKey(normalUrl));
            InputStream stream = object.getObjectContent();
            byte[] bytes = IOUtils.toByteArray(stream);
            closeQuietly(stream);
            return bytes;
        } catch (Exception e) {
            LOGGER.error("downloadAsByteArray to oss error.", e);
            return null;
        } finally {
            try {
                object.forcedClose();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String downloadAsString(String bucketName, String normalUrl) {
        OSSBucket bucket = buckets.get(bucketName);
        if (bucket == null) {
            LOGGER.error("downloadAsString to oss error: bucket does't exits.");
            return null;
        }
        OSSObject object = null;
        try {
            object = client.getObject(bucketName, getKey(normalUrl));
            InputStream stream = object.getObjectContent();
            String str = IOUtils.toString(stream, Charset.defaultCharset());
            closeQuietly(stream);
            return str;
        } catch (Exception e) {
            LOGGER.error("downloadAsString to oss error.", e);
            return null;
        } finally {
            try {
                object.forcedClose();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean downloadAsFile(String bucketName, String normalUrl, String destFilePath) {
        File destFile = new File(destFilePath);
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }

        OSSBucket bucket = buckets.get(bucketName);
        if (bucket == null) {
            LOGGER.error("downloadAsFile to oss error: bucket does't exits.");
            return false;
        }
        OSSObject object = null;
        try {
            object = client.getObject(bucketName, getKey(normalUrl));
            InputStream input = object.getObjectContent();
            OutputStream output = new FileOutputStream(destFile);
            byte[] bytes = IOUtils.toByteArray(input);
            closeQuietly(input);
            IOUtils.write(bytes, output);
            closeQuietly(output);
            return true;
        } catch (Exception e) {
            LOGGER.error("downloadAsFile to oss error.", e);
            return false;
        } finally {
            try {
                object.forcedClose();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String getDomain(String bucket) {
        OSSBucket ossBucket = buckets.get(bucket);
        return ossBucket == null ? "" : ossBucket.getDomain();
    }

    public static BucketAccessRole gettAccessRole(String bucket) {
        OSSBucket ossBucket = buckets.get(bucket);
        return ossBucket == null ? null : ossBucket.getAccessRole();
    }

    public static void closeQuietly(final Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (final IOException ioe) {
            // ignore
        }
    }

    public void destroy() {
        LOGGER.info("[begin]Shuts down the OSS instance (release all resources).");
        client.shutdown();
        LOGGER.info("[Success]Shuts down the OSS instance (release all resources).");
    }
}
