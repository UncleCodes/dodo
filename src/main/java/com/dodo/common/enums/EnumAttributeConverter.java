package com.dodo.common.enums;

import javax.persistence.AttributeConverter;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public abstract class EnumAttributeConverter<T extends EnumInterface> implements AttributeConverter<T, Integer> {

    @Override
    public Integer convertToDatabaseColumn(T attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getSeq();
    }

    public abstract Class<T> getClazz();

    @Override
    public T convertToEntityAttribute(Integer dbData) {
        return (T) EnumInterface.valueOf(getClazz(), dbData);
    }
}
