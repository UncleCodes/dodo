package com.dodo.utils.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class HttpUtils {
    private static final Pattern browserTypePattern = Pattern
                                                            .compile(
                                                                    "(MSIE [\\d]+\\.[\\d]+)|(Maxthon/[\\d]+\\.[\\d]+)|(QQBrowser)|(GreenBrowser)|(360SE)|(QIHU 360EE)|(Firefox/[\\d]+\\.[\\d]+)|(Opera/[\\d]+\\.[\\d]+)|(Chrome/([\\d]+\\.?)+[\\d]+)|(Safari/[\\d]+\\.[\\d]+)",
                                                                    Pattern.MULTILINE);
    public final static String   UNKNOWN            = "unknown";
    private static final Pattern URIPATTERN         = Pattern
                                                            .compile("^[\\w]+://([\\w-]+\\.)+([\\w-]+)(:[\\d]+)?([\\w/\\.-]*)\\??(.*)$");
    private static final Logger  LOGGER             = LoggerFactory.getLogger(HttpUtils.class);

    protected static <T> T get(HttpClient httpClient, HttpPack pack, HttpWork<T> work) {
        return exec(httpClient, pack, HttpMethod.Get, work);
    }

    protected static <T> T post(HttpClient httpClient, HttpPack pack, HttpWork<T> work) {
        return exec(httpClient, pack, HttpMethod.Post, work);
    }

    protected static <T> T get(HttpPack pack, HttpWork<T> work) {
        return get(HttpClientPool.getConnection(), pack, work);
    }

    protected static <T> T post(HttpPack pack, HttpWork<T> work) {
        return post(HttpClientPool.getConnection(), pack, work);
    }

    private static <T> T exec(HttpClient httpClient, HttpPack pack, HttpMethod methodType, HttpWork<T> work) {
        HttpUriRequest method = null;
        switch (methodType) {
        case Get:
            method = pack.get();
            break;
        case Post:
            method = pack.post();
            break;
        default:
            break;
        }
        if (method == null) {
            return null;
        }

        HttpResponse response = null;
        try {
            response = httpClient.execute(method);
            return work.doWithResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            method.abort();
        }
    }

    public static String read(HttpResponse response) {
        HttpEntity entity = response.getEntity();
        if (entity == null) {
            return null;
        }
        String charset = "utf-8";
        Header contentType = entity.getContentType();
        if (contentType != null) {
            charset = contentType.getValue().indexOf("gbk") == -1 ? "utf-8" : "gbk";
        }
        try {
            return EntityUtils.toString(entity, charset);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] readBytes(HttpResponse response) {
        try {
            return EntityUtils.toByteArray(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void show(HttpResponse response) {
        for (Header h : response.getAllHeaders()) {
            LOGGER.info("{}:{}", h.getName(), h.getValue());
        }
        LOGGER.info(response.getStatusLine().toString());
    }

    public static String getETag(HttpResponse response) {
        String etag = "";
        for (Header h : response.getAllHeaders()) {
            if (h.getName().toLowerCase().equals("etag")) {
                etag = h.getValue();
                break;
            }
        }
        return etag;
    }

    public static boolean saveToFile(HttpResponse response, File file) {
        try {
            InputStream inputStream = response.getEntity().getContent();
            byte[] b = new byte[1024];
            FileOutputStream fStream = new FileOutputStream(file);
            int readLen = -1;
            while ((readLen = inputStream.read(b)) != -1) {
                fStream.write(b, 0, readLen);
            }
            fStream.close();
            LOGGER.info(file.getAbsolutePath());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 性能测试：执行10万次，耗时：547毫秒，每次 0.00547 毫秒
     * */
    public static String getBrowser(HttpServletRequest request) {
        Matcher matcher = browserTypePattern.matcher(request.getHeader("User-Agent"));
        return matcher.find() ? matcher.group() : UNKNOWN;
    }

    // for nginx proxy get real ip
    public static String getRemoteAddr(HttpServletRequest request) {
        String ipString = request.getHeader("X-Real-IP");
        return ipString == null ? request.getRemoteAddr() : ipString;
    }

    public static String getReferer(HttpServletRequest request) {
        return request.getHeader("Referer");
    }

    public static String getUri(String url) {
        Matcher matcher = URIPATTERN.matcher(url == null ? "" : url);
        return matcher.find() ? matcher.group(matcher.groupCount() - 1) : UNKNOWN;
    }
}
