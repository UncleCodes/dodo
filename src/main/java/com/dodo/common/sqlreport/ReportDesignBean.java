package com.dodo.common.sqlreport;

import java.util.ArrayList;
import java.util.List;
/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class ReportDesignBean {
	private String menuSELECT;
	private String updateReportId;
	private String name;
	private String beforeConditionSELECT;
	private String afterConditionSELECT;
	private String execSql;
	private List<String> fieldName = new ArrayList<String>();
	private List<String> fieldType = new ArrayList<String>();
	private List<String> fieldLabel = new ArrayList<String>();
	private List<String> jumpLink = new ArrayList<String>();
	private List<Boolean> fieldIsShow = new ArrayList<Boolean>();
	private List<String> fieldQueryType = new ArrayList<String>();
	public String getName() {
		return name;
	}
	public String getBeforeConditionSELECT() {
		return beforeConditionSELECT;
	}
	public String getAfterConditionSELECT() {
		return afterConditionSELECT;
	}
	public String getExecSql() {
		return execSql;
	}
	public List<String> getFieldLabel() {
		return fieldLabel;
	}
	public List<Boolean> getFieldIsShow() {
		return fieldIsShow;
	}
	public List<String> getFieldQueryType() {
		return fieldQueryType;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setBeforeConditionSELECT(String beforeConditionSELECT) {
		this.beforeConditionSELECT = beforeConditionSELECT;
	}
	public void setAfterConditionSELECT(String afterConditionSELECT) {
		this.afterConditionSELECT = afterConditionSELECT;
	}
	public void setExecSql(String execSql) {
		this.execSql = execSql;
	}
	public void setFieldLabel(List<String> fieldLabel) {
		this.fieldLabel = fieldLabel;
	}
	public void setFieldIsShow(List<Boolean> fieldIsShow) {
		this.fieldIsShow = fieldIsShow;
	}
	public void setFieldQueryType(List<String> fieldQueryType) {
		this.fieldQueryType = fieldQueryType;
	}
	public String getUpdateReportId() {
		return updateReportId;
	}
	public void setUpdateReportId(String updateReportId) {
		this.updateReportId = updateReportId;
	}
	public List<String> getFieldName() {
		return fieldName;
	}
	public List<String> getFieldType() {
		return fieldType;
	}
	public void setFieldName(List<String> fieldName) {
		this.fieldName = fieldName;
	}
	public void setFieldType(List<String> fieldType) {
		this.fieldType = fieldType;
	}
	public List<String> getJumpLink() {
		return jumpLink;
	}
	public void setJumpLink(List<String> jumpLink) {
		this.jumpLink = jumpLink;
	}
	
	public String getMenuSELECT() {
		return menuSELECT;
	}
	public void setMenuSELECT(String menuSELECT) {
		this.menuSELECT = menuSELECT;
	}
	@Override
	public String toString() {
		return "ReportDesignBean [menuSELECT=" + menuSELECT + ", updateReportId=" + updateReportId + ", name=" + name + ", beforeConditionSELECT=" + beforeConditionSELECT + ", afterConditionSELECT=" + afterConditionSELECT + ", execSql=" + execSql + ", fieldName=" + fieldName + ", fieldType=" + fieldType + ", fieldLabel=" + fieldLabel + ", jumpLink=" + jumpLink + ", fieldIsShow=" + fieldIsShow + ", fieldQueryType=" + fieldQueryType + "]";
	}
}
