package com.dodo.testing;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

/**
 * 
 * 单元测试配置入口类
 * 
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
@Configuration
@ImportResource("classpath:/application-context.xml")
@PropertySource({ "classpath:/dodo_framework_config.properties", "classpath:/aliyunoss.properties",
        "classpath:/logging.properties", "classpath:/tenpay_config.properties", "classpath:/wechat_config.properties",
        "classpath:/idworker.properties" })
public class DodoTest {

}
