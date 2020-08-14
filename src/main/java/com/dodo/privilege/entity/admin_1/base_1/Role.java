package com.dodo.privilege.entity.admin_1.base_1;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SortNatural;

import com.dodo.common.annotation.action.DodoAction;
import com.dodo.common.annotation.action.DodoEntity;
import com.dodo.common.annotation.field.DodoField;
import com.dodo.common.annotation.field.DodoShowColumn;
import com.dodo.common.annotation.menu.DodoMenu;
import com.dodo.common.annotation.right.DodoRowRight;
import com.dodo.common.framework.entity.BaseEntity;
import com.dodo.privilege.entity.admin_1.config_5.FieldRight;
import com.dodo.privilege.entity.admin_1.config_5.Right;

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
@DodoEntity(nameKey = "dodo.privilege.admin.base.Role.entityKey", actions = { DodoAction.ALL }, levelOne = @DodoMenu(nameKey = "dodo.privilege.admin.menuNameKey", sortSeq = 1), levelTwo = @DodoMenu(nameKey = "dodo.privilege.admin.base.menuNameKey", sortSeq = 1), levelThree = @DodoMenu(nameKey = "dodo.privilege.admin.base.Role.menuNameKey", sortSeq = 2))
@DodoRowRight(entityProperty = "admin")
public class Role extends BaseEntity {
    private static final long   serialVersionUID = -6614052029623997372L;

    @DodoField(nameKey = "dodo.privilege.admin.base.Role.namekey.admin", sortSeq = 0, isAdmin = true)
    private Admin               admin;

    @DodoShowColumn(sortSeq = 0)
    @DodoField(nameKey = "dodo.privilege.admin.base.Role.namekey.name", sortSeq = 1, queryOnList = true, isRemoteCheck = true)
    private String              name;

    @DodoField(nameKey = "dodo.privilege.admin.base.Role.namekey.isSystem", sortSeq = 2, addable = false, editable = false)
    @DodoShowColumn(sortSeq = 1)
    private Boolean             isSystem;

    @DodoField(nameKey = "dodo.privilege.admin.base.Role.namekey.description", sortSeq = 4, listable = false, isTextArea = true)
    private String              description;

    @DodoField(nameKey = "dodo.privilege.admin.base.Role.namekey.allRights", sortSeq = 5, isnullable = false, listable = false)
    private Set<Right>          allRights;

    @DodoField(nameKey = "dodo.privilege.admin.base.Role.namekey.allFieldRights", sortSeq = 6, listable = false)
    private Set<FieldRight>     allFieldRights;

    private Set<Admin>          adminSet         = new HashSet<Admin>();

    @DodoField(nameKey = "dodo.privilege.admin.base.Role.namekey.rowrightConfiger", sortSeq = 7)
    private Map<String, String> rowrightConfiger;

    @ElementCollection
    @MapKeyColumn(name = "principal_key", nullable = false, length = 30)
    @Column(name = "rowright_value", nullable = false, length = 1024)
    @SortNatural
    public Map<String, String> getRowrightConfiger() {
        return rowrightConfiger;
    }

    public void setRowrightConfiger(Map<String, String> rowrightConfiger) {
        this.rowrightConfiger = rowrightConfiger;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @OrderBy("sortSeq asc")
    public Set<FieldRight> getAllFieldRights() {
        return allFieldRights;
    }

    public void setAllFieldRights(Set<FieldRight> allFieldRights) {
        this.allFieldRights = allFieldRights;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @OrderBy("sortSeq asc")
    public Set<Right> getAllRights() {
        return allRights;
    }

    public void setAllRights(Set<Right> allRights) {
        this.allRights = allRights;
    }

    @Column(nullable = false, length = 32)
    public String getName() {
        return this.name;
    }

    public void setName(String paramString) {
        this.name = paramString;
    }

    @Column(nullable = false)
    public Boolean getIsSystem() {
        return this.isSystem;
    }

    public void setIsSystem(Boolean paramBoolean) {
        this.isSystem = paramBoolean;
    }

    @Column(length = 128)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String paramString) {
        this.description = paramString;
    }

    @ManyToMany(mappedBy = "roleSet", fetch = FetchType.LAZY)
    public Set<Admin> getAdminSet() {
        return this.adminSet;
    }

    public void setAdminSet(Set<Admin> paramSet) {
        this.adminSet = paramSet;
    }

    @Transient
    public void onSave() {
        super.onSave();
        if (this.isSystem == null) {
            this.isSystem = Boolean.FALSE;
        }
    }

    @Transient
    public void onUpdate() {
        super.onUpdate();
        if (this.isSystem == null) {
            this.isSystem = Boolean.FALSE;
        }
    }

    @OneToOne(fetch = FetchType.LAZY)
    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public String toString() {
        return this.name;
    }

}
