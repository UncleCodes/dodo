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
public class DodoFile implements Serializable{
	private static final long serialVersionUID = 3237681781093228749L;
	private String sortSeq;
	private String fileId;
	private String fileName;
	private String fileExt;
	private String pureSize;
	private String formatSize;
	private String filePath;
	private String mimeType;
	private String pageCount;
	private String isComplete;
	private String isSplitPage;
	private String fileIcon;
	private String httpPath = "";
	private int width;
	private int height;
	private Boolean isUploadOk = Boolean.TRUE;
	private Map<String,String> attr = new HashMap<String, String>();
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
	public String getPageCount() {
		return pageCount;
	}
	public String getIsComplete() {
		return isComplete;
	}
	public Map<String, String> getAttr() {
		return attr;
	}
	public void addAttr(String key,String value) {
		attr.put(key, value);
	}
	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
	}
	public void setIsComplete(String isComplete) {
		this.isComplete = isComplete;
	}
	public String getIsSplitPage() {
		return isSplitPage;
	}
	public void setIsSplitPage(String isSplitPage) {
		this.isSplitPage = isSplitPage;
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
	public void setAttr(Map<String, String> attr) {
		this.attr = attr;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public Boolean getIsUploadOk() {
		return isUploadOk;
	}
	public void setIsUploadOk(Boolean isUploadOk) {
		this.isUploadOk = isUploadOk;
	}
}
