package com.dodo.common.framework.interceptor;

import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.propertyeditors.LocaleEditor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class DodoAdminLocaleInterceptor extends HandlerInterceptorAdapter {
    private String defaultLocale;

    public String getDefaultLocale() {
        return defaultLocale;
    }

    public void setDefaultLocale(String defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws ServletException {
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        if (localeResolver == null) {
            throw new IllegalStateException("No LocaleResolver found In Dodo FrameWork!");
        }

        String clientlanguage = request.getParameter("clientlang");
        if (StringUtils.isNotBlank(clientlanguage)) {
            LocaleEditor localeEditor = new LocaleEditor();
            localeEditor.setAsText(clientlanguage);
            localeResolver.setLocale(request, response, (Locale) localeEditor.getValue());
        } else {
            Locale locale = localeResolver.resolveLocale(request);
            if (clientlanguage != null || locale == null || StringUtils.isBlank(locale.toString())) {
                LocaleEditor localeEditor = new LocaleEditor();
                localeEditor.setAsText(defaultLocale);
                localeResolver.setLocale(request, response, (Locale) localeEditor.getValue());
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        if (localeResolver == null) {
            throw new IllegalStateException("No LocaleResolver found In Dodo FrameWork!");
        }
        if (modelAndView != null) {
            Locale locale = localeResolver.resolveLocale(request);
            modelAndView.getModelMap().put("clientlanguage", locale.toString());
            modelAndView.getModelMap().put("clientlanguage_lan", locale.getLanguage());
            modelAndView.getModelMap().put("clientlanguage_con", locale.getCountry());
        }
    }
}