package com.dodo.weixin.msg.custome.music;

import com.dodo.weixin.msg.custome.ResponseCustome;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class ResponseCustomeMusic implements ResponseCustome{
	private String touser;
	private String msgtype;
	private ResponseCustomeMusicContent music;

	public ResponseCustomeMusicContent getMusic() {
		return music;
	}

	public void setMusic(ResponseCustomeMusicContent music) {
		this.music = music;
	}

	public String getTouser() {
		return touser;
	}

	public String getMsgtype() {
		return msgtype;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	@Override
	public String toString() {
		return "ResponseCustomeText [touser=" + touser + ", msgtype=" + msgtype
				+ ", music=" + music + "]";
	}
}
