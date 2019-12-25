package com.dodo.testing.tests;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.dodo.common.framework.service.HqlHelperService;
import com.dodo.testing.DodoTestBase;
import com.pay.tenpay.PayConfig;
import com.third.aliyun.oss.OSSService;

/**
 * 
 * 单元测试示例
 * 
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class ExampleTest extends DodoTestBase {

    @Autowired
    private OSSService       ossService;

    @Autowired
    private HqlHelperService helperService;

    @Test
    public void testConfig() {
        System.err.println("1:  " + PayConfig.getTenpayAccount("AccountB"));
        System.err.println("2:  " + ossService);
        System.err.println("3:  " + helperService);
        // HqlHelper helper = HqlHelper.queryFrom(Province.class);
        //        helper.fetch(ProvinceFields.id, ProvinceFields.name).orderBy(ProvinceFields.name, OrderType.desc);
        //
        //        Records records = helperService.getRecords(helper, Boolean.FALSE);
        //        records.forEach(record -> {
        //            System.err.println(record);
        //        });
    }
}
