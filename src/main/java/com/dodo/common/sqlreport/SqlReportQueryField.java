package com.dodo.common.sqlreport;

import com.dodo.common.annotation.report.ReportFieldType;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class SqlReportQueryField {
    private String          fieldName;

    @JsonIgnore
    private ReportFieldType fieldTypeEnum;
    private String          fieldType;

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public ReportFieldType getFieldTypeEnum() {
        return fieldTypeEnum;
    }

    public void setFieldTypeEnum(ReportFieldType fieldTypeEnum) {
        this.fieldTypeEnum = fieldTypeEnum;
    }

    @Override
    public String toString() {
        return "SqlReportQueryField [fieldName=" + fieldName + ", fieldTypeEnum=" + fieldTypeEnum + ", fieldType="
                + fieldType + "]";
    }
}
