package com.dodo.weixin.msg.request;
 
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.dodo.weixin.msg.WeixinXmlTagName;

/**
 * 微信消息头
 * 
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class RequestMsgHead {
	// 开发者微信号
	private String toUserName;
	// 发送方帐号（一个OpenID）
	private String fromUserName;
	// 消息创建时间 （整型）
	private String createTime;
	// 消息类型：text\image\
	private String msgType;
	// 适配安全模式 & 兼容模式
	private String encrypt;
	
	private RequestMsgHead() {}

	public RequestMsgHead read(Document document) {
		 
		this.toUserName = document.getElementsByTagName(WeixinXmlTagName.TO_USER_NAME).item(0).getTextContent();
		this.fromUserName = getElementContent(document,WeixinXmlTagName.FROM_USER_NAME);
		this.createTime = getElementContent(document,WeixinXmlTagName.CREATE_TIME);
		this.msgType = getElementContent(document,WeixinXmlTagName.MSG_TYPE);
		this.encrypt = getElementContent(document,WeixinXmlTagName.Encrypt);
		return this;
		
	}
	
	/**
	 * 获取节点文本内容
	 */
	protected String getElementContent(Document document, String element){
		NodeList nodeList = document.getElementsByTagName(element);
		if(nodeList.getLength()==0){
			return null;
		}
		return nodeList.item(0).getTextContent();
	}
	
	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getEncrypt() {
		return encrypt;
	}

	@Override
	public String toString() {
		return "RequestMsgHead [toUserName=" + toUserName + ", fromUserName="
				+ fromUserName + ", createTime=" + createTime + ", msgType="
				+ msgType + ", encrypt=" + encrypt + "]";
	}
}
