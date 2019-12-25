package com.dodo.testing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

/**
 * 
 * 单元测试基类，单元测试需集成此类
 * 
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
@ContextConfiguration(classes = DodoTest.class)
@Configuration
public abstract class DodoTestBase extends AbstractTestNGSpringContextTests {
    public static final Logger       logger = LoggerFactory.getLogger(DodoTestBase.class);
    public static ApplicationContext ctx;

    @BeforeSuite
    public void doBeforeSuite() {
        logger.info("【" + getClass().getSimpleName() + "】Begin++++++++++++++++++++++++++++");
    }

    @AfterSuite
    public void doAfterSuite() {
        System.setProperty("dubbo.service.shutdown.wait", "88");
        logger.info("【" + getClass().getSimpleName() + "】End++++++++++++++++++++++++++++");
    }

    @BeforeClass
    public void doBeforeClass() {
        ctx = applicationContext;
        logger.info("Spring container instance is ready!");
    }

    @AfterClass
    public void doAfterClass() {
    }
}
