package com.dodo.privilege.entity.admin_1.data_4;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;

import com.dodo.common.annotation.DodoCodeGenerator;
import com.dodo.common.annotation.action.DodoAction;
import com.dodo.common.annotation.action.DodoActionGenerator;
import com.dodo.common.annotation.dao.DodoDaoGenerator;
import com.dodo.common.annotation.field.DodoField;
import com.dodo.common.annotation.menu.DodoMenu;
import com.dodo.common.annotation.menu.DodoMenuLevel;
import com.dodo.common.annotation.right.DodoRight;
import com.dodo.common.annotation.right.DodoRowRight;
import com.dodo.common.annotation.service.DodoSrvGenerator;
import com.dodo.common.framework.entity.BaseEntity;
import com.dodo.privilege.entity.admin_1.base_1.Admin;
import com.dodo.privilege.entity.admin_1.config_5.Entity;
import com.dodo.privilege.enums.ConvertStatus;

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
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DodoMenu(nameKey = "dodo.privilege.admin.menuNameKey", level = DodoMenuLevel.LEVEL1, sortSeq = 1)
@DodoMenu(nameKey = "dodo.privilege.admin.data.menuNameKey", level = DodoMenuLevel.LEVEL2, sortSeq = 4)
@DodoMenu(nameKey = "dodo.privilege.admin.data.DocConvertTask.menuNameKey", level = DodoMenuLevel.LEVEL3, sortSeq = 5)
@DodoRight(nameKey = "dodo.privilege.admin.data.DocConvertTask.entityKey")
@DodoCodeGenerator(daoGenerator = @DodoDaoGenerator, srvGenerator = @DodoSrvGenerator, actGenerator = @DodoActionGenerator(actions = {
        DodoAction.VIEW, DodoAction.EXPORT, DodoAction.DELETE, DodoAction.CHART }))
@DodoRowRight(entityProperty = "admin")
public class DocConvertTask extends BaseEntity {
    private static final long serialVersionUID = 4620199539932170447L;

    @DodoField(sortSeq = -1, nameKey = "dodo.privilege.admin.data.DocConvertTask.namekey.admin")
    private Admin             admin;

    @DodoField(sortSeq = 0, nameKey = "dodo.privilege.admin.data.DocConvertTask.namekey.convertStatus", queryOnList = true)
    private ConvertStatus     convertStatus;

    @DodoField(sortSeq = 1, nameKey = "dodo.privilege.admin.data.DocConvertTask.namekey.convertPercent")
    private String            convertPercent;

    @DodoField(sortSeq = 2, nameKey = "dodo.privilege.admin.data.DocConvertTask.namekey.targetEntity", addable = false, editable = false)
    private Entity            targetEntity;

    @DodoField(sortSeq = 3, nameKey = "dodo.privilege.admin.data.DocConvertTask.namekey.entityId", addable = false, editable = false)
    private String            entityId;

    @DodoField(sortSeq = 4, nameKey = "dodo.privilege.admin.data.DocConvertTask.namekey.fieldName", addable = false, editable = false)
    private String            fieldName;

    @DodoField(sortSeq = 5, nameKey = "dodo.privilege.admin.data.DocConvertTask.namekey.fieldShowName", addable = false, editable = false)
    private String            fieldShowName;

    @DodoField(sortSeq = 6, nameKey = "dodo.privilege.admin.data.DocConvertTask.namekey.extFieldName", addable = false, editable = false)
    private String            extFieldName;

    @DodoField(sortSeq = 7, nameKey = "dodo.privilege.admin.data.DocConvertTask.namekey.extFieldShowName", addable = false, editable = false)
    private String            extFieldShowName;

    @DodoField(sortSeq = 8, nameKey = "dodo.privilege.admin.data.DocConvertTask.namekey.fileId", addable = false, editable = false)
    private String            fileId;

    @DodoField(sortSeq = 9, nameKey = "dodo.privilege.admin.data.DocConvertTask.namekey.fileStr", addable = false, editable = false, isDoc = true)
    private String            fileStr;

    @Override
    public void onSave() {
        super.onSave();
        if (convertStatus == null) {
            convertStatus = ConvertStatus.NEW;
        }
        if (convertPercent == null) {
            convertPercent = "0.00%";
        }
    }

    @OneToOne(fetch = FetchType.LAZY)
    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public void setTargetEntity(Entity targetEntity) {
        this.targetEntity = targetEntity;
    }

    @OneToOne(fetch = FetchType.EAGER)
    public Entity getTargetEntity() {
        return targetEntity;
    }

    @Column(length = 32)
    public String getEntityId() {
        return entityId;
    }

    @Column(length = 64)
    public String getFieldName() {
        return fieldName;
    }

    @Column(length = 64)
    public String getFieldShowName() {
        return fieldShowName;
    }

    @Lob
    public String getFileStr() {
        return fileStr;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setFieldShowName(String fieldShowName) {
        this.fieldShowName = fieldShowName;
    }

    public void setFileStr(String fileStr) {
        this.fileStr = fileStr;
    }

    @Column(length = 64)
    public String getExtFieldName() {
        return extFieldName;
    }

    @Column(length = 64)
    public String getExtFieldShowName() {
        return extFieldShowName;
    }

    public void setExtFieldName(String extFieldName) {
        this.extFieldName = extFieldName;
    }

    public void setExtFieldShowName(String extFieldShowName) {
        this.extFieldShowName = extFieldShowName;
    }

    @Column(length = 3)
    @Convert(converter = ConvertStatus.Converter.class)
    public ConvertStatus getConvertStatus() {
        return convertStatus;
    }

    @Column(length = 7)
    public String getConvertPercent() {
        return convertPercent;
    }

    public void setConvertStatus(ConvertStatus convertStatus) {
        this.convertStatus = convertStatus;
    }

    public void setConvertPercent(String convertPercent) {
        this.convertPercent = convertPercent;
    }

    @Column(length = 64)
    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}
