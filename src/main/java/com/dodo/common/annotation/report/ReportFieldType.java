package com.dodo.common.annotation.report;

import com.dodo.common.enums.EnumAttributeConverter;
import com.dodo.common.enums.EnumInterface;

/**
 * 不同类型的报表字段配置：显示名称、约束等
 * 
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public enum ReportFieldType implements EnumInterface{
	String(1,"dodo.enums.ReportFieldType.name.String","dodo.enums.ReportFieldType.desc.String",""),
	BigDecimal(2,"dodo.enums.ReportFieldType.name.BigDecimal","dodo.enums.ReportFieldType.desc.BigDecimal","class='{number:true}'"),
	Boolean(3,"dodo.enums.ReportFieldType.name.Boolean","dodo.enums.ReportFieldType.desc.Boolean",""),
	Byte(4,"dodo.enums.ReportFieldType.name.Byte","dodo.enums.ReportFieldType.desc.Byte","class='{digits:true,min:-128,max:127}'"),
	Short(5,"dodo.enums.ReportFieldType.name.Short","dodo.enums.ReportFieldType.desc.Short","class='{digits:true,min:-32768,max:32767}'"),
	Integer(6,"dodo.enums.ReportFieldType.name.Integer","dodo.enums.ReportFieldType.desc.Integer","class='{digits:true,min:-2147483648,max:2147483647}'"),
	Long(7,"dodo.enums.ReportFieldType.name.Long","dodo.enums.ReportFieldType.desc.Long","class='{digits:true,min:-9233372036854477808,max:9233372036854477807}'"),
	Float(8,"dodo.enums.ReportFieldType.name.Float","dodo.enums.ReportFieldType.desc.Float","class='{number:true}'"),
	Double(9,"dodo.enums.ReportFieldType.name.Double","dodo.enums.ReportFieldType.desc.Double","class='{number:true}'"),
	Date(10,"dodo.enums.ReportFieldType.name.Date","dodo.enums.ReportFieldType.desc.Date","onclick=\"WdatePicker({lang:'_clientlanguage_',dateFmt:'yyyy-MM-dd',position:{top:'under'}});\" readOnly=\"readOnly\""),
	Time(11,"dodo.enums.ReportFieldType.name.Time","dodo.enums.ReportFieldType.desc.Time","onclick=\"WdatePicker({lang:'_clientlanguage_',dateFmt:'HH:mm:ss',position:{top:'under'}});\" readOnly=\"readOnly\""),
	Timestamp(12,"dodo.enums.ReportFieldType.name.Timestamp","dodo.enums.ReportFieldType.desc.Timestamp","onclick=\"WdatePicker({lang:'_clientlanguage_',dateFmt:'yyyy-MM-dd HH:mm:ss',position:{top:'under'}});\" readOnly=\"readOnly\""),
	ByteArray(13,"dodo.enums.ReportFieldType.name.ByteArray","dodo.enums.ReportFieldType.desc.ByteArray",""),
	Object(14,"dodo.enums.ReportFieldType.name.Object","dodo.enums.ReportFieldType.desc.Object","");
	private ReportFieldType(Integer seq,String nameKey,String descKey,String expression) {
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
	@Override
	public Integer getSeq() {
	    return seq;
	}
	
	public static class Converter extends EnumAttributeConverter<ReportFieldType> {

        @Override
        public Class<ReportFieldType> getClazz() {
            return ReportFieldType.class;
        }
    }
}