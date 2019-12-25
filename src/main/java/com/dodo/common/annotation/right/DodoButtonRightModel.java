package com.dodo.common.annotation.right;

/**
 * 按钮权限的方式<br/>
 * <strong>ROW-数据行按钮</strong>,生成的按钮将出现在每一行记录上<br/>
 * <strong>MODEL-模块按钮</strong>,生成的按钮将出现在列表页面底端或顶端<strong>由{@link DodoButtonLocation}指定</strong>,模块级别<br/>
 * 
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public enum DodoButtonRightModel {
	ROW("数据行按钮",""),
	MODEL("模块按钮","");
	private DodoButtonRightModel(String name,String desc){
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
