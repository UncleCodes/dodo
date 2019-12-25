package com.dodo.common.video;

import java.io.File;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class VideoUtil {
	/**
	 * 获取文件扩展名
	 */
	public static String getFileSuffix(String fileFullName) {
		int splitIndex = fileFullName.lastIndexOf(".");
		if(splitIndex==-1){
			return "";
		}
		return fileFullName.substring(splitIndex + 1);
	}

	/**
	 * 获取文件扩展名
	 */
	public static String getFileSuffix(File file) {
		String fileFullName = file.getName();
		return getFileSuffix(fileFullName);
	}
	
	/**
	 * 获取文件名 不包含扩展名
	 */
	public static String getFilePrefix(String fileFullName) {
		int splitIndex = fileFullName.lastIndexOf(".");
		if(splitIndex==-1){
			return fileFullName;
		}
		return fileFullName.substring(0, splitIndex);
	}

	/**
	 * 获取文件名 不包含扩展名
	 */
	public static String getFilePrefix(File file) {
		String fileFullName = file.getName();
		return getFilePrefix(fileFullName);
	}
}
