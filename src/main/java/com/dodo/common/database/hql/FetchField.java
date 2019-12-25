package com.dodo.common.database.hql;

import java.io.Serializable;
/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class FetchField implements Serializable{
	private static final long serialVersionUID = 5519119740541634946L;
	private String tableAlias;
	private String fieldNames;
	private String fieldAlias;
	public FetchField(String tableAlias, String fieldNames, String fieldAlias) {
		super();
		this.tableAlias = tableAlias.trim();
		this.fieldNames = fieldNames.trim();
		this.fieldAlias = fieldAlias==null?null:fieldAlias.trim();
	}
	public String getTableAlias() {
		return tableAlias;
	}
	public String getFieldNames() {
		return fieldNames;
	}
	public String getFieldAlias() {
		return fieldAlias;
	}
	
	public StringBuilder toHqlString(){
		return new StringBuilder().append(tableAlias).append(".").append(fieldNames).append(" as ").append(fieldAlias);
	}
	
	public StringBuilder toFunctionString(boolean isDistinct){
		return new StringBuilder(isDistinct?" distinct ":"").append(tableAlias).append(".").append(fieldNames);
	}
	
	public String getFirstField(){
		return fieldNames.contains(".")?fieldNames.substring(0,fieldNames.indexOf(".")):fieldNames;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fieldAlias == null) ? 0 : fieldAlias.hashCode());
		result = prime * result
				+ ((fieldNames == null) ? 0 : fieldNames.hashCode());
		result = prime * result
				+ ((tableAlias == null) ? 0 : tableAlias.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FetchField other = (FetchField) obj;
		if (fieldAlias == null) {
			if (other.fieldAlias != null)
				return false;
		} else if (!fieldAlias.equals(other.fieldAlias))
			return false;
		if (fieldNames == null) {
			if (other.fieldNames != null)
				return false;
		} else if (!fieldNames.equals(other.fieldNames))
			return false;
		if (tableAlias == null) {
			if (other.tableAlias != null)
				return false;
		} else if (!tableAlias.equals(other.tableAlias))
			return false;
		return true;
	}
}
