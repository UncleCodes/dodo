package com.example.entity.demo_4.base_1;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.hibernate.annotations.DynamicInsert;

import com.dodo.common.annotation.action.DodoAction;
import com.dodo.common.annotation.action.DodoEntity;
import com.dodo.common.annotation.field.DodoField;
import com.dodo.common.annotation.field.DodoShowColumn;
import com.dodo.common.annotation.menu.DodoMenu;
import com.dodo.common.annotation.menu.DodoMenus;
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
@DodoEntity(name = "单表树", actions = { DodoAction.ALL })
@DodoMenus(levelOne = @DodoMenu(name = "Demo系统", sortSeq = 7), levelTwo = @DodoMenu(name = "基础演示", sortSeq = 1), levelThree = @DodoMenu(name = "单表树演示", sortSeq = 11))
@DodoTreeRef(mapParentField = "parentDemo")
public class SingleTreeDemo extends BaseEntity {

    private static final long serialVersionUID = 5030052771143415896L;

    @DodoField(sortSeq = 0, name = "上级")
    private SingleTreeDemo    parentDemo;

    @DodoShowColumn(sortSeq = 1)
    @DodoField(sortSeq = 1, name = "名称", isnullable = false)
    private String            name;

    @OneToOne
    public SingleTreeDemo getParentDemo() {
        return parentDemo;
    }

    public void setParentDemo(SingleTreeDemo parentDemo) {
        this.parentDemo = parentDemo;
    }

    @Column(length = 16)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
