package com.dodo.common.annotation.field;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.dodo.common.annotation.tree.DodoTreeRef;
import com.dodo.common.framework.bean.location.DodoLocationInfo;

/**
 * 该注解表示字段详细配置<br/>
 * 说明：<br/>
 * 1、当字段类型为枚举类型的时候 ，该枚举类型需要实现接口 <code>com.dodo.common.enums.EnumInterface</code>
 * 
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
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
public @interface DodoField {
    /** 字段的显示名称 */
    public String name() default "";

    /** 字段的显示名称 ,标识Spring国际化资源文件中的一个key <strong>优先级高于 name </strong> */
    public String nameKey() default "";

    /** 字段的排序顺序，即：在页面上出现的先后顺序 asc */
    public int sortSeq();

    /** 字段是否可以为空 只对非基本类型有效 */
    public boolean isnullable() default true;

    /** 字段是否可以被添加 */
    public boolean addable() default true;

    /** 字段是否可以被编辑 */
    public boolean editable() default true;

    /** 字段是否可以被在列表页面展示 ，建议 树属性等引用其他类多个对象的属性以及富文本 配置该选项为false */
    public boolean listable() default true;

    /** 字段是否是密码 ，密码属性的字段 必须为字符串 */
    public boolean ispassword() default false;

    /** 表示字段的最小长度 只对String类型的字段有效 配置大于0表示启用限制 */
    public int minLength() default -1;

    /** 表示字段的最大长度 只对String类型的字段有效 配置大于0表示启用限制 */
    public int maxLength() default -1;

    /** 字段是否是邮箱 只对String类型的字段有效 */
    public boolean isEmail() default false;

    /** 字段是否设置默认值 默认为 true */
    public boolean isSetDefault() default true;

    /** 字段是否是手机号码 只对String类型的字段有效 */
    public boolean isMobile() default false;

    /** 字段是否需要进行远程唯一性校验，如果是，则生成一系列代码，用于前台页面远程验证字段是否已经被使用，如用户名 */
    public boolean isRemoteCheck() default false;

    /** 字段是否是用户名 只对String类型的字段有效 */
    public boolean isUsername() default false;

    /** 字段是否是Url格式 只对String类型的字段有效 */
    public boolean isUrl() default false;

    /** 字段是否是数字 只对数字类型 byte short int long float double 以及对应的包装类有效 */
    public boolean isNumber() default false;

    /** 字段是否是整数 只对数字类型 byte short int long 以及对应的包装类有效 */
    public boolean isDigits() default false;

    /** 字段是否是信用卡类型 只对String类型的字段有效 */
    public boolean isCreditcard() default false;

    /** 字段是否是Ip地址 只对String类型的字段有效 */
    public boolean isIp() default false;

    /** 字段是否是文本域 String 配置true 有效 文本域 页面使用<strong>textarea</strong>处理 */
    public boolean isTextArea() default false;

    /** 字段是否是富文本 String 配置true 有效 富文本页面将生成富文本编辑器处理 */
    public boolean isRichText() default false;

    /**
     * 当字段引用了多个树状结构的实体时<br/>
     * 1、配置该属性为<code>true</code>表示忽略 <code>@DodoTreeRef </code>的
     * <code> selfQueryParams </code>属性<br/>
     * 2、不配置该属性，默认<code>false</code>，表示启用 <code>@DodoTreeRef </code>的
     * <code> selfQueryParams </code>属性<br/>
     * 
     * @see DodoTreeRef#selfQueryParams()
     * */
    public boolean isShowWholeTree() default false;

    /**
     * 表示字段的最大值 只对数字类型 byte short int long float double 以及对应的包装类和BigDecimal有效<br/>
     * 配置该属性时，您需要保证属性值可以被正确地转换为数字
     * */
    public String max() default "";

    /**
     * 表示字段的最小值 只对数字类型 byte short int long float double 以及对应的包装类和BigDecimal有效 <br/>
     * 配置该属性时，您需要保证属性值可以被正确地转换为数字
     * */
    public String min() default "";

    /** 字段的提示信息 提示信息在添加页面、修改页面用于提示 */
    public String infoTip() default "";

    /**
     * 字段的提示信息 提示信息在添加页面、修改页面用于提示 ,标识Spring国际化资源文件中的一个key <br.>
     * <strong>如果 infoTip也配置，则infoTip 和 infoTipKey代表的实际提示语 均在页面显示</strong>
     * */
    public String infoTipKey() default "";

    /**
     * 表示字段的内容是文件 <br/>
     * 添加该配置的字段长度请参见 {@link FileStyle} <br/>
     * 只对String类型的字段有效
     */
    public boolean isFile() default false;

    /**
     * 文件字段的文件保存类型，默认：FullInfo <br/>
     * <br/>
     * OnlyPath:只保存文件路径，此时字段长度 至少为128 <br/>
     * FullInfo:保存文件的详细信息，此时字段长度 至少为 1024 <br/>
     */
    public FileStyle fileStyle() default FileStyle.FullInfo;

    /**
     * 设置该字段后，文件将被上传至对应的OSS内 <br/>
     * 只在isFile=true && fileStyle=FileStyle.OnlyPath 下有效
     */
    public String ossBucket() default "";

    /**
     * 配合
     * <strong>isDoc</strong>、<strong>isFile</strong>和<strong>isVideo</strong>
     * 使用</br>
     * 当<strong>isDoc</strong>的时候，该字段可以不设置，默认值为<strong>文档文件</strong></br>
     * 当<strong>isFile</strong>的时候，该字段可以不设置，默认值为<strong>所有类型文件</strong></br>
     * 当<strong>isVideo</strong>的时候，该字段可以不设置，默认值为<strong>视频文件</strong></br>
     * 添加该配置的字段长度请参见 {@link FileStyle} <br/>
     * 只对String类型的字段有效<br/>
     * 常用组合<br/>
     * 
     * @DodoFileType(titleKey="dodo.file.upload.titlekey.images",extensions="jpg,jpeg,gif,png,bmp")<br/> 
     *                                                                                                   "{title:'视频文件',extensions:'asx,asf,mpg,wmv,3gp,mp4,mov,avi,flv,wmv9,rm,rmvb'}"
     * <br/>
     *                                                                                                   "{title:'压缩文件',extensions:'rar,zip,cab,iso,jar,ace,7z,tar,gz,arj,lzh,uue,bz2,z'}"
     * <br/>
     *                                                                                                   "{title:'文档文件',extensions:'pdf,html,odt,sxw,doc,docx,rtf,wpd,txt,wiki,ods,sxc,xls,xlsx,csv,tsv,odp,sxi,ppt,pptx,odg,svg'}"
     * <br/>
     * */
    public DodoFileType[] fileType() default {};

    /**
     * 表示字段的扩展属性 <br/>
     * 配合 <strong>isLocation</strong>、<strong>isDoc</strong>、<strong>isFile</
     * strong>和<strong>isVideo</strong>使用</br>
     * */
    public DodoExtAttr[] extAttr() default {};

    /**
     * 表示字段的内容是文档，将由转换器进行转换，需要安装Openoffice 和 SwfTools <br/>
     * 添加该配置的字段长度请参见 {@link FileStyle} <br/>
     * 只对String类型的字段有效
     */
    public boolean isDoc() default false;

    /**
     * 表示字段的内容是视频，将由转换器进行转换，需要安装ffmpeg、mencoder和flvtool2 <br/>
     * 添加该配置的字段长度请参见 {@link FileStyle} <br/>
     * 只对String类型的字段有效
     */
    public boolean isVideo() default false;

    /**
     * 表示字段的内容是当前管理员 <br/>
     * 字段类型必须为<strong>Admin</strong>类型<br/>
     * 如此设置 则该字段不可编辑、不可添加 并且生成数据的时候 默认取值当前登陆 principal
     */
    public boolean isAdmin() default false;

    /**
     * 附加查询条件，多个查询条件使用符号“;”隔开 查询条件为与关系<br/>
     * 配置该属性的字段必须为<strong>其他实体类型</strong><br/>
     * 如：eq("userName", "zhangsan");gt("score",1000);like("city","%中国%");<br/>
     * 当属性类型为<strong>其他实体类型</strong>时，并且<strong>‘其他实体类型’展示方式非树状展示</strong>时使用 <br/>
     * 所有支持列表如下<br/>
     * 1、eq(String propertyName,Object value)<b>[propertyName 值等于 value]</b><br/>
     * 2、ne(String propertyName,Object value)<b>[propertyName 值不等于 value]</b><br/>
     * 3、ge(String propertyName,Object value)<b>[propertyName 值大于等于 value]</b><br/>
     * 4、gt(String propertyName,Object value)<b>[propertyName 值大于 value]</b><br/>
     * 5、lt(String propertyName,Object value)<b>[propertyName 值小于 value]</b><br/>
     * 6、le(String propertyName,Object value)<b>[propertyName 值小于等于 value]</b><br/>
     * 7、in(String propertyName,Object... values)<b>[propertyName 值在 values
     * 中存在]</b><br/>
     * 8、notIn(String propertyName,Object... values)<b>[propertyName 值在 values
     * 中不存在]</b><br/>
     * 9、eqProperty(String propertyName,String
     * otherPropertyName)<b>[propertyName 值等于 otherPropertyName 的值]</b><br/>
     * 10、neProperty(String propertyName,String
     * otherPropertyName)<b>[propertyName 值不等于 otherPropertyName 的值]</b><br/>
     * 11、geProperty(String propertyName,String
     * otherPropertyName)<b>[propertyName 值大于等于 otherPropertyName 的值]</b><br/>
     * 12、gtProperty(String propertyName,String
     * otherPropertyName)<b>[propertyName 值大于 otherPropertyName 的值]</b><br/>
     * 13、ltProperty(String propertyName,String
     * otherPropertyName)<b>[propertyName 值小于 otherPropertyName 的值]</b><br/>
     * 14、leProperty(String propertyName,String
     * otherPropertyName)<b>[propertyName 值小于等于 otherPropertyName 的值]</b><br/>
     * 15、like(String propertyName,String value)<b>[propertyName 值模糊匹配
     * value、区分大小写]</b><br/>
     * 16、notLike(String propertyName,String value)<b>[propertyName 值模糊不匹配
     * value、区分大小写]</b><br/>
     * 17、ilike(String propertyName,String value)<b>[propertyName 值模糊匹配
     * value、不区分大小写]</b><br/>
     * 18、notiLike(String propertyName,String value)<b>[propertyName 值模糊不匹配
     * value、不区分大小写]</b><br/>
     * 19、isEmpty(String propertyName)<b>[propertyName
     * 为空，propertyName必须为集合类型]</b><br/>
     * 20、isNotEmpty(String propertyName)<b>[propertyName
     * 不为空，propertyName必须为集合类型]</b><br/>
     * 21、isNotNull(String propertyName)<b>[propertyName
     * 不为Null，propertyName不能为集合类型]</b><br/>
     * 22、isNull(String propertyName)<b>[propertyName
     * 为Null，propertyName不能为集合类型]</b><br/>
     * 23、sizeEq(String propertyName, Integer size)<b>[propertyName 元素个数等于
     * size，propertyName必须为集合类型]</b><br/>
     * 24、sizeGe(String propertyName, Integer size)<b>[propertyName 元素个数大于等于
     * size，propertyName必须为集合类型]</b><br/>
     * 25、sizeGt(String propertyName, Integer size)<b>[propertyName 元素个数大于
     * size，propertyName必须为集合类型]</b><br/>
     * 26、sizeLe(String propertyName, Integer size)<b>[propertyName 元素个数小于等于
     * size，propertyName必须为集合类型]</b><br/>
     * 27、sizeLt(String propertyName, Integer size)<b>[propertyName 元素个数小于
     * size，propertyName必须为集合类型]</b><br/>
     * 28、sizeNe(String propertyName, Integer size)<b>[propertyName 元素个数不等于
     * size，propertyName必须为集合类型]</b><br/>
     * 29、lengthEq(propertyName, Integer length)<b>[propertyName 长度等于
     * length，propertyName必须为String类型]</b><br/>
     * 30、lengthGe(propertyName, Integer length)<b>[propertyName 长度大于等于
     * length，propertyName必须为String类型]</b><br/>
     * 31、lengthGt(propertyName, Integer length)<b>[propertyName 长度大于
     * length，propertyName必须为String类型]</b><br/>
     * 32、lengthLe(propertyName, Integer length)<b>[propertyName 长度小于等于
     * length，propertyName必须为String类型]</b><br/>
     * 33、lengthLt(propertyName, Integer length)<b>[propertyName 长度小于
     * length，propertyName必须为String类型]</b><br/>
     * 34、lengthNe(propertyName, Integer length)<b>[propertyName 长度不等于
     * length，propertyName必须为String类型]</b><br/>
     * 35、between(String propertyName,Object value1,Object
     * value2)<b>[propertyName 值介于 value1 和 value2 之间]</b><br/>
     * 36、notBetween(String propertyName,Object value1,Object
     * value2)<b>[propertyName 值不介于 value1 和 value2 之间]</b><br/>
     * 37、lengthEqProperty(String propertyName,String
     * otherPropertyName)<b>[propertyName 得长度 等于
     * otherPropertyName的长度，propertyName必须为String类型]</b><br/>
     * 38、lengthGeProperty(String propertyName,String
     * otherPropertyName)<b>[propertyName 得长度 大于等于
     * otherPropertyName的长度，propertyName必须为String类型]</b><br/>
     * 39、lengthGtProperty(String propertyName,String
     * otherPropertyName)<b>[propertyName 得长度 大于
     * otherPropertyName的长度，propertyName必须为String类型]</b><br/>
     * 40、lengthLeProperty(String propertyName,String
     * otherPropertyName)<b>[propertyName 得长度 小于等于
     * otherPropertyName的长度，propertyName必须为String类型]</b><br/>
     * 41、lengthLtProperty(String propertyName,String
     * otherPropertyName)<b>[propertyName 得长度 小于
     * otherPropertyName的长度，propertyName必须为String类型]</b><br/>
     * 42、lengthNeProperty(String propertyName,String
     * otherPropertyName)<b>[propertyName 得长度 不等于
     * otherPropertyName的长度，propertyName必须为String类型]</b><br/>
     */
    public String queryParams() default "";

    /**
     * 如果字段是<strong>日期类型</strong>,该字段代表选择的日期的格式<br/>
     * 如：yyyy-MM-dd HH:mm:ss<br/>
     * <br/>
     * 默认值：yyyy-MM-dd HH:mm:ss<br/>
     * <br/>
     * 注意：只对日期类型有效 包括java.util.Date java.sql.Date java.sql.Timestamp
     * java.sql.Time
     */
    public String dateFormat() default "yyyy-MM-dd HH:mm:ss";

    /**
     * 当字段为引用其他<strong>实体类</strong>的时候，该配置有效<br/>
     * 表示该<strong>实体类型</strong>字段是否允许被<strong>查看详细</strong><br/>
     * 在以下三个条件均满足的时候有效<br/>
     * 1、设置该属性为<strong>true</strong>,默认也是<strong>true</strong><br/>
     * 2、该实体类代码生成的时候配置了<strong>DodoActionType.VIEW</strong><br/>
     * 3、当前登录的管理员用户拥有权限<strong>查看该实体类型的详细信息</strong>
     * */
    public boolean isDetailView() default true;

    /**
     * 当字段为引用其他<strong>实体类</strong>的时候，该配置有效<br/>
     * 表示该<strong>实体类型</strong>字段是否允许被<strong>修改</strong><br/>
     * 在以下四个条件均满足的时候有效<br/>
     * 1、设置该属性为<strong>true</strong>,默认也是<strong>true</strong><br/>
     * 2、该实体类代码生成的时候配置了<strong>DodoActionType.TOUPDATE</strong><br/>
     * 3、当前登录的管理员用户拥有权限<strong>跳转到该实体类型的修改页面</strong><br/>
     * 4、dodo.code.generate.referentity.edit.is=true</strong>
     * */
    public boolean isDetailEdit() default false;

    /**
     * 当字段为引用其他<strong>实体类</strong>的时候，该配置有效<br/>
     * 表示该<strong>实体类型</strong>的选择方式<br/>
     * 1、<strong>true</strong>表示弹出筛选界面供用户选择，适用于目标实体类记录非常多的情况<br/>
     * 2、<strong>false</strong>表示页面使用下拉列表供用户选择，适用于目标实体类记录很少的情况<br/>
     * */
    public boolean isPopup() default false;

    /**
     * 当字段为文件字段，即：isFile、isDoc、isVideo 的时候启用该配置
     * 1、<strong>true</strong>表示允许上传多个文件<br/>
     * 2、<strong>false</strong>表示只允许上传一个文件<br/>
     * 2、<strong>注意：</strong>当 isVideo=true,即：视频文件，无论该属性是否配置，系统将强制为 false<br/>
     * */
    public boolean isMultiFile() default false;

    /**
     * 当字段为<code>java.lang.String</code>的时候启用改配置
     * 1、<strong>true</strong>表示对输入的文本进行特殊字符转义后存储<br/>
     * 2、<strong>false</strong>表示对输入的文本不做处理，直接存储，这种情况下可能引起XSS攻击<br/>
     * */
    public boolean isEncode() default true;

    /**
     * 该注解表示<strong>系统管理员类的一个属性名称</strong></br>
     * 该属性名称将用于该字段的<strong>取值来源</strong></br>
     * */
    public String srcAdminField() default "";

    /**
     * 该注解表示，该字段存取的是地理位置信息<br/>
     * 如果使用这个配置，那么添加该字段时将使用地图选择器<br/>
     * 保存时，将保存一个<strong>地理位置信息Bean</strong>序列化后的Json字符串
     * 
     * @see DodoLocationInfo
     * */
    public boolean isLocation() default false;

    /** 字段是否是颜色String 配置true 有效 页面将生成颜色选择器处理 */
    public boolean isColor() default false;

    /** 当字段配置<strong>isFile=true</strong>，并且上传的文件是图片，该配置有效，表示允许的图片宽度 单位 px */
    public int picWidth() default -1;

    /** 当字段配置<strong>isFile=true</strong>，并且上传的文件是图片，该配置有效，表示允许的图片高度 单位 px */
    public int picHeight() default -1;

    /** 文件上传时指明文件的最大尺寸，<strong>单位:MB</strong> */
    public int maxFileSize() default -1;

    /**
     * 文件是否允许断点续传 <br/>
     * 1、不允许断点续传的时候，只有当文件全部上传完成，才允许提交表单<br/>
     * 2、允许断点续传的时候，文件上传的过程中，可以随时提交表单
     * */
    public boolean isSuppBreakpoint() default false;

    /**
     * 配置当前实体的一个字段名，当这个字段值变化的时候，控制当前字段的显示与隐藏 <br/>
     * 1、被配置的字段必须为枚举类型<br/>
     * 2、与<strong>showOnValue</strong>配合使用<br/>
     * */
    public String showOnField() default "";

    /**
     * 配置枚举类型字段<strong>showOnField</strong>的一个枚举值<br/>
     * 1、配置枚举值的<strong>name()</strong><br/>
     * 2、与<strong>showOnField</strong>配合使用<br/>
     * 3、多个值用逗号隔开
     * */
    public String showOnValue() default "";

    /**
     * 是否将该字段作为查询条件放在列表页面
     * */
    public boolean queryOnList() default false;

    /**
     * 字符串类型的字段，校验正则表达式
     * */
    public String regExp() default "";

    /**
     * 字符串类型的字段，校验正则表达式不通过后的提示信息
     * */
    public String regExpTip() default "";

    /**
     * 字符串类型的字段，校验正则表达式不通过后的提示信息 ,标识Spring国际化资源文件中的一个key<br/>
     * <strong>优先级高于 regExpTip </strong>
     */
    public String regExpTipKey() default "";
}
