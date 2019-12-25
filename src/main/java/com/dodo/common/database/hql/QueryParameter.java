package com.dodo.common.database.hql;

import java.text.MessageFormat;

import org.apache.commons.lang3.StringUtils;

import com.dodo.common.database.hql.HqlHelper.MatchType;
import com.dodo.utils.CommonUtil;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class QueryParameter {
    private String      propertyName;
    private String      propertyName1;
    private Object[]    propertyValues;
    private OperateType operateType;
    private String      placeholder1;
    private String      placeholder2;
    private Integer     seqNum;
    private MatchType   matchType;
    private String      tableAlias1;
    private String      tableAlias2;

    public QueryParameter(String tableAlias, String propertyName, OperateType operateType, Object... propertyValues) {
        super();
        this.tableAlias1 = tableAlias;
        this.propertyName = propertyName;
        this.operateType = operateType;
        if (operateType == OperateType.in || operateType == OperateType.notIn) {
            if (propertyValues.length == 1 && CommonUtil.isCollectionType(propertyValues[0].getClass())) {
                this.propertyValues = new Object[] { propertyValues[0] };
            } else {
                this.propertyValues = new Object[] { propertyValues };
            }
        } else {
            this.propertyValues = propertyValues;
        }
    }

    public QueryParameter(String tableAlias, String propertyName, OperateType operateType, MatchType matchType,
            String propertyValue) {
        super();
        this.tableAlias1 = tableAlias;
        this.propertyName = propertyName;
        this.operateType = operateType;
        this.matchType = matchType;
        this.propertyValues = new Object[] { propertyValue };
    }

    // property compare
    public QueryParameter(String tableAlias, String propertyName, OperateType operateType, String otherTableAlias,
            String propertyName1) {
        super();
        this.propertyName = propertyName;
        this.propertyName1 = propertyName1;
        this.operateType = operateType;
        this.tableAlias1 = tableAlias;
        this.tableAlias2 = otherTableAlias;
    }

    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
        if (operateType.isPropertyCompare()) {
            return;
        }
        StringBuilder sbBuilder = new StringBuilder(propertyName.replaceAll("\\.", "_"));
        sbBuilder.append(HqlHelper.whereLast);
        sbBuilder.append(seqNum);
        placeholder1 = sbBuilder.toString();
        if (operateType == OperateType.between || operateType == OperateType.notBetween) {
            placeholder2 = placeholder1 + HqlHelper.betweenHign;
            placeholder1 = placeholder1 + HqlHelper.betweenLow;
        }
    }

    public String toHqlString() {
        StringBuilder sbBuilder = new StringBuilder();
        if (operateType.isPropertyCompare()) {
            placeholder1 = sbBuilder.append(tableAlias2).append(".").append(propertyName1).toString();
            return MessageFormat.format(operateType.operator(),
                    sbBuilder.delete(0, sbBuilder.length()).append(tableAlias1).append(".").append(propertyName),
                    placeholder1, placeholder2);
        }
        return MessageFormat.format(operateType.operator(),
                sbBuilder.append(tableAlias1).append(".").append(propertyName), placeholder1, placeholder2);
    }

    public Integer getSeqNum() {
        return seqNum;
    }

    public String getPlaceholder1() {
        return placeholder1;
    }

    public String getPlaceholder2() {
        return placeholder2;
    }

    public Object[] getPropertyValues() {
        return propertyValues;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public OperateType getOperateType() {
        return operateType;
    }

    public boolean isCollectionOperator() {
        return operateType.isCollectionOperator();
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public String getTableAlias1() {
        return tableAlias1;
    }

    public String getPropertyName1() {
        return propertyName1;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public void setPropertyName1(String propertyName1) {
        this.propertyName1 = propertyName1;
    }

    public String getTableAlias2() {
        return tableAlias2;
    }

    public void toMapPropertyName() {
        propertyName = new StringBuilder().append(StringUtils.substringBefore(propertyName, ".")).append("['")
                .append(StringUtils.substringAfter(propertyName, ".")).append("']").toString();
    }

    public void toMapPropertyName1() {
        propertyName1 = new StringBuilder().append(StringUtils.substringBefore(propertyName1, ".")).append("['")
                .append(StringUtils.substringAfter(propertyName1, ".")).append("']").toString();
    }
}
