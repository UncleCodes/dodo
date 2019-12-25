package com.pay.tenpay.pack;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.pay.tenpay.util.MD5Util;
import com.pay.tenpay.util.WXUtil;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class WXPay {
   
	public static SortedMap<String, String> createPackageValueJSAPIAsMap(String appid, String appKey, String prepay_id)  {
		SortedMap<String, String> nativeObj = new TreeMap<String, String>();
		nativeObj.put("appId", appid);
		String randomStr = WXUtil.getNonceStr();
		nativeObj.put("nonceStr", MD5Util.MD5Encode(randomStr, "utf-8").toLowerCase());
		nativeObj.put("package", "prepay_id=" + prepay_id);
		nativeObj.put("signType", "MD5");
		nativeObj.put("timeStamp", WXUtil.getTimeStamp());
		nativeObj.put("paySign", createSign(nativeObj, appKey));
		return nativeObj;
	}
	
	public static PrepayPackJSAPI createPackageValueJSAPI(String appid, String appKey, String prepay_id)  {
		SortedMap<String, String> nativeObj = createPackageValueJSAPIAsMap(appid, appKey, prepay_id);
		PrepayPackJSAPI packJSAPI = new PrepayPackJSAPI();
		packJSAPI.setAppId(nativeObj.get("appId"));
		packJSAPI.setNonceStr(nativeObj.get("nonceStr"));
		packJSAPI.setPrepayId(nativeObj.get("package"));
		packJSAPI.setSignType(nativeObj.get("signType"));
		packJSAPI.setTimeStamp(nativeObj.get("timeStamp"));
		packJSAPI.setPaySign(nativeObj.get("paySign"));
		return packJSAPI;
	}
	
	 
	public static PrepayPackAPP createPackageValueAPP(String appid, String partnerid, String appKey, String prepay_id)  {
		SortedMap<String, String> nativeObj = createPackageValueAPPAsMap(appid, partnerid,appKey, prepay_id);
		PrepayPackAPP packAPP = new PrepayPackAPP();
		packAPP.setAppId(nativeObj.get("appid"));
		packAPP.setNonceStr(nativeObj.get("noncestr"));
		packAPP.setPrepayId(nativeObj.get("prepayid"));
		packAPP.setSignType("MD5");
		packAPP.setTimeStamp(nativeObj.get("timestamp"));
		packAPP.setPaySign(nativeObj.get("sign"));
		packAPP.setPartnerid(nativeObj.get("partnerid"));
		return packAPP;
	}
	
	
	public static SortedMap<String, String> createPackageValueAPPAsMap(String appid, String partnerid,String appKey, String prepay_id)  {
		SortedMap<String, String> treeMap = new TreeMap<String, String>();
		treeMap.put("appid", appid);
		treeMap.put("noncestr", WXUtil.getNonceStr());
		treeMap.put("package", "Sign=WXPay");
		treeMap.put("partnerid", partnerid);
		treeMap.put("prepayid", prepay_id);
		treeMap.put("timestamp", WXUtil.getTimeStamp());
		treeMap.put("sign", createSign(treeMap, appKey));
		return treeMap;
	}

	/**
	 * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
	 */
	public static String createSign(SortedMap<String, String> packageParams, String AppKey) {
		StringBuffer sb = new StringBuffer();
		Set<Entry<String, String>> es = packageParams.entrySet();
		Iterator<Entry<String, String>> it = es.iterator();
		while (it.hasNext()) {
			Entry<String, String> entry = it.next();
			String k =  entry.getKey();
			String v =  entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + AppKey);
		String sign = MD5Util.MD5Encode(sb.toString(), "UTF-8").toUpperCase();
		return sign;
	}
}
