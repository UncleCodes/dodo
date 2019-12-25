package com.dodo.weixin.utils;

import java.util.HashMap;
import java.util.Map;

import com.dodo.utils.http.HttpPack;
import com.dodo.utils.http.HttpUtils;
import com.dodo.weixin.bean.url.UrlLong2ShortResult;
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
public class WeixinUrlUtil {
    public static final String URL_LONG_2_SHORT = "https://api.weixin.qq.com/cgi-bin/shorturl";

    /**
     * 长URL 转 短URL
     * 
     * @param longurl
     * @throws WeixinException
     */
    public static UrlLong2ShortResult long2short(String longurl, String weChatAccount) throws WeixinException {
        try {
            Map<String, String> args = new HashMap<String, String>(0);
            args.put("action", "long2short");
            args.put("long_url", longurl);
            String url = URL_LONG_2_SHORT + "?access_token=" + WeixinConfig.getAccessToken(weChatAccount);
            HttpPack pack = HttpPack.create(url).body(WeixinJsonUtil.toJackson(args));
            String resultStr = (String) pack.post(resp -> {
                return HttpUtils.read(resp);
            });
            return WeixinJsonUtil.toObject(resultStr, UrlLong2ShortResult.class);
        } catch (Exception e) {
            throw new WeixinException(e);
        }
    }
}
