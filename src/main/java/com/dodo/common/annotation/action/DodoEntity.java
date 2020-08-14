package com.dodo.common.annotation.action;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.dodo.common.annotation.menu.DodoMenu;

/**
 * 该注解所在的实体会纳入<code>Dodo Framework</code>管理范围
 * 
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
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
public @interface DodoEntity {
    /**
     * 实体名称
     **/
    public String name() default "";

    /**
     * 实体名称 标识Spring国际化资源文件中的一个key <strong>优先级高于 name </strong>
     **/
    public String nameKey() default "";

    public String remark() default "";

    public DodoAction[] actions() default {};

    public DodoMenu levelOne();

    public DodoMenu levelTwo();

    public DodoMenu levelThree();
}
