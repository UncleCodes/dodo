package com.dodo.common.database.hql;

import java.text.MessageFormat;

import com.dodo.common.database.hql.HqlHelper.MatchType;
import com.dodo.utils.CommonUtil;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class HavingQueryParameter {
	private GroupByFunction groupByField;
	private Object[] propertyValues;
	private HavingOperateType operateType;
	private String placeholder1;
	private String placeholder2;
	private Integer seqNum;
	private MatchType matchType;
	public HavingQueryParameter(GroupByFunction groupByField, HavingOperateType operateType, Object... propertyValues) {
		super();
		this.groupByField = groupByField;
		this.operateType = operateType;
		if(operateType==HavingOperateType.in||operateType==HavingOperateType.notIn){
			if(propertyValues.length==1&&CommonUtil.isCollectionType(propertyValues[0].getClass())){
				this.propertyValues = new Object[]{propertyValues[0]};	
			}else{
				this.propertyValues = new Object[]{propertyValues};
			}
		}else{
			this.propertyValues = propertyValues;
		}
	}
	
	public HavingQueryParameter(GroupByFunction groupByField, HavingOperateType operateType,
			MatchType matchType,String propertyValue) {
		super();
		this.groupByField = groupByField;
		this.operateType = operateType;
		this.matchType = matchType;
		this.propertyValues = new Object[]{propertyValue};
	}
	
	public void setSeqNum(Integer seqNum) {
		this.seqNum = seqNum;
		StringBuilder sbBuilder = new StringBuilder("Having");
		sbBuilder.append(HqlHelper.whereLast);
		sbBuilder.append(seqNum);
		placeholder1 = sbBuilder.toString();
		if(operateType==HavingOperateType.between||operateType==HavingOperateType.notBetween){
			placeholder2 = placeholder1+HqlHelper.betweenHign;
			placeholder1 = placeholder1+HqlHelper.betweenLow;
		}
	}
	
	public String toHqlString(){
		return MessageFormat.format(operateType.operator(),groupByField.toHqlStringHaving(),placeholder1,placeholder2);
	}

	public Integer getSeqNum() {
		return seqNum;
	}
	
	public String getPlaceholder1() {
		return placeholder1;
	}

	public String getPlaceholder2() {
		return placeholder2;
	}

	public Object[] getPropertyValues() {
		return propertyValues;
	}

	public HavingOperateType getOperateType() {
		return operateType;
	}
	
	public MatchType getMatchType() {
		return matchType;
	}
}
