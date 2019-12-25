package com.dodo.weixin.msg.request;

import org.w3c.dom.Document;

import com.dodo.weixin.msg.WeixinXmlTagName;

/**
 * 图片消息
 * 
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class RequestMsgImage extends RequestMsg{
	//图片链接
	private String picUrl;
	//消息id，64位整型
	private String msgId;
	// 图片消息媒体id
	private String mediaId;
	
	private RequestMsgImage(RequestMsgHead head) {
		this.head = head;
	}

	@Override
	public void read(Document document) {
		this.picUrl = document.getElementsByTagName(WeixinXmlTagName.PIC_URL).item(0).getTextContent();
		this.msgId   = document.getElementsByTagName(WeixinXmlTagName.MSG_ID).item(0).getTextContent();
		this.mediaId = getElementContent(document, WeixinXmlTagName.MEDIAID);
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}


	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	@Override
	public String toString() {
		return "RequestMsgImage [picUrl=" + picUrl + ", msgId=" + msgId
				+ ", mediaId=" + mediaId + ", head=" + head + "]";
	}
	
	public static String getMsgType(){
		return MSG_TYPE_IMAGE;
	}
}
