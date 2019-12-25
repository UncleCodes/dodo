package com.dodo.weixin.bean.user;

import com.dodo.weixin.bean.ResultMsgBean;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class UserBasicInfo extends ResultMsgBean{
	public enum WeinXinUserSex{
		unknown,male,female
	}
	private String subscribe;
	private String openid; 
	private String nickname; 
	private Integer sex;
	private String language; 
	private String city;
	private String province;
	private String country;
	private String headimgurl; 
	private String subscribe_time;
	private String unionid;
	private String remark;
	public String getSubscribe() {
		return subscribe;
	}
	public String getOpenid() {
		return openid;
	}
	public String getNickname() {
		return nickname;
	}
	public WeinXinUserSex getSex() {
		if(sex==null){
			sex = 0;
		}
		return WeinXinUserSex.values()[sex];
	}
	public String getLanguage() {
		return language;
	}
	public String getCity() {
		return city;
	}
	public String getProvince() {
		return province;
	}
	public String getCountry() {
		return country;
	}
	public String getHeadimgurl() {
		return headimgurl;
	}
	public String getSubscribe_time() {
		return subscribe_time;
	}
	public String getUnionid() {
		return unionid;
	}
	public void setSubscribe(String subscribe) {
		this.subscribe = subscribe;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	public void setSubscribe_time(String subscribe_time) {
		this.subscribe_time = subscribe_time;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Override
	public String toString() {
		return super.toString()+"\nUserBasicInfo [subscribe=" + subscribe + ", openid=" + openid
				+ ", nickname=" + nickname + ", sex=" + sex + ", language="
				+ language + ", city=" + city + ", province=" + province
				+ ", country=" + country + ", headimgurl=" + headimgurl
				+ ", subscribe_time=" + subscribe_time + ", unionid=" + unionid
				+ ", remark=" + remark + "]";
	}
}
