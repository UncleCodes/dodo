package com.dodo.weixin.utils;

import java.io.File;

import org.apache.commons.codec.binary.Base64;

import com.dodo.utils.http.HttpPack;
import com.dodo.utils.http.HttpUtils;
import com.dodo.weixin.bean.qrcode.QrCodeCreateResult;
import com.dodo.weixin.bean.qrcode.TempQrCodeCreateBean;
import com.dodo.weixin.bean.qrcode.UnlimitQrCodeCreateBean;
import com.dodo.weixin.exception.WeixinException;

/**
 * 带参数二维码
 * 
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class WeixinQrCodeUtil {
    public static final String URL_CREATE_QRCODE          = "https://api.weixin.qq.com/cgi-bin/qrcode/create";
    public static final String URL_TRANS_TICKET_TO_QRCODE = "https://mp.weixin.qq.com/cgi-bin/showqrcode";

    /**
     * 创建临时二维码
     * 
     * @param json
     * @throws WeixinException
     */
    public static QrCodeCreateResult createTempQrCode(TempQrCodeCreateBean qrCodeCreateBean, String weChatAccount)
            throws WeixinException {
        try {
            String url = URL_CREATE_QRCODE + "?access_token=" + WeixinConfig.getAccessToken(weChatAccount);
            HttpPack pack = HttpPack.create(url).body(WeixinJsonUtil.toJackson(qrCodeCreateBean));
            String resultStr = (String) pack.post(resp -> {
                return HttpUtils.read(resp);
            });
            return WeixinJsonUtil.toObject(resultStr, QrCodeCreateResult.class);
        } catch (Exception e) {
            throw new WeixinException(e);
        }
    }

    /**
     * 创建永久二维码
     * 
     * @param json
     * @throws WeixinException
     */
    public static QrCodeCreateResult createUnlimitQrCode(UnlimitQrCodeCreateBean qrCodeCreateBean, String weChatAccount)
            throws WeixinException {
        try {
            String url = URL_CREATE_QRCODE + "?access_token=" + WeixinConfig.getAccessToken(weChatAccount);
            HttpPack pack = HttpPack.create(url).body(WeixinJsonUtil.toJackson(qrCodeCreateBean));
            String resultStr = (String) pack.post(resp -> {
                return HttpUtils.read(resp);
            });
            return WeixinJsonUtil.toObject(resultStr, QrCodeCreateResult.class);
        } catch (Exception e) {
            throw new WeixinException(e);
        }
    }

    /**
     * 获取二维码图片
     * 
     * @param json
     * @throws WeixinException
     */
    public static byte[] getQrCodeByTicketAsByteArr(String ticket) throws WeixinException {
        try {
            String url = URL_TRANS_TICKET_TO_QRCODE + "?ticket=" + ticket;
            HttpPack pack = HttpPack.create(url);
            byte[] byteRes = (byte[]) pack.get(resp -> {
                return HttpUtils.readBytes(resp);
            });
            return byteRes;
        } catch (Exception e) {
            throw new WeixinException(e);
        }
    }

    /**
     * 获取二维码图片
     * 
     * @param json
     * @throws WeixinException
     */
    public static String getQrCodeByTicketAsBase64Str(String ticket) throws WeixinException {
        try {
            String url = URL_TRANS_TICKET_TO_QRCODE + "?ticket=" + ticket;
            HttpPack pack = HttpPack.create(url);
            byte[] byteRes = (byte[]) pack.get(resp -> {
                return HttpUtils.readBytes(resp);
            });
            return Base64.encodeBase64String(byteRes);
        } catch (Exception e) {
            throw new WeixinException(e);
        }
    }

    /**
     * 获取二维码图片
     * 
     * @param json
     * @throws WeixinException
     */
    public static File getQrCodeByTicketAsFile(String ticket, File distFile) throws WeixinException {
        try {
            if (distFile.isDirectory()) {
                throw new WeixinException("distFile can not be a directory.");
            }
            String url = URL_TRANS_TICKET_TO_QRCODE + "?ticket=" + ticket;
            HttpPack pack = HttpPack.create(url);
            Boolean isSuccess = (Boolean) pack.get(resp -> {
                return HttpUtils.saveToFile(resp, distFile);
            });
            return isSuccess ? distFile : null;
        } catch (Exception e) {
            throw new WeixinException(e);
        }
    }
}
