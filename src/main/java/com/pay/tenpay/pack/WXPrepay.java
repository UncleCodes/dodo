package com.pay.tenpay.pack;

import java.util.Map;
import java.util.TreeMap;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dodo.utils.http.HttpClientPool;
import com.pay.tenpay.util.MD5Util;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class WXPrepay {
	private static final Logger LOGGER = LoggerFactory.getLogger(WXPrepay.class);
	private static String unifiedorder = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	private String appid;
	private String mch_id;
	private String nonce_str;
	private String body;
	private String out_trade_no;
	private String total_fee;
	private String spbill_create_ip;
	private String trade_type;
	private String notify_url;
	private String sign;
	private String partnerKey;
	private String attach;
	private String openid;
	// 预支付订单号
	private String prepay_id;

	/**
	 * 预支付订单
	 * 
	 * @return
	 */
	public String submitXmlGetPrepayId() {
		// HttpClient
		HttpClient httpClient = HttpClientPool.getConnection();
		HttpPost httpPost = new HttpPost(unifiedorder);
		String xml = getPackage();
		StringEntity entity;
		try {
			entity = new StringEntity(xml, "utf-8");
			httpPost.setEntity(entity);
			HttpResponse httpResponse;
			// post请求
			httpResponse = httpClient.execute(httpPost);
			int status = httpResponse.getStatusLine().getStatusCode();  
			if (status < 200 || status >= 300) {  
				httpPost.abort();
			}else{
				HttpEntity httpEntity = httpResponse.getEntity();
				if (httpEntity != null) {
					// 打印响应内容
					String result = EntityUtils.toString(httpEntity, "UTF-8");
					
					LOGGER.info(result);
					// 过滤
					result = result.replaceAll("<![CDATA[|]]>", "");
					String prepay_id = Jsoup.parse(result).select("prepay_id").html();
					this.prepay_id = prepay_id;
					if (prepay_id != null)
						return prepay_id;
				}
				EntityUtils.consume(httpResponse.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
			httpPost.abort();
		}
		return prepay_id;
	}

	public String getPackage() {
		TreeMap<String, String> treeMap = new TreeMap<String, String>();
		treeMap.put("appid", this.appid);
		treeMap.put("mch_id", this.mch_id);
		treeMap.put("nonce_str", this.nonce_str);
		treeMap.put("body", this.body);
		treeMap.put("out_trade_no", this.out_trade_no);
		treeMap.put("total_fee", this.total_fee);
		treeMap.put("spbill_create_ip", this.spbill_create_ip);
		treeMap.put("trade_type", this.trade_type);
		treeMap.put("notify_url", this.notify_url);
		treeMap.put("attach", this.attach);
		if("JSAPI".equals(this.trade_type)){
			treeMap.put("openid", this.openid);
		}
		StringBuilder sb = new StringBuilder();
		for (String key : treeMap.keySet()) {
			sb.append(key).append("=").append(treeMap.get(key)).append("&");
		}
		sb.append("key=" + partnerKey);
		
		LOGGER.info("---------"+sb.toString()+"----------");
		
		sign = MD5Util.MD5Encode(sb.toString(), "utf-8").toUpperCase();
		LOGGER.info("sign:"+sign);
		
		treeMap.put("sign", sign);
		StringBuilder xml = new StringBuilder();
		xml.append("<xml>\n");
		for (Map.Entry<String, String> entry : treeMap.entrySet()) {
			if ("body".equals(entry.getKey()) || "sign".equals(entry.getKey())) {
				xml.append("<" + entry.getKey() + "><![CDATA[").append(entry.getValue()).append("]]></" + entry.getKey() + ">\n");
			} else {
				xml.append("<" + entry.getKey() + ">").append(entry.getValue()).append("</" + entry.getKey() + ">\n");
			}
		}
		xml.append("</xml>");
		LOGGER.info(xml.toString());
		return xml.toString();
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}

	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getPartnerKey() {
		return partnerKey;
	}

	public void setPartnerKey(String partnerKey) {
		this.partnerKey = partnerKey;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getSign() {
		return sign;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}
	
	public void setOpenid(String openid) {
		this.openid = openid;
	}
}
