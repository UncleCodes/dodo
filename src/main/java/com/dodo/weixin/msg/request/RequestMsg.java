package com.dodo.weixin.msg.request;

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
public abstract class RequestMsg {

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
	/** 错误消息 */
	public static final String MSG_TYPE_ERROR = "error";
	/** 消息头 */
	protected RequestMsgHead head = null;
	
	/**
	 * 读取消息内容
	 * @param document
	 */
	public abstract void read(Document document);

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
	
	
	public RequestMsgHead getHead() {
		return head;
	}

	public void setHead(RequestMsgHead head) {
		this.head = head;
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

	public void setCreateTime(String createTime) {
		head.setCreateTime(createTime);
	}
	
	 
}
