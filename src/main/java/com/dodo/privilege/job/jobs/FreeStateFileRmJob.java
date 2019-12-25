package com.dodo.privilege.job.jobs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.dodo.common.database.data.Ite;
import com.dodo.common.database.data.Record;
import com.dodo.common.database.data.Records;
import com.dodo.common.database.hql.HqlHelper;
import com.dodo.common.framework.bean.file.DodoFile;
import com.dodo.common.framework.bean.file.DodoVideoFile;
import com.dodo.common.framework.service.HqlHelperService;
import com.dodo.privilege.entity.admin_1.data_4.FreeStateFile;
import com.dodo.privilege.job.jobexec.JobExecutor;
import com.dodo.utils.JacksonUtil;
import com.dodo.utils.SpringUtil;
import com.dodo.utils.file.FileUtils;
import com.third.aliyun.oss.OSSService;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class FreeStateFileRmJob implements Job {
    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        HqlHelperService hqlHelperService = (HqlHelperService) SpringUtil.getBean("hqlHelperServiceImpl");
        HqlHelper helper = HqlHelper.queryFrom(FreeStateFile.class);

        helper.fetch("id", "fileStr", "videoFileStr", "ossBucket").setFirstResult(0).setMaxResults(100);
        Records freeStateFiles = hqlHelperService.getRecords(helper, Boolean.FALSE);
        if (freeStateFiles.size() == 0) {
            arg0.setResult(JobExecutor.JOB_DO_NOTHING_RESULT);
            return;
        }

        List<Object> freeStateFilesIds = new ArrayList<Object>(freeStateFiles.size());
        DodoFile[] dodoFiles = null;
        DodoVideoFile[] videoFiles = null;
        File tempFile = null;
        String fileStr = null;
        String videoFileStr = null;
        String ossBucket = null;
        Record record = null;
        Ite ite = freeStateFiles.iterator();
        while (ite.hasNext()) {
            record = ite.next();
            try {
                tempFile = null;
                fileStr = record.get("fileStr");
                videoFileStr = record.get("videoFileStr");
                ossBucket = record.get("ossBucket");

                if (StringUtils.isBlank(fileStr) && StringUtils.isBlank(videoFileStr)) {
                    freeStateFilesIds.add(record.get("id"));
                    continue;
                }
                // file doc
                if (StringUtils.isNotBlank(fileStr)) {
                    if (fileStr.startsWith("[")) {
                        dodoFiles = JacksonUtil.toObject(fileStr, DodoFile[].class);
                        if (dodoFiles != null && dodoFiles.length > 0) {
                            tempFile = new File(FileUtils.getUploadTargetRootDir(), dodoFiles[0].getFilePath());
                        }
                    }
                    // only path
                    else if (!fileStr.startsWith("http")) {
                        tempFile = new File(FileUtils.getUploadTargetRootDir(), fileStr);
                    } else {
                        if (OSSService.delete(ossBucket, fileStr)) {
                            freeStateFilesIds.add(record.get("id"));
                        }
                    }
                }
                // video
                else if (StringUtils.isNotBlank(videoFileStr)) {
                    videoFiles = JacksonUtil.toObject(videoFileStr, DodoVideoFile[].class);
                    if (videoFiles != null && videoFiles.length > 0) {
                        tempFile = new File(FileUtils.getUploadTargetRootDir(), videoFiles[0].getFilePath());
                    }
                }

                if (tempFile != null) {
                    if (tempFile.getParentFile().getName().startsWith("dododoc")) {
                        org.apache.commons.io.FileUtils.deleteDirectory(tempFile.getParentFile());
                    } else {
                        tempFile.delete();
                    }
                    freeStateFilesIds.add(record.get("id"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (freeStateFilesIds.size() > 0) {
            helper.resetQueryFrom(FreeStateFile.class).in("id", freeStateFilesIds);
            hqlHelperService.delete(helper);
        }
    }
}
