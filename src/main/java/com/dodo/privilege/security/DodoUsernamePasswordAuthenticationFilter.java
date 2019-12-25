package com.dodo.privilege.security;

import java.io.Serializable;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.dodo.common.captcha.octo.CaptchaUtil;
import com.dodo.common.database.data.Record;
import com.dodo.common.database.hql.HqlHelper;
import com.dodo.common.framework.service.HqlHelperService;
import com.dodo.privilege.entity.admin_1.base_1.Admin;
import com.dodo.privilege.entity.monitor_2.log_1.LoginLog;
import com.dodo.utils.SpringUtil;
import com.dodo.utils.config.DodoFrameworkConfigUtil.DodoCommonConfigUtil;
import com.dodo.utils.http.HttpUtils;

/**
 * 用户登录的时候的自定义处理类 判断用户名、验证码等
 * 
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class DodoUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    public static final String USERNAME                   = "j_username";
    public static final String PASSWORD                   = "j_password";
    public static final String OLD_SESSION_ID             = "OLD_SESSION";
    public static final String LOGIN_SALT_IN_SESSION      = "LOGIN_SALT_IN_SESSION";
    public static final String LOGIN_SALT_DATE_IN_SESSION = "LOGIN_SALT_DATE_IN_SESSION";

    private HqlHelperService   hqlHelperService;

    public void setHqlHelperService(HqlHelperService hqlHelperService) {
        this.hqlHelperService = hqlHelperService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(SpringUtil.getMessageBack("dodo.loginlog.login.method.error",
                    request) + request.getMethod());
        }
        // 验证码
        if (!CaptchaUtil.validateJCaptcha(request)) {
            throw new AuthenticationServiceException(SpringUtil.getMessageBack("dodo.loginlog.login.validate.error",
                    request));
        }
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new AuthenticationServiceException(SpringUtil.getMessageBack("dodo.loginlog.login.salt.error",
                    request));
        }

        // 动态密钥是否过期
        Object saltDate = session.getAttribute(LOGIN_SALT_DATE_IN_SESSION);
        Object loginSalt = session.getAttribute(LOGIN_SALT_IN_SESSION);
        if (saltDate == null
                || loginSalt == null
                || new Date().after(DateUtils.addSeconds((Date) saltDate,
                        DodoCommonConfigUtil.passwordDynamicSaltTime.intValue()))) {
            throw new AuthenticationServiceException(SpringUtil.getMessageBack("dodo.loginlog.login.salt.error",
                    request));
        }

        String username = obtainUsername(request);
        String password = obtainPassword(request);

        username = username.trim();

        HqlHelper helper = HqlHelper.queryFrom(Admin.class);
        helper.fetch("id", "isAccountLocked", "adminPassword", "loginFailureCount").eq("username", username);

        Record admin = hqlHelperService.getRecord(helper);
        // 用户名
        if (admin == null) {
            throw new AuthenticationServiceException(SpringUtil.getMessageBack("dodo.loginlog.login.upn", request));
        }
        Boolean isAccountLocked = admin.get("isAccountLocked");
        Serializable id = admin.get("id");
        String adminPassword = admin.get("adminPassword");
        Integer loginFailureCount = admin.get("loginFailureCount");

        String sessionId = session.getId();

        // 密码
        if (!DigestUtils.md5Hex(adminPassword + loginSalt).equals(password)) {
            helper.resetQueryFrom(Admin.class).update("loginFailureCount", loginFailureCount + 1);
            if (loginFailureCount + 1 >= DodoCommonConfigUtil.loginFailureLockCount + 1) {
                helper.update("isAccountLocked", Boolean.TRUE).update("lockedDate", new Date());
            }
            helper.eq("id", id);
            hqlHelperService.update(helper);

            try {
                LoginLog loginLog = new LoginLog();
                loginLog.setLoginIp(HttpUtils.getRemoteAddr(request));
                loginLog.setAdmin(hqlHelperService.load(id, Admin.class));
                loginLog.setLoginFailReason(SpringUtil
                        .getMessageBack("dodo.loginlog.login.log.input.password", request)
                        + "-->"
                        + password
                        + "<--"
                        + SpringUtil.getMessageBack("dodo.loginlog.login.log.wrong", request)
                        + ","
                        + SpringUtil.getMessageBack("dodo.loginlog.login.log.wrong.times", request)
                        + ":"
                        + (loginFailureCount + 1));
                loginLog.setLoginFlag(Boolean.FALSE);
                loginLog.setBrowserType(HttpUtils.getBrowser(request));
                loginLog.setSessionId(sessionId);
                hqlHelperService.save(loginLog);
            } catch (Exception e) {
                e.printStackTrace();
            }

            throw new AuthenticationServiceException(SpringUtil.getMessageBack("dodo.loginlog.login.upn", request));
        }

        // 账户锁定
        if (isAccountLocked) {
            try {
                LoginLog loginLog = new LoginLog();
                loginLog.setLoginIp(HttpUtils.getRemoteAddr(request));
                loginLog.setAdmin(hqlHelperService.load(id, Admin.class));
                loginLog.setLoginFailReason(SpringUtil.getMessageBack("dodo.loginlog.login.account.locked", request));
                loginLog.setLoginFlag(Boolean.FALSE);
                loginLog.setBrowserType(HttpUtils.getBrowser(request));
                loginLog.setSessionId(sessionId);
                hqlHelperService.save(loginLog);
            } catch (Exception e) {
                e.printStackTrace();
            }

            throw new AuthenticationServiceException(SpringUtil.getMessageBack("dodo.loginlog.login.account.locked",
                    request));
        }

        session.removeAttribute(LOGIN_SALT_DATE_IN_SESSION);
        session.removeAttribute(LOGIN_SALT_IN_SESSION);
        session.setAttribute(OLD_SESSION_ID, sessionId);
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username,
                adminPassword);
        setDetails(request, authRequest);
        Authentication authentication = this.getAuthenticationManager().authenticate(authRequest);
        return authentication;
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        Object obj = request.getParameter(USERNAME);
        return null == obj ? "" : obj.toString();
    }

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        Object obj = request.getParameter(PASSWORD);
        return null == obj ? "" : obj.toString();
    }
}
