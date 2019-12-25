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
public class GroupCreateResultBean extends ResultMsgBean{
	private GroupBean group;

	public GroupBean getGroup() {
		return group;
	}

	public void setGroup(GroupBean group) {
		this.group = group;
	}

	@Override
	public String toString() {
		return super.toString()+"\nUserGroupCreateResultBean [group=" + group + "]";
	}
}
