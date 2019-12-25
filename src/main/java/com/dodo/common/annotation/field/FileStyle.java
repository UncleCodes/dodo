package com.dodo.common.annotation.field;

import com.dodo.common.enums.EnumAttributeConverter;
import com.dodo.common.enums.EnumInterface;

/**
 * 文件字段的文件保存类型<br/>
 * 
 * OnlyPath:只保存文件路径，此时字段长度 至少为128<br/>
 * FullInfo:保存文件的详细信息，此时字段长度 至少为 1024<br/>
 * 
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public enum FileStyle implements EnumInterface {
    OnlyPath(1, "仅文件路径", ""), FullInfo(2, "完整信息", "保存文件扩展名、大小、路径等信息");
    private FileStyle(Integer seq, String name, String desc) {
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
    public String getNameKey() {
        return null;
    }

    @Override
    public String getDescKey() {
        return null;
    }

    public Integer getSeq() {
        return seq;
    }

    public static class Converter extends EnumAttributeConverter<FileStyle> {

        @Override
        public Class<FileStyle> getClazz() {
            return FileStyle.class;
        }
    }
}
