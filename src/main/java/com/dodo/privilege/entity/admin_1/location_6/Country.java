package com.dodo.privilege.entity.admin_1.location_6;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
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
@DodoEntity(nameKey = "dodo.privilege.admin.location.Country.entityKey", actions = { DodoAction.ALL })
@DodoMenus(levelOne = @DodoMenu(nameKey = "dodo.privilege.admin.menuNameKey", sortSeq = 1), levelTwo = @DodoMenu(nameKey = "dodo.privilege.admin.location.menuNameKey", sortSeq = 6), levelThree = @DodoMenu(nameKey = "dodo.privilege.admin.location.Country.menuNameKey", sortSeq = 1))
@DodoTreeRef(mapParentField = "__ROOT_END__", selfQueryParams = "eq(\"inUse\",true);")
public class Country extends BaseEntity {
    private static final long serialVersionUID = 3849237922158766894L;

    @DodoField(sortSeq = 0, nameKey = "dodo.privilege.admin.location.Country.namekey.name", isnullable = false, queryOnList = true)
    @DodoShowColumn(sortSeq = 1)
    private String            name;

    @DodoField(sortSeq = 1, nameKey = "dodo.privilege.admin.location.Country.namekey.areaCode", isnullable = false)
    @DodoShowColumn(sortSeq = 0)
    private String            areaCode;

    @DodoField(sortSeq = 2, nameKey = "dodo.privilege.admin.location.Country.namekey.inUse", isnullable = false)
    private Boolean           inUse;

    @DodoField(sortSeq = 3, nameKey = "dodo.privilege.admin.location.Country.namekey.provinceList", addable = false, editable = false, listable = false, isShowWholeTree = true)
    private List<Province>    provinceList;

    @DodoField(sortSeq = 6, nameKey = "dodo.privilege.admin.location.Country.namekey.iconImage", isFile = true, fileType = { @DodoFileType(titleKey = "dodo.file.upload.titlekey.images", extensions = "jpg,jpeg,gif,png,bmp") }, fileStyle = FileStyle.OnlyPath)
    private String            iconImage;

    @DodoField(sortSeq = 7, nameKey = "dodo.privilege.admin.location.Country.namekey.description", isRichText = true)
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

    @OneToMany(cascade = { CascadeType.REMOVE }, fetch = FetchType.LAZY, targetEntity = Province.class, mappedBy = "country")
    @Fetch(FetchMode.SUBSELECT)
    @OrderBy("sortSeq asc")
    public List<Province> getProvinceList() {
        return provinceList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInUse(Boolean inUse) {
        this.inUse = inUse;
    }

    public void setProvinceList(List<Province> provinceList) {
        this.provinceList = provinceList;
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

    @Column(unique = true, length = 16)
    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    @Override
    public String toString() {
        return areaCode + "-" + name;
    }
}
