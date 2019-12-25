package com.dodo.privilege.entity.admin_1.job_2;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;

import com.dodo.common.annotation.DodoCodeGenerator;
import com.dodo.common.annotation.action.DodoAction;
import com.dodo.common.annotation.action.DodoActionGenerator;
import com.dodo.common.annotation.dao.DodoDaoGenerator;
import com.dodo.common.annotation.field.DodoField;
import com.dodo.common.annotation.field.DodoShowColumn;
import com.dodo.common.annotation.menu.DodoMenu;
import com.dodo.common.annotation.menu.DodoMenuLevel;
import com.dodo.common.annotation.right.DodoRight;
import com.dodo.common.annotation.service.DodoSrvGenerator;
import com.dodo.common.framework.entity.BaseEntity;

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
@DodoMenu(nameKey = "dodo.privilege.admin.job.QuartzJob.menuNameKey", level = DodoMenuLevel.LEVEL3, sortSeq = 1)
@DodoRight(nameKey = "dodo.privilege.admin.job.QuartzJob.entityKey")
@DodoCodeGenerator(daoGenerator = @DodoDaoGenerator, srvGenerator = @DodoSrvGenerator, actGenerator = @DodoActionGenerator(actions = {
        DodoAction.VIEW, DodoAction.CHART, DodoAction.EXPORT }))
public class QuartzJob extends BaseEntity implements java.io.Serializable {
    private static final long serialVersionUID = 4416453129912851423L;

    @DodoField(sortSeq = 0, nameKey = "dodo.privilege.admin.job.QuartzJob.namekey.jobName", queryOnList = true)
    @DodoShowColumn(sortSeq = 0)
    private String            jobName;

    @DodoField(sortSeq = 1, nameKey = "dodo.privilege.admin.job.QuartzJob.namekey.jobClass")
    private String            jobClass;

    @DodoField(sortSeq = 2, nameKey = "dodo.privilege.admin.job.QuartzJob.namekey.jobDesc", isTextArea = true)
    private String            jobDesc;

    @DodoField(sortSeq = 3, nameKey = "dodo.privilege.admin.job.QuartzJob.namekey.startTime", isnullable = false)
    private Date              startTime;

    @DodoField(sortSeq = 4, nameKey = "dodo.privilege.admin.job.QuartzJob.namekey.endTime")
    private Date              endTime;

    @DodoField(sortSeq = 5, nameKey = "dodo.privilege.admin.job.QuartzJob.namekey.repeatCount", isDigits = true, min = "-1", max = Integer.MAX_VALUE
            + "", infoTipKey = "dodo.privilege.admin.job.QuartzJob.infoTip.repeatCount")
    private int               repeatCount;

    @DodoField(sortSeq = 6, nameKey = "dodo.privilege.admin.job.QuartzJob.namekey.repeatInterval", isDigits = true, min = "0", max = Integer.MAX_VALUE
            + "", infoTipKey = "dodo.privilege.admin.job.QuartzJob.infoTip.repeatInterval")
    private int               repeatInterval;

    @DodoField(sortSeq = 7, nameKey = "dodo.privilege.admin.job.QuartzJob.namekey.inUse")
    private Boolean           inUse;

    @DodoField(sortSeq = 8, nameKey = "dodo.privilege.admin.job.QuartzJob.namekey.isAutoExec")
    private Boolean           isAutoExec;

    @Override
    public void onSave() {
        super.onSave();
        if (inUse == null) {
            inUse = Boolean.FALSE;
        }
        if (isAutoExec == null) {
            isAutoExec = Boolean.FALSE;
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (inUse == null) {
            inUse = Boolean.FALSE;
        }
        if (isAutoExec == null) {
            isAutoExec = Boolean.FALSE;
        }
    }

    public Boolean getIsAutoExec() {
        return isAutoExec;
    }

    public void setIsAutoExec(Boolean isAutoExec) {
        this.isAutoExec = isAutoExec;
    }

    @Column(length = 64)
    public String getJobName() {
        return jobName;
    }

    public String getJobClass() {
        return jobClass;
    }

    @Column(length = 128)
    public String getJobDesc() {
        return jobDesc;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public int getRepeatInterval() {
        return repeatInterval;
    }

    public Boolean getInUse() {
        return inUse;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    public void setRepeatInterval(int repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    public void setInUse(Boolean inUse) {
        this.inUse = inUse;
    }
}
