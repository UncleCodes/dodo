package com.dodo.privilege.interceptor.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.dodo.utils.config.DodoFrameworkConfigUtil.DodoCommonConfigUtil;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class ClientInterceptor extends HandlerInterceptorAdapter {
    private static final Logger LOGGER    = LoggerFactory.getLogger(ClientInterceptor.class);

    // ClientType/ClientPlat/version/buildNum/deviceBrand/deviceSysversion
    // myapp/Android/3.0.3/22/OPPO_OPPO R9s/6.0.1
    // myapp/iOS/3.1.3/106/iPhone 6s Plus/11.4.1
    private static Pattern      uaPattern = Pattern.compile("([\\w]+)/([\\w]+)/([\\w\\d\\.]+)/([\\d]+)(/.+)?");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userAgent = StringUtils.defaultIfBlank(request.getHeader("User-Agent"), "Unknown/Unknown/8.8.8/888");
        Matcher matcher = uaPattern.matcher(userAgent);
        if (DodoCommonConfigUtil.isDev) {
            LOGGER.info("userAgent==" + userAgent);
        }
        if (matcher.find()) {
            String type = Client.CLIENTTYPE_UNKNOWN;
            try {
                type = matcher.group(1);
            } catch (Exception e) {
            }
            ClientPlat plat = ClientPlat.Unknown;
            try {
                plat = ClientPlat.valueOf(matcher.group(2));
            } catch (Exception e) {
            }
            String version = matcher.group(3);
            int buildNum = Integer.parseInt(matcher.group(4));
            String deviceBrand = null;
            String deviceSysversion = null;
            try {
                String osInfo = matcher.group(5);
                if (osInfo != null && osInfo.startsWith("/")) {
                    String[] osInfoArr = osInfo.split("/");
                    if (osInfoArr.length >= 3) {
                        deviceBrand = osInfoArr[1];
                        deviceSysversion = osInfoArr[2];
                    }
                }
            } catch (Exception e) {
            }
            new Client(type, version, buildNum, plat, deviceBrand, deviceSysversion, request);
        } else if (userAgent.toLowerCase().indexOf("micromessenger") != -1) {
            new Client(Client.CLIENTTYPE_WECHAT, "8.8.8", 888, ClientPlat.Wechat, null, null, request);
        } else {
            new Client(Client.CLIENTTYPE_UNKNOWN, "8.8.8", 888, ClientPlat.Unknown, null, null, request);
        }
        return super.preHandle(request, response, handler);
    }
}