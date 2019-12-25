package com.dodo.common.doc2swf.office;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.document.DocumentFamily;
import org.artofsolving.jodconverter.document.DocumentFormat;
import org.artofsolving.jodconverter.document.SimpleDocumentFormatRegistry;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.ExternalOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeConnectionProtocol;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dodo.common.doc2swf.Doc2swfUtil;
import com.dodo.utils.config.DodoFrameworkConfigUtil.DodoShowDocUtil;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class DodoOfficeConverter {
    private static final Logger                 LOGGER                 = LoggerFactory
                                                                               .getLogger(DodoOfficeConverter.class);
    private static OfficeConnectionProtocol     CONNECTION_PROTOCOL;
    private static File                         OFFICE_PROFILE;
    private static OfficeManager                officeManager;
    private static String                       OFFICE_HOME            = "";
    private static String                       HOST                   = "127.0.0.1";
    private static int[]                        PORT                   = { 8100 };
    private static SimpleDocumentFormatRegistry documentFormatRegistry = null;
    private static List<DocumentFormat>         documentFormats        = new ArrayList<DocumentFormat>();
    private static StringBuilder                allSupportSb           = new StringBuilder();
    private static Boolean                      isInit                 = Boolean.FALSE;
    private static Boolean                      isServiceStarted       = Boolean.FALSE;

    public static void init() {
        try {
            if (isInit) {
                LOGGER.info("The DodoOfficeConverter has been correctly configured! Do nothing...");
                return;
            }
            documentFormatRegistry = new SimpleDocumentFormatRegistry();
            allSupportSb = new StringBuilder();
            InputStream inputStream = DodoOfficeConverter.class.getClassLoader().getResourceAsStream(
                    DodoOfficeConverter.class.getPackage().getName().replaceAll("\\.", "/") + "/docsupport.js");
            JSONArray array = new JSONArray(IOUtils.toString(inputStream, Charset.defaultCharset()));
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (final IOException ioe) {
            }
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonFormat = array.getJSONObject(i);
                DocumentFormat format = new DocumentFormat();
                format.setName(jsonFormat.getString("name"));
                format.setExtension(jsonFormat.getString("extension"));
                format.setMediaType(jsonFormat.getString("mediaType"));
                if (jsonFormat.has("inputFamily")) {
                    format.setInputFamily(DocumentFamily.valueOf(jsonFormat.getString("inputFamily")));
                }
                if (jsonFormat.has("loadProperties")) {
                    format.setLoadProperties(toJavaMap(jsonFormat.getJSONObject("loadProperties")));
                }
                if (jsonFormat.has("storePropertiesByFamily")) {
                    JSONObject jsonStorePropertiesByFamily = jsonFormat.getJSONObject("storePropertiesByFamily");
                    for (String key : JSONObject.getNames(jsonStorePropertiesByFamily)) {
                        Map<String, ?> storeProperties = toJavaMap(jsonStorePropertiesByFamily.getJSONObject(key));
                        format.setStoreProperties(DocumentFamily.valueOf(key), storeProperties);
                    }
                }
                documentFormatRegistry.addFormat(format);
                documentFormats.add(format);
                if (i != 0) {
                    allSupportSb.append(",");
                }
                allSupportSb.append(format.getExtension());
            }

            LOGGER.info("Configuring DodoOfficeConverter....");
            setOfficeHome(DodoShowDocUtil.officeHome);
            setHost(DodoShowDocUtil.officeHost);
            setPort(DodoShowDocUtil.officePort);
            setConnectionProtocol(OfficeConnectionProtocol.valueOf(DodoShowDocUtil.officeProtocol.toUpperCase()));
            if (StringUtils.isNotBlank(DodoShowDocUtil.officeProfile)) {
                setTemplateProfileDir(new File(DodoShowDocUtil.officeProfile));
            }
            LOGGER.info("Configure DodoOfficeConverter ... OK!....");
            isInit = Boolean.TRUE;
        } catch (Exception e) {
            LOGGER.error("Configure DodoOfficeConverter ... Error! ", e);
            isInit = Boolean.FALSE;
        }
    }

    public static Boolean isSupport(String fileExtends) {
        if (!isInit) {
            LOGGER.error("Please configure DodoOfficeConverter has been correctly configured!");
            return null;
        }
        return documentFormatRegistry.getFormatByExtension(fileExtends) != null;
    }

    public static List<DocumentFormat> getAllSupport() {
        if (!isInit) {
            LOGGER.error("Please configure DodoOfficeConverter first!");
            return null;
        }
        return documentFormats;
    }

    /**
     * 将office转换为PDF
     * 
     * @param inputFile
     * @param out
     *            文件名或文件地址
     * @throws ConnectException
     */
    public static File toPDF(File inputFile, File outputFile) throws IOException {
        if (inputFile.exists()) {
            if (!outputFile.getParentFile().exists()) {
                outputFile.getParentFile().mkdirs();
            }
            return convert(inputFile, outputFile);
        } else {
            throw new IOException("file not found!");
        }
    }

    /**
     * 根据传入文件后缀名转换文件
     * 
     * @param inputFile
     * @param outputFile
     * @return File
     * @throws java.io.IOException
     */
    private static File convert(File inputFile, File outputFile) throws IOException {
        try {
            if (!isServiceStarted) {
                LOGGER.error("The Office Service Doesn't be started!");
                return null;
            }
            if (outputFile.exists()) {
                outputFile.delete();
            }
            boolean isWindows = System.getProperty("os.name").startsWith("Windows");
            if (inputFile.getName().endsWith(".txt")) {
                Charset fileCharset = Doc2swfUtil.getFileEncoding(inputFile);
                if (fileCharset != null) {
                    Charset systemCharset = Charset.defaultCharset();
                    if (!fileCharset.equals(systemCharset)
                            && !(systemCharset.equals(Charset.forName("GBK")) && fileCharset.name().toLowerCase()
                                    .equals("gb2312"))) {
                        String encodedFileName = Doc2swfUtil.getFilePrefix(inputFile.getPath()) + "_encoded."
                                + (isWindows ? "odt" : "txt");
                        File encodedFile = new File(encodedFileName);
                        try {
                            Doc2swfUtil.convertFileEncodingToSys(inputFile, encodedFile);
                        } catch (Exception e) {
                            org.apache.commons.io.FileUtils.copyFile(inputFile, encodedFile);
                        }
                        inputFile = encodedFile;
                    } else if (isWindows) {
                        String encodedFileName = Doc2swfUtil.getFilePrefix(inputFile.getPath()) + "_encoded.odt";
                        File encodedFile = new File(encodedFileName);
                        org.apache.commons.io.FileUtils.copyFile(inputFile, encodedFile);
                        inputFile = encodedFile;
                    }
                }
            }
            LOGGER.info("Converting File:{} --> {}", inputFile.getPath(), outputFile.getPath());
            OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);

            converter.convert(inputFile, outputFile);

            LOGGER.info("Convert File...OK:{} --> {} ", inputFile.getPath(), outputFile.getPath());
            return outputFile;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getCause().getClass().getName());
            if (outputFile.exists()) {
                outputFile.delete();
            }
        } finally {
        }
        return null;
    }

    public static Boolean isReady() {
        return isServiceStarted;
    }

    public static void startService() {
        if (!isInit) {
            LOGGER.error("Please Init DodoOfficeConverter First, Then Start Office Service!");
            return;
        }
        if (isServiceStarted) {
            LOGGER.info("The Office Service has been correctly started! Do nothing...");
            return;
        }
        try {
            try {
                LOGGER.info("Try to connect to an office service that already exists ...");
                ExternalOfficeManagerConfiguration externalProcessOfficeManager = new ExternalOfficeManagerConfiguration();
                externalProcessOfficeManager.setHost(getHost1());
                if (CONNECTION_PROTOCOL != null) {
                    externalProcessOfficeManager.setConnectionProtocol(CONNECTION_PROTOCOL);
                }
                externalProcessOfficeManager.setConnectOnStart(true);
                externalProcessOfficeManager.setPortNumber(getPort()[0]);
                OfficeManager officeManager = externalProcessOfficeManager.buildOfficeManager();
                officeManager.start();
                LOGGER.info("office service connected!");
            } catch (Exception ex) {
                ex.printStackTrace();
                LOGGER.info("Office service does not exist, try to start...");
            }

            DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();
            LOGGER.info("Starting Office Service....");
            configuration.setOfficeHome(getOfficeHome());
            configuration.setPortNumbers(getPort());
            configuration.setTaskExecutionTimeout(1000 * 60 * 5L);
            configuration.setTaskQueueTimeout(1000 * 60 * 60 * 24L);
            if (CONNECTION_PROTOCOL != null) {
                configuration.setConnectionProtocol(CONNECTION_PROTOCOL);
            }
            if (OFFICE_PROFILE != null) {
                configuration.setTemplateProfileDir(OFFICE_PROFILE);
            }
            officeManager = configuration.buildOfficeManager();
            officeManager.start();
            LOGGER.info("Start Office Service... OK!");
            isServiceStarted = Boolean.TRUE;
        } catch (Exception ce) {
            LOGGER.error("Start Office Service... Fail! More Information:", ce);
            isServiceStarted = Boolean.FALSE;
        }
    }

    public static void stopService() {
        LOGGER.info("Closing Office Service ....");
        try {
            if (isServiceStarted && officeManager != null) {
                officeManager.stop();
            }
        } catch (Exception e) {
            LOGGER.error("Close Office Service... Fail! More Information:", e);
        } finally {
            isServiceStarted = Boolean.FALSE;
        }
        LOGGER.info("Office Service Closed!");
    }

    public static String getOfficeHome() {
        if (!isInit) {
            LOGGER.error("Please configure DodoOfficeConverter first!");
            return null;
        }
        return OFFICE_HOME;
    }

    public static String getHost1() {
        if (!isInit) {
            LOGGER.error("Please configure DodoOfficeConverter first!");
            return null;
        }
        return HOST;
    }

    public static int[] getPort() {
        if (!isInit) {
            LOGGER.error("Please configure DodoOfficeConverter first!");
            return null;
        }
        return PORT;
    }

    public static void setOfficeHome(String officeHome) {
        DodoOfficeConverter.OFFICE_HOME = Doc2swfUtil.appendFileSeparator(officeHome);
        LOGGER.info("DodoOfficeConverter:setOfficeHome：{}", DodoOfficeConverter.OFFICE_HOME);
    }

    public static void setPort(int[] port) {
        for (int p : port) {
            LOGGER.info("DodoOfficeConverter:setPort：{}", p);
        }
        DodoOfficeConverter.PORT = port;
    }

    public static void setHost(String host) {
        LOGGER.info("DodoOfficeConverter:setHost：{}", host);
        DodoOfficeConverter.HOST = host;
    }

    public static void setConnectionProtocol(OfficeConnectionProtocol protocol) {
        LOGGER.info("DodoOfficeConverter:setConnectionProtocol：{}", protocol);
        DodoOfficeConverter.CONNECTION_PROTOCOL = protocol;
    }

    public static void setTemplateProfileDir(File file) {
        LOGGER.info("DodoOfficeConverter:setTemplateProfileDir：{}", file.getPath());
        DodoOfficeConverter.OFFICE_PROFILE = file;
    }

    private static Map<String, ?> toJavaMap(JSONObject jsonMap) throws JSONException {
        String[] names = JSONObject.getNames(jsonMap);
        Map<String, Object> map = new HashMap<String, Object>(names.length);
        for (String key : names) {
            Object value = jsonMap.get(key);
            if (value instanceof JSONObject) {
                map.put(key, toJavaMap((JSONObject) value));
            } else {
                map.put(key, value);
            }
        }
        return map;
    }

    public static List<DocumentFormat> getDocumentFormats() {
        return documentFormats;
    }

    public static StringBuilder getAllSupportSb() {
        return allSupportSb;
    }
}
