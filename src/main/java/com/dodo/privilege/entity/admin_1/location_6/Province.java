package com.dodo.privilege.entity.admin_1.location_6;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
@DodoEntity(nameKey = "dodo.privilege.admin.location.Province.entityKey", actions = { DodoAction.ALL })
@DodoMenus(levelOne = @DodoMenu(nameKey = "dodo.privilege.admin.menuNameKey", sortSeq = 1), levelTwo = @DodoMenu(nameKey = "dodo.privilege.admin.location.menuNameKey", sortSeq = 6), levelThree = @DodoMenu(nameKey = "dodo.privilege.admin.location.Province.menuNameKey", sortSeq = 2))
@DodoTreeRef(mapParentField = "country", selfQueryParams = "eq(\"inUse\",true);")
public class Province extends BaseEntity implements java.io.Serializable {
    private static final long serialVersionUID = 3461291242143172195L;

    @DodoField(sortSeq = 0, nameKey = "dodo.privilege.admin.location.Province.namekey.name", isnullable = false, queryOnList = true)
    @DodoShowColumn(sortSeq = 1)
    private String            name;

    @DodoField(sortSeq = 2, nameKey = "dodo.privilege.admin.location.Province.namekey.inUse", isnullable = false)
    private Boolean           inUse;

    @DodoField(sortSeq = 3, nameKey = "dodo.privilege.admin.location.Province.namekey.country", isnullable = false, queryParams = "eq(\"inUse\",true);")
    private Country           country;

    @DodoField(sortSeq = 4, nameKey = "dodo.privilege.admin.location.Province.namekey.cityList", addable = false, editable = false, listable = false, isShowWholeTree = true)
    private List<City>        cityList;

    @DodoField(sortSeq = 5, nameKey = "dodo.privilege.admin.location.Province.namekey.iconImage", isFile = true, fileType = { @DodoFileType(titleKey = "dodo.file.upload.titlekey.images", extensions = "jpg,jpeg,gif,png,bmp") }, fileStyle = FileStyle.OnlyPath)
    private String            iconImage;

    @DodoField(sortSeq = 6, nameKey = "dodo.privilege.admin.location.Province.namekey.description", listable = false, isRichText = true)
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
    public Country getCountry() {
        return country;
    }

    @OneToMany(cascade = { CascadeType.REMOVE }, fetch = FetchType.LAZY, targetEntity = City.class, mappedBy = "province")
    @Fetch(FetchMode.SUBSELECT)
    @OrderBy("sortSeq asc")
    public List<City> getCityList() {
        return cityList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInUse(Boolean inUse) {
        this.inUse = inUse;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
    }

    @Lob
    public String getDescription() {
        return description;
    }

    @Column(length = 128)
    public String getIconImage() {
        return iconImage;
    }

    public void setIconImage(String iconImage) {
        this.iconImage = iconImage;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name;
    }
}