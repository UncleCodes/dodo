package com.dodo.weixin.msg;

import java.util.ArrayList;
import java.util.List;

import com.dodo.weixin.msg.custome.ResponseCustome;
import com.dodo.weixin.msg.custome.image.ResponseCustomeImage;
import com.dodo.weixin.msg.custome.image.ResponseCustomeImageContent;
import com.dodo.weixin.msg.custome.imagetext.ResponseCustomeImageText;
import com.dodo.weixin.msg.custome.imagetext.ResponseCustomeImageTextArticle;
import com.dodo.weixin.msg.custome.imagetext.ResponseCustomeImageTextNews;
import com.dodo.weixin.msg.custome.music.ResponseCustomeMusic;
import com.dodo.weixin.msg.custome.music.ResponseCustomeMusicContent;
import com.dodo.weixin.msg.custome.text.ResponseCustomeText;
import com.dodo.weixin.msg.custome.text.ResponseCustomeTextContent;
import com.dodo.weixin.msg.custome.video.ResponseCustomeVideo;
import com.dodo.weixin.msg.custome.video.ResponseCustomeVideoContent;
import com.dodo.weixin.msg.custome.voice.ResponseCustomeVoice;
import com.dodo.weixin.msg.custome.voice.ResponseCustomeVoiceContent;
import com.dodo.weixin.msg.response.ResponseMsg;
import com.dodo.weixin.msg.response.ResponseMsgImage;
import com.dodo.weixin.msg.response.ResponseMsgImageText;
import com.dodo.weixin.msg.response.ResponseMsgItem;
import com.dodo.weixin.msg.response.ResponseMsgMusic;
import com.dodo.weixin.msg.response.ResponseMsgText;
import com.dodo.weixin.msg.response.ResponseMsgVideo;
import com.dodo.weixin.msg.response.ResponseMsgVoice;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class ResponseMsgConvertFactory {
	public static ResponseCustome convertToCustome(ResponseMsg msg){
		if(ResponseMsg.MSG_TYPE_IMAGE.equals(msg.getHead().getMsgType())){
			ResponseMsgImage responseMsg = (ResponseMsgImage)msg;
			
			ResponseCustomeImage responseCustome = new ResponseCustomeImage();
			responseCustome.setTouser(responseMsg.getToUserName());
			responseCustome.setMsgtype(responseMsg.getMsgType());
			
			ResponseCustomeImageContent content = new ResponseCustomeImageContent();
			content.setMedia_id(responseMsg.getMediaId());
			
			responseCustome.setImage(content);
			
			return responseCustome;
		}else if(ResponseMsg.MSG_TYPE_IMAGE_TEXT.equals(msg.getHead().getMsgType())){
			ResponseMsgImageText responseMsg = (ResponseMsgImageText)msg;
			
			ResponseCustomeImageText responseCustome = new ResponseCustomeImageText();
			responseCustome.setTouser(responseMsg.getToUserName());
			responseCustome.setMsgtype(responseMsg.getMsgType());
			
			List<ResponseCustomeImageTextArticle> articles = new ArrayList<ResponseCustomeImageTextArticle>(responseMsg.getItems().size());
			
			for(ResponseMsgItem item:responseMsg.getItems()){
				ResponseCustomeImageTextArticle article = new ResponseCustomeImageTextArticle();
				article.setDescription(item.getDescription());
				article.setPicurl(item.getPicUrl());
				article.setTitle(item.getTitle());
				article.setUrl(item.getUrl());
				articles.add(article);
			}
			
			ResponseCustomeImageTextNews news = new ResponseCustomeImageTextNews();
			news.setArticles(articles);
			
			responseCustome.setNews(news);
			
			return responseCustome;
		}else if(ResponseMsg.MSG_TYPE_MUSIC.equals(msg.getHead().getMsgType())){
			ResponseMsgMusic responseMsg = (ResponseMsgMusic)msg;
			
			ResponseCustomeMusic responseCustome = new ResponseCustomeMusic();
			responseCustome.setTouser(responseMsg.getToUserName());
			responseCustome.setMsgtype(responseMsg.getMsgType());
			
			ResponseCustomeMusicContent content = new ResponseCustomeMusicContent();
			content.setDescription(responseMsg.getDescription());
			content.setHqmusicurl(responseMsg.gethQMusicUrl());
			content.setTitle(responseMsg.getTitle());
			content.setMusicurl(responseMsg.getMusicUrl());
			content.setThumb_media_id(responseMsg.getThumbMediaId());
			
			responseCustome.setMusic(content);
			
			return responseCustome;
		}else if(ResponseMsg.MSG_TYPE_TEXT.equals(msg.getHead().getMsgType())){
			ResponseMsgText responseMsg = (ResponseMsgText)msg;
			
			ResponseCustomeText responseCustome = new ResponseCustomeText();
			responseCustome.setTouser(responseMsg.getToUserName());
			responseCustome.setMsgtype(responseMsg.getMsgType());
			
			ResponseCustomeTextContent content = new ResponseCustomeTextContent();
			content.setContent(responseMsg.getContent());
			
			responseCustome.setText(content);
			
			return responseCustome;
		}else if(ResponseMsg.MSG_TYPE_VIDEO.equals(msg.getHead().getMsgType())){
			ResponseMsgVideo responseMsg = (ResponseMsgVideo)msg;
			
			ResponseCustomeVideo responseCustome = new ResponseCustomeVideo();
			responseCustome.setTouser(responseMsg.getToUserName());
			responseCustome.setMsgtype(responseMsg.getMsgType());
			
			ResponseCustomeVideoContent content = new ResponseCustomeVideoContent();
			content.setDescription(responseMsg.getDescription());
			content.setMedia_id(responseMsg.getMediaId());
			content.setTitle(responseMsg.getTitle());
			
			responseCustome.setVideo(content);
			
			return responseCustome;
		}else if(ResponseMsg.MSG_TYPE_VOICE.equals(msg.getHead().getMsgType())){
			ResponseMsgVoice responseMsg = (ResponseMsgVoice)msg;
			
			ResponseCustomeVoice responseCustome = new ResponseCustomeVoice();
			responseCustome.setTouser(responseMsg.getToUserName());
			responseCustome.setMsgtype(responseMsg.getMsgType());
			
			ResponseCustomeVoiceContent content = new ResponseCustomeVoiceContent();
			content.setMedia_id(responseMsg.getMediaId());
			
			responseCustome.setVoice(content);
			
			return responseCustome;
		}
		return null;
	}
}
