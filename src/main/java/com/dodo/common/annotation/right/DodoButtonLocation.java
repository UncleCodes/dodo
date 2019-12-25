package com.dodo.common.annotation.right;

/**
 * 按钮权限的位置<br/>
 * <strong>TOP-顶部<br/>
 * <strong>BOTTOM-底部<br/>
 * 
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public enum DodoButtonLocation {
	TOP("顶部",""),
	BOTTOM("底部","");
	private DodoButtonLocation(String name,String desc){
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
