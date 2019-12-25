package com.dodo.security;

import org.springframework.security.web.access.AccessDeniedHandler;


/**
 * 扩展AccessDeniedHandler接口，是为了可以由配置文件中方便设置权限不足页面，以便程序跳转
 * 
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public interface DodoAccessDeniedHandler extends AccessDeniedHandler{
	public abstract String getAccessDenyPage();
}
