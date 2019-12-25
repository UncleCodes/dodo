package com.dodo.common.framework.interceptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

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
public class DodoAdminThemeInterceptor extends HandlerInterceptorAdapter {

    private static final String DODO_THEME_KEY = "dodo_theme";

    private String              defaultTheme;

    public String getDefaultTheme() {
        return defaultTheme;
    }

    public void setDefaultTheme(String defaultTheme) {
        this.defaultTheme = defaultTheme;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws ServletException {
        String dodo_theme = request.getParameter(DODO_THEME_KEY);
        if (StringUtils.isNotBlank(dodo_theme)) {
            WebUtil.addCookie(request, response, DODO_THEME_KEY, dodo_theme);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && StringUtils.isNotBlank(modelAndView.getViewName())) {
            String dodo_theme = request.getParameter(DODO_THEME_KEY);
            if (StringUtils.isBlank(dodo_theme)) {
                dodo_theme = WebUtil.getCookie(request, DODO_THEME_KEY);
            }
            if (StringUtils.isBlank(dodo_theme)) {
                dodo_theme = defaultTheme;
            }

            if (StringUtils.isNotBlank(dodo_theme)) {
                modelAndView.setViewName(new StringBuilder(dodo_theme).append("/").append(modelAndView.getViewName())
                        .toString());
            }

        }
    }
}