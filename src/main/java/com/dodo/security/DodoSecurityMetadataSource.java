package com.dodo.security;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import com.dodo.utils.config.DodoFrameworkConfigUtil.DodoCommonConfigUtil;

/**
 * 系统全部 资源与权限 元数据源
 * 
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class DodoSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    private static final Logger        LOGGER             = LoggerFactory.getLogger(DodoSecurityMetadataSource.class);
    private String                     adminRootPath;

    public static Boolean              refreshSysMetadata = Boolean.TRUE;

    private DodoMetadataSourceProvider dataSourceProvider;

    public DodoMetadataSourceProvider getDataSourceProvider() {
        return dataSourceProvider;
    }

    public void setDataSourceProvider(DodoMetadataSourceProvider dataSourceProvider) {
        this.dataSourceProvider = dataSourceProvider;
    }

    private static Map<String, Collection<ConfigAttribute>> rightsMap = null;

    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    public boolean supports(Class<?> clazz) {
        return true;
    }

    private synchronized void loadConfigAttributeMap() {
        if (rightsMap == null || rightsMap.size() == 0 || refreshSysMetadata) {
            rightsMap = dataSourceProvider.loadConfigAttributeMap();
            adminRootPath = dataSourceProvider.getAdminRootPath();
            refreshSysMetadata = Boolean.FALSE;
        }
    }

    //返回所请求资源所需要的权限
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        LOGGER.info("requestUrl is {}", requestUrl);
        requestUrl = StringUtils.substringAfter(requestUrl, DodoCommonConfigUtil.viewRootPath);
        if (requestUrl.indexOf("?") != -1) {
            requestUrl = requestUrl.substring(0, requestUrl.indexOf("?"));
        }
        LOGGER.info("Real requestUrl is {}", requestUrl);
        if (rightsMap == null || rightsMap.size() == 0 || refreshSysMetadata) {
            loadConfigAttributeMap();
        }

        Collection<ConfigAttribute> configAttributes = rightsMap.get(requestUrl);
        if (configAttributes != null) {
            return configAttributes;
        }

        if (adminRootPath != null) {
            return rightsMap.get(adminRootPath);
        }

        return null;
    }
}
