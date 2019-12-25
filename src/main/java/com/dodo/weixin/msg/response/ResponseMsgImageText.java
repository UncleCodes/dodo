package com.dodo.weixin.msg.response;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.dodo.weixin.msg.WeixinXmlTagName;

/**
 * 图文消息
 * 
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class ResponseMsgImageText extends ResponseMsg {

	// 图文消息个数，限制为10条以内
	private String articleCount;
	// 图文消息的数据
	private List<ResponseMsgItem> items = new ArrayList<ResponseMsgItem>(3);
	// 位0x0001被标志时，星标刚收到的消息。
	private String funcFlag;
	
	public ResponseMsgImageText() {
		this.head = new ResponseMsgHead();
		this.head.setMsgType(ResponseMsg.MSG_TYPE_IMAGE_TEXT);
	}
	
	public void write(Document document) {
		Element root = document.createElement(WeixinXmlTagName.ROOT);
		head.write(root, document);
		Element articleCountElement = document.createElement(WeixinXmlTagName.ARTICLE_COUNT);
		articleCountElement.setTextContent(this.articleCount);
		
		Element articlesElement = document.createElement(WeixinXmlTagName.ARTICLES);
		int size = Integer.parseInt(this.articleCount);
		for(int i = 0; i<size; i++){
			ResponseMsgItem currentItem = items.get(i);//获取当前
			Element itemElement = document.createElement(WeixinXmlTagName.ITEM);
			Element titleElement = document.createElement(WeixinXmlTagName.TITLE);
			titleElement.setTextContent(currentItem.getTitle());
			Element descriptionElement = document.createElement(WeixinXmlTagName.DESCRITION);
			descriptionElement.setTextContent(currentItem.getDescription());
			Element picUrlElement = document.createElement(WeixinXmlTagName.PIC_URL);
			picUrlElement.setTextContent(currentItem.getPicUrl());
			Element urlElement = document.createElement(WeixinXmlTagName.URL);
			urlElement.setTextContent(currentItem.getUrl());
			itemElement.appendChild(titleElement);
			itemElement.appendChild(descriptionElement);
			itemElement.appendChild(picUrlElement);
			itemElement.appendChild(urlElement);
			
			articlesElement.appendChild(itemElement);
		}
		
		Element funcFlagElement = document.createElement(WeixinXmlTagName.FUNC_FLAG);
		funcFlagElement.setTextContent(this.funcFlag);
		
		root.appendChild(articleCountElement);
		root.appendChild(articlesElement);
		
		document.appendChild(root);
	}

	public String getFuncFlag() {
		return funcFlag;
	}

	public void setFuncFlag(String funcFlag) {
		this.funcFlag = funcFlag;
	}
	
	public void addItem(ResponseMsgItem item){
		this.items.add(item);
		this.articleCount = ""+this.items.size();
	}
	
	public void removeItem(int index){
		this.items.remove(index);
		this.articleCount = ""+this.items.size();
	}

	public List<ResponseMsgItem> getItems() {
		return items;
	}

	@Override
	public String toString() {
		return "ResponseMsgImageText [articleCount=" + articleCount
				+ ", items=" + items + ", funcFlag=" + funcFlag + ", head="
				+ head + "]";
	}
}
