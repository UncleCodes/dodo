package com.dodo.common.doc2swf.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dodo.common.doc2swf.Doc2swfUtil;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfEncryptor;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class PDFSecurer {
    protected static final Logger LOGGER = LoggerFactory.getLogger(PDFSecurer.class);

    /**
     * 将输入的pdf进行水印处理，并且加密后生成新的pdf文件
     * 
     * @param pdfFileName
     *            原始的pdf文件名，全路径
     * @param subject
     *            报告的标题，会加入到pdf的属性内
     * @param author
     *            作者，会加入到pdf的属性内
     * @param watermark
     *            水印，会加入到页面中间底部
     * @param pager
     *            页底下面的版权说明文字
     * @return 加密后的pdf文件
     */
    public static File securePDF(String pdfFileName, String subject, String author, String watermark, String pager,
            String password) {
        PdfReader reader = null;
        PdfStamper stamp = null;
        try {
            // 取消后缀后的名称
            LOGGER.info("输入的文件名是：{}", pdfFileName);
            File f = new File(pdfFileName);
            if (f.exists()) {
                String baseName = Doc2swfUtil.getFilePrefix(pdfFileName);
                String pagerFileName = baseName + "_watermark.pdf"; // 加了水印后的pdf的文件名
                // we create a reader for a certain document
                reader = new PdfReader(pdfFileName);
                if (reader.isEncrypted()) {
                    LOGGER.info("encrypted pdf! 准备另存");
                    pdfFileName = baseName + "_decrypted.pdf";
                    File decryptPdf = new File(pdfFileName);
                    if (!decryptPdf.exists()) {
                        decrypt(reader, decryptPdf.getPath());
                        LOGGER.info("PDF解密完成");
                    }
                    reader.close();
                    reader = new PdfReader(pdfFileName);
                } else {
                    LOGGER.info("---文档未加密---");
                }
                int n = reader.getNumberOfPages();
                // we create a stamper that will copy the document to a new file
                FileOutputStream fileOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(pagerFileName);
                    stamp = new PdfStamper(reader, fileOutputStream);
                } catch (Exception e) {
                    e.printStackTrace();
                    LOGGER.error("Error..", e);
                }
                // adding some metadata
                Map<String, String> moreInfo = new HashMap<String, String>();
                moreInfo.put("Title", subject);
                moreInfo.put("Author", author);
                stamp.setMoreInfo(moreInfo);
                // adding content to each page
                int i = 0;
                PdfContentByte under;
                PdfContentByte over;
                BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
                while (i < n) {
                    i++;
                    over = stamp.getOverContent(i);
                    over.beginText();
                    over.setColorFill(BaseColor.LIGHT_GRAY);
                    over.setTextMatrix(30, 30);
                    over.setFontAndSize(bf, 8);
                    over.showText(pager + " 第" + i + "页");
                    over.endText();

                    // watermark under the existing page
                    under = stamp.getUnderContent(i);
                    // under.addImage(img);
                    // text over the existing page
                    under.beginText();
                    under.setColorFill(BaseColor.LIGHT_GRAY);
                    under.setTextMatrix(30, 30);
                    under.setFontAndSize(bf, 41);
                    under.showTextAligned(Element.ALIGN_LEFT, watermark, 130, 430, 45);
                    under.endText();
                }
                stamp.close();
                fileOutputStream.flush();
                fileOutputStream.close();
                reader.close();
                File encryptedFile = new File(baseName + "_encrypted.pdf");
                if (password != null) {
                    reader = new PdfReader(pagerFileName);
                    encrypt(reader, encryptedFile, password);
                    LOGGER.info("加密完成：{}", encryptedFile.getPath());
                    return encryptedFile;
                } else {
                    LOGGER.warn("Password is null,So not encrypted!");
                }
            } else {
                LOGGER.error("输入的文件不存在：{}", pdfFileName);
            }

        } catch (Exception de) {
            LOGGER.error(de.getMessage());
            de.printStackTrace();
        } finally {
            if (stamp != null) {
                try {
                    stamp.close();
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                reader.close();
            }
        }

        return null;
    }

    public static File encrypt(PdfReader reader, File out, String password) throws IOException, DocumentException {
        return encrypt(reader, out.getPath(), password);
    }

    public static File encrypt(PdfReader reader, String out, String password) throws IOException, DocumentException {
        FileOutputStream outputStream = new FileOutputStream(out);
        PdfEncryptor.encrypt(reader, outputStream, null, password.getBytes(), 0, false);
        outputStream.flush();
        outputStream.close();
        return new File(out);
    }

    public static File decrypt(File file) throws IOException, DocumentException {
        PdfReader reader = null;
        try {
            reader = new PdfReader(file.getPath());
            // if pdf is a encrypted file unencrypted
            if (reader.isEncrypted()) {
                return decrypt(reader, Doc2swfUtil.getFilePrefix(file.getPath()) + "_decrypted.pdf");
            } else {
                return file;
            }
        } finally {
            reader.close();
        }
    }

    public static File decrypt(PdfReader reader, String out) throws IOException, DocumentException {
        PdfStamper stamper = null;
        try {
            // if pdf is a encrypted file unencrypted
            File decryptedFile = new File(out);
            if (reader.isEncrypted()) {
                stamper = new PdfStamper(reader, new FileOutputStream(decryptedFile));
                return decryptedFile;
            } else {
                return decryptedFile;
            }
        } finally {
            stamper.close();
        }
    }

}
