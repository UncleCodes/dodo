package com.dodo.privilege.entity.admin_1.data_4;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.OneToOne;

import org.hibernate.annotations.DynamicInsert;

import com.dodo.common.annotation.action.DodoAction;
import com.dodo.common.annotation.action.DodoEntity;
import com.dodo.common.annotation.field.DodoField;
import com.dodo.common.annotation.menu.DodoMenu;
import com.dodo.common.annotation.right.DodoRowRight;
import com.dodo.common.framework.entity.BaseEntity;
import com.dodo.privilege.entity.admin_1.base_1.Admin;
import com.dodo.privilege.entity.admin_1.config_5.Entity;
import com.dodo.privilege.enums.ExcelUpdateStatus;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
@javax.persistence.Entity
@DynamicInsert
@DodoEntity(
        nameKey = "dodo.privilege.admin.data.ExcelUpdateInfo.entityKey",
        actions = { DodoAction.VIEW, DodoAction.EXPORT },
        levelOne = @DodoMenu(nameKey = "dodo.privilege.admin.menuNameKey", sortSeq = 1),
        levelTwo = @DodoMenu(nameKey = "dodo.privilege.admin.data.menuNameKey", sortSeq = 4),
        levelThree = @DodoMenu(nameKey = "dodo.privilege.admin.data.ExcelUpdateInfo.menuNameKey", sortSeq = 2))
@DodoRowRight(entityProperty = "admin")
public class ExcelUpdateInfo extends BaseEntity {
    private static final long serialVersionUID = -1528666175360594482L;

    @DodoField(
            sortSeq = 1,
            nameKey = "dodo.privilege.admin.data.ExcelUpdateInfo.namekey.updateStatus",
            queryOnList = true)
    private ExcelUpdateStatus updateStatus;

    @DodoField(sortSeq = 2, nameKey = "dodo.privilege.admin.data.ExcelUpdateInfo.namekey.updatePercent")
    private String            updatePercent;

    @DodoField(sortSeq = 3, nameKey = "dodo.privilege.admin.data.ExcelUpdateInfo.namekey.admin")
    private Admin             admin;

    @DodoField(sortSeq = 4, nameKey = "dodo.privilege.admin.data.ExcelUpdateInfo.namekey.entity")
    private Entity            entity;

    @DodoField(sortSeq = 5, nameKey = "dodo.privilege.admin.data.ExcelUpdateInfo.namekey.updateFile", isFile = true)
    private String            updateFile;

    @DodoField(sortSeq = 6, nameKey = "dodo.privilege.admin.data.ExcelUpdateInfo.namekey.resultFile", isFile = true)
    private String            resultFile;

    @DodoField(sortSeq = 7, nameKey = "dodo.privilege.admin.data.ExcelUpdateInfo.namekey.sheetCount")
    private Integer           sheetCount;

    @DodoField(sortSeq = 8, nameKey = "dodo.privilege.admin.data.ExcelUpdateInfo.namekey.totalRows")
    private Integer           totalRows;

    @DodoField(sortSeq = 9, nameKey = "dodo.privilege.admin.data.ExcelUpdateInfo.namekey.successRows")
    private Integer           successRows;

    @DodoField(sortSeq = 10, nameKey = "dodo.privilege.admin.data.ExcelUpdateInfo.namekey.failRows")
    private Integer           failRows;

    @DodoField(
            sortSeq = 11,
            nameKey = "dodo.privilege.admin.data.ExcelUpdateInfo.namekey.triggerName",
            listable = false)
    private String            triggerName;

    @DodoField(sortSeq = 12, nameKey = "dodo.privilege.admin.data.ExcelUpdateInfo.namekey.sheetNote")
    private String            sheetNote;

    @Override
    public void onSave() {
        super.onSave();
        if (updateStatus == null) {
            updateStatus = ExcelUpdateStatus.NEW;
        }
        if (sheetCount == null) {
            sheetCount = 0;
        }
        if (totalRows == null) {
            totalRows = 0;
        }
        if (successRows == null) {
            successRows = 0;
        }
        if (failRows == null) {
            failRows = 0;
        }
        if (updatePercent == null) {
            updatePercent = "0.00%";
        }
        if (sheetNote == null) {
            sheetNote = "";
        }
    }

    @Column(length = 3)
    @Convert(converter = ExcelUpdateStatus.Converter.class)
    public ExcelUpdateStatus getUpdateStatus() {
        return updateStatus;
    }

    @Column(length = 7)
    public String getUpdatePercent() {
        return updatePercent;
    }

    @OneToOne
    public Admin getAdmin() {
        return admin;
    }

    @OneToOne
    public Entity getEntity() {
        return entity;
    }

    @Column(length = 1024)
    public String getUpdateFile() {
        return updateFile;
    }

    @Column(length = 1024)
    public String getResultFile() {
        return resultFile;
    }

    public Integer getSheetCount() {
        return sheetCount;
    }

    public Integer getTotalRows() {
        return totalRows;
    }

    public Integer getSuccessRows() {
        return successRows;
    }

    public Integer getFailRows() {
        return failRows;
    }

    public void setUpdateStatus(ExcelUpdateStatus updateStatus) {
        this.updateStatus = updateStatus;
    }

    public void setUpdatePercent(String updatePercent) {
        this.updatePercent = updatePercent;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public void setUpdateFile(String updateFile) {
        this.updateFile = updateFile;
    }

    public void setResultFile(String resultFile) {
        this.resultFile = resultFile;
    }

    public void setSheetCount(Integer sheetCount) {
        this.sheetCount = sheetCount;
    }

    public void setTotalRows(Integer totalRows) {
        this.totalRows = totalRows;
    }

    public void setSuccessRows(Integer successRows) {
        this.successRows = successRows;
    }

    public void setFailRows(Integer failRows) {
        this.failRows = failRows;
    }

    @Column(length = 32)
    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    @Column(length = 512)
    public String getSheetNote() {
        return sheetNote;
    }

    public void setSheetNote(String sheetNote) {
        this.sheetNote = sheetNote;
    }
}
