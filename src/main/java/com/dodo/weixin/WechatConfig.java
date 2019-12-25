package com.dodo.weixin;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public final class WechatConfig implements Serializable {
    private static final long serialVersionUID = -3326595555103833317L;
    private String            APPID;
    private String            SECRET;
    private String            lastEncodingAESKey;
    private String            currEncodingAESKey;
    private String            token;
    private long              accessTokenGotTime;
    private String            accessToken;
    private int               accessTokenExpiresIn;
    private String            weburl;
    private long              get_jsapi_Time;
    private String            jsapi_ticket;

    private String            name;

    public WechatConfig() {
    }

    public String getAPPID() {
        return APPID;
    }

    public String getSECRET() {
        return SECRET;
    }

    public String getLastEncodingAESKey() {
        return lastEncodingAESKey;
    }

    public String getCurrEncodingAESKey() {
        return currEncodingAESKey;
    }

    public String getToken() {
        return token;
    }

    public long getAccessTokenGotTime() {
        return accessTokenGotTime;
    }

    public int getAccessTokenExpiresIn() {
        return accessTokenExpiresIn;
    }

    public void setAPPID(String aPPID) {
        APPID = aPPID;
    }

    public void setSECRET(String sECRET) {
        SECRET = sECRET;
    }

    public void setLastEncodingAESKey(String lastEncodingAESKey) {
        this.lastEncodingAESKey = lastEncodingAESKey;
    }

    public void setCurrEncodingAESKey(String currEncodingAESKey) {
        this.currEncodingAESKey = currEncodingAESKey;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setAccessTokenGotTime(long accessTokenGotTime) {
        this.accessTokenGotTime = accessTokenGotTime;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setAccessTokenExpiresIn(int accessTokenExpiresIn) {
        this.accessTokenExpiresIn = accessTokenExpiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getWeburl() {
        return weburl;
    }

    public void setWeburl(String weburl) {
        this.weburl = weburl;
    }

    public long getGet_jsapi_Time() {
        return get_jsapi_Time;
    }

    public void setGet_jsapi_Time(long get_jsapi_Time) {
        this.get_jsapi_Time = get_jsapi_Time;
    }

    public String getJsapi_ticket() {
        return jsapi_ticket;
    }

    public void setJsapi_ticket(String jsapi_ticket) {
        this.jsapi_ticket = jsapi_ticket;
    }

    @JsonIgnore
    public boolean isExpired() {
        return (accessToken == null || (System.currentTimeMillis() - accessTokenGotTime > accessTokenExpiresIn - 1000));
    }

    @JsonIgnore
    public boolean isExpiredJsapi_ticket() {
        return (jsapi_ticket == null || (System.currentTimeMillis() - get_jsapi_Time > 7200 - 1000));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "WechatConfig [APPID=" + APPID + ", SECRET=" + SECRET + ", lastEncodingAESKey=" + lastEncodingAESKey
                + ", currEncodingAESKey=" + currEncodingAESKey + ", token=" + token + ", accessTokenGotTime="
                + accessTokenGotTime + ", accessToken=" + accessToken + ", accessTokenExpiresIn="
                + accessTokenExpiresIn + ", weburl=" + weburl + ", get_jsapi_Time=" + get_jsapi_Time
                + ", jsapi_ticket=" + jsapi_ticket + ", name=" + name + "]";
    }
}
