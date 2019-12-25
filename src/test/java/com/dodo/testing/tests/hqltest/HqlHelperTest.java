package com.dodo.testing.tests.hqltest;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.dodo.common.database.data.Record;
import com.dodo.common.database.data.Records;
import com.dodo.common.database.hql.HqlHelper;
import com.dodo.common.database.hql.HqlHelper.MatchType;
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
public class HqlHelperTest extends DodoTestBase {

    @Autowired
    private HqlHelperService helperService;

    @Test
    public void testHql() {
        // 一个基本的查询例子 - 查询单个
        // 查询id=1160799039167057920 的城市名称以及归属的省份名称
        HqlHelper helper = HqlHelper.queryFrom(City.class);
        helper.fetch("name").join(HqlHelper.currTable, "province", "p").fetchOther("p", "name", "provinceName")
                .eq("id", "1160799039167057920");
        Record oneCity = helperService.getRecord(helper);
        System.err.println("oneCity=" + oneCity);

        // 一个基本的查询例子 - 查询列表
        // 查询所有城市名称以及归属的省份名称，返回2条记录
        helper.resetQueryFrom(City.class).fetch("name").join(HqlHelper.currTable, "province", "p")
                .fetchOther("p", "name", "provinceName").setFirstResult(0).setMaxResults(2);
        Records cityList = helperService.getRecords(helper, false);
        System.err.println("cityList=" + cityList);

        //        // 一个基本的查询例子 - 查询列表
        //        // 查询所有城市名称以及归属的省份名称，返回2条记录
        //        helper.resetQueryFrom(City.class).fetch(CityFields.name).join(HqlHelper.currTable, CityFields.province, "p")
        //                .fetchOther("p", ProvinceFields.name, "provinceName").setFirstResult(0).setMaxResults(2);
        //        Records cityList = helperService.getRecords(helper, false);
        //        System.err.println("cityList=" + cityList);

        // 一个基本的更新例子
        // 将id=1160799039167057920 的城市名称更新为`新名称`
        // 并且，将sortSeq字段加1
        helper.resetQueryFrom(City.class).update("name", "新名称").updateHqlSegment("sortSeq", "sortSeq + 1")
                .eq("id", "1160799039167057920");
        int updateRow = helperService.update(helper);
        System.err.println("updateRow =" + updateRow);

        // 一个基本的删除例子
        //将name以「测试」开头的城市都删除掉
        helper.resetQueryFrom(City.class).like("name", "测试", MatchType.START);
        int deleteRow = helperService.delete(helper);
        System.err.println("deleteRow =" + deleteRow);

        // 一个基本的分组查询例子
        // 查询每个省份下的城市数量和最大的区号，返回城市数量 between 2 and 5 的数据
        helper.resetQueryFrom(City.class).join(HqlHelper.currTable, "province", "p")
                .groupByOther("p", "name", "provinceName").count("id", "cityCount").having_between(2L, 5L)
                .max("areaCode", "maxAreaCode");
        Records provinceCityCount = helperService.getRecordsGroup(helper);
        System.err.println("provinceCityCount =" + provinceCityCount);
    }
}
