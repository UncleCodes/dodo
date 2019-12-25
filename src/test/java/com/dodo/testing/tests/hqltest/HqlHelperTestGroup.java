package com.dodo.testing.tests.hqltest;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.dodo.common.database.data.Records;
import com.dodo.common.database.hql.HqlHelper;
import com.dodo.common.database.hql.HqlHelper.MatchType;
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
public class HqlHelperTestGroup extends DodoTestBase {

    @Autowired
    private HqlHelperService helperService;

    @Test
    public void testHql() {
        HqlHelper helper = HqlHelper.queryFrom(City.class);
        // 示例一
        // 查询每个省份下的城市数量和最大的区号，返回城市数量 between 2 and 10 的数据
        helper.resetQueryFrom(City.class).join(HqlHelper.currTable, "province", "p")
                .groupByOther("p", "name", "provinceName").count("id", "cityCount").having_between(2L, 10L)
                .max("areaCode", "maxAreaCode");
        Records provinceCityCount = helperService.getRecordsGroup(helper);
        System.err.println("provinceCityCount1 =" + provinceCityCount);

        // 示例二
        // 在示例一的基础上
        //（1）去掉`吉林`省的记录
        //（2）过滤分组结果，只读取最大区号08和09开头的数据
        //（3）按照最大区号升序排列
        //（4）读取3条记录
        helper.resetQueryFrom(City.class).join(HqlHelper.currTable, "province", "p").ne("p", "name", "吉林")
                .groupByOther("p", "name", "provinceName").count("id", "cityCount").having_between(2L, 10L)
                .max("areaCode", "maxAreaCode").having_and().having_like("08", MatchType.START).having_or()
                .having_like("09", MatchType.START).having_end().having_end().group_orderBy(OrderType.asc)
                .setFirstResult(0).setMaxResults(3);
        provinceCityCount = helperService.getRecordsGroup(helper);
        System.err.println("provinceCityCount2 =" + provinceCityCount);
    }
}
