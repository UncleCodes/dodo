package com.dodo.weixin.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;

import com.dodo.utils.CommonUtil;
import com.dodo.utils.http.HttpPack;
import com.dodo.utils.http.HttpUtils;
import com.dodo.weixin.bean.media.MediaUploadResult;
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
public class WeixinMediaUtil {
    public enum MediaType {
        image, voice, video, thumb
    }

    public static final String DOWNLOADMEDIAURL = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token={0}&media_id={1}";
    public static final String UPLOADMEDIAURL   = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token={0}&type={1}";

    public static void downLoadMedia(String mediaId, String shopName, String smokeNumber, String weChatAccount)
            throws WeixinException {
        try {
            String urlStr = MessageFormat.format(DOWNLOADMEDIAURL, WeixinConfig.getAccessToken(weChatAccount), mediaId);
            String saveFile = CommonUtil.getWebRootPath() + "f1_file/" + shopName + "_" + smokeNumber + ".mp4";
            URL url = new URL(urlStr);
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();

            FileOutputStream fs = new FileOutputStream(saveFile);
            int byteread = 0;

            byte[] buffer = new byte[1204];
            while ((byteread = inStream.read(buffer)) != -1) {
                fs.write(buffer, 0, byteread);
            }
            fs.flush();
            inStream.close();
            fs.close();

        } catch (Exception e) {
            throw new WeixinException(e);
        }
    }

    public static MediaUploadResult uploadMedia(String weChatAccount, MediaType mediaType, File media)
            throws WeixinException {
        try {
            String urlStr = MessageFormat.format(UPLOADMEDIAURL, WeixinConfig.getAccessToken(weChatAccount),
                    mediaType.name());
            HttpPack pack = HttpPack.create(urlStr);
            pack.param("media", media);

            String resultStr = (String) pack.post(resp -> {
                return HttpUtils.read(resp);
            });
            return WeixinJsonUtil.toObject(resultStr, MediaUploadResult.class);
        } catch (Exception e) {
            throw new WeixinException(e);
        }
    }
}
