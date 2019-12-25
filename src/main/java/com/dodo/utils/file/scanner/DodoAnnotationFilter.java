package com.dodo.utils.file.scanner;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
public class DodoAnnotationFilter implements DodoScannerFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(DodoAnnotationFilter.class);

    public DodoAnnotationFilter(Class<? extends Annotation>[] annotationClass) {
        super();
        this.annotationClass = annotationClass;
    }

    private List<Class<?>>                classList       = new ArrayList<Class<?>>();
    private Class<? extends Annotation>[] annotationClass = null;

    public boolean filter(String urlStr, URL url, ClassLoader classLoader) {
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
            if (annotationClass != null && annotationClass.length > 0) {
                for (Class<? extends Annotation> cls : annotationClass) {
                    if (!clazz.isAnnotationPresent(cls)) {
                        isAdd = false;
                        break;
                    }
                }
                if (isAdd) {
                    classList.add(clazz);
                    LOGGER.info("classPathStr={}..................Got!", classPathStr);
                    return true;
                } else {
                    LOGGER.info("classPathStr={}..................Pass!", classPathStr);
                    return false;
                }
            } else {
                LOGGER.info("classPathStr={}..................Pass!", classPathStr);
                return false;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Class<?>[] getAnnotationClass() {
        return annotationClass;
    }

    public void setAnnotationClass(Class<? extends Annotation>[] annotationClass) {
        this.annotationClass = annotationClass;
    }

    public List<Class<?>> getClassList() {
        return classList;
    }
}
