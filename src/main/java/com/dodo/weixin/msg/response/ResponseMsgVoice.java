package com.dodo.weixin.msg.response;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
public class ResponseMsgVoice extends ResponseMsg{
	// 语音消息媒体id，可以调用多媒体文件下载接口拉取该媒体
	private String mediaId;
	
	public ResponseMsgVoice(){
		this.head = new ResponseMsgHead();
		this.head.setMsgType(ResponseMsg.MSG_TYPE_VOICE);
	}
	
	public void write(Document document) {
		Element root = document.createElement(WeixinXmlTagName.ROOT);
		head.write(root, document);
		
		Element mediaIdElement = document.createElement(WeixinXmlTagName.MEDIAID);
		mediaIdElement.setTextContent(this.mediaId);
		
		Element voiceElement = document.createElement(WeixinXmlTagName.VOICE);
		voiceElement.appendChild(mediaIdElement);
		
		root.appendChild(voiceElement);
		document.appendChild(root);
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	@Override
	public String toString() {
		return "ResponseMsgVoice [mediaId=" + mediaId + ", head=" + head + "]";
	}
}
