package com.dodo.common.database.hql;

import java.text.MessageFormat;

import com.dodo.common.framework.bean.pager.PageModel.OrderType;
/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class GroupByFunction {
	private GroupByType groupByType;
	private String tableAlias;
	private String fieldName;
	private String fieldAlias;
	private OrderType orderType;
	public GroupByFunction(GroupByType groupByType, String tableAlias,
			String fieldName,String fieldAlias) {
		super();
		this.groupByType = groupByType;
		this.tableAlias = tableAlias;
		this.fieldName = fieldName;
		this.fieldAlias = fieldAlias;
	}
	public GroupByType getGroupByType() {
		return groupByType;
	}
	public String getTableAlias() {
		return tableAlias;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setGroupByType(GroupByType groupByType) {
		this.groupByType = groupByType;
	}
	public void setTableAlias(String tableAlias) {
		this.tableAlias = tableAlias;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public String getFieldAlias() {
		return fieldAlias;
	}
	public void setFieldAlias(String fieldAlias) {
		this.fieldAlias = fieldAlias;
	}
 	public String toHqlString(){
		return MessageFormat.format(groupByType.getQueryOperator(),tableAlias,fieldName,fieldAlias);
	}
	
	public String toHqlStringHaving(){
		return MessageFormat.format(groupByType.getHavingOperator(),tableAlias,fieldName);
 	}
	public String toHqlStringOrderBy(){
		return orderType==null?null:new StringBuilder(fieldAlias).append(" ").append(orderType.name()).toString();
	}
	
	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}
}
