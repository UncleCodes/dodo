package com.dodo.common.annotation.field;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 控制自动生成字段的值，如果<code>${generateClass}</code>存在，则调用<code>${generateClass}.${generateMethodName}</code>
 * 否则，自动生成一个方法，从数据库读取
 * 目前只支持字段类型为String，Long，Integer
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
public @interface DodoValueGenerator {
	public String generateMethodName() default "";
	public Class<?> generateClass() default Object.class;
}
