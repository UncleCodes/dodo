package com.dodo.weixin.msg.request;

import org.w3c.dom.Document;

import com.dodo.weixin.msg.WeixinXmlTagName;

/**
 * 链接消息
 * 
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class RequestMsgLink extends RequestMsg {
	//消息标题
	private String title;	 
	//消息描述
	private String description;	 
	//消息链接
	private String url;	 
	//消息id，64位整型
	private String msgId;
	
	private RequestMsgLink(RequestMsgHead head) {
		this.head = head; 
	}

	@Override
	public void read(Document document) {
		this.title = document.getElementsByTagName(WeixinXmlTagName.TITLE).item(0).getTextContent();
		this.description = document.getElementsByTagName(WeixinXmlTagName.DESCRITION).item(0).getTextContent();
		this.url = document.getElementsByTagName(WeixinXmlTagName.URL).item(0).getTextContent();
		this.msgId = document.getElementsByTagName(WeixinXmlTagName.MSG_ID).item(0).getTextContent();
	} 
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	@Override
	public String toString() {
		return "RequestMsgLink [title=" + title + ", description="
				+ description + ", url=" + url + ", msgId=" + msgId + ", head="
				+ head + "]";
	}
	public static String getMsgType(){
		return MSG_TYPE_LINK;
	}
}
