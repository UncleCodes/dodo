package com.example.entity.demo_4.base_1;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.hibernate.annotations.DynamicInsert;

import com.dodo.common.annotation.action.DodoAction;
import com.dodo.common.annotation.action.DodoEntity;
import com.dodo.common.annotation.field.DodoField;
import com.dodo.common.annotation.menu.DodoMenu;
import com.dodo.common.annotation.right.DodoRowRight;
import com.dodo.common.framework.entity.BaseEntity;
import com.dodo.privilege.entity.admin_1.base_1.Admin;

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
@DodoEntity(name = "行级权限(只能操作自己添加的数据)", actions = { DodoAction.ALL }, levelOne = @DodoMenu(name = "Demo系统", sortSeq = 7), levelTwo = @DodoMenu(name = "基础演示", sortSeq = 1), levelThree = @DodoMenu(name = "行级权限演示(1)", sortSeq = 9))
@DodoRowRight(entityProperty = "addBy")
public class RowRightDemo1 extends BaseEntity {

    private static final long serialVersionUID = 2340857123849986982L;

    @DodoField(sortSeq = 1, name = "管理员", isAdmin = true)
    private Admin             addBy;

    @DodoField(sortSeq = 2, name = "其他信息", isnullable = false)
    private String            field1;

    @OneToOne
    public Admin getAddBy() {
        return addBy;
    }

    public void setAddBy(Admin addBy) {
        this.addBy = addBy;
    }

    @Column(length = 8)
    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }
}
