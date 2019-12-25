package com.dodo.common.framework.bean.location;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class DodoLocationInfo implements Serializable{
	private static final long serialVersionUID = -432913182048727184L;
	private String longitude;
	private String latitude;
	private Map<String,String> attr = new HashMap<String, String>();
	public String getLongitude() {
		return longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public Map<String, String> getAttr() {
		return attr;
	}
	public void addAttr(String key,String value) {
		attr.put(key, value);
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public void setAttr(Map<String, String> attr) {
		this.attr = attr;
	}
	
	@JsonIgnore
	public Boolean isLegal(){
		return longitude!=null&&latitude!=null&&longitude.matches("\\d+\\.\\d+")&&latitude.matches("\\d+\\.\\d+");
	}
}
