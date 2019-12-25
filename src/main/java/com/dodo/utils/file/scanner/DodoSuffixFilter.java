package com.dodo.utils.file.scanner;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dodo.utils.file.FileUtils;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class DodoSuffixFilter implements DodoScannerFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(DodoSuffixFilter.class);

    public DodoSuffixFilter(String[] fileSuffixs) {
        super();
        this.fileSuffixs = fileSuffixs;
        int i = 0;
        if (fileSuffixs != null) {
            for (String suffix : fileSuffixs) {
                this.fileSuffixs[i++] = suffix.toUpperCase().trim();
            }
        }
    }

    private String[]  fileSuffixs = null;
    private List<URL> result      = new ArrayList<URL>();

    public boolean filter(String urlStr, URL url, ClassLoader classLoader) {
        if (fileSuffixs == null || fileSuffixs.length == 0) {
            LOGGER.error("{}......Pass!", url);
            LOGGER.error("FileSuffixs Is Null or FileSuffixs's Length Is 0,Please Chcek!");
            return false;
        }

        if (ArrayUtils.contains(fileSuffixs, FileUtils.getFileSuffix(url.getPath()).toUpperCase())) {
            result.add(url);
            LOGGER.info("......Got!", url.getPath());
            return true;
        }
        return false;
    }

    public List<URL> getResult() {
        return result;
    }
}
