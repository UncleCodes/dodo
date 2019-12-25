package com.dodo.common.framework.service;

import java.io.Serializable;
import java.util.List;

import com.dodo.common.database.data.Record;
import com.dodo.common.database.data.Records;
import com.dodo.common.database.hql.DBException;
import com.dodo.common.database.hql.HqlHelper;
import com.dodo.common.framework.bean.pager.PageModel;
import com.dodo.common.framework.entity.BaseEntity;
import com.dodo.common.sqlreport.SqlReportLimit;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public interface HqlHelperService {
    /**
     * 保存一个实体
     */
    <T extends BaseEntity> Serializable save(T T);

    /**
     * 批量保存实体
     */
    <T extends BaseEntity> List<Serializable> saveAll(List<T> TS);

    /**
     * 删除一个实体
     */
    <T extends BaseEntity> void delete(T T);

    /**
     * 通过ID删除实体
     */
    <T extends BaseEntity> void delete(Serializable ID, Class<T> clazz);

    /**
     * 批量通过ID删除实体
     */
    <T extends BaseEntity> void delete(Serializable[] IDS, Class<T> clazz);

    /**
     * 删除并返回被删除的实体
     */
    <T extends BaseEntity> T deleteAndReturn(Serializable ID, Class<T> clazz);

    /**
     * 批量删除并返回被删除的实体
     */
    <T extends BaseEntity> List<T> deleteAndReturn(Serializable[] IDS, Class<T> clazz);

    /**
     * 通过HqlHelper删除实体
     */
    <T extends BaseEntity> int delete(HqlHelper hqlHelper);

    /**
     * 通过HqlHelper删除并返回被删除的实体
     */
    <T extends BaseEntity> List<T> deleteAndReturn(HqlHelper hqlHelper);

    /**
     * 更新一个实体
     */
    <T extends BaseEntity> void update(T T);

    /**
     * 通过HqlHelper更新实体
     */
    <T extends BaseEntity> int update(HqlHelper hqlHelper);

    /**
     * 通过ID查询一个实体 get 方式
     */
    <T extends BaseEntity> T get(Serializable ID, Class<T> clazz);

    /**
     * 通过ID查询一个实体 load 方式
     */
    <T extends BaseEntity> T load(Serializable ID, Class<T> clazz);

    /**
     * 查询count
     */
    <T extends BaseEntity> Long getCountByHql(HqlHelper hqlHelper, boolean isDistinct);

    /**
     * 查询max
     */
    <T extends BaseEntity> Object getMaxByHql(HqlHelper hqlHelper);

    /**
     * 查询min
     */
    <T extends BaseEntity> Object getMinByHql(HqlHelper hqlHelper);

    /**
     * 查询sum
     */
    <T extends BaseEntity> Object getSumByHql(HqlHelper hqlHelper, boolean isDistinct);

    /**
     * 查询avg
     */
    <T extends BaseEntity> Double getAvgByHql(HqlHelper hqlHelper, boolean isDistinct);

    /**
     * 查询实体类全部数据，返回实体对象
     */
    <T extends BaseEntity> List<T> getEntitys(Class<T> clazz);

    /**
     * 通过HqlHelper查询实体数据，返回实体对象
     */
    <T extends BaseEntity> List<T> getEntitys(HqlHelper hqlHelper);

    /**
     * 通过HqlHelper查询实体数据，分页查询，返回分页实体对象
     */
    <T extends BaseEntity> PageModel getEntitysPager(HqlHelper hqlHelper, PageModel pageModel);

    /**
     * 通过HqlHelper查询一个实体，返回实体对象
     */
    <T extends BaseEntity> T getEntity(HqlHelper hqlHelper);

    /**
     * 通过HqlHelper批量查询，返回Records，Records中保存的是Map格式的数据
     */
    <T extends BaseEntity> Records getRecords(HqlHelper hqlHelper, boolean isDistinct);

    /**
     * 通过HqlHelper查询，返回Record，Record中保存的是Map格式的数据
     */
    <T extends BaseEntity> Record getRecord(HqlHelper hqlHelper);

    /**
     * 通过HqlHelper分页查询，返回PageModel，PageModel中保存的是Map格式的数据
     */
    <T extends BaseEntity> PageModel getRecordsPager(PageModel pageModel, HqlHelper hqlHelper, boolean isDistinct);

    /**
     * 通过HqlHelper分组查询，返回Records，Records中保存的是Map格式的数据
     */
    <T extends BaseEntity> Records getRecordsGroup(HqlHelper hqlHelper);

    /**
     * 通过HqlHelper分组查询，返回Record，Record中保存的是Map格式的数据
     */
    <T extends BaseEntity> Record getRecordGroup(HqlHelper hqlHelper);

    /**
     * 统计的时候使用，获取统计数据
     */
    <T extends BaseEntity> PageModel getChartDataList(PageModel pageModel, HqlHelper helper, String ct_one_field,
            String ct_two_field);

    // other
    void flush();

    void evict(Object obj);

    void clear();

    /**
     * 根据当前的数据库，返回分页的SQL
     */
    SqlReportLimit getLimitSQL(String sql, String whereClause, List<Object> whereClauseVars, int pageNumber,
            int pageSize) throws DBException;

    /**
     * 根据当前的数据库，返回获取数量的SQL
     */
    String getCountSQL(String sql, String whereClause) throws DBException;
}
