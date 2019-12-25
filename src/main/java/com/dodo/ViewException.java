package com.dodo;

import freemarker.core.Environment;
import freemarker.template.TemplateException;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class ViewException extends TemplateException{

	private static final long serialVersionUID = 1358885922950712485L;

	public ViewException(Environment env) {
		super(env);
		// TODO Auto-generated constructor stub
	}

	public ViewException(Exception cause, Environment env) {
		super(cause, env);
		// TODO Auto-generated constructor stub
	}

	public ViewException(String description, Environment env) {
		super(description, env);
		// TODO Auto-generated constructor stub
	}

	public ViewException(String arg0, Exception arg1, Environment arg2) {
		super(arg0, arg1, arg2);
		// TODO Auto-generated constructor stub
	}

}
