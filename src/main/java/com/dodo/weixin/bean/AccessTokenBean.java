package com.dodo.weixin.bean;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class AccessTokenBean extends ResultMsgBean{
	private String access_token;
	private int expires_in;
	public String getAccess_token() {
		return access_token;
	}
	public int getExpires_in() {
		return expires_in;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}
	@Override
	public String toString() {
		return super.toString()+"\nAccessTokenBean [access_token=" + access_token
				+ ", expires_in=" + expires_in + "]";
	}
}
