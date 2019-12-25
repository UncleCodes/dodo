package com.dodo.testing.tests.hqltest;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.dodo.common.database.data.Record;
import com.dodo.common.database.data.Records;
import com.dodo.common.database.hql.HqlHelper;
import com.dodo.common.framework.service.HqlHelperService;
import com.dodo.privilege.entity.admin_1.base_1.Role;
import com.dodo.privilege.entity.admin_1.location_6.City;
import com.dodo.privilege.entity.admin_1.location_6.Province;
import com.dodo.testing.DodoTestBase;

/**
 * HqlHelper 关联示例
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class HqlHelperJoinTest extends DodoTestBase {

    @Autowired
    private HqlHelperService helperService;

    //inner join
    @Test
    public void innerJoinTest() {
        // 查询ID=1160799039167057920的城市的省份名称
        HqlHelper helper = HqlHelper.queryFrom(City.class);
        helper.join(HqlHelper.currTable, "province", "p").fetchOther("p", "name", "provinceName")
                .eq("id", "1160799039167057920");
        Record tempRecord = helperService.getRecord(helper);
        System.err.println(tempRecord);
    }

    //full join
    @Test
    public void fullJoinTest() {
        // 查询具有相同名称的省份和城市，取一条记录
        HqlHelper helper = HqlHelper.queryFrom(City.class);
        helper.join(Province.class, "p").fetchOther("p", "name", "provinceName")
                .eqProperty(HqlHelper.currTable, "name", "p", "name");
        Record tempRecord = helperService.getRecord(helper);
        System.err.println(tempRecord);
    }

    //right join
    @Test
    public void rightJoinTest() {
        // 角色右关联查询创建该角色的管理员，返回角色名称和管理员名称
        // 因为'报表+Demo权限，行权限：妹妹'这个管理员没有创建过角色，因为是right join ，所以返回的roleName=null
        HqlHelper helper = HqlHelper.queryFrom(Role.class);
        helper.fetchOther(HqlHelper.currTable, "name", "roleName").rightJoin(HqlHelper.currTable, "admin", "a")
                .fetchOther("a", "name", "adminName");
        Records tempRecords = helperService.getRecords(helper, false);
        System.err.println(tempRecords);
    }

    //left join
    @Test
    public void leftJoinTest() {
        // 角色左关联查询创建该角色的管理员，返回角色名称和管理员名称
        // 因为'系统管理员'这个角色没有创建人，因为是left join ，所以返回的adminName=null
        HqlHelper helper = HqlHelper.queryFrom(Role.class);
        helper.fetchOther(HqlHelper.currTable, "name", "roleName").leftJoin(HqlHelper.currTable, "admin", "a")
                .fetchOther("a", "name", "adminName");
        Records tempRecords = helperService.getRecords(helper, false);
        System.err.println(tempRecords);
    }
}
