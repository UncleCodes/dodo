package com.pay.action.front;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.dodo.utils.CommonUtil;
import com.dodo.utils.RespData;
import com.dodo.utils.http.HttpUtils;
import com.pay.PayBusiType;
import com.pay.alipay.AliPay;
import com.pay.tenpay.PayConfig;
import com.pay.tenpay.TenpayAccount;
import com.pay.tenpay.TenpayAccount.AccountType;
import com.pay.tenpay.pack.PayPack;
import com.pay.tenpay.pack.PayUtil;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
@Controller
public class PayAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(PayAction.class);

    @RequestMapping(value = "/alipay/wap/paynow.htm", method = RequestMethod.GET)
    public String givealipay(Model model, HttpServletRequest request, String tradeNo) {
        PayBusiType payBusiType = PayBusiType.BUSI1;
        String remark = "Test";//query
        BigDecimal total_fee = new BigDecimal(1000);//query
        String sHtmlText = "唤起支付网关失败";
        try {
            sHtmlText = AliPay.wapPay("http://www.mydomain.com/pay/alipay/end/" + payBusiType.name(), tradeNo,
                    new DecimalFormat("0.##").format(total_fee), remark, payBusiType);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        model.addAttribute("body", sHtmlText);
        return "alipay/alipay_page";
    }

    @RequestMapping(value = "/alipay/end/{payBusiType}.htm")
    public String givealipayend(Model model, HttpServletRequest request, String sign, BigDecimal total_amount,
            String trade_no, String sign_type, String charset, String auth_app_id, String app_id, String method,
            String seller_id, String out_trade_no, String version, @PathVariable String payBusiType) {
        AlipayTradeQueryRequest alipayTradeQueryRequest = new AlipayTradeQueryRequest();//创建API对应的request类
        alipayTradeQueryRequest.setBizContent("{" + "\"out_trade_no\":\"" + out_trade_no + "\"," + "\"trade_no\":\""
                + trade_no + "\"" + "}");
        try {
            AlipayTradeQueryResponse response = AliPay.alipayClient.execute(alipayTradeQueryRequest);
            String trade_status = response.getTradeStatus();
            if ("TRADE_FINISHED".equals(trade_status) || "TRADE_SUCCESS".equals(trade_status)) {
                model.addAttribute("payBusiType", payBusiType);
                model.addAttribute("out_trade_no", out_trade_no);
                return "alipay/alipay_end";
            } else {
                model.addAttribute("errorMsg", "您还没有支付该订单~");
                return "alipay/error";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            model.addAttribute("errorMsg", "获取支付宝订单状态失败~");
            return "alipay/error";
        }
    }

    // 微信支付：获取预支付订单
    @RequestMapping("/ten_pay/prepay.json")
    @ResponseBody
    public RespData tenpayPrepay(HttpServletRequest request, String accountName, String tradeNo, String openId) {
        // 待支付的业务类型
        PayBusiType payBusiType = PayBusiType.BUSI1;
        // 商品名称
        String remark = "Test";//query
        // 支付的金额，单位：分
        BigDecimal total_fee = new BigDecimal(1000);//query
        // 收款账户
        TenpayAccount account = PayConfig.getTenpayAccount(accountName);
        // 打包支付请求
        PayPack pack = new PayPack();
        // 系统内部订单号
        pack.setOut_trade_no(tradeNo);
        // 支付的业务类型
        pack.setPayBusiType(payBusiType);
        // 商品名称
        pack.setRemark(remark);
        // 待支付金额
        pack.setTotal_fee(total_fee);
        // 支付成功后的回调URL
        pack.setNotifyUrl(account.getNotifyUrl());
        // APP支付，获取预支付订单
        if (account.getType() == AccountType.APP) {
            return RespData.success(PayUtil.makeWeixinPrepayOrderAPP(pack, request, accountName));
        }
        // JSAPI支付，获取预支付订单
        else {
            return RespData.success(PayUtil.makeWeixinPrepayOrderJSAPI(openId, pack, request, accountName));
        }
    }

    // 微信支付回调
    @RequestMapping("/ten_pay/notify.dhtml")
    public void tenpayNotify(HttpServletRequest request, HttpServletResponse response) {
        try {
            BufferedInputStream buf = null;
            buf = new BufferedInputStream(request.getInputStream());
            byte[] buffer = new byte[1024];
            StringBuffer data = new StringBuffer("");
            int a;
            while ((a = buf.read(buffer)) != -1) {
                data.append(new String(buffer, 0, a, "gbk"));
            }

            LOGGER.info("/tenpay_notify = {}", data.toString());

            Element rootElement = null;
            rootElement = DocumentHelper.parseText(data.toString()).getRootElement();
            String return_code = rootElement.elementText("return_code");
            if ("SUCCESS".equals(return_code)) {
                String result_code = rootElement.elementText("result_code");
                if ("SUCCESS".equals(result_code)) {
                    // String appid = rootElement.elementText("appid");
                    String bank_type = rootElement.elementText("bank_type");
                    // String fee_type = rootElement.elementText("fee_type");
                    String is_subscribe = rootElement.elementText("is_subscribe");
                    // String mch_id = rootElement.elementText("mch_id");
                    // String nonce_str = rootElement.elementText("nonce_str");
                    String openid = rootElement.elementText("openid");
                    String out_trade_no = rootElement.elementText("out_trade_no");
                    // String sign = rootElement.elementText("sign");
                    String time_end = rootElement.elementText("time_end");
                    Integer total_fee = Integer.parseInt(rootElement.elementText("total_fee"));
                    String trade_type = rootElement.elementText("trade_type");
                    String transaction_id = rootElement.elementText("transaction_id");
                    String attach = rootElement.elementText("attach");

                    PayBusiType payBusiType = PayBusiType.valueOf(attach);
                    String ipAddr = HttpUtils.getRemoteAddr(request);
                    String browser = HttpUtils.getBrowser(request);
                    String payRemark = trade_type + "|" + openid + "|" + is_subscribe + "|" + transaction_id + "|"
                            + time_end + "|" + bank_type + "|" + total_fee;
                    LOGGER.info(out_trade_no);
                    LOGGER.info(ipAddr);
                    LOGGER.info(browser);
                    LOGGER.info(payRemark);
                    // 业务1的回调
                    if (payBusiType == PayBusiType.BUSI1) {

                    }
                    // 业务2的回调
                    else if (payBusiType == PayBusiType.BUSI2) {
                    }
                }
            }
            try {
                PayUtil.notifyBackSuccess("Ok", response);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                PayUtil.notifyBackFail("Error", response);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    //支付宝回调
    @RequestMapping("/alipay/mobile/notify.dhtml")
    public void alipayNotify(Model model, HttpServletRequest request, HttpServletResponse response,
            String buyer_logon_id, String buyer_id, String seller_email, String seller_id, String subject,
            String out_trade_no, String trade_no, String trade_status, BigDecimal total_amount, String passback_params) {

        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }

        boolean signVerified = false;
        try {
            signVerified = AlipaySignature.rsaCheckV1(params, AliPay.ALIPAY_PUBLIC_KEY, AliPay.CHARSET);
        } catch (AlipayApiException e1) {
            e1.printStackTrace();
        }
        if (signVerified) {
            LOGGER.info("givealipay_notiify params=={}", params);

            BigDecimal needMoney = new BigDecimal(10000);

            if (total_amount.doubleValue() < needMoney.doubleValue()) {
                try {
                    LOGGER.info("金额支付不够的不进行处理");
                    response.getWriter().print("success");
                    response.getWriter().flush();
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if ("TRADE_FINISHED".equals(trade_status) || "TRADE_SUCCESS".equals(trade_status)) {
                PayBusiType payBusiType = PayBusiType.valueOf(passback_params);
                String ipAddr = HttpUtils.getRemoteAddr(request);
                String browser = HttpUtils.getBrowser(request);
                String payRemark = "buyer_logon_id:" + buyer_logon_id + "|" + "buyer_id:" + buyer_id + "|"
                        + "trade_no:" + trade_no + "|" + "pay_date:"
                        + CommonUtil.getSpecialDateStr(new Date(), "yyyy-MM-dd HH:mm:ss");
                LOGGER.info(payBusiType.toString());
                LOGGER.info(ipAddr);
                LOGGER.info(browser);
                LOGGER.info(payRemark);

                // 业务1的回调
                if (payBusiType == PayBusiType.BUSI1) {

                }
                // 业务2的回调
                else if (payBusiType == PayBusiType.BUSI2) {
                }

            } else if (trade_status.equals("WAIT_BUYER_PAY")) {
                //交易创建，等待买家付款
                //业务处理 不处理
            } else if (trade_status.equals("TRADE_CLOSED")) {
                // 在指定时间段内未支付时关闭的交易 
                // 在交易完成全额退款成功时关闭的交易。
                // 不处理
            }

            try {
                response.getWriter().print("success");
                response.getWriter().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                response.getWriter().print("failure");
                response.getWriter().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
