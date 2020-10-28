# Dodo <font color="#376BFB">Framework</font>

## 资源
* DodoFramework 项目主站 <https://www.0yi0.com>
* 查看在线文档 <https://www.0yi0.com/doc/>
* 组织在这里（QQ①群）：678785173

## 介绍
一个基于代码生成引擎的Java Web自动化开发框架，开发效率提升90+%，后台管理系统秒级生成。

### 优势及场景：

**（1）重复的工作，代码生成，不再烦心。**

**（2）学习成本低：仅需学习如何对实体类加注解即可；实体类的注解代码生成完成后，可以删掉，不影响运行**

**（3）中小型Java Web系统、APP或者小程序的后台管理系统，基本上生成无须或者很少改动。让精力更加focus在业务逻辑上。**

**（4）共十一套页面模板，随意挑选**

## 示例效果图
![登录图片](https://www.0yi0.com/doc/dodo_pro_snapshots/WechatIMG37.jpeg)
![登录图片](https://www.0yi0.com/doc/dodo_pro_snapshots/WechatIMG38.jpeg)
![登录图片](https://www.0yi0.com/doc/dodo_pro_snapshots/WechatIMG74.jpeg)
![](https://www.0yi0.com/doc/dodo_pro_snapshots/WechatIMG50.jpeg)
![](https://www.0yi0.com/doc/dodo_pro_snapshots/WechatIMG56.jpeg)
![登录图片](https://www.0yi0.com/doc/dodo_pro_snapshots/WechatIMG85.jpeg)
![](https://www.0yi0.com/doc/dodo_pro_snapshots/WechatIMG57.jpeg)
![](https://www.0yi0.com/doc/dodo_pro_snapshots/WechatIMG62.jpeg)
![](https://www.0yi0.com/doc/dodo_pro_snapshots/WechatIMG64.jpeg)
![](https://www.0yi0.com/doc/dodo_pro_snapshots/WechatIMG65.jpeg)
![](https://www.0yi0.com/doc/dodo_pro_snapshots/WechatIMG67.jpeg)
![](https://www.0yi0.com/doc/dodo_pro_snapshots/WechatIMG70.jpeg)

## Get Started
### 第一步：POJO 实体类注解
#### 写好Java 实体类后，使用Dodo注解扩展

```java
 
    @DodoField(name = "产品名称", sortSeq = 0, isRemoteCheck = true)
    private String            name;

    @DodoField(name = "产品描述", sortSeq = 5, listable = false, isnullable = false, isRichText = true)
    private String            productDesc;

    @DodoField(name = "产品价格", sortSeq = 1)
    private BigDecimal        price;

    @DodoField(name = "产品积分", sortSeq = 2, max = Integer.MAX_VALUE + "", min = Integer.MIN_VALUE + "")
    private int               buyScore;

    @DodoField(name = "产品状态", sortSeq = 4, isnullable = false, isRemoteCheck = false)
    private ProductStatus     productStatus;

    @DodoField(name = "产品颜色", sortSeq = 5, isnullable = false, isColor = true)
    private String            productColor;
    
```


### 第二步：生成代码
#### 运行代码生成引擎，生成MVC、DB代码数据
```javascript

16:56:21.791 [main] INFO  c.g.f.p.counter.FileLineCounter - Congratulations:
16:56:21.791 [main] INFO  c.g.f.p.counter.FileLineCounter - Finally:
16:56:21.791 [main] INFO  c.g.f.p.counter.FileLineCounter - You have written: 43 files : 8670 lines
16:56:21.791 [main] INFO  c.g.f.p.counter.FileLineCounter - Generate: 772 files : 381785 lines
16:56:21.791 [main] INFO  c.g.f.p.counter.FileLineCounter - The percentage of generated code is 97.78%:
16:56:21.791 [main] INFO  c.generate.utils.Step_4_GenerateCode - 
16:56:21.791 [main] INFO  c.generate.utils.Step_4_GenerateCode - ******************************************************
16:56:21.791 [main] INFO  c.generate.utils.Step_4_GenerateCode - Step_4_GenerateCode ....Exec OK!
16:56:21.791 [main] INFO  c.generate.utils.Step_4_GenerateCode - Take time -> 00:00:05:037
                    
```


### 第三步：Coffee time
### 部署三联：刷新、打包、部署；大部分项目产出的管理后台无须任何改动。

## DEMOS（11套页面模板）
### DEMO系统的编码量只是写了几个实体类而已，其余全部由引擎生成，包括权限管理、基础管理等模块

####（1）Dodo Framework Pro 在线演示
* [Durian：页面模板采用最新版本的layui-v2.5.6](https://admin-pro.0yi0.com/back/enjoy/login_index.jhtml) --> 强烈推荐 + 置顶++ 

####（2）Dodo Framework 在线演示
* [Cherry：导航树模式，jquery+html](https://admin.0yi0.com/back/enjoy/login_index.jhtml?dodo_theme=cherry) --> 强烈推荐 + 置顶1

* [Jujube：easyui抽屉树+换肤+极速，easyui](https://admin.0yi0.com/back/enjoy/login_index.jhtml?dodo_theme=jujube) --> 强烈推荐 + 置顶2

* [Grape：bootstrap 模式，bootstrap](https://admin.0yi0.com/back/enjoy/login_index.jhtml?dodo_theme=grape) --> 强烈推荐 + 置顶3

* [Apple：传统导航+菜单模式，jquery+html](https://admin.0yi0.com/back/enjoy/login_index.jhtml?dodo_theme=apple)

* [Banana：导航+菜单模式，jquery.ui+html](https://admin.0yi0.com/back/enjoy/login_index.jhtml?dodo_theme=banana)

* [Duke：easyui导航树框架+html内页模式，easyui+html](https://admin.0yi0.com/back/enjoy/login_index.jhtml?dodo_theme=duke)

* [Fig：easyui抽屉树框架+html内页模式，easyui+html](https://admin.0yi0.com/back/enjoy/login_index.jhtml?dodo_theme=fig)

* [Gingko：dwz框架+html内页模式，dwz+html](https://admin.0yi0.com/back/enjoy/login_index.jhtml?dodo_theme=gingko)

* [Greengage：easyui导航树模式，easyui](https://admin.0yi0.com/back/enjoy/login_index.jhtml?dodo_theme=greengage)

* [Hawthorn：easyui抽屉树模式，easyui](https://admin.0yi0.com/back/enjoy/login_index.jhtml?dodo_theme=hawthorn)


## What is Dodo?
一句话概括这个项目：这是一个基于代码生成引擎的Java Web系统自动化开发框架。简单的说，就是一个Java Web整合的基础框架加上一个代码生成引擎。

学习成本极低（几个注解，仅应用在Java 实体类），大众化的基础框架（Spring MVC、Hibernate、Freemarker），瞬间生成完全手写代码，代码注释完备，安全健壮，命名规范，可读性高，可维护，可扩展，针对特定需求可轻松修改。

### （1）安全稳定的基础框架
基于Spring MVC-5.1、Hibernate-5.4、Freemarker、Druid框架组合

管理系统安全框架：Spring Security-5.2

安全增强：登录加盐，动态Salt、静态Salt；支持随时变换后台访问地址，上线后由运营同学修改，增加安全性

8年使用实践，0问题报告

### （2）MVC、DB全代码生成
全代码自动生成：对Java实体注解增强，然后瞬间生成MVC每层代码；代码全注释，修改易维护

生成功能多样可配置，需求轻松应对；无需关心数据库，数据库表自动生成（带注释），数据库表命名规范、可维护；基础数据自动生成；

内置十七种Detector，配置问题提前发现，确保生成系统正常运行，万无一失

### （3）节省开发时间
学习成本极低：只需要学习几个注解，而且注解只应用于Java POJO类上

支持近二十种组件，每种组件一个注解搞定，而且支持各种校验、控制，PDF自动在线预览，断点续传，自动取值，文档、视频自动转换展示等等

集成微信SDK，微信开发更快速

HQLHelper：链式编写HQL查询语句，无需了解HQL知识；另外支持JDBC

### （4）简单强大的在线报表
在线编辑SQL、配置菜单、配置查询条件、分页筛选、导出Excel、指定子视图等等

### （5）精细的权限控制
功能权限、字段权限、行级数据权限、按钮权限等，支持权限委托、权限动态更新

### （6）完善的日志记录
业务日志、登录日志、请求日志、Slow Sql日志、后台登录会话查看、踢出

## Features
* 基础框架简单安全，人人都会：Spring MVC、Spring Security、Hibernate、Freemarker、Druid
* 全代码自动生成：对Java实体注解增强，然后瞬间生成MVC每层代码；代码全注释，修改易维护
* 生成功能多样可配置，需求轻松应对：CRUD、批量导入导出、批量更新、统计列表图表、弹出选择等
* 无需关心数据库，数据库表自动生成（带注释），数据库表命名规范、可维护
* 基础数据自动生成：菜单、权限、字段权限、角色、管理员、实体、字段配置等等
* 代码生成全面预校验：内置十七种Detector，将配置问题暴露代码生成之前，麻麻再也不用担心生成代码无法运行、报错了
* 全面国际化支持：菜单、字段、枚举、提示文本、easyUI、ueditor、分页等等，一键切换
* 支持多种组件：字符串、文本域、富文本、文件、文档、视频、多文件、地理位置、颜色、密码域、数字、整数、日期、等等
* 组件支持各种校验：非空、最大最小、邮箱、手机、URL、信用卡、IP地址、日期，自定义正则表达式
* 组件支持各种控制：不同组件录入及展示方式不同，断点续传、图片尺寸校验、文档自动转换在线预览、视频自动转换在线预览、pdf在线预览、自动取值等
* HQLHelper：链式编写HQL查询语句，无需了解HQL知识；另外支持JDBC
* 精细的权限控制：功能权限、字段权限、行级数据权限、按钮权限等，支持权限委托、权限动态更新
* 简单强大的在线报表：在线编辑SQL、配置菜单、配置查询条件、分页筛选、导出Excel、指定子视图等等
* 完善的日志记录：业务日志、登录日志、请求日志、Slow Sql日志、后台登录会话查看、踢出
* 无限级联支持：多组级联、混合级联
* 各种树支持：单表树、多表树、混合树
* 支持模型扩展（后台）：无需生成代码，后台通过模型扩展模块给模型添加虚拟字段，支持各种组件
* 游离文件自动删除：上传的垃圾文件自动记录并自动删除（可选）
* 多数据源支持
* 安全增强：登录加盐，动态Salt、静态Salt；支持随时变换后台访问地址，上线后由运营同学修改，增加安全性
* 更多特性开发中...


## License
* Dodo的开源协议 **Apache-2.0 License**.
* Dodo可以被用来**完全免费**开发任何**私有的或者商业**项目。
* 但是将Dodo本身（包括以任何形式的修改）作为一个工具或者开发平台、框架而从事商业活动，是不被授权允许的。Dodo保留运用法律手段的权利。
* **软著登记号：2019SR0331113**

