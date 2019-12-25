package com.dodo.weixin.bean.qrcode;

import com.dodo.weixin.bean.ResultMsgBean;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class QrCodeCreateResult extends ResultMsgBean{
	private String ticket;
	private Integer expire_seconds;
	private String url;
	public String getTicket() {
		return ticket;
	}
	public Integer getExpire_seconds() {
		return expire_seconds;
	}
	public String getUrl() {
		return url;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public void setExpire_seconds(Integer expire_seconds) {
		this.expire_seconds = expire_seconds;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public String toString() {
		return super.toString()+"\nQrCodeCreateResult [ticket=" + ticket + ", expire_seconds="
				+ expire_seconds + ", url=" + url + "]";
	}
}
