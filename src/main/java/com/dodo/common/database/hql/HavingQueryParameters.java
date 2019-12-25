package com.dodo.common.database.hql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
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
public class HavingQueryParameters {
	public enum BeginOperator{
		AND("and"),OR("or ");
		private String operator;
		private BeginOperator(String operator) {
			this.operator = operator;
		}
		public String getOperator() {
			return operator;
		}
	}
	private List<HavingQueryParameter> parameters = new LinkedList<HavingQueryParameter>();
	private Map<Integer,List<BeginOperator>> begin = new HashMap<Integer,List<BeginOperator>>();
	private Map<Integer,Integer> end = new HashMap<Integer,Integer>();
	private HavingQueryParameters(){
	}
	
	public void end(){
		List<BeginOperator> value = begin.get(parameters.size());
		if(value!=null){
			if(value.size()>1){
				value.remove(value.size()-1);
			}else{
				begin.remove(parameters.size());
			}
		}else {
			Integer endValue = end.get(parameters.size());
			if(endValue==null){
				endValue=0;
			}
			end.put(parameters.size(), endValue+1);
		}
	}
	public void begin(BeginOperator operator){
		List<BeginOperator> value = begin.get(parameters.size());
		if(value==null){
			value=new ArrayList<BeginOperator>();
		}
		value.add(operator);
		begin.put(parameters.size(), value);
	}
	
	public List<BeginOperator> beginStatus(Integer index){
		return begin.get(index);
	}
	
	public Integer endStatus(Integer index){
		return end.get(index);
	}
	
	public static HavingQueryParameters newInstance(){
		return new HavingQueryParameters();
	}
	
	public void clear(){
		parameters.clear();
		begin.clear();
		end.clear();
	}
	
	public void addQueryParameter(HavingQueryParameter parameter){
		if(parameter!=null){
			parameter.setSeqNum(parameters.size());
			parameters.add(parameter);
		}
	}
	public List<HavingQueryParameter> getParameters() {
		return parameters;
	}
	
}
