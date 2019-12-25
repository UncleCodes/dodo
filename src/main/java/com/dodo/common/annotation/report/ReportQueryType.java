package com.dodo.common.annotation.report;

import com.dodo.common.enums.EnumAttributeConverter;
import com.dodo.common.enums.EnumInterface;

/**
 * 报表的查询类型配置
 * 
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public enum ReportQueryType implements EnumInterface{
	eq(1," {0} = ? ","dodo.enums.ReportQueryType.name.eq","dodo.enums.ReportQueryType.desc.eq"),
	ne(2," {0} <> ? ","dodo.enums.ReportQueryType.name.ne","dodo.enums.ReportQueryType.desc.ne"),
	gt(3," {0} > ?","dodo.enums.ReportQueryType.name.gt","dodo.enums.ReportQueryType.desc.gt"),
	lt(4," {0} < ? ","dodo.enums.ReportQueryType.name.lt","dodo.enums.ReportQueryType.desc.lt"),
	ge(5," {0} >= ? ","dodo.enums.ReportQueryType.name.ge","dodo.enums.ReportQueryType.desc.ge"),
	le(6," {0} <= ? ","dodo.enums.ReportQueryType.name.le","dodo.enums.ReportQueryType.desc.le"),
	between(7," {0} between ? and ? ","dodo.enums.ReportQueryType.name.between","dodo.enums.ReportQueryType.desc.between"),
	notBetween(8,"{0} not between ? and ? ","dodo.enums.ReportQueryType.name.notBetween","dodo.enums.ReportQueryType.desc.notBetween"),
	in(9," {0} in ({1}) ","dodo.enums.ReportQueryType.name.in","dodo.enums.ReportQueryType.desc.in"),
	notIn(10," {0} not in ({1}) ","dodo.enums.ReportQueryType.name.notIn","dodo.enums.ReportQueryType.desc.notIn"),
	isNull(11," {0} is null ","dodo.enums.ReportQueryType.name.isNull","dodo.enums.ReportQueryType.desc.isNull"),
	isNotNull(12," {0} is not null ","dodo.enums.ReportQueryType.name.isNotNull","dodo.enums.ReportQueryType.desc.isNotNull"),
	like(13," {0} like ? ","dodo.enums.ReportQueryType.name.like","dodo.enums.ReportQueryType.desc.like"),
	notLike(14," {0} not like ? ","dodo.enums.ReportQueryType.name.notLike","dodo.enums.ReportQueryType.desc.notLike");
	private ReportQueryType(Integer seq,String expression,String nameKey,String descKey) {
		this.nameKey = nameKey;
		this.descKey = descKey;
		this.expression = expression;
		this.seq = seq;
	}

	private Integer seq;
	private String name;
	private String nameKey;
	private String desc;
	private String descKey;
	private String expression;
	public String getName() {
		return name;
	}
	public String getNameKey() {
		return nameKey;
	}
	public String getDesc() {
		return desc;
	}
	public String getDescKey() {
		return descKey;
	}
	public String getExpression() {
		return expression;
	}	
	public Integer getSeq() {
        return seq;
    }
	
	public static class Converter extends EnumAttributeConverter<ReportQueryType> {

        @Override
        public Class<ReportQueryType> getClazz() {
            return ReportQueryType.class;
        }
    }
}