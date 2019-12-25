package com.dodo.common.database.hql;

import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class UpdateParameter {
	public enum HelperUpdateType{
		BINDVAR,SQLSEGMENT
	}
	private String fieldName;
	private Object fieldValue;
	private Boolean isCollection;
	private HelperUpdateType updateType;
	private Boolean isMapField = Boolean.FALSE;
	private Boolean isSortedMapField = Boolean.FALSE;
	public UpdateParameter(String fieldName, Object fieldValue,
			HelperUpdateType updateType) {
		super();
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
		this.updateType = updateType;
		this.isCollection = Boolean.FALSE;
	}
	public String getFieldName() {
		return fieldName;
	}
	public Object getFieldValue() {
		return fieldValue;
	}
	public HelperUpdateType getUpdateType() {
		return updateType;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public void setFieldValue(Object fieldValue) {
		this.fieldValue = fieldValue;
	}
	public void setUpdateType(HelperUpdateType updateType) {
		this.updateType = updateType;
	}
	public Boolean getIsCollection() {
		return isCollection;
	}
	public void setIsCollection(Boolean isCollection) {
		this.isCollection = isCollection;
	}
	
	public StringBuilder toHqlString(){
		StringBuilder setBuilder = new StringBuilder("");
		if(updateType==HelperUpdateType.SQLSEGMENT){
			setBuilder.append(HqlHelper.currTable)
			.append(".")
			.append(fieldName)
			.append("=")
			.append(HqlHelper.currTable)
			.append(".")
			.append(fieldValue);
		}else{
			setBuilder.append(HqlHelper.currTable)
			.append(".")
			.append(fieldName)
			.append("=:")
			.append(fieldName)
			.append(HqlHelper.setLast);
		}
		return setBuilder;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fieldName == null) ? 0 : fieldName.hashCode());
		result = prime * result
				+ ((updateType == null) ? 0 : updateType.hashCode());
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
		UpdateParameter other = (UpdateParameter) obj;
		if (fieldName == null) {
			if (other.fieldName != null)
				return false;
		} else if (!fieldName.equals(other.fieldName))
			return false;
		if (updateType != other.updateType)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "UpdateParameter [fieldName=" + fieldName + ", fieldValue="
				+ fieldValue + ", isCollection=" + isCollection
				+ ", updateType=" + updateType + "]";
	}
	public Boolean getIsMapField() {
		return isMapField;
	}
	public Boolean getIsSortedMapField() {
		return isSortedMapField;
	}
	public void setIsMapField(Boolean isMapField) {
		this.isMapField = isMapField;
		if(fieldValue!=null && !(fieldValue instanceof Map)){
			throw new HqlHelperException("Can't update 'java.util.Map' field '"+fieldName+"' to '"+fieldValue.getClass().getName()+"'!");
		}
	}
	@SuppressWarnings("unchecked")
	public void setIsSortedMapField(Boolean isSortedMapField) {
		this.isSortedMapField = isSortedMapField;
		if(fieldValue!=null && !(fieldValue instanceof SortedMap)){
			Map<String,String> srcMap = (Map<String,String>)fieldValue;
			Map<String,String> treeMap = new TreeMap<String, String>();
			Iterator<String> it = srcMap.keySet().iterator();
			String key = null;
			while (it.hasNext()) {
				key = (String) it.next();
				treeMap.put(key, srcMap.get(key));
			}
			fieldValue = treeMap;
		}
	}
}
