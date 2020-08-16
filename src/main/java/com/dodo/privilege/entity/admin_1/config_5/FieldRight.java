package com.dodo.privilege.entity.admin_1.config_5;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.DynamicInsert;

import com.dodo.common.annotation.action.DodoAction;
import com.dodo.common.annotation.action.DodoEntity;
import com.dodo.common.annotation.field.DodoField;
import com.dodo.common.annotation.field.DodoShowColumn;
import com.dodo.common.annotation.field.DodoValueGenerator;
import com.dodo.common.annotation.menu.DodoMenu;
import com.dodo.common.annotation.tree.DodoTreeRef;
import com.dodo.common.framework.entity.BaseEntity;
import com.dodo.privilege.entity.admin_1.base_1.Role;
import com.dodo.privilege.enums.FieldRightType;

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
        nameKey = "dodo.privilege.admin.config.FieldRight.entityKey",
        actions = { DodoAction.VIEW, DodoAction.EXPORT, DodoAction.CHART },
        levelOne = @DodoMenu(nameKey = "dodo.privilege.admin.menuNameKey", sortSeq = 1),
        levelTwo = @DodoMenu(nameKey = "dodo.privilege.admin.config.menuNameKey", sortSeq = 5),
        levelThree = @DodoMenu(nameKey = "dodo.privilege.admin.config.FieldRight.menuNameKey", sortSeq = 5))
@DodoTreeRef(mapParentField = "field")
public class FieldRight extends BaseEntity implements java.io.Serializable {
    private static final long serialVersionUID = 4222565985045637831L;
    @DodoField(sortSeq = 0, nameKey = "dodo.privilege.admin.config.FieldRight.namekey.fieldRightType")
    @DodoShowColumn(sortSeq = 1)
    private FieldRightType    fieldRightType;

    @DodoField(
            sortSeq = 1,
            nameKey = "dodo.privilege.admin.config.FieldRight.namekey.rightCode",
            addable = false,
            editable = false)
    @DodoValueGenerator
    @DodoShowColumn(sortSeq = 0)
    private String            rightCode;

    private Set<Role>         roles;

    @DodoField(sortSeq = 2, nameKey = "dodo.privilege.admin.config.FieldRight.namekey.field")
    private Field             field;

    @Column(nullable = false, length = 16, unique = true)
    public String getRightCode() {
        return rightCode;
    }

    @ManyToMany(mappedBy = "allFieldRights", fetch = FetchType.LAZY)
    public Set<Role> getRoles() {
        return roles;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Field getField() {
        return field;
    }

    public void setRightCode(String rightCode) {
        this.rightCode = rightCode;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setField(Field field) {
        this.field = field;
    }

    @Column(length = 3)
    @Convert(converter = FieldRightType.Converter.class)
    public FieldRightType getFieldRightType() {
        return fieldRightType;
    }

    public void setFieldRightType(FieldRightType fieldRightType) {
        this.fieldRightType = fieldRightType;
    }
}
