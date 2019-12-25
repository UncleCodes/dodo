package com.dodo.testing.tests.hqltest;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.dodo.common.database.data.Records;
import com.dodo.common.database.hql.HqlHelper;
import com.dodo.common.framework.service.HqlHelperService;
import com.dodo.privilege.entity.admin_1.location_6.City;
import com.dodo.testing.DodoTestBase;

/**
 * HqlHelper 条件示例
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class HqlHelperConditionTest extends DodoTestBase {

    @Autowired
    private HqlHelperService helperService;

    @Test
    public void conditionGroup() {
        // 查询sortSeq>98，并且名称包含`天`或者`庆`字的城市  查询2条记录
        HqlHelper helper = HqlHelper.queryFrom(City.class);
        helper.fetch("id", "name").gt("sortSeq", 98).and().like("name", "天").or().like("name", "庆").end().end()
                .setFirstResult(0).setMaxResults(2);
        Records tempRecords = helperService.getRecords(helper, false);
        System.err.println(tempRecords);
    }
}
