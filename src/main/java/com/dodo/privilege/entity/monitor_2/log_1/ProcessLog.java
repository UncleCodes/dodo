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
@DodoMenu(nameKey = "dodo.privilege.monitor.log.ProcessLog.menuNameKey", level = DodoMenuLevel.LEVEL3, sortSeq = 3)
@DodoRight(nameKey = "dodo.privilege.monitor.log.ProcessLog.entityKey")
@DodoCodeGenerator(daoGenerator = @DodoDaoGenerator, srvGenerator = @DodoSrvGenerator, actGenerator = @DodoActionGenerator(actions = {
        DodoAction.VIEW, DodoAction.CHART, DodoAction.EXPORT, DodoAction.DELETE }))
public class ProcessLog extends BaseEntity implements java.io.Serializable {
    private static final long serialVersionUID = -4135230699431949799L;

    @DodoField(sortSeq = 0, nameKey = "dodo.privilege.monitor.log.ProcessLog.namekey.servletPath", queryOnList = true)
    private String            servletPath;

    @DodoField(sortSeq = 1, nameKey = "dodo.privilege.monitor.log.ProcessLog.namekey.requestParameters", isRichText = true, listable = false)
    private String            requestParameters;

    @DodoField(sortSeq = 2, nameKey = "dodo.privilege.monitor.log.ProcessLog.namekey.requestIp", queryOnList = true)
    private String            requestIp;

    @DodoField(sortSeq = 3, nameKey = "dodo.privilege.monitor.log.ProcessLog.namekey.browserType", addable = false, editable = false)
    private String            browserType;

    @DodoField(sortSeq = 4, nameKey = "dodo.privilege.monitor.log.ProcessLog.namekey.requestHeaders", isRichText = true, listable = false)
    private String            requestHeaders;

    @DodoField(sortSeq = 5, nameKey = "dodo.privilege.monitor.log.ProcessLog.namekey.actionBean", queryOnList = true)
    private String            actionBean;

    @DodoField(sortSeq = 6, nameKey = "dodo.privilege.monitor.log.ProcessLog.namekey.actionMethod")
    private String            actionMethod;

    @DodoField(sortSeq = 7, nameKey = "dodo.privilege.monitor.log.ProcessLog.namekey.actionView", queryOnList = true)
    private String            actionView;

    @DodoField(sortSeq = 8, nameKey = "dodo.privilege.monitor.log.ProcessLog.namekey.requestBeginTime")
    private String            requestBeginTime;

    @DodoField(sortSeq = 9, nameKey = "dodo.privilege.monitor.log.ProcessLog.namekey.requestEndTime")
    private String            requestEndTime;

    @DodoField(sortSeq = 10, nameKey = "dodo.privilege.monitor.log.ProcessLog.namekey.totalTime")
    private String            totalTime;

    @DodoField(sortSeq = 11, nameKey = "dodo.privilege.monitor.log.ProcessLog.namekey.actionTime")
    private String            actionTime;

    @DodoField(sortSeq = 12, nameKey = "dodo.privilege.monitor.log.ProcessLog.namekey.logTime")
    private String            logTime;

    @DodoField(sortSeq = 13, nameKey = "dodo.privilege.monitor.log.ProcessLog.namekey.viewTime")
    private String            viewTime;

    @DodoField(sortSeq = 14, nameKey = "dodo.privilege.monitor.log.ProcessLog.namekey.admin")
    private Admin             admin;

    @DodoField(sortSeq = 15, nameKey = "dodo.privilege.monitor.log.ProcessLog.namekey.exceptionInfo", isTextArea = true, isEncode = false, listable = false)
    private String            exceptionInfo;

    public String getBrowserType() {
        return browserType;
    }

    public void setBrowserType(String browserType) {
        this.browserType = browserType;
    }

    public String getActionBean() {
        return actionBean;
    }

    public String getActionMethod() {
        return actionMethod;
    }

    public String getActionView() {
        return actionView;
    }

    public String getRequestBeginTime() {
        return requestBeginTime;
    }

    public String getRequestEndTime() {
        return requestEndTime;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public String getActionTime() {
        return actionTime;
    }

    public String getLogTime() {
        return logTime;
    }

    public String getViewTime() {
        return viewTime;
    }

    @OneToOne
    public Admin getAdmin() {
        return admin;
    }

    public void setActionBean(String actionBean) {
        this.actionBean = actionBean;
    }

    public void setActionMethod(String actionMethod) {
        this.actionMethod = actionMethod;
    }

    public void setActionView(String actionView) {
        this.actionView = actionView;
    }

    public void setRequestBeginTime(String requestBeginTime) {
        this.requestBeginTime = requestBeginTime;
    }

    public void setRequestEndTime(String requestEndTime) {
        this.requestEndTime = requestEndTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public void setActionTime(String actionTime) {
        this.actionTime = actionTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public void setViewTime(String viewTime) {
        this.viewTime = viewTime;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    @Lob
    public String getRequestParameters() {
        return requestParameters;
    }

    public void setRequestParameters(String requestParameters) {
        this.requestParameters = requestParameters;
    }

    @Column(length = 1024)
    public String getServletPath() {
        return servletPath;
    }

    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }

    @Lob
    public String getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(String requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public String getRequestIp() {
        return requestIp;
    }

    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp;
    }

    @Lob
    public String getExceptionInfo() {
        return exceptionInfo;
    }

    public void setExceptionInfo(String exceptionInfo) {
        this.exceptionInfo = exceptionInfo;
    }
}
