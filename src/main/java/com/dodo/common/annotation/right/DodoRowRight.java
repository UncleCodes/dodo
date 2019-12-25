package com.dodo.common.annotation.right;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.dodo.privilege.entity.admin_1.base_1.Admin;
import com.dodo.privilege.entity.admin_1.base_1.Role;

/**
 * 该注解，表示该实体的数据行级别的访问权限<br/>
 * 使用场景<br/>
 * 1、假如某表中有字段：[addBy-添加人]，类型为{@link Admin}
 * 时，为了限制管理员只能访问自己添加的数据，则配置entityProperty=addBy,principalKey无需配置<br/>
 * 2、假如{@link Admin}
 * 和某表中分别有字段someField1,someField2,这两个字段有相同的类型<strong>[该类型必须为实体类型，
 * 也就是hibernate管理的实体]</strong>， 要限制当前管理员只能访问表中someField2的值与{@link Admin}
 * 中someField1的值相同的数据，则配置entityProperty=someField2，principalKey=someField1<br/>
 * 3、假如{@link Admin}有字段someField,类型为实体类型B，也就是hibernate管理的实体，要限制当前管理员只能访问B表中
 * {@link Admin}的someField代表的数据， 则配置entityProperty=this，principalKey=someField<br/>
 * 4、假如实体表A中有某字段BField，字段BField的类型为<strong>Byte/Short/Integer/Long/Float/Double/
 * BigDecimal /Boolean/String/枚举/其他实体类型(暂不支持)</strong>，
 * 为了限制某管理员只能访问实体表A中BField字段值为XX和YY的数据，则配置entityProperty
 * =BField，principalKey=标识符<strong>标识符由数字、字母、下划线组成</strong>， 这时候，系统会自动扩展
 * {@link Role} 表，系统启动后，在后台Role表管理页面录入限制值列表XX和YY，即可完成限制<br/>
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
@Repeatable(DodoRowRights.class)
public @interface DodoRowRight {
    /**
     * 属性取值来源
     * */
    public String principalKey() default "__principal__self__";

    /**
     * 当 entityProperty 表示的字段不为实体类型的时候，该字段与principalKeyShowNameKey二选一必填<br/>
     * 该字段代表principalKey在前台录入限制时候的显示名称
     * */
    public String principalKeyShowName() default "";

    /**
     * 当 entityProperty 表示的字段不为实体类型的时候，该字段与principalKeyShowName二选一必填<br/>
     * 该字段代表principalKey在前台录入限制时候的显示名称在Spring资源文件中的Key
     * */
    public String principalKeyShowNameKey() default "";

    /**
     * 被校验字段属性 <strong>this</strong>代表当前实体
     * */
    public String entityProperty();
}
