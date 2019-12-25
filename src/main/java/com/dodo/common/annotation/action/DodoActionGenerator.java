package com.dodo.common.annotation.action;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 该注解所在的实体会生成对应的<code>action</code>层接口和实现代码
 * 
 * <p><code>baseName</code>表示生成代码的二级路径，默认使用实体类名称全小写<br/>
 * 假设有实体：{@code UserInfo}<br/>
 * （1）、不配置，则二级路径为 ：userinfo<br/>
 * （2）、配置baseName 为 user 的时候，则二级路径为 ：user<br/>
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
public @interface DodoActionGenerator {
	public String baseName() default "";
	public DodoAction[] actions() default {};
}
