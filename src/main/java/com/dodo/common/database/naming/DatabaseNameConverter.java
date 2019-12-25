package com.dodo.common.database.naming;

import java.lang.reflect.Field;
/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public interface DatabaseNameConverter {
	public String getTableNameByClass(Class<?> clazz);
	public String getTableNameByClassName(String clazzName);
	public String getColumnNameByProperty(Field field);
	public String getColumnNameByPropertyName(String fieldName);
}
