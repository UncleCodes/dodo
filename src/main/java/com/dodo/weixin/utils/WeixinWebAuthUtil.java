package com.dodo.weixin.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;

import com.dodo.utils.http.HttpPack;
import com.dodo.utils.http.HttpUtils;
import com.dodo.weixin.WechatConfig;
import com.dodo.weixin.bean.CodeToTokenBean;
import com.dodo.weixin.bean.user.WebAuthUserInfo;
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
public class WeixinWebAuthUtil {

    public static final String webAuthUrl            = "https://open.weixin.qq.com/connect/oauth2/authorize?appid={0}&redirect_uri={1}&response_type=code&scope={2}&state={3}#wechat_redirect";
    public static final String USER_INFO_GET         = "https://api.weixin.qq.com/sns/userinfo?access_token={0}&openid={1}&lang=zh_CN";
    public static final String webAuthCodeToTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={0}&secret={1}&code={2}&grant_type=authorization_code";

    public static String getWebAuthUrlBase(String url, String state, String weChatAccount)
            throws UnsupportedEncodingException {
        WechatConfig config = WeixinConfig.getWechatConfig(weChatAccount);
        return MessageFormat.format(webAuthUrl, config.getAPPID(),
                URLEncoder.encode(config.getWeburl() + url, "utf-8"), "snsapi_base", state);
    }

    public static String getWebAuthUrlUserInfo(String url, String state, String weChatAccount)
            throws UnsupportedEncodingException {
        WechatConfig config = WeixinConfig.getWechatConfig(weChatAccount);
        return MessageFormat.format(webAuthUrl, config.getAPPID(),
                URLEncoder.encode(config.getWeburl() + url, "utf-8"), "snsapi_userinfo", state);
    }

    public static CodeToTokenBean getTokenFromCode(String code, String weChatAccount) throws WeixinException {
        try {
            WechatConfig config = WeixinConfig.getWechatConfig(weChatAccount);
            HttpPack pack = HttpPack.create(MessageFormat.format(webAuthCodeToTokenUrl, config.getAPPID(),
                    config.getSECRET(), code));
            String resultStr = (String) pack.get(resp -> {
                return HttpUtils.read(resp);
            });
            CodeToTokenBean codeToTokenBean = WeixinJsonUtil.toObject(resultStr, CodeToTokenBean.class);
            return codeToTokenBean;
        } catch (Exception e) {
            throw new WeixinException(e);
        }
    }

    /**
     * 查询用户基本信息
     * 
     * @return
     * @throws WeixinException
     */
    public static WebAuthUserInfo getWebAuthUserInfo(String accessToken, String userOpenId) throws WeixinException {
        try {
            String url = MessageFormat.format(USER_INFO_GET, accessToken, userOpenId);
            HttpPack pack = HttpPack.create(url);
            String resultStr = (String) pack.get(resp -> {
                return HttpUtils.read(resp);
            });
            return WeixinJsonUtil.toObject(resultStr, WebAuthUserInfo.class);
        } catch (Exception e) {
            throw new WeixinException(e);
        }
    }

    /**
     * 查询用户基本信息
     * 
     * @return
     * @throws WeixinException
     */
    public static WebAuthUserInfo getWebAuthUserInfo(CodeToTokenBean codeToTokenBean) throws WeixinException {
        return getWebAuthUserInfo(codeToTokenBean.getAccess_token(), codeToTokenBean.getOpenid());
    }
}
