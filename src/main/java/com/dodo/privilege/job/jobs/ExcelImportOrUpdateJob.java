package com.dodo.privilege.job.jobs;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

import com.dodo.common.database.data.Ite;
import com.dodo.common.database.data.Record;
import com.dodo.common.database.data.Records;
import com.dodo.common.database.hql.HqlHelper;
import com.dodo.common.framework.bean.file.DodoFile;
import com.dodo.common.framework.bean.pager.PageModel.OrderType;
import com.dodo.common.framework.service.HqlHelperService;
import com.dodo.privilege.entity.admin_1.base_1.Admin;
import com.dodo.privilege.entity.admin_1.base_1.Role;
import com.dodo.privilege.entity.admin_1.data_4.ExcelImportInfo;
import com.dodo.privilege.entity.admin_1.data_4.ExcelUpdateInfo;
import com.dodo.privilege.enums.ExcelImportStatus;
import com.dodo.privilege.enums.ExcelUpdateStatus;
import com.dodo.privilege.job.jobexec.JobExecutor;
import com.dodo.utils.JacksonUtil;
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
public class ExcelImportOrUpdateJob implements Job {
    public void execute(JobExecutionContext context) {
        HqlHelperService hqlHelperService = (HqlHelperService) SpringUtil.getBean("hqlHelperServiceImpl");
        HqlHelper helper = HqlHelper.queryFrom(ExcelImportInfo.class);

        if (hqlHelperService.getRecord(helper.fetch("id").eq("importStatus", ExcelImportStatus.ING)) != null) {
            //context.setResult("发现处理中导入任务，任务终止...");
            context.setResult(JobExecutor.JOB_DO_NOTHING_RESULT);
            return;
        }

        if (hqlHelperService.getRecord(helper.resetQueryFrom(ExcelUpdateInfo.class).fetch("id")
                .eq("updateStatus", ExcelUpdateStatus.ING)) != null) {
            //context.setResult("发现处理中更新任务，任务终止...");
            context.setResult(JobExecutor.JOB_DO_NOTHING_RESULT);
            return;
        }

        helper.resetQueryFrom(ExcelImportInfo.class).fetch("id", "triggerName", "importFile", "admin.id")
                .eq("importStatus", ExcelImportStatus.NEW).orderBy("createDate", OrderType.asc);
        Record oneImport = hqlHelperService.getRecord(helper);
        if (oneImport != null) {
            String triggerName = oneImport.get("triggerName");
            Serializable id = oneImport.get("id");
            String importFilePath = null;
            try {
                importFilePath = JacksonUtil.toObject((String) oneImport.get("importFile"), DodoFile[].class)[0]
                        .getFilePath();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            Object trigger = SpringUtil.getBackBean(triggerName);
            Object triggerExecutor = null;
            Admin principal = hqlHelperService.get((Serializable) oneImport.get("admin_id"), Admin.class);
            initPrincipalRight(principal, hqlHelperService);
            try {
                triggerExecutor = Class.forName(trigger.getClass().getName() + "$ExcelDataEngine").getConstructors()[0]
                        .newInstance(trigger);
                triggerExecutor
                        .getClass()
                        .getDeclaredMethod("executeImportJdbcBatch",
                                new Class[] { id.getClass(), String.class, principal.getClass() })
                        .invoke(triggerExecutor, new Object[] { id, importFilePath, principal });
                context.setResult("触发器返回：数据导入成功...");
            } catch (Exception e) {
                e.printStackTrace();
                context.setResult("触发器执行异常，任务终止...");
                throw new RuntimeException(e);
            }
        } else {
            helper.resetQueryFrom(ExcelUpdateInfo.class).fetch("id", "triggerName", "updateFile", "admin.id")
                    .eq("updateStatus", ExcelUpdateStatus.NEW).orderBy("createDate", OrderType.asc);
            Record oneUpdate = hqlHelperService.getRecord(helper);
            if (oneUpdate != null) {
                String triggerName = oneUpdate.get("triggerName");
                Serializable id = oneUpdate.get("id");
                String updateFilePath = null;
                try {
                    updateFilePath = JacksonUtil.toObject((String) oneUpdate.get("updateFile"), DodoFile[].class)[0]
                            .getFilePath();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                Object trigger = SpringUtil.getBackBean(triggerName);
                Object triggerExecutor = null;
                Admin principal = hqlHelperService.get((Serializable) oneUpdate.get("admin_id"), Admin.class);
                initPrincipalRight(principal, hqlHelperService);
                try {
                    triggerExecutor = Class.forName(trigger.getClass().getName() + "$ExcelDataEngine")
                            .getConstructors()[0].newInstance(trigger);
                    triggerExecutor
                            .getClass()
                            .getDeclaredMethod("executeUpdate",
                                    new Class[] { id.getClass(), String.class, principal.getClass() })
                            .invoke(triggerExecutor, new Object[] { id, updateFilePath, principal });
                    context.setResult("触发器返回：数据导入成功...");
                } catch (Exception e) {
                    e.printStackTrace();
                    context.setResult("触发器执行异常，任务终止...");
                    throw new RuntimeException(e);
                }
            } else {
                //context.setResult("无导入请求，任务完成...");
                context.setResult(JobExecutor.JOB_DO_NOTHING_RESULT);
                return;
            }
        }
    }

    private void initPrincipalRight(Admin principal, HqlHelperService hqlHelperService) {
        HqlHelper helper = HqlHelper.queryFrom(Admin.class);
        principal.setIsSystemAdmin(Boolean.FALSE);
        Set<String> haveFieldRights = new HashSet<String>();

        helper.fetch("roleSet.id").eq("id", principal.getId());
        Record adminTemp = hqlHelperService.getRecord(helper);

        List<Map<String, Object>> roleSet = adminTemp.get("roleSet");
        List<Object> roleIds = new ArrayList<Object>(roleSet.size());
        for (Map<String, Object> role : roleSet) {
            roleIds.add(role.get("id"));
        }
        helper.resetQueryFrom(Role.class).fetch("allFieldRights.rightCode", "isSystem").in("id", roleIds);
        Records roles = hqlHelperService.getRecords(helper, Boolean.FALSE);
        List<Map<String, Object>> tempListMap = null;
        Record record = null;
        Ite ite = roles.iterator();
        while (ite.hasNext()) {
            record = ite.next();
            tempListMap = (record.get("allFieldRights"));
            for (Map<String, Object> map : tempListMap) {
                haveFieldRights.add(map.get("rightCode").toString());
            }
            if ((Boolean) record.get("isSystem")) {
                principal.setIsSystemAdmin(Boolean.TRUE);
            }
        }

        principal.setFieldRightHaveCode(haveFieldRights);
    }
}
