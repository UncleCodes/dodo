package com.gentools;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dodo.generate.datainit.BusinessDataIniter;
import com.dodo.utils.CommonUtil;
import com.gentools.datainit.jobs.JobDataInit;
import com.gentools.datainit.jobs.LocationInit;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class Step_3_InitBusinessData {
    private static final Logger LOGGER = LoggerFactory.getLogger(Step_3_InitBusinessData.class);

    public static void main(String[] args) throws Exception {
        long beginTime = System.currentTimeMillis();
        try {
            BusinessDataIniter initer = new BusinessDataIniter();
            initer.addTarget(new LocationInit());
            initer.addTarget(new JobDataInit());
            initer.execute();
            LOGGER.info("");
            LOGGER.info("******************************************************");
            LOGGER.info(Step_3_InitBusinessData.class.getSimpleName() + " ....Exec OK!");
            LOGGER.info("Take time -> " + CommonUtil.getOnlineTimeStr(new Date().getTime() - beginTime));
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("");
            LOGGER.error(Step_3_InitBusinessData.class.getSimpleName() + " ....Exec Error!");
        }
        System.exit(0);
    }
}
