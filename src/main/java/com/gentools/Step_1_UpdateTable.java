package com.gentools;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dodo.generate.hibernate.HibernateConfigUtil;
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
public class Step_1_UpdateTable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Step_1_UpdateTable.class);

    public static void main(String[] args) throws Exception {
        long beginTime = System.currentTimeMillis();
        HibernateConfigUtil configUtil = null;
        try {
            configUtil = new HibernateConfigUtil();
            configUtil.update();
            LOGGER.info("");
            LOGGER.info("******************************************************");
            LOGGER.info(Step_1_UpdateTable.class.getSimpleName() + " ....Exec OK!");
            LOGGER.info("Take time -> " + CommonUtil.getOnlineTimeStr(new Date().getTime() - beginTime));
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("");
            LOGGER.error(Step_1_UpdateTable.class.getSimpleName() + " ....Exec Error!");
        } finally {
            if (configUtil != null) {
                configUtil.close();
            }
        }
        System.exit(0);
    }
}
