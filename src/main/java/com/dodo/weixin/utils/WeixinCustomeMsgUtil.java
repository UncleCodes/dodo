package com.dodo.weixin.utils;

import com.dodo.utils.http.HttpPack;
import com.dodo.utils.http.HttpUtils;
import com.dodo.weixin.bean.ResultMsgBean;
import com.dodo.weixin.exception.WeixinException;
import com.dodo.weixin.msg.ResponseMsgConvertFactory;
import com.dodo.weixin.msg.custome.ResponseCustome;
import com.dodo.weixin.msg.response.ResponseMsg;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class WeixinCustomeMsgUtil {

    public static final String customMsgSendUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=";

    public static ResultMsgBean sendResponseMsgAsCustomeMsg(ResponseMsg msg, String weChatAccount)
            throws WeixinException {
        if (msg == null) {
            return null;
        }
        try {
            String url = customMsgSendUrl + WeixinConfig.getAccessToken(weChatAccount);
            HttpPack pack = HttpPack.create(url).body(
                    WeixinJsonUtil.toJackson(ResponseMsgConvertFactory.convertToCustome(msg)));
            String resultStr = (String) pack.post(resp -> {
                return HttpUtils.read(resp);
            });
            ResultMsgBean operationBean = WeixinJsonUtil.toObject(resultStr, ResultMsgBean.class);
            return operationBean;
        } catch (Exception e) {
            throw new WeixinException(e);
        }
    }

    public static ResultMsgBean sendCustomeMsg(ResponseCustome msg, String weChatAccount) throws WeixinException {
        if (msg == null) {
            return null;
        }
        try {
            String url = customMsgSendUrl + WeixinConfig.getAccessToken(weChatAccount);
            HttpPack pack = HttpPack.create(url).body(WeixinJsonUtil.toJackson(msg));
            String resultStr = (String) pack.post(resp -> {
                return HttpUtils.read(resp);
            });
            ResultMsgBean operationBean = WeixinJsonUtil.toObject(resultStr, ResultMsgBean.class);
            return operationBean;
        } catch (Exception e) {
            throw new WeixinException(e);
        }
    }
}
