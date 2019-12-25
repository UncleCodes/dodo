package com.dodo.privilege.listener;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.dodo.common.database.hql.HqlHelper;
import com.dodo.common.framework.service.HqlHelperService;
import com.dodo.privilege.entity.admin_1.base_1.Admin;
import com.dodo.utils.http.HttpUtils;
import com.dodo.utils.web.WebUtil;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
@Component("adminSecurityListener")
public class AdminSecurityListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    private HqlHelperService hqlHelperService;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent arg0) {
        Authentication authentication = (Authentication) arg0.getSource();
        Admin admin = (Admin) authentication.getPrincipal();
        HqlHelper helper = HqlHelper.queryFrom(Admin.class);
        helper.update("lockedDate", null).update("loginFailureCount", 0).update("isAccountLocked", Boolean.FALSE)
                .update("loginIp", HttpUtils.getRemoteAddr(WebUtil.getRequest())).update("loginDate", new Date())
                .eq("id", admin.getId());

        hqlHelperService.update(helper);
    }
}
