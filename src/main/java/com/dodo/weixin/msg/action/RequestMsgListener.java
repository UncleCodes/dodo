package com.dodo.weixin.msg.action;

import javax.servlet.http.HttpServletRequest;

import com.dodo.weixin.msg.request.RequestMsgError;
import com.dodo.weixin.msg.request.RequestMsgEvent;
import com.dodo.weixin.msg.request.RequestMsgImage;
import com.dodo.weixin.msg.request.RequestMsgLink;
import com.dodo.weixin.msg.request.RequestMsgLocation;
import com.dodo.weixin.msg.request.RequestMsgText;
import com.dodo.weixin.msg.request.RequestMsgVideo;
import com.dodo.weixin.msg.request.RequestMsgVoice;


/**
 * 接收微信服务器消息的接口
 * 
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public interface RequestMsgListener {

	/**
	 * 收到文本消息
	 */
	public abstract void onTextMsg(RequestMsgText msg,HttpServletRequest  request);
	
	/**
	 * 收到图片消息
	 */
	public abstract void onImageMsg(RequestMsgImage msg,HttpServletRequest  request);
	
	/**
	 * 收到事件推送消息
	 */
	public abstract void onEventMsg(RequestMsgEvent msg,HttpServletRequest  request);
	
	/**
	 * 收到链接消息
	 */
	public abstract void onLinkMsg(RequestMsgLink msg,HttpServletRequest  request);
	
	/**
	 * 收到地理位置消息
	 */
	public abstract void onLocationMsg(RequestMsgLocation msg,HttpServletRequest  request);
	
	/**
	 * 语音识别消息
	 */
	public abstract void onVoiceMsg(RequestMsgVoice msg,HttpServletRequest  request);
	
	/**
	 * 错误消息
	 */
	public abstract void onErrorMsg(RequestMsgError msg,HttpServletRequest  request);

	/**
	 * 视频消息
	 */
	public abstract void onVideoMsg(RequestMsgVideo msg,HttpServletRequest  request);
}
