package com.dodo.privilege.job.jobexec;

import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dodo.common.database.hql.HqlHelper;
import com.dodo.common.framework.destroyer.Destroyable;
import com.dodo.common.framework.service.HqlHelperService;
import com.dodo.privilege.entity.admin_1.data_4.DocConvertTask;
import com.dodo.privilege.entity.admin_1.data_4.ExcelImportInfo;
import com.dodo.privilege.entity.admin_1.data_4.ExcelUpdateInfo;
import com.dodo.privilege.entity.admin_1.data_4.VideoConvertTask;
import com.dodo.privilege.entity.admin_1.job_2.QuartzJob;
import com.dodo.privilege.entity.admin_1.job_2.QuartzJobExec;
import com.dodo.privilege.enums.ConvertStatus;
import com.dodo.privilege.enums.ExcelImportStatus;
import com.dodo.privilege.enums.ExcelUpdateStatus;
import com.dodo.privilege.enums.QuartzJobStatus;
import com.dodo.privilege.job.listener.DodoJobListener;
import com.dodo.utils.config.DodoFrameworkConfigUtil.DodoCommonConfigUtil;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
@Component("customerJobExecutor")
public class JobExecutor implements Destroyable {
    private static final Logger LOGGER                = LoggerFactory.getLogger(JobExecutor.class);

    public static final String  JOB_DO_NOTHING_RESULT = "_J_D_N_R_";

    @Autowired
    private Scheduler           scheduler;

    @Autowired
    private HqlHelperService    hqlHelperService;

    @SuppressWarnings("unchecked")
    public void execute() {
        if (!DodoCommonConfigUtil.isRunJob) {
            return;
        }
        HqlHelper helper = HqlHelper.queryFrom(QuartzJobExec.class);
        try {
            helper.update("jobStatus", QuartzJobStatus.ERROR).eq("jobStatus", QuartzJobStatus.ING);
            hqlHelperService.update(helper);
        } catch (Exception e1) {
        }
        try {
            helper.resetQueryFrom(ExcelImportInfo.class).update("importStatus", ExcelImportStatus.ERROR)
                    .eq("importStatus", ExcelImportStatus.ING);
            hqlHelperService.update(helper);
        } catch (Exception e1) {
        }
        try {
            helper.resetQueryFrom(ExcelUpdateInfo.class).update("updateStatus", ExcelUpdateStatus.ERROR)
                    .eq("updateStatus", ExcelUpdateStatus.ING);
            hqlHelperService.update(helper);
        } catch (Exception e1) {
        }
        try {
            helper.resetQueryFrom(DocConvertTask.class).update("convertStatus", ConvertStatus.ERROR)
                    .eq("convertStatus", ConvertStatus.ING);
            hqlHelperService.update(helper);
        } catch (Exception e1) {
        }
        try {
            helper.resetQueryFrom(VideoConvertTask.class).update("convertStatus", ConvertStatus.ERROR)
                    .eq("convertStatus", ConvertStatus.ING);
            hqlHelperService.update(helper);
        } catch (Exception e1) {
        }
        List<QuartzJob> allJobs = hqlHelperService.getEntitys(QuartzJob.class);
        for (QuartzJob job : allJobs) {
            QuartzJobExec quartzJobExec = new QuartzJobExec();
            quartzJobExec.setInitTime(new Date());
            quartzJobExec.setQuartzJob(job);
            quartzJobExec.setJobExecLog("Init OK...");
            quartzJobExec.setJobStatus(QuartzJobStatus.INIT);

            if (!job.getInUse()) {
                quartzJobExec.setJobExecLog("The Job has not been enabled.");
                hqlHelperService.save(quartzJobExec);
                continue;
            }

            if (job.getEndTime() != null && job.getEndTime().before(job.getStartTime())) {
                quartzJobExec.setJobExecLog("The end of the Job time is greater than the start time.");
                hqlHelperService.save(quartzJobExec);
                continue;
            }

            if (job.getEndTime() != null && job.getEndTime().before(new Date())) {
                quartzJobExec.setJobExecLog("The Job has been overdue.");
                hqlHelperService.save(quartzJobExec);
                continue;
            }

            if (job.getJobClass() == null) {
                quartzJobExec.setJobExecLog("The Job Class Error.");
                hqlHelperService.save(quartzJobExec);
                continue;
            }
            Class<? extends Job> clazz = null;
            try {
                clazz = (Class<? extends Job>) Class.forName(job.getJobClass());
            } catch (Exception e) {
                quartzJobExec.setJobExecLog("The Job Class does not exist or illegal.");
                hqlHelperService.save(quartzJobExec);
                continue;
            }

            Class<?>[] clazzInterfaces = clazz.getInterfaces();
            boolean isJob = false;
            for (Class<?> interfaceClass : clazzInterfaces) {
                if (interfaceClass.equals(org.quartz.Job.class)) {
                    isJob = true;
                }
            }

            if (!isJob) {
                quartzJobExec.setJobExecLog("The Job must implement the 'org.quartz.Job'.");
                hqlHelperService.save(quartzJobExec);
                continue;
            }

            try {
                JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(job.getJobClass() + "_Job").build();

                SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule();
                if (job.getRepeatCount() > 0) {
                    scheduleBuilder.withRepeatCount(job.getRepeatCount());
                } else {
                    scheduleBuilder.repeatForever();
                }

                scheduleBuilder.withIntervalInSeconds(job.getRepeatInterval());

                TriggerBuilder<SimpleTrigger> triggerBuilder = TriggerBuilder.newTrigger()
                        .withIdentity(job.getJobClass() + "Trigger").withSchedule(scheduleBuilder);

                if (job.getIsAutoExec()) {
                    triggerBuilder.startNow();
                } else {
                    triggerBuilder.startAt(job.getStartTime());
                }

                if (job.getEndTime() != null) {
                    triggerBuilder.endAt(job.getEndTime());
                }

                scheduler.scheduleJob(jobDetail, triggerBuilder.build());
                LOGGER.info("ScheduleJob:{}", jobDetail);
            } catch (Exception e) {
                e.printStackTrace();
                quartzJobExec.setJobExecLog("Start Fail...,Exception:" + e.getMessage());
            }
            hqlHelperService.save(quartzJobExec);
        }

        try {
            if (!scheduler.isStarted()) {
                scheduler.getListenerManager().addJobListener(new DodoJobListener());
                scheduler.start();
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        try {
            LOGGER.info("[begin] Halts the Scheduler's firing of Triggers, and cleans up all resources associated with the Scheduler.");
            scheduler.shutdown(true);
            LOGGER.info("[success] Halts the Scheduler's firing of Triggers, and cleans up all resources associated with the Scheduler.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
