package com.dodo.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.dodo.common.framework.entity.BaseEntity;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class ReflectUtil {
    public static String getSelectProperty(Collection<? extends BaseEntity> c, String lastAppend) {
        List<String> list = new ArrayList<String>(c.size());
        for (BaseEntity f : c) {
            list.add(lastAppend + String.valueOf(f.getId()));
        }
        return list.size() == 0 ? list.toString() : list.toString().replace("[", "['").replace("]", "']")
                .replace(",", "','").replace(" ", "");
    }

    public static Object getValueByGetMethod(Object obj, String propertyName) {
        String str = "get" + StringUtils.capitalize(propertyName);
        try {
            Method m = obj.getClass().getMethod(str, new Class[0]);
            return m.invoke(obj, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setValueBySetMethod(Object o, String propertyName, Object propertyValue, Class<?> propertyClass) {
        String str = "set" + StringUtils.capitalize(propertyName);
        try {
            Method m = o.getClass().getMethod(str, new Class[] { propertyClass });
            m.invoke(o, new Object[] { propertyValue });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object getFieldValue(Object o, String propertyName) {
        Field f = getAccessibleField(o, propertyName);
        if (f == null)
            throw new IllegalArgumentException("Could not find field " + propertyName);
        Object o1 = null;
        try {
            o1 = f.get(o);
        } catch (IllegalAccessException e) {
        }
        return o1;
    }

    public static void setFieldValue(Object o, String propertyName, Object propertyValue) {
        Field f = getAccessibleField(o, propertyName);
        if (f == null)
            throw new IllegalArgumentException("Could not find field " + propertyName);
        try {
            f.set(o, propertyValue);
        } catch (IllegalAccessException e) {
        }
    }

    private static Field getAccessibleField(Object obj, String propertyName) {
        Class<?> clazz = obj.getClass();
        while (clazz != Object.class) {
            try {
                Field f = clazz.getDeclaredField(propertyName);
                f.setAccessible(true);
                return f;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }
}
