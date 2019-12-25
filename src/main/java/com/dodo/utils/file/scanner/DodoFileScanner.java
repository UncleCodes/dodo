package com.dodo.utils.file.scanner;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
public class DodoFileScanner {
    private static final Logger LOGGER           = LoggerFactory.getLogger(DodoFileScanner.class);
    private boolean             includeOrExclude = true;
    private DodoScannerFilter   scannerFilter    = null;
    private List<URL>           fileList         = new ArrayList<URL>();

    public DodoFileScanner() {
    }

    public DodoFileScanner(Boolean includeOrExclude, DodoScannerFilter filter) {
        this.includeOrExclude = includeOrExclude;
        this.scannerFilter = filter;
    }

    public DodoFileScanner(DodoScannerFilter filter) {
        this.scannerFilter = filter;
    }

    public List<URL> find(ClassLoader classLoader, String basePackage, File pathFile, boolean isFindChild) {
        File findPathFile = pathFile;
        if (findPathFile == null) {
            findPathFile = new File(classLoader.getResource("").getFile());
        }
        try {
            URL findPathUrl = findPathFile.toURI().toURL();
            if (findPathFile.isDirectory()) {
                scanFileFromDir(basePackage, URLDecoder.decode(findPathUrl.getFile(), "UTF-8"), isFindChild,
                        classLoader);
            } else if (findPathFile.isFile() && findPathFile.getName().endsWith(".jar")) {
                scanFileFromJar(basePackage, new URL("jar:" + findPathUrl.toString() + "!/"), isFindChild, classLoader);
            } else {
                LOGGER.info("File Type Error!,Need 'Directory' or 'Jar File'.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileList;
    }

    private void scanFileFromJar(String basePackage, URL url, final boolean isFindChild, ClassLoader classLoader) {
        String package2Path = basePackage.replace('.', '/');
        JarFile jar;
        try {
            jar = ((JarURLConnection) url.openConnection()).getJarFile();
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (!name.startsWith(package2Path) || entry.isDirectory()) {
                    continue;
                }

                if (!isFindChild && (name.endsWith("/") || name.lastIndexOf('/') != package2Path.length())) {
                    continue;
                }

                URL fileUrl = new URL(url + name.substring(name.lastIndexOf("/")));

                boolean filter = scannerFilter.filter(name, fileUrl, classLoader);
                if (includeOrExclude ? filter : !filter) {
                    this.fileList.add(fileUrl);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void scanFileFromDir(final String packageName, String packagePath, boolean isFindChild,
            final ClassLoader classLoader) {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        final boolean fileIsFindChild = isFindChild;
        File[] dirfiles = dir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return fileIsFindChild;
                }
                boolean filter = false;
                try {
                    if (file.getName().endsWith(".jar")) {
                        return true;
                    } else {
                        filter = scannerFilter.filter(packageName + "." + file.getName(), file.toURI().toURL(),
                                classLoader);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return includeOrExclude ? filter : !filter;
            }
        });
        for (File file : dirfiles) {
            if (file.isDirectory()) {
                scanFileFromDir(packageName + "." + file.getName(), file.getAbsolutePath(), isFindChild, classLoader);
            } else if (file.getName().endsWith(".jar")) {
                try {
                    scanFileFromJar(packageName, new URL("jar:" + file.toURI().toURL() + "!/"), isFindChild,
                            classLoader);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    this.fileList.add(file.toURI().toURL());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public boolean isCheckInOrEx() {
        return includeOrExclude;
    }

    public void setCheckInOrEx(boolean pCheckInOrEx) {
        includeOrExclude = pCheckInOrEx;
    }

    public DodoScannerFilter getScannerFilter() {
        return scannerFilter;
    }

    public void setScannerFilter(DodoScannerFilter scannerFilter) {
        this.scannerFilter = scannerFilter;
    }
}
