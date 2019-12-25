package com.dodo.weixin.msg.response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.dodo.weixin.msg.WeixinXmlTagName;

/**
 * 响应 微信消息头
 * 
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class ResponseMsgHead {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    // 接收方帐号（收到的OpenID） 
    private String                         toUserName;
    // 开发者微信号
    private String                         fromUserName;
    // 消息创建时间 （整型）
    private String                         createTime;
    // 消息类型：text\image\
    private String                         msgType;

    public ResponseMsgHead() {
        this.createTime = formatter.format(LocalDateTime.now());
    }

    public void write(Element root, Document document) {
        Element toUserNameElement = document.createElement(WeixinXmlTagName.TO_USER_NAME);
        toUserNameElement.setTextContent(this.toUserName);
        Element fromUserNameElement = document.createElement(WeixinXmlTagName.FROM_USER_NAME);
        fromUserNameElement.setTextContent(this.fromUserName);
        Element createTimeElement = document.createElement(WeixinXmlTagName.CREATE_TIME);
        createTimeElement.setTextContent(this.createTime);
        Element msgTypeElement = document.createElement(WeixinXmlTagName.MSG_TYPE);
        msgTypeElement.setTextContent(this.msgType);

        root.appendChild(toUserNameElement);
        root.appendChild(fromUserNameElement);
        root.appendChild(createTimeElement);
        root.appendChild(msgTypeElement);
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    @Override
    public String toString() {
        return "ResponseMsgHead [toUserName=" + toUserName + ", fromUserName=" + fromUserName + ", createTime="
                + createTime + ", msgType=" + msgType + "]";
    }
}
