package com.dodo.weixin.msg.request;

import org.w3c.dom.Document;

import com.dodo.weixin.msg.WeixinXmlTagName;

/**
 * 地理位置消息
 * 
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class RequestMsgLocation extends RequestMsg {
	// 地理位置纬度
	private String location_X;
	// 地理位置经度
	private String location_Y;
	// 地图缩放大小
	private String scale;
	// 地理位置信息
	private String label;
	//消息id，64位整型
	private String msgId;
	
	private RequestMsgLocation(RequestMsgHead head) {
		this.head = head;
	}
	
	@Override
	public void read(Document document) {
		this.location_X = document.getElementsByTagName(WeixinXmlTagName.LOCATION_X).item(0).getTextContent();
		this.location_Y = document.getElementsByTagName(WeixinXmlTagName.LOCATION_Y).item(0).getTextContent();
		this.scale = document.getElementsByTagName(WeixinXmlTagName.SCALE).item(0).getTextContent();
		this.label = document.getElementsByTagName(WeixinXmlTagName.LABEL).item(0).getTextContent();
		this.msgId = document.getElementsByTagName(WeixinXmlTagName.MSG_ID).item(0).getTextContent();
	}
	
	public String getLocation_X() {
		return location_X;
	}
	public void setLocation_X(String location_X) {
		this.location_X = location_X;
	}
	public String getLocation_Y() {
		return location_Y;
	}
	public void setLocation_Y(String location_Y) {
		this.location_Y = location_Y;
	}
	public String getScale() {
		return scale;
	}
	public void setScale(String scale) {
		this.scale = scale;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	@Override
	public String toString() {
		return "RequestMsgLocation [location_X=" + location_X + ", location_Y="
				+ location_Y + ", scale=" + scale + ", label=" + label
				+ ", msgId=" + msgId + ", head=" + head + "]";
	}
	public static String getMsgType(){
		return MSG_TYPE_LOCATION;
	}
}
