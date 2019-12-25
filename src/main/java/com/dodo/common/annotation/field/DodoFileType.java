package com.dodo.common.annotation.field;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 该注解表示<strong>web页面上文件上传，文件筛选类型配置</strong> <br/>
 * 
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
@Target({})
@Retention(RetentionPolicy.RUNTIME)  
@Documented  
@Inherited 
public @interface DodoFileType {
	/**
	 * 筛选器名称
	 * **/
	public String title() default "";
	
	/**
	 * 筛选器名称 筛选器Key 标识Spring国际化资源文件中的一个key <strong>优先级高于 title </strong>
	 * **/
	public String titleKey() default "";
	/**
	 * 文件后缀 逗号隔开 如 "jpg,jpeg,png,bmp"
	 * **/	
	public String extensions();
}
