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
import com.dodo.common.annotation.right.DodoButtonLocation;
import com.dodo.common.annotation.right.DodoButtonRight;
import com.dodo.common.annotation.right.DodoButtonRightEvent;
import com.dodo.common.annotation.right.DodoButtonRightModel;
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
@DodoMenu(name = "自定义按钮演示", level = DodoMenuLevel.LEVEL3, sortSeq = 7)
@DodoRight(name = "自定义按钮")
@DodoCodeGenerator(daoGenerator = @DodoDaoGenerator, srvGenerator = @DodoSrvGenerator, actGenerator = @DodoActionGenerator(actions = { DodoAction.ALL }))
@DodoButtonRight(name = "底部AJAX", path = "/bottom/ajax", model = DodoButtonRightModel.MODEL, location = DodoButtonLocation.BOTTOM, event = DodoButtonRightEvent.AJAX)
@DodoButtonRight(name = "顶部AJAX", path = "/top/ajax", model = DodoButtonRightModel.MODEL, location = DodoButtonLocation.TOP, event = DodoButtonRightEvent.AJAX)
@DodoButtonRight(name = "底部URL", path = "https://www.0yi0.com", model = DodoButtonRightModel.MODEL, location = DodoButtonLocation.BOTTOM, event = DodoButtonRightEvent.URL, urlTarget = "_blank")
@DodoButtonRight(name = "顶部URL", path = "https://www.0yi0.com", model = DodoButtonRightModel.MODEL, location = DodoButtonLocation.TOP, event = DodoButtonRightEvent.URL, urlTarget = "_blank")
@DodoButtonRight(name = "行AJAX", path = "/row/ajax", model = DodoButtonRightModel.ROW, event = DodoButtonRightEvent.AJAX)
@DodoButtonRight(name = "后台URL", path = "${rootPath}/special/view.jhtml", model = DodoButtonRightModel.ROW, event = DodoButtonRightEvent.URL, urlTarget = "_blank")
@DodoButtonRight(name = "前台URL", path = "${webHomeUrl}/test.html", model = DodoButtonRightModel.ROW, event = DodoButtonRightEvent.URL, urlTarget = "_blank")
@DodoButtonRight(name = "站外URL", path = "https://www.0yi0.com", model = DodoButtonRightModel.ROW, event = DodoButtonRightEvent.URL, urlTarget = "_blank")
public class ButtonRightDemo extends BaseEntity {

    private static final long serialVersionUID = 5475359959248427119L;

    @DodoField(sortSeq = 4, name = "字段1")
    private String            field1;

    @DodoField(sortSeq = 5, name = "字段2")
    private String            field2;

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
}
