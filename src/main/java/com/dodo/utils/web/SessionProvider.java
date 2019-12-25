package com.dodo.utils.web;

import java.io.Serializable;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public interface SessionProvider {
	public Serializable getAttribute(String name);

	public void setAttribute(String name, Serializable value);

	public String getSessionId();

	public void logout();
}
