package com.gentools.datainit.jobs;

import java.util.Date;

import org.hibernate.Session;

import com.dodo.generate.datainit.BusinessDataInitFace;
import com.dodo.privilege.entity.admin_1.job_2.QuartzJob;
import com.dodo.privilege.job.jobs.DefaultConvertJob;
import com.dodo.privilege.job.jobs.ExcelImportOrUpdateJob;
import com.dodo.privilege.job.jobs.FreeStateFileRmJob;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class JobDataInit extends BusinessDataInitFace {
    @Override
    public void doInitBusiness(Session session) throws Exception {
        QuartzJob tempJob = (QuartzJob) session.createQuery("from QuartzJob c where c.jobClass = :jobClass")
                .setParameter("jobClass", ExcelImportOrUpdateJob.class.getName()).uniqueResult();
        if (tempJob == null) {
            QuartzJob excelImportOrUpdateJob = new QuartzJob();
            excelImportOrUpdateJob.setCreateDate(new Date());
            excelImportOrUpdateJob.setEndTime(null);
            excelImportOrUpdateJob.setInUse(Boolean.TRUE);
            excelImportOrUpdateJob.setJobClass(ExcelImportOrUpdateJob.class.getName());
            excelImportOrUpdateJob.setJobDesc("Excel Import Or Update Job");
            excelImportOrUpdateJob.setJobName("Excel文件导入/批量更新后台任务");
            excelImportOrUpdateJob.setModifyDate(new Date());
            excelImportOrUpdateJob.setRepeatCount(-1);
            excelImportOrUpdateJob.setRepeatInterval(5 * 60);
            excelImportOrUpdateJob.setSortSeq(0);
            excelImportOrUpdateJob.setStartTime(new Date());
            excelImportOrUpdateJob.setIsAutoExec(Boolean.FALSE);
            session.save(excelImportOrUpdateJob);
        }
        tempJob = (QuartzJob) session.createQuery("from QuartzJob c where c.jobClass = :jobClass")
                .setParameter("jobClass", FreeStateFileRmJob.class.getName()).uniqueResult();
        if (tempJob == null) {
            QuartzJob freeStateFileRmJob = new QuartzJob();
            freeStateFileRmJob.setCreateDate(new Date());
            freeStateFileRmJob.setEndTime(null);
            freeStateFileRmJob.setInUse(Boolean.TRUE);
            freeStateFileRmJob.setJobClass(FreeStateFileRmJob.class.getName());
            freeStateFileRmJob.setJobDesc("remove free state file .");
            freeStateFileRmJob.setJobName("游离态文件删除任务");
            freeStateFileRmJob.setModifyDate(new Date());
            freeStateFileRmJob.setRepeatCount(-1);
            freeStateFileRmJob.setRepeatInterval(60 * 60);
            freeStateFileRmJob.setSortSeq(0);
            freeStateFileRmJob.setStartTime(new Date());
            freeStateFileRmJob.setIsAutoExec(Boolean.TRUE);
            session.save(freeStateFileRmJob);
        }

        tempJob = (QuartzJob) session.createQuery("from QuartzJob c where c.jobClass = :jobClass")
                .setParameter("jobClass", DefaultConvertJob.class.getName()).uniqueResult();
        if (tempJob == null) {
            QuartzJob defaultConvertJob = new QuartzJob();
            defaultConvertJob.setCreateDate(new Date());
            defaultConvertJob.setEndTime(null);
            defaultConvertJob.setInUse(Boolean.TRUE);
            defaultConvertJob.setJobClass(DefaultConvertJob.class.getName());
            defaultConvertJob.setJobDesc("convert video & doc files .");
            defaultConvertJob.setJobName("视频和文档转换");
            defaultConvertJob.setModifyDate(new Date());
            defaultConvertJob.setRepeatCount(-1);
            defaultConvertJob.setRepeatInterval(5 * 60);
            defaultConvertJob.setSortSeq(0);
            defaultConvertJob.setStartTime(new Date());
            defaultConvertJob.setIsAutoExec(Boolean.TRUE);
            session.save(defaultConvertJob);
        }
    }
}
