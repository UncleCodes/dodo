package com.dodo.common.framework.bean.menu;

import java.io.Serializable;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class DodoMenuExt implements Serializable{
	private static final long serialVersionUID = 548687588985368043L;
	private String menuLink;
	private String topMenuLink;
	
	public DodoMenuExt(String menuLink,String topMenuLink) {
		super();
		this.menuLink = menuLink;
		this.topMenuLink = topMenuLink;
	}
	public String getMenuLink() {
		return menuLink;
	}
	public String getTopMenuLink() {
		return topMenuLink;
	}
	public void setMenuLink(String menuLink) {
		this.menuLink = menuLink;
	}
	public void setTopMenuLink(String topMenuLink) {
		this.topMenuLink = topMenuLink;
	}
	@Override
	public String toString() {
		return "DodoMenuExt [menuLink=" + menuLink + ", topMenuLink="
				+ topMenuLink + "]";
	}
}
