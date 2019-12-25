package com.dodo.weixin.msg.request;

import org.w3c.dom.Document;

import com.dodo.weixin.msg.WeixinXmlTagName;
 
/**
 * 文本消息
 * 
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class RequestMsgText extends RequestMsg {

	// 文本消息内容
	private String content;
	// 消息id，64位整型
	private String msgId;
	
	private RequestMsgText(RequestMsgHead head) {
		this.head = head;
	}

	@Override
	public void read(Document document) {
		this.content = document.getElementsByTagName(WeixinXmlTagName.CONTENT).item(0).getTextContent();
		this.msgId = document.getElementsByTagName(WeixinXmlTagName.MSG_ID).item(0).getTextContent();
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	@Override
	public String toString() {
		return "RequestMsgText [content=" + content + ", msgId=" + msgId
				+ ", head=" + head + "]";
	}
	
	public static String getMsgType(){
		return MSG_TYPE_TEXT;
	}
}
