package com.dodo.privilege.entity.admin_1.data_4;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

import org.hibernate.annotations.DynamicInsert;

import com.dodo.common.annotation.action.DodoAction;
import com.dodo.common.annotation.action.DodoEntity;
import com.dodo.common.annotation.field.DodoField;
import com.dodo.common.annotation.menu.DodoMenu;
import com.dodo.common.annotation.menu.DodoMenus;
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
@DodoEntity(nameKey = "dodo.privilege.admin.data.FreeStateFile.entityKey", actions = { DodoAction.VIEW })
@DodoMenus(levelOne = @DodoMenu(nameKey = "dodo.privilege.admin.menuNameKey", sortSeq = 1), levelTwo = @DodoMenu(nameKey = "dodo.privilege.admin.data.menuNameKey", sortSeq = 4), levelThree = @DodoMenu(nameKey = "dodo.privilege.admin.data.FreeStateFile.menuNameKey", sortSeq = 4))
public class FreeStateFile extends BaseEntity {
    private static final long serialVersionUID = -4838958645030206041L;

    @DodoField(sortSeq = 0, nameKey = "dodo.privilege.admin.data.FreeStateFile.namekey.entityClassName", addable = false, editable = false, queryOnList = true)
    private String            entityClassName;

    @DodoField(sortSeq = 1, nameKey = "dodo.privilege.admin.data.FreeStateFile.namekey.entityName", addable = false, editable = false, queryOnList = true)
    private String            entityName;

    @DodoField(sortSeq = 2, nameKey = "dodo.privilege.admin.data.FreeStateFile.namekey.entityId", addable = false, editable = false, queryOnList = true)
    private String            entityId;

    @DodoField(sortSeq = 3, nameKey = "dodo.privilege.admin.data.FreeStateFile.namekey.fieldName", addable = false, editable = false, queryOnList = true)
    private String            fieldName;

    @DodoField(sortSeq = 4, nameKey = "dodo.privilege.admin.data.FreeStateFile.namekey.fieldShowName", addable = false, editable = false, queryOnList = true)
    private String            fieldShowName;

    @DodoField(sortSeq = 5, nameKey = "dodo.privilege.admin.data.FreeStateFile.namekey.extFieldName", addable = false, editable = false)
    private String            extFieldName;

    @DodoField(sortSeq = 6, nameKey = "dodo.privilege.admin.data.FreeStateFile.namekey.extFieldShowName", addable = false, editable = false)
    private String            extFieldShowName;

    @DodoField(sortSeq = 7, nameKey = "dodo.privilege.admin.data.FreeStateFile.namekey.fileStr", addable = false, editable = false, isFile = true)
    private String            fileStr;

    @DodoField(sortSeq = 8, nameKey = "dodo.privilege.admin.data.FreeStateFile.namekey.videoFileStr", addable = false, editable = false, isVideo = true)
    private String            videoFileStr;

    @DodoField(sortSeq = 9, nameKey = "dodo.privilege.admin.data.FreeStateFile.namekey.ossBucket", addable = false, editable = false)
    private String            ossBucket;

    public String getEntityClassName() {
        return entityClassName;
    }

    public String getEntityName() {
        return entityName;
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

    public void setEntityClassName(String entityClassName) {
        this.entityClassName = entityClassName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
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

    @Lob
    public String getVideoFileStr() {
        return videoFileStr;
    }

    public void setVideoFileStr(String videoFileStr) {
        this.videoFileStr = videoFileStr;
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

    @Column(length = 16)
    public String getOssBucket() {
        return ossBucket;
    }

    public void setOssBucket(String ossBucket) {
        this.ossBucket = ossBucket;
    }
}
