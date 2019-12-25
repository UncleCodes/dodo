package com.dodo.common.database.hql;

import org.apache.commons.lang3.StringUtils;

import com.dodo.common.framework.bean.pager.PageModel.OrderType;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class OrderByEntry {
    private String    fieldName;
    private OrderType orderType;
    private String    tableAlias;

    public OrderByEntry(String tableAlias, String fieldName, OrderType orderType) {
        super();
        this.fieldName = fieldName.trim();
        this.orderType = orderType;
        this.tableAlias = tableAlias.trim();
    }

    public String getFieldName() {
        return fieldName;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public String getTableAlias() {
        return tableAlias;
    }

    public String toHqlString() {
        StringBuilder sBuilder = new StringBuilder("");
        sBuilder.append(tableAlias);
        if (StringUtils.isNotBlank(tableAlias)) {
            sBuilder.append(".");
        }
        sBuilder.append(fieldName).append(" ").append(orderType.name());
        return sBuilder.toString();
    }

    public void toMapFieldName() {
        fieldName = new StringBuilder().append(StringUtils.substringBefore(fieldName, ".")).append("['")
                .append(StringUtils.substringAfter(fieldName, ".")).append("']").toString();
    }
}
