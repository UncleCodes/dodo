package com.dodo.weixin.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.dodo.utils.http.HttpPack;
import com.dodo.utils.http.HttpUtils;
import com.dodo.weixin.WechatConfig;
import com.dodo.weixin.bean.AccessTokenBean;
import com.dodo.weixin.bean.JsapiTicketBean;
import com.dodo.weixin.bean.ip.WeixinServerIpsBean;
import com.dodo.weixin.exception.WeixinException;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
@Configuration
@PropertySource("classpath:/wechat_config.properties")
public class WeixinConfig {

    @Autowired
    private Environment                      env;

    public static final String               URL_ACCESSTOKEN               = "https://api.weixin.qq.com/cgi-bin/token";
    public static final String               URL_WEIXIN_SERVER_IPS         = "https://api.weixin.qq.com/cgi-bin/getcallbackip";

    public static final String               URL_JSAPI_TICKET              = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";

    private static Map<String, WechatConfig> configs                       = new HashMap<String, WechatConfig>();

    // 获取 ACCESS_TOKEN 时 使用 
    public static final String               grant_type                    = "client_credential";
    //自定义菜单的动作类型 click
    public static final String               MENU_TYPE_CLICK               = "click";
    //自定义菜单的动作类型 view
    public static final String               MENU_TYPE_VIEW                = "view";

    private static byte[]                    lock                          = new byte[0];

    private static final Logger              LOGGER                        = LoggerFactory
                                                                                   .getLogger(WeixinConfig.class);

    public static final String               WECHAT_ACCOUNTS_KEY           = "dodo.weixin.wechat.accounts";
    public static final String               WECHAT_TOKEN_KEY              = "dodo.weixin.{0}.token";
    public static final String               WECHAT_APP_ID_KEY             = "dodo.weixin.{0}.appid";
    public static final String               WECHAT_SECRET_KEY_KEY         = "dodo.weixin.{0}.secret";
    public static final String               WECHAT_WEBURL_KEY             = "dodo.weixin.{0}.weburl";
    public static final String               WECHAT_LASTENCODINGAESKEY_KEY = "dodo.weixin.{0}.lastEncodingAESKey";
    public static final String               WECHAT_CURRENCODINGAESKEY_KEY = "dodo.weixin.{0}.currEncodingAESKey";

    @PostConstruct
    public void init() {
        String[] accounts = env.getProperty(WECHAT_ACCOUNTS_KEY).split(",");
        for (int i = 0; i < accounts.length; i++) {
            WechatConfig wechatConfig = new WechatConfig();
            wechatConfig.setName(accounts[i]);
            wechatConfig.setToken(env.getProperty(MessageFormat.format(WECHAT_TOKEN_KEY, wechatConfig.getName())));
            wechatConfig.setAPPID(env.getProperty(MessageFormat.format(WECHAT_APP_ID_KEY, wechatConfig.getName())));
            wechatConfig
                    .setSECRET(env.getProperty(MessageFormat.format(WECHAT_SECRET_KEY_KEY, wechatConfig.getName())));
            wechatConfig.setWeburl(env.getProperty(MessageFormat.format(WECHAT_WEBURL_KEY, wechatConfig.getName())));
            wechatConfig.setLastEncodingAESKey(env.getProperty(MessageFormat.format(WECHAT_LASTENCODINGAESKEY_KEY,
                    wechatConfig.getName())));
            wechatConfig.setCurrEncodingAESKey(env.getProperty(MessageFormat.format(WECHAT_CURRENCODINGAESKEY_KEY,
                    wechatConfig.getName())));
            configs.put(wechatConfig.getName(), wechatConfig);
            LOGGER.info(wechatConfig.toString());
        }
        LOGGER.info("*********WechatConfig*****************");
        LOGGER.info(configs.toString());
        LOGGER.info("*********WechatConfig*****************");
    }

    public static void _getAccessToken(WechatConfig config) throws UnsupportedEncodingException, IllegalStateException,
            IOException {
        String url = URL_ACCESSTOKEN + "?" + "grant_type=" + grant_type + "&appid=" + config.getAPPID() + "&secret="
                + config.getSECRET();
        HttpPack pack = HttpPack.create(url);
        String resultStr = (String) pack.post(resp -> {
            return HttpUtils.read(resp);
        });
        AccessTokenBean accessTokenBean = WeixinJsonUtil.toObject(resultStr, AccessTokenBean.class);
        long getTime = System.currentTimeMillis();
        String access_token = accessTokenBean.getAccess_token();
        int expires_in = accessTokenBean.getExpires_in();
        config.setAccessToken(access_token);
        config.setAccessTokenExpiresIn(expires_in);
        config.setAccessTokenGotTime(getTime);
    }

