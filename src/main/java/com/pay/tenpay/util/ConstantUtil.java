package com.pay.tenpay.util;

import java.io.IOException;
import java.util.Properties;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class ConstantUtil {
	 
	private static Properties ps  = new Properties();
	static{
		try {
			ps.load(ConstantUtil.class.getClassLoader().getResourceAsStream("pay_config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//初始化
	public static String APP_ID = ps.getProperty("tenpay.APP_ID");  //微信开发平台应用id
	
	public static String PARTNER = ps.getProperty("tenpay.PARTNER");//财付通商户号
	
	public static String PARTNER_KEY =  ps.getProperty("tenpay.PARTNER_KEY");//商户号对应的密钥
	
	//接收财付通通知的URL
	public static String NOTIFY_URL =  ps.getProperty("tenpay.NOTIFY_URL") ;
	
	
	public static String APP_ID_APP = "wx13da1841eb20c2ef";
	
	public static String PARTNER_APP = "1359169702";
	
	public static String PARTNER_KEY_APP =  "asuihjcmxnhiwjnkmsadasdnasuhdasj";
	
}
