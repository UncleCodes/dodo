package com.dodo.common.framework.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.dodo.common.annotation.field.DodoField;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
@MappedSuperclass
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = -6299018543295811572L;

    @DodoField(nameKey = "dodo.common.bean.namekey.id", sortSeq = -3412, addable = false, editable = false, queryOnList = true)
    protected String          id;

    @DodoField(nameKey = "dodo.common.bean.namekey.createDate", sortSeq = 3412, addable = false, editable = false, listable = false)
    protected Date            createDate;
    @DodoField(nameKey = "dodo.common.bean.namekey.modifyDate", sortSeq = 3413, addable = false, editable = false, listable = false)
    protected Date            modifyDate;

    @DodoField(nameKey = "dodo.common.bean.namekey.sortSeq", sortSeq = 3414, addable = false, isDigits = true, min = Integer.MIN_VALUE
            + "", max = Integer.MAX_VALUE + "", listable = false)
    protected Integer         sortSeq;

    /** 临时变量 **/
    private Integer           _excelRowIndex_;

    @Id
    @Column(length = 20)
    @GeneratedValue(generator = "dodo_id_generator")
    @GenericGenerator(name = "dodo_id_generator", strategy = "com.dodo.common.framework.entity.DodoIdGenerator")
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(updatable = false)
    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getSortSeq() {
        return sortSeq;
    }

    public void setSortSeq(Integer sortSeq) {
        this.sortSeq = sortSeq;
    }

    public Date getModifyDate() {
        return this.modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    @Transient
    public void onSave() {
    }

    @Transient
    public void onUpdate() {
    }

    public Integer _excelRowIndex_() {
        return _excelRowIndex_;
    }

    public void _excelRowIndex_(Integer _excelRowIndex_) {
        this._excelRowIndex_ = _excelRowIndex_;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (getId() == null ? 0 : getId().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        Class<?> thisClass = getClass();
        Class<?> objClass = obj.getClass();
        if (thisClass != objClass && thisClass.getSuperclass() != objClass && thisClass != objClass.getSuperclass()) {
            return false;
        }
        BaseEntity other = (BaseEntity) obj;

        if (getId() == null && other.getId() != null) {
            return false;
        } else if (!getId().equals(other.getId())) {
            return false;
        }
        return true;
    }
}