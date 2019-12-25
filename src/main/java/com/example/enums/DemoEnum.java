package com.example.enums;

import com.dodo.common.enums.EnumAttributeConverter;
import com.dodo.common.enums.EnumInterface;

public enum DemoEnum implements EnumInterface {
    VALUE1(1, "枚举值1", "For演示Test"), VALUE2(2, "枚举值2", "For演示Test");
    private DemoEnum(Integer seq, String name, String desc) {
        this.seq = seq;
        this.name = name;
        this.desc = desc;
    }

    private String name;
    private String desc;
    private int    seq;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public Integer getSeq() {
        return seq;
    }

    @Override
    public String getNameKey() {
        return null;
    }

    @Override
    public String getDescKey() {
        return null;
    }

    public static class Converter extends EnumAttributeConverter<DemoEnum> {
        @Override
        public Class<DemoEnum> getClazz() {
            return DemoEnum.class;
        }
    }
}
