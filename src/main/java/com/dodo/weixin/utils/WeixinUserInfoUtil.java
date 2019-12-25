package com.dodo.weixin.utils;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dodo.utils.JacksonUtil;
import com.dodo.utils.http.HttpPack;
import com.dodo.utils.http.HttpUtils;
import com.dodo.weixin.bean.ResultMsgBean;
import com.dodo.weixin.bean.user.SubscribeUserList;
import com.dodo.weixin.bean.user.UserBasicInfo;
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
public class WeixinUserInfoUtil {
    public static final String USER_REMARK_MODIFY  = "https://api.weixin.qq.com/cgi-bin/user/info/updateremark";
    public static final String USER_INFO_GET       = "https://api.weixin.qq.com/cgi-bin/user/info?access_token={0}&openid={1}&lang=zh_CN";
    public static final String SUBSCRIBE_USER_LIST = "https://api.weixin.qq.com/cgi-bin/user/get?access_token={0}&next_openid={1}";

    /**
     * 修改用户备注名
     * 
     * @return
     * @throws WeixinException
     */
    public static ResultMsgBean modifyUserRemark(String userOpenId, String remark, String weChatAccount)
            throws WeixinException {
        try {
            Map<String, Object> postMap = new HashMap<String, Object>(2);
            postMap.put("openid", userOpenId);
            postMap.put("remark", remark);
            String url = USER_REMARK_MODIFY + "?access_token=" + WeixinConfig.getAccessToken(weChatAccount);
            HttpPack pack = HttpPack.create(url).body(JacksonUtil.toJackson(postMap));
            String resultStr = (String) pack.post(resp -> {
                return HttpUtils.read(resp);
            });
            return WeixinJsonUtil.toObject(resultStr, ResultMsgBean.class);
        } catch (Exception e) {
            throw new WeixinException(e);
        }
    }

    /**
     * 查询用户基本信息
     * 
     * @return
     * @throws WeixinException
     */
    public static UserBasicInfo get(String userOpenId, String weChatAccount) throws WeixinException {
        try {
            String url = MessageFormat.format(USER_INFO_GET, WeixinConfig.getAccessToken(weChatAccount), userOpenId);
            HttpPack pack = HttpPack.create(url);
            String resultStr = (String) pack.get(resp -> {
                return HttpUtils.read(resp);
            });
            return WeixinJsonUtil.toObject(resultStr, UserBasicInfo.class);
        } catch (Exception e) {
            throw new WeixinException(e);
        }
    }

    /**
     * 查询关注者列表
     * 
     * @return
     * @throws WeixinException
     */
    public static SubscribeUserList getSubscribeUserList(String next_openid, String weChatAccount)
            throws WeixinException {
        try {
            if (StringUtils.isBlank(next_openid)) {
                next_openid = "";
            }
            String url = MessageFormat.format(SUBSCRIBE_USER_LIST, WeixinConfig.getAccessToken(weChatAccount),
                    next_openid);
            HttpPack pack = HttpPack.create(url);
            String resultStr = (String) pack.get(resp -> {
                return HttpUtils.read(resp);
            });
            return WeixinJsonUtil.toObject(resultStr, SubscribeUserList.class);
        } catch (Exception e) {
            throw new WeixinException(e);
        }
    }

    /**
     * 查询关注者列表
     * 
     * @return
     * @throws WeixinException
     */
    public static SubscribeUserList getSubscribeUserList(String weChatAccount) throws WeixinException {
        return getSubscribeUserList(null, weChatAccount);
    }
}
