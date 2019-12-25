package com.dodo.privilege.security;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.dodo.common.database.data.Record;
import com.dodo.common.database.hql.HqlHelper;
import com.dodo.common.framework.service.HqlHelperService;
import com.dodo.privilege.entity.monitor_2.log_1.LoginLog;
import com.dodo.utils.CommonUtil;
import com.dodo.utils.SpringUtil;

/**
 * <code>logout</code>方法在执行主动退出前被执行，用于记录日志
 * 
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class DodoLogoutHandler implements LogoutHandler {
    private HqlHelperService hqlHelperService;

    public void setHqlHelperService(HqlHelperService hqlHelperService) {
        this.hqlHelperService = hqlHelperService;
    }

    @Override
    public void logout(HttpServletRequest arg0, HttpServletResponse arg1, Authentication arg2) {
        HttpSession session = arg0.getSession(false);
        if (session != null) {
            HqlHelper helper = HqlHelper.queryFrom(LoginLog.class);
            helper.fetch("createDate", "logoutRemark").eq("sessionId", session.getId());
            Record logMap = hqlHelperService.getRecord(helper);
            if (logMap != null && StringUtils.isBlank((String) logMap.get("logoutRemark"))) {
                helper.update("logoutDate", new Date())
                        .update("logoutRemark", SpringUtil.getMessageBack("dodo.loginlog.logout.success", arg0))
                        .update("onlineTime",
                                CommonUtil.getOnlineTimeStr(new Date().getTime()
                                        - ((Date) logMap.get("createDate")).getTime()));
                hqlHelperService.update(helper);
            }
        }
    }
}
