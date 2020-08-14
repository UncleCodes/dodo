package com.example.entity.demo_4.base_1;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.hibernate.annotations.DynamicInsert;

import com.dodo.common.annotation.action.DodoAction;
import com.dodo.common.annotation.action.DodoEntity;
import com.dodo.common.annotation.check.DodoUniqueGroup;
import com.dodo.common.annotation.field.DodoField;
import com.dodo.common.annotation.field.DodoFileType;
import com.dodo.common.annotation.field.DodoValueGenerator;
import com.dodo.common.annotation.field.FileStyle;
import com.dodo.common.annotation.menu.DodoMenu;
import com.dodo.common.framework.entity.BaseEntity;
import com.dodo.privilege.entity.admin_1.location_6.City;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
@Entity
@DynamicInsert
@DodoEntity(name = "字段校验", actions = { DodoAction.ALL }, levelOne = @DodoMenu(name = "Demo系统", sortSeq = 7), levelTwo = @DodoMenu(name = "基础演示", sortSeq = 1), levelThree = @DodoMenu(name = "字段校验演示", sortSeq = 5))
@DodoUniqueGroup(fieldNames = { "clazz", "name" })
public class BaseCheckDemo extends BaseEntity {

    private static final long serialVersionUID = -5083977727340920244L;

    @DodoField(sortSeq = 1, name = "IP地址", isIp = true, queryOnList = true)
    private String            ipAddr;

    @DodoField(sortSeq = 2, name = "邮箱", isEmail = true)
    private String            emailAddr;

    @DodoField(sortSeq = 3, name = "URL", isUrl = true)
    private String            urlStr;

    @DodoField(sortSeq = 4, name = "数据库唯一", isRemoteCheck = true)
    private String            remoteStr;

    @DodoField(sortSeq = 5, name = "限制长度", minLength = 3, maxLength = 10)
    private String            lengthStr;

    @DodoField(sortSeq = 6, name = "非空", isnullable = false)
    private String            notnullStr;

    @DodoField(sortSeq = 7, name = "限制大小", min = "666", max = "888")
    private Integer           maxminNumber;

    @DodoField(sortSeq = 8, name = "城市1", infoTip = "弹出选择，所有名字包含'张'的城市", queryParams = "like(\"name\",\"张\")", isPopup = true, queryOnList = true)
    private City              city1;

    @DodoField(sortSeq = 9, name = "城市2", infoTip = "所有名字包含'门'的城市", queryParams = "like(\"name\",\"门\")")
    private City              city2;

    @DodoField(sortSeq = 11, name = "自动取值：数据库")
    @DodoValueGenerator
    private String            autoValue1;

    @DodoField(sortSeq = 12, name = "自动取值：自定义方法")
    @DodoValueGenerator(generateClass = com.example.utils.AutoValue.class, generateMethodName = "getAutoValue")
    private String            autoValue2;

    @DodoField(sortSeq = 13, name = "正则校验", regExp = "\\d{3,8}", regExpTip = "请输入我设定的规则~~哈", infoTip = "正则：3-8位数字")
    private String            regStr;

    @DodoField(sortSeq = 20, name = "1M以内图片", isFile = true, fileStyle = FileStyle.OnlyPath, fileType = { @DodoFileType(title = "图片文件", extensions = "jpg,jpeg,gif,png,bmp") }, maxFileSize = 1)
    private String            imageField;

    @DodoField(sortSeq = 21, name = "班级", isSetDefault = false, infoTip = "与'姓名'联合唯一")
    private String            clazz;

    @DodoField(sortSeq = 22, name = "姓名", isSetDefault = false, infoTip = "与'班级'联合唯一")
    private String            name;

    @Column(length = 32)
    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    @Column(length = 32)
    public String getEmailAddr() {
        return emailAddr;
    }

    public void setEmailAddr(String emailAddr) {
        this.emailAddr = emailAddr;
    }

    @Column(length = 64)
    public String getUrlStr() {
        return urlStr;
    }

    public void setUrlStr(String urlStr) {
        this.urlStr = urlStr;
    }

    @Column(length = 16)
    public String getRemoteStr() {
        return remoteStr;
    }

    public void setRemoteStr(String remoteStr) {
        this.remoteStr = remoteStr;
    }

    @Column(length = 16)
    public String getLengthStr() {
        return lengthStr;
    }

    public void setLengthStr(String lengthStr) {
        this.lengthStr = lengthStr;
    }

    @Column(length = 8)
    public String getNotnullStr() {
        return notnullStr;
    }

    public void setNotnullStr(String notnullStr) {
        this.notnullStr = notnullStr;
    }

    public Integer getMaxminNumber() {
        return maxminNumber;
    }

    public void setMaxminNumber(Integer maxminNumber) {
        this.maxminNumber = maxminNumber;
    }

    @OneToOne
    public City getCity1() {
        return city1;
    }

    public void setCity1(City city1) {
        this.city1 = city1;
    }

    @OneToOne
    public City getCity2() {
        return city2;
    }

    public void setCity2(City city2) {
        this.city2 = city2;
    }

    @Column(length = 16)
    public String getAutoValue1() {
        return autoValue1;
    }

    public void setAutoValue1(String autoValue1) {
        this.autoValue1 = autoValue1;
    }

    @Column(length = 32)
    public String getAutoValue2() {
        return autoValue2;
    }

    public void setAutoValue2(String autoValue2) {
        this.autoValue2 = autoValue2;
    }

    @Column(length = 16)
    public String getRegStr() {
        return regStr;
    }

    public void setRegStr(String regStr) {
        this.regStr = regStr;
    }

    @Column(length = 128)
    public String getImageField() {
        return imageField;
    }

    public void setImageField(String imageField) {
        this.imageField = imageField;
    }

    @Column(length = 8)
    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    @Column(length = 8)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
