package com.dodo.weixin.msg.request;

import org.w3c.dom.Document;

/**
 * 错误了
 * 
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class RequestMsgError extends RequestMsg {

	private RequestMsgError(RequestMsgHead head) {
		this.head = head;
	}

	@Override
	public void read(Document document) {
	}

	@Override
	public String toString() {
		return "RequestMsgError [head=" + head + "]";
	}
	public static String getMsgType(){
		return MSG_TYPE_ERROR;
	}
}
