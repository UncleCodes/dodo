package com.dodo.weixin.msg.request;

import org.w3c.dom.Document;

import com.dodo.weixin.msg.WeixinXmlTagName;

/**
 * 事件消息
 * 
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class RequestMsgEvent extends RequestMsg {
	/** 订阅 */
	public static final String SUBSCRIBE = "subscribe";
	
	/** 取消订阅 */
	public static final String UNSUBSCRIBE = "unsubscribe";
	
	/** 自定义菜单 点击时 */
	public static final String CLICK = "CLICK";
	
	/** 自定义菜单 点击打开 URL时 */
	public static final String VIEW = "VIEW";
	
	/** 扫描带参数的二维码 */
	public static final String SCAN = "SCAN";
	
	/** 上报地理位置 */
	public static final String LOCATION = "LOCATION";
	
	/** 上报地理位置结束 */
	public static final String TEMPLATESENDJOBFINISH = "TEMPLATESENDJOBFINISH";
	
	/** 事件类型 */
	private String event;
	/**
	 * 1、扫描带参数的二维码
	 * 2、自定义菜单
	 * */
	private String eventKey;
	
	/** 二维码的ticket，可用来换取二维码图片 */
	private String ticket;
	
	/**地理位置纬度 */
	private String latitude;
	
	/**地理位置经度 */
	private String longitude;
	
	/**地理位置精度 */
	private String precision;
	
	/**模板消息发送消息ID */
	private String msgId;
	
	/**模板消息发送状态 
	 * 1、failed: system failed 发送状态为发送失败（非用户拒绝） 
	 * 2、failed:user block 发送状态为用户拒绝接收 
	 * 3、success 发送状态为成功 
	 * */
	private String status;
	
	private RequestMsgEvent(RequestMsgHead head) {
		this.head = head;
	}
	
	@Override
	public void read(Document document) {
		 
		
		this.event = getElementContent(document, WeixinXmlTagName.EVENT);
		if(SCAN.equals(this.event)){
			this.eventKey = getElementContent(document, WeixinXmlTagName.EVENT_KEY);
			this.ticket = getElementContent(document, WeixinXmlTagName.TICKET);
		}else if(LOCATION.equals(this.event)){// 上报地理位置事件
			this.latitude = getElementContent(document, WeixinXmlTagName.LATITUDE);
			this.longitude = getElementContent(document, WeixinXmlTagName.LONGITUDE); 
			this.precision = getElementContent(document, WeixinXmlTagName.PRECISION);
		}else if(CLICK.equals(this.event)||VIEW.equals(this.event)){// 自定义菜单事件
			this.eventKey = getElementContent(document, WeixinXmlTagName.EVENT_KEY);
		}else if(TEMPLATESENDJOBFINISH.equals(this.event)){
			this.msgId = getElementContent(document, WeixinXmlTagName.MSG_ID);
			this.status = getElementContent(document, WeixinXmlTagName.STATUS);
		}else if(SUBSCRIBE.equals(this.event)){
			//qrscene_
			this.eventKey = getElementContent(document, WeixinXmlTagName.EVENT_KEY);
			if(this.eventKey!=null){
				this.eventKey = this.eventKey.replace("qrscene_", "");
			}
			this.ticket = getElementContent(document, WeixinXmlTagName.TICKET);
		}
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getEventKey() {
		return eventKey;
	}


	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getPrecision() {
		return precision;
	}

	public void setPrecision(String precision) {
		this.precision = precision;
	}

	public String getMsgId() {
		return msgId;
	}

	public String getStatus() {
		return status;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "RequestMsgEvent [event=" + event + ", eventKey=" + eventKey
				+ ", ticket=" + ticket + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", precision=" + precision
				+ ", msgId=" + msgId + ", status=" + status + ", head=" + head
				+ "]";
	}
	public static String getMsgType(){
		return MSG_TYPE_EVENT;
	}
}
