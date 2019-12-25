package com.dodo.weixin.bean.group;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class GroupBean{
	private Integer id;
	private String name;
	private int count;
	public Integer getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public int getCount() {
		return count;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setCount(int count) {
		this.count = count;
	}
	@Override
	public String toString() {
		return "UserGroupBean [id=" + id + ", name=" + name + ", count="
				+ count + "]";
	}
}
