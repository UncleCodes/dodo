package com.dodo.privilege.entity.report_3.config_1;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;

import com.dodo.common.annotation.DodoCodeGenerator;
import com.dodo.common.annotation.action.DodoAction;
import com.dodo.common.annotation.action.DodoActionGenerator;
import com.dodo.common.annotation.dao.DodoDaoGenerator;
import com.dodo.common.annotation.field.DodoField;
import com.dodo.common.annotation.field.DodoShowColumn;
import com.dodo.common.annotation.menu.DodoMenu;
import com.dodo.common.annotation.menu.DodoMenuLevel;
import com.dodo.common.annotation.report.ReportFieldType;
import com.dodo.common.annotation.report.ReportQueryType;
import com.dodo.common.annotation.right.DodoRight;
import com.dodo.common.annotation.service.DodoSrvGenerator;
import com.dodo.common.framework.entity.BaseEntity;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
@Entity
@DynamicInsert
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DodoMenu(nameKey = "dodo.privilege.report.menuNameKey", level = DodoMenuLevel.LEVEL1, sortSeq = 3)
@DodoMenu(nameKey = "dodo.privilege.report.config.menuNameKey", level = DodoMenuLevel.LEVEL2, sortSeq = 1)
@DodoMenu(nameKey = "dodo.privilege.report.config.ReportField.menuNameKey", level = DodoMenuLevel.LEVEL3, sortSeq = 4)
@DodoRight(nameKey = "dodo.privilege.report.config.ReportField.entityKey")
@DodoCodeGenerator(daoGenerator = @DodoDaoGenerator, srvGenerator = @DodoSrvGenerator, actGenerator = @DodoActionGenerator(actions = {
        DodoAction.VIEW, DodoAction.UPDATE, DodoAction.EXPORT }))
public class ReportField extends BaseEntity {
    private static final long serialVersionUID = -4443407897694944602L;

    @DodoField(sortSeq = 0, nameKey = "dodo.privilege.report.config.ReportField.namekey.reportEntity", isnullable = false, editable = false, queryOnList = true)
    private ReportEntity      reportEntity;

    @DodoField(sortSeq = 1, nameKey = "dodo.privilege.report.config.ReportField.namekey.queryField", isnullable = false, editable = false)
    @DodoShowColumn(sortSeq = 0)
    private String            queryField;

    @DodoField(sortSeq = 2, nameKey = "dodo.privilege.report.config.ReportField.namekey.showName", isnullable = false, maxLength = 64)
    @DodoShowColumn(sortSeq = 1)
    private String            showName;

    @DodoField(sortSeq = 3, nameKey = "dodo.privilege.report.config.ReportField.namekey.fieldType", isnullable = false, editable = false)
    private ReportFieldType   fieldType;

    @DodoField(sortSeq = 4, nameKey = "dodo.privilege.report.config.ReportField.namekey.queryType")
    private ReportQueryType   queryType;

    @DodoField(sortSeq = 5, nameKey = "dodo.privilege.report.config.ReportField.namekey.isShow", isnullable = false)
    private Boolean           isShow;

    @DodoField(sortSeq = 6, nameKey = "dodo.privilege.report.config.ReportField.namekey.jumpLink", editable = false, infoTipKey = "dodo.privilege.report.config.ReportField.infoTip.jumpLink")
    private String            jumpLink;

    @ManyToOne
    public ReportEntity getReportEntity() {
        return reportEntity;
    }

    @Column(length = 64)
    public String getQueryField() {
        return queryField;
    }

    @Column(length = 64)
    public String getShowName() {
        return showName;
    }

    @Column(length = 3)
    @Convert(converter = ReportFieldType.Converter.class)
    public ReportFieldType getFieldType() {
        return fieldType;
    }

    @Column(length = 3)
    @Convert(converter = ReportQueryType.Converter.class)
    public ReportQueryType getQueryType() {
        return queryType;
    }

    public void setReportEntity(ReportEntity reportEntity) {
        this.reportEntity = reportEntity;
    }

    public void setQueryField(String queryField) {
        this.queryField = queryField;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public void setFieldType(ReportFieldType fieldType) {
        this.fieldType = fieldType;
    }

    public void setQueryType(ReportQueryType queryType) {
        this.queryType = queryType;
    }

    public Boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(Boolean isShow) {
        this.isShow = isShow;
    }

    @Column(length = 128)
    public String getJumpLink() {
        return jumpLink;
    }

    public void setJumpLink(String jumpLink) {
        this.jumpLink = jumpLink;
    }
}
