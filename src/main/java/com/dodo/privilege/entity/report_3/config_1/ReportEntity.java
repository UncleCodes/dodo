package com.dodo.privilege.entity.report_3.config_1;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.dodo.common.annotation.action.DodoAction;
import com.dodo.common.annotation.action.DodoEntity;
import com.dodo.common.annotation.field.DodoField;
import com.dodo.common.annotation.field.DodoShowColumn;
import com.dodo.common.annotation.menu.DodoMenu;
import com.dodo.common.annotation.right.DodoButtonLocation;
import com.dodo.common.annotation.right.DodoButtonRight;
import com.dodo.common.annotation.right.DodoButtonRightEvent;
import com.dodo.common.annotation.right.DodoButtonRightModel;
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
@DodoEntity(
        nameKey = "dodo.privilege.report.config.ReportEntity.entityKey",
        actions = { DodoAction.UPDATE, DodoAction.DELETE, DodoAction.CHART, DodoAction.VIEW, DodoAction.EXPORT },
        levelOne = @DodoMenu(nameKey = "dodo.privilege.report.menuNameKey", sortSeq = 3),
        levelTwo = @DodoMenu(nameKey = "dodo.privilege.report.config.menuNameKey", sortSeq = 1),
        levelThree = @DodoMenu(nameKey = "dodo.privilege.report.config.ReportEntity.menuNameKey", sortSeq = 3))
@DodoButtonRight(
        nameKey = "dodo.privilege.report.config.ReportEntity.button.design.namekey",
        path = "${rootPath}/sqlreport/design.jhtml",
        model = DodoButtonRightModel.ROW,
        event = DodoButtonRightEvent.URL)
@DodoButtonRight(
        nameKey = "dodo.privilege.report.config.ReportEntity.button.viewreport.namekey",
        path = "${rootPath}/sqlreport/report.jhtml",
        model = DodoButtonRightModel.ROW,
        urlTarget = "_blank",
        event = DodoButtonRightEvent.URL)
@DodoButtonRight(
        nameKey = "dodo.privilege.report.config.ReportEntity.button.design.namekey",
        path = "${rootPath}/sqlreport/design.jhtml",
        model = DodoButtonRightModel.MODEL,
        event = DodoButtonRightEvent.URL,
        urlTarget = "_blank",
        location = DodoButtonLocation.TOP)
public class ReportEntity extends BaseEntity {
    private static final long serialVersionUID = -2026205118666904848L;

    @DodoField(
            sortSeq = 1,
            nameKey = "dodo.privilege.report.config.ReportEntity.namekey.menu",
            isnullable = false,
            editable = false)
    private ReportMenu        menu;

    @DodoField(
            sortSeq = 2,
            nameKey = "dodo.privilege.report.config.ReportEntity.namekey.name",
            isnullable = false,
            maxLength = 64,
            queryOnList = true,
            editable = false)
    @DodoShowColumn(sortSeq = 0)
    private String            name;

    @DodoField(
            sortSeq = 3,
            nameKey = "dodo.privilege.report.config.ReportEntity.namekey.execSql",
            isnullable = false,
            isTextArea = true,
            listable = false,
            editable = false)
    private String            execSql;

    @DodoField(
            sortSeq = 4,
            nameKey = "dodo.privilege.report.config.ReportEntity.namekey.reportFields",
            editable = false,
            addable = false,
            listable = false)
    private List<ReportField> reportFields;

    @Column(length = 64)
    public String getName() {
        return name;
    }

    @Column(length = 1024)
    public String getExecSql() {
        return execSql;
    }

    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = "reportEntity")
    @Fetch(FetchMode.SUBSELECT)
    @OrderBy("sortSeq asc")
    public List<ReportField> getReportFields() {
        return reportFields;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExecSql(String execSql) {
        this.execSql = execSql;
    }

    public void setReportFields(List<ReportField> reportFields) {
        this.reportFields = reportFields;
    }

    @OneToOne
    public ReportMenu getMenu() {
        return menu;
    }

    public void setMenu(ReportMenu menu) {
        this.menu = menu;
    }
}
