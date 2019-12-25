package com.dodo.weixin.msg.response;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * 接收到的消息基类 公共变量定义等
 * 
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class ResponseMsg {

	/** 文本消息 */
	public static final String MSG_TYPE_TEXT = "text";
	/** 图片消息 */
	public static final String MSG_TYPE_IMAGE = "image";
	/** 音乐消息 */
	public static final String MSG_TYPE_MUSIC = "music";
	/** 地理位置消息 */
	public static final String MSG_TYPE_LOCATION = "location";
	/** 链接消息 */
	public static final String MSG_TYPE_LINK = "link";
	/** 图文消息 */
	public static final String MSG_TYPE_IMAGE_TEXT = "news";
	/** 事件消息 */
	public static final String MSG_TYPE_EVENT = "event";
	/** 语音识别消息 */
	public static final String MSG_TYPE_VOICE = "voice";
	/** 视频消息 */
	public static final String MSG_TYPE_VIDEO = "video";
	/** 多客服消息转接 */
	public static final String MSG_TYPE_TRANSFER_CUSTOMER_SERVICE = "transfer_customer_service";
	
	/** 消息头 */
	protected ResponseMsgHead head;
	
	/**
	 * 读取消息内容
	 * @param document
	 */
	public void write(Document document){};

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
	
	
	public ResponseMsgHead getHead() {
		return head;
	}

	public String getToUserName() {
		return head.getToUserName();
	}
	
	public void setToUserName(String toUserName) {
		head.setToUserName(toUserName);
	}

	public String getFromUserName() {
		return head.getFromUserName();
	}

	public void setFromUserName(String fromUserName) {
		head.setFromUserName(fromUserName);
	}
	
	public String getCreateTime() {
		return head.getCreateTime();
	}
	
	public String getMsgType() {
		return head.getMsgType();
	}

	@Override
	public String toString() {
		return "ResponseMsg [head=" + head + "]";
	}
}
