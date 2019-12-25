package com.dodo.common.database.hql;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public enum GroupByType {
	AVG("avg({0}.{1}) as {2}","avg({0}.{1})"),
	COUNT("count({0}.{1}) as {2}","count({0}.{1})"),
	COUNTDISTINCT("count(distinct {0}.{1}) as {2}","count(distinct {0}.{1})"),
	MAX("max({0}.{1}) as {2}","max({0}.{1})"),
	MIN("min({0}.{1}) as {2}","min({0}.{1})"),
	SUM("sum({0}.{1}) as {2}","sum({0}.{1})");
	private String queryOperator;
	private String havingOperator;

	private GroupByType(String queryOperator,String havingOperator) {
		this.queryOperator = queryOperator;
		this.havingOperator = havingOperator;
	}

	public String getQueryOperator() {
		return queryOperator;
	}
	
	public String getHavingOperator() {
		return havingOperator;
	}
}
