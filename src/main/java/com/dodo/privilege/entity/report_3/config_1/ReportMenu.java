package com.dodo.privilege.entity.report_3.config_1;

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
import com.dodo.common.annotation.field.DodoShowColumn;
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
@DodoMenu(nameKey = "dodo.privilege.report.menuNameKey", level = DodoMenuLevel.LEVEL1, sortSeq = 3)
@DodoMenu(nameKey = "dodo.privilege.report.config.menuNameKey", level = DodoMenuLevel.LEVEL2, sortSeq = 1)
@DodoMenu(nameKey = "dodo.privilege.report.config.ReportMenu.menuNameKey", level = DodoMenuLevel.LEVEL3, sortSeq = 1)
@DodoRight(nameKey = "dodo.privilege.report.config.ReportMenu.entityKey")
@DodoCodeGenerator(daoGenerator = @DodoDaoGenerator, srvGenerator = @DodoSrvGenerator, actGenerator = @DodoActionGenerator(actions = { DodoAction.ALL }))
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
