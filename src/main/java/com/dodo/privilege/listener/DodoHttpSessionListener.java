package com.dodo.privilege.listener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.lang3.StringUtils;

import com.dodo.common.database.data.Record;
import com.dodo.common.database.hql.HqlHelper;
import com.dodo.common.framework.service.HqlHelperService;
import com.dodo.privilege.entity.monitor_2.active_2.BackSessionInfo;
import com.dodo.privilege.entity.monitor_2.log_1.LoginLog;
import com.dodo.privilege.security.DodoSavedRequestAwareAuthenticationSuccessHandler;
import com.dodo.utils.CommonUtil;
import com.dodo.utils.SpringUtil;

/**
 * 之所以重新实现方法，是为了在session超时的时候，记录日志 该监听器仅仅处理后台session，性能影响忽略不计
 * 
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class DodoHttpSessionListener implements HttpSessionListener {
    private HqlHelperService                hqlHelperService;

    private static Map<String, HttpSession> allLoginBackSessions = new HashMap<String, HttpSession>();

    @Override
    public void sessionCreated(HttpSessionEvent se) {
    }

    public static void registNewLoginBackSession(HttpSession session) {
        allLoginBackSessions.put(session.getId(), session);
    }

    public static void unRegistLoginBackSession(String sessionId) {
        allLoginBackSessions.remove(sessionId);
    }

    public static Map<String, HttpSession> getAllLoginBackSessions() {
        return allLoginBackSessions;
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        if (session != null
                && session.getAttribute(DodoSavedRequestAwareAuthenticationSuccessHandler.BACK_SESSION_KEY) != null) {
            if (hqlHelperService == null) {
                hqlHelperService = (HqlHelperService) SpringUtil.getBean("hqlHelperServiceImpl");
            }

            // 清除内存中缓存的 HttpSession
            unRegistLoginBackSession(session.getId());

            HqlHelper helper = HqlHelper.queryFrom(LoginLog.class);
            helper.fetch("createDate", "logoutRemark").eq("sessionId", session.getId());
            Record logMap = hqlHelperService.getRecord(helper);
            if (logMap != null && StringUtils.isBlank((String) logMap.get("logoutRemark"))) {
                helper.update("logoutDate", new Date())
                        .update("logoutRemark", SpringUtil.getMessageBack("dodo.loginlog.login.time.out"))
                        .update("onlineTime",
                                CommonUtil.getOnlineTimeStr(new Date().getTime()
                                        - ((Date) logMap.get("createDate")).getTime()));
                hqlHelperService.update(helper);
            }

            hqlHelperService.delete(helper.resetQueryFrom(BackSessionInfo.class).eq("sessionId", session.getId()));
        }
    }
}
