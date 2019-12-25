package com.dodo.common.annotation.dao;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注解所在的实体会生成对应的<code>dao</code>层接口和实现代码，<code>baseName</code>表示生成代码的基本名称
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
public @interface DodoDaoGenerator {
	public String baseName() default "";
}
