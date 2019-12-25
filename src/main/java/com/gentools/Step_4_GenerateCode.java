package com.gentools;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dodo.generate.freemaker.DodoFreemarkerConfiguration;
import com.dodo.utils.CommonUtil;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class Step_4_GenerateCode {
    private static final Logger LOGGER = LoggerFactory.getLogger(Step_4_GenerateCode.class);

    public static void main(String[] args) throws Exception {
        long beginTime = System.currentTimeMillis();
        try {
            DodoFreemarkerConfiguration configuration = new DodoFreemarkerConfiguration();
            /**
             * 不传参数表示直接生成到项目中 生成完之后 需要手工刷新项目 当传一个指定文件夹（目录）生成文件到指定目录中
             * 随后需要手工合并到项目中
             * 
             */
            // 生成 Action
            configuration.GenerateCodeActions();
            // 生成 Dao
            configuration.GenerateCodeDaos();
            // 生成 Service
            configuration.GenerateCodeServies();
            // 生成 Views
            configuration.GenerateCodeViews();
            // 生成 实体类的静态字段类
            configuration.GenerateEntityPropertiesClasses();
            // 生成 打印生成结果
            configuration.printGenerateStatus();
            LOGGER.info("");
            LOGGER.info("******************************************************");
            LOGGER.info(Step_4_GenerateCode.class.getSimpleName() + " ....Exec OK!");
            LOGGER.info("Take time -> " + CommonUtil.getOnlineTimeStr(new Date().getTime() - beginTime));
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("");
            LOGGER.error(Step_4_GenerateCode.class.getSimpleName() + " ....Exec Error!");
        }
        System.exit(0);
    }
}
