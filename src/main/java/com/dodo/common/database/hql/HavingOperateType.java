package com.dodo.common.database.hql;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public enum HavingOperateType{
	eq(" {0}=:{1} "),
	ne(" {0}<>:{1} "),
	gt(" {0}>:{1} "),
	lt(" {0}<:{1} "),
	ge(" {0}>=:{1} "),
	le(" {0}<=:{1} "),
	between(" {0} between :{1} and :{2} "),
	notBetween("{0} not between :{1} and :{2} "),
	in(" {0} in (:{1}) "),
	notIn(" {0} not in (:{1}) "),
	like(" {0} like :{1} "),
	ilike(" upper({0}) like upper(:{1}) "),
	notLike(" {0} not like :{1} "),
	notiLike(" upper({0}) not like upper(:{1}) "),
	lengthEq(" length({0})=:{1} "),
	lengthGe(" length({0})>=:{1} "),
	lengthGt(" length({0})>:{1} "),
	lengthLe(" length({0})<=:{1} "),
	lengthLt(" length({0})<:{1} "),
	lengthNe(" length({0})<>:{1} ");
	private HavingOperateType(String operator) {
		this.operator = operator;
	}
	private String operator;
	public String operator() {
		return operator;
	}
}