package com.dodo.privilege.entity.admin_1.location_6;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import org.hibernate.annotations.DynamicInsert;

import com.dodo.common.annotation.action.DodoAction;
import com.dodo.common.annotation.action.DodoEntity;
import com.dodo.common.annotation.field.DodoField;
import com.dodo.common.annotation.field.DodoFileType;
import com.dodo.common.annotation.field.DodoShowColumn;
import com.dodo.common.annotation.field.FileStyle;
import com.dodo.common.annotation.menu.DodoMenu;
import com.dodo.common.annotation.menu.DodoMenus;
import com.dodo.common.annotation.tree.DodoTreeRef;
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
@DodoEntity(nameKey = "dodo.privilege.admin.location.District.entityKey", actions = { DodoAction.ALL })
@DodoMenus(levelOne = @DodoMenu(nameKey = "dodo.privilege.admin.menuNameKey", sortSeq = 1), levelTwo = @DodoMenu(nameKey = "dodo.privilege.admin.location.menuNameKey", sortSeq = 6), levelThree = @DodoMenu(nameKey = "dodo.privilege.admin.location.District.menuNameKey", sortSeq = 4))
@DodoTreeRef(mapParentField = "city", selfQueryParams = "eq(\"inUse\",true);")
public class District extends BaseEntity implements java.io.Serializable {
    private static final long serialVersionUID = -3479293736348006559L;

    @DodoField(sortSeq = 0, nameKey = "dodo.privilege.admin.location.District.namekey.name", isnullable = false, queryOnList = true)
    @DodoShowColumn(sortSeq = 1)
    private String            name;

    @DodoField(sortSeq = 1, nameKey = "dodo.privilege.admin.location.District.namekey.postCode", isnullable = false)
    @DodoShowColumn(sortSeq = 0)
    private String            postCode;

    @DodoField(sortSeq = 2, nameKey = "dodo.privilege.admin.location.District.namekey.inUse", isnullable = false)
    private Boolean           inUse;

    @DodoField(sortSeq = 3, nameKey = "dodo.privilege.admin.location.District.namekey.city", isPopup = true, isnullable = false, queryParams = "eq(\"inUse\",true)")
    private City              city;

    @DodoField(sortSeq = 4, nameKey = "dodo.privilege.admin.location.District.namekey.iconImage", isFile = true, fileType = { @DodoFileType(titleKey = "dodo.file.upload.titlekey.images", extensions = "jpg,jpeg,gif,png,bmp") }, fileStyle = FileStyle.OnlyPath)
    private String            iconImage;

    @DodoField(sortSeq = 5, nameKey = "dodo.privilege.admin.location.District.namekey.description", listable = false, isRichText = true)
    private String            description;

    @Override
    public void onSave() {
        super.onSave();
        if (inUse == null) {
            inUse = Boolean.TRUE;
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (inUse == null) {
            inUse = Boolean.TRUE;
        }
    }

    @Column(length = 32)
    public String getName() {
        return name;
    }

    public Boolean getInUse() {
        return inUse;
    }

    @OneToOne(fetch = FetchType.LAZY)
    public City getCity() {
        return city;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInUse(Boolean inUse) {
        this.inUse = inUse;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Column(length = 128)
    public String getIconImage() {
        return iconImage;
    }

    public void setIconImage(String iconImage) {
        this.iconImage = iconImage;
    }

    @Lob
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(length = 8)
    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    @Override
    public String toString() {
        return postCode + "-" + name;
    }
}