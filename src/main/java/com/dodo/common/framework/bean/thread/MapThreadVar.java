package com.dodo.common.framework.bean.thread;

import java.util.Map;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class MapThreadVar {
    private static ThreadLocal<Map<String, Object>> tl = new ThreadLocal<Map<String, Object>>();

    public static void set(Map<String, Object> obj) {
        tl.set(obj);
    }

    public static Map<String, Object> get() {
        return tl.get();
    }

    public static void remove() {
        tl.remove();
    }
}
