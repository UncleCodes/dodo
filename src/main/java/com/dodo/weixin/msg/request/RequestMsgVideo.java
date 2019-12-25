package com.dodo.weixin.msg.request;

import org.w3c.dom.Document;

import com.dodo.weixin.msg.WeixinXmlTagName;

/**
 * 视频消息
 * 
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class RequestMsgVideo extends RequestMsg{
	// 视频消息媒体id，可以调用多媒体文件下载接口拉取数据。
	private String mediaId;
	// 视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。
	private String thumbMediaId;
	// 消息id，64位整型
	private String msgId;
 
	private RequestMsgVideo(RequestMsgHead head) {
		this.head = head;
	}
	
	@Override
	public void read(Document document) {
		this.mediaId = getElementContent(document, WeixinXmlTagName.MEDIAID);
		this.thumbMediaId = getElementContent(document, WeixinXmlTagName.THUMBMEDIAID);
		this.msgId = getElementContent(document, WeixinXmlTagName.MSG_ID);
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getThumbMediaId() {
		return thumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	@Override
	public String toString() {
		return "RequestMsgVideo [mediaId=" + mediaId + ", thumbMediaId="
				+ thumbMediaId + ", msgId=" + msgId + ", head=" + head + "]";
	}
	public static String getMsgType(){
		return MSG_TYPE_VIDEO;
	}
}