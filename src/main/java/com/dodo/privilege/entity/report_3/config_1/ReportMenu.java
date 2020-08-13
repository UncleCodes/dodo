package com.dodo.privilege.entity.report_3.config_1;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.annotations.DynamicInsert;

import com.dodo.common.annotation.action.DodoAction;
import com.dodo.common.annotation.action.DodoEntity;
import com.dodo.common.annotation.field.DodoField;
import com.dodo.common.annotation.field.DodoShowColumn;
import com.dodo.common.annotation.menu.DodoMenu;
import com.dodo.common.annotation.menu.DodoMenus;
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
@DodoEntity(nameKey = "dodo.privilege.report.config.ReportMenu.entityKey", actions = { DodoAction.ALL })
@DodoMenus(levelOne = @DodoMenu(nameKey = "dodo.privilege.report.menuNameKey", sortSeq = 3), levelTwo = @DodoMenu(nameKey = "dodo.privilege.report.config.menuNameKey", sortSeq = 1), levelThree = @DodoMenu(nameKey = "dodo.privilege.report.config.ReportMenu.menuNameKey", sortSeq = 1))
public class ReportMenu extends BaseEntity {
    private static final long serialVersionUID = 5267212173675296962L;

    @DodoField(sortSeq = 2, nameKey = "dodo.privilege.report.config.ReportMenu.namekey.menuName", isnullable = false, maxLength = 64, queryOnList = true)
    @DodoShowColumn(sortSeq = 1)
    private String            menuName;

    @Column(length = 32)
    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

}
