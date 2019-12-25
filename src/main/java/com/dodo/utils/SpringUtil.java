package com.dodo.utils;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * 公共Bean获取、前台Bean获取、后台Bean获取、前台国际化信息获取、后台国际化信息获取
 * 
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class SpringUtil implements DisposableBean, ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {
    private static ApplicationContext applicationContext      = null;
    private static ApplicationContext applicationContextBack  = null;
    private static ApplicationContext applicationContextFront = null;
    private static LocaleResolver     localeResolver          = null;

    public void setApplicationContext(ApplicationContext paramApplicationContext) {
        applicationContext = paramApplicationContext;
    }

    public void destroy() {
        applicationContext = null;
        applicationContextBack = null;
        applicationContextFront = null;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> type) {
        return applicationContext.getBeansOfType(type);
    }

    public static Object getFrontBean(String name) {
        return applicationContextFront.getBean(name);
    }

    public static Object getBackBean(String name) {
        return applicationContextBack.getBean(name);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent arg0) {
        ApplicationContext c0 = arg0.getApplicationContext();
        if (c0.getDisplayName().contains("dodoBack")) {
            applicationContextBack = c0;
            try {
                localeResolver = (LocaleResolver) c0.getBean("localeResolver");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (c0.getDisplayName().contains("dodoFront")) {
            applicationContextFront = c0;
        }
    }

    // main 
    public static String getMessage(String name, Object[] objs, Locale locale, HttpServletRequest request) {
        return request == null ? applicationContext.getMessage(name, objs, locale) : applicationContext.getMessage(
                name, objs, RequestContextUtils.getLocaleResolver(request).resolveLocale(request));
    }

    public static String getMessage(String name, Locale locale) {
        return getMessage(name, new Object[0], locale, null);
    }

    public static String getMessage(String name, Object[] objs, Locale locale) {
        return getMessage(name, objs, locale, null);
    }

    public static String getMessage(String name, HttpServletRequest request) {
        return getMessage(name, new Object[0], null, request);
    }

    public static String getMessage(String name, Object[] objs, HttpServletRequest request) {
        return getMessage(name, objs, null, request);
    }

    // back 
    public static String getMessageBack(String name, Object[] objs, Locale locale, HttpServletRequest request) {
        if (request == null) {
            return applicationContextBack.getMessage(name, objs, locale == null ? Locale.CHINA : locale);
        } else {
            Locale localLocal = locale;
            try {
                LocaleResolver localeResolverLocal = RequestContextUtils.getLocaleResolver(request);
                if (localLocal == null && localeResolverLocal != null) {
                    localLocal = localeResolverLocal.resolveLocale(request);
                }
                if (localLocal == null && localeResolver != null) {
                    localLocal = localeResolver.resolveLocale(request);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (localLocal == null) {
                localLocal = Locale.CHINA;
            }
            return applicationContextBack.getMessage(name, objs, localLocal);
        }
    }

    public static String getMessageBack(String name) {
        return getMessageBack(name, new Object[0], null, null);
    }

    public static String getMessageBack(String name, Object[] objs) {
        return getMessageBack(name, objs, null, null);
    }

    public static String getMessageBack(String name, Object[] objs, Locale locale) {
        return getMessageBack(name, objs, locale, null);
    }

    public static String getMessageBack(String name, HttpServletRequest request) {
        return getMessageBack(name, new Object[0], null, request);
    }

    public static String getMessageBack(String name, Object[] objs, HttpServletRequest request) {
        return getMessageBack(name, objs, null, request);
    }

    // front
    public static String getMessageFront(String name, Object[] objs, Locale locale, HttpServletRequest request) {
        return request == null ? applicationContextFront.getMessage(name, objs, locale) : applicationContextFront
                .getMessage(name, objs, RequestContextUtils.getLocaleResolver(request).resolveLocale(request));
    }

    public static String getMessageFront(String name, Locale locale) {
        return getMessageFront(name, new Object[0], locale, null);
    }

    public static String getMessageFront(String name, Object[] objs, Locale locale) {
        return getMessageFront(name, objs, locale, null);
    }

    public static String getMessageFront(String name, HttpServletRequest request) {
        return getMessageFront(name, new Object[0], null, request);
    }

    public static String getMessageFront(String name, Object[] objs, HttpServletRequest request) {
        return getMessageFront(name, objs, null, request);
    }
}
