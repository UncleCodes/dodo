package com.dodo.common.video;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class VideoInfo {
	private String videoPath;
	private String duration;
	private String bitrate;
	private String size;
	
	public String getDuration() {
		return duration;
	}
	public String getBitrate() {
		return bitrate;
	}
	public String getSize() {
		return size;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public void setBitrate(String bitrate) {
		this.bitrate = bitrate;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getVideoPath() {
		return videoPath;
	}
	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}
	@Override
	public String toString() {
		return "VideoInfo [videoPath=" + videoPath + ", duration=" + duration
				+ ", bitrate=" + bitrate + ", size=" + size  + "]";
	}
}
