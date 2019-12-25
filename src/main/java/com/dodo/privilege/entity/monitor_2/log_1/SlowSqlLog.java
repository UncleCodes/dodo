package com.dodo.privilege.entity.monitor_2.log_1;

import javax.persistence.Entity;
import javax.persistence.Lob;

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
@DodoMenu(nameKey = "dodo.privilege.monitor.menuNameKey", level = DodoMenuLevel.LEVEL1, sortSeq = 2)
@DodoMenu(nameKey = "dodo.privilege.monitor.log.menuNameKey", level = DodoMenuLevel.LEVEL2, sortSeq = 1)
@DodoMenu(nameKey = "dodo.privilege.monitor.log.SlowSqlLog.menuNameKey", level = DodoMenuLevel.LEVEL3, sortSeq = 4)
@DodoRight(nameKey = "dodo.privilege.monitor.log.SlowSqlLog.entityKey")
@DodoCodeGenerator(daoGenerator = @DodoDaoGenerator, srvGenerator = @DodoSrvGenerator, actGenerator = @DodoActionGenerator(actions = {
        DodoAction.VIEW, DodoAction.CHART, DodoAction.EXPORT, DodoAction.DELETE }))
public class SlowSqlLog extends BaseEntity implements java.io.Serializable {
    private static final long serialVersionUID = -3058263835482460716L;

    @DodoField(sortSeq = 1, nameKey = "dodo.privilege.monitor.log.SlowSqlLog.namekey.executeMillis", addable = false, editable = false, queryOnList = true)
    private Long              executeMillis;

    @DodoField(sortSeq = 2, nameKey = "dodo.privilege.monitor.log.SlowSqlLog.namekey.compareMillis", addable = false, editable = false, queryOnList = true)
    private Long              compareMillis;

    @DodoField(sortSeq = 3, nameKey = "dodo.privilege.monitor.log.SlowSqlLog.namekey.executeSql", addable = false, editable = false, isTextArea = true)
    private String            executeSql;

    @DodoField(sortSeq = 4, nameKey = "dodo.privilege.monitor.log.SlowSqlLog.namekey.slowParameters", addable = false, editable = false, isTextArea = true)
    private String            slowParameters;

    @Lob
    public String getExecuteSql() {
        return executeSql;
    }

    @Lob
    public String getSlowParameters() {
        return slowParameters;
    }

    public Long getExecuteMillis() {
        return executeMillis;
    }

    public Long getCompareMillis() {
        return compareMillis;
    }

    public void setExecuteSql(String executeSql) {
        this.executeSql = executeSql;
    }

    public void setSlowParameters(String slowParameters) {
        this.slowParameters = slowParameters;
    }

    public void setExecuteMillis(Long executeMillis) {
        this.executeMillis = executeMillis;
    }

    public void setCompareMillis(Long compareMillis) {
        this.compareMillis = compareMillis;
    }
}
