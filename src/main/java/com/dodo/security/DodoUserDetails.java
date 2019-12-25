package com.dodo.security;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * 扩展 UserDetails 接口
 * 
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public interface DodoUserDetails extends UserDetails{
	public boolean hasFieldRight(String fieldRightCode);
	public boolean hasRight(String rightCode);
}
