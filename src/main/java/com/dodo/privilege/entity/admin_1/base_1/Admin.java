package com.dodo.privilege.entity.admin_1.base_1;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.core.GrantedAuthority;

import com.dodo.common.annotation.action.DodoAction;
import com.dodo.common.annotation.action.DodoEntity;
import com.dodo.common.annotation.field.DodoField;
import com.dodo.common.annotation.field.DodoShowColumn;
import com.dodo.common.annotation.field.DodoViewGroup;
import com.dodo.common.annotation.menu.DodoMenu;
import com.dodo.common.annotation.right.DodoRowRight;
import com.dodo.common.framework.bean.tree.DodoTree;
import com.dodo.common.framework.entity.BaseEntity;
import com.dodo.security.DodoUserDetails;

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
@DodoEntity(nameKey = "dodo.privilege.admin.base.Admin.entityKey", actions = { DodoAction.ALL }, levelOne = @DodoMenu(nameKey = "dodo.privilege.admin.menuNameKey", sortSeq = 1), levelTwo = @DodoMenu(nameKey = "dodo.privilege.admin.base.menuNameKey", sortSeq = 1), levelThree = @DodoMenu(nameKey = "dodo.privilege.admin.base.Admin.menuNameKey", sortSeq = 1))
@DodoRowRight(entityProperty = "addBy")
public class Admin extends BaseEntity implements DodoUserDetails {
    private static final long            serialVersionUID = -7519486823153844426L;

    @DodoField(nameKey = "dodo.privilege.admin.base.Admin.namekey.addBy", sortSeq = -1, isAdmin = true, queryOnList = true)
    @DodoViewGroup(groupSeq = 0, groupNameKey = "dodo.privilege.admin.base.Admin.groupName.0")
    private Admin                        addBy;

    @DodoShowColumn(sortSeq = 0)
    @DodoField(nameKey = "dodo.privilege.admin.base.Admin.namekey.username", isnullable = false, sortSeq = 0, editable = false, isRemoteCheck = true, queryOnList = true)
    @DodoViewGroup(groupSeq = 0, groupNameKey = "dodo.privilege.admin.base.Admin.groupName.0")
    private String                       username;

    @DodoField(nameKey = "dodo.privilege.admin.base.Admin.namekey.adminPassword", sortSeq = 1, listable = false, ispassword = true, infoTipKey = "dodo.privilege.admin.base.Admin.infoTip.adminPassword")
    @DodoViewGroup(groupSeq = 0, groupNameKey = "dodo.privilege.admin.base.Admin.groupName.0")
    private String                       adminPassword;

    @DodoField(nameKey = "dodo.privilege.admin.base.Admin.namekey.email", sortSeq = 2, isEmail = true)
    @DodoViewGroup(groupSeq = 0, groupNameKey = "dodo.privilege.admin.base.Admin.groupName.0")
    private String                       email;

    @DodoField(nameKey = "dodo.privilege.admin.base.Admin.namekey.mobile", sortSeq = 2, isMobile = true)
    @DodoViewGroup(groupSeq = 0, groupNameKey = "dodo.privilege.admin.base.Admin.groupName.0")
    private String                       mobile;

    @DodoShowColumn(sortSeq = 1)
    @DodoField(nameKey = "dodo.privilege.admin.base.Admin.namekey.name", sortSeq = 3)
    @DodoViewGroup(groupSeq = 0, groupNameKey = "dodo.privilege.admin.base.Admin.groupName.0")
    private String                       name;

    @DodoField(nameKey = "dodo.privilege.admin.base.Admin.namekey.department", sortSeq = 4, queryOnList = true)
    @DodoViewGroup(groupSeq = 1, groupNameKey = "dodo.privilege.admin.base.Admin.groupName.1")
    private String                       department;

    @DodoField(nameKey = "dodo.privilege.admin.base.Admin.namekey.isAccountEnabled", sortSeq = 5)
    @DodoViewGroup(groupSeq = 2, groupNameKey = "dodo.privilege.admin.base.Admin.groupName.2")
    private Boolean                      isAccountEnabled;

    @DodoField(nameKey = "dodo.privilege.admin.base.Admin.namekey.isAccountLocked", sortSeq = 6, addable = false)
    @DodoViewGroup(groupSeq = 2, groupNameKey = "dodo.privilege.admin.base.Admin.groupName.2")
    private Boolean                      isAccountLocked;

