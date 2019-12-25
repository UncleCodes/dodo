package com.dodo.weixin.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang3.StringUtils;

import com.dodo.weixin.WechatConfig;
import com.dodo.weixin.exception.WeixinException;
import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;

/**
 * 加密算法
 * 
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class WeixinSecurityUtil {
    public static final String SHA_1   = "SHA-1";
    public static final String SHA_256 = "SHA-256";
    public static final String MD5     = "MD5";

    public String encode(String strSrc, String encodeType) {
        MessageDigest md = null;
        String strDes = null;
        byte[] bt = strSrc.getBytes();
        try {
            md = MessageDigest.getInstance(encodeType);
            md.update(bt);
            strDes = bytes2Hex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            return strSrc;
        }
        return strDes;
    }

    public String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }

    public static String getEncryptMsg(String replyMsg, String timestamp, String nonce, String weChatAccount)
            throws WeixinException {
        WechatConfig config = WeixinConfig.getWechatConfig(weChatAccount);
        if (StringUtils.isBlank(config.getLastEncodingAESKey()) || StringUtils.isBlank(config.getCurrEncodingAESKey())) {
            throw new WeixinException(
                    "'dodo.weixin.lastEncodingAESKey' and 'dodo.weixin.currEncodingAESKey' must be configured!");
        }
        WXBizMsgCrypt pc = null;
        try {
            pc = new WXBizMsgCrypt(config.getToken(), config.getCurrEncodingAESKey(), config.getAPPID());
            return pc.encryptMsg(replyMsg, timestamp, nonce);
        } catch (Exception e) {
            try {
                pc = new WXBizMsgCrypt(config.getToken(), config.getLastEncodingAESKey(), config.getAPPID());
                return pc.encryptMsg(replyMsg, timestamp, nonce);
            } catch (AesException e1) {
                e1.printStackTrace();
                throw new WeixinException(e);
            }
        }
    }

    public static String getDecryptMsg(String revieveMsg, String msgSignature, String timestamp, String nonce,
            String weChatAccount) throws WeixinException {
        WechatConfig config = WeixinConfig.getWechatConfig(weChatAccount);
        if (StringUtils.isBlank(config.getLastEncodingAESKey()) || StringUtils.isBlank(config.getCurrEncodingAESKey())) {
            throw new WeixinException(
                    "'dodo.weixin.lastEncodingAESKey' and 'dodo.weixin.currEncodingAESKey' must be configured!");
        }
        WXBizMsgCrypt pc = null;
        try {
            pc = new WXBizMsgCrypt(config.getToken(), config.getCurrEncodingAESKey(), config.getAPPID());
            return pc.decryptMsg(msgSignature, timestamp, nonce, revieveMsg);
        } catch (Exception e) {
            try {
                pc = new WXBizMsgCrypt(config.getToken(), config.getLastEncodingAESKey(), config.getAPPID());
                return pc.decryptMsg(msgSignature, timestamp, nonce, revieveMsg);
            } catch (AesException e1) {
                e1.printStackTrace();
                throw new WeixinException(e);
            }
        }
    }
}