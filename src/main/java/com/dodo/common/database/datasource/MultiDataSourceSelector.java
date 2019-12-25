package com.dodo.common.database.datasource;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class MultiDataSourceSelector {
	private static final ThreadLocal<String> SELECTOR = new ThreadLocal<String>();
	public static void set(String dsType){
		SELECTOR.set(dsType);
	}
	public static String get(){
		return SELECTOR.get();
	}
	public static void remove(){
		SELECTOR.remove();
	}
}
