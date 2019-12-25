package com.dodo.weixin.msg.response;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.dodo.weixin.msg.WeixinXmlTagName;

/**
 * 转接到多客服
 * 
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class ResponseMsgTransferCustomerService extends ResponseMsg{
	public ResponseMsgTransferCustomerService() {
		this.head = new ResponseMsgHead();
		this.head.setMsgType(ResponseMsg.MSG_TYPE_TRANSFER_CUSTOMER_SERVICE);
	}
	
	public void write(Document document) {
		Element root = document.createElement(WeixinXmlTagName.ROOT);
		head.write(root, document);
		document.appendChild(root);
	}

	@Override
	public String toString() {
		return "ResponseMsgTransferCustomerService [head=" + head + "]";
	}
}
