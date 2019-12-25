package com.dodo.weixin.msg.action;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dodo.weixin.msg.request.RequestMsgError;
import com.dodo.weixin.msg.request.RequestMsgEvent;
import com.dodo.weixin.msg.request.RequestMsgImage;
import com.dodo.weixin.msg.request.RequestMsgLink;
import com.dodo.weixin.msg.request.RequestMsgLocation;
import com.dodo.weixin.msg.request.RequestMsgText;
import com.dodo.weixin.msg.request.RequestMsgVideo;
import com.dodo.weixin.msg.request.RequestMsgVoice;

/**
 * 处理消息适配器(适配器模式)
 * 
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class RequestMsgAdapter implements RequestMsgListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestMsgAdapter.class);

    //接收到文本消息
    public void onTextMsg(RequestMsgText msg, HttpServletRequest request) {
        LOGGER.debug("Received：", msg.toString());
    }

    //接收到图片消息
    public void onImageMsg(RequestMsgImage msg, HttpServletRequest request) {
        LOGGER.debug("Received：", msg.toString());
    }

    //收到事件推送消息
    public void onEventMsg(RequestMsgEvent msg, HttpServletRequest request) {
        LOGGER.debug("Received：", msg.toString());
    }

    //接收到链接消息
    public void onLinkMsg(RequestMsgLink msg, HttpServletRequest request) {
        LOGGER.debug("Received：", msg.toString());
    }

    //接收到LBS消息
    public void onLocationMsg(RequestMsgLocation msg, HttpServletRequest request) {
        LOGGER.debug("Received：", msg.toString());
    }

    //语音识别消息
    public void onVoiceMsg(RequestMsgVoice msg, HttpServletRequest request) {
        LOGGER.debug("Received：", msg.toString());
    }

    //接收到视频消息
    public void onVideoMsg(RequestMsgVideo msg, HttpServletRequest request) {
        LOGGER.debug("Received：", msg.toString());
    }

    //接收到错误消息
    public void onErrorMsg(RequestMsgError msg, HttpServletRequest request) {
        LOGGER.debug("Received：", msg.toString());
    }
}
