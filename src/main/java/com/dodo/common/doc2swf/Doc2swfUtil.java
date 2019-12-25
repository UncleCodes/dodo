package com.dodo.common.doc2swf;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class Doc2swfUtil {
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

    public static String getFilePrefix(String fileFullName) {
        int splitIndex = fileFullName.lastIndexOf(".");
        if (splitIndex == -1) {
            return fileFullName;
        }
        return fileFullName.substring(0, splitIndex);
    }

    public static byte[] convertFileEncodingToSys(File inFile) throws IOException {
        return convertFileEncoding(inFile, (File) null, Charset.defaultCharset());
    }

    public static byte[] convertFileEncoding(File inFile, File outFile, Charset outCharset) throws IOException {
        return convertFileEncoding(inFile, getFileEncoding(inFile), outFile, outCharset);
    }

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

    public static byte[] convertFileEncodingToSys(File inFile, File outFile) throws IOException {
        return convertFileEncoding(inFile, outFile, Charset.defaultCharset());
    }

    public static String appendFileSeparator(String path) {
        if (StringUtils.isBlank(path))
            return "";
        return path + (path.lastIndexOf(File.separator) == path.length() - 1 ? "" : File.separator);
    }

    public static String getFilePrefix(File file) {
        String fileFullName = file.getName();
        return getFilePrefix(fileFullName);
    }

    public static File writeFile(InputStream inputStream, File file) {
        FileOutputStream out = null;
        FileChannel outChannel = null;

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
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
}
