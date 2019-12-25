package com.dodo.utils.file.icon;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class FileIconDetectorProxy implements FileIconDetector{
	private final static String DEFAULT_ICON ="att";  
	private List<FileIconDetector> detectors = new ArrayList<FileIconDetector>();
	private FileIconDetectorProxy(){}
	public static FileIconDetectorProxy getInstance(){
		return new FileIconDetectorProxy();
	}
	public void registerDetector(FileIconDetector detector){
		if(detector!=null&&(!detectors.contains(detector))){
			detectors.add(detector);
		}
	}
	
	@Override
	public String detector(String s) {
		String result = null;
		for(FileIconDetector detector : detectors){
			result = detector.detector(s);
			if(result!=null){
				return result;
			}
		}
		return DEFAULT_ICON;
	}
}
