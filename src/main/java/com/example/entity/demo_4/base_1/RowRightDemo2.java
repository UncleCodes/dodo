package com.example.entity.demo_4.base_1;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;

import org.hibernate.annotations.DynamicInsert;

import com.dodo.common.annotation.action.DodoAction;
import com.dodo.common.annotation.action.DodoEntity;
import com.dodo.common.annotation.field.DodoField;
import com.dodo.common.annotation.menu.DodoMenu;
import com.dodo.common.annotation.right.DodoRowRight;
import com.dodo.common.framework.entity.BaseEntity;
import com.example.enums.PersonKind;

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
@DodoEntity(name = "行级权限(使用扩展属性)", actions = { DodoAction.ALL }, levelOne = @DodoMenu(name = "Demo系统", sortSeq = 7), levelTwo = @DodoMenu(name = "基础演示", sortSeq = 1), levelThree = @DodoMenu(name = "行级权限演示(2)", sortSeq = 10))
@DodoRowRight(entityProperty = "personKind", principalKey = "extPersonKind", principalKeyShowName = "行级权限Demo2：人员类别")
public class RowRightDemo2 extends BaseEntity {

    private static final long serialVersionUID = 2340857123849986982L;

    @DodoField(sortSeq = 1, name = "人员类别", isnullable = false, infoTip = "添加数据成功后，需要在角色管理里面，修改扩展属性'行级权限Demo2：人员类别'，来决定某个角色可以操作哪些数据")
    private PersonKind        personKind;

    @DodoField(sortSeq = 2, name = "其他信息", isnullable = false)
    private String            field1;

    @Column(length = 3)
    @Convert(converter = PersonKind.Converter.class)
    public PersonKind getPersonKind() {
        return personKind;
    }

    public void setPersonKind(PersonKind personKind) {
        this.personKind = personKind;
    }

    @Column(length = 8)
    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }
}
