package com.dodo.weixin.exception;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class WeixinException extends Exception {
	private static final long serialVersionUID = 7305868944205612287L;
	public WeixinException(String msg) {
		super(msg); 
	}
	public WeixinException(String message, Throwable cause) {
		super(message, cause);
	}
	public WeixinException(Throwable cause) {
		super(cause);
	}
}
