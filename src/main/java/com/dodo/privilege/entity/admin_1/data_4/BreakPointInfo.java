package com.dodo.privilege.entity.admin_1.data_4;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.hibernate.annotations.DynamicInsert;

import com.dodo.common.annotation.action.DodoAction;
import com.dodo.common.annotation.action.DodoEntity;
import com.dodo.common.annotation.field.DodoField;
import com.dodo.common.annotation.menu.DodoMenu;
import com.dodo.common.annotation.right.DodoRowRight;
import com.dodo.common.framework.entity.BaseEntity;
import com.dodo.privilege.entity.admin_1.base_1.Admin;

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
@DodoEntity(nameKey = "dodo.privilege.admin.data.BreakPointInfo.entityKey", actions = { DodoAction.VIEW,
        DodoAction.CHART, DodoAction.EXPORT }, levelOne = @DodoMenu(nameKey = "dodo.privilege.admin.menuNameKey", sortSeq = 1), levelTwo = @DodoMenu(nameKey = "dodo.privilege.admin.data.menuNameKey", sortSeq = 4), levelThree = @DodoMenu(nameKey = "dodo.privilege.admin.data.BreakPointInfo.menuNameKey", sortSeq = 3))
@DodoRowRight(entityProperty = "admin")
public class BreakPointInfo extends BaseEntity {
    private static final long serialVersionUID = -5940620771612943217L;

    @DodoField(sortSeq = 1, isPopup = true, nameKey = "dodo.privilege.admin.data.BreakPointInfo.namekey.admin")
    private Admin             admin;

    @DodoField(sortSeq = 2, nameKey = "dodo.privilege.admin.data.BreakPointInfo.namekey.entityName")
    private String            entityName;

    @DodoField(sortSeq = 3, nameKey = "dodo.privilege.admin.data.BreakPointInfo.namekey.entityShowName")
    private String            entityShowName;

    @DodoField(sortSeq = 4, nameKey = "dodo.privilege.admin.data.BreakPointInfo.namekey.fieldName")
    private String            fieldName;

    @DodoField(sortSeq = 5, nameKey = "dodo.privilege.admin.data.BreakPointInfo.namekey.fieldShowName")
    private String            fieldShowName;

    @DodoField(sortSeq = 6, nameKey = "dodo.privilege.admin.data.BreakPointInfo.namekey.dataId")
    private String            dataId;

    @DodoField(sortSeq = 7, nameKey = "dodo.privilege.admin.data.BreakPointInfo.namekey.fileId")
    private String            fileId;

    @DodoField(sortSeq = 8, nameKey = "dodo.privilege.admin.data.BreakPointInfo.namekey.fileName")
    private String            fileName;

    @DodoField(sortSeq = 9, nameKey = "dodo.privilege.admin.data.BreakPointInfo.namekey.fileSize")
    private String            fileSize;

    @DodoField(sortSeq = 10, nameKey = "dodo.privilege.admin.data.BreakPointInfo.namekey.chunkCount")
    private Integer           chunkCount;

    @DodoField(sortSeq = 11, nameKey = "dodo.privilege.admin.data.BreakPointInfo.namekey.chunkSize")
    private Integer           chunkSize;

    @DodoField(sortSeq = 12, nameKey = "dodo.privilege.admin.data.BreakPointInfo.namekey.currChunk", infoTipKey = "dodo.privilege.admin.data.BreakPointInfo.infoTip.currChunk")
    private Integer           currChunk;

    @DodoField(sortSeq = 64, nameKey = "dodo.privilege.admin.data.BreakPointInfo.namekey.sessionId")
    private String            sessionId;

    @Override
    public void onSave() {
        super.onSave();
        if (chunkCount == null) {
            chunkCount = 0;
        }
        if (chunkSize == null) {
            chunkSize = 0;
        }
        if (currChunk == null) {
            currChunk = 0;
        }
    }

    @OneToOne
    public Admin getAdmin() {
        return admin;
    }

    public String getDataId() {
        return dataId;
    }

    @Column(length = 128)
    public String getFileId() {
        return fileId;
    }

    @Column(length = 128)
    public String getFileName() {
        return fileName;
    }

    public Integer getChunkCount() {
        return chunkCount;
    }

    public Integer getChunkSize() {
        return chunkSize;
    }

    public Integer getCurrChunk() {
        return currChunk;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setChunkCount(Integer chunkCount) {
        this.chunkCount = chunkCount;
    }

    public void setChunkSize(Integer chunkSize) {
        this.chunkSize = chunkSize;
    }

    public void setCurrChunk(Integer currChunk) {
        this.currChunk = currChunk;
    }

    public String getEntityName() {
        return entityName;
    }

    @Column(length = 128)
    public String getFieldName() {
        return fieldName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Column(length = 64)
    public String getEntityShowName() {
        return entityShowName;
    }

    @Column(length = 64)
    public String getFieldShowName() {
        return fieldShowName;
    }

    public void setEntityShowName(String entityShowName) {
        this.entityShowName = entityShowName;
    }

    public void setFieldShowName(String fieldShowName) {
        this.fieldShowName = fieldShowName;
    }

    @Column(length = 64)
    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    @Column(length = 64)
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

}
