package com.dodo.weixin.utils;

import java.util.HashMap;
import java.util.Map;

import com.dodo.utils.JacksonUtil;
import com.dodo.utils.http.HttpPack;
import com.dodo.utils.http.HttpUtils;
import com.dodo.weixin.bean.ResultMsgBean;
import com.dodo.weixin.bean.group.GroupBean;
import com.dodo.weixin.bean.group.GroupCreateResultBean;
import com.dodo.weixin.bean.group.GroupQueryBean;
import com.dodo.weixin.bean.group.UserGroupBean;
import com.dodo.weixin.exception.WeixinException;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class WeixinUserGroupUtil {
    public static final String GROUPS_CREATE     = "https://api.weixin.qq.com/cgi-bin/groups/create";
    public static final String GROUPS_UPDATE     = "https://api.weixin.qq.com/cgi-bin/groups/update";
    public static final String GROUPS_GET        = "https://api.weixin.qq.com/cgi-bin/groups/get";
    public static final String USER_GROUP_GET    = "https://api.weixin.qq.com/cgi-bin/groups/getid";
    public static final String USER_GROUP_MODIFY = "https://api.weixin.qq.com/cgi-bin/groups/members/update";

    /**
     * 创建分组
     * 
     * @param json
     * @throws WeixinException
     */
    private static GroupCreateResultBean _create(String json, String weChatAccount) throws WeixinException {
        try {
            String url = GROUPS_CREATE + "?access_token=" + WeixinConfig.getAccessToken(weChatAccount);
            HttpPack pack = HttpPack.create(url).body(json);
            String resultStr = (String) pack.post(resp -> {
                return HttpUtils.read(resp);
            });
            return WeixinJsonUtil.toObject(resultStr, GroupCreateResultBean.class);
        } catch (Exception e) {
            throw new WeixinException(e);
        }
    }

    /**
     * 修改分组
     * 
     * @param json
     * @throws WeixinException
     */
    private static ResultMsgBean _update(String json, String weChatAccount) throws WeixinException {
        try {
            String url = GROUPS_UPDATE + "?access_token=" + WeixinConfig.getAccessToken(weChatAccount);
            HttpPack pack = HttpPack.create(url).body(json);
            String resultStr = (String) pack.post(resp -> {
                return HttpUtils.read(resp);
            });
            return WeixinJsonUtil.toObject(resultStr, ResultMsgBean.class);
        } catch (Exception e) {
            throw new WeixinException(e);
        }
    }

    /**
     * 创建分组
     * 
     * @param groupBean
     * @throws WeixinException
     */
    public static GroupCreateResultBean create(GroupBean groupBean, String weChatAccount) throws WeixinException {
        Map<String, Object> gMap = new HashMap<String, Object>(1);
        gMap.put("group", groupBean);
        try {
            return _create(WeixinJsonUtil.toJackson(gMap), weChatAccount);
        } catch (JsonProcessingException e) {
            throw new WeixinException(e);
        }
    }

    /**
     * 修改分组
     * 
     * @param groupBean
     * @throws WeixinException
     */
    public static ResultMsgBean update(GroupBean groupBean, String weChatAccount) throws WeixinException {
        Map<String, Object> gMap = new HashMap<String, Object>(1);
        gMap.put("group", groupBean);
        try {
            return _update(WeixinJsonUtil.toJackson(gMap), weChatAccount);
        } catch (JsonProcessingException e) {
            throw new WeixinException(e);
        }
    }

    /**
     * 修改分组
     * 
     * @param groupId
     * @param groupName
     * @throws WeixinException
     */
    public static ResultMsgBean update(Integer groupId, String groupName, String weChatAccount) throws WeixinException {
        GroupBean groupBean = new GroupBean();
        groupBean.setId(groupId);
        groupBean.setName(groupName);
        return update(groupBean, weChatAccount);
    }

    /**
     * 修改分组
     * 
     * @param groupName
     * @throws WeixinException
     */
    public static GroupCreateResultBean create(String groupName, String weChatAccount) throws WeixinException {
        GroupBean groupBean = new GroupBean();
        groupBean.setName(groupName);
        return create(groupBean, weChatAccount);
    }

    /**
     * 查询所有分组
     * 
     * @return
     * @throws WeixinException
     */
    public static GroupQueryBean get(String weChatAccount) throws WeixinException {
        try {
            String url = GROUPS_GET + "?access_token=" + WeixinConfig.getAccessToken(weChatAccount);
            HttpPack pack = HttpPack.create(url);
            String resultStr = (String) pack.get(resp -> {
                return HttpUtils.read(resp);
            });
            return WeixinJsonUtil.toObject(resultStr, GroupQueryBean.class);
        } catch (Exception e) {
            throw new WeixinException(e);
        }
    }

    /**
     * 查询用户所在分组
     * 
     * @return
     * @throws WeixinException
     */
    public static UserGroupBean getUserGroup(String userOpenId, String weChatAccount) throws WeixinException {
        try {
            Map<String, String> postMap = new HashMap<String, String>(1);
            postMap.put("openid", userOpenId);
            String url = USER_GROUP_GET + "?access_token=" + WeixinConfig.getAccessToken(weChatAccount);
            HttpPack pack = HttpPack.create(url).body(JacksonUtil.toJackson(postMap));
            String resultStr = (String) pack.post(resp -> {
                return HttpUtils.read(resp);
            });
            return WeixinJsonUtil.toObject(resultStr, UserGroupBean.class);
        } catch (Exception e) {
            throw new WeixinException(e);
        }
    }

    /**
     * 移动用户分组
     * 
     * @return
     * @throws WeixinException
     */
    public static ResultMsgBean moveUserToGroup(String userOpenId, Integer groupId, String weChatAccount)
            throws WeixinException {
        try {
            Map<String, Object> postMap = new HashMap<String, Object>(2);
            postMap.put("openid", userOpenId);
            postMap.put("to_groupid", groupId);
            String url = USER_GROUP_MODIFY + "?access_token=" + WeixinConfig.getAccessToken(weChatAccount);
            HttpPack pack = HttpPack.create(url).body(JacksonUtil.toJackson(postMap));
            String resultStr = (String) pack.post(resp -> {
                return HttpUtils.read(resp);
            });
            return WeixinJsonUtil.toObject(resultStr, ResultMsgBean.class);
        } catch (Exception e) {
            throw new WeixinException(e);
        }
    }
}
