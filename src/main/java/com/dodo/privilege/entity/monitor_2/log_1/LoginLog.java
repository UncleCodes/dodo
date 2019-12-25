package com.dodo.privilege.entity.monitor_2.log_1;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

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
@DodoMenu(nameKey = "dodo.privilege.monitor.log.LoginLog.menuNameKey", level = DodoMenuLevel.LEVEL3, sortSeq = 2)
@DodoRight(nameKey = "dodo.privilege.monitor.log.LoginLog.entityKey")
@DodoCodeGenerator(daoGenerator = @DodoDaoGenerator, srvGenerator = @DodoSrvGenerator, actGenerator = @DodoActionGenerator(actions = {
        DodoAction.VIEW, DodoAction.CHART, DodoAction.EXPORT }))
public class LoginLog extends BaseEntity implements java.io.Serializable {
    private static final long serialVersionUID = -4135230699431949799L;

    @DodoField(sortSeq = 1, nameKey = "dodo.privilege.monitor.log.LoginLog.namekey.admin", addable = false, editable = false, queryOnList = true)
    private Admin             admin;

    @DodoField(sortSeq = 2, nameKey = "dodo.privilege.monitor.log.LoginLog.namekey.sessionId", addable = false)
    @DodoShowColumn(sortSeq = 0)
    private String            sessionId;

    @DodoField(sortSeq = 3, nameKey = "dodo.privilege.monitor.log.LoginLog.namekey.loginIp", addable = false, editable = false, queryOnList = true)
    private String            loginIp;

    @DodoField(sortSeq = 4, nameKey = "dodo.privilege.monitor.log.LoginLog.namekey.browserType", addable = false, editable = false)
    private String            browserType;

    @DodoField(sortSeq = 5, nameKey = "dodo.privilege.monitor.log.LoginLog.namekey.loginFlag", addable = false, editable = false)
    private Boolean           loginFlag;

    @DodoField(sortSeq = 6, nameKey = "dodo.privilege.monitor.log.LoginLog.namekey.loginFailReason", addable = false)
    private String            loginFailReason;

    @DodoField(sortSeq = 7, nameKey = "dodo.privilege.monitor.log.LoginLog.namekey.onlineTime", addable = false, editable = false)
    private String            onlineTime;

    @DodoField(sortSeq = 8, nameKey = "dodo.privilege.monitor.log.LoginLog.namekey.migrateSessionFlag", addable = false, editable = false)
    private Boolean           migrateSessionFlag;

    @DodoField(sortSeq = 9, nameKey = "dodo.privilege.monitor.log.LoginLog.namekey.oldSessionId", addable = false)
    private String            oldSessionId;

    @DodoField(sortSeq = 10, nameKey = "dodo.privilege.monitor.log.LoginLog.namekey.logoutDate", addable = false)
    private Date              logoutDate;

    @DodoField(sortSeq = 11, nameKey = "dodo.privilege.monitor.log.LoginLog.namekey.logoutRemark", addable = false)
    private String            logoutRemark;

    @Column(length = 16)
    public String getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(String onlineTime) {
        this.onlineTime = onlineTime;
    }

    public Date getLogoutDate() {
        return logoutDate;
    }

    @Column(length = 128)
    public String getLogoutRemark() {
        return logoutRemark;
    }

    public void setLogoutDate(Date logoutDate) {
        this.logoutDate = logoutDate;
    }

    public void setLogoutRemark(String logoutRemark) {
        this.logoutRemark = logoutRemark;
    }

    @Column(unique = true, length = 64)
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Column(length = 64)
    public String getBrowserType() {
        return browserType;
    }

    public void setBrowserType(String browserType) {
        this.browserType = browserType;
    }

    @Column(length = 64)
    public String getLoginIp() {
        return loginIp;
    }

    @OneToOne
    public Admin getAdmin() {
        return admin;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public Boolean getLoginFlag() {
        return loginFlag;
    }

    public void setLoginFlag(Boolean loginFlag) {
        this.loginFlag = loginFlag;
    }

    @Column(length = 128)
    public String getLoginFailReason() {
        return loginFailReason;
    }

    public void setLoginFailReason(String loginFailReason) {
        this.loginFailReason = loginFailReason;
    }

    public Boolean getMigrateSessionFlag() {
        return migrateSessionFlag;
    }

    @Column(length = 64)
    public String getOldSessionId() {
        return oldSessionId;
    }

    public void setMigrateSessionFlag(Boolean migrateSessionFlag) {
        this.migrateSessionFlag = migrateSessionFlag;
    }

    public void setOldSessionId(String oldSessionId) {
        this.oldSessionId = oldSessionId;
    }
}