    /**
     * 获取access_token
     * 
     * @return
     * @throws WeixinException
     */
    public static String getAccessToken(String weChatAccount) {
        WechatConfig config = configs.get(weChatAccount);
        if (StringUtils.isBlank(config.getAPPID())) {
            return null;
        }

        if (!config.isExpired()) {
            return config.getAccessToken();
        }

        synchronized (lock) {
            if (!config.isExpired()) {
                return config.getAccessToken();
            }
            try {
                _getAccessToken(config);
            } catch (Exception e) {
                try {
                    _getAccessToken(config);
                } catch (Exception ex) {
                    config.setAccessToken(null);
                    config.setAccessTokenExpiresIn(0);
                    config.setAccessTokenGotTime(0);
                    ex.printStackTrace();
                }
            }
        }
        if (!config.isExpired()) {
            return config.getAccessToken();
        }
        return null;
    }

    private static void _getJsapi_ticket(String weChatAccount, WechatConfig config)
            throws UnsupportedEncodingException, IllegalStateException, IOException {
        String url = URL_JSAPI_TICKET + "?access_token=" + getAccessToken(weChatAccount) + "&type=jsapi";
        HttpPack pack = HttpPack.create(url);
        String resultStr = (String) pack.get(resp -> {
            return HttpUtils.read(resp);
        });
        JsapiTicketBean jsapiTicketBean = WeixinJsonUtil.toObject(resultStr, JsapiTicketBean.class);
        config.setGet_jsapi_Time(System.currentTimeMillis());
        config.setJsapi_ticket(jsapiTicketBean.getTicket());
    }

    public static String getJsapi_ticket(String weChatAccount) throws WeixinException {
        WechatConfig config = configs.get(weChatAccount);
        if (StringUtils.isBlank(config.getAPPID())) {
            return null;
        }
        if (!config.isExpiredJsapi_ticket()) {
            return config.getJsapi_ticket();
        }

        synchronized (lock) {
            if (!config.isExpiredJsapi_ticket()) {
                return config.getJsapi_ticket();
            }
            try {
                _getJsapi_ticket(weChatAccount, config);
            } catch (Exception e) {
                try {
                    _getJsapi_ticket(weChatAccount, config);
                } catch (Exception ex) {
                    config.setJsapi_ticket(null);
                    config.setGet_jsapi_Time(0);
                    ex.printStackTrace();
                }
            }
        }
        if (!config.isExpiredJsapi_ticket()) {
            return config.getJsapi_ticket();
        }
        return null;
    }

    public static WeixinServerIpsBean getWeixinServerIps(String weChatAccount) throws WeixinException {
        try {
            String url = URL_WEIXIN_SERVER_IPS + "?access_token=" + getAccessToken(weChatAccount);
            HttpPack pack = HttpPack.create(url);
            String resultStr = (String) pack.get(resp -> {
                return HttpUtils.read(resp);
            });
            return WeixinJsonUtil.toObject(resultStr, WeixinServerIpsBean.class);
        } catch (Exception e) {
            throw new WeixinException(e);
        }
    }

    public static WechatConfig getWechatConfig(String weChatAccount) {
        getAccessToken(weChatAccount);
        return configs.get(weChatAccount);
    }

    public static String enterVerify(String timestamp, String nonce, String signature, String echostr,
            String weChatAccount) throws WeixinException {
        if (StringUtils.isBlank(echostr)) {
            throw new WeixinException("'echostr' can't be blank");
        }
        List<String> list = new ArrayList<String>(3);
        list.add(getWechatConfig(weChatAccount).getToken());
        list.add(timestamp);
        list.add(nonce);
        Collections.sort(list);
        String tmpStr = new WeixinSecurityUtil().encode(list.get(0) + list.get(1) + list.get(2),
                WeixinSecurityUtil.SHA_1);
        if (signature.equals(tmpStr)) {
            return echostr;
        } else {
            return "";
        }
    }

    public static Boolean msgVerify(String timestamp, String nonce, String signature, String weChatAccount) {
        List<String> list = new ArrayList<String>(3);
        list.add(getWechatConfig(weChatAccount).getToken());
        list.add(timestamp);
        list.add(nonce);
        Collections.sort(list);
        String tmpStr = new WeixinSecurityUtil().encode(list.get(0) + list.get(1) + list.get(2),
                WeixinSecurityUtil.SHA_1);
        return signature.equals(tmpStr);
    }
}
