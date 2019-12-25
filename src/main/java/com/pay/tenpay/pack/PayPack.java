package com.pay.tenpay.pack;

import java.math.BigDecimal;

import com.pay.PayBusiType;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class PayPack {
	private PayBusiType payBusiType;
	private String out_trade_no;
	private BigDecimal total_fee;
	private String remark;
	private String notifyUrl;
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	public BigDecimal getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(BigDecimal total_fee) {
		this.total_fee = total_fee;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public PayBusiType getPayBusiType() {
		return payBusiType;
	}
	public void setPayBusiType(PayBusiType payBusiType) {
		this.payBusiType = payBusiType;
	}
}
