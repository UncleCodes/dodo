package com.dodo.weixin.utils;

import com.dodo.utils.http.HttpPack;
import com.dodo.utils.http.HttpUtils;
import com.dodo.weixin.bean.multicustservice.CustServiceRecordResult;
import com.dodo.weixin.bean.multicustservice.PagerForRecord;
import com.dodo.weixin.exception.WeixinException;
import com.dodo.weixin.msg.request.RequestMsg;
import com.dodo.weixin.msg.response.ResponseMsg;
import com.dodo.weixin.msg.response.ResponseMsgTransferCustomerService;
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
public class WeixinMultiCustomerUtil {
    public static final String CUST_SERVICE_RECOED = "https://api.weixin.qq.com/cgi-bin/customservice/getrecord";

    /**
     * 获取转接多客服消息，直接发送
     */
    public static ResponseMsg getTransferCustomerResponse(RequestMsg requestMsg) {
        ResponseMsg responseMsg = new ResponseMsgTransferCustomerService();
        responseMsg.setFromUserName(requestMsg.getToUserName());
        responseMsg.setToUserName(requestMsg.getFromUserName());
        return responseMsg;
    }

    /**
     * 查询客服聊天记录
     * 
     * @return
     * @throws WeixinException
     */
    public static CustServiceRecordResult getRecord(String json, String weChatAccount) throws WeixinException {
        try {
            String url = CUST_SERVICE_RECOED + "?access_token=" + WeixinConfig.getAccessToken(weChatAccount);
            HttpPack pack = HttpPack.create(url).body(json);
            String resultStr = (String) pack.post(resp -> {
                return HttpUtils.read(resp);
            });
            return WeixinJsonUtil.toObject(resultStr, CustServiceRecordResult.class);
        } catch (Exception e) {
            throw new WeixinException(e);
        }
    }

    /**
     * 查询客服聊天记录
     * 
     * @return
     * @throws WeixinException
     */
    public static CustServiceRecordResult getRecord(PagerForRecord pager, String weChatAccount) throws WeixinException {
        try {
            return getRecord(WeixinJsonUtil.toJackson(pager), weChatAccount);
        } catch (JsonProcessingException e) {
            throw new WeixinException(e);
        }
    }
}
