package com.dodo.privilege.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;

import com.dodo.security.DodoAccessDeniedHandler;

/**
 * 之所以重新实现接口，是为了可以由配置文件中方便设置权限不足页面，以便程序跳转
 * 
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class DodoAccessDenyHandlerImpl implements DodoAccessDeniedHandler {
    private String accessDenyPage;

    @Override
    public void handle(HttpServletRequest arg0, HttpServletResponse arg1, AccessDeniedException arg2)
            throws IOException, ServletException {
        arg2.printStackTrace();
        arg1.sendRedirect(arg0.getContextPath() + getAccessDenyPage());
    }

    @Override
    public String getAccessDenyPage() {
        return accessDenyPage == null ? "" : accessDenyPage;
    }

    public void setAccessDenyPage(String accessDenyPage) {
        this.accessDenyPage = accessDenyPage;
    }
}
