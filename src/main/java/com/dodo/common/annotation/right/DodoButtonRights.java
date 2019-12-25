package com.dodo.common.annotation.right;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注解，表示该实体的按钮权限<br/>
 * 配置了该注解的实体，将自动生成标准的按钮权限相关代码<br/>
 * 
 * @see DodoButtonRight
 * 
 *      <p>
 *      Dodo Framework. <a
 *      href="https://www.bydodo.com">https://www.bydodo.com</a>
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
public @interface DodoButtonRights {
    public DodoButtonRight[] value();
}
