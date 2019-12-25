package com.dodo.common.database.hql;
/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class HqlHelperException extends RuntimeException{
	private static final long serialVersionUID = 3400575541455771461L;

	public HqlHelperException() {
		super();
	}

	public HqlHelperException(String message, Throwable cause) {
		super(message, cause);
	}

	public HqlHelperException(String message) {
		super(message);
	}

	public HqlHelperException(Throwable cause) {
		super(cause);
	}
	
}
