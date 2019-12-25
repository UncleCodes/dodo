package com.dodo.common.database.hql;

import java.io.Serializable;

import com.dodo.common.database.hql.HqlHelper.JoinType;
/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class QueryJoin implements Serializable{
	private static final long serialVersionUID = -3543792764904561193L;
	private String domainName;
	private String domainAlias;
	private JoinType joinType;
	private String fieldAlias;
	public QueryJoin(String domainName, String domainAlias, JoinType joinType,String fieldAlias) {
		super();
		this.domainName = domainName;
		this.domainAlias = domainAlias;
		this.joinType = joinType;
		this.fieldAlias =fieldAlias;
	}
	public String getDomainName() {
		return domainName;
	}
	public String getDomainAlias() {
		return domainAlias;
	}
	public JoinType getJoinType() {
		return joinType;
	}
	public String getFieldAlias() {
		return fieldAlias;
	}
}
