package com.dodo.common.framework.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dodo.common.database.data.Record;
import com.dodo.common.database.data.Records;
import com.dodo.common.database.hql.HqlHelper;
import com.dodo.common.framework.bean.pager.PageModel;
import com.dodo.common.framework.dao.BaseDao;
import com.dodo.common.framework.service.BaseService;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
@Transactional
public class BaseServiceImpl<T, ID extends Serializable> implements BaseService<T, ID> {
    private BaseDao<T, ID> baseDao;

    public BaseDao<T, ID> getBaseDao() {
        return this.baseDao;
    }

    public void setBaseDao(BaseDao<T, ID> baseDao) {
        this.baseDao = baseDao;
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public T get(ID ID) {
        return this.baseDao.get(ID);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public T load(ID ID) {
        return this.baseDao.load(ID);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<T> getEntitys() {
        return this.baseDao.getEntitys();
    }

    public ID save(T T) {
        return this.baseDao.save(T);
    }

    @Override
    public List<ID> saveAll(Collection<T> TS) {
        return this.baseDao.saveAll(TS);
    }

    public void update(T T) {
        this.baseDao.update(T);
    }

    public void delete(T T) {
        this.baseDao.delete(T);
    }

    public void delete(ID ID) {
        this.baseDao.delete(ID);
    }

    public void delete(ID[] IDS) {
        this.baseDao.delete(IDS);
    }

    @Override
    public int delete(HqlHelper hqlHelper) {
        return this.baseDao.delete(hqlHelper);
    }

    @Override
    public List<T> deleteAndReturn(HqlHelper hqlHelper) {
        return this.baseDao.deleteAndReturn(hqlHelper);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public void flush() {
        this.baseDao.flush();
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public void evict(Object OBJ) {
        this.baseDao.evict(OBJ);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public void clear() {
        this.baseDao.clear();
    }

    @Override
    public T deleteAndReturn(ID ID) {
        return this.baseDao.deleteAndReturn(ID);
    }

    @Override
    public List<T> deleteAndReturn(ID[] IDS) {
        return this.baseDao.deleteAndReturn(IDS);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public PageModel getChartDataList(PageModel pageModel, HqlHelper helper, String ct_one_field, String ct_two_field) {
        return this.baseDao.getChartDataList(pageModel, helper, ct_one_field, ct_two_field);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public PageModel getEntitysPager(HqlHelper hqlHelper, PageModel pageModel) {
        return this.baseDao.getEntitysPager(hqlHelper, pageModel);
    }

    @Override
    public int update(HqlHelper hqlHelper) {
        return this.baseDao.update(hqlHelper);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<T> getEntitys(HqlHelper hqlHelper) {
        return this.baseDao.getEntitys(hqlHelper);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Long getCountByHql(HqlHelper hqlHelper, boolean isDistinct) {
        return this.baseDao.getCountByHql(hqlHelper, isDistinct);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Object getMaxByHql(HqlHelper hqlHelper) {
        return this.baseDao.getMaxByHql(hqlHelper);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Object getMinByHql(HqlHelper hqlHelper) {
        return this.baseDao.getMinByHql(hqlHelper);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Object getSumByHql(HqlHelper hqlHelper, boolean isDistinct) {
        return this.baseDao.getSumByHql(hqlHelper, isDistinct);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Double getAvgByHql(HqlHelper hqlHelper, boolean isDistinct) {
        return this.baseDao.getAvgByHql(hqlHelper, isDistinct);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Records getRecords(HqlHelper hqlHelper, boolean isDistinct) {
        return this.baseDao.getRecords(hqlHelper, isDistinct);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public PageModel getRecordsPager(PageModel pageModel, HqlHelper hqlHelper, boolean isDistinct) {
        return this.baseDao.getRecordsPager(pageModel, hqlHelper, isDistinct);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Records getRecordsGroup(HqlHelper hqlHelper) {
        return this.baseDao.getRecordsGroup(hqlHelper);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public T getEntity(HqlHelper hqlHelper) {
        return this.baseDao.getEntity(hqlHelper);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Record getRecord(HqlHelper hqlHelper) {
        return this.baseDao.getRecord(hqlHelper);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Record getRecordGroup(HqlHelper hqlHelper) {
        return this.baseDao.getRecordGroup(hqlHelper);
    }
}