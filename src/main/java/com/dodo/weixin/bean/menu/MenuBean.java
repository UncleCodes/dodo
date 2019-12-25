package com.dodo.weixin.bean.menu;

import java.util.ArrayList;
import java.util.List;

import com.dodo.weixin.exception.WeixinException;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class MenuBean {
	public enum MenuType{
		click,view;
	}
	private MenuType type;
	private String name;
	private String key;
	private String url;
	private List<MenuBean> sub_button = new ArrayList<MenuBean>(5);
	public MenuType getType() {
		return type;
	}
	public String getName() {
		return name;
	}
	public String getKey() {
		return key;
	}
	public List<MenuBean> getSub_button() {
		return sub_button;
	}
	public void setType(MenuType type) {
		this.type = type;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public void setSub_button(List<MenuBean> sub_button) {
		this.sub_button = sub_button;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void addSubMenu(MenuBean menu) throws WeixinException{
		if(sub_button.size() < 5){
			sub_button.add(menu);
		}else{
			throw new WeixinException("sub menu max count 5");
		}
	}
	
	
	public MenuBean() {
		super();
	}
	
	public MenuBean(MenuType type, String name, String key, String url) {
		super();
		this.type = type;
		this.name = name;
		this.key = key;
		this.url = url;
	}
	@Override
	public String toString() {
		return "MenuBean [type=" + type + ", name=" + name + ", key=" + key
				+ ", url=" + url + ", sub_button=" + sub_button + "]";
	}
}
