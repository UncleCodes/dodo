package com.dodo.utils.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import com.dodo.common.framework.entity.BaseEntity;
import com.dodo.utils.CommonUtil;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public final class DodoFrameworkConfigUtil {
    public static Class<? extends Serializable> idFieldType;
    static {
        try {
            Class<?> idFieldClass = BaseEntity.class.getDeclaredField("id").getType();
            if (idFieldClass == long.class || idFieldClass == Long.class) {
                idFieldType = Long.class;
            } else {
                idFieldType = String.class;
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static Serializable getRightTypeIdValue(String idPrepare) {
        if (idFieldType == Long.class) {
            return Long.parseLong(idPrepare);
        } else {
            return idPrepare;
        }
    }

    private static Properties ps;
    static {
        ps = new Properties();
        try {
            File parentFile = new File(DodoFrameworkConfigUtil.class.getClassLoader().getResource("").toURI());
            File confFile = new File(parentFile, "dodo_framework_config.properties");

            if (confFile.exists()) {
                try (FileInputStream inStream = new FileInputStream(confFile)) {
                    ps.load(inStream);
                }
            } else {
                // idea IDE dev mode
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setProperty(String key, String value) {
        ps.setProperty(key, value);
    }

    public static String get(String key) {
        return ps.getProperty(key);
    }

    // show doc config 
    public static class DodoShowDocUtil {
        public static Boolean isEncryption;
        public static Boolean isSplitPage;
        public static Boolean isDynamic;
        public static String  defaultSecretkey;
        public static int     secretkeyLength;
        public static String  officeHome;
        public static int[]   officePort;
        public static String  officeHost;
        public static String  officeProtocol;
        public static String  officeProfile;
        public static String  pdfCommand;
        public static int     pdfSinglePageMaxThread;
        public static String  secretkeySeed = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        static {
            try {
                isEncryption = Boolean.valueOf(get("dodo.doc2swf.encryption.is"));
            } catch (Exception e) {
            }
            try {
                isSplitPage = Boolean.valueOf(get("dodo.doc2swf.converter.splitPage"));
            } catch (Exception e) {
            }
            try {
                isDynamic = Boolean.valueOf(get("dodo.doc2swf.encryption.dynamic.is"));
            } catch (Exception e) {
            }
            defaultSecretkey = get("dodo.doc2swf.encryption.secretkey");
            try {
                secretkeyLength = Integer.parseInt(get("dodo.doc2swf.encryption.dynamic.secretkey.length"));
            } catch (Exception e) {
            }
            officeHome = get("dodo.doc2swf.converter.office.home");
            officeHost = get("dodo.doc2swf.converter.office.host");
            officeProtocol = get("dodo.doc2swf.converter.office.protocol");
            pdfCommand = get("dodo.doc2swf.converter.pdf.command");
            try {
                pdfSinglePageMaxThread = Integer.parseInt(get("dodo.doc2swf.converter.pdf.mode.singlePage.maxThread"));
            } catch (Exception e) {
            }
            try {
                String[] portStrs = get("dodo.doc2swf.converter.office.port").split(",");
                officePort = new int[portStrs.length];
                int i = 0;
                for (String s : portStrs) {
                    officePort[i] = Integer.parseInt(s);
                    i++;
                }
            } catch (Exception e) {
            }
            officeProfile = get("dodo.doc2swf.converter.office.profile");
        }

        public static String getSecretkey() {
            return isDynamic ? getRandomKey() : defaultSecretkey;
        }

        public static String getRandomKey() {
            StringBuffer sb = new StringBuffer();
            Random r = new Random();
            int seedLength = secretkeySeed.length();
            for (int i = 0; i < secretkeyLength; i++) {
                sb.append(secretkeySeed.charAt(r.nextInt(seedLength)));
            }
            return sb.toString();
        }
    }

    // log record config 
    public static class DodoLogRecordUtil {
        public static Boolean isLogRecord;

        static {
            try {
                isLogRecord = Boolean.valueOf(get("dodo.backmanage.busilog.record.is"));
            } catch (Exception e) {
                isLogRecord = false;
            }
        }
    }

    // uploader config
    public static class DodoUploaderUtil {
        public static String tempFileDir;
        public static String targetFileDir;
        public static String ueditorUploaderType;
        public static String ueditorOssBucket;

        public static String tempFileDirServletPrefix;
        public static String targetFileDirServletPrefix;

        static {
            ueditorUploaderType = get("dodo.io.uploader.ueditor.uploaderType");
            ueditorOssBucket = get("dodo.io.uploader.ueditor.oss.bucket");
            tempFileDir = get("dodo.io.uploader.tempfiledir").replace("{webroot}", CommonUtil.getWebRootPath());
            targetFileDir = get("dodo.io.uploader.targetfiledir").replace("{webroot}", CommonUtil.getWebRootPath());
            tempFileDirServletPrefix = get("dodo.io.uploader.tempfiledir").replace("{webroot}", "")
                    .replaceAll("\\\\", "/").replaceAll("//", "/");
            targetFileDirServletPrefix = get("dodo.io.uploader.tempfiledir").replace("{webroot}", "")
                    .replaceAll("\\\\", "/").replaceAll("//", "/");
        }
    }

    // page list  config 
    public static class DodoPageListUtil {
        public static Integer globalPageSize;
        public static Integer backPageSize;
        static {
            try {
                globalPageSize = Integer.parseInt(get("dodo.page.list.global_page_size"));
                if (globalPageSize <= 0) {
                    globalPageSize = 20;
                }
            } catch (Exception e) {
                globalPageSize = 20;
            }

            try {
                backPageSize = Integer.parseInt(get("dodo.page.list.back_page_size"));
                if (backPageSize <= 0) {
                    backPageSize = 20;
                }
            } catch (Exception e) {
                backPageSize = 20;
            }
        }
    }

    // database naming strategy config 
    public static class DodoDatabaseNamingStrategyUtil {
        public static Boolean isMarkFontUnderlines;
        public static String  tablePrefix;
        public static Integer maxLength;
        static {
            try {
                isMarkFontUnderlines = Boolean.valueOf(get("dodo.database.namingstrategy.isMarkFontUnderlines"));
            } catch (Exception e) {
                isMarkFontUnderlines = true;
            }

            tablePrefix = get("dodo.database.namingstrategy.tablePrefix");

            try {
                maxLength = Integer.parseInt(get("dodo.database.namingstrategy.maxLength"));
                if (maxLength <= 0) {
                    maxLength = 64;
                }
            } catch (Exception e) {
                maxLength = 64;
            }
        }
    }

    // common config 
    public static class DodoCommonConfigUtil {
        public static Boolean isLoginFailureLock;
        public static Integer loginFailureLockCount;
        public static String  webHomeUrl;
        public static Boolean isRunJob;
        public static String  fileServer;
        public static String  fileServerTemp;
        public static String  fileServerSubstitute;
        public static String  fileServerNotAllowed;
        public static String  passwordSalt;
        public static Long    passwordDynamicSaltTime;
        public static String  viewRootPath;
        public static Boolean isDev = Boolean.FALSE;
        static {
            try {
                isLoginFailureLock = Boolean.valueOf(get("dodo.common.config.isLoginFailureLock"));
            } catch (Exception e) {
                isLoginFailureLock = true;
            }

            try {
                loginFailureLockCount = Integer.parseInt(get("dodo.common.config.loginFailureLockCount"));
                if (loginFailureLockCount <= 0) {
                    loginFailureLockCount = 5;
                }
            } catch (Exception e) {
                loginFailureLockCount = 5;
            }
            webHomeUrl = get("dodo.common.config.web.homeurl");
            isRunJob = Boolean.valueOf(get("dodo.common.config.isRunJob"));
            fileServer = get("dodo.common.config.web.fileserver");
            fileServerSubstitute = get("dodo.common.config.web.fileserver.substitute");
            fileServerTemp = get("dodo.common.config.web.fileserver.temp");
            fileServerNotAllowed = get("dodo.common.config.web.fileserver.notallowed");
            viewRootPath = get("dodo.backmanage.view.rootPath");
            if (StringUtils.isBlank(fileServerNotAllowed)) {
                fileServerNotAllowed = null;
            } else if (!fileServerNotAllowed.endsWith(",")) {
                fileServerNotAllowed = fileServerNotAllowed + ",";
            }
            if (StringUtils.isBlank(fileServer)) {
                fileServer = null;
            }
            if (StringUtils.isBlank(fileServerTemp)) {
                fileServerTemp = null;
            }
            passwordSalt = get("dodo.common.config.password.salt");
            if (passwordSalt == null) {
                passwordSalt = "";
            }
            try {
                passwordDynamicSaltTime = Long.parseLong(get("dodo.common.config.password.dynamic.salt.time.seconds"));
            } catch (Exception e) {
                passwordDynamicSaltTime = 300L;
            }
            isDev = "dev".equals(get("dodo.common.config.mode"));
        }
    }

    // java mail send config 
    public static class DodoMailSenderUtil {
        public static String  smtpHost;
        public static Integer smtpPort;
        public static String  smtpUsername;
        public static String  smtpPassword;
        public static String  smtpCompanyName;
        public static String  smtpFromMail;
        static {
            smtpHost = get("dodo.mailsender.smtpHost");
            try {
                smtpPort = Integer.parseInt(get("dodo.mailsender.smtpPort"));
            } catch (Exception e) {
                smtpPort = 25;
            }
            smtpUsername = get("dodo.mailsender.smtpUsername");
            smtpPassword = get("dodo.mailsender.smtpPassword");
            smtpCompanyName = get("dodo.mailsender.smtpCompanyName");
            smtpFromMail = get("dodo.mailsender.smtpFromMail");
        }
    }

    // video config
    public static class DodoVideoUtil {
        public static String       getThumbnailCommand;
        public static String       getInfoCommand;
        public static String       converterToflvCommand;
        public static String       converterTomp4Command;
        public static String       converterToswfCommand;
        public static String       converterToaviCommand;
        public static String       timeframeAddCommand;
        public static List<String> support = new ArrayList<String>();
        static {
            getThumbnailCommand = get("dodo.video.get.thumbnail.command");
            getInfoCommand = get("dodo.video.get.info.command");
            converterToflvCommand = get("dodo.video.converter.toflv.command");
            converterTomp4Command = get("dodo.video.converter.tomp4.command");
            converterToswfCommand = get("dodo.video.converter.toswf.command");
            converterToaviCommand = get("dodo.video.converter.toavi.command");
            timeframeAddCommand = get("dodo.video.timeframe.add.command");
            try {
                support = Arrays.asList(get("dodo.video.converter.support").split(","));
            } catch (Exception e) {
            }
        }
    }
}
