package com.dodo.weixin.bean.user;

import com.dodo.weixin.bean.ResultMsgBean;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class SubscribeUserList extends ResultMsgBean{
	private Integer total;
	private Integer count;
	private String next_openid;
	private SubscribeUserListData data;
	public Integer getTotal() {
		return total;
	}
	public Integer getCount() {
		return count;
	}
	public String getNext_openid() {
		return next_openid;
	}
	public SubscribeUserListData getData() {
		return data;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public void setNext_openid(String next_openid) {
		this.next_openid = next_openid;
	}
	public void setData(SubscribeUserListData data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return super.toString()+"\nSubscribeUserList [total=" + total + ", count=" + count
				+ ", next_openid=" + next_openid + ", data=" + data + "]";
	}
}
