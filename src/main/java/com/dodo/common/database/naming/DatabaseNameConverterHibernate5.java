package com.dodo.common.database.naming;

import java.lang.reflect.Field;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class DatabaseNameConverterHibernate5 implements DatabaseNameConverter {

    private PhysicalNamingStrategy physicalNamingStrategy;

    public String getTableNameByClass(Class<?> clazz) {
        return physicalNamingStrategy.toPhysicalTableName(Identifier.toIdentifier(clazz.getSimpleName()), null)
                .getText();
    }

    public String getTableNameByClassName(String clazzName) {
        return physicalNamingStrategy.toPhysicalTableName(Identifier.toIdentifier(clazzName), null).getText();
    }

    public String getColumnNameByProperty(Field field) {
        return physicalNamingStrategy.toPhysicalColumnName(Identifier.toIdentifier(field.getName()), null).getText();
    }

    public String getColumnNameByPropertyName(String fieldName) {
        return physicalNamingStrategy.toPhysicalColumnName(Identifier.toIdentifier(fieldName), null).getText();
    }

    public void setPhysicalNamingStrategy(PhysicalNamingStrategy physicalNamingStrategy) {
        this.physicalNamingStrategy = physicalNamingStrategy;
    }
}
