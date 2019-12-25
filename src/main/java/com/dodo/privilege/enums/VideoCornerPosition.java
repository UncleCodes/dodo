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
public enum VideoCornerPosition implements EnumInterface{
	TOP_LEFT(1,"dodo.privilege.enums.VideoCornerPosition.name.topleft","top-left"),
	TOP_RIGHT(2,"dodo.privilege.enums.VideoCornerPosition.name.topright","top-right"),
	BOTTOM_LEFT(3,"dodo.privilege.enums.VideoCornerPosition.name.bottomleft","bottom-left"),
	BOTTOM_RIGHT(4,"dodo.privilege.enums.VideoCornerPosition.name.bottomright","bottom-right");
	
	private VideoCornerPosition(Integer seq,String nameKey, String position) {
		this.nameKey = nameKey;
		this.position = position;
		this.seq = seq;
	}

	private Integer seq;
	private String name;
	private String nameKey;
	private String position;
	private String descKey;
	public String getName() {
		return name;
	}
	public String getNameKey() {
		return nameKey;
	}
	public String getDesc() {
		return null;
	}
	public String getDescKey() {
		return descKey;
	}
	public String getPosition() {
        return position;
    }
	
    @Override
    public Integer getSeq() {
        return seq;
    }
    public static class Converter extends EnumAttributeConverter<VideoCornerPosition> {

        @Override
        public Class<VideoCornerPosition> getClazz() {
            return VideoCornerPosition.class;
        }
    }
}
