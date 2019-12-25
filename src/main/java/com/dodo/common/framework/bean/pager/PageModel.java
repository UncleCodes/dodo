package com.dodo.common.framework.bean.pager;

import java.io.Serializable;
import java.util.List;

import com.dodo.common.database.data.Records;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class PageModel implements Serializable{
	private static final long serialVersionUID = -2354735761626077868L;

	public enum OrderType {
		asc, desc
	}
	public static final Integer MAX_PAGE_SIZE = 200; 
	public static final Integer DEFAULT_PAGE_SIZE = 20;
	private Integer pageNumber; 
	private Integer pageSize; 
	private Integer pageCount = 0;
	private String orderBy = "createDate"; 
	private OrderType orderType = OrderType.desc; 
	private Integer totalCount = 0; 
	private List<?> result;
	private Records records;

	public PageModel() {
		super();
	}

	public PageModel(Integer pageSize) {
		super();
		setPageSize(pageSize);
	}
	public Integer getPageCount() {
		getPageSize();
		this.pageCount = (this.totalCount+this.pageSize-1)/this.pageSize;
		return this.pageCount;
	}

	public Integer getPageNumber() {
		if(this.pageNumber==null||this.pageNumber<1){
			this.pageNumber = 1;
		}
		return this.pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		if (pageNumber==null||pageNumber < 1) {
			pageNumber = 1;
		}
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		if(this.pageSize==null||this.pageSize==0){
			this.pageSize = DEFAULT_PAGE_SIZE;
		}
		return this.pageSize;
	}

	public void setPageSize(Integer pageSize) {
		if (pageSize < 1) {
			pageSize = 1;
		} else if (pageSize > MAX_PAGE_SIZE) {
			pageSize = MAX_PAGE_SIZE;
		}
		this.pageSize = pageSize;
	}

	public String getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public OrderType getOrder() {
		return this.orderType;
	}

	public void setOrder(OrderType orderType) {
		this.orderType = orderType;
	}
	
	// for dwz 
	public void setPageNum(Integer pageNum) {
		setPageNumber(pageNum);
	}
	
	// for dwz 
	public void setNumPerPage(Integer numPerPage) {
		setPageSize(numPerPage);
	}
	
	
	//for jqueryui
	public void setPage(Integer page) {
		setPageNumber(page);
	}
	
	// for jqueryui 
	public void setRows(Integer rows) {
		setPageSize(rows);
	}
	
	// for jqueryui 
	public void setSort(String sort) {
		this.orderBy = sort;
	}
	
	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public List<?> getResult() {
		return this.records==null?this.result:this.records.getRawData();
	}

	public void setResult(List<?> result) {
		this.result = result;
	}
	
	public Records getRecords() {
		return records;
	}
	
	public void setRecords(Records records) {
		this.records = records;
	}

	@Override
	public String toString() {
		return "PageModel [pageNumber=" + pageNumber + ", pageSize=" + pageSize
				+ ", pageCount=" + pageCount 
				+ ", orderBy=" + orderBy + ", orderType=" + orderType
				+ ", totalCount=" + totalCount + "]";
	}
}