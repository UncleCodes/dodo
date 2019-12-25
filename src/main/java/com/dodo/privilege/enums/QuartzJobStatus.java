package com.dodo.privilege.enums;

import com.dodo.common.enums.EnumAttributeConverter;
import com.dodo.common.enums.EnumInterface;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public enum QuartzJobStatus implements EnumInterface {
    INIT(1,"dodo.privilege.enums.QuartzJobStatus.name.init", "dodo.privilege.enums.QuartzJobStatus.desc.init"), 
    ING(2,"dodo.privilege.enums.QuartzJobStatus.name.ing", "dodo.privilege.enums.QuartzJobStatus.desc.ing"), 
    OK(3,"dodo.privilege.enums.QuartzJobStatus.name.ok", "dodo.privilege.enums.QuartzJobStatus.desc.ok"), 
    ERROR(4,"dodo.privilege.enums.QuartzJobStatus.name.error", "dodo.privilege.enums.QuartzJobStatus.desc.error"), 
    VETOED(5,"dodo.privilege.enums.QuartzJobStatus.name.vetoed", "dodo.privilege.enums.QuartzJobStatus.desc.vetoed"), 
    EXCEPTION(6,"dodo.privilege.enums.QuartzJobStatus.name.exception","dodo.privilege.enums.QuartzJobStatus.desc.exception");
    private QuartzJobStatus(Integer seq,String nameKey, String descKey) {
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
    public static class Converter extends EnumAttributeConverter<QuartzJobStatus> {

        @Override
        public Class<QuartzJobStatus> getClazz() {
            return QuartzJobStatus.class;
        }
    }
}
