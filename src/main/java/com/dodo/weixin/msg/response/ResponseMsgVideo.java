package com.dodo.weixin.msg.response;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
public class ResponseMsgVideo extends ResponseMsg{
	// 视频消息媒体id，可以调用多媒体文件下载接口拉取数据。
	private String mediaId;
	// 视频消息的标题 
	private String title;
	// 视频消息的描述 
	private String description;
 
	public ResponseMsgVideo() {
		this.head = new ResponseMsgHead();
		this.head.setMsgType(ResponseMsg.MSG_TYPE_VIDEO);
	}
	
	public void write(Document document) {
		Element root = document.createElement(WeixinXmlTagName.ROOT);
		head.write(root, document);
		
		Element mediaIdElement = document.createElement(WeixinXmlTagName.MEDIAID);
		mediaIdElement.setTextContent(this.mediaId);
		
		Element titleElement = document.createElement(WeixinXmlTagName.TITLE);
		titleElement.setTextContent(this.title);
		
		Element descriptionElement = document.createElement(WeixinXmlTagName.DESCRITION);
		descriptionElement.setTextContent(this.description);
		
		Element videoElement = document.createElement(WeixinXmlTagName.VIDEO);
		videoElement.appendChild(mediaIdElement);
		videoElement.appendChild(titleElement);
		videoElement.appendChild(descriptionElement);
		
		root.appendChild(videoElement);
		document.appendChild(root);
	}


	public String getMediaId() {
		return mediaId;
	}


	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "ResponseMsgVideo [mediaId=" + mediaId + ", title=" + title
				+ ", description=" + description + ", head=" + head + "]";
	}
}