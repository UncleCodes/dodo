package com.dodo.weixin.msg.engine;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.dodo.weixin.exception.WeixinException;
import com.dodo.weixin.msg.action.RequestMsgListener;
import com.dodo.weixin.msg.asynchronous.ReplyAsynchronousService;
import com.dodo.weixin.msg.request.RequestMsg;
import com.dodo.weixin.msg.request.RequestMsgHead;
import com.dodo.weixin.msg.response.ResponseMsg;
import com.dodo.weixin.utils.WeixinCustomeMsgUtil;
import com.dodo.weixin.utils.WeixinSecurityUtil;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class WeixinMsgEngine {
    private static final Logger                                   LOGGER                = LoggerFactory
                                                                                                .getLogger(WeixinMsgEngine.class);
    private static ThreadLocal<WeixinMsgEngine>                   currEngineThreadLocal = new ThreadLocal<WeixinMsgEngine>();
    private static final Map<String, Class<? extends RequestMsg>> requestMsgs           = new HashMap<String, Class<? extends RequestMsg>>();
    private static final Map<String, Method>                      requestMsgMethods     = new HashMap<String, Method>();

    private List<RequestMsgListener>                              listeners             = new ArrayList<RequestMsgListener>(
                                                                                                3);
    private InputStream                                           msgInputStream;
    private OutputStream                                          msgOutputStream;

    private static DocumentBuilder                                builder;
    private static TransformerFactory                             tffactory;
    private ReplyAsynchronousService                              replyAsynchronousService;
    private Boolean                                               isSendImmediately     = Boolean.FALSE;
    private String                                                encrypt_type;
    private String                                                msg_signature;
    private String                                                timestamp;
    private String                                                nonce;
    private final String                                          Encoding_RAW          = "raw";
    private final String                                          Encoding_AES          = "aes";
    private final String                                          encrypt_format        = "<xml><ToUserName><![CDATA[%1$s]]></ToUserName><Encrypt><![CDATA[%2$s]]></Encrypt></xml>";
    private String                                                weChatAccount         = null;
    static {
        initMsgInvoker();
    }

    // 默认实例
    public static WeixinMsgEngine newInstance(String weChatAccount) {
        return newInstance(weChatAccount, null);
    }

    // 传入一个异步消息回复服务
    public static WeixinMsgEngine newInstance(String weChatAccount, ReplyAsynchronousService replyAsynchronousService) {
        currEngineThreadLocal.set(new WeixinMsgEngine(weChatAccount, replyAsynchronousService));
        return currEngineThreadLocal.get();
    }

    public static WeixinMsgEngine getCurrentEngine() {
        return currEngineThreadLocal.get();
    }

    // 私有构造方法
    private WeixinMsgEngine(String weChatAccount, ReplyAsynchronousService replyAsynchronousService) {
        this.replyAsynchronousService = replyAsynchronousService;
        this.weChatAccount = weChatAccount;
        initDomParser();
    }

    //初始化dom解析
    private void initDomParser() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        tffactory = TransformerFactory.newInstance();
    }

    // 添加消息监听器
    public WeixinMsgEngine addMessageListener(RequestMsgListener handleMassge) {
        listeners.add(handleMassge);
        return this;
    }

    // 初始化消息解析器 事件处理器
    @SuppressWarnings("unchecked")
    public static void initMsgInvoker() {
        try {
            Method[] listenerMethods = RequestMsgListener.class.getDeclaredMethods();
            for (Method method : listenerMethods) {
                Class<? extends RequestMsg> clazz = (Class<? extends RequestMsg>) method.getParameterTypes()[0];
                String msgType = (String) clazz.getMethod("getMsgType").invoke(clazz);
                requestMsgs.put(msgType, clazz);
                requestMsgMethods.put(msgType, method);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 处理消息
    public WeixinMsgEngine handleMessage(HttpServletRequest request, HttpServletResponse response)
            throws WeixinException {
        try {
            encrypt_type = StringUtils.defaultIfBlank(request.getParameter("encrypt_type"), Encoding_RAW);
            msg_signature = request.getParameter("msg_signature");
            timestamp = request.getParameter("timestamp");
            nonce = request.getParameter("nonce");
            LOGGER.info("encrypt_type={}", encrypt_type);
            handleMessage(request.getInputStream(), response.getOutputStream(), request);
        } catch (IOException e) {
            throw new WeixinException(e);
        }
        return this;
    }

    // 处理消息
    private WeixinMsgEngine handleMessage(InputStream msgInputStream, OutputStream msgOutputStream,
            HttpServletRequest request) throws WeixinException {
        try {
            this.msgInputStream = msgInputStream;
            this.msgOutputStream = msgOutputStream;
            Document document = builder.parse(msgInputStream);

            Constructor<RequestMsgHead> headConstructor = RequestMsgHead.class.getDeclaredConstructor();
            headConstructor.setAccessible(Boolean.TRUE);
            RequestMsgHead head = headConstructor.newInstance().read(document);
            // 适配安全模式 & 兼容模式
            if (Encoding_AES.equals(encrypt_type)) {
                String msg = WeixinSecurityUtil.getDecryptMsg(
                        String.format(encrypt_format, head.getToUserName(), head.getEncrypt()), msg_signature,
                        timestamp, nonce, weChatAccount);
                LOGGER.info("WeixinSecurityUtil.getDecryptMsg Got={}", msg);
                document = builder.parse(new InputSource(new StringReader(msg)));
                head = head.read(document);
            }
            String methodType = head.getMsgType();
            Class<? extends RequestMsg> clazz = requestMsgs.get(methodType);
            if (clazz == null) {
                methodType = RequestMsg.MSG_TYPE_ERROR;
                clazz = requestMsgs.get(methodType);
            }
            Constructor<? extends RequestMsg> msgConstructor = clazz.getDeclaredConstructor(RequestMsgHead.class);
            msgConstructor.setAccessible(Boolean.TRUE);
            RequestMsg requestMsg = msgConstructor.newInstance(head);
            requestMsg.read(document);

            for (RequestMsgListener listener : listeners) {
                requestMsgMethods.get(methodType).invoke(listener, requestMsg, request);
            }
        } catch (Exception e) {
            throw new WeixinException(e);
        } finally {
            replyNullStrToDisconnect();
            currEngineThreadLocal.remove();
        }
        return this;
    }

    // 立即回复
    public WeixinMsgEngine replyImmediately(ResponseMsg msg) throws WeixinException {
        try {
            if (isSendImmediately) {
                throw new WeixinException("Can't deal expired message!");
            }
            isSendImmediately = Boolean.TRUE;
            Document document = builder.newDocument();
            msg.write(document);
            Transformer transformer = tffactory.newTransformer();
            if (Encoding_RAW.equals(encrypt_type)) {
                transformer.transform(new DOMSource(document), new StreamResult(new OutputStreamWriter(msgOutputStream,
                        "utf-8")));
            } else {
                StringWriter stringWriter = new StringWriter();
                transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
                msgOutputStream.write(WeixinSecurityUtil.getEncryptMsg(stringWriter.toString(), timestamp, nonce,
                        weChatAccount).getBytes());
            }
        } catch (Exception e) {
            throw new WeixinException(e);
        } finally {
            close();
        }
        return this;
    }

    // 立即回复空字符串
    public WeixinMsgEngine replyNullStrToDisconnect() throws WeixinException {
        if (isSendImmediately) {
            return this;
        }
        try {
            msgOutputStream.write("".getBytes());
            isSendImmediately = Boolean.TRUE;
        } catch (Exception e) {
            throw new WeixinException(e);
        } finally {
            close();
        }
        return this;
    }

    // 异步发送消息 如果没有传入异步发送服务 则使用客服接口发送
    public synchronized WeixinMsgEngine replyAsynchronous(ResponseMsg msg) throws WeixinException {
        if (replyAsynchronousService != null) {
            replyAsynchronousService.replay(msg);
        } else {
            WeixinCustomeMsgUtil.sendResponseMsgAsCustomeMsg(msg, weChatAccount);
        }
        return this;
    }

    private void close() {
        try {
            if (msgInputStream != null) {
                msgInputStream.close();
                msgInputStream = null;
            }
            if (msgOutputStream != null) {
                msgOutputStream.flush();
                msgOutputStream.close();
                msgOutputStream = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
