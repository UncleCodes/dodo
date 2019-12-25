package com.dodo.common.database.hql;
/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public enum OperateType{
	eq(" {0}=:{1} ",false,false),
	ne(" {0}<>:{1} ",false,false),
	gt(" {0}>:{1} ",false,false),
	lt(" {0}<:{1} ",false,false),
	ge(" {0}>=:{1} ",false,false),
	le(" {0}<=:{1} ",false,false),
	between(" {0} between :{1} and :{2} ",false,false),
	notBetween("{0} not between :{1} and :{2} ",false,false),
	in(" {0} in (:{1}) ",false,false),
	notIn(" {0} not in (:{1}) ",false,false),
	isNull(" {0} is null ",true,false),
	isNotNull(" {0} is not null ",true,false),
	isEmpty(" {0} is empty ",true,true),
	isNotEmpty(" {0} is not empty ",true,true),
	like(" {0} like :{1} ",false,false),
	ilike(" upper({0}) like upper(:{1}) ",false,false),
	notLike(" {0} not like :{1} ",false,false),
	notiLike(" upper({0}) not like upper(:{1}) ",false,false),
	sizeEq(" size({0})=:{1} ",false,true),
	sizeGe(" size({0})>=:{1} ",false,true),
	sizeGt(" size({0})>:{1} ",false,true),
	sizeLe(" size({0})<=:{1} ",false,true),
	sizeLt(" size({0})<:{1} ",false,true),
	sizeNe(" size({0})<>:{1} ",false,true),
	lengthEq(" length({0})=:{1} ",false,false),
	lengthGe(" length({0})>=:{1} ",false,false),
	lengthGt(" length({0})>:{1} ",false,false),
	lengthLe(" length({0})<=:{1} ",false,false),
	lengthLt(" length({0})<:{1} ",false,false),
	lengthNe(" length({0})<>:{1} ",false,false),	
	eqProperty(" {0}={1} ",true,false),
	neProperty(" {0}<>{1} ",true,false),
	gtProperty(" {0}>{1} ",true,false),
	ltProperty(" {0}<{1} ",true,false),
	geProperty(" {0}>={1} ",true,false),
	leProperty(" {0}<={1} ",true,false),
	lengthEqProperty(" length({0})=length({1}) ",true,false),
	lengthGeProperty(" length({0})>=length({1}) ",true,false),
	lengthGtProperty(" length({0})>length({1}) ",true,false),
	lengthLeProperty(" length({0})<=length({1}) ",true,false),
	lengthLtProperty(" length({0})<length({1}) ",true,false),
	lengthNeProperty(" length({0})<>length({1}) ",true,false);
	private OperateType(String operator,boolean isPropertyCompare,boolean isCollectionOperator) {
		this.operator = operator;
		this.isPropertyCompare = isPropertyCompare;
		this.isCollectionOperator = isCollectionOperator;
	}
	private String operator;
	private boolean isPropertyCompare;
	private boolean isCollectionOperator;
	public String operator() {
		return operator;
	}
	public boolean isPropertyCompare() {
		return isPropertyCompare;
	}
	
	public boolean isCollectionOperator() {
		return isCollectionOperator;
	}
}