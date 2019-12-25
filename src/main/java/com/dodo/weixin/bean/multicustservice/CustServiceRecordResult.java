package com.dodo.weixin.bean.multicustservice;

import java.util.List;

import com.dodo.weixin.bean.ResultMsgBean;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class CustServiceRecordResult extends ResultMsgBean{
	private List<CustServiceRecord> recordlist;

	public List<CustServiceRecord> getRecordlist() {
		return recordlist;
	}

	public void setRecordlist(List<CustServiceRecord> recordlist) {
		this.recordlist = recordlist;
	}

	@Override
	public String toString() {
		return super.toString()+"\nCustServiceRecordResult [recordlist=" + recordlist + "]";
	}
}
