package com.dodo.common.annotation.field;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 该注解表示分组视图配置，用于添加、修改、详情页面<br/>
 * 
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)  
@Documented  
@Inherited 
public @interface DodoViewGroup {
	/**
	 * 分组视图的排列顺序，越小的越靠前
	 * */
	public int groupSeq();
	
	/**
	 * 分组视图的显示名称
	 * */
	public String groupName() default "";
	
	/**
	 * 分组视图的显示名称， 标识Spring国际化资源文件中的一个key <strong>优先级高于 groupName </strong>
	 * */
	public String groupNameKey() default "";
}
