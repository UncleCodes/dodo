package com.dodo.common.sqlreport;

import org.springframework.jdbc.core.PreparedStatementSetter;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class SqlReportLimit {
	private PreparedStatementSetter preparedStatementSetter;
	private String limitSql;
	private int pageNumber;
	private int pageSize;
	public PreparedStatementSetter getPreparedStatementSetter() {
		return preparedStatementSetter;
	}
	public String getLimitSql() {
		return limitSql;
	}
	public void setPreparedStatementSetter(
			PreparedStatementSetter preparedStatementSetter) {
		this.preparedStatementSetter = preparedStatementSetter;
	}
	public void setLimitSql(String limitSql) {
		this.limitSql = limitSql;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
