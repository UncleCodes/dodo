package com.dodo.privilege.entity.admin_1.config_5;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;

import com.dodo.common.annotation.DodoCodeGenerator;
import com.dodo.common.annotation.action.DodoAction;
import com.dodo.common.annotation.action.DodoActionGenerator;
import com.dodo.common.annotation.dao.DodoDaoGenerator;
import com.dodo.common.annotation.field.DodoField;
import com.dodo.common.annotation.field.DodoShowColumn;
import com.dodo.common.annotation.field.DodoValueGenerator;
import com.dodo.common.annotation.menu.DodoMenu;
import com.dodo.common.annotation.menu.DodoMenuLevel;
import com.dodo.common.annotation.right.DodoRight;
import com.dodo.common.annotation.service.DodoSrvGenerator;
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
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DodoMenu(nameKey = "dodo.privilege.admin.menuNameKey", level = DodoMenuLevel.LEVEL1, sortSeq = 1)
@DodoMenu(nameKey = "dodo.privilege.admin.config.menuNameKey", level = DodoMenuLevel.LEVEL2, sortSeq = 5)
@DodoMenu(nameKey = "dodo.privilege.admin.config.FieldRight.menuNameKey", level = DodoMenuLevel.LEVEL3, sortSeq = 5)
@DodoRight(nameKey = "dodo.privilege.admin.config.FieldRight.entityKey")
@DodoCodeGenerator(daoGenerator = @DodoDaoGenerator, srvGenerator = @DodoSrvGenerator, actGenerator = @DodoActionGenerator(actions = {
        DodoAction.VIEW, DodoAction.EXPORT, DodoAction.CHART }))
@DodoTreeRef(mapParentField = "field")
public class FieldRight extends BaseEntity implements java.io.Serializable {
    private static final long serialVersionUID = 4222565985045637831L;
    @DodoField(sortSeq = 0, nameKey = "dodo.privilege.admin.config.FieldRight.namekey.fieldRightType")
    @DodoShowColumn(sortSeq = 1)
    private FieldRightType    fieldRightType;

    @DodoField(sortSeq = 1, nameKey = "dodo.privilege.admin.config.FieldRight.namekey.rightCode", addable = false, editable = false)
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
