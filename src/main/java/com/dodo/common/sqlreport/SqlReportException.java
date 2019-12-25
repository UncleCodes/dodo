package com.dodo.common.sqlreport;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class SqlReportException extends RuntimeException{

	private static final long serialVersionUID = 4701344034560014687L;

	public SqlReportException() {
		super();
	}

	public SqlReportException(String message, Throwable cause) {
		super(message, cause);
	}

	public SqlReportException(String message) {
		super(message);
	}

	public SqlReportException(Throwable cause) {
		super(cause);
	}
}
