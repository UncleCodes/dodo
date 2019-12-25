package com.dodo.privilege.job.jobs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dodo.common.database.data.Record;
import com.dodo.common.database.hql.HqlHelper;
import com.dodo.common.doc2swf.office.DodoOfficeConverter;
import com.dodo.common.doc2swf.pdf.DodoPDFConverter;
import com.dodo.common.framework.bean.file.DodoFile;
import com.dodo.common.framework.bean.file.DodoVideoFile;
import com.dodo.common.framework.bean.pager.PageModel.OrderType;
import com.dodo.common.framework.entity.BaseEntity;
import com.dodo.common.framework.service.HqlHelperService;
import com.dodo.common.video.VideoConvertor;
import com.dodo.common.video.VideoConvertor.ConvertorType;
import com.dodo.common.video.VideoInfo;
import com.dodo.privilege.entity.admin_1.data_4.DocConvertTask;
import com.dodo.privilege.entity.admin_1.data_4.VideoConvertTask;
import com.dodo.privilege.enums.ConvertStatus;
import com.dodo.privilege.job.jobexec.JobExecutor;
import com.dodo.utils.JacksonUtil;
import com.dodo.utils.SpringUtil;
import com.dodo.utils.file.FileUtils;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class DefaultConvertJob implements Job {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultConvertJob.class);

    private Object getCorrectTypeEntityId(String entityId) {
        Class<?> clazz = BaseEntity.class;
        try {
            Class<?> idClazz = clazz.getDeclaredField("id").getType();
            if (idClazz == int.class || idClazz == Integer.class) {
                return Integer.parseInt(entityId);
            }
            if (idClazz == long.class || idClazz == Long.class) {
                return Long.parseLong(entityId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entityId;
    }

    @SuppressWarnings("unchecked")
    private void doDocConvertTask(DocConvertTask task) {
        HqlHelperService helperService = (HqlHelperService) SpringUtil.getBean("hqlHelperServiceImpl");
        HqlHelper helper = HqlHelper.queryFrom(DocConvertTask.class);
        try {
            // 初始化  并启动转换服务
            DodoOfficeConverter.init();
            DodoOfficeConverter.startService();
            DodoPDFConverter.init();
            if (!DodoOfficeConverter.isReady() || !DodoPDFConverter.isReady()) {
                LOGGER.error("DodoOfficeConverter or DodoPDFConverter is not ready.");
                return;
            }

            // 更新转换任务状态为处理中
            helperService.update(helper.update("convertStatus", ConvertStatus.ING).eq("id", task.getId()));

            // 找到要转换的文件
            DodoFile[] dodoFilesTask = JacksonUtil.toObject(task.getFileStr(), DodoFile[].class);
            if (dodoFilesTask == null) {
                dodoFilesTask = new DodoFile[0];
            }
            DodoFile taskDodoFile = null;
            for (DodoFile file : dodoFilesTask) {
                if (file.getFileId().equals(task.getFileId())) {
                    taskDodoFile = file;
                    break;
                }
            }
            // 文件不存在 返回
            if (taskDodoFile == null) {
                helperService.update(helper.resetQueryFrom(DocConvertTask.class)
                        .update("convertStatus", ConvertStatus.NOFILE).eq("id", task.getId()));
                return;
            }
            // 得到目标 Class
            Class<? extends BaseEntity> targetClass = null;
            try {
                targetClass = (Class<? extends BaseEntity>) Class.forName(task.getTargetEntity().getClassName());
            } catch (Exception e) {
                e.printStackTrace();
                helperService.update(helper.resetQueryFrom(DocConvertTask.class)
                        .update("convertStatus", ConvertStatus.EXCEPTION).eq("id", task.getId()));
                return;
            }
            // 读取目标对象 字段
            Object entityId = getCorrectTypeEntityId(task.getEntityId());
            String fileStr = null;
            helper.resetQueryFrom(targetClass).fetch(task.getFieldName()).eq("id", entityId);
            Map<String, String> extendMap = null;
            Record propertyMap = (Record) helperService.getClass()
                    .getMethod("getRecord", new Class[] { HqlHelper.class }).invoke(helperService, helper);
            if (propertyMap == null) {
                helperService.update(helper.resetQueryFrom(DocConvertTask.class)
                        .update("convertStatus", ConvertStatus.NOFILE).eq("id", task.getId()));
                return;
            }
            if (StringUtils.isNotBlank(task.getExtFieldName())) {
                extendMap = (Map<String, String>) propertyMap.get(task.getFieldName());
                fileStr = extendMap.get(task.getExtFieldName());
            } else {
                fileStr = (String) propertyMap.get(task.getFieldName());
            }

            // 找到要转换的文件 目标对象上的
            DodoFile targetDodoFile = null;
            DodoFile[] dodoFilesTarget = JacksonUtil.toObject(fileStr, DodoFile[].class);
            if (dodoFilesTarget == null) {
                dodoFilesTarget = new DodoFile[0];
            }
            for (DodoFile file : dodoFilesTarget) {
                if (file.getFileId().equals(task.getFileId())) {
                    targetDodoFile = file;
                    break;
                }
            }

            // 文件不存在 返回
            if (targetDodoFile == null || (!targetDodoFile.getFilePath().equals(taskDodoFile.getFilePath()))) {
                helperService.update(helper.resetQueryFrom(DocConvertTask.class)
                        .update("convertStatus", ConvertStatus.NOFILE).eq("id", task.getId()));
                return;
            }

            // 已经转换成功 返回
            if ("1".equals(targetDodoFile.getIsComplete())) {
                helperService.update(helper.resetQueryFrom(DocConvertTask.class)
                        .update("convertStatus", ConvertStatus.OK).update("convertPercent", "100.00%")
                        .eq("id", task.getId()));
                return;
            }

            // 找到磁盘文件
            File inputFile = new File(FileUtils.getUploadTargetRootDir(), targetDodoFile.getFilePath());
            // 文件不存在，表示文件已经被删除 无需转换
            if (!inputFile.exists()) {
                helperService.update(helper.resetQueryFrom(DocConvertTask.class)
                        .update("convertStatus", ConvertStatus.NOFILE).eq("id", task.getId()));
                return;
            }

            // 获得文件前后缀
            String fileSuffix = FileUtils.getFileSuffix(inputFile.getAbsolutePath());
            String filePrefix = FileUtils.getFilePrefix(inputFile.getAbsolutePath());

            // 不支持的文件类型
            if (!DodoOfficeConverter.isSupport(fileSuffix)) {
                helperService.update(helper.resetQueryFrom(DocConvertTask.class)
                        .update("convertStatus", ConvertStatus.NOTSUPPORT).eq("id", task.getId()));
                return;
            }

            // 文档不是PDF则首先将其转换为PDF
            if (!"pdf".equals(fileSuffix)) {
                DodoOfficeConverter.toPDF(inputFile, new File(filePrefix + ".pdf"));
            }

            //PDF转换失败
            File pdfFile = new File(filePrefix + ".pdf");
            if (!pdfFile.exists()) {
                helperService.update(helper.resetQueryFrom(DocConvertTask.class)
                        .update("convertStatus", ConvertStatus.EXCEPTION).eq("id", task.getId()));
                return;
            }

            // 开始将PDF转换为SWF
            new DodoPDFConverter(pdfFile, false, pdfFile.getParent()).pdf2swf();

            // 判断是否转换成功
            String filePageNumber = "";
            if (Boolean.valueOf(targetDodoFile.getIsSplitPage())) {
                filePageNumber = "1";
            }

            // 转换失败
            if (!new File(FileUtils.getUploadTargetRootDir(), FileUtils.getFilePrefix(targetDodoFile.getFilePath())
                    + "_page" + filePageNumber + ".swf").exists()) {
                helperService.update(helper.resetQueryFrom(DocConvertTask.class)
                        .update("convertStatus", ConvertStatus.EXCEPTION).eq("id", task.getId()));
                return;
            }
            // 已经转换成功
            targetDodoFile.setIsComplete("1");
            taskDodoFile.setIsComplete("1");

            // 更新页码信息
            File pageCountFile = new File(FileUtils.getUploadTargetRootDir(), FileUtils.getFilePrefix(targetDodoFile
                    .getFilePath()) + "_info");
            if (pageCountFile.exists()) {
                try {
                    FileInputStream fileInputStream = new FileInputStream(pageCountFile);
                    targetDodoFile.setPageCount(IOUtils.toString(fileInputStream, Charset.defaultCharset()).trim());
                    try {
                        if (fileInputStream != null) {
                            fileInputStream.close();
                        }
                    } catch (final IOException ioe) {
                    }
                    taskDodoFile.setPageCount(targetDodoFile.getPageCount());
                } catch (Exception ex) {
                }
            }

            // 更新转换结果
            if (StringUtils.isNotBlank(task.getExtFieldName())) {
                extendMap.put(task.getExtFieldName(), JacksonUtil.toJackson(dodoFilesTarget));
                helper.resetQueryFrom(targetClass).update(task.getFieldName(), extendMap).eq("id", entityId);
            } else {
                helper.resetQueryFrom(targetClass).update(task.getFieldName(), JacksonUtil.toJackson(dodoFilesTarget))
                        .eq("id", entityId);
            }
            helperService.getClass().getMethod("update", new Class[] { HqlHelper.class }).invoke(helperService, helper);
            //更新转换任务状态
            helper.resetQueryFrom(DocConvertTask.class).update("convertStatus", ConvertStatus.OK)
                    .update("fileStr", JacksonUtil.toJackson(dodoFilesTask)).update("convertPercent", "100.00%")
                    .eq("id", task.getId());
            helperService.update(helper);
        } catch (Exception e) {
            e.printStackTrace();
            // 异常时， 如果状态是转换中 则还原现场
            helper.resetQueryFrom(DocConvertTask.class).update("convertStatus", ConvertStatus.NEW)
                    .eq("convertStatus", ConvertStatus.ING).eq("id", task.getId());
            helperService.update(helper);

            LOGGER.error("DocConvertTask fail:ID=" + task.getId());
        } finally {
            DodoOfficeConverter.stopService();
        }
    }

    @SuppressWarnings("unchecked")
    private void doVideoConvertTask(VideoConvertTask task) {
        HqlHelperService helperService = (HqlHelperService) SpringUtil.getBean("hqlHelperServiceImpl");
        HqlHelper helper = HqlHelper.queryFrom(VideoConvertTask.class);
        try {
            // 更新转换任务状态为处理中
            helperService.update(helper.update("convertStatus", ConvertStatus.ING).eq("id", task.getId()));

            // 找到要转换的文件
            DodoVideoFile[] videoFilesTask = JacksonUtil.toObject(task.getVideoFileStr(), DodoVideoFile[].class);
            if (videoFilesTask == null) {
                videoFilesTask = new DodoVideoFile[0];
            }
            DodoVideoFile taskVideoFile = null;
            for (DodoVideoFile file : videoFilesTask) {
                if (file.getFileId().equals(task.getVideoFileId())) {
                    taskVideoFile = file;
                    break;
                }
            }
            // 文件不存在 返回
            if (taskVideoFile == null) {
                helperService.update(helper.resetQueryFrom(VideoConvertTask.class)
                        .update("convertStatus", ConvertStatus.NOFILE).eq("id", task.getId()));
                return;
            }
            // 得到目标 Class
            Class<? extends BaseEntity> targetClass = null;
            try {
                targetClass = (Class<? extends BaseEntity>) Class.forName(task.getTargetEntity().getClassName());
            } catch (Exception e) {
                e.printStackTrace();
                helperService.update(helper.resetQueryFrom(VideoConvertTask.class)
                        .update("convertStatus", ConvertStatus.EXCEPTION).eq("id", task.getId()));
                return;
            }
            // 读取目标对象 字段
            Object entityId = getCorrectTypeEntityId(task.getEntityId());
            String videoFileStr = null;
            helper.resetQueryFrom(targetClass).fetch(task.getFieldName()).eq("id", entityId);
            Map<String, String> extendMap = null;
            Record propertyMap = (Record) helperService.getClass()
                    .getMethod("getRecord", new Class[] { HqlHelper.class }).invoke(helperService, helper);
            if (propertyMap == null) {
                helperService.update(helper.resetQueryFrom(VideoConvertTask.class)
                        .update("convertStatus", ConvertStatus.NOFILE).eq("id", task.getId()));
                return;
            }
            if (StringUtils.isNotBlank(task.getExtFieldName())) {
                extendMap = (Map<String, String>) propertyMap.get(task.getFieldName());
                videoFileStr = extendMap.get(task.getExtFieldName());
            } else {
                videoFileStr = (String) propertyMap.get(task.getFieldName());
            }

            // 找到要转换的文件 目标对象上的
            DodoVideoFile targetVideoFile = null;
            DodoVideoFile[] videoFilesTarget = JacksonUtil.toObject(videoFileStr, DodoVideoFile[].class);
            if (videoFilesTarget == null) {
                videoFilesTarget = new DodoVideoFile[0];
            }
            for (DodoVideoFile file : videoFilesTarget) {
                if (file.getFileId().equals(task.getVideoFileId())) {
                    targetVideoFile = file;
                    break;
                }
            }

            // 文件不存在 返回
            if (targetVideoFile == null || (!targetVideoFile.getFilePath().equals(taskVideoFile.getFilePath()))) {
                helperService.update(helper.resetQueryFrom(VideoConvertTask.class)
                        .update("convertStatus", ConvertStatus.NOFILE).eq("id", task.getId()));
                return;
            }

            // 已经转换成功 返回
            if ("1".equals(targetVideoFile.getIsComplete())) {
                helperService.update(helper.resetQueryFrom(VideoConvertTask.class)
                        .update("convertStatus", ConvertStatus.OK).update("convertPercent", "100.00%")
                        .eq("id", task.getId()));
                return;
            }

            // 找到磁盘文件
            File inputFile = new File(FileUtils.getUploadTargetRootDir(), targetVideoFile.getFilePath());
            // 文件不存在，表示文件已经被删除 无需转换
            if (!inputFile.exists()) {
                helperService.update(helper.resetQueryFrom(VideoConvertTask.class)
                        .update("convertStatus", ConvertStatus.NOFILE).eq("id", task.getId()));
                return;
            }

            // 获得文件前后缀
            String fileSuffix = FileUtils.getFileSuffix(inputFile.getAbsolutePath());

            // 不支持的文件类型
            if (!VideoConvertor.isSupport(fileSuffix)) {
                helperService.update(helper.resetQueryFrom(VideoConvertTask.class)
                        .update("convertStatus", ConvertStatus.NOTSUPPORT).eq("id", task.getId()));
                return;
            }

            // 开始转换 转换失败则更新任务状态
            if (!VideoConvertor.convert(inputFile)) {
                helperService.update(helper.resetQueryFrom(VideoConvertTask.class)
                        .update("convertStatus", ConvertStatus.EXCEPTION).eq("id", task.getId()));
                return;
            }
            // 判断是否最终转换成功 判断依据是生成了视频的缩略图
            File thumbnailFile = VideoConvertor.getThumbnailFile(inputFile);
            if (!thumbnailFile.exists() || thumbnailFile.length() == 0) {
                helperService.update(helper.resetQueryFrom(VideoConvertTask.class)
                        .update("convertStatus", ConvertStatus.EXCEPTION).eq("id", task.getId()));
                return;
            }
            // 获得转换结果文件
            File flvFile = VideoConvertor.getTargetFile(inputFile, ConvertorType.TO_FLV);
            File mp4File = VideoConvertor.getTargetFile(inputFile, ConvertorType.TO_MP4);
            File swfFile = VideoConvertor.getTargetFile(inputFile, ConvertorType.TO_SWF);

            // 更新转换状态及结果 --目标
            targetVideoFile.setIsComplete("1");
            targetVideoFile.setThumbnailPath(thumbnailFile.getAbsolutePath().replace(
                    FileUtils.getUploadTargetRootDir(), ""));
            targetVideoFile.setFlvPath(flvFile.getAbsolutePath().replace(FileUtils.getUploadTargetRootDir(), ""));
            targetVideoFile.setMp4Path(mp4File.getAbsolutePath().replace(FileUtils.getUploadTargetRootDir(), ""));
            targetVideoFile.setSwfPath(swfFile.getAbsolutePath().replace(FileUtils.getUploadTargetRootDir(), ""));
            VideoInfo videoInfo = VideoConvertor.getVideoInfo(flvFile);
            targetVideoFile.setDuration(videoInfo.getDuration());
            targetVideoFile.setBitrate(videoInfo.getBitrate());
            targetVideoFile.setSize(videoInfo.getSize());

            // 更新转换结果 -- 目标
            if (StringUtils.isNotBlank(task.getExtFieldName())) {
                extendMap.put(task.getExtFieldName(), JacksonUtil.toJackson(videoFilesTarget));
                helper.resetQueryFrom(targetClass).update(task.getFieldName(), extendMap).eq("id", entityId);
            } else {
                helper.resetQueryFrom(targetClass).update(task.getFieldName(), JacksonUtil.toJackson(videoFilesTarget))
                        .eq("id", entityId);
            }
            helperService.getClass().getMethod("update", new Class[] { HqlHelper.class }).invoke(helperService, helper);
            // 更新转换状态及结果 --任务中的
            taskVideoFile.setIsComplete("1");
            taskVideoFile.setThumbnailPath(targetVideoFile.getThumbnailPath());
            taskVideoFile.setFlvPath(targetVideoFile.getFlvPath());
            taskVideoFile.setMp4Path(targetVideoFile.getMp4Path());
            taskVideoFile.setSwfPath(targetVideoFile.getSwfPath());
            taskVideoFile.setDuration(targetVideoFile.getDuration());
            taskVideoFile.setBitrate(targetVideoFile.getBitrate());
            taskVideoFile.setSize(targetVideoFile.getSize());

            //更新转换任务状态
            helper.resetQueryFrom(VideoConvertTask.class).update("convertStatus", ConvertStatus.OK)
                    .update("videoFileStr", JacksonUtil.toJackson(videoFilesTask)).update("convertPercent", "100.00%")
                    .eq("id", task.getId());
            helperService.update(helper);
        } catch (Exception e) {
            e.printStackTrace();
            // 异常时， 如果状态是转换中 则还原现场
            helper.resetQueryFrom(VideoConvertTask.class).update("convertStatus", ConvertStatus.NEW)
                    .eq("convertStatus", ConvertStatus.ING).eq("id", task.getId());
            helperService.update(helper);

            LOGGER.error("VideoConvertTask fail:ID=" + task.getId());
        }
    }

    public void execute(JobExecutionContext context) {
        HqlHelperService hqlHelperService = (HqlHelperService) SpringUtil.getBean("hqlHelperServiceImpl");
        HqlHelper helper = HqlHelper.queryFrom(DocConvertTask.class);

        if (hqlHelperService.getRecord(helper.fetch("id").eq("convertStatus", ConvertStatus.ING)) != null) {
            //context.setResult("发现处理中文档转换任务，任务终止...");
            context.setResult(JobExecutor.JOB_DO_NOTHING_RESULT);
            return;
        }

        if (hqlHelperService.getRecord(helper.resetQueryFrom(VideoConvertTask.class).fetch("id")
                .eq("convertStatus", ConvertStatus.ING)) != null) {
            //context.setResult("发现处理中视频转换任务，任务终止...");
            context.setResult(JobExecutor.JOB_DO_NOTHING_RESULT);
            return;
        }

        helper.resetQueryFrom(DocConvertTask.class).eq("convertStatus", ConvertStatus.NEW)
                .orderBy("createDate", OrderType.asc);
        DocConvertTask docConvertTask = hqlHelperService.getEntity(helper);
        if (docConvertTask != null) {
            doDocConvertTask(docConvertTask);
        } else {
            helper.resetQueryFrom(VideoConvertTask.class).eq("convertStatus", ConvertStatus.NEW)
                    .orderBy("createDate", OrderType.asc);
            VideoConvertTask videoConvertTask = hqlHelperService.getEntity(helper);
            if (videoConvertTask != null) {
                doVideoConvertTask(videoConvertTask);
            } else {
                // 没有需要转换的任务
                context.setResult(JobExecutor.JOB_DO_NOTHING_RESULT);
            }
        }
    }
}
