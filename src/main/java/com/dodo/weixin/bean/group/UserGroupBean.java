package com.dodo.weixin.bean.group;

import com.dodo.weixin.bean.ResultMsgBean;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class UserGroupBean extends ResultMsgBean{
	private Integer groupid;

	public Integer getGroupid() {
		return groupid;
	}

	public void setGroupid(Integer groupid) {
		this.groupid = groupid;
	}

	@Override
	public String toString() {
		return super.toString()+"\nUserGroupBean [groupid=" + groupid + "]";
	}
}