    @DodoField(nameKey = "dodo.privilege.admin.base.Admin.namekey.isAccountExpired", sortSeq = 7, addable = false)
    @DodoViewGroup(groupSeq = 2, groupNameKey = "dodo.privilege.admin.base.Admin.groupName.2")
    private Boolean                      isAccountExpired;

    @DodoField(nameKey = "dodo.privilege.admin.base.Admin.namekey.isCredentialsExpired", sortSeq = 8, addable = false)
    @DodoViewGroup(groupSeq = 2, groupNameKey = "dodo.privilege.admin.base.Admin.groupName.2")
    private Boolean                      isCredentialsExpired;

    @DodoField(nameKey = "dodo.privilege.admin.base.Admin.namekey.loginFailureCount", sortSeq = 9, addable = false, editable = false)
    @DodoViewGroup(groupSeq = 2, groupNameKey = "dodo.privilege.admin.base.Admin.groupName.2")
    private Integer                      loginFailureCount;

    @DodoField(nameKey = "dodo.privilege.admin.base.Admin.namekey.lockedDate", sortSeq = 10, addable = false, editable = false)
    @DodoViewGroup(groupSeq = 2, groupNameKey = "dodo.privilege.admin.base.Admin.groupName.2")
    private Date                         lockedDate;

    @DodoField(nameKey = "dodo.privilege.admin.base.Admin.namekey.loginDate", sortSeq = 11, addable = false, editable = false)
    @DodoViewGroup(groupSeq = 3, groupNameKey = "dodo.privilege.admin.base.Admin.groupName.3")
    private Date                         loginDate;

    @DodoField(nameKey = "dodo.privilege.admin.base.Admin.namekey.loginIp", sortSeq = 12, addable = false, editable = false)
    @DodoViewGroup(groupSeq = 3, groupNameKey = "dodo.privilege.admin.base.Admin.groupName.3")
    private String                       loginIp;

    @DodoField(nameKey = "dodo.privilege.admin.base.Admin.namekey.browserType", sortSeq = 13, addable = false, editable = false)
    @DodoViewGroup(groupSeq = 3, groupNameKey = "dodo.privilege.admin.base.Admin.groupName.3")
    private String                       browserType;

    @DodoField(nameKey = "dodo.privilege.admin.base.Admin.namekey.roleSet", isnullable = false, sortSeq = 14, isPopup = true)
    @DodoViewGroup(groupSeq = 4, groupNameKey = "dodo.privilege.admin.base.Admin.groupName.4")
    private Set<Role>                    roleSet          = new HashSet<Role>();
    private Collection<GrantedAuthority> authorities;
    private DodoTree                     menuInfoTree;

    private Set<String>                  fieldRightHaveCode;
    private Set<String>                  rightHaveCode;
    private Boolean                      isSystemAdmin;

    @Column(length = 64)
    public String getBrowserType() {
        return browserType;
    }

    public void setBrowserType(String browserType) {
        this.browserType = browserType;
    }

