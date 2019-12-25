package com.dodo.common.annotation.right;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注解所表示该实体类的权限配置
 * 
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)  
@Documented  
@Inherited 
public @interface DodoRight {
	/**
	 * 实体名称
	 **/
	public String name() default "";
	/**
	 * 实体名称 标识Spring国际化资源文件中的一个key <strong>优先级高于 name </strong>
	 **/
	public String nameKey() default "";
	
	public String remark() default "";
	/**
	 * 实体名称基本路径 一般不建议填写
	 **/
	public String path() default "";
}
