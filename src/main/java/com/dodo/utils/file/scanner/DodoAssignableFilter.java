package com.dodo.utils.file.scanner;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class DodoAssignableFilter implements DodoScannerFilter {
    private static final Logger LOGGER          = LoggerFactory.getLogger(DodoAssignableFilter.class);

    private List<Class<?>>      classList       = new ArrayList<Class<?>>();
    private Class<?>[]          assignableClass = null;

    public DodoAssignableFilter(Class<?>... assignableClass) {
        super();
        this.assignableClass = assignableClass;
    }

    public boolean filter(String urlStr, URL url, ClassLoader classLoader) {
        Assert.notNull(assignableClass, "assignableClass can't be null");
        Assert.notEmpty(assignableClass, "assignableClass can't be null");
        if (urlStr.startsWith(".")) {
            urlStr = urlStr.substring(1);
        }
        if (!urlStr.endsWith(".class")) {
            return false;
        }
        String classPathStr = urlStr.replace("/", ".");
        classPathStr = classPathStr.substring(0, classPathStr.length() - 6);
        try {
            Class<?> clazz = classLoader.loadClass(classPathStr);
            boolean isAdd = true;
            for (Class<?> cls : assignableClass) {
                if (!cls.isAssignableFrom(clazz)) {
                    isAdd = false;
                    break;
                }
            }
            if (isAdd) {
                classList.add(clazz);
                LOGGER.info("classPathStr={}..................Got!", classPathStr);
                return true;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Class<?>> getClassList() {
        return classList;
    }
}
