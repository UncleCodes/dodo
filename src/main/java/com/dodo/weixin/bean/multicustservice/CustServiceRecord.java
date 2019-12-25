package com.dodo.weixin.bean.multicustservice;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class CustServiceRecord {
	private static Map<Integer,String> opercodesMap = new HashMap<Integer, String>(){
		private static final long serialVersionUID = 1125349909878104934L;
		{
			put(1000,"创建未接入会话");
			put(1001,"接入会话");
			put(1002,"主动发起会话");
			put(1004,"关闭会话");
			put(1005,"抢接会话");
			put(2001,"公众号收到消息");
			put(2002,"客服发送消息");
			put(2003,"客服收到消息");
		}
	};
	
	private String worker;
	private String openid;
	private Integer opercode;
	private String opercodeChinese;
	private Long time;
	private String text;
	public String getWorker() {
		return worker;
	}
	public String getOpenid() {
		return openid;
	}
	public Integer getOpercode() {
		return opercode;
	}
	public Long getTime() {
		return time;
	}
	public String getText() {
		return text;
	}
	public void setWorker(String worker) {
		this.worker = worker;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public void setOpercode(Integer opercode) {
		this.opercode = opercode;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Date getServiceDate(){
		if(time==null){
			return null;
		}
		return new Date(time*1000);
	}
	public String getOpercodeChinese() {
		if(opercode!=null && opercodeChinese==null){
			opercodeChinese = opercodesMap.get(opercode);
		}
		return opercodeChinese;
	}
	@Override
	public String toString() {
		if(opercode!=null){
			opercodeChinese = opercodesMap.get(opercode);
		}
		return "CustServiceRecord [worker=" + worker + ", openid=" + openid
				+ ", opercode=" + opercode + ", opercodeChinese="
				+ opercodeChinese + ", time=" + time + ", text=" + text + "]";
	}
}
