package com.pay.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.pay.PayBusiType;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class AliPay {
    public static final String ALIPAY_GATEWAY    = "https://openapi.alipay.com/gateway.do";
    public static final String APP_ID            = "";
    public static final String APP_PRIVATE_KEY   = "";
    public static final String CHARSET           = "UTF-8";
    public static final String ALIPAY_PUBLIC_KEY = "";
    public static AlipayClient alipayClient      = new DefaultAlipayClient(ALIPAY_GATEWAY, APP_ID, APP_PRIVATE_KEY,
                                                         "json", CHARSET, ALIPAY_PUBLIC_KEY);

    public static String wapPay(String jumpUrl, String out_trade_no, String total_amount, String subject,
            PayBusiType payBusiType) throws AlipayApiException {
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
        alipayRequest.setReturnUrl(jumpUrl);
        alipayRequest.setNotifyUrl("http://www.mydomain.com/pay/alipay/mobile/notify");//在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent("{" + "    \"out_trade_no\":\"" + out_trade_no + "\","
                + "    \"passback_params\":\"" + payBusiType.name() + "\"," + "    \"total_amount\":" + total_amount
                + "," + "    \"subject\":\"" + subject + "\"," + "    \"seller_id\":\"2088002670340143\","
                + "    \"product_code\":\"QUICK_WAP_PAY\"" + "  }");//填充业务参数
        String form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        return form;
    }
}
