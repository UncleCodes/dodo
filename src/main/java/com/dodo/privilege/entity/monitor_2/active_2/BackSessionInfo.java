package com.dodo.privilege.entity.monitor_2.active_2;

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
import com.dodo.common.annotation.menu.DodoMenu;
import com.dodo.common.annotation.menu.DodoMenuLevel;
import com.dodo.common.annotation.right.DodoButtonRight;
import com.dodo.common.annotation.right.DodoButtonRightEvent;
import com.dodo.common.annotation.right.DodoButtonRightModel;
import com.dodo.common.annotation.right.DodoRight;
import com.dodo.common.annotation.service.DodoSrvGenerator;
import com.dodo.common.framework.entity.BaseEntity;
import com.dodo.privilege.entity.admin_1.base_1.Admin;
import com.dodo.privilege.entity.monitor_2.log_1.LoginLog;

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
@DodoMenu(nameKey = "dodo.privilege.monitor.active.menuNameKey", level = DodoMenuLevel.LEVEL2, sortSeq = 2)
@DodoMenu(nameKey = "dodo.privilege.monitor.active.BackSessionInfo.menuNameKey", level = DodoMenuLevel.LEVEL3, sortSeq = 1)
@DodoRight(nameKey = "dodo.privilege.monitor.active.BackSessionInfo.entityKey")
@DodoCodeGenerator(daoGenerator = @DodoDaoGenerator, srvGenerator = @DodoSrvGenerator, actGenerator = @DodoActionGenerator(actions = {
        DodoAction.VIEW, DodoAction.CHART, DodoAction.EXPORT }))
@DodoButtonRight(nameKey = "dodo.privilege.monitor.active.BackSessionInfo.button.killone.namekey", model = DodoButtonRightModel.ROW, path = "/killone", event = DodoButtonRightEvent.AJAX)
@DodoButtonRight(nameKey = "dodo.privilege.monitor.active.BackSessionInfo.button.killall.namekey", model = DodoButtonRightModel.MODEL, path = "/killall", event = DodoButtonRightEvent.AJAX)
public class BackSessionInfo extends BaseEntity implements java.io.Serializable {
    private static final long serialVersionUID = 8726840843700240844L;

    @DodoField(sortSeq = -1, nameKey = "dodo.privilege.monitor.active.BackSessionInfo.namekey.admin", queryOnList = true)
    private Admin             admin;

    @DodoField(sortSeq = 0, nameKey = "dodo.privilege.monitor.active.BackSessionInfo.namekey.loginLog")
    private LoginLog          loginLog;

    @DodoField(sortSeq = 1, nameKey = "dodo.privilege.monitor.active.BackSessionInfo.namekey.beginTime")
    private Date              beginTime;

    @DodoField(sortSeq = 2, nameKey = "dodo.privilege.monitor.active.BackSessionInfo.namekey.userName", queryOnList = true)
    private String            userName;

    @DodoField(sortSeq = 3, nameKey = "dodo.privilege.monitor.active.BackSessionInfo.namekey.name")
    private String            name;

    @DodoField(sortSeq = 4, nameKey = "dodo.privilege.monitor.active.BackSessionInfo.namekey.email")
    private String            email;

    @DodoField(sortSeq = 5, nameKey = "dodo.privilege.monitor.active.BackSessionInfo.namekey.department")
    private String            department;

    @DodoField(sortSeq = 6, nameKey = "dodo.privilege.monitor.active.BackSessionInfo.namekey.loginIp")
    private String            loginIp;

    @DodoField(sortSeq = 7, nameKey = "dodo.privilege.monitor.active.BackSessionInfo.namekey.browserType")
    private String            browserType;

    @DodoField(sortSeq = 8, nameKey = "dodo.privilege.monitor.active.BackSessionInfo.namekey.sessionId")
    private String            sessionId;

    @OneToOne
    public Admin getAdmin() {
        return admin;
    }

    @OneToOne
    public LoginLog getLoginLog() {
        return loginLog;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public String getUserName() {
        return userName;
    }

    @Column(length = 16)
    public String getName() {
        return name;
    }

    @Column(length = 64)
    public String getEmail() {
        return email;
    }

    @Column(length = 32)
    public String getDepartment() {
        return department;
    }

    @Column(length = 128)
    public String getLoginIp() {
        return loginIp;
    }

    @Column(length = 64)
    public String getBrowserType() {
        return browserType;
    }

    @Column(length = 64)
    public String getSessionId() {
        return sessionId;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public void setLoginLog(LoginLog loginLog) {
        this.loginLog = loginLog;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public void setBrowserType(String browserType) {
        this.browserType = browserType;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}