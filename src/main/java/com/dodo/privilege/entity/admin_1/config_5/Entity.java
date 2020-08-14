package com.dodo.privilege.entity.admin_1.config_5;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.dodo.common.annotation.action.DodoAction;
import com.dodo.common.annotation.action.DodoEntity;
import com.dodo.common.annotation.field.DodoField;
import com.dodo.common.annotation.field.DodoShowColumn;
import com.dodo.common.annotation.menu.DodoMenu;
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
@javax.persistence.Entity
@DynamicInsert
@DodoEntity(nameKey = "dodo.privilege.admin.config.Entity.entityKey", actions = { DodoAction.VIEW, DodoAction.CHART,
        DodoAction.EXPORT }, levelOne = @DodoMenu(nameKey = "dodo.privilege.admin.menuNameKey", sortSeq = 1), levelTwo = @DodoMenu(nameKey = "dodo.privilege.admin.config.menuNameKey", sortSeq = 5), levelThree = @DodoMenu(nameKey = "dodo.privilege.admin.config.Entity.menuNameKey", sortSeq = 2))
@DodoTreeRef(mapParentField = "menuInfo")
public class Entity extends BaseEntity implements java.io.Serializable {
    private static final long serialVersionUID = -1772991102203306349L;
    @DodoField(sortSeq = -1, nameKey = "dodo.privilege.admin.config.Entity.namekey.className")
    private String            className;

    @DodoField(sortSeq = 0, nameKey = "dodo.privilege.admin.config.Entity.namekey.name")
    @DodoShowColumn(sortSeq = 0)
    private String            name;

    @DodoField(sortSeq = 1, nameKey = "dodo.privilege.admin.config.Entity.namekey.showKey", listable = false)
    private String            showKey;

    @DodoField(sortSeq = 2, nameKey = "dodo.privilege.admin.config.Entity.namekey.menuInfoPath")
    private String            menuInfoPath;

    @DodoField(sortSeq = 3, nameKey = "dodo.privilege.admin.config.Entity.namekey.basePath")
    private String            basePath;

    @DodoField(sortSeq = 4, nameKey = "dodo.privilege.admin.config.Entity.namekey.daoName")
    private String            daoName;

    @DodoField(sortSeq = 5, nameKey = "dodo.privilege.admin.config.Entity.namekey.serviceName")
    private String            serviceName;

    @DodoField(sortSeq = 6, nameKey = "dodo.privilege.admin.config.Entity.namekey.actionName")
    private String            actionName;

    @DodoField(sortSeq = 7, nameKey = "dodo.privilege.admin.config.Entity.namekey.actionType")
    private String            actionType;

    @DodoField(sortSeq = 8, nameKey = "dodo.privilege.admin.config.Entity.namekey.wholeTreeMethod")
    private String            wholeTreeMethod;

    @DodoField(sortSeq = 9, nameKey = "dodo.privilege.admin.config.Entity.namekey.allFields", listable = false, editable = false)
    private Set<Field>        allFields;

    @DodoField(sortSeq = 10, nameKey = "dodo.privilege.admin.config.Entity.namekey.menuInfo")
    private MenuInfo          menuInfo;

    @DodoField(sortSeq = 11, nameKey = "dodo.privilege.admin.config.Entity.namekey.isModelExtend")
    private Boolean           isModelExtend;

    public String getClassName() {
        return className;
    }

    @Column(length = 64)
    public String getName() {
        return name;
    }

    @Column(length = 128)
    public String getBasePath() {
        return basePath;
    }

    public String getDaoName() {
        return daoName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getActionName() {
        return actionName;
    }

    @Column(length = 512)
    public String getActionType() {
        return actionType;
    }

    @Column(length = 64)
    public String getWholeTreeMethod() {
        return wholeTreeMethod;
    }

    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = "entity")
    @Fetch(FetchMode.SUBSELECT)
    @OrderBy("sortSeq asc")
    public Set<Field> getAllFields() {
        return allFields;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public void setDaoName(String daoName) {
        this.daoName = daoName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public void setWholeTreeMethod(String wholeTreeMethod) {
        this.wholeTreeMethod = wholeTreeMethod;
    }

    public void setAllFields(Set<Field> allFields) {
        this.allFields = allFields;
    }

    @Column(length = 128)
    public String getMenuInfoPath() {
        return menuInfoPath;
    }

    @OneToOne(fetch = FetchType.LAZY)
    public MenuInfo getMenuInfo() {
        return menuInfo;
    }

    public void setMenuInfoPath(String menuInfoPath) {
        this.menuInfoPath = menuInfoPath;
    }

    public void setMenuInfo(MenuInfo menuInfo) {
        this.menuInfo = menuInfo;
    }

    @Column(length = 128)
    public String getShowKey() {
        return showKey;
    }

    public void setShowKey(String showKey) {
        this.showKey = showKey;
    }

    public Boolean getIsModelExtend() {
        return isModelExtend;
    }

    public void setIsModelExtend(Boolean isModelExtend) {
        this.isModelExtend = isModelExtend;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
