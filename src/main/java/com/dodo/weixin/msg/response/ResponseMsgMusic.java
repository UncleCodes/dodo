package com.dodo.weixin.msg.response;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.dodo.weixin.msg.WeixinXmlTagName;

/**
 * 音乐消息
 * 
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class ResponseMsgMusic extends ResponseMsg{
	//标题
	private String title;
	//描述
	private String description;
	//音乐链接
	private String musicUrl;
	//高质量音乐链接，WIFI环境优先使用该链接播放音乐
	private String hQMusicUrl;
	//位0x0001被标志时，星标刚收到的消息。
	private String funcFlag;
	//缩略图的媒体id，通过上传多媒体文件，得到的id
	private String thumbMediaId;
	
	public ResponseMsgMusic() {
		this.head = new ResponseMsgHead();
		this.head.setMsgType(ResponseMsg.MSG_TYPE_MUSIC);
	}
	
	public void write(Document document) {
		Element root = document.createElement(WeixinXmlTagName.ROOT);
		head.write(root, document);
		
		Element musicElement = document.createElement(WeixinXmlTagName.MUSIC);
		
		Element titleElement = document.createElement(WeixinXmlTagName.TITLE);
		titleElement.setTextContent(this.title);
		Element descriptionElement = document.createElement(WeixinXmlTagName.DESCRITION);
		descriptionElement.setTextContent(this.description);
		Element musicUrlElement = document.createElement(WeixinXmlTagName.MUSIC_URL);
		musicUrlElement.setTextContent(this.musicUrl);
		Element hQMusicUrlElement = document.createElement(WeixinXmlTagName.HQ_MUSIC_URL);
		hQMusicUrlElement.setTextContent(this.hQMusicUrl);
		Element thumbMediaIdElement = document.createElement(WeixinXmlTagName.THUMBMEDIAID);
		thumbMediaIdElement.setTextContent(this.thumbMediaId);
		
		musicElement.appendChild(titleElement);
		musicElement.appendChild(descriptionElement);
		musicElement.appendChild(musicUrlElement);
		musicElement.appendChild(hQMusicUrlElement);
		musicElement.appendChild(thumbMediaIdElement);
		root.appendChild(musicElement);
		
		Element funcFlagElement = document.createElement(WeixinXmlTagName.FUNC_FLAG);
		funcFlagElement.setTextContent(this.funcFlag);
		
		root.appendChild(funcFlagElement);
		
		document.appendChild(root);
	}
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getMusicUrl() {
		return musicUrl;
	}
	public void setMusicUrl(String musicUrl) {
		this.musicUrl = musicUrl;
	}
	public String getHQMusicUrl() {
		return hQMusicUrl;
	}
	public void setHQMusicUrl(String hQMusicUrl) {
		this.hQMusicUrl = hQMusicUrl;
	}
	public String getFuncFlag() {
		return funcFlag;
	}
	public void setFuncFlag(String funcFlag) {
		this.funcFlag = funcFlag;
	}

	public String gethQMusicUrl() {
		return hQMusicUrl;
	}

	public String getThumbMediaId() {
		return thumbMediaId;
	}

	public void sethQMusicUrl(String hQMusicUrl) {
		this.hQMusicUrl = hQMusicUrl;
	}

	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}

	@Override
	public String toString() {
		return "ResponseMsgMusic [title=" + title + ", description="
				+ description + ", musicUrl=" + musicUrl + ", hQMusicUrl="
				+ hQMusicUrl + ", funcFlag=" + funcFlag + ", thumbMediaId="
				+ thumbMediaId + ", head=" + head + "]";
	}
}
