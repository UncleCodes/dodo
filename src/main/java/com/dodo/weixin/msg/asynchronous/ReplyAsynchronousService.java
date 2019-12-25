package com.dodo.weixin.msg.asynchronous;

import com.dodo.weixin.exception.WeixinException;
import com.dodo.weixin.msg.response.ResponseMsg;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public interface ReplyAsynchronousService {
	void replay(ResponseMsg msg) throws WeixinException;
}
