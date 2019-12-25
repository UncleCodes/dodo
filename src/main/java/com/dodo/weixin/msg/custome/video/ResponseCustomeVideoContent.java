package com.dodo.weixin.msg.custome.video;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class ResponseCustomeVideoContent{
	private String media_id;
	private String thumb_media_id;
	private String title;
	private String description;

	public String getMedia_id() {
		return media_id;
	}

	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}

	public String getThumb_media_id() {
		return thumb_media_id;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public void setThumb_media_id(String thumb_media_id) {
		this.thumb_media_id = thumb_media_id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "ResponseCustomeVideoContent [media_id=" + media_id
				+ ", thumb_media_id=" + thumb_media_id + ", title=" + title
				+ ", description=" + description + "]";
	}
}
