package com.dodo.privilege.job.listener;

import java.io.PrintStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

import com.dodo.common.database.data.Record;
import com.dodo.common.database.hql.HqlHelper;
import com.dodo.common.framework.service.HqlHelperService;
import com.dodo.privilege.entity.admin_1.job_2.QuartzJob;
import com.dodo.privilege.entity.admin_1.job_2.QuartzJobExec;
import com.dodo.privilege.enums.QuartzJobStatus;
import com.dodo.privilege.job.jobexec.JobExecutor;
import com.dodo.utils.CommonUtil;
import com.dodo.utils.SpringUtil;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class DodoJobListener implements JobListener {
    private HqlHelperService          hqlHelperService  = null;
    private Map<String, Serializable> jobs              = new HashMap<String, Serializable>();
    private static String             _KEY_JOB_EXEC_ID_ = "_KEY_JOB_EXEC_ID_";

    public DodoJobListener() {
        hqlHelperService = (HqlHelperService) SpringUtil.getBean("hqlHelperServiceImpl");
    }

    @Override
    public String getName() {
        return "DodoJobListener";
    }

    public synchronized Serializable getJobId(String jobClass) {
        Serializable jobId = jobs.get(jobClass);
        if (jobId == null) {
            HqlHelper hqlHelper = HqlHelper.queryFrom(QuartzJob.class);
            hqlHelper.fetch("id").eq("jobClass", jobClass);
            Record returnMap = hqlHelperService.getRecord(hqlHelper);
            if (returnMap == null || returnMap.size() == 0) {
                return null;
            }
            jobId = (Serializable) (returnMap.get("id"));
            jobs.put(jobClass, jobId);
        }
        return jobId;
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        String jobClass = context.getJobDetail().getJobClass().getName();
        Serializable jobId = getJobId(jobClass);
        if (jobId != null) {
            Object jobExecId = context.get(_KEY_JOB_EXEC_ID_);
            if (jobExecId != null) {
                HqlHelper helper = HqlHelper.queryFrom(QuartzJobExec.class);
                helper.update("jobExecLog", "调度器拒绝执行任务...").update("jobStatus", QuartzJobStatus.VETOED)
                        .update("beginTime", context.getFireTime()).update("endTime", new Date())
                        .update("excuteTime", CommonUtil.getOnlineTimeStr(context.getJobRunTime()))
                        .update("nextTime", context.getNextFireTime())
                        .update("prevTime", context.getPreviousFireTime()).eq("id", jobExecId);
                hqlHelperService.update(helper);
                context.put(_KEY_JOB_EXEC_ID_, null);
            } else {
                QuartzJobExec quartzJobExec = new QuartzJobExec();
                quartzJobExec.setQuartzJob(hqlHelperService.load(jobId, QuartzJob.class));
                quartzJobExec.setJobExecLog("调度器拒绝执行任务...");
                quartzJobExec.setJobStatus(QuartzJobStatus.VETOED);
                quartzJobExec.setBeginTime(context.getFireTime());
                quartzJobExec.setEndTime(new Date());
                quartzJobExec.setExcuteTime(CommonUtil.getOnlineTimeStr(context.getJobRunTime()));
                quartzJobExec.setNextTime(context.getNextFireTime());
                quartzJobExec.setPrevTime(context.getPreviousFireTime());
                hqlHelperService.save(quartzJobExec);
            }
        }
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        String jobClass = context.getJobDetail().getJobClass().getName();
        Serializable jobId = getJobId(jobClass);
        if (jobId != null) {
            Object jobExecId = context.get(_KEY_JOB_EXEC_ID_);
            if (jobExecId != null) {
                HqlHelper helper = HqlHelper.queryFrom(QuartzJobExec.class);
                helper.update("jobExecLog", "开始执行任务...").update("jobStatus", QuartzJobStatus.ING)
                        .update("beginTime", context.getFireTime()).update("nextTime", context.getNextFireTime())
                        .update("prevTime", context.getPreviousFireTime()).eq("id", jobExecId);
                hqlHelperService.update(helper);
            } else {
                QuartzJobExec quartzJobExec = new QuartzJobExec();
                quartzJobExec.setQuartzJob(hqlHelperService.load(jobId, QuartzJob.class));
                quartzJobExec.setJobExecLog("开始执行任务...");
                quartzJobExec.setJobStatus(QuartzJobStatus.ING);
                quartzJobExec.setBeginTime(context.getFireTime());
                quartzJobExec.setNextTime(context.getNextFireTime());
                quartzJobExec.setPrevTime(context.getPreviousFireTime());
                hqlHelperService.save(quartzJobExec);
                context.put(_KEY_JOB_EXEC_ID_, quartzJobExec.getId());
            }
        }
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException exception) {
        StringBuilder exceptionbBuilder = new StringBuilder("<pre>");
        if (exception != null) {
            ByteArrayOutputStream byteArrOut = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(byteArrOut);
            exception.printStackTrace(printStream);
            exceptionbBuilder.append(byteArrOut.toString(Charset.defaultCharset()));
            exceptionbBuilder.append("<br/>");
            if (printStream != null) {
                printStream.close();
            }
        }
        exceptionbBuilder.append("</pre>");

        String jobClass = context.getJobDetail().getJobClass().getName();
        Serializable jobId = getJobId(jobClass);
        if (jobId != null) {
            Object jobExecId = context.get(_KEY_JOB_EXEC_ID_);
            context.put(_KEY_JOB_EXEC_ID_, null);

            if (JobExecutor.JOB_DO_NOTHING_RESULT.equals(context.getResult())) {
                if (jobExecId != null) {
                    hqlHelperService.delete(HqlHelper.queryFrom(QuartzJobExec.class).eq("id", jobExecId));
                }
            } else {
                if (jobExecId != null) {
                    HqlHelper helper = HqlHelper.queryFrom(QuartzJobExec.class);
                    helper.update("jobExecLog", context.getResult() == null ? "" : context.getResult().toString())
                            .update("beginTime", context.getFireTime())
                            .update("endTime", new Date())
                            .update("excuteTime",
                                    CommonUtil.getOnlineTimeStr(new Date().getTime() - context.getFireTime().getTime()))
                            .update("nextTime", context.getNextFireTime())
                            .update("prevTime", context.getPreviousFireTime())
                            .update("jobExecException", exceptionbBuilder.toString()).eq("id", jobExecId);
                    if (exception == null) {
                        helper.update("jobStatus", QuartzJobStatus.OK);
                    } else {
                        helper.update("jobStatus", QuartzJobStatus.EXCEPTION);
                    }
                    hqlHelperService.update(helper);
                } else {
                    QuartzJobExec quartzJobExec = new QuartzJobExec();
                    quartzJobExec.setQuartzJob(hqlHelperService.load(jobId, QuartzJob.class));
                    quartzJobExec.setJobExecLog(context.getResult() == null ? "" : context.getResult().toString());
                    if (exception == null) {
                        quartzJobExec.setJobStatus(QuartzJobStatus.OK);
                    } else {
                        quartzJobExec.setJobStatus(QuartzJobStatus.EXCEPTION);
                    }
                    quartzJobExec.setBeginTime(context.getFireTime());
                    quartzJobExec.setEndTime(new Date());
                    quartzJobExec.setExcuteTime(CommonUtil.getOnlineTimeStr(new Date().getTime()
                            - context.getFireTime().getTime()));
                    quartzJobExec.setNextTime(context.getNextFireTime());
                    quartzJobExec.setPrevTime(context.getPreviousFireTime());
                    quartzJobExec.setJobExecException(exceptionbBuilder.toString());
                    hqlHelperService.save(quartzJobExec);
                }
            }
        }
    }
}
