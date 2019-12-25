package com.pay.tenpay.pack;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dodo.utils.http.HttpUtils;
import com.pay.PayBusiType;
import com.pay.tenpay.PayConfig;
import com.pay.tenpay.TenpayAccount;
import com.pay.tenpay.util.WXUtil;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class PayUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(PayUtil.class);
    private static String notufy_back_template = "<xml>" + "  <return_code><![CDATA[{0}]]></return_code>" + "  <return_msg><![CDATA[{1}]]></return_msg>" + "</xml>";

    public static void notifyBackSuccess(String msg, HttpServletResponse response) throws IOException {
        String strHtml = MessageFormat.format(notufy_back_template, "SUCCESS", msg);
        PrintWriter out = response.getWriter();
        out.println(strHtml);
        out.flush();
        out.close();
    }

    public static void notifyBackFail(String msg, HttpServletResponse response) throws IOException {
        String strHtml = MessageFormat.format(notufy_back_template, "FAIL", msg);
        PrintWriter out = response.getWriter();
        out.println(strHtml);
        out.flush();
        out.close();
    }

    public static PrepayPackJSAPI makeWeixinPrepayOrderJSAPI(String openId, PayPack pack, HttpServletRequest request, String accountName) {
        TenpayAccount account = PayConfig.getTenpayAccount(accountName);

        PrepayPackJSAPI packJSAPI = null;
        // 单位：分
        Integer total_fee = pack.getTotal_fee().intValue();
        // 商品描述
        String remark = pack.getRemark();
        // 第三方订单号
        String out_trade_no = pack.getOut_trade_no();
        // 回调URL
        String notifyUrl = pack.getNotifyUrl();
        // 业务类型
        PayBusiType payBusiType = pack.getPayBusiType();

        WXPrepay wxPrepay = new WXPrepay();
        wxPrepay.setAppid(account.getAppId());
        wxPrepay.setMch_id(account.getPartner());
        wxPrepay.setNonce_str(WXUtil.getNonceStr());
        wxPrepay.setBody(remark);
        wxPrepay.setOut_trade_no(out_trade_no);
        wxPrepay.setTotal_fee(total_fee + "");
        wxPrepay.setSpbill_create_ip(HttpUtils.getRemoteAddr(request));
        wxPrepay.setNotify_url(notifyUrl);
        wxPrepay.setTrade_type("JSAPI");
        wxPrepay.setPartnerKey(account.getPartnerKey());
        wxPrepay.setAttach(payBusiType.name());
        wxPrepay.setOpenid(openId);

        String prepayid = wxPrepay.submitXmlGetPrepayId();

        LOGGER.info("HttpUtils.getRemoteAddr(request) ----》" + HttpUtils.getRemoteAddr(request));

        if (null != prepayid && !"".equals(prepayid)) {
            packJSAPI = WXPay.createPackageValueJSAPI(account.getAppId(), account.getPartnerKey(), prepayid);
        } else {
            packJSAPI = PrepayPackJSAPI.fail(-2, "获取prepayId失败");
        }

        return packJSAPI;
    }

    public static PrepayPackAPP makeWeixinPrepayOrderAPP(PayPack pack, HttpServletRequest request, String accountName) {
        TenpayAccount account = PayConfig.getTenpayAccount(accountName);

        PrepayPackAPP packAPP = null;
        // 单位：分
        Integer total_fee = pack.getTotal_fee().intValue();
        // 商品描述
        String remark = pack.getRemark();
        // 第三方订单号
        String out_trade_no = pack.getOut_trade_no();
        // 回调URL
        String notifyUrl = pack.getNotifyUrl();

        // 业务类型
        PayBusiType payBusiType = pack.getPayBusiType();

        WXPrepay wxPrepay = new WXPrepay();
        wxPrepay.setAppid(account.getAppId());
        wxPrepay.setMch_id(account.getPartner());
        wxPrepay.setNonce_str(WXUtil.getNonceStr());
        wxPrepay.setBody(remark);
        wxPrepay.setOut_trade_no(out_trade_no);
        wxPrepay.setTotal_fee(total_fee + "");
        wxPrepay.setSpbill_create_ip(HttpUtils.getRemoteAddr(request));
        wxPrepay.setNotify_url(notifyUrl);
        wxPrepay.setTrade_type("APP");
        wxPrepay.setPartnerKey(account.getPartnerKey());
        wxPrepay.setAttach(payBusiType.name());

        String prepayid = wxPrepay.submitXmlGetPrepayId();

        LOGGER.info("HttpUtils.getRemoteAddr(request) ----》" + HttpUtils.getRemoteAddr(request));

        if (null != prepayid && !"".equals(prepayid)) {
            packAPP = WXPay.createPackageValueAPP(account.getAppId(), account.getPartner(), account.getPartnerKey(), prepayid);
        } else {
            packAPP = PrepayPackAPP.fail(-2, "获取prepayId失败");
        }

        return packAPP;
    }
}