    @Column(length = 64)
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String paramString) {
        this.username = paramString;
    }

    @Override
    @Transient
    public String getPassword() {
        return adminPassword;
    }

    @Column(nullable = false, length = 128)
    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    @Column(nullable = false, length = 64)
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String paramString) {
        this.email = paramString;
    }

    @Column(length = 16)
    public String getName() {
        return this.name;
    }

    public void setName(String paramString) {
        this.name = paramString;
    }

    @Column(length = 32)
    public String getDepartment() {
        return this.department;
    }

    public void setDepartment(String paramString) {
        this.department = paramString;
    }

    @Column(nullable = false)
    public Boolean getIsAccountEnabled() {
        return this.isAccountEnabled;
    }

    public void setIsAccountEnabled(Boolean paramBoolean) {
        this.isAccountEnabled = paramBoolean;
    }

    @Column(nullable = false)
    public Boolean getIsAccountLocked() {
        return this.isAccountLocked;
    }

    public void setIsAccountLocked(Boolean paramBoolean) {
        this.isAccountLocked = paramBoolean;
    }

    @Column(nullable = false)
    public Boolean getIsAccountExpired() {
        return this.isAccountExpired;
    }

    public void setIsAccountExpired(Boolean paramBoolean) {
        this.isAccountExpired = paramBoolean;
    }

    @Column(nullable = false)
    public Boolean getIsCredentialsExpired() {
        return this.isCredentialsExpired;
    }

    public void setIsCredentialsExpired(Boolean paramBoolean) {
        this.isCredentialsExpired = paramBoolean;
    }

    @Column(nullable = false)
    public Integer getLoginFailureCount() {
        return this.loginFailureCount;
    }

    public void setLoginFailureCount(Integer paramInteger) {
        this.loginFailureCount = paramInteger;
    }

    public Date getLockedDate() {
        return this.lockedDate;
    }

    public void setLockedDate(Date paramDate) {
        this.lockedDate = paramDate;
    }

    public Date getLoginDate() {
        return this.loginDate;
    }

    public void setLoginDate(Date paramDate) {
        this.loginDate = paramDate;
    }

    @Column(length = 128)
    public String getLoginIp() {
        return this.loginIp;
    }

    public void setLoginIp(String paramString) {
        this.loginIp = paramString;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @OrderBy("sortSeq asc")
    public Set<Role> getRoleSet() {
        return this.roleSet;
    }

    public void setRoleSet(Set<Role> paramSet) {
        this.roleSet = paramSet;
    }

    public void onSave() {
        super.onSave();
        if (this.isAccountEnabled == null) {
            this.isAccountEnabled = Boolean.valueOf(true);
        }
        if (this.isAccountLocked == null) {
            this.isAccountLocked = Boolean.valueOf(false);
        }
        if (this.isAccountExpired == null) {
            this.isAccountExpired = Boolean.valueOf(false);
        }
        if (this.isCredentialsExpired == null) {
            this.isCredentialsExpired = Boolean.valueOf(false);
        }
        if ((this.loginFailureCount == null) || (this.loginFailureCount.intValue() < 0)) {
            this.loginFailureCount = Integer.valueOf(0);
        }
        if (department == null) {
            department = "admin.Dept";
        }
    }

    public void onUpdate() {
        super.onUpdate();
        if (this.isAccountEnabled == null) {
            this.isAccountEnabled = Boolean.valueOf(true);
        }
        if (this.isAccountLocked == null) {
            this.isAccountLocked = Boolean.valueOf(false);
        }
        if (this.isAccountExpired == null) {
            this.isAccountExpired = Boolean.valueOf(false);
        }
        if (this.isCredentialsExpired == null) {
            this.isCredentialsExpired = Boolean.valueOf(false);
        }
        if ((this.loginFailureCount == null) || (this.loginFailureCount.intValue() < 0)) {
            this.loginFailureCount = Integer.valueOf(0);
        }
    }

    @Transient
    public Collection<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(Collection<GrantedAuthority> paramArrayOfGrantedAuthority) {
        this.authorities = paramArrayOfGrantedAuthority;
    }

    @Transient
    public boolean isEnabled() {
        return this.isAccountEnabled.booleanValue();
    }

    @Transient
    public boolean isAccountNonLocked() {
        return !this.isAccountLocked.booleanValue();
    }

    @Transient
    public boolean isAccountNonExpired() {
        return !this.isAccountExpired.booleanValue();
    }

    @Transient
    public boolean isCredentialsNonExpired() {
        return !this.isCredentialsExpired.booleanValue();
    }

    @Transient
    public boolean hasFieldRight(String fieldRightCode) {
        return fieldRightCode != null && fieldRightHaveCode.contains(fieldRightCode);
    }

    @Transient
    public boolean hasRight(String rightCode) {
        return rightCode != null && rightHaveCode.contains(rightCode);
    }

    @Transient
    public Set<String> getRightHaveCode() {
        return rightHaveCode;
    }

    public void setRightHaveCode(Set<String> rightHaveCode) {
        this.rightHaveCode = rightHaveCode;
    }

    @Transient
    public Set<String> getFieldRightHaveCode() {
        return fieldRightHaveCode;
    }

    public void setFieldRightHaveCode(Set<String> fieldRightHaveCode) {
        this.fieldRightHaveCode = fieldRightHaveCode;
    }

    @Transient
    public DodoTree getMenuInfoTree() {
        return menuInfoTree;
    }

    public void setMenuInfoTree(DodoTree menuInfoTree) {
        this.menuInfoTree = menuInfoTree;
    }

    @Override
    public int hashCode() {
        return this.username.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        Admin other = (Admin) obj;
        return this.username.equals(other.getUsername());
    }

    @Transient
    public Boolean getIsSystemAdmin() {
        return isSystemAdmin;
    }

    public void setIsSystemAdmin(Boolean isSystemAdmin) {
        this.isSystemAdmin = isSystemAdmin;
    }

    @Column(length = 16)
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @OneToOne
    public Admin getAddBy() {
        return addBy;
    }

    public void setAddBy(Admin addBy) {
        this.addBy = addBy;
    }
}
