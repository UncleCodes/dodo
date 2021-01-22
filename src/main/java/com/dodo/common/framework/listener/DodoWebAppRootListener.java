package com.dodo.common.framework.listener;

import java.io.File;
import java.text.MessageFormat;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

/**
 * 之所不使用<code>org.springframework.web.util.WebAppRootListener</code>，是为了解决以下问题<br/>
 * 1、每个项目的<code>webAppRootKey</code>都需要单独在<code>web.xml</code>中指定<br/>
 * 2、每个应用在Java代码中获取变量的时候，都需要写死<code>webAppRootKey</code><br/>
 * 3、如果由于程序员疏忽，同一Web服务器中的不同应用配置了相同的<code>webAppRootKey</code>则会导致系统错乱<br/>
 * <br/>
 * <code>DodoWebAppRootListener</code>解决了以上问题<br/>
 * 1、<code>web.xml</code>中无需配置<code>webAppRootKey</code><br/>
 * 2、无需在程序中写死<code>webAppRootKey</code><br/>
 * 3、多应用配置不会错乱
 * 
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class DodoWebAppRootListener implements ServletContextListener {
    public static String WEB_APP_ROOT_KEY = "DODO_WEBAPP_ROOT_{0}";
    public static String WEB_APP_NAME_KEY = "DODO_WEBAPP_NAME_{0}";

    public void contextInitialized(ServletContextEvent event) {
        ServletContext servletContext = event.getServletContext();
        Assert.notNull(servletContext, "ServletContext must not be null");
        String rootPath = servletContext.getRealPath("/");
        if (rootPath == null) {
            throw new IllegalStateException("Cannot set web app root system property when WAR file is not expanded");
        }
        // web app root path
        WEB_APP_ROOT_KEY = MessageFormat.format(WEB_APP_ROOT_KEY, DigestUtils.md5Hex(rootPath));
        System.setProperty(WEB_APP_ROOT_KEY, rootPath);
        servletContext.log("Set web app root system property: '" + WEB_APP_ROOT_KEY + "' = [" + rootPath + "]");

        // web app name
        String webappName = StringUtils.substringBeforeLast(rootPath, File.separator);
        webappName = StringUtils.substringAfterLast(webappName, File.separator);
        WEB_APP_NAME_KEY = MessageFormat.format(WEB_APP_NAME_KEY, DigestUtils.md5Hex(rootPath));
        System.setProperty(WEB_APP_NAME_KEY, webappName);
        servletContext.log("Set web app name system property: '" + WEB_APP_NAME_KEY + "' = [" + webappName + "]");
    }

    public void contextDestroyed(ServletContextEvent event) {
        ServletContext servletContext = event.getServletContext();
        Assert.notNull(servletContext, "ServletContext must not be null");
        System.getProperties().remove(WEB_APP_ROOT_KEY);
    }
}
