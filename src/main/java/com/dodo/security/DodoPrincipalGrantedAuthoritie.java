package com.dodo.security;

import org.springframework.security.core.GrantedAuthority;
/**
 * 权限
 * 
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class DodoPrincipalGrantedAuthoritie implements GrantedAuthority{
	private static final long serialVersionUID = 6818749751889847768L;
	private String authority;
	@Override
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	public DodoPrincipalGrantedAuthoritie(String authority) {
		super();
		this.authority = authority;
	}
}
