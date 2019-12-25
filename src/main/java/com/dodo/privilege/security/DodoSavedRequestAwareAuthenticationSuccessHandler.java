package com.dodo.privilege.security;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.dodo.common.database.data.Record;
import com.dodo.common.database.hql.HqlHelper;
import com.dodo.common.framework.service.HqlHelperService;
import com.dodo.privilege.entity.admin_1.base_1.Admin;
import com.dodo.privilege.entity.monitor_2.active_2.BackSessionInfo;
import com.dodo.privilege.entity.monitor_2.log_1.LoginLog;
import com.dodo.privilege.listener.DodoHttpSessionListener;
import com.dodo.utils.http.HttpUtils;

/**
 * 之所以重新实现方法，是为了在session固话防护完成时，记录用户确定登录状态
 * 
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class DodoSavedRequestAwareAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    public static String     BACK_SESSION_KEY   = "B_A_C_K_S";
    public static byte[]     BACK_SESSION_VALUE = new byte[0];

    private HqlHelperService hqlHelperService;

    private SessionRegistry  sessionRegistry;

    public void setHqlHelperService(HqlHelperService hqlHelperService) {
        this.hqlHelperService = hqlHelperService;
    }

    public void setSessionRegistry(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    private void initBackSessionInfos() {
        HqlHelper helper = HqlHelper.queryFrom(BackSessionInfo.class);
        Date createDate = null;
        String loginIp = null;
        String browserType = null;
        try {
            hqlHelperService.delete(helper);
            List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
            Admin admin = null;
            for (Object obj : allPrincipals) {
                admin = (Admin) obj;
                List<SessionInformation> sessionInfos = sessionRegistry.getAllSessions(admin, false);
                for (SessionInformation sessionInformation : sessionInfos) {
                    if (!sessionInformation.isExpired()) {
                        helper.resetQueryFrom(LoginLog.class).fetch("createDate", "loginIp", "browserType", "id")
                                .eq("sessionId", sessionInformation.getSessionId());
                        Record log = hqlHelperService.getRecord(helper);
                        if (log != null) {
                            createDate = log.get("createDate");
                            loginIp = log.get("loginIp");
                            browserType = log.get("browserType");
                            BackSessionInfo backSessionInfo = new BackSessionInfo();
                            backSessionInfo.setAdmin(admin);
                            backSessionInfo.setBeginTime(createDate);
                            backSessionInfo.setBrowserType(browserType);
                            backSessionInfo.setDepartment(admin.getDepartment());
                            backSessionInfo.setEmail(admin.getEmail());
                            backSessionInfo.setLoginIp(loginIp);
                            backSessionInfo.setLoginLog(hqlHelperService.load((Serializable) log.get("id"),
                                    LoginLog.class));
                            backSessionInfo.setName(admin.getName());
                            backSessionInfo.setSessionId(sessionInformation.getSessionId());
                            backSessionInfo.setUserName(admin.getUsername());
                            hqlHelperService.save(backSessionInfo);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {
        super.onAuthenticationSuccess(request, response, authentication);
        try {
            HttpSession session = request.getSession(false);
            Object oldSessionid = session.getAttribute(DodoUsernamePasswordAuthenticationFilter.OLD_SESSION_ID);
            LoginLog loginLog = new LoginLog();
            loginLog.setLoginIp(HttpUtils.getRemoteAddr(request));
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                loginLog.setAdmin((Admin) auth.getPrincipal());
            }
            loginLog.setLoginFlag(Boolean.TRUE);
            loginLog.setBrowserType(HttpUtils.getBrowser(request));
            loginLog.setSessionId(session.getId());
            loginLog.setOldSessionId(oldSessionid == null ? "" : oldSessionid.toString());
            loginLog.setMigrateSessionFlag(Boolean.TRUE);
            hqlHelperService.save(loginLog);

            HqlHelper helper = HqlHelper.queryFrom(Admin.class);
            helper.update("browserType", HttpUtils.getBrowser(request)).eq("id",
                    ((Admin) authentication.getPrincipal()).getId());
            hqlHelperService.update(helper);

            // 用于区分后台固化后session，后台固化session需要记录超时退出日志 以提高性能
            session.setAttribute(BACK_SESSION_KEY, BACK_SESSION_VALUE);
            DodoHttpSessionListener.registNewLoginBackSession(session);
            initBackSessionInfos();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
