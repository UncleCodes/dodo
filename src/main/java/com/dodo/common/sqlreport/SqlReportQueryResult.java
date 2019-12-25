package com.dodo.common.sqlreport;

import java.util.List;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class SqlReportQueryResult {
	private List<SqlReportQueryField> queryFields;
	private List<List<String>> queryDatas;
	private int pageNumber;
	private int totalCount;
	private int pageSize;
	private Boolean isSuccess = Boolean.TRUE;
	private String message = "Exec Ok...";
	private Long timeMills;
	public List<SqlReportQueryField> getQueryFields() {
		return queryFields;
	}
	public List<List<String>> getQueryDatas() {
		return queryDatas;
	}
	public void setQueryFields(List<SqlReportQueryField> queryFields) {
		this.queryFields = queryFields;
	}
	public void setQueryDatas(List<List<String>> queryDatas) {
		this.queryDatas = queryDatas;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public Boolean getIsSuccess() {
		return isSuccess;
	}
	public String getMessage() {
		return message;
	}
	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "SqlReportQueryResult [queryFields=" + queryFields
				+ ", queryDatas=" + queryDatas + ", pageNumber=" + pageNumber
				+ ", totalCount=" + totalCount + ", pageSize=" + pageSize
				+ ", isSuccess=" + isSuccess + ", message=" + message + "]";
	}
	public Long getTimeMills() {
		return timeMills;
	}
	public void setTimeMills(Long timeMills) {
		this.timeMills = timeMills;
	}
}
