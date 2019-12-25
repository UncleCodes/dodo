package com.dodo.common.framework.bean.file;

import java.io.Serializable;
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
public class DodoVideoFile implements Serializable{
	private static final long serialVersionUID = -6304800964195958370L;
	private String sortSeq;
	private String fileId;
	private String fileName;
	private String pureSize;
	private String formatSize;
	private String fileExt;
	private String filePath;
	private String flvPath = "";
	private String mp4Path = "";
	private String swfPath = "";
	private String thumbnailPath = "";
	private String duration = "";
	private String bitrate = "";
	private String size = "";
	private String mimeType;
	private String isComplete;
	private String fileIcon;
	private Boolean isPreOtherAd = Boolean.FALSE;
	private Boolean isPreVideoAd = Boolean.FALSE;
	private Boolean isPauseAd = Boolean.FALSE;
	private Boolean isCornerAd = Boolean.FALSE;
	private Boolean isAfterAd = Boolean.FALSE;
	private String handFeeTime="-1";
	private String httpPath = "";
	//black red dark-blue green purple peak-green 
	private String videoColor = "black";
	private Map<String,String> attr = new HashMap<String, String>();
	private Boolean isUploadOk = Boolean.TRUE;
	public String getVideoColor() {
		return videoColor;
	}
	public void setVideoColor(String videoColor) {
		this.videoColor = videoColor;
	}
	public String getHandFeeTime() {
		return handFeeTime;
	}
	public void setHandFeeTime(String handFeeTime) {
		this.handFeeTime = handFeeTime;
		try {
			this.handFeeTime = Integer.parseInt(handFeeTime)+"";
		} catch (Exception e) {
			this.handFeeTime = "-1";
		}
	}
	public Boolean getIsPreOtherAd() {
		return isPreOtherAd;
	}
	public Boolean getIsPreVideoAd() {
		return isPreVideoAd;
	}
	public Boolean getIsPauseAd() {
		return isPauseAd;
	}
	public Boolean getIsCornerAd() {
		return isCornerAd;
	}
	public Boolean getIsAfterAd() {
		return isAfterAd;
	}
	public Map<String, String> getAttr() {
		return attr;
	}
	public void addAttr(String key,String value) {
		attr.put(key, value);
	}
	public void setIsPreOtherAd(Boolean isPreOtherAd) {
		this.isPreOtherAd = isPreOtherAd;
	}
	public void setIsPreVideoAd(Boolean isPreVideoAd) {
		this.isPreVideoAd = isPreVideoAd;
	}
	public void setIsPauseAd(Boolean isPauseAd) {
		this.isPauseAd = isPauseAd;
	}
	public void setIsCornerAd(Boolean isCornerAd) {
		this.isCornerAd = isCornerAd;
	}
	public void setIsAfterAd(Boolean isAfterAd) {
		this.isAfterAd = isAfterAd;
	}
	public String getSortSeq() {
		return sortSeq;
	}
	public void setSortSeq(String sortSeq) {
		this.sortSeq = sortSeq;
	}
	public String getFileName() {
		return fileName;
	}
	public String getPureSize() {
		return pureSize;
	}
	public String getFormatSize() {
		return formatSize;
	}
	public String getFilePath() {
		return filePath;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public void setPureSize(String pureSize) {
		this.pureSize = pureSize;
	}
	public void setFormatSize(String formatSize) {
		this.formatSize = formatSize;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public String getIsComplete() {
		return isComplete;
	}
	public void setIsComplete(String isComplete) {
		this.isComplete = isComplete;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getFileIcon() {
		return fileIcon;
	}
	public void setFileIcon(String fileIcon) {
		this.fileIcon = fileIcon;
	}
	public String getFlvPath() {
		return flvPath;
	}
	public String getThumbnailPath() {
		return thumbnailPath;
	}
	public String getDuration() {
		return duration;
	}
	public String getBitrate() {
		return bitrate;
	}
	public String getSize() {
		return size;
	}
	public void setFlvPath(String flvPath) {
		this.flvPath = flvPath;
	}
	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
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
	
	public String getFileExt() {
		return fileExt;
	}
	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}
	public String getHttpPath() {
		return httpPath;
	}
	public void setHttpPath(String httpPath) {
		this.httpPath = httpPath;
	}
	public String getMp4Path() {
		return mp4Path;
	}
	public void setMp4Path(String mp4Path) {
		this.mp4Path = mp4Path;
	}
	public String getSwfPath() {
		return swfPath;
	}
	public void setSwfPath(String swfPath) {
		this.swfPath = swfPath;
	}
	public void setAttr(Map<String, String> attr) {
		this.attr = attr;
	}
	public Boolean getIsUploadOk() {
		return isUploadOk;
	}
	public void setIsUploadOk(Boolean isUploadOk) {
		this.isUploadOk = isUploadOk;
	}
	public void setAd(String[] ads){
		if(ads!=null){
			for(String s:ads){
				if("preOtherAd".equals(s)){
					setIsPreOtherAd(Boolean.TRUE);
				}else if("preVideoAd".equals(s)){
					setIsPreVideoAd(Boolean.TRUE);
				}else if("pauseAd".equals(s)){
					setIsPauseAd(Boolean.TRUE);
				}else if("cornerAd".equals(s)){
					setIsCornerAd(Boolean.TRUE);
				}else if("afterAd".equals(s)){
					setIsAfterAd(Boolean.TRUE);
				}
			}
		}
	}
}
