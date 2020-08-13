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
import com.dodo.common.annotation.menu.DodoMenus;
import com.dodo.common.framework.entity.BaseEntity;
import com.dodo.privilege.enums.VideoCountDownsPosition;

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
@DodoEntity(nameKey = "dodo.privilege.admin.videoad.VideoPreOtherAd.entityKey", actions = { DodoAction.ALL })
@DodoMenus(levelOne = @DodoMenu(nameKey = "dodo.privilege.admin.menuNameKey", sortSeq = 1), levelTwo = @DodoMenu(nameKey = "dodo.privilege.admin.videoad.menuNameKey", sortSeq = 3), levelThree = @DodoMenu(nameKey = "dodo.privilege.admin.videoad.VideoPreOtherAd.menuNameKey", sortSeq = 2))
public class VideoPreOtherAd extends BaseEntity implements java.io.Serializable {
    private static final long       serialVersionUID = 2989790219927042165L;

    @DodoField(sortSeq = -1, nameKey = "dodo.privilege.admin.videoad.VideoPreOtherAd.namekey.adTitle", isnullable = false, queryOnList = true)
    @DodoShowColumn(sortSeq = 0)
    private String                  adTitle;

    @DodoField(sortSeq = 0, nameKey = "dodo.privilege.admin.videoad.VideoPreOtherAd.namekey.beginTime", dateFormat = "yyyy-MM-dd HH:mm:ss", isnullable = false)
    private Date                    beginTime;

    @DodoField(sortSeq = 1, nameKey = "dodo.privilege.admin.videoad.VideoPreOtherAd.namekey.endTime", dateFormat = "yyyy-MM-dd HH:mm:ss", isnullable = false)
    private Date                    endTime;

    @DodoField(sortSeq = 2, nameKey = "dodo.privilege.admin.videoad.VideoPreOtherAd.namekey.countTime", infoTipKey = "dodo.privilege.admin.videoad.VideoPreOtherAd.infoTip.countTime", isDigits = true, min = "1", max = "3600", isnullable = false)
    private Integer                 countTime;

    @DodoField(sortSeq = 3, nameKey = "dodo.privilege.admin.videoad.VideoPreOtherAd.namekey.width", infoTipKey = "dodo.privilege.admin.videoad.VideoPreOtherAd.infoTip.width", isDigits = true, min = "1", max = "3600", isnullable = false)
    private Integer                 width;

    @DodoField(sortSeq = 4, nameKey = "dodo.privilege.admin.videoad.VideoPreOtherAd.namekey.height", infoTipKey = "dodo.privilege.admin.videoad.VideoPreOtherAd.infoTip.height", isDigits = true, min = "1", max = "3600", isnullable = false)
    private Integer                 height;

    @DodoField(sortSeq = 5, nameKey = "dodo.privilege.admin.videoad.VideoPreOtherAd.namekey.videoCountDownsPosition", isnullable = false)
    private VideoCountDownsPosition videoCountDownsPosition;

    @DodoField(sortSeq = 6, nameKey = "dodo.privilege.admin.videoad.VideoPreOtherAd.namekey.adFile", isnullable = false, isFile = true, fileType = { @DodoFileType(titleKey = "dodo.file.upload.titlekey.files", extensions = "swf,jpg,gif,png") })
    private String                  adFile;

    @DodoField(sortSeq = 7, nameKey = "dodo.privilege.admin.videoad.VideoPreOtherAd.namekey.adRealUrl", infoTipKey = "dodo.privilege.admin.videoad.VideoPreOtherAd.infoTip.adRealUrl", isnullable = false)
    private String                  adRealUrl;

    public Date getBeginTime() {
        return beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    @Column(length = 3)
    @Convert(converter = VideoCountDownsPosition.Converter.class)
    public VideoCountDownsPosition getVideoCountDownsPosition() {
        return videoCountDownsPosition;
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

    public void setVideoCountDownsPosition(VideoCountDownsPosition videoCountDownsPosition) {
        this.videoCountDownsPosition = videoCountDownsPosition;
    }

    public void setAdFile(String adFile) {
        this.adFile = adFile;
    }

    public void setAdRealUrl(String adRealUrl) {
        this.adRealUrl = adRealUrl;
    }

    public Integer getCountTime() {
        return countTime;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setCountTime(Integer countTime) {
        this.countTime = countTime;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    @Column(length = 64)
    public String getAdTitle() {
        return adTitle;
    }

    public void setAdTitle(String adTitle) {
        this.adTitle = adTitle;
    }
}
