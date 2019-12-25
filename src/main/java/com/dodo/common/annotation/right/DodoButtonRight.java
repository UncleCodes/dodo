package com.dodo.common.annotation.right;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注解，表示一个单独的按钮权限<br/>
 * 
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
@Repeatable(DodoButtonRights.class)
public @interface DodoButtonRight {
    /**
     * 按钮的显示文本名称</br> 该配置和<strong>nameKey</strong>属性只能二选一</br>
     * 
     * @see DodoButtonRight#nameKey()
     * */
    public String name() default "";

    /**
     * 按钮的显示文本名称Key 该Key来源于Spring资源文件 messages*.properties中的Key</br>
     * 该配置和<strong>name</strong>属性只能二选一</br>
     * 
     * @see DodoButtonRight#name()
     * */
    public String nameKey() default "";

    /**
     * 访问模式 打开URL方式 或者 ajax方式
     * 
     * @see DodoButtonRightEvent
     * */
    public DodoButtonRightEvent event();

    /**
     * <strong>【1】、当event = AJAX时，配置ajax访问地址，即：资源路径<br/>
     * </strong> 实际访问路径 = 后台根路径 + 模块路径 + path()<br/>
     * 配置示例：/cancel,/make/all,/test_make<br/>
     * 此时会生成对应的ajax Controller层方法 <br/>
     * <strong>【2】、当event = URL时，配置需要打开的页面地址<br/>
     * </strong> 1、绝对路径，如https://www.bydodo.com<br/>
     * 2.1、相对于后台路径，如${rootPath}/xxx/xxx.jhtml<br/>
     * 2.1、相对于前台路径，如${webHomeUrl}/xxx/xxx.dhtml 或者 ${webHomeUrl}/xxx/xxx.htm<br/>
     * <strong>注：<br/>
     * ${rootPath} 代表配置文件中配置的后台管理总路径:dodo.backmanage.view.rootPath<br/>
     * ${webHomeUrl} 代表配置文件中配置的网站前台Url:dodo.common.config.web.homeurl<br/>
     * </strong><br/>
     * 跳转时，会携带参数：<br/>
     * （1）如果是数据行按钮，则携带本行数据的ID，如：entityId=1160825085175668736<br/>
     * （2）如果是模块按，则携带已选的数据的ID，如：entityIds=1207864625027227648,1160825085175668736
     * */
    public String path();

    /**
     * 按钮模式：ROW=数据行按钮，MODEL=模块按钮
     * 
     * @see DodoButtonRightModel
     * */
    public DodoButtonRightModel model();

    /**
     * 按钮显示的位置，model=DodoButtonRightModel.MODEL时启用，默认是在下方；取值：TOP=顶部，BOTTOM=底部
     * 
     * @see DodoButtonLocation
     * */
    public DodoButtonLocation location() default DodoButtonLocation.BOTTOM;

    /**
     * 排列顺序，表示按钮出现的先后顺序，升序
     * */
    public int sortSeq() default 0;

    public String urlTarget() default "_self";
}
