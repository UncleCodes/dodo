package com.example.entity.demo_4.base_1;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;

import com.dodo.common.annotation.DodoCodeGenerator;
import com.dodo.common.annotation.action.DodoAction;
import com.dodo.common.annotation.action.DodoActionGenerator;
import com.dodo.common.annotation.dao.DodoDaoGenerator;
import com.dodo.common.annotation.field.DodoField;
import com.dodo.common.annotation.field.DodoFileType;
import com.dodo.common.annotation.field.FileStyle;
import com.dodo.common.annotation.menu.DodoMenu;
import com.dodo.common.annotation.menu.DodoMenuLevel;
import com.dodo.common.annotation.right.DodoRight;
import com.dodo.common.annotation.service.DodoSrvGenerator;
import com.dodo.common.framework.entity.BaseEntity;
import com.dodo.privilege.entity.admin_1.base_1.Admin;
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
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DodoMenu(name = "Demo系统", level = DodoMenuLevel.LEVEL1, sortSeq = 7)
@DodoMenu(name = "基础演示", level = DodoMenuLevel.LEVEL2, sortSeq = 1)
@DodoMenu(name = "常用字段类型演示", level = DodoMenuLevel.LEVEL3, sortSeq = 6)
@DodoRight(name = "常用字段类型")
@DodoCodeGenerator(daoGenerator = @DodoDaoGenerator, srvGenerator = @DodoSrvGenerator, actGenerator = @DodoActionGenerator(actions = { DodoAction.ALL }))
public class BaseTypeDemo extends BaseEntity {

    private static final long serialVersionUID = -4958412697888233929L;

    @DodoField(sortSeq = 1, name = "Byte类型字段")
    private Byte              byteField;

    @DodoField(sortSeq = 2, name = "Short类型字段")
    private Short             shortField;

    @DodoField(sortSeq = 3, name = "Integer类型字段", infoTip = "列表查询条件", queryOnList = true)
    private Integer           integerField;

    @DodoField(sortSeq = 4, name = "Long类型字段")
    private Long              longField;

    @DodoField(sortSeq = 5, name = "Float类型字段")
    private Float             floatField;

    @DodoField(sortSeq = 6, name = "Double类型字段")
    private Double            doubleField;

    @DodoField(sortSeq = 7, name = "Boolean类型字段")
    private Boolean           booleanField;

    @DodoField(sortSeq = 8, name = "BigDecimal类型字段")
    private BigDecimal        bigDecimalField;

    @DodoField(sortSeq = 9, name = "String类型字段", infoTip = "列表查询条件", queryOnList = true)
    private String            stringField;

    @DodoField(sortSeq = 9, name = "String类型字段（NOT Encode）", isEncode = false, infoTip = "字符串不进行URLEncode", listable = false)
    private String            encodeField;

    @DodoField(sortSeq = 10, name = "Date类型字段", dateFormat = "yyyy-MM-dd")
    private Date              dateField;

    @DodoField(sortSeq = 11, name = "Time类型字段")
    private Time              timeField;

    @DodoField(sortSeq = 12, name = "Timestamp类型字段")
    private Timestamp         timestampField;

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

    @DodoField(sortSeq = 20, name = "单文件类型字段（限制图片）", isFile = true, fileStyle = FileStyle.OnlyPath, fileType = { @DodoFileType(title = "图片文件", extensions = "jpg,jpeg,gif,png,bmp") }, maxFileSize = 1)
    private String            imageField;

    @DodoField(sortSeq = 21, name = "单文件类型字段（限制ZIP）", isFile = true, fileStyle = FileStyle.OnlyPath, fileType = { @DodoFileType(title = "Zip文件", extensions = "zip") }, maxFileSize = 1)
    private String            zipField;

    @DodoField(sortSeq = 22, name = "多文件类型字段", isFile = true, isMultiFile = true, maxFileSize = 1)
    private String            multiZipField;

    @DodoField(sortSeq = 23, name = "文档字段", isDoc = true, maxFileSize = 1)
    private String            docField;

    @DodoField(sortSeq = 24, name = "视频文件", isVideo = true, maxFileSize = 15)
    private String            videoField;

    @DodoField(sortSeq = 25, name = "自动取值（当前用户）", isAdmin = true)
    private Admin             admin;

    @DodoField(sortSeq = 26, name = "颜色类型字段", isColor = true)
    private String            colorField;

    @DodoField(sortSeq = 27, name = "地理位置类型字段", isLocation = true)
    private String            locationField;

    public Byte getByteField() {
        return byteField;
    }

    public void setByteField(Byte byteField) {
        this.byteField = byteField;
    }

    public Short getShortField() {
        return shortField;
    }

    public void setShortField(Short shortField) {
        this.shortField = shortField;
    }

    public Integer getIntegerField() {
        return integerField;
    }

    public void setIntegerField(Integer integerField) {
        this.integerField = integerField;
    }

    public Long getLongField() {
        return longField;
    }

    public void setLongField(Long longField) {
        this.longField = longField;
    }

    public Float getFloatField() {
        return floatField;
    }

    public void setFloatField(Float floatField) {
        this.floatField = floatField;
    }

    public Double getDoubleField() {
        return doubleField;
    }

    public void setDoubleField(Double doubleField) {
        this.doubleField = doubleField;
    }

    public Boolean getBooleanField() {
        return booleanField;
    }

    public void setBooleanField(Boolean booleanField) {
        this.booleanField = booleanField;
    }

    public BigDecimal getBigDecimalField() {
        return bigDecimalField;
    }

    public void setBigDecimalField(BigDecimal bigDecimalField) {
        this.bigDecimalField = bigDecimalField;
    }

    @Column(length = 16)
    public String getStringField() {
        return stringField;
    }

    public void setStringField(String stringField) {
        this.stringField = stringField;
    }

    public Date getDateField() {
        return dateField;
    }

    public void setDateField(Date dateField) {
        this.dateField = dateField;
    }

    public Time getTimeField() {
        return timeField;
    }

    public void setTimeField(Time timeField) {
        this.timeField = timeField;
    }

    public Timestamp getTimestampField() {
        return timestampField;
    }

    public void setTimestampField(Timestamp timestampField) {
        this.timestampField = timestampField;
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

    @Column(length = 128)
    public String getEncodeField() {
        return encodeField;
    }

    public void setEncodeField(String encodeField) {
        this.encodeField = encodeField;
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

    @Lob
    public String getDocField() {
        return docField;
    }

    public void setDocField(String docField) {
        this.docField = docField;
    }

    @Lob
    public String getVideoField() {
        return videoField;
    }

    public void setVideoField(String videoField) {
        this.videoField = videoField;
    }

    @OneToOne
    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
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
