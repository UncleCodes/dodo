package com.dodo.privilege.entity.admin_1.config_5;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.DynamicInsert;

import com.dodo.common.annotation.action.DodoAction;
import com.dodo.common.annotation.action.DodoEntity;
import com.dodo.common.annotation.field.DodoField;
import com.dodo.common.annotation.field.DodoShowColumn;
import com.dodo.common.annotation.field.DodoValueGenerator;
import com.dodo.common.annotation.menu.DodoMenu;
import com.dodo.common.annotation.menu.DodoMenus;
import com.dodo.common.annotation.tree.DodoTreeRef;
import com.dodo.common.framework.entity.BaseEntity;
import com.dodo.privilege.entity.admin_1.base_1.Role;

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
@DodoEntity(nameKey = "dodo.privilege.admin.config.Right.entityKey", actions = { DodoAction.ALL })
@DodoMenus(levelOne = @DodoMenu(nameKey = "dodo.privilege.admin.menuNameKey", sortSeq = 1), levelTwo = @DodoMenu(nameKey = "dodo.privilege.admin.config.menuNameKey", sortSeq = 5), levelThree = @DodoMenu(nameKey = "dodo.privilege.admin.config.Right.menuNameKey", sortSeq = 3))
@DodoTreeRef(mapParentField = "menuInfo", selfQueryParams = "isNull(\"managerRight\")")
public class Right extends BaseEntity implements java.io.Serializable {
    private static final long serialVersionUID = -2048846785994031276L;

    @DodoField(sortSeq = -1, nameKey = "dodo.privilege.admin.config.Right.namekey.entityName", addable = false, editable = false)
    @DodoShowColumn(sortSeq = 0)
    private String            entityName;

    @DodoField(sortSeq = 0, nameKey = "dodo.privilege.admin.config.Right.namekey.entityKey", listable = false, addable = false, editable = false)
    private String            entityKey;

    @DodoField(sortSeq = 1, nameKey = "dodo.privilege.admin.config.Right.namekey.rightName")
    @DodoShowColumn(sortSeq = 1)
    private String            rightName;

    @DodoField(sortSeq = 2, nameKey = "dodo.privilege.admin.config.Right.namekey.rightKey", listable = false)
    private String            rightKey;

    @DodoField(sortSeq = 3, nameKey = "dodo.privilege.admin.config.Right.namekey.isSystem", addable = false, editable = false)
    private Boolean           isSystem;

    @DodoField(sortSeq = 4, nameKey = "dodo.privilege.admin.config.Right.namekey.managerRight", addable = false, editable = false)
    private Right             managerRight;

    @DodoField(sortSeq = 5, nameKey = "dodo.privilege.admin.config.Right.namekey.rightCode", addable = false, editable = false)
    @DodoValueGenerator
    @DodoShowColumn(sortSeq = 0)
    private String            rightCode;

    @DodoField(sortSeq = 6, nameKey = "dodo.privilege.admin.config.Right.namekey.rightRemark", isTextArea = true)
    private String            rightRemark;

    @DodoField(sortSeq = 7, nameKey = "dodo.privilege.admin.config.Right.namekey.menuInfo", isPopup = true, isnullable = false, queryParams = "eq(\"menuLevel\",DodoMenuLevel.LEVEL3)")
    private MenuInfo          menuInfo;

    @DodoField(sortSeq = 8, nameKey = "dodo.privilege.admin.config.Right.namekey.rightLink", isRemoteCheck = true)
    private String            rightLink;

    @DodoField(sortSeq = 9, nameKey = "dodo.privilege.admin.config.Right.namekey.className", addable = false, editable = false)
    private String            className;

    private Set<Role>         roles;

    @Override
    public void onSave() {
        super.onSave();
        if (isSystem == null) {
            isSystem = Boolean.FALSE;
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (isSystem == null) {
            isSystem = Boolean.FALSE;
        }
    }

    @Column(length = 128)
    public String getRightName() {
        return this.rightName;
    }

    public void setRightName(String rightName) {
        this.rightName = rightName;
    }

    @Column(nullable = false, length = 8, unique = true)
    public String getRightCode() {
        return this.rightCode;
    }

    public void setRightCode(String rightCode) {
        this.rightCode = rightCode;
    }

    @Column(length = 128)
    public String getRightRemark() {
        return rightRemark;
    }

    public void setRightRemark(String rightRemark) {
        this.rightRemark = rightRemark;
    }

    @ManyToMany(mappedBy = "allRights", fetch = FetchType.LAZY)
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    public MenuInfo getMenuInfo() {
        return menuInfo;
    }

    public void setMenuInfo(MenuInfo menuInfo) {
        this.menuInfo = menuInfo;
    }

    @Column(length = 128)
    public String getRightLink() {
        return rightLink;
    }

    public void setRightLink(String rightLink) {
        this.rightLink = rightLink;
    }

    @Override
    public String toString() {
        return this.rightName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Column(length = 64)
    public String getEntityName() {
        return entityName;
    }

    @Column(length = 128)
    public String getEntityKey() {
        return entityKey;
    }

    @Column(length = 128)
    public String getRightKey() {
        return rightKey;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public void setEntityKey(String entityKey) {
        this.entityKey = entityKey;
    }

    public void setRightKey(String rightKey) {
        this.rightKey = rightKey;
    }

    public Boolean getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(Boolean isSystem) {
        this.isSystem = isSystem;
    }

    @OneToOne
    public Right getManagerRight() {
        return managerRight;
    }

    public void setManagerRight(Right managerRight) {
        this.managerRight = managerRight;
    }

}