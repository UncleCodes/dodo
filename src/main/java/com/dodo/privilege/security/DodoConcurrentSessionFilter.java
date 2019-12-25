package com.dodo.privilege.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import com.dodo.common.database.data.Record;
import com.dodo.common.database.hql.HqlHelper;
import com.dodo.common.framework.service.HqlHelperService;
import com.dodo.privilege.entity.monitor_2.log_1.LoginLog;
import com.dodo.utils.CommonUtil;
import com.dodo.utils.SpringUtil;

/**
 * 之所以重新实现接口，是为了在session被别人挤掉（用户在别的地方登陆[仅当配置了同一账号同一时间只允许保持一个回话]）的时候，记录日志
 * 
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class DodoConcurrentSessionFilter extends ConcurrentSessionFilter {
    private SessionRegistry sessionRegistry;

    public DodoConcurrentSessionFilter(SessionRegistry sessionRegistry,
            SessionInformationExpiredStrategy sessionInformationExpiredStrategy) {
        super(sessionRegistry, sessionInformationExpiredStrategy);
        this.sessionRegistry = sessionRegistry;
    }

    public DodoConcurrentSessionFilter(SessionRegistry sessionRegistry) {
        super(sessionRegistry);
        this.sessionRegistry = sessionRegistry;
    }

    private HqlHelperService hqlHelperService;

    public void setHqlHelperService(HqlHelperService hqlHelperService) {
        this.hqlHelperService = hqlHelperService;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain arg2) throws IOException,
            ServletException {
        try {
            HttpServletRequest request = (HttpServletRequest) req;
            HttpSession session = request.getSession(false);
            if (session != null) {
                SessionInformation info = sessionRegistry.getSessionInformation(session.getId());
                if (info != null && info.isExpired()) {
                    HqlHelper helper = HqlHelper.queryFrom(LoginLog.class);
                    helper.fetch("createDate", "logoutRemark").eq("sessionId", session.getId());
                    Record logMap = hqlHelperService.getRecord(helper);
                    if (logMap != null && StringUtils.isBlank((String) logMap.get("logoutRemark"))) {
                        helper.update("logoutDate", new Date())
                                .update("logoutRemark",
                                        SpringUtil.getMessageBack("dodo.loginlog.login.other.login", request))
                                .update("onlineTime",
                                        CommonUtil.getOnlineTimeStr(new Date().getTime()
                                                - ((Date) logMap.get("createDate")).getTime()));
                        hqlHelperService.update(helper);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.doFilter(req, res, arg2);
    }
}
