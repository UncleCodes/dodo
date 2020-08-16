package com.example.entity.demo_4.base_1;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import org.hibernate.annotations.DynamicInsert;

import com.dodo.common.annotation.action.DodoAction;
import com.dodo.common.annotation.action.DodoEntity;
import com.dodo.common.annotation.field.DodoField;
import com.dodo.common.annotation.field.DodoFileType;
import com.dodo.common.annotation.field.FileStyle;
import com.dodo.common.annotation.menu.DodoMenu;
import com.dodo.common.framework.entity.BaseEntity;
import com.dodo.privilege.entity.admin_1.location_6.City;
import com.dodo.privilege.entity.admin_1.location_6.Province;
import com.example.enums.DemoEnum;

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
@DodoEntity(
        name = "单记录测试",
        actions = { DodoAction.ALL },
        levelOne = @DodoMenu(name = "Demo系统", sortSeq = 7),
        levelTwo = @DodoMenu(name = "基础演示", sortSeq = 1),
        levelThree = @DodoMenu(name = "单记录测试", sortSeq = 11),
        singleRecord = true)
public class SingleRecord extends BaseEntity {

    private static final long serialVersionUID = 3130356442497463407L;

    @DodoField(sortSeq = 7, name = "Boolean类型字段")
    private Boolean           booleanField;

    @DodoField(sortSeq = 10, name = "Date类型字段", dateFormat = "yyyy-MM-dd")
    private Date              dateField;

    @DodoField(sortSeq = 13, name = "枚举类型字段")
    private DemoEnum          enumField;

    @DodoField(sortSeq = 14, name = "引用类型字段（下拉）", infoTip = "列表查询条件", queryOnList = true)
    private Province          province;

    @DodoField(sortSeq = 15, name = "引用类型字段（弹出）", isPopup = true)
    private City              city;

    @DodoField(sortSeq = 18, name = "Textarea类型字段", isTextArea = true)
    private String            textareaField;

    @DodoField(sortSeq = 19, name = "富文本类型字段", isRichText = true)
    private String            richtextField;

    @DodoField(
            sortSeq = 20,
            name = "单文件类型字段（限制图片）",
            isFile = true,
            fileStyle = FileStyle.OnlyPath,
            fileType = { @DodoFileType(title = "图片文件", extensions = "jpg,jpeg,gif,png,bmp") },
            maxFileSize = 1)
    private String            imageField;

    @DodoField(
            sortSeq = 21,
            name = "单文件类型字段（限制ZIP）",
            isFile = true,
            fileStyle = FileStyle.OnlyPath,
            fileType = { @DodoFileType(title = "Zip文件", extensions = "zip") },
            maxFileSize = 1)
    private String            zipField;

    @DodoField(sortSeq = 22, name = "多文件类型字段", isFile = true, isMultiFile = true, maxFileSize = 1)
    private String            multiZipField;

    @DodoField(sortSeq = 26, name = "颜色类型字段", isColor = true)
    private String            colorField;

    @DodoField(sortSeq = 27, name = "地理位置类型字段", isLocation = true)
    private String            locationField;

    public Boolean getBooleanField() {
        return booleanField;
    }

    public void setBooleanField(Boolean booleanField) {
        this.booleanField = booleanField;
    }

    public Date getDateField() {
        return dateField;
    }

    public void setDateField(Date dateField) {
        this.dateField = dateField;
    }

    @Column(length = 3)
    @Convert(converter = DemoEnum.Converter.class)
    public DemoEnum getEnumField() {
        return enumField;
    }

    public void setEnumField(DemoEnum enumField) {
        this.enumField = enumField;
    }

    @OneToOne
    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    @OneToOne
    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Column(length = 64)
    public String getTextareaField() {
        return textareaField;
    }

    public void setTextareaField(String textareaField) {
        this.textareaField = textareaField;
    }

    @Lob
    public String getRichtextField() {
        return richtextField;
    }

    public void setRichtextField(String richtextField) {
        this.richtextField = richtextField;
    }

    @Column(length = 128)
    public String getImageField() {
        return imageField;
    }

    public void setImageField(String imageField) {
        this.imageField = imageField;
    }

    @Column(length = 128)
    public String getZipField() {
        return zipField;
    }

    public void setZipField(String zipField) {
        this.zipField = zipField;
    }

    @Lob
    public String getMultiZipField() {
        return multiZipField;
    }

    public void setMultiZipField(String multiZipField) {
        this.multiZipField = multiZipField;
    }

    @Column(length = 16)
    public String getColorField() {
        return colorField;
    }

    public void setColorField(String colorField) {
        this.colorField = colorField;
    }

    @Column(length = 64)
    public String getLocationField() {
        return locationField;
    }

    public void setLocationField(String locationField) {
        this.locationField = locationField;
    }
}
