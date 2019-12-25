package com.dodo.common.framework.dao;

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
public abstract interface HqlHelperDao {
    // insert
    <T extends BaseEntity> Serializable save(T T);

    <T extends BaseEntity> List<Serializable> saveAll(List<T> TS);

    // delete 
    <T extends BaseEntity> void delete(T T);

    <T extends BaseEntity> void delete(Serializable ID, Class<T> clazz);

    <T extends BaseEntity> void delete(Serializable[] IDS, Class<T> clazz);

    <T extends BaseEntity> T deleteAndReturn(Serializable ID, Class<T> clazz);

    <T extends BaseEntity> List<T> deleteAndReturn(Serializable[] IDS, Class<T> clazz);

    // 按条件delete hql方式
    <T extends BaseEntity> int delete(HqlHelper hqlHelper);

    <T extends BaseEntity> List<T> deleteAndReturn(HqlHelper hqlHelper);

    // update
    <T extends BaseEntity> void update(T T);

    <T extends BaseEntity> int update(HqlHelper hqlHelper);

    // query one
    <T extends BaseEntity> T get(Serializable ID, Class<T> clazz);

    <T extends BaseEntity> T load(Serializable ID, Class<T> clazz);

    // query basic function 
    <T extends BaseEntity> Long getCountByHql(HqlHelper hqlHelper, boolean isDistinct);

    <T extends BaseEntity> Object getMaxByHql(HqlHelper hqlHelper);

    <T extends BaseEntity> Object getMinByHql(HqlHelper hqlHelper);

    <T extends BaseEntity> Object getSumByHql(HqlHelper hqlHelper, boolean isDistinct);

    <T extends BaseEntity> Double getAvgByHql(HqlHelper hqlHelper, boolean isDistinct);

    // query list
    <T extends BaseEntity> List<T> getEntitys(Class<T> clazz);

    // hql query
    <T extends BaseEntity> List<T> getEntitys(HqlHelper hqlHelper);

    <T extends BaseEntity> PageModel getEntitysPager(HqlHelper hqlHelper, PageModel pageModel);

    <T extends BaseEntity> T getEntity(HqlHelper hqlHelper);

    <T extends BaseEntity> Records getRecords(HqlHelper hqlHelper, boolean isDistinct);

    <T extends BaseEntity> Record getRecord(HqlHelper hqlHelper);

    <T extends BaseEntity> PageModel getRecordsPager(PageModel pageModel, HqlHelper hqlHelper, boolean isDistinct);

    <T extends BaseEntity> Records getRecordsGroup(HqlHelper hqlHelper);

    <T extends BaseEntity> Record getRecordGroup(HqlHelper hqlHelper);

    //query chart data
    <T extends BaseEntity> PageModel getChartDataList(PageModel pageModel, HqlHelper helper, String ct_one_field,
            String ct_two_field);

    // other
    void flush();

    void evict(Object obj);

    void clear();

    SqlReportLimit getLimitSQL(String sql, String whereClause, List<Object> whereClauseVars, int pageNumber,
            int pageSize) throws DBException;

    String getCountSQL(String sql, String whereClause) throws DBException;
}
