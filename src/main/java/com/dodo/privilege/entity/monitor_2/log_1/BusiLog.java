package com.dodo.privilege.entity.monitor_2.log_1;

import javax.persistence.Column;
import javax.persistence.Entity;
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
import com.dodo.common.annotation.service.DodoSrvGenerator;
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
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DodoMenu(nameKey = "dodo.privilege.monitor.menuNameKey", level = DodoMenuLevel.LEVEL1, sortSeq = 2)
@DodoMenu(nameKey = "dodo.privilege.monitor.log.menuNameKey", level = DodoMenuLevel.LEVEL2, sortSeq = 1)
@DodoMenu(nameKey = "dodo.privilege.monitor.log.BusiLog.menuNameKey", level = DodoMenuLevel.LEVEL3, sortSeq = 1)
@DodoRight(nameKey = "dodo.privilege.monitor.log.BusiLog.entityKey")
@DodoCodeGenerator(daoGenerator = @DodoDaoGenerator, srvGenerator = @DodoSrvGenerator, actGenerator = @DodoActionGenerator(actions = {
        DodoAction.VIEW, DodoAction.CHART, DodoAction.DELETE, DodoAction.EXPORT }))
public class BusiLog extends BaseEntity implements java.io.Serializable {
    private static final long serialVersionUID = -4135230699431949799L;

    @DodoField(sortSeq = 0, nameKey = "dodo.privilege.monitor.log.BusiLog.namekey.entityClassName", addable = false, editable = false)
    private String            entityClassName;

    @DodoField(sortSeq = 1, nameKey = "dodo.privilege.monitor.log.BusiLog.namekey.entityName", addable = false, editable = false)
    private String            entityName;

    @DodoField(sortSeq = 2, nameKey = "dodo.privilege.monitor.log.BusiLog.namekey.entityId", addable = false, editable = false)
    private String            entityId;

    @DodoField(sortSeq = 3, nameKey = "dodo.privilege.monitor.log.BusiLog.namekey.updateType", addable = false, editable = false, queryOnList = true)
    private String            updateType;

    @DodoField(sortSeq = 4, nameKey = "dodo.privilege.monitor.log.BusiLog.namekey.updatePerson", addable = false, editable = false, queryOnList = true)
    private Admin             updatePerson;

    @DodoField(sortSeq = 5, nameKey = "dodo.privilege.monitor.log.BusiLog.namekey.updatePersonIp", addable = false, editable = false)
    private String            updatePersonIp;

    @DodoField(sortSeq = 6, nameKey = "dodo.privilege.monitor.log.BusiLog.namekey.updateBrowserType", addable = false, editable = false)
    private String            updateBrowserType;

    @DodoField(sortSeq = 7, nameKey = "dodo.privilege.monitor.log.BusiLog.namekey.modifyLog", addable = false, isRichText = true, listable = false)
    private String            modifyLog;

    public String getEntityClassName() {
        return entityClassName;
    }

    @Column(length = 32)
    public String getEntityId() {
        return entityId;
    }

    @OneToOne
    public Admin getUpdatePerson() {
        return updatePerson;
    }

    @Lob
    public String getModifyLog() {
        return modifyLog;
    }

    public void setEntityClassName(String entityClassName) {
        this.entityClassName = entityClassName;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public void setUpdatePerson(Admin updatePerson) {
        this.updatePerson = updatePerson;
    }

    public void setModifyLog(String modifyLog) {
        this.modifyLog = modifyLog;
    }

    @Column(length = 20)
    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    @Column(length = 32)
    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    @Column(length = 32)
    public String getUpdatePersonIp() {
        return updatePersonIp;
    }

    @Column(length = 64)
    public String getUpdateBrowserType() {
        return updateBrowserType;
    }

    public void setUpdatePersonIp(String updatePersonIp) {
        this.updatePersonIp = updatePersonIp;
    }

    public void setUpdateBrowserType(String updateBrowserType) {
        this.updateBrowserType = updateBrowserType;
    }

    @Override
    public String toString() {
        return "BusiLog [entityClassName=" + entityClassName + ", entityName=" + entityName + ", entityId=" + entityId
                + ", updateType=" + updateType + ", updatePerson=" + updatePerson + ", updatePersonIp="
                + updatePersonIp + ", updateBrowserType=" + updateBrowserType + ", modifyLog=" + modifyLog + "]";
    }
}
