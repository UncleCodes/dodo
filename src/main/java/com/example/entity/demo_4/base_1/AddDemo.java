package com.example.entity.demo_4.base_1;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;

import com.dodo.common.annotation.DodoCodeGenerator;
import com.dodo.common.annotation.action.DodoAction;
import com.dodo.common.annotation.action.DodoActionGenerator;
import com.dodo.common.annotation.dao.DodoDaoGenerator;
import com.dodo.common.annotation.field.DodoField;
import com.dodo.common.annotation.menu.DodoMenu;
import com.dodo.common.annotation.menu.DodoMenuLevel;
import com.dodo.common.annotation.right.DodoRight;
import com.dodo.common.annotation.service.DodoSrvGenerator;
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
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DodoMenu(name = "Demo系统", level = DodoMenuLevel.LEVEL1, sortSeq = 7)
@DodoMenu(name = "基础演示", level = DodoMenuLevel.LEVEL2, sortSeq = 1)
@DodoMenu(name = "添加和修改演示", level = DodoMenuLevel.LEVEL3, sortSeq = 4)
@DodoRight(name = "添加和修改")
@DodoCodeGenerator(daoGenerator = @DodoDaoGenerator, srvGenerator = @DodoSrvGenerator, actGenerator = @DodoActionGenerator(actions = { DodoAction.ALL }))
public class AddDemo extends BaseEntity {

    private static final long serialVersionUID = 5173398927310922869L;

    // 该字段只可编辑，不能添加
    @DodoField(sortSeq = 1, name = "字段1(不可添加)", addable = false)
    private String            field1;

    // 该字段只可添加，不能编辑
    @DodoField(sortSeq = 2, name = "字段2(不可编辑)", editable = false)
    private String            field2;

    // 以下字段即可以修改、也可以添加
    @DodoField(sortSeq = 3, name = "字段3")
    private String            field3;

    @DodoField(sortSeq = 4, name = "字段4")
    private String            field4;

    @DodoField(sortSeq = 5, name = "字段5")
    private String            field5;

    @Column(length = 8)
    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    @Column(length = 8)
    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    @Column(length = 8)
    public String getField3() {
        return field3;
    }

    public void setField3(String field3) {
        this.field3 = field3;
    }

    @Column(length = 8)
    public String getField4() {
        return field4;
    }

    public void setField4(String field4) {
        this.field4 = field4;
    }

    @Column(length = 8)
    public String getField5() {
        return field5;
    }

    public void setField5(String field5) {
        this.field5 = field5;
    }
}
