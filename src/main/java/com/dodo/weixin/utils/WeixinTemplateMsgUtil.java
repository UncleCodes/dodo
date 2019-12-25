package com.dodo.weixin.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.dodo.utils.http.HttpPack;
import com.dodo.utils.http.HttpUtils;
import com.dodo.weixin.bean.ResultMsgBean;
import com.dodo.weixin.exception.WeixinException;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class WeixinTemplateMsgUtil {
    public static final String templateSendUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send";

    public static ResultMsgBean sendTemplateMsg(String json, String weChatAccount) throws WeixinException {
        try {
            String url = templateSendUrl + "?access_token=" + WeixinConfig.getAccessToken(weChatAccount);
            HttpPack pack = HttpPack.create(url).body(json);
            String resultStr = (String) pack.post(resp -> {
                return HttpUtils.read(resp);
            });
            ResultMsgBean menuOperationBean = WeixinJsonUtil.toObject(resultStr, ResultMsgBean.class);
            return menuOperationBean;
        } catch (Exception e) {
            throw new WeixinException(e);
        }
    }

    public static String getTemplateContent(Map<String, String> datas, String touser, String template_id, String url)
            throws JsonProcessingException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("touser", touser);
        map.put("template_id", template_id);
        map.put("url", url);
        map.put("topcolor", "#FF0000");

        Map<String, Object> dataMap = new HashMap<String, Object>();

        Iterator<String> keyIts = datas.keySet().iterator();

        String key = null;
        while (keyIts.hasNext()) {
            key = keyIts.next();
            Map<String, Object> firstDataMap = new HashMap<String, Object>();
            firstDataMap.put("value", datas.get(key));
            firstDataMap.put("color", "#173177");
            dataMap.put(key, firstDataMap);
        }

        map.put("data", dataMap);

        return WeixinJsonUtil.toJackson(map);
    }
}
