package com.dodo.weixin.msg.response;


/**
 * 图文消息对象
 * 
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class ResponseMsgItem {

	// 图文消息标题
	private String title;	 
	
	// 图文消息描述
	private String description;	 
	
	// 图片链接，支持JPG、PNG格式，较好的效果为大图640*320，小图80*80。
	private String picUrl;	 
	
	// 点击图文消息跳转链接
	private String url;
	
	/**
	 * 默认构造方法
	 * */
	public ResponseMsgItem() {}

	public ResponseMsgItem(String title, String description, String picUrl, String url) {
		this.title = title;
		this.description = description;
		this.picUrl = picUrl;
		this.url = url;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "ResponseMsgItem [title=" + title + ", description="
				+ description + ", picUrl=" + picUrl + ", url=" + url + "]";
	}
}
