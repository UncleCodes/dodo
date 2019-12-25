package com.dodo.common.framework.service.impl;

import java.io.Serializable;
import java.util.List;

import com.dodo.common.database.data.Record;
import com.dodo.common.database.data.Records;
import com.dodo.common.database.hql.DBException;
import com.dodo.common.database.hql.HqlHelper;
import com.dodo.common.framework.bean.pager.PageModel;
import com.dodo.common.framework.dao.HqlHelperDao;
import com.dodo.common.framework.entity.BaseEntity;
import com.dodo.common.framework.service.HqlHelperService;
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
public class HqlHelperServiceImpl implements HqlHelperService {
    private HqlHelperDao helperDao;

    public HqlHelperDao getHelperDao() {
        return helperDao;
    }

    public void setHelperDao(HqlHelperDao helperDao) {
        this.helperDao = helperDao;
    }

    @Override
    public <T extends BaseEntity> Serializable save(T T) {
        return this.helperDao.save(T);
    }

    @Override
    public <T extends BaseEntity> List<Serializable> saveAll(List<T> TS) {
        return this.helperDao.saveAll(TS);
    }

    @Override
    public <T extends BaseEntity> void delete(T T) {
        this.helperDao.delete(T);
    }

    @Override
    public <T extends BaseEntity> void delete(Serializable ID, Class<T> clazz) {
        this.helperDao.delete(ID, clazz);
    }

    @Override
    public <T extends BaseEntity> void delete(Serializable[] IDS, Class<T> clazz) {
        this.helperDao.delete(IDS, clazz);
    }

    @Override
    public <T extends BaseEntity> T deleteAndReturn(Serializable ID, Class<T> clazz) {
        return this.helperDao.deleteAndReturn(ID, clazz);
    }

    @Override
    public <T extends BaseEntity> List<T> deleteAndReturn(Serializable[] IDS, Class<T> clazz) {
        return this.helperDao.deleteAndReturn(IDS, clazz);
    }

    @Override
    public <T extends BaseEntity> int delete(HqlHelper hqlHelper) {
        return this.helperDao.delete(hqlHelper);
    }

    @Override
    public <T extends BaseEntity> List<T> deleteAndReturn(HqlHelper hqlHelper) {
        return this.helperDao.deleteAndReturn(hqlHelper);
    }

    @Override
    public <T extends BaseEntity> void update(T T) {
        this.helperDao.update(T);
    }

    @Override
    public <T extends BaseEntity> int update(HqlHelper hqlHelper) {
        return this.helperDao.update(hqlHelper);
    }

    @Override
    public <T extends BaseEntity> T get(Serializable ID, Class<T> clazz) {
        return this.helperDao.get(ID, clazz);
    }

    @Override
    public <T extends BaseEntity> T load(Serializable ID, Class<T> clazz) {
        return this.helperDao.load(ID, clazz);
    }

    @Override
    public <T extends BaseEntity> Long getCountByHql(HqlHelper hqlHelper, boolean isDistinct) {
        return this.helperDao.getCountByHql(hqlHelper, isDistinct);
    }

    @Override
    public <T extends BaseEntity> Object getMaxByHql(HqlHelper hqlHelper) {
        return this.helperDao.getMaxByHql(hqlHelper);
    }

    @Override
    public <T extends BaseEntity> Object getMinByHql(HqlHelper hqlHelper) {
        return this.helperDao.getMinByHql(hqlHelper);
    }

    @Override
    public <T extends BaseEntity> Object getSumByHql(HqlHelper hqlHelper, boolean isDistinct) {
        return this.helperDao.getSumByHql(hqlHelper, isDistinct);
    }

    @Override
    public <T extends BaseEntity> Double getAvgByHql(HqlHelper hqlHelper, boolean isDistinct) {
        return this.helperDao.getAvgByHql(hqlHelper, isDistinct);
    }

    @Override
    public <T extends BaseEntity> List<T> getEntitys(Class<T> clazz) {
        return this.helperDao.getEntitys(clazz);
    }

    @Override
    public <T extends BaseEntity> List<T> getEntitys(HqlHelper hqlHelper) {
        return this.helperDao.getEntitys(hqlHelper);
    }

    @Override
    public <T extends BaseEntity> PageModel getEntitysPager(HqlHelper hqlHelper, PageModel pageModel) {
        return this.helperDao.getEntitysPager(hqlHelper, pageModel);
    }

    @Override
    public <T extends BaseEntity> T getEntity(HqlHelper hqlHelper) {
        return this.helperDao.getEntity(hqlHelper);
    }

    @Override
    public <T extends BaseEntity> Records getRecords(HqlHelper hqlHelper, boolean isDistinct) {
        return this.helperDao.getRecords(hqlHelper, isDistinct);
    }

    @Override
    public <T extends BaseEntity> Record getRecord(HqlHelper hqlHelper) {
        return this.helperDao.getRecord(hqlHelper);
    }

    @Override
    public <T extends BaseEntity> Record getRecordGroup(HqlHelper hqlHelper) {
        return this.helperDao.getRecordGroup(hqlHelper);
    }

    @Override
    public <T extends BaseEntity> PageModel getRecordsPager(PageModel pageModel, HqlHelper hqlHelper, boolean isDistinct) {
        return this.helperDao.getRecordsPager(pageModel, hqlHelper, isDistinct);
    }

    @Override
    public <T extends BaseEntity> Records getRecordsGroup(HqlHelper hqlHelper) {
        return this.helperDao.getRecordsGroup(hqlHelper);
    }

    @Override
    public <T extends BaseEntity> PageModel getChartDataList(PageModel pageModel, HqlHelper helper,
            String ct_one_field, String ct_two_field) {
        return this.helperDao.getChartDataList(pageModel, helper, ct_one_field, ct_two_field);
    }

    @Override
    public void flush() {
        this.helperDao.flush();
    }

    @Override
    public void evict(Object obj) {
        this.helperDao.evict(obj);
    }

    @Override
    public void clear() {
        this.helperDao.clear();
    }

    @Override
    public SqlReportLimit getLimitSQL(String sql, String whereClause, List<Object> whereClauseVars, int pageNumber,
            int pageSize) throws DBException {
        return this.helperDao.getLimitSQL(sql, whereClause, whereClauseVars, pageNumber, pageSize);
    }

    @Override
    public String getCountSQL(String sql, String whereClause) throws DBException {
        return this.helperDao.getCountSQL(sql, whereClause);
    }
}