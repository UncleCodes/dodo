package com.dodo.privilege.interceptor.client;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class Client {
    public static final String  CLIENTTYPE_UFBALL      = "hyapp";                 //app
    public static final String  CLIENTTYPE_WECHAT      = "wechat";
    public static final String  CLIENTTYPE_UNKNOWN     = "Unknown";

    private static final String CLIENT_IN_REQUEST_ATTR = "CLIENT_IN_REQUEST_ATTR";
    //应用标识，例：hyapp
    //hyapp, Wechat, Unknown
    private String              type;
    //应用版本，如3.0.1
    private String              version;
    //应用buildnumber
    private int                 buildNum;
    //平台信息，例iOS、Android
    private ClientPlat          platform;
    //设备型号，手机时如Xiaomi_MI 5s
    private String              deviceBrand;
    //设备系统版本，如6.0.1
    private String              deviceSysversion;

    public Client(String type, String version, int buildNum, ClientPlat platform, String deviceBrand,
            String deviceSysversion, HttpServletRequest request) {
        super();
        this.type = type;
        this.version = version;
        this.buildNum = buildNum;
        this.platform = platform;
        this.deviceBrand = deviceBrand;
        this.deviceSysversion = deviceSysversion;
        request.setAttribute(CLIENT_IN_REQUEST_ATTR, this);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getBuildNum() {
        return buildNum;
    }

    public void setBuildNum(int buildNum) {
        this.buildNum = buildNum;
    }

    public ClientPlat getPlatform() {
        return platform;
    }

    public void setPlatform(ClientPlat platform) {
        this.platform = platform;
    }

    public String getDeviceBrand() {
        return deviceBrand;
    }

    public void setDeviceBrand(String deviceBrand) {
        this.deviceBrand = deviceBrand;
    }

    public String getDeviceSysversion() {
        return deviceSysversion;
    }

    public void setDeviceSysversion(String deviceSysversion) {
        this.deviceSysversion = deviceSysversion;
    }

    public static Client getCurrentClient(HttpServletRequest request) {
        return (Client) request.getAttribute(CLIENT_IN_REQUEST_ATTR);
    }

    @Override
    public String toString() {
        return String.format("%s/%s/%s/%s/%s/%s", type, platform.name(), version, buildNum, deviceBrand,
                deviceSysversion);
        //return "Client [type=" + type + ", version=" + version + ", buildNum=" + buildNum + ", platform=" + platform + ", deviceBrand=" + deviceBrand + ", deviceSysversion=" + deviceSysversion + "]";
    }
}
