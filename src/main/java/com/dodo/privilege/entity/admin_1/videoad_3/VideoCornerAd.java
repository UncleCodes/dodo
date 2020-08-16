package com.dodo.privilege.entity.admin_1.videoad_3;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;

import org.hibernate.annotations.DynamicInsert;

import com.dodo.common.annotation.action.DodoAction;
import com.dodo.common.annotation.action.DodoEntity;
import com.dodo.common.annotation.field.DodoField;
import com.dodo.common.annotation.field.DodoFileType;
import com.dodo.common.annotation.field.DodoShowColumn;
import com.dodo.common.annotation.menu.DodoMenu;
import com.dodo.common.framework.entity.BaseEntity;
import com.dodo.privilege.enums.VideoCornerPosition;

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
        nameKey = "dodo.privilege.admin.videoad.VideoCornerAd.entityKey",
        actions = { DodoAction.ALL },
        levelOne = @DodoMenu(nameKey = "dodo.privilege.admin.menuNameKey", sortSeq = 1),
        levelTwo = @DodoMenu(nameKey = "dodo.privilege.admin.videoad.menuNameKey", sortSeq = 3),
        levelThree = @DodoMenu(nameKey = "dodo.privilege.admin.videoad.VideoCornerAd.menuNameKey", sortSeq = 4))
public class VideoCornerAd extends BaseEntity implements java.io.Serializable {
    private static final long   serialVersionUID = -5382027939326793244L;

    @DodoField(
            sortSeq = -1,
            nameKey = "dodo.privilege.admin.videoad.VideoCornerAd.namekey.adTitle",
            isnullable = false,
            queryOnList = true)
    @DodoShowColumn(sortSeq = 0)
    private String              adTitle;

    @DodoField(
            sortSeq = 0,
            nameKey = "dodo.privilege.admin.videoad.VideoCornerAd.namekey.beginTime",
            dateFormat = "yyyy-MM-dd HH:mm:ss",
            isnullable = false)
    private Date                beginTime;

    @DodoField(
            sortSeq = 1,
            nameKey = "dodo.privilege.admin.videoad.VideoCornerAd.namekey.endTime",
            dateFormat = "yyyy-MM-dd HH:mm:ss",
            isnullable = false)
    private Date                endTime;

    @DodoField(
            sortSeq = 3,
            nameKey = "dodo.privilege.admin.videoad.VideoCornerAd.namekey.videoCornerPosition",
            isnullable = false)
    private VideoCornerPosition videoCornerPosition;

    @DodoField(
            sortSeq = 4,
            nameKey = "dodo.privilege.admin.videoad.VideoCornerAd.namekey.adFile",
            isnullable = false,
            isFile = true,
            fileType = { @DodoFileType(titleKey = "dodo.file.upload.titlekey.files", extensions = "swf,jpg,gif,png") })
    private String              adFile;

    @DodoField(
            sortSeq = 5,
            nameKey = "dodo.privilege.admin.videoad.VideoCornerAd.namekey.adRealUrl",
            infoTipKey = "dodo.privilege.admin.videoad.VideoCornerAd.infoTip.adRealUrl",
            isnullable = false)
    private String              adRealUrl;

    @DodoField(
            sortSeq = 6,
            nameKey = "dodo.privilege.admin.videoad.VideoCornerAd.namekey.width",
            infoTipKey = "dodo.privilege.admin.videoad.VideoCornerAd.infoTip.width",
            isDigits = true,
            min = "1",
            max = "3600",
            isnullable = false)
    private Integer             width;

    @DodoField(
            sortSeq = 7,
            nameKey = "dodo.privilege.admin.videoad.VideoCornerAd.namekey.height",
            infoTipKey = "dodo.privilege.admin.videoad.VideoCornerAd.infoTip.height",
            isDigits = true,
            min = "1",
            max = "3600",
            isnullable = false)
    private Integer             height;

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    @Column(length = 1024)
    public String getAdFile() {
        return adFile;
    }

    @Column(length = 128)
    public String getAdRealUrl() {
        return adRealUrl;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setAdFile(String adFile) {
        this.adFile = adFile;
    }

    public void setAdRealUrl(String adRealUrl) {
        this.adRealUrl = adRealUrl;
    }

    @Column(length = 3)
    @Convert(converter = VideoCornerPosition.Converter.class)
    public VideoCornerPosition getVideoCornerPosition() {
        return videoCornerPosition;
    }

    public void setVideoCornerPosition(VideoCornerPosition videoCornerPosition) {
        this.videoCornerPosition = videoCornerPosition;
    }

    @Column(length = 64)
    public String getAdTitle() {
        return adTitle;
    }

    public void setAdTitle(String adTitle) {
        this.adTitle = adTitle;
    }
}
