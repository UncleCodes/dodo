package com.dodo.utils.web;

import java.io.Serializable;

import javax.servlet.http.HttpSession;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class HttpSessionProvider implements SessionProvider {

	public Serializable getAttribute(String name) {
		HttpSession session = WebUtil.getSession();
		if (session != null) {
			return (Serializable) session.getAttribute(name);
		} else {
			return null;
		}
	}

	public void setAttribute(String name, Serializable value) {
		HttpSession session = WebUtil.getSession();
		session.setAttribute(name, value);
	}

	public String getSessionId() {
		return WebUtil.getSession().getId();
	}

	public void logout() {
		HttpSession session = WebUtil.getSession();
		if (session != null) {
			session.invalidate();
		}
	}
}
