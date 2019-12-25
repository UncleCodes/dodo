package com.dodo.utils.file.icon;

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
public class FileIconSuffixDetector implements FileIconDetector{
	private static Map<String,String> suffixMap = new HashMap<String, String>(60){
		private static final long serialVersionUID = -5229264202356879752L;
		{
			put("rar","rar");
			put("zip","zip");
			put("7z","zip");
			put("kz","zip");
			put("doc","doc");
			put("docx","doc");
			put("pdf","pdf");
			put("xls","xls");
			put("chm","chm");
			put("ppt","ppt");
			put("pptx","ppt");
			put("avi","avi");
			put("rm","mv");
			put("rmvb","mv");
			put("wmv","mv");
			put("wma","wma");
			put("wav","wav");
			put("exe","exe");
			put("psd","psd");
			put("txt","txt");
			put("bat","bat");
			put("bmp","bmp");
			put("sh","txt");
			put("cmd","cmd");
			put("dll","dll");
			put("swf","flash");
			put("flv","flv");
			put("gif","gif");
			put("png","png");
			put("html","html");
			put("htm","html");
			put("ini","ini");
			put("jpeg","jpeg");
			put("jpg","jpeg");
			put("mid","mid");
			put("mp3","mp3");
			put("mp4","mp4");
			put("mpg","mpg");
			put("reg","reg");
			put("sys","sys");
		}
	};
	@Override
	public String detector(String s) {
		return suffixMap.get(s);
	}
}
