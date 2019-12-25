package com.dodo.weixin.msg.response;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
public class ResponseMsgImage extends ResponseMsg{

	//位0x0001被标志时，星标刚收到的消息。
	private String funcFlag;
	// 图片消息媒体id
	private String mediaId;
	
	public ResponseMsgImage() {
		this.head = new ResponseMsgHead();
		this.head.setMsgType(ResponseMsg.MSG_TYPE_IMAGE);
	}


	public void write(Document document) {
		Element root = document.createElement(WeixinXmlTagName.ROOT);
		head.write(root, document);
		
		Element mediaIdElement = document.createElement(WeixinXmlTagName.MEDIAID);
		mediaIdElement.setTextContent(this.mediaId);
		
		Element imageElement = document.createElement(WeixinXmlTagName.IMAGE);
		imageElement.appendChild(mediaIdElement);
		
		Element funcFlagElement = document.createElement(WeixinXmlTagName.FUNC_FLAG);
		funcFlagElement.setTextContent(this.funcFlag);
		
		root.appendChild(imageElement);
		root.appendChild(funcFlagElement);
		document.appendChild(root);
	}
	
	public String getFuncFlag() {
		return funcFlag;
	}

	public void setFuncFlag(String funcFlag) {
		this.funcFlag = funcFlag;
	}
	
	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}


	@Override
	public String toString() {
		return "ResponseMsgImage [funcFlag=" + funcFlag + ", mediaId="
				+ mediaId + ", head=" + head + "]";
	}
}
