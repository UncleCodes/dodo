package com.dodo.common.annotation.menu;

import com.dodo.common.enums.EnumAttributeConverter;
import com.dodo.common.enums.EnumInterface;

/**
 * 菜单级别配置，系统菜单为3级菜单
 * 
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public enum DodoMenuLevel implements EnumInterface{
	LEVEL0(1,"dodo.privilege.enums.MenuLevelEnum.name.level0","dodo.privilege.enums.MenuLevelEnum.desc.level0"),
	LEVEL1(2,"dodo.privilege.enums.MenuLevelEnum.name.level1","dodo.privilege.enums.MenuLevelEnum.desc.level1"),
	LEVEL2(3,"dodo.privilege.enums.MenuLevelEnum.name.level2","dodo.privilege.enums.MenuLevelEnum.desc.level2"),
	LEVEL3(4,"dodo.privilege.enums.MenuLevelEnum.name.level3","dodo.privilege.enums.MenuLevelEnum.desc.level3");
	private DodoMenuLevel(Integer seq,String nameKey,String descKey) {
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
	public static class Converter extends EnumAttributeConverter<DodoMenuLevel> {

        @Override
        public Class<DodoMenuLevel> getClazz() {
            return DodoMenuLevel.class;
        }
    }
}
