package com.dodo.common.annotation.field;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 该注解表示<strong>文件/地理信息</strong> 扩展属性配置 <br/>
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
public @interface DodoExtAttr {
	/**
	 * <strong>文件/地理信息</strong> 扩展属性 Key<br/>
	 * 英文字母开头，数字、字母、下划线自由组合<br/>
	 * <strong>必须唯一</strong>
	 * **/
	public String attrKey();
	/**
	 * <strong>文件/地理信息</strong> 扩展属性 名称<br/>
	 * 页面显示使用
	 * **/	
	public String attrLabel() default "";
	
	/**
	 * <strong>文件/地理信息</strong> 扩展属性 名称Key<br/>
	 * 页面显示使用 代表Spring 国际化资源文件中的一个key<br/>
	 * 优先级高于 attrLabel
	 * **/	
	public String attrLabelKey() default "";
}
