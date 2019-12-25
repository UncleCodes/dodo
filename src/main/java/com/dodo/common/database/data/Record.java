package com.dodo.common.database.data;

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
public class Record {
    private Map<String, Object> rawData;

    public Record(Map<String, Object> rawData) {
        super();
        this.rawData = rawData;
    }

    public Map<String, Object> getRawData() {
        return rawData;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) rawData.get(key);
    }

    public void remove(String key) {
        rawData.remove(key);
    }

    public void put(String key, Object value) {
        rawData.put(key, value);
    }

    public int size() {
        return rawData.size();
    }

    @Override
    public String toString() {
        return "Record [rawData=" + rawData + "]";
    }
}
