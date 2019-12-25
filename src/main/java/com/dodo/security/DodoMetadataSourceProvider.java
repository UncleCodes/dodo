package com.dodo.security;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.access.ConfigAttribute;
/**
 * 系统全部 资源与权限 元数据源 提供者
 * 
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public interface DodoMetadataSourceProvider {
	Map<String, Collection<ConfigAttribute>> loadConfigAttributeMap();
	String getAdminRootPath();
}
