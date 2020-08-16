package com.dodo.privilege.entity.admin_1.config_5;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.dodo.common.annotation.action.DodoAction;
import com.dodo.common.annotation.action.DodoEntity;
import com.dodo.common.annotation.field.DodoField;
import com.dodo.common.annotation.field.DodoShowColumn;
import com.dodo.common.annotation.field.DodoValueGenerator;
import com.dodo.common.annotation.menu.DodoMenu;
import com.dodo.common.annotation.menu.DodoMenuLevel;
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
@DodoEntity(
        nameKey = "dodo.privilege.admin.config.MenuInfo.entityKey",
        actions = { DodoAction.ALL },
        levelOne = @DodoMenu(nameKey = "dodo.privilege.admin.menuNameKey", sortSeq = 1),
        levelTwo = @DodoMenu(nameKey = "dodo.privilege.admin.config.menuNameKey", sortSeq = 5),
        levelThree = @DodoMenu(nameKey = "dodo.privilege.admin.config.MenuInfo.menuNameKey", sortSeq = 1))
@DodoTreeRef(mapParentField = "parentMenuInfo")
public class MenuInfo extends BaseEntity implements java.io.Serializable {
    private static final long serialVersionUID = 8523228719650980079L;

    @DodoField(
            sortSeq = 0,
            nameKey = "dodo.privilege.admin.config.MenuInfo.namekey.menuLevel",
            isnullable = false,
            editable = false,
            queryOnList = true)
    @DodoShowColumn(sortSeq = 1)
    private DodoMenuLevel     menuLevel;

    @DodoField(sortSeq = 1, nameKey = "dodo.privilege.admin.config.MenuInfo.namekey.menuName")
    @DodoShowColumn(sortSeq = 2)
    private String            menuName;

    @DodoField(sortSeq = 2, nameKey = "dodo.privilege.admin.config.MenuInfo.namekey.menuNameKey", listable = false)
    private String            menuNameKey;

    @DodoField(sortSeq = 3, isTextArea = true, nameKey = "dodo.privilege.admin.config.MenuInfo.namekey.menuRemark")
    private String            menuRemark;

    @DodoField(
            sortSeq = 4,
            nameKey = "dodo.privilege.admin.config.MenuInfo.namekey.menuLink",
            showOnField = "menuLevel",
            showOnValue = "LEVEL3",
            isnullable = false)
    private String            menuLink;

    @DodoShowColumn(sortSeq = 0)
    @DodoField(
            sortSeq = 5,
            nameKey = "dodo.privilege.admin.config.MenuInfo.namekey.menuCode",
            addable = false,
            editable = false)
    @DodoValueGenerator
    private String            menuCode;

    @DodoField(
            sortSeq = 6,
            nameKey = "dodo.privilege.admin.config.MenuInfo.namekey.parentMenuInfo",
            isnullable = false,
            queryParams = "ne(\"menuLevel\",DodoMenuLevel.LEVEL3)")
    private MenuInfo          parentMenuInfo;

    @DodoField(
            sortSeq = 7,
            nameKey = "dodo.privilege.admin.config.MenuInfo.namekey.allRights",
            addable = false,
            listable = false,
            editable = false)
    private List<Right>       allRights        = new ArrayList<Right>();

    private List<MenuInfo>    childMenuInfo    = new ArrayList<MenuInfo>();

    @Override
    public void onSave() {
        super.onSave();
        if (menuLevel == DodoMenuLevel.LEVEL1) {
            menuLink = "/framemenu/left_{0}.jhtml";
        } else if (menuLink == null || menuLevel == DodoMenuLevel.LEVEL2) {
            menuLink = "";
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (menuLevel == DodoMenuLevel.LEVEL1) {
            menuLink = "/framemenu/left_{0}.jhtml";
        } else if (menuLink == null || menuLevel == DodoMenuLevel.LEVEL2) {
            menuLink = "";
        }
    }

    @Column(length = 64)
    public String getMenuName() {
        return this.menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    @Column(nullable = true, length = 128)
    public String getMenuRemark() {
        return menuRemark;
    }

    public void setMenuRemark(String menuRemark) {
        this.menuRemark = menuRemark;
    }

    @Column(length = 3)
    @Convert(converter = DodoMenuLevel.Converter.class)
    public DodoMenuLevel getMenuLevel() {
        return menuLevel;
    }

    public void setMenuLevel(DodoMenuLevel menuLevel) {
        this.menuLevel = menuLevel;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public MenuInfo getParentMenuInfo() {
        return parentMenuInfo;
    }

    public void setParentMenuInfo(MenuInfo parentMenuInfo) {
        this.parentMenuInfo = parentMenuInfo;
    }

    @OneToMany(targetEntity = MenuInfo.class, cascade = { CascadeType.ALL }, mappedBy = "parentMenuInfo")
    @Fetch(FetchMode.SUBSELECT)
    @OrderBy("sortSeq asc")
    public List<MenuInfo> getChildMenuInfo() {
        return childMenuInfo;
    }

    public void setChildMenuInfo(List<MenuInfo> childMenuInfo) {
        this.childMenuInfo = childMenuInfo;
    }

    @OneToMany(targetEntity = Right.class, cascade = { CascadeType.ALL }, mappedBy = "menuInfo")
    @Fetch(FetchMode.SUBSELECT)
    @OrderBy("sortSeq asc")
    public List<Right> getAllRights() {
        return allRights;
    }

    public void setAllRights(List<Right> allRights) {
        this.allRights = allRights;
    }

    @Column(nullable = false, length = 128)
    public String getMenuLink() {
        return menuLink;
    }

    public void setMenuLink(String menuLink) {
        this.menuLink = menuLink;
    }

    @Column(nullable = false, length = 8, unique = true)
    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    @Column(length = 128)
    public String getMenuNameKey() {
        return menuNameKey;
    }

    public void setMenuNameKey(String menuNameKey) {
        this.menuNameKey = menuNameKey;
    }

    @Override
    public String toString() {
        return this.menuName;
    }
}