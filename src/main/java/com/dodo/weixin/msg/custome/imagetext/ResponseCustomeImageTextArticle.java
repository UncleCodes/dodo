package com.dodo.weixin.msg.custome.imagetext;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class ResponseCustomeImageTextArticle{
	private String title;
	private String description;
	private String url;
	private String picurl;
	public String getTitle() {
		return title;
	}
	public String getDescription() {
		return description;
	}
	public String getUrl() {
		return url;
	}
	public String getPicurl() {
		return picurl;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}
	@Override
	public String toString() {
		return "ResponseCustomeImageTextArticles [title=" + title
				+ ", description=" + description + ", url=" + url + ", picurl="
				+ picurl + "]";
	}
}
