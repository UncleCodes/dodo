package com.dodo.common.framework.dao;

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
public abstract interface BaseDao<T, ID extends Serializable> {
    // insert
    ID save(T T);

    List<ID> saveAll(Collection<T> TS);

    // delete 
    void delete(T T);

    void delete(ID ID);

    void delete(ID[] IDS);

    T deleteAndReturn(ID ID);

    List<T> deleteAndReturn(ID[] IDS);

    // 按条件delete hql方式
    int delete(HqlHelper hqlHelper);

    List<T> deleteAndReturn(HqlHelper hqlHelper);

    // update
    void update(T T);

    int update(HqlHelper hqlHelper);

    // query one
    T get(ID ID);

    T load(ID ID);

    // query basic function 
    Long getCountByHql(HqlHelper hqlHelper, boolean isDistinct);

    Object getMaxByHql(HqlHelper hqlHelper);

    Object getMinByHql(HqlHelper hqlHelper);

    Object getSumByHql(HqlHelper hqlHelper, boolean isDistinct);

    Double getAvgByHql(HqlHelper hqlHelper, boolean isDistinct);

    // query list
    List<T> getEntitys();

    // hql query
    List<T> getEntitys(HqlHelper hqlHelper);

    PageModel getEntitysPager(HqlHelper hqlHelper, PageModel pageModel);

    T getEntity(HqlHelper hqlHelper);

    Records getRecords(HqlHelper hqlHelper, boolean isDistinct);

    Record getRecord(HqlHelper hqlHelper);

    PageModel getRecordsPager(PageModel pageModel, HqlHelper hqlHelper, boolean isDistinct);

    Records getRecordsGroup(HqlHelper hqlHelper);

    Record getRecordGroup(HqlHelper hqlHelper);

    //query chart data
    PageModel getChartDataList(PageModel pageModel, HqlHelper helper, String ct_one_field, String ct_two_field);

    // other
    void flush();

    void evict(Object obj);

    void clear();
}
