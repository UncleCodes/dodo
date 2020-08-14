package com.example.entity.demo_4.base_1;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.annotations.DynamicInsert;

import com.dodo.common.annotation.action.DodoAction;
import com.dodo.common.annotation.action.DodoEntity;
import com.dodo.common.annotation.field.DodoField;
import com.dodo.common.annotation.menu.DodoMenu;
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
// actions = { DodoAction.ALL } = 开启全部功能生成
@DodoEntity(name = "全部Action", actions = { DodoAction.ALL }, levelOne = @DodoMenu(name = "Demo系统", sortSeq = 7), levelTwo = @DodoMenu(name = "基础演示", sortSeq = 1), levelThree = @DodoMenu(name = "全部Action演示", sortSeq = 1))
public class ActionDemoAll extends BaseEntity {

    private static final long serialVersionUID = 7953109081252074508L;
    @DodoField(sortSeq = 5, name = "字段5")
    private String            field5;

    @Column(length = 8)
    public String getField5() {
        return field5;
    }

    public void setField5(String field5) {
        this.field5 = field5;
    }
}
