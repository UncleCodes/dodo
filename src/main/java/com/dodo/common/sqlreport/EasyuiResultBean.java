package com.dodo.common.sqlreport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class EasyuiResultBean {
	private int total;
	private List<Map<String,String>> rows = new ArrayList<Map<String,String>>(20);
	public int getTotal() {
		return total;
	}
	public List<Map<String, String>> getRows() {
		return rows;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public void setRows(List<Map<String, String>> rows) {
		this.rows = rows;
	}
	public void addRow(Map<String, String> row){
		rows.add(row);
	}
}
