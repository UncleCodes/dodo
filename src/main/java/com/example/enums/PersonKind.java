package com.example.enums;

import com.dodo.common.enums.EnumAttributeConverter;
import com.dodo.common.enums.EnumInterface;

public enum PersonKind implements EnumInterface {
    GeGe(1, "哥哥"), MeiMei(2, "妹妹"), DiDi(3, "弟弟");
    private PersonKind(Integer seq, String name) {
        this.seq = seq;
        this.name = name;
    }

    private String name;
    private int    seq;

    @Override
    public String getName() {
        return name;
    }

    public Integer getSeq() {
        return seq;
    }

    @Override
    public String getDesc() {
        return null;
    }

    @Override
    public String getNameKey() {
        return null;
    }

    @Override
    public String getDescKey() {
        return null;
    }

    public static class Converter extends EnumAttributeConverter<PersonKind> {

        @Override
        public Class<PersonKind> getClazz() {
            return PersonKind.class;
        }
    }
}
