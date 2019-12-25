package com.dodo.testing.tests;

import org.testng.annotations.Test;

import com.dodo.utils.IdcardInfoExtractor;
import com.dodo.utils.IdcardValidator;

/**
 * 身份证工具类测试
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class IdCardTest {
    @Test
    public void testIdCard() {
        // 校验身份证信息是否合法
        Boolean isValid = IdcardValidator.isValidatedAllIdcard("身份证号码");
        System.err.println(isValid);
        // 获取身份证信息
        IdcardInfoExtractor extractor = new IdcardInfoExtractor("身份证号码");
        // 省份
        System.err.println(extractor.getProvince());
        // 生日 及 出生年 月 日
        System.err.println(extractor.getBirthday());
        System.err.println(extractor.getYear());
        System.err.println(extractor.getMonth());
        System.err.println(extractor.getDay());
        // 性别
        System.err.println(extractor.getGender());
    }
}
