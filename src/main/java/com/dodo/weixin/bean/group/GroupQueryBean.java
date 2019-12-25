package com.dodo.weixin.bean.group;

import java.util.List;

import com.dodo.weixin.bean.ResultMsgBean;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class GroupQueryBean extends ResultMsgBean{
	private List<GroupBean> groups;

	public List<GroupBean> getGroups() {
		return groups;
	}

	public void setGroups(List<GroupBean> groups) {
		this.groups = groups;
	}

	@Override
	public String toString() {
		return super.toString()+"\nUserGroupQueryBean [groups=" + groups + "]";
	}
}
