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
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class WechatQrCodeUtil {
    public static final String URL_CREATE_QRCODE          = "https://api.weixin.qq.com/cgi-bin/qrcode/create";
    public static final String URL_TRANS_TICKET_TO_QRCODE = "https://mp.weixin.qq.com/cgi-bin/showqrcode";

    /**
     * 创建临时二维码
     * 
     */
    public static QrCodeCreateResult createTempQrCode(TempQrCodeCreateBean qrCodeCreateBean, String weChatAccount) {
        try {
            String url = URL_CREATE_QRCODE + "?access_token=" + WeixinConfig.getAccessToken(weChatAccount);
            HttpPack pack = HttpPack.create(url).body(WeixinJsonUtil.toJackson(qrCodeCreateBean));
            String resultStr = (String) pack.post(resp -> {
                return HttpUtils.read(resp);
            });
            return WeixinJsonUtil.toObject(resultStr, QrCodeCreateResult.class);
        } catch (Exception e) {
            e.printStackTrace();
            QrCodeCreateResult operationBean = new QrCodeCreateResult();
            operationBean.setErrcode(-2);
            operationBean.setErrmsg(e.getMessage());
            return operationBean;
        }
    }

    /**
     * 创建永久二维码
     * 
     */
    public static QrCodeCreateResult createUnlimitQrCode(UnlimitQrCodeCreateBean qrCodeCreateBean, String weChatAccount) {
        try {
            String url = URL_CREATE_QRCODE + "?access_token=" + WeixinConfig.getAccessToken(weChatAccount);
            HttpPack pack = HttpPack.create(url).body(WeixinJsonUtil.toJackson(qrCodeCreateBean));
            String resultStr = (String) pack.post(resp -> {
                return HttpUtils.read(resp);
            });
            return WeixinJsonUtil.toObject(resultStr, QrCodeCreateResult.class);
        } catch (Exception e) {
            e.printStackTrace();
            QrCodeCreateResult operationBean = new QrCodeCreateResult();
            operationBean.setErrcode(-2);
            operationBean.setErrmsg(e.getMessage());
            return operationBean;
        }
    }

    /**
     * 获取二维码图片
     * 
     */
    public static byte[] getQrCodeByTicketAsByteArr(String ticket) {
        try {
            String url = URL_TRANS_TICKET_TO_QRCODE + "?ticket=" + ticket;
            HttpPack pack = HttpPack.create(url);
            byte[] byteRes = (byte[]) pack.post(resp -> {
                return HttpUtils.readBytes(resp);
            });
            return byteRes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取二维码图片
     * 
     */
    public static String getQrCodeByTicketAsBase64Str(String ticket) {
        try {
            return Base64.encodeBase64String(getQrCodeByTicketAsByteArr(ticket));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取二维码图片
     * 
     */
    public static File getQrCodeByTicketAsFile(String ticket, File distFile) {
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
            e.printStackTrace();
            return null;
        }
    }

    public static String getTempQrCodeWithSceneId(Integer scene_id, Integer expire_seconds, String weChatAccount) {
        TempQrCodeCreateBean qrCodeCreateBean = new TempQrCodeCreateBean(expire_seconds, scene_id);
        QrCodeCreateResult createResult = createTempQrCode(qrCodeCreateBean, weChatAccount);
        if (createResult.getErrcode().intValue() == 0) {

            return getQrCodeByTicketAsBase64Str(createResult.getTicket());
        }
        return null;
    }

    public static String getUnlimitQrCodeWithSceneId(Integer scene_id, String weChatAccount) {
        UnlimitQrCodeCreateBean qrCodeCreateBean = new UnlimitQrCodeCreateBean(scene_id);
        QrCodeCreateResult createResult = createUnlimitQrCode(qrCodeCreateBean, weChatAccount);
        if (createResult.getErrcode().intValue() == 0) {
            return getQrCodeByTicketAsBase64Str(createResult.getTicket());
        }
        return null;
    }

}
