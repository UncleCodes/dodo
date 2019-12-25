package com.dodo.weixin.msg.custome.music;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class ResponseCustomeMusicContent{
	private String title;
	private String description;
	private String musicurl;
	private String hqmusicurl;
	private String thumb_media_id;
	public String getTitle() {
		return title;
	}
	public String getDescription() {
		return description;
	}
	public String getMusicurl() {
		return musicurl;
	}
	public String getHqmusicurl() {
		return hqmusicurl;
	}
	public String getThumb_media_id() {
		return thumb_media_id;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setMusicurl(String musicurl) {
		this.musicurl = musicurl;
	}
	public void setHqmusicurl(String hqmusicurl) {
		this.hqmusicurl = hqmusicurl;
	}
	public void setThumb_media_id(String thumb_media_id) {
		this.thumb_media_id = thumb_media_id;
	}
	@Override
	public String toString() {
		return "ResponseCustomeMusicContent [title=" + title
				+ ", description=" + description + ", musicurl=" + musicurl
				+ ", hqmusicurl=" + hqmusicurl + ", thumb_media_id="
				+ thumb_media_id + "]";
	}
}
