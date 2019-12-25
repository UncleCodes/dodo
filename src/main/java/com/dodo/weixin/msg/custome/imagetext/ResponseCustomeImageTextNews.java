package com.dodo.weixin.msg.custome.imagetext;

import java.util.List;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class ResponseCustomeImageTextNews{
	private List<ResponseCustomeImageTextArticle> articles;

	public List<ResponseCustomeImageTextArticle> getArticles() {
		return articles;
	}

	public void setArticles(List<ResponseCustomeImageTextArticle> articles) {
		this.articles = articles;
	}

	@Override
	public String toString() {
		return "ResponseCustomeImageTextNews [articles=" + articles + "]";
	}
}
