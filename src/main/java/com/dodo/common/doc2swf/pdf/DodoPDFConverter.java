package com.dodo.common.doc2swf.pdf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dodo.common.doc2swf.Doc2swfUtil;
import com.dodo.utils.CommonUtil;
import com.dodo.utils.config.DodoFrameworkConfigUtil.DodoShowDocUtil;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class DodoPDFConverter {
    private static final Logger LOGGER = LoggerFactory.getLogger(DodoPDFConverter.class);

    private enum pdf2swfErrorType {
        COMMON, INVALID_CHARID, COMPLEX
    }

    private static String    COMMAND                     = "";
    private static Integer   SINGLE_PAGE_MODE_MAX_THREAD = 5;
    private static String    COMMON_MSG                  = "NOTICE  processing PDF page";
    private static String    INVALID_CHARID_MSG          = "ERROR   Invalid charid";
    private static String    COMPLEX_MSG                 = "ERROR   This file is too complex";
    private int              pageCount;
    private String           outPutDir;
    private String           execCommand;
    private File             pdfFile;
    private static boolean   splitPage;
    private boolean          poly2bitmap;
    private List<String>     pdf2swfOutPut;
    private int              errorPageNumber;
    private pdf2swfErrorType errorType                   = pdf2swfErrorType.COMMON;
    private static Boolean   isInit                      = Boolean.FALSE;

    public DodoPDFConverter(File pdfFile, boolean poly2bitmap, String outPutDir) {
        this.outPutDir = outPutDir;
        File file = new File(outPutDir);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        this.pdfFile = pdfFile;
        this.poly2bitmap = poly2bitmap;
        try {
            initPdf();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Boolean isReady() {
        return isInit;
    }

    public static void init() {
        if (isInit) {
            LOGGER.info("The PDFConverter has been correctly configured! Do nothing...");
            return;
        }
        BufferedReader bufferedReader = null;
        FileWriter fileWriter = null;
        try {
            String languagedir = CommonUtil.getWebRootPath() + "WEB-INF" + File.separator + "classes" + File.separator
            //					+ (File.separator.equals("\\") ? DodoPDFConverter.class
            //							.getPackage().getName().replaceAll("\\.", "\\\\")
            //							: DodoPDFConverter.class.getPackage().getName()
            //									.replaceAll("\\.", "/")) + File.separator
                    + "xpdf-chinese-simplified";

            File languaFile = new File(languagedir);
            String resourceDir = DodoPDFConverter.class.getClassLoader()
                    .getResource(DodoPDFConverter.class.getPackage().getName().replaceAll("\\.", "/")).getFile();
            if (resourceDir.contains("jar!")) {
                if (languaFile.exists()) {
                    FileUtils.deleteDirectory(languaFile);
                }
                languaFile = new File(languagedir);
                languaFile.mkdirs();
                resourceDir = URLDecoder.decode(resourceDir, "utf-8");
                String resourcePrefix = DodoPDFConverter.class.getPackage().getName().replaceAll("\\.", "/")
                        + "/xpdf-chinese-simplified/";
                JarFile jarRootFile = new JarFile(resourceDir.substring(0, resourceDir.lastIndexOf("!")).replace(
                        "file:", ""));
                Enumeration<JarEntry> jarEntries = jarRootFile.entries();
                JarEntry jarEntry = null;
                String jarEntryName = null;
                while (jarEntries.hasMoreElements()) {
                    jarEntry = jarEntries.nextElement();
                    jarEntryName = jarEntry.getName();
                    if (jarEntryName.startsWith(resourcePrefix)) {
                        if (jarEntry.isDirectory() && (!jarEntryName.equals(resourcePrefix))) {
                            new File(languagedir + File.separator + jarEntryName.replace(resourcePrefix, "")).mkdirs();
                        } else if (!jarEntry.isDirectory()) {
                            Doc2swfUtil.writeFile(jarRootFile.getInputStream(jarEntry), new File(languagedir
                                    + File.separator + jarEntryName.replace(resourcePrefix, "")));
                        }
                    }
                }
                jarRootFile.close();
            } else {
                languagedir = CommonUtil.getWebRootPath()
                        + "WEB-INF"
                        + File.separator
                        + "classes"
                        + File.separator
                        + (File.separator.equals("\\") ? DodoPDFConverter.class.getPackage().getName()
                                .replaceAll("\\.", "\\\\") : DodoPDFConverter.class.getPackage().getName()
                                .replaceAll("\\.", "/")) + File.separator + "xpdf-chinese-simplified";
            }
            // 文件路径没有空格
            if (!languagedir.contains(" ")) {
                DodoShowDocUtil.pdfCommand = DodoShowDocUtil.pdfCommand.replace("\"", "");
            }
            setCommand(DodoShowDocUtil.pdfCommand.replace("${languagedir}", languagedir));
            setSinglePageModeMaxThread(DodoShowDocUtil.pdfSinglePageMaxThread);
            setSplitPage(DodoShowDocUtil.isSplitPage);
            File charsetTempFile = new File(languagedir + File.separator + "add-to-xpdfrc.template");
            File charsetTargetFile = new File(languagedir + File.separator + "add-to-xpdfrc");
            if (charsetTargetFile.exists()) {
                charsetTargetFile.delete();
            }
            FileReader fileReader = new FileReader(charsetTempFile);
            fileWriter = new FileWriter(charsetTargetFile);
            bufferedReader = new BufferedReader(fileReader);
            String sTemp = null;
            while ((sTemp = bufferedReader.readLine()) != null) {
                if (File.separator.equals("\\")) {
                    sTemp = sTemp.replaceAll("/", "\\\\");
                }
                fileWriter.write(sTemp.replace("${languagedir}", languagedir));
                fileWriter.write("\n");
            }
            LOGGER.info("Configure PDFConverter ... OK!....");
            isInit = Boolean.TRUE;
        } catch (Exception e) {
            LOGGER.error("Configure PDFConverter ... Error! Cause:" + e);
            isInit = Boolean.FALSE;
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (fileWriter != null) {
                    fileWriter.flush();
                    fileWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setCommand(String command) {
        DodoPDFConverter.COMMAND = command;
        LOGGER.info("PDFConverter:setCommand :" + command);
    }

    public static boolean isSplitPage() {
        return splitPage;
    }

    public static void setSplitPage(boolean splitPage) {
        DodoPDFConverter.splitPage = splitPage;
        LOGGER.info("PDFConverter:setSplitPage :" + splitPage);
    }

    public static void setSinglePageModeMaxThread(Integer value) {
        DodoPDFConverter.SINGLE_PAGE_MODE_MAX_THREAD = value;
        LOGGER.info("PDFConverter:setSinglePageModeMaxThread :" + value);
    }

    private int execCommand(String command) throws InterruptedException, IOException {
        pdf2swfOutPut = new ArrayList<String>();
        LOGGER.info(command);
        final Process pdf2swfProcess = Runtime.getRuntime().exec(command);
        final InputStream inputStream = pdf2swfProcess.getInputStream();
        final InputStream errorStream = pdf2swfProcess.getErrorStream();
        Thread inputStreamThread = new Thread() {
            public void run() {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                try {
                    String line = null;

                    while ((line = br.readLine()) != null) {
                        pdf2swfOutPut.add(line);
                        LOGGER.info(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    LOGGER.error(e.getCause().getClass().getName());
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        Thread errorStreamThread = new Thread() {
            public void run() {
                BufferedReader br = new BufferedReader(new InputStreamReader(errorStream));
                try {

                    String line = null;
                    while ((line = br.readLine()) != null) {
                        LOGGER.info(line);
                        pdf2swfOutPut.add(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    LOGGER.error(e.getCause().getClass().getName());
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        inputStreamThread.start();
        errorStreamThread.start();
        pdf2swfProcess.waitFor();
        while (inputStreamThread.isAlive() || errorStreamThread.isAlive()) {
            LOGGER.info(" WaitFor Thread....");
            Thread.sleep(1000);
        }

        int exitValue = pdf2swfProcess.exitValue();
        if (pdf2swfProcess != null) {
            pdf2swfProcess.destroy();
        }
        return exitValue;
    }

    private void initPdf() throws Exception, DocumentException {
        if (!pdfFile.isFile() || !pdfFile.exists() || pdfFile.getName().lastIndexOf(".pdf") == -1) {
            throw new Exception("PDF File Doesn't Exists!");
        }
        String pdfPath = pdfFile.getPath();
        String pdfName = Doc2swfUtil.getFilePrefix(pdfFile.getName());

        File decryptedPdf = new File(Doc2swfUtil.getFilePrefix(pdfFile) + "_decrypted.pdf");
        File info = new File(Doc2swfUtil.appendFileSeparator(outPutDir) + pdfName + "_info");
        int pageCount = 0;
        if (!info.exists()) {
            PdfReader reader = new PdfReader(pdfPath);
            pageCount = reader.getNumberOfPages();
            if (pageCount != 0) {
                OutputStream out = null;
                try {
                    info.createNewFile();
                    out = new FileOutputStream(info);
                    out.write((pageCount + "").getBytes("UTF-8"));
                } finally {
                    if (out != null) {
                        out.flush();
                        out.close();
                    }
                }
            }

            if (!decryptedPdf.exists()) {
                if (reader.isEncrypted()) {
                    LOGGER.info("--- decrypt pdf! Save ---");
                    pdfPath = PDFSecurer.decrypt(reader, decryptedPdf.getAbsolutePath()).getPath();
                    LOGGER.info("decrypt --OK");
                } else {
                    LOGGER.info("--- Document not encryption ---");
                }
            } else {
                pdfPath = decryptedPdf.getPath();
            }
            reader.close();
        } else {
            if (decryptedPdf.exists()) {
                pdfPath = decryptedPdf.getPath();
            }
            FileInputStream in = new FileInputStream(info);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            try {
                pageCount = Integer.parseInt(reader.readLine());
            } catch (Exception e) {
            } finally {
                reader.close();
                in.close();
            }
        }
        this.pageCount = pageCount;
        this.execCommand = COMMAND.replace("${in}", pdfPath).replace(
                "${out}",
                Doc2swfUtil.appendFileSeparator(outPutDir)
                        + (splitPage ? pdfName + "_page%.swf" : pdfName + "_page.swf"))
                + (poly2bitmap ? " -s poly2bitmap -s multiply=4" : "");
    }

    private void makeErrorInfo() {
        errorPageNumber = 0;
        errorType = pdf2swfErrorType.COMMON;
        String s;
        int errorSize = pdf2swfOutPut.size();
        int i;
        // make Error Type
        for (i = errorSize - 1; i >= 0; i--) {
            s = pdf2swfOutPut.get(i);
            if (s.lastIndexOf(COMPLEX_MSG) != -1) {
                this.errorType = pdf2swfErrorType.COMPLEX;
                break;
            } else if (s.lastIndexOf(INVALID_CHARID_MSG) != -1) {
                this.errorType = pdf2swfErrorType.INVALID_CHARID;
                break;
            }
        }

        if (this.errorType != pdf2swfErrorType.COMMON) {
            i--;
            // make Error Number
            for (; i >= 0; i--) {
                s = pdf2swfOutPut.get(i);
                if (s.lastIndexOf(COMMON_MSG) != -1) {
                    s = s.replace(COMMON_MSG, "");
                    errorPageNumber = Integer.parseInt(s.substring(0, s.indexOf("(")).trim());
                }
            }
        }
    }

    private void convertAsOnePageMode(final int startPage, final Integer endPage) throws Exception {
        final List<Thread> threads = new LinkedList<Thread>();
        int covertCount = 0;
        int page = getSinglePageModeMaxThreadCount();
        int realEndPage = (endPage == null || endPage > pageCount ? pageCount : endPage);
        for (int i = startPage; i <= realEndPage; i++) {
            final String commandExec = this.execCommand + " -p " + i + " -s poly2bitmap";
            Thread t = new Thread() {
                public void run() {
                    try {
                        if (execCommand(commandExec) != 0) {
                            makeErrorInfo();
                            if (errorType != pdf2swfErrorType.COMMON) {
                                throw new Exception("Conversion failed:" + errorType + ";\n"
                                        + StringUtils.join(pdf2swfOutPut, "\n"));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        LOGGER.error(e.getCause().getClass().getName());
                    }
                }
            };
            threads.add(t);
            t.start();
            covertCount++;
            if (covertCount == page) {
                for (Thread thread : threads) {
                    thread.join();
                }
                threads.clear();
                covertCount = 0;
            }
        }
    }

    public void pdf2swf(int startPage, Integer endPage) throws Exception {
        if (!isInit) {
            LOGGER.error("Please configure DodoPDFConverter first!");
            return;
        }
        String command = this.execCommand;
        if (startPage != -1) {
            command = this.execCommand
                    + " -p "
                    + startPage
                    + (endPage != null && startPage == endPage ? "" : "-"
                            + (endPage == null || endPage > pageCount ? pageCount : endPage));
        }
        LOGGER.info("---pdf2swf begin ---");
        int pdf2swfExitValue = execCommand(command);
        if (pdf2swfExitValue != 0) {
            makeErrorInfo();
            if (errorType == pdf2swfErrorType.COMPLEX && !this.poly2bitmap) {
                this.poly2bitmap = true;
                LOGGER.info("---The First Time pdf2swf Fail，Try Again,Set:poly2bitmap = true ---");
                if (splitPage) {
                    new Thread() {
                        public void run() {
                            try {
                                pdf2swf(errorPageNumber, errorPageNumber);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                    convertAsOnePageMode(errorPageNumber + 1, null);
                } else {
                    pdf2swf();
                }
            } else {
                throw new Exception("Conversion failed:" + errorType + ";\n"
                        + StringUtils.join(this.pdf2swfOutPut, "\n"));
            }
        }
        LOGGER.info("---pdf2swf end ---");
    }

    public void pdf2swf() throws Exception {
        pdf2swf(-1, null);
    }

    private int getSinglePageModeMaxThreadCount() {
        if (SINGLE_PAGE_MODE_MAX_THREAD < 1) {
            return pageCount;
        } else {
            return SINGLE_PAGE_MODE_MAX_THREAD > pageCount ? pageCount : SINGLE_PAGE_MODE_MAX_THREAD;
        }
    }

}
