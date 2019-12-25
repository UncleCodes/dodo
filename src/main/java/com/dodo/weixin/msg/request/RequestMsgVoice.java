package com.dodo.weixin.msg.request;

import org.w3c.dom.Document;

import com.dodo.weixin.msg.WeixinXmlTagName;

/**
 * 语音消息
 * 
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class RequestMsgVoice extends RequestMsg{

	// 语音消息媒体id，可以调用多媒体文件下载接口拉取该媒体
	private String mediaId;
	// 语音格式：amr
	private String format;
	// 语音识别结果，UTF8编码
	private String recognition;
	// 消息id，64位整型
	private String msgId;
	
	private RequestMsgVoice(RequestMsgHead head){
		this.head = head;
	}
	
	@Override
	public void read(Document document) {
		this.mediaId = getElementContent(document, WeixinXmlTagName.MEDIAID);
		this.format = getElementContent(document, WeixinXmlTagName.FORMAT);
		this.recognition = getElementContent(document, WeixinXmlTagName.RECOGNITION);
		this.msgId = getElementContent(document, WeixinXmlTagName.MSG_ID);
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getRecognition() {
		return recognition;
	}

	public void setRecognition(String recognition) {
		this.recognition = recognition;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	@Override
	public String toString() {
		return "RequestMsgVoice [mediaId=" + mediaId + ", format=" + format
				+ ", recognition=" + recognition + ", msgId=" + msgId
				+ ", head=" + head + "]";
	}
	public static String getMsgType(){
		return MSG_TYPE_VOICE;
	}
}
