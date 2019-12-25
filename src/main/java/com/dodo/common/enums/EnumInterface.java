package com.dodo.common.enums;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public interface EnumInterface {
    public static Map<Class<? extends EnumInterface>, Map<Integer, EnumInterface>> cacheSeq  = new HashMap<Class<? extends EnumInterface>, Map<Integer, EnumInterface>>();
    public static Map<Class<? extends EnumInterface>, Map<String, EnumInterface>>  cacheName = new HashMap<Class<? extends EnumInterface>, Map<String, EnumInterface>>();

    public String getName();

    public String getDesc();

    public String getNameKey();

    public String getDescKey();

    public String name();

    public int ordinal();

    @JsonValue
    public Integer getSeq();

    @SuppressWarnings("unchecked")
    public static <T extends EnumInterface> T valueOf(Class<T> clazz, Integer seq) {
        if (seq == null) {
            return null;
        }
        Map<Integer, EnumInterface> enumsMap = cacheSeq.get(clazz);
        if (enumsMap != null) {
            return (T) enumsMap.get(seq);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T extends EnumInterface> T valueOf(Class<T> clazz, String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        Map<String, EnumInterface> enumsMap = cacheName.get(clazz);
        if (enumsMap != null) {
            return (T) enumsMap.get(name);
        }
        return null;
    }

    public static void init(Class<? extends EnumInterface> clazz) {
        Map<String, EnumInterface> enumsMapNames = new HashMap<String, EnumInterface>();
        Map<Integer, EnumInterface> enumsMapSeqs = new HashMap<Integer, EnumInterface>();
        EnumInterface[] enums = clazz.getEnumConstants();
        for (EnumInterface t : enums) {
            enumsMapNames.put(t.name(), t);
            enumsMapSeqs.put(t.getSeq(), t);
        }
        cacheName.put(clazz, enumsMapNames);
        cacheSeq.put(clazz, enumsMapSeqs);
    }
}
