package com.dodo.common.annotation.right;

/**
 * 按钮权限的访问方式<br/>
 * <strong>URL-直接打开配置的URL<br/>
 * <strong>AJAX-ajax方式访问<br/>
 * 
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public enum DodoButtonRightEvent {
	URL("Url访问模式",""),
	AJAX("ajax模式","");
	private DodoButtonRightEvent(String name,String desc){
		this.name = name;
		this.desc = desc;
	}
	private String name;
	private String desc;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
