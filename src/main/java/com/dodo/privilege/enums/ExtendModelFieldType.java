package com.dodo.privilege.enums;

import com.dodo.common.enums.EnumAttributeConverter;
import com.dodo.common.enums.EnumInterface;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public enum ExtendModelFieldType implements EnumInterface{
	STRING(1,"dodo.privilege.enums.ExtendModelFieldType.name.STRING","dodo.privilege.enums.ExtendModelFieldType.desc.STRING"),
	NUMBER(2,"dodo.privilege.enums.ExtendModelFieldType.name.NUMBER","dodo.privilege.enums.ExtendModelFieldType.desc.NUMBER"),
	DIGITS(3,"dodo.privilege.enums.ExtendModelFieldType.name.DIGITS","dodo.privilege.enums.ExtendModelFieldType.desc.DIGITS"),
	TEXTAREA(4,"dodo.privilege.enums.ExtendModelFieldType.name.TEXTAREA","dodo.privilege.enums.ExtendModelFieldType.desc.TEXTAREA"),
	RICHTEXT(5,"dodo.privilege.enums.ExtendModelFieldType.name.RICHTEXT","dodo.privilege.enums.ExtendModelFieldType.desc.RICHTEXT"),
	SINGLEFILE(6,"dodo.privilege.enums.ExtendModelFieldType.name.SINGLEFILE","dodo.privilege.enums.ExtendModelFieldType.desc.SINGLEFILE"),
	MULTIFILE(7,"dodo.privilege.enums.ExtendModelFieldType.name.MULTIFILE","dodo.privilege.enums.ExtendModelFieldType.desc.MULTIFILE"),
	DOC(8,"dodo.privilege.enums.ExtendModelFieldType.name.DOC","dodo.privilege.enums.ExtendModelFieldType.desc.DOC"),
	DATE(9,"dodo.privilege.enums.ExtendModelFieldType.name.DATE","dodo.privilege.enums.ExtendModelFieldType.desc.DATE"),
	DATETIME(10,"dodo.privilege.enums.ExtendModelFieldType.name.DATETIME","dodo.privilege.enums.ExtendModelFieldType.desc.DATETIME"),
	TIME(11,"dodo.privilege.enums.ExtendModelFieldType.name.TIME","dodo.privilege.enums.ExtendModelFieldType.desc.TIME"),
	LOCATION(12,"dodo.privilege.enums.ExtendModelFieldType.name.LOCATION","dodo.privilege.enums.ExtendModelFieldType.desc.LOCATION"),
	COLOR(13,"dodo.privilege.enums.ExtendModelFieldType.name.COLOR","dodo.privilege.enums.ExtendModelFieldType.desc.COLOR"),
	SELECT(14,"dodo.privilege.enums.ExtendModelFieldType.name.SELECT","dodo.privilege.enums.ExtendModelFieldType.desc.SELECT"),
	RADIO(15,"dodo.privilege.enums.ExtendModelFieldType.name.RADIO","dodo.privilege.enums.ExtendModelFieldType.desc.RADIO"),
	CHECKBOX(16,"dodo.privilege.enums.ExtendModelFieldType.name.CHECKBOX","dodo.privilege.enums.ExtendModelFieldType.desc.CHECKBOX"),
	VIDEO(17,"dodo.privilege.enums.ExtendModelFieldType.name.VIDEO","dodo.privilege.enums.ExtendModelFieldType.desc.VIDEO");
	
	private ExtendModelFieldType(Integer seq,String nameKey,String descKey) {
		this.nameKey = nameKey;
		this.descKey = descKey;
		this.seq = seq;
	}

	private Integer seq;
	private String name;
	private String nameKey;
	private String desc;
	private String descKey;
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
    @Override
    public Integer getSeq() {
        return seq;
    }	
    
    public static class Converter extends EnumAttributeConverter<ExtendModelFieldType> {

        @Override
        public Class<ExtendModelFieldType> getClazz() {
            return ExtendModelFieldType.class;
        }
    }
}
