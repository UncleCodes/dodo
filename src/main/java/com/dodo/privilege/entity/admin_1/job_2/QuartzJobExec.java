package com.dodo.privilege.entity.admin_1.job_2;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;

import com.dodo.common.annotation.DodoCodeGenerator;
import com.dodo.common.annotation.action.DodoAction;
import com.dodo.common.annotation.action.DodoActionGenerator;
import com.dodo.common.annotation.dao.DodoDaoGenerator;
import com.dodo.common.annotation.field.DodoField;
import com.dodo.common.annotation.menu.DodoMenu;
import com.dodo.common.annotation.menu.DodoMenuLevel;
import com.dodo.common.annotation.right.DodoRight;
import com.dodo.common.annotation.service.DodoSrvGenerator;
import com.dodo.common.framework.entity.BaseEntity;
import com.dodo.privilege.enums.QuartzJobStatus;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
@Entity
@DynamicInsert
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DodoMenu(nameKey = "dodo.privilege.admin.menuNameKey", level = DodoMenuLevel.LEVEL1, sortSeq = 1)
@DodoMenu(nameKey = "dodo.privilege.admin.job.menuNameKey", level = DodoMenuLevel.LEVEL2, sortSeq = 2)
@DodoMenu(nameKey = "dodo.privilege.admin.job.QuartzJobExec.menuNameKey", level = DodoMenuLevel.LEVEL3, sortSeq = 2)
@DodoRight(nameKey = "dodo.privilege.admin.job.QuartzJobExec.entityKey")
@DodoCodeGenerator(daoGenerator = @DodoDaoGenerator, srvGenerator = @DodoSrvGenerator, actGenerator = @DodoActionGenerator(actions = {
        DodoAction.VIEW, DodoAction.CHART, DodoAction.EXPORT, DodoAction.DELETE }))
public class QuartzJobExec extends BaseEntity implements java.io.Serializable {
    private static final long serialVersionUID = -5609193860589402642L;

    @DodoField(sortSeq = 0, nameKey = "dodo.privilege.admin.job.QuartzJobExec.namekey.quartzJob", queryOnList = true)
    private QuartzJob         quartzJob;

    @DodoField(sortSeq = 1, nameKey = "dodo.privilege.admin.job.QuartzJobExec.namekey.jobStatus", queryOnList = true)
    private QuartzJobStatus   jobStatus;

    @DodoField(sortSeq = 2, nameKey = "dodo.privilege.admin.job.QuartzJobExec.namekey.initTime")
    private Date              initTime;

    @DodoField(sortSeq = 3, nameKey = "dodo.privilege.admin.job.QuartzJobExec.namekey.prevTime")
    private Date              prevTime;

    @DodoField(sortSeq = 4, nameKey = "dodo.privilege.admin.job.QuartzJobExec.namekey.beginTime")
    private Date              beginTime;

    @DodoField(sortSeq = 5, nameKey = "dodo.privilege.admin.job.QuartzJobExec.namekey.endTime")
    private Date              endTime;

    @DodoField(sortSeq = 6, nameKey = "dodo.privilege.admin.job.QuartzJobExec.namekey.nextTime")
    private Date              nextTime;

    @DodoField(sortSeq = 7, nameKey = "dodo.privilege.admin.job.QuartzJobExec.namekey.excuteTime")
    private String            excuteTime;

    @DodoField(sortSeq = 8, nameKey = "dodo.privilege.admin.job.QuartzJobExec.namekey.jobExecLog")
    private String            jobExecLog;

    @DodoField(sortSeq = 9, nameKey = "dodo.privilege.admin.job.QuartzJobExec.namekey.jobExecException", isTextArea = true, listable = false)
    private String            jobExecException;

    @OneToOne
    public QuartzJob getQuartzJob() {
        return quartzJob;
    }

    @Lob
    public String getJobExecLog() {
        return jobExecLog;
    }

    public void setQuartzJob(QuartzJob quartzJob) {
        this.quartzJob = quartzJob;
    }

    public void setJobExecLog(String jobExecLog) {
        this.jobExecLog = jobExecLog;
    }

    public Date getEndTime() {
        return endTime;
    }

    @Column(length = 12)
    public String getExcuteTime() {
        return excuteTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setExcuteTime(String excuteTime) {
        this.excuteTime = excuteTime;
    }

    @Column(length = 3)
    @Convert(converter = QuartzJobStatus.Converter.class)
    public QuartzJobStatus getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(QuartzJobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getPrevTime() {
        return prevTime;
    }

    public Date getNextTime() {
        return nextTime;
    }

    @Lob
    public String getJobExecException() {
        return jobExecException;
    }

    public void setPrevTime(Date prevTime) {
        this.prevTime = prevTime;
    }

    public void setNextTime(Date nextTime) {
        this.nextTime = nextTime;
    }

    public void setJobExecException(String jobExecException) {
        this.jobExecException = jobExecException;
    }

    public Date getInitTime() {
        return initTime;
    }

    public void setInitTime(Date initTime) {
        this.initTime = initTime;
    }
}
