package com.dodo.common.framework.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.dodo.common.database.data.Record;
import com.dodo.common.database.data.Records;
import com.dodo.common.database.hql.HqlHelper;
import com.dodo.common.framework.bean.pager.PageModel;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public interface BaseService<T, ID extends Serializable> {
    /**
     * 保存一个实体
     */
    ID save(T T);

    /**
     * 批量保存实体
     */
    List<ID> saveAll(Collection<T> TS);

    /**
     * 删除一个实体
     */
    void delete(T T);

    /**
     * 通过ID删除实体
     */
    void delete(ID ID);

    /**
     * 批量通过ID删除实体
     */
    void delete(ID[] IDS);

    /**
     * 删除并返回被删除的实体
     */
    T deleteAndReturn(ID ID);

    /**
     * 批量删除并返回被删除的实体
     */
    List<T> deleteAndReturn(ID[] IDS);

    /**
     * 通过HqlHelper删除实体
     */
    int delete(HqlHelper hqlHelper);

    /**
     * 通过HqlHelper删除并返回被删除的实体
     */
    List<T> deleteAndReturn(HqlHelper hqlHelper);

    /**
     * 更新一个实体
     */
    void update(T T);

    /**
     * 通过HqlHelper更新实体
     */
    int update(HqlHelper hqlHelper);

    /**
     * 通过ID查询一个实体 get 方式
     */
    T get(ID ID);

    /**
     * 通过ID查询一个实体 load 方式
     */
    T load(ID ID);

    /**
     * 查询count
     */
    Long getCountByHql(HqlHelper hqlHelper, boolean isDistinct);

    /**
     * 查询max
     */
    Object getMaxByHql(HqlHelper hqlHelper);

    /**
     * 查询min
     */
    Object getMinByHql(HqlHelper hqlHelper);

    /**
     * 查询sum
     */
    Object getSumByHql(HqlHelper hqlHelper, boolean isDistinct);

    /**
     * 查询avg
     */
    Double getAvgByHql(HqlHelper hqlHelper, boolean isDistinct);

    /**
     * 查询实体类全部数据，返回实体对象
     */
    List<T> getEntitys();

    /**
     * 通过HqlHelper查询实体数据，返回实体对象
     */
    List<T> getEntitys(HqlHelper hqlHelper);

    /**
     * 通过HqlHelper查询实体数据，分页查询，返回分页实体对象
     */
    PageModel getEntitysPager(HqlHelper hqlHelper, PageModel pageModel);

    /**
     * 通过HqlHelper查询一个实体，返回实体对象
     */
    T getEntity(HqlHelper hqlHelper);

    /**
     * 通过HqlHelper批量查询，返回Records，Records中保存的是Map格式的数据
     */
    Records getRecords(HqlHelper hqlHelper, boolean isDistinct);

    /**
     * 通过HqlHelper查询，返回Record，Record中保存的是Map格式的数据
     */
    Record getRecord(HqlHelper hqlHelper);

    /**
     * 通过HqlHelper分页查询，返回PageModel，PageModel中保存的是Map格式的数据
     */
    PageModel getRecordsPager(PageModel pageModel, HqlHelper hqlHelper, boolean isDistinct);

    /**
     * 通过HqlHelper分组查询，返回Records，Records中保存的是Map格式的数据
     */
    Records getRecordsGroup(HqlHelper hqlHelper);

    /**
     * 通过HqlHelper分组查询，返回Record，Record中保存的是Map格式的数据
     */
    Record getRecordGroup(HqlHelper hqlHelper);

    /**
     * 统计的时候使用，获取统计数据
     */
    PageModel getChartDataList(PageModel pageModel, HqlHelper helper, String ct_one_field, String ct_two_field);

    // other
    void flush();

    void evict(Object obj);

    void clear();
}
