package com.dodo.utils.file;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dodo.common.doc2swf.pdf.XXTEA;
import com.dodo.common.framework.bean.file.DodoFile;
import com.dodo.common.framework.bean.file.DodoVideoFile;
import com.dodo.utils.CommonUtil;
import com.dodo.utils.JacksonUtil;
import com.dodo.utils.config.DodoFrameworkConfigUtil.DodoShowDocUtil;
import com.dodo.utils.config.DodoFrameworkConfigUtil.DodoUploaderUtil;
import com.dodo.utils.file.icon.FileIconDetectorProxy;
import com.dodo.utils.file.icon.FileIconMimeTypeDetector;
import com.dodo.utils.file.icon.FileIconSuffixDetector;
import com.third.aliyun.oss.OSSService;

import eu.medsea.mimeutil.MimeUtil;

/**
 * FileUtils
 * 
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class FileUtils {
    public final static String           FILE_PROCESS_SUCCESS  = "SUCCESS";
    private static final Logger          LOGGER                = LoggerFactory.getLogger(FileUtils.class);
    private static FileIconDetectorProxy fileIconDetectorProxy = FileIconDetectorProxy.getInstance();
    static {
        fileIconDetectorProxy.registerDetector(new FileIconSuffixDetector());
        fileIconDetectorProxy.registerDetector(new FileIconMimeTypeDetector());
        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.ExtensionMimeDetector");
    }

    /**
     * 
     * @param s
     *            suffix Or mime type of the file
     * @return icon
     */
    public static String getFileIcon(String s) {
        return fileIconDetectorProxy.detector(s);
    }

    /**
     * 获取文件名 不包含扩展名
     */
    public static String getFilePrefix(String fileFullName) {
        int splitIndex = fileFullName.lastIndexOf(".");
        if (splitIndex == -1) {
            return fileFullName;
        }
        return fileFullName.substring(0, splitIndex);
    }

    /**
     * 获取文件名 不包含扩展名
     */
    public static String getFilePrefix(File file) {
        String fileFullName = file.getName();
        return getFilePrefix(fileFullName);
    }

    /**
     * 获取文件扩展名
     */
    public static String getFileSuffix(String fileFullName) {
        int splitIndex = fileFullName.lastIndexOf(".");
        if (splitIndex == -1) {
            return "";
        }
        return fileFullName.substring(splitIndex + 1);
    }

    /**
     * 获取文件扩展名
     */
    public static String getFileSuffix(File file) {
        String fileFullName = file.getName();
        return getFileSuffix(fileFullName);
    }

    /**
     * 文件路径添加分隔符
     */
    public static String appendFileSeparator(String path) {
        if (StringUtils.isBlank(path))
            return "";
        return path + (path.lastIndexOf(File.separator) == path.length() - 1 ? "" : File.separator);
    }

    public static byte[] encryptToBytes(File inFile, String key) throws UnsupportedEncodingException {
        return XXTEA.encrypt(FileUtils.getBytesFromFile(inFile), key.getBytes("UTF-8"));
    }

    public static File encryptToFile(File inFile, String fileId, String key) throws UnsupportedEncodingException {
        File outFile = new File(inFile.getParentFile().getParentFile() + File.separator + key + File.separator + fileId
                + File.separator + inFile.getName());
        if (outFile.exists() && outFile.isFile()) {
            return outFile;
        } else {
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
        }
        return FileUtils.getFileFromBytes(XXTEA.encrypt(FileUtils.getBytesFromFile(inFile), key.getBytes("UTF-8")),
                outFile);
    }

    /**
     * 文件转化为字节数组
     */
    public static byte[] getBytesFromFile(File f) {
        if (f == null) {
            return null;
        }
        try {
            FileInputStream stream = new FileInputStream(f);
            ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = stream.read(b)) != -1)
                out.write(b, 0, n);
            stream.close();
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
        }
        return null;
    }

    /**
     * 把字节数组保存为一个文件
     */
    public static File getFileFromBytes(byte[] b, File outputFile) {
        BufferedOutputStream stream = null;
        try {
            FileOutputStream fstream = new FileOutputStream(outputFile);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return outputFile;
    }

    /**
     * 从字节数组获取对象
     * 
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object getObjectFromBytes(byte[] objBytes) throws IOException, ClassNotFoundException {
        if (objBytes == null || objBytes.length == 0) {
            return null;
        }
        ByteArrayInputStream bi = new ByteArrayInputStream(objBytes);
        ObjectInputStream oi = new ObjectInputStream(bi);
        return oi.readObject();
    }

    /**
     * 从对象获取一个字节数组
     * 
     * @throws IOException
     */
    public static byte[] getBytesFromObject(Serializable obj) throws IOException {
        if (obj == null) {
            return null;
        }
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = new ObjectOutputStream(bo);
        oo.writeObject(obj);
        return bo.toByteArray();
    }

    /**
     * 获取文件编码格式(字符集)
     * 
     * @param file
     *            输入文件
     * @return 字符集名称，如果不支持的字符集则返回null
     */
    public static Charset getFileEncoding(File file) {
        /*------------------------------------------------------------------------
          detector是探测器，它把探测任务交给具体的探测实现类的实例完成。
          cpDetector内置了一些常用的探测实现类，这些探测实现类的实例可以通过add方法
          加进来，如ParsingDetector、 JChardetFacade、ASCIIDetector、UnicodeDetector。
          detector按照“谁最先返回非空的探测结果，就以该结果为准”的原则返回探测到的
          字符集编码。
           使用需要用到三个第三方JAR包：antlr.jar、chardet.jar和cpdetector.jar
           cpDetector是基于统计学原理的，不保证完全正确。
        --------------------------------------------------------------------------*/
        CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
        /*-------------------------------------------------------------------------
          ParsingDetector可用于检查HTML、XML等文件或字符流的编码,构造方法中的参数用于
          指示是否显示探测过程的详细信息，为false不显示。
        ---------------------------------------------------------------------------*/
        detector.add(new ParsingDetector(false));
        /*--------------------------------------------------------------------------
         JChardetFacade封装了由Mozilla组织提供的JChardet，它可以完成大多数文件的编码
         测定。所以，一般有了这个探测器就可满足大多数项目的要求，如果你还不放心，可以
         再多加几个探测器，比如下面的ASCIIDetector、UnicodeDetector等。
        ---------------------------------------------------------------------------*/
        detector.add(JChardetFacade.getInstance());// 用到antlr.jar、chardet.jar
        // ASCIIDetector用于ASCII编码测定
        detector.add(ASCIIDetector.getInstance());
        // UnicodeDetector用于Unicode家族编码的测定
        detector.add(UnicodeDetector.getInstance());
        Charset charset = null;
        try {
            charset = detector.detectCodepage(file.toURI().toURL());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return charset;
    }

    /**
     * 文件编码格式转换
     * 
     * @param inFile
     *            输入文件
     * @param inCharset
     *            输入文件字符集
     * @param outFile
     *            输出文件
     * @param outCharset
     *            输出文件字符集
     * @return 转码后的字符流
     * @throws IOException
     */
    public static byte[] convertFileEncoding(File inFile, Charset inCharset, File outFile, Charset outCharset)
            throws IOException {

        RandomAccessFile inRandom = new RandomAccessFile(inFile, "r");

        FileChannel inChannel = inRandom.getChannel();

        // 将输入文件的通道通过只读的权限 映射到内存中。
        MappedByteBuffer byteMapper = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, (int) inFile.length());

        CharsetDecoder inDecoder = inCharset.newDecoder();
        CharsetEncoder outEncoder = outCharset.newEncoder();

        CharBuffer cb = inDecoder.decode(byteMapper);
        ByteBuffer outBuffer = null;
        try {
            outBuffer = outEncoder.encode(cb);

            RandomAccessFile outRandom = null;
            FileChannel outChannel = null;
            if (outFile != null) {
                try {
                    outRandom = new RandomAccessFile(outFile, "rw");
                    outChannel = outRandom.getChannel();
                    outChannel.write(outBuffer);
                } finally {
                    if (outChannel != null) {
                        outChannel.close();
                    }
                    if (outRandom != null) {
                        outRandom.close();
                    }
                }
            }
        } finally {
            inChannel.close();
            inRandom.close();
        }

        return outBuffer.array();
    }

    /**
     * 文件编码格式转换
     * 
     * @param inFile
     *            输入文件
     * @param inCharset
     *            输入文件字符集
     * @param outCharset
     *            输出字符集
     * @return 转码后的字符流
     * @throws IOException
     *             Exception
     */
    public static byte[] convertFileEncoding(File inFile, Charset inCharset, Charset outCharset) throws IOException {
        return convertFileEncoding(inFile, inCharset, (File) null, outCharset);
    }

    /**
     * 将文件字符集转化为指定字符集
     * 
     * @param inFile
     *            输入文件
     * @param outFile
     *            输出文件
     * @param outCharset
     *            输出文件字符集
     * @return 转码后的字符流
     * @throws IOException
     */
    public static byte[] convertFileEncoding(File inFile, File outFile, Charset outCharset) throws IOException {
        return convertFileEncoding(inFile, getFileEncoding(inFile), outFile, outCharset);
    }

    /**
     * 将文件字符集转化为指定字符集
     * 
     * @param inFile
     *            输入文件
     * @return 转码后的字符流
     * @throws IOException
     */
    public static byte[] convertFileEncoding(File inFile, Charset outCharset) throws IOException {
        return convertFileEncoding(inFile, (File) null, outCharset);
    }

    /**
     * 将文件字符集转换为系统字符集
     * 
     * @param inFile
     *            输入文件
     * @return 转码后的字符流
     * @throws IOException
     */
    public static byte[] convertFileEncodingToSys(File inFile) throws IOException {
        return convertFileEncoding(inFile, (File) null, Charset.defaultCharset());
    }

    /**
     * 将文件字符集转换为系统字符集
     * 
     * @param inFile
     *            输入文件
     * @param outFile
     *            输出文件
     * @return 转码后的字符流
     * @throws IOException
     */
    public static byte[] convertFileEncodingToSys(File inFile, File outFile) throws IOException {
        return convertFileEncoding(inFile, outFile, Charset.defaultCharset());
    }

    /**
     * 将数据写入文件
     * 
     * @param inputStream
     * @param filePath
     * @return
     */
    public static File writeFile(InputStream inputStream, File file) {
        FileOutputStream out = null;
        FileChannel outChannel = null;

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (file.exists()) {
            file.delete();
        }

        try {

            out = new FileOutputStream(file);
            outChannel = out.getChannel();

            byte[] buffer = new byte[1024];

            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

            ByteBuffer outBuffer = ByteBuffer.allocate(buffer.length);
            int i = 0;
            while ((i = bufferedInputStream.read(buffer)) != -1) {
                outBuffer.put(buffer, 0, i);
                outBuffer.flip();
                outChannel.write(outBuffer);
                outBuffer.clear();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outChannel != null) {
                try {
                    outChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return file.exists() ? file : null;
    }

    public static File getUploadTempFile(String uploaderId, String fileName) {
        return new File(appendFileSeparator(DodoUploaderUtil.tempFileDir) + uploaderId + File.separator + fileName);
    }

    public static File getUploadTargetFile(String fileName) {
        return new File(appendFileSeparator(DodoUploaderUtil.targetFileDir)
                + CommonUtil.getSpecialDateStr(new java.sql.Date(System.currentTimeMillis()), "yyyyMM")
                + File.separator + fileName);
    }

    public static File getUploadTargetFile(String fileName, String parentDir) {
        return new File(appendFileSeparator(DodoUploaderUtil.targetFileDir) + parentDir + File.separator
                + CommonUtil.getSpecialDateStr(new java.sql.Date(System.currentTimeMillis()), "yyyyMM")
                + File.separator + fileName);
    }

    public static String getUploadTargetRootDir() {
        return new File(appendFileSeparator(DodoUploaderUtil.targetFileDir)).getAbsolutePath();
    }

    public static String getUploadTempRootDir() {
        return new File(appendFileSeparator(DodoUploaderUtil.tempFileDir)).getAbsolutePath();
    }

    /**
     * 存储文件
     * 
     * @param inputStream
     *            上传文件
     * @param uploaderId
     *            上传器ID
     * @param fileName
     *            上传文件名
     * @param chunks
     *            总文件块数
     * @param chunk
     *            当前块大小
     * @param chunkSize
     *            接受的块大小
     * @param uploadFlag
     *            上传模式 1= append 2=once
     * @return 是否存存成功
     */
    public static boolean saveFile(InputStream inputStream, String uploaderId, String fileName, int chunks, int chunk,
            int chunkSize, int uploadFlag) {
        File dstFile = getUploadTempFile(uploaderId, fileName);
        // 分块上传
        if (uploadFlag == 1) {
            if (chunkSize > 0) {
                File file = null;
                // 文件已存在删除旧文件（上传了同名的文件）
                if (chunk == 0 && dstFile.exists()) {
                    dstFile.delete();
                } else if (!(file = dstFile.getParentFile()).exists()) {
                    file.mkdirs();
                }
                appendChunk(inputStream, dstFile, chunkSize);
            } else {
                return false;
            }
        } else {// 一次上传
            writeFile(inputStream, dstFile);

            String filePrefix = null;
            String fileSuffix = null;
            if (fileName.lastIndexOf(".") != -1) {
                fileSuffix = FileUtils.getFileSuffix(fileName);
            }

            StringBuilder sBuilder = new StringBuilder();
            sBuilder.append(StringUtils.substringBeforeLast(FileUtils.getFilePrefix(fileName), "_"));

            filePrefix = sBuilder.toString();
            if (StringUtils.isNotBlank(fileSuffix)) {
                sBuilder.append(".").append(fileSuffix).toString();
            }
            String finalFileName = sBuilder.toString();
            File finalFile = getUploadTempFile(uploaderId, finalFileName);

            // 最后一次上传 合并文件
            if (chunks == chunk + 1) {
                File f = null;
                for (int i = 0; i < chunks; i++) {
                    sBuilder.delete(0, sBuilder.length());
                    sBuilder.append(filePrefix).append("_").append(i);
                    if (StringUtils.isNotBlank(fileSuffix)) {
                        sBuilder.append(".").append(fileSuffix).toString();
                    }
                    f = getUploadTempFile(uploaderId, sBuilder.toString());
                    if (!f.exists()) {
                        continue;
                    }
                    try {
                        appendChunk(new FileInputStream(f), finalFile, chunkSize);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return true;
    }

    /**
     * 向文件内追加文件块
     * 
     * @param src
     *            欲追加文件块
     * @param dst
     *            追加到的目标文件
     * @param bufferSize
     *            块大小
     */
    public static void appendChunk(File src, File dst, int bufferSize) {
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(src), bufferSize);
            appendChunk(in, dst, bufferSize);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 向文件内追加文件块
     * 
     * @param in
     *            欲追加文件
     * @param dst
     *            追加到的目标文件
     * @param bufferSize
     *            块大小
     */
    public static void appendChunk(InputStream in, File dst, int bufferSize) {
        OutputStream out = null;
        try {
            if (dst.exists()) {
                out = new BufferedOutputStream(new FileOutputStream(dst, true), bufferSize); // plupload 配置了chunk的时候新上传的文件appand到文件末尾
            } else {
                dst.getParentFile().mkdirs();
                dst.createNewFile();
                out = new BufferedOutputStream(new FileOutputStream(dst), bufferSize);
            }

            byte[] buffer = new byte[bufferSize];
            int len = 0;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getMimeType(File file) {
        Collection<?> mimeTypes = MimeUtil.getMimeTypes(file);
        for (Object object : mimeTypes) {
            return object.toString();
        }
        return "";
    }

    /**
     * 将数据写入文件
     *
     * @param content
     * @param filePath
     * @return
     */
    public static File writeFile(byte[] content, String filePath) {
        FileOutputStream out = null;
        FileChannel outChannel = null;
        File file = new File(filePath);

        if (file.exists()) {
            file.delete();
        }

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        ByteBuffer outBuffer = ByteBuffer.allocate(content.length);
        outBuffer.put(content);
        outBuffer.flip();
        try {
            out = new FileOutputStream(file);

            outChannel = out.getChannel();

            outChannel.write(outBuffer);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outChannel != null) {
                try {
                    outChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return file.exists() ? file : null;
    }

    /**
     * 将字符串写入文件
     *
     * @param content
     * @param filePath
     * @return
     */
    public static File writeFile(String content, String filePath) {
        byte[] contentByte = new byte[0];
        if (StringUtils.isNotBlank(content)) {
            try {
                contentByte = content.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return writeFile(contentByte, filePath);
    }

    public static File writeFile(String content, File file) {
        byte[] contentByte = new byte[0];
        if (StringUtils.isNotBlank(content)) {
            try {
                contentByte = content.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return writeFile(contentByte, file.getAbsolutePath());
    }

    /**
     * 读取文件
     *
     * @param file
     * @return
     */
    public static String readFile(File file) throws IOException {
        LOGGER.info(file.getAbsolutePath());
        if (!file.exists() || file.isDirectory() || !file.canRead()) {
            return null;
        }
        FileInputStream fileInputStream = new FileInputStream(file);
        return readFile(fileInputStream);
    }

    //    public static String readFile(File file) throws IOException {
    //        if (!file.exists() || file.isDirectory() || !file.canRead()) {
    //            return null;
    //        }
    //        StringBuilder sbBuilder = new StringBuilder();
    //        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
    //        String string = "";
    //        while ((string = bufferedReader.readLine())!=null) {
    //        	sbBuilder.append(string);
    //		}
    //        bufferedReader.close();
    //        return sbBuilder.toString();
    //    }

    /**
     * 读取文件
     *
     * @param fileInputStream
     * @return
     */
    public static String readFile(FileInputStream fileInputStream) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        StringBuffer contentBuffer = new StringBuffer();
        Charset charset = null;
        CharsetDecoder decoder = null;
        CharBuffer charBuffer = null;
        try {
            FileChannel channel = fileInputStream.getChannel();
            while (true) {
                buffer.clear();
                int pos = channel.read(buffer);
                if (pos == -1) {
                    break;
                }
                buffer.flip();
                try {
                    charset = Charset.forName("UTF-8");
                    decoder = charset.newDecoder();
                    charBuffer = decoder.decode(buffer);
                } catch (Exception e) {
                    try {
                        charset = Charset.forName("gbk");
                        decoder = charset.newDecoder();
                        charBuffer = decoder.decode(buffer);
                    } catch (Exception e1) {
                        try {
                            charset = Charset.forName("gb2312");
                            decoder = charset.newDecoder();
                            charBuffer = decoder.decode(buffer);
                        } catch (Exception e2) {
                            charset = Charset.forName("ISO-8859-1");
                            decoder = charset.newDecoder();
                            charBuffer = decoder.decode(buffer);
                        }
                    }
                }
                contentBuffer.append(charBuffer.toString());
            }
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return contentBuffer.toString();
    }

    /**
     * String 返回 <code>FILE_PROCESS_SUCCESS</code> 表示成功 <br/>
     * 非 <code>FILE_PROCESS_SUCCESS</code> 表示 文件后缀错误 此时 返回值保存错误的文件名<br/>
     * 
     * @throws IOException
     * @see FileUtils#FILE_PROCESS_SUCCESS
     * 
     * */
    public static String getUploadFiles(String uploaderName, List<DodoFile> fileFieldList, HttpServletRequest request,
            String allowFileTypes, String fileAttrJson, int fileFlag, String ossBucket) throws IOException {
        List<String> allowFileTypesList = null;
        if (StringUtils.isNotBlank(allowFileTypes) && (!"*".equals(allowFileTypes))) {
            allowFileTypesList = Arrays.asList(allowFileTypes.split(","));
        }
        List<?> fileAttr = null;
        if (StringUtils.isNotBlank(fileAttrJson)) {
            fileAttr = JacksonUtil.toObject(fileAttrJson, List.class);
        }
        String fileFieldUploaderId = request.getParameter(uploaderName);
        String[] fileFieldFileIds = null;
        if (fileFieldUploaderId != null) {
            fileFieldFileIds = request.getParameterValues(fileFieldUploaderId);
        }
        if (fileFieldFileIds != null) {
            int fileFieldSortSeq = 1;
            String fileFieldFileName = null;
            String fileFieldFilePureSize = null;
            String fileFieldFileFormatSize = null;
            String fileFieldFileSuffix = null;
            int fileFieldFileWidth = -1;
            int fileFieldFileHeight = -1;
            for (String fileFieldFileId : fileFieldFileIds) {
                fileFieldFileName = request.getParameter(fileFieldFileId + "-name");
                fileFieldFilePureSize = request.getParameter(fileFieldFileId + "-puresize");
                fileFieldFileFormatSize = request.getParameter(fileFieldFileId + "-formatsize");
                fileFieldFileSuffix = getFileSuffix(fileFieldFileName).toLowerCase();
                fileFieldFileWidth = Integer.parseInt(request.getParameter(fileFieldFileId + "-width"));
                fileFieldFileHeight = Integer.parseInt(request.getParameter(fileFieldFileId + "-height"));
                if (allowFileTypesList != null
                        && (StringUtils.isBlank(fileFieldFileSuffix) || (!allowFileTypesList
                                .contains(fileFieldFileSuffix)))) {
                    return fileFieldFileName;
                }
                String fileFieldUploadFileName = fileFieldFileId + "." + fileFieldFileSuffix;
                File fileFieldOldFile = getUploadTempFile(fileFieldUploaderId, fileFieldUploadFileName);
                File fileFieldNewFile = null;

                DodoFile dodoFile = new DodoFile();
                dodoFile.setFileExt(fileFieldFileSuffix);
                dodoFile.setSortSeq(fileFieldSortSeq++ + "");
                dodoFile.setFileId(fileFieldFileId);
                dodoFile.setFileName(fileFieldFileName);
                dodoFile.setPureSize(fileFieldFilePureSize);
                dodoFile.setFormatSize(fileFieldFileFormatSize);
                dodoFile.setMimeType(getMimeType(fileFieldOldFile));
                dodoFile.setPageCount("0");
                dodoFile.setIsComplete("0");
                dodoFile.setIsSplitPage(String.valueOf(DodoShowDocUtil.isSplitPage));
                dodoFile.setFileIcon(getFileIcon(fileFieldFileSuffix));
                dodoFile.setWidth(fileFieldFileWidth);
                dodoFile.setHeight(fileFieldFileHeight);
                if (fileAttr != null) {
                    for (Object key : fileAttr) {
                        dodoFile.addAttr((String) key,
                                CommonUtil.escapeHtml(request.getParameter(fileFieldFileId + "-key-" + key)).trim());
                    }
                }

                if (fileFlag == 1) {
                    fileFieldNewFile = getUploadTargetFile(fileFieldFileId + File.separator + fileFieldUploadFileName,
                            "excel_import");
                } else if (fileFlag == 2) {
                    fileFieldNewFile = getUploadTargetFile(fileFieldFileId + File.separator + fileFieldUploadFileName,
                            "excel_update");
                } else {
                    fileFieldNewFile = getUploadTargetFile(fileFieldFileId + File.separator + fileFieldUploadFileName);
                }

                dodoFile.setFilePath(fileFieldNewFile.getAbsolutePath().replace(getUploadTargetRootDir(), ""));
                dodoFile.setHttpPath(dodoFile.getFilePath().replaceAll("\\\\", "/"));

                if (fileFieldOldFile.exists()) {
                    if (StringUtils.isNotBlank(ossBucket)) {
                        OSSService.upload(fileFieldOldFile, dodoFile.getHttpPath().substring(1), ossBucket);
                        dodoFile.setHttpPath(OSSService.getDomain(ossBucket) + dodoFile.getHttpPath());
                        dodoFile.setFilePath(fileFieldOldFile.getAbsolutePath().replace(getUploadTempRootDir(), ""));
                    } else {
                        org.apache.commons.io.FileUtils.copyFile(fileFieldOldFile, fileFieldNewFile);
                    }
                } else {
                    int i = 0;
                    File currChunkFile = getUploadTempFile(fileFieldUploaderId, fileFieldFileId + "_currChunk");
                    if (currChunkFile.exists()) {
                        i = Integer.parseInt(readFile(currChunkFile));
                    }
                    for (;; i++) {
                        fileFieldOldFile = getUploadTempFile(fileFieldUploaderId, fileFieldFileId + "_" + i + "."
                                + fileFieldFileSuffix);
                        if (!fileFieldOldFile.exists()) {
                            break;
                        }
                        appendChunk(fileFieldOldFile, fileFieldNewFile, Long.valueOf(fileFieldOldFile.length())
                                .intValue());
                    }
                }

                fileFieldList.add(dodoFile);
            }
            // 如果假设临时文件服务器用于 上传预览，则这里不能删除临时文件夹，这时候需要在主机上定期清理
            // 否则，这里可以直接删除临时文件夹 以释放磁盘空间
            //org.apache.commons.io.FileUtils.deleteDirectory(FileUtils.getUploadTempFile(fileFieldUploaderId,""));
        }
        return FILE_PROCESS_SUCCESS;
    }

    public static String getUploadFiles(String uploaderName, List<DodoFile> fileFieldList, HttpServletRequest request,
            String allowFileTypes, String fileAttrJson, String ossBucket) throws IOException {
        return getUploadFiles(uploaderName, fileFieldList, request, allowFileTypes, fileAttrJson, 0, ossBucket);
    }

    /**
     * String 返回 <code>FILE_PROCESS_SUCCESS</code> 表示成功 <br/>
     * 非 <code>FILE_PROCESS_SUCCESS</code> 表示 文件后缀错误 此时 返回值保存错误的文件名<br/>
     * 
     * @throws IOException
     * @see FileUtils#FILE_PROCESS_SUCCESS
     * 
     * */
    public static String getUploadVideos(String uploaderName, List<DodoVideoFile> videoFieldList,
            HttpServletRequest request, String allowFileTypes, String fileAttrJson) throws IOException {
        List<String> allowFileTypesList = null;
        if (StringUtils.isNotBlank(allowFileTypes) && (!"*".equals(allowFileTypes))) {
            allowFileTypesList = Arrays.asList(allowFileTypes.split(","));
        }
        List<?> fileAttr = null;
        if (StringUtils.isNotBlank(fileAttrJson)) {
            fileAttr = JacksonUtil.toObject(fileAttrJson, List.class);
        }
        String videoFieldUploaderId = request.getParameter(uploaderName);
        String[] videoFieldFileIds = null;
        if (videoFieldUploaderId != null) {
            videoFieldFileIds = request.getParameterValues(videoFieldUploaderId);
        }
        if (videoFieldFileIds != null) {
            int videoFieldSortSeq = 1;
            String videoFieldFileName = null;
            String videoFieldFilePureSize = null;
            String videoFieldFileFormatSize = null;
            String[] videoFieldFileAd = null;
            String videoFieldFileColor = null;
            String videoFieldFileHandfee = null;
            String videoFieldFileSuffix = null;
            for (String videoFieldFileId : videoFieldFileIds) {
                videoFieldFileName = request.getParameter(videoFieldFileId + "-name");
                videoFieldFilePureSize = request.getParameter(videoFieldFileId + "-puresize");
                videoFieldFileFormatSize = request.getParameter(videoFieldFileId + "-formatsize");
                videoFieldFileAd = request.getParameterValues(videoFieldFileId + "-ad");
                videoFieldFileColor = request.getParameter(videoFieldFileId + "-color");
                videoFieldFileHandfee = request.getParameter(videoFieldFileId + "-handfee");
                videoFieldFileSuffix = FileUtils.getFileSuffix(videoFieldFileName).toLowerCase();
                if (allowFileTypesList != null
                        && (StringUtils.isBlank(videoFieldFileSuffix) || (!allowFileTypesList
                                .contains(videoFieldFileSuffix)))) {
                    return videoFieldFileName;
                }

                String videoFieldUploadFileName = videoFieldFileId + "." + videoFieldFileSuffix;
                File videoFieldOldFile = FileUtils.getUploadTempFile(videoFieldUploaderId, videoFieldUploadFileName);
                File videoFieldNewFile = FileUtils.getUploadTargetFile(videoFieldFileId + File.separator
                        + videoFieldUploadFileName);

                if (videoFieldOldFile.exists()) {
                    org.apache.commons.io.FileUtils.copyFile(videoFieldOldFile, videoFieldNewFile);
                } else {
                    int i = 0;
                    File currChunkFile = getUploadTempFile(videoFieldUploaderId, videoFieldFileId + "_currChunk");
                    if (currChunkFile.exists()) {
                        i = Integer.parseInt(readFile(currChunkFile));
                    }
                    for (;; i++) {
                        videoFieldOldFile = getUploadTempFile(videoFieldUploaderId, videoFieldFileId + "_" + i + "."
                                + videoFieldFileSuffix);
                        if (!videoFieldOldFile.exists()) {
                            break;
                        }
                        appendChunk(videoFieldOldFile, videoFieldNewFile, Long.valueOf(videoFieldOldFile.length())
                                .intValue());
                    }
                }

                DodoVideoFile videoFile = new DodoVideoFile();
                videoFile.setFileExt(videoFieldFileSuffix);
                videoFile.setSortSeq(videoFieldSortSeq++ + "");
                videoFile.setFileId(videoFieldFileId);
                videoFile.setFileName(videoFieldFileName);
                videoFile.setPureSize(videoFieldFilePureSize);
                videoFile.setFormatSize(videoFieldFileFormatSize);
                videoFile.setFilePath(videoFieldNewFile.getAbsolutePath().replace(FileUtils.getUploadTargetRootDir(),
                        ""));
                videoFile.setMimeType(FileUtils.getMimeType(videoFieldNewFile));
                videoFile.setIsComplete("0");
                videoFile.setFileIcon(FileUtils.getFileIcon(videoFieldFileSuffix));
                videoFile.setAd(videoFieldFileAd);
                try {
                    Integer tempInteger = Integer.parseInt(videoFieldFileHandfee);
                    videoFile.setHandFeeTime(tempInteger.toString());
                } catch (Exception e) {
                    videoFile.setHandFeeTime("-1");
                }
                videoFile.setVideoColor(videoFieldFileColor);
                videoFile.setHttpPath(videoFile.getFilePath().replaceAll("\\\\", "/"));
                if (fileAttr != null) {
                    for (Object key : fileAttr) {
                        videoFile.addAttr((String) key,
                                CommonUtil.escapeHtml(request.getParameter(videoFieldFileId + "-key-" + key)).trim());
                    }
                }
                videoFieldList.add(videoFile);
            }
            org.apache.commons.io.FileUtils.deleteDirectory(FileUtils.getUploadTempFile(videoFieldUploaderId, ""));
        }
        return FILE_PROCESS_SUCCESS;
    }

    public static String getFileFormatSize(long length) {
        String result = null;
        int sub_string = 0;
        if (length >= 1073741824) {
            sub_string = String.valueOf((float) length / 1073741824).indexOf(".");
            result = ((float) length / 1073741824 + "000").substring(0, sub_string + 3) + " GB";
        } else if (length >= 1048576) {
            sub_string = String.valueOf((float) length / 1048576).indexOf(".");
            result = ((float) length / 1048576 + "000").substring(0, sub_string + 3) + " MB";
        } else if (length >= 1024) {
            sub_string = String.valueOf((float) length / 1024).indexOf(".");
            result = ((float) length / 1024 + "000").substring(0, sub_string + 3) + " KB";
        } else if (length < 1024)
            result = Long.toString(length) + " B";

        return result;
    }
}
