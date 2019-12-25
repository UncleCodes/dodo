package com.dodo.weixin.utils;

import com.dodo.utils.http.HttpPack;
import com.dodo.utils.http.HttpUtils;
import com.dodo.weixin.bean.ResultMsgBean;
import com.dodo.weixin.bean.menu.MenuButtonsBean;
import com.dodo.weixin.bean.menu.MenuQueryBean;
import com.dodo.weixin.exception.WeixinException;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * 菜单工具类 提供创建、删除、查询菜单
 * 
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class WeixinMenuUtil {
    public static final String URL_MENU_CREATE = "https://api.weixin.qq.com/cgi-bin/menu/create";
    public static final String URL_MENU_GET    = "https://api.weixin.qq.com/cgi-bin/menu/get";
    public static final String URL_MENU_DELETE = "https://api.weixin.qq.com/cgi-bin/menu/delete";

    /**
     * 创建菜单
     * 
     * @param json
     * @throws WeixinException
     */
    public static ResultMsgBean create(String json, String weChatAccount) throws WeixinException {
        try {
            String url = URL_MENU_CREATE + "?access_token=" + WeixinConfig.getAccessToken(weChatAccount);
            HttpPack pack = HttpPack.create(url).body(json);
            String resultStr = (String) pack.post(resp -> {
                return HttpUtils.read(resp);
            });
            ResultMsgBean menuOperationBean = WeixinJsonUtil.toObject(resultStr, ResultMsgBean.class);
            return menuOperationBean;
        } catch (Exception e) {
            throw new WeixinException(e);
        }
    }

    /**
     * 创建菜单
     * 
     * @param json
     * @throws WeixinException
     */
    public static ResultMsgBean create(MenuButtonsBean buttonsBean, String weChatAccount) throws WeixinException {
        try {
            return create(WeixinJsonUtil.toJackson(buttonsBean), weChatAccount);
        } catch (JsonProcessingException e) {
            throw new WeixinException(e);
        }
    }

    /**
     * 查询菜单
     * 
     * @return
     * @throws WeixinException
     */
    public static MenuQueryBean get(String weChatAccount) throws WeixinException {
        try {
            String url = URL_MENU_GET + "?access_token=" + WeixinConfig.getAccessToken(weChatAccount);
            HttpPack pack = HttpPack.create(url);
            String resultStr = (String) pack.get(resp -> {
                return HttpUtils.read(resp);
            });
            MenuQueryBean menuQueryBean = WeixinJsonUtil.toObject(resultStr, MenuQueryBean.class);
            return menuQueryBean;
        } catch (Exception e) {
            throw new WeixinException(e);
        }
    }

    /**
     * 删除菜单
     * 
     * @throws WeixinException
     */
    public static ResultMsgBean delete(String weChatAccount) throws WeixinException {
        try {
            String url = URL_MENU_DELETE + "?access_token=" + WeixinConfig.getAccessToken(weChatAccount);
            HttpPack pack = HttpPack.create(url);
            String resultStr = (String) pack.get(resp -> {
                return HttpUtils.read(resp);
            });
            ResultMsgBean menuOperationBean = WeixinJsonUtil.toObject(resultStr, ResultMsgBean.class);
            return menuOperationBean;
        } catch (Exception e) {
            throw new WeixinException(e);
        }
    }

}
