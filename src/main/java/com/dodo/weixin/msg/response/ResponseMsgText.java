package com.dodo.weixin.msg.response;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
public class ResponseMsgText extends ResponseMsg {
	// 文本消息内容
	private String content;
	//位0x0001被标志时，星标刚收到的消息。
	private String funcFlag;
	
	/**
	 * 默认构造
	 * 初始化head对象，主要由开发者调用
	 * */
	public ResponseMsgText() {
		this.head = new ResponseMsgHead();
		this.head.setMsgType(ResponseMsg.MSG_TYPE_TEXT);
	}

	public void write(Document document) {
		Element root = document.createElement(WeixinXmlTagName.ROOT);
		head.write(root, document);
		
		Element contentElement = document.createElement(WeixinXmlTagName.CONTENT);
		contentElement.setTextContent(this.content);
		
		Element funcFlagElement = document.createElement(WeixinXmlTagName.FUNC_FLAG);
		funcFlagElement.setTextContent(this.funcFlag);
		
		root.appendChild(contentElement);
		root.appendChild(funcFlagElement);
		document.appendChild(root);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	public String getFuncFlag() {
		return funcFlag;
	}

	public void setFuncFlag(String funcFlag) {
		this.funcFlag = funcFlag;
	}

	@Override
	public String toString() {
		return "ResponseMsgText [content=" + content + ", funcFlag=" + funcFlag
				+ ", head=" + head + "]";
	}
}
