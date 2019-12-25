package com.dodo.weixin.bean.media;

import com.dodo.weixin.bean.ResultMsgBean;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class MediaUploadResult extends ResultMsgBean{
	private String type;
	private String media_id;
	private Long created_at;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMedia_id() {
		return media_id;
	}
	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}
	public Long getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Long created_at) {
		this.created_at = created_at;
	}
	@Override
	public String toString() {
		return "MediaUploadResult [type=" + type + ", media_id=" + media_id
				+ ", created_at=" + created_at + "]";
	}
}
