package com.dodo.weixin.servlet;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dodo.weixin.exception.WeixinException;
import com.dodo.weixin.msg.action.RequestMsgAdapter;
import com.dodo.weixin.msg.action.RequestMsgListener;
import com.dodo.weixin.msg.engine.WeixinMsgEngine;
import com.dodo.weixin.utils.WeixinConfig;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public abstract class WinxinServlet extends HttpServlet {
    private static final long serialVersionUID = 3180777472624179191L;

    public abstract String getWeChatAccount();

    public abstract List<RequestMsgListener> getRequestMsgListeners();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        Writer out = response.getWriter();
        try {
            out.write(WeixinConfig.enterVerify(timestamp, nonce, signature, echostr, getWeChatAccount()));
        } catch (WeixinException e) {
            e.printStackTrace();
            out.write("");
        }
        out.flush();
        out.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        if (!WeixinConfig.msgVerify(timestamp, nonce, signature, getWeChatAccount())) {
            return;
        }
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");
        final WeixinMsgEngine weixinMsgEngine = WeixinMsgEngine.newInstance(getWeChatAccount());
        List<RequestMsgListener> requestMsgListeners = getRequestMsgListeners();
        if (requestMsgListeners == null || requestMsgListeners.size() == 0) {
            weixinMsgEngine.addMessageListener(new RequestMsgAdapter());
        } else {
            for (RequestMsgListener listener : requestMsgListeners) {
                weixinMsgEngine.addMessageListener(listener);
            }
        }
        try {
            weixinMsgEngine.handleMessage(request, response);
        } catch (WeixinException e) {
            e.printStackTrace();
        }
    }
}
