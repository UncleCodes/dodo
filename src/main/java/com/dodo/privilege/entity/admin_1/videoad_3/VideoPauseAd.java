package com.dodo.privilege.entity.admin_1.videoad_3;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.annotations.DynamicInsert;

import com.dodo.common.annotation.action.DodoAction;
import com.dodo.common.annotation.action.DodoEntity;
import com.dodo.common.annotation.field.DodoField;
import com.dodo.common.annotation.field.DodoFileType;
import com.dodo.common.annotation.field.DodoShowColumn;
import com.dodo.common.annotation.menu.DodoMenu;
import com.dodo.common.framework.entity.BaseEntity;

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
@DodoEntity(nameKey = "dodo.privilege.admin.videoad.VideoPauseAd.entityKey", actions = { DodoAction.ALL }, levelOne = @DodoMenu(nameKey = "dodo.privilege.admin.menuNameKey", sortSeq = 1), levelTwo = @DodoMenu(nameKey = "dodo.privilege.admin.videoad.menuNameKey", sortSeq = 3), levelThree = @DodoMenu(nameKey = "dodo.privilege.admin.videoad.VideoPauseAd.menuNameKey", sortSeq = 3))
public class VideoPauseAd extends BaseEntity implements java.io.Serializable {
    private static final long serialVersionUID = 4785854694751681222L;

    @DodoField(sortSeq = -1, nameKey = "dodo.privilege.admin.videoad.VideoPauseAd.namekey.adTitle", isnullable = false, queryOnList = true)
    @DodoShowColumn(sortSeq = 0)
    private String            adTitle;

    @DodoField(sortSeq = 0, nameKey = "dodo.privilege.admin.videoad.VideoPauseAd.namekey.beginTime", dateFormat = "yyyy-MM-dd HH:mm:ss", isnullable = false)
    private Date              beginTime;

    @DodoField(sortSeq = 1, nameKey = "dodo.privilege.admin.videoad.VideoPauseAd.namekey.endTime", dateFormat = "yyyy-MM-dd HH:mm:ss", isnullable = false)
    private Date              endTime;

    @DodoField(sortSeq = 4, nameKey = "dodo.privilege.admin.videoad.VideoPauseAd.namekey.adFile", isnullable = false, isFile = true, fileType = { @DodoFileType(titleKey = "dodo.file.upload.titlekey.files", extensions = "swf,jpg,gif,png") })
    private String            adFile;

    @DodoField(sortSeq = 5, nameKey = "dodo.privilege.admin.videoad.VideoPauseAd.namekey.adRealUrl", infoTipKey = "dodo.privilege.admin.videoad.VideoPauseAd.infoTip.adRealUrl", isnullable = false)
    private String            adRealUrl;

    @DodoField(sortSeq = 6, nameKey = "dodo.privilege.admin.videoad.VideoPauseAd.namekey.width", infoTipKey = "dodo.privilege.admin.videoad.VideoPauseAd.infoTip.width", isDigits = true, min = "1", max = "3600", isnullable = false)
    private Integer           width;

    @DodoField(sortSeq = 7, nameKey = "dodo.privilege.admin.videoad.VideoPauseAd.namekey.height", infoTipKey = "dodo.privilege.admin.videoad.VideoPauseAd.infoTip.height", isDigits = true, min = "1", max = "3600", isnullable = false)
    private Integer           height;

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

    @Column(length = 64)
    public String getAdTitle() {
        return adTitle;
    }

    public void setAdTitle(String adTitle) {
        this.adTitle = adTitle;
    }
}
