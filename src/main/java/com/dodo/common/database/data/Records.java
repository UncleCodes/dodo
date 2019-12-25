package com.dodo.common.database.data;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
public class Records implements Collection<Record> {
    private List<Map<String, Object>> rawData;

    public Records() {
        this.rawData = new ArrayList<Map<String, Object>>();
    }

    public Records(List<Map<String, Object>> rawData) {
        this.rawData = rawData;
    }

    public List<Map<String, Object>> getRawData() {
        return rawData;
    }

    public int size() {
        return rawData.size();
    }

    public Record get(int index) {
        return new Record(rawData.get(index));
    }

    public boolean add(Record record) {
        return rawData.add(record.getRawData());
    }

    public void add(Map<String, Object> e) {
        rawData.add(e);
    }

    public void addAll(Records records) {
        rawData.addAll(records.getRawData());
    }

    public void addAll(List<Map<String, Object>> es) {
        rawData.addAll(es);
    }

    @Override
    public boolean addAll(Collection<? extends Record> c) {
        return rawData.addAll(_toList(c));
    }

    @Override
    public void clear() {
        rawData.clear();
    }

    @Override
    public boolean contains(Object o) {
        if (o instanceof Record) {
            return rawData.contains(((Record) o).getRawData());
        } else if (o instanceof Map) {
            return rawData.contains(o);
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return rawData.containsAll(c);
    }

    @Override
    public boolean isEmpty() {
        return rawData.isEmpty();
    }

    @Override
    public Ite iterator() {
        return new Ite(rawData);
    }

    @Override
    public boolean remove(Object o) {
        if (o instanceof Record) {
            return rawData.remove(((Record) o).getRawData());
        } else if (o instanceof Map) {
            return rawData.remove(o);
        }
        return rawData.remove(o);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return rawData.removeAll(_convert(c));
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return rawData.retainAll(_convert(c));
    }

    @Override
    public Object[] toArray() {
        return rawData.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return rawData.toArray(a);
    }

    @SuppressWarnings("unchecked")
    private Collection<?> _convert(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }

        ParameterizedType parameterizedType = (ParameterizedType) c.getClass().getGenericInterfaces()[0];
        Type[] types = parameterizedType.getActualTypeArguments();
        if (types[0] == Record.class) {
            return _toList((Collection<Record>) c);
        }
        return c;
    }

    private List<Map<String, Object>> _toList(Collection<? extends Record> c) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(c.size());
        for (Record record : c) {
            list.add(record.getRawData());
        }
        return list;
    }

    @Override
    public String toString() {
        return "Records [rawData=" + rawData + "]";
    }
}
