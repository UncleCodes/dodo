package com.dodo.testing.tests.hqltest;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.dodo.common.database.data.Records;
import com.dodo.common.database.hql.HqlHelper;
import com.dodo.common.framework.bean.pager.PageModel.OrderType;
import com.dodo.common.framework.service.HqlHelperService;
import com.dodo.privilege.entity.admin_1.location_6.City;
import com.dodo.testing.DodoTestBase;

/**
 * HqlHelper 测试
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class HqlHelperTestOrderBy extends DodoTestBase {

    @Autowired
    private HqlHelperService helperService;

    @Test
    public void testHql() {
        // 查询城市名称、序号，按照序号降序排列，读取3条记录
        HqlHelper helper = HqlHelper.queryFrom(City.class);
        helper.fetch("sortSeq", "name").orderBy("sortSeq", OrderType.desc).setFirstResult(0).setMaxResults(3);
        Records cityList = helperService.getRecords(helper, false);
        System.err.println("cityList=" + cityList);
    }
}
