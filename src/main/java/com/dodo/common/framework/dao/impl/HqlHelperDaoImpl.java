package com.dodo.common.framework.dao.impl;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.pagination.AbstractLimitHandler;
import org.hibernate.dialect.pagination.LimitHelper;
import org.hibernate.engine.spi.RowSelection;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.query.Query;
import org.hibernate.type.MapType;
import org.hibernate.type.SortedMapType;
import org.hibernate.type.Type;
import org.springframework.jdbc.core.PreparedStatementSetter;

import com.dodo.common.database.data.Record;
import com.dodo.common.database.data.Records;
import com.dodo.common.database.hql.DBException;
import com.dodo.common.database.hql.FetchField;
import com.dodo.common.database.hql.FunctionType;
import com.dodo.common.database.hql.GroupByFunction;
import com.dodo.common.database.hql.HavingQueryParameter;
import com.dodo.common.database.hql.HavingQueryParameters;
import com.dodo.common.database.hql.HqlHelper;
import com.dodo.common.database.hql.HqlHelper.MatchType;
import com.dodo.common.database.hql.HqlHelperException;
import com.dodo.common.database.hql.OrderByEntry;
import com.dodo.common.database.hql.QueryJoin;
import com.dodo.common.database.hql.QueryParameter;
import com.dodo.common.database.hql.QueryParameters;
import com.dodo.common.database.hql.UpdateParameter;
import com.dodo.common.database.hql.UpdateParameter.HelperUpdateType;
import com.dodo.common.framework.bean.pager.PageModel;
import com.dodo.common.framework.bean.pager.PageModel.OrderType;
import com.dodo.common.framework.dao.HqlHelperDao;
import com.dodo.common.framework.entity.BaseEntity;
import com.dodo.common.sqlreport.SqlReportLimit;
import com.dodo.privilege.entity.admin_1.base_1.Admin;
import com.dodo.utils.ReflectUtil;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class HqlHelperDaoImpl implements HqlHelperDao {
    protected SessionFactoryImplementor       sessionFactory;
    private static String                     idPropertyName;
    private static Map<String, ClassMetadata> classMetadatas = new HashMap<String, ClassMetadata>();

    public void setSessionFactory(SessionFactoryImplementor sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.sessionFactory.getMetamodel().entityPersisters().forEach((entityName, entityPersister) -> {
            classMetadatas.put(entityPersister.getEntityName(), entityPersister.getClassMetadata());
        });
        idPropertyName = classMetadatas.get(Admin.class.getName()).getIdentifierPropertyName();
    }

    protected Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }

    // insert
    @Override
    public <T extends BaseEntity> Serializable save(T T) {
        if ((T instanceof BaseEntity)) {
            try {
                T.getClass().getMethod("onSave", new Class[0]).invoke(T, new Object[0]);
                return getSession().save(T);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return getSession().save(T);
    }

    @Override
    public <T extends BaseEntity> List<Serializable> saveAll(List<T> TS) {
        List<Serializable> IDS = new ArrayList<Serializable>(TS.size());
        Method onSaveMethod = null;
        try {
            onSaveMethod = TS.get(0).getClass().getMethod("onSave", new Class[0]);
        } catch (Exception e) {
            e.printStackTrace();
            for (int i = 0; i < IDS.size(); i++) {
                IDS.add(null);
            }
            return IDS;
        }
        for (T T : TS) {
            try {
                onSaveMethod.invoke(T, new Object[0]);
                IDS.add((Serializable) getSession().save(T));
            } catch (Exception e) {
                e.printStackTrace();
                IDS.add(null);
            }
        }
        flush();
        clear();
        return IDS;
    }

    // delete 
    @Override
    public <T extends BaseEntity> void delete(T T) {
        getSession().delete(T);
    }

    @Override
    public <T extends BaseEntity> void delete(Serializable ID, Class<T> clazz) {
        if (ID != null) {
            getSession().delete(getSession().load(clazz, ID));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends BaseEntity> T deleteAndReturn(Serializable ID, Class<T> clazz) {
        if (ID == null) {
            return null;
        }
        Object o = getSession().get(clazz, ID);
        if (o != null) {
            getSession().delete(o);
        }
        return o == null ? null : (T) o;
    }

    @Override
    public <T extends BaseEntity> List<T> deleteAndReturn(Serializable[] IDS, Class<T> clazz) {
        List<T> list = new ArrayList<T>();
        int j = IDS.length;
        for (int i = 0; i < j; i++) {
            T t = deleteAndReturn(IDS[i], clazz);
            if (t != null) {
                list.add(t);
            }
        }
        return list;
    }

    @Override
    public <T extends BaseEntity> void delete(Serializable[] IDS, Class<T> clazz) {
        for (Serializable id : IDS) {
            getSession().delete(getSession().load(clazz, id));
        }
    }

    @Override
    public <T extends BaseEntity> int delete(HqlHelper hqlHelper) {
        if (hqlHelper.getJoinDomains().size() > 0) {
            throw new HqlHelperException("you can't use join tables in delete statement.");
        }
        StringBuilder whereBuilder = getWhereHql(hqlHelper.getFromClazz(), hqlHelper.getQueryParameters());
        Query<?> query = getSession().createQuery(getDeleteHql(hqlHelper.getFromClazz(), whereBuilder).toString());
        // where 语句赋值
        if (whereBuilder.length() > 0) {
            setQueryParameters(query, hqlHelper.getQueryParameters());
        }
        return query.executeUpdate();
    }

    @Override
    public <T extends BaseEntity> List<T> deleteAndReturn(HqlHelper hqlHelper) {
        List<T> list = getEntitys(hqlHelper);
        if (hqlHelper.getJoinDomains().size() > 0) {
            for (T t : list) {
                delete(t);
            }
        } else {
            delete(hqlHelper);
        }
        return list;
    }

    // update
    @Override
    public <T extends BaseEntity> void update(T T) {
        if ((T instanceof BaseEntity)) {
            try {
                T.getClass().getMethod("onUpdate", new Class[0]).invoke(T, new Object[0]);
                getSession().update(T);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            getSession().update(T);
        }
    }

    @Override
    public <T extends BaseEntity> int update(HqlHelper hqlHelper) {
        if (hqlHelper.getJoinDomains().size() > 0) {
            throw new HqlHelperException("you can't use join tables in update statement.");
        }
        Set<UpdateParameter> updateParameters = hqlHelper.getUpdateParameters();
        if (updateParameters == null || updateParameters.size() == 0) {
            throw new HqlHelperException("please give me some update fileds in update statement.");
        }
        int rowEffect = 0;
        StringBuilder setBuilder = getSetHql(hqlHelper.getFromClazz(), updateParameters);
        StringBuilder whereBuilder = getWhereHql(hqlHelper.getFromClazz(), hqlHelper.getQueryParameters());
        // 更新
        if (setBuilder.length() > 0) {
            Query<?> query = getSession().createQuery(
                    getUpdateHql(hqlHelper.getFromClazz(), setBuilder, whereBuilder).toString());
            // set 语句赋值
            for (UpdateParameter parameter : updateParameters) {
                if (parameter.getUpdateType() == HelperUpdateType.SQLSEGMENT || parameter.getIsCollection()) {
                    continue;
                }
                query.setParameter(parameter.getFieldName() + HqlHelper.setLast, parameter.getFieldValue());
            }

            // where 语句赋值
            if (whereBuilder.length() > 0) {
                setQueryParameters(query, hqlHelper.getQueryParameters());
            }
            rowEffect = query.executeUpdate();
        }
        // if has collection
        List<T> beans = null;
        for (UpdateParameter parameter : updateParameters) {
            if (parameter.getIsCollection()) {
                if (beans == null) {
                    beans = getEntitys(hqlHelper);
                    if (beans == null || beans.size() == 0) {
                        break;
                    }
                }
                for (T bean : beans) {
                    classMetadatas.get(hqlHelper.getFromClazz().getName()).setPropertyValue(bean,
                            parameter.getFieldName(), parameter.getFieldValue());
                }
            }
        }
        return rowEffect;
    }

    // query one
    @Override
    public <T extends BaseEntity> T get(Serializable ID, Class<T> clazz) {
        return (T) getSession().get(clazz, ID);
    }

    @Override
    public <T extends BaseEntity> T load(Serializable ID, Class<T> clazz) {
        return (T) getSession().load(clazz, ID);
    }

    // query basic function 
    @Override
    public <T extends BaseEntity> Long getCountByHql(HqlHelper hqlHelper, boolean isDistinct) {
        if (hqlHelper.getFunctionField() == null) {
            throw new HqlHelperException(
                    "you must set function field with 'HqlHelper.function' when you use 'getCountByHql'");
        }
        StringBuilder sbBuilder = new StringBuilder(isDistinct ? " distinct " : "").append(hqlHelper.getFunctionField()
                .toFunctionString(false));
        return (Long) getQuery(hqlHelper.getFromClazz(), hqlHelper.getQueryParameters(), null, FunctionType.count,
                sbBuilder.toString(), false, false, null, null, hqlHelper.getJoinDomains()).uniqueResult();
    }

    @Override
    public <T extends BaseEntity> Object getMaxByHql(HqlHelper hqlHelper) {
        if (hqlHelper.getFunctionField() == null) {
            throw new HqlHelperException(
                    "you must set function field with 'HqlHelper.function' when you use 'getMaxByHql'");
        }
        return getQuery(hqlHelper.getFromClazz(), hqlHelper.getQueryParameters(), null, FunctionType.max,
                hqlHelper.getFunctionField().toFunctionString(false).toString(), false, false, null, null,
                hqlHelper.getJoinDomains()).uniqueResult();
    }

    @Override
    public <T extends BaseEntity> Object getMinByHql(HqlHelper hqlHelper) {
        if (hqlHelper.getFunctionField() == null) {
            throw new HqlHelperException(
                    "you must set function field with 'HqlHelper.function' when you use 'getMinByHql'");
        }
        return getQuery(hqlHelper.getFromClazz(), hqlHelper.getQueryParameters(), null, FunctionType.min,
                hqlHelper.getFunctionField().toFunctionString(false).toString(), false, false, null, null,
                hqlHelper.getJoinDomains()).uniqueResult();
    }

    @Override
    public <T extends BaseEntity> Object getSumByHql(HqlHelper hqlHelper, boolean isDistinct) {
        if (hqlHelper.getFunctionField() == null) {
            throw new HqlHelperException("you must set some count fields when you use 'getSumByHql'");
        }
        return getQuery(hqlHelper.getFromClazz(), hqlHelper.getQueryParameters(), null, FunctionType.sum,
                hqlHelper.getFunctionField().toFunctionString(isDistinct).toString(), false, false, null, null,
                hqlHelper.getJoinDomains()).uniqueResult();
    }

    @Override
    public <T extends BaseEntity> Double getAvgByHql(HqlHelper hqlHelper, boolean isDistinct) {
        if (hqlHelper.getFunctionField() == null) {
            throw new HqlHelperException(
                    "you must set function field with 'HqlHelper.function' when you use 'getSumByHql'");
        }
        Object object = getQuery(hqlHelper.getFromClazz(), hqlHelper.getQueryParameters(), null, FunctionType.avg,
                hqlHelper.getFunctionField().toFunctionString(isDistinct).toString(), false, false, null, null,
                hqlHelper.getJoinDomains()).uniqueResult();
        return object != null ? (Double) object : null;
    }

    // query list
    @SuppressWarnings("unchecked")
    @Override
    public <T extends BaseEntity> List<T> getEntitys(Class<T> clazz) {
        return getSession().createQuery("from " + clazz.getName() + " as entity order by entity.createDate desc")
                .setCacheable(true).list();
    }

    // hql query
    @SuppressWarnings("unchecked")
    @Override
    public <T extends BaseEntity> List<T> getEntitys(HqlHelper hqlHelper) {
        Query<T> query = getQuery(hqlHelper.getFromClazz(), hqlHelper.getQueryParameters(),
                hqlHelper.getOrderByConditions(), null, null, false, false, null, null, hqlHelper.getJoinDomains());
        if (hqlHelper.getFirstResult() != null) {
            query.setFirstResult(hqlHelper.getFirstResult());
            if (hqlHelper.getMaxResults() == null) {
                throw new HqlHelperException("you must use setMaxResults the same time when use setFirstResult.");
            }
        }
        if (hqlHelper.getMaxResults() != null) {
            query.setMaxResults(hqlHelper.getMaxResults());
            if (hqlHelper.getFirstResult() == null) {
                throw new HqlHelperException("you must use setFirstResult the same time when use setMaxResults.");
            }
        }
        return query.list();
    }

    @Override
    public <T extends BaseEntity> PageModel getEntitysPager(HqlHelper hqlHelper, PageModel pageModel) {
        if (hqlHelper.getOrderByConditions().size() == 0) {
            hqlHelper.orderBy(pageModel.getOrderBy(), pageModel.getOrder());
        }
        Query<?> query = getQuery(hqlHelper.getFromClazz(), hqlHelper.getQueryParameters(),
                hqlHelper.getOrderByConditions(), null, null, false, false, null, null, hqlHelper.getJoinDomains());
        query.setFirstResult((pageModel.getPageNumber() - 1) * pageModel.getPageSize());
        query.setMaxResults(pageModel.getPageSize());

        hqlHelper.function(HqlHelper.currTable, idPropertyName);
        pageModel.setTotalCount(getCountByHql(hqlHelper, Boolean.FALSE).intValue());
        pageModel.setResult(query.list());
        return pageModel;
    }

    @Override
    public <T extends BaseEntity> T getEntity(HqlHelper hqlHelper) {
        hqlHelper.setFirstResult(0).setMaxResults(1);
        List<T> list = getEntitys(hqlHelper);
        return list.size() == 1 ? list.get(0) : null;
    }

    @Override
    public <T extends BaseEntity> Record getRecord(HqlHelper hqlHelper) {
        hqlHelper.setFirstResult(0).setMaxResults(1);
        Records records = getRecords(hqlHelper, false);
        return records.size() == 1 ? records.get(0) : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends BaseEntity> Records getRecords(HqlHelper hqlHelper, boolean isDistinct) {
        if (hqlHelper.getFetchFields().size() == 0) {
            throw new HqlHelperException("please give me some select fileds in select statement.");
        }
        List<EntityFieldFetch> entityFieldFetchs = new ArrayList<EntityFieldFetch>();
        Query<Map<String, Object>> query = getQuery(hqlHelper.getFromClazz(), hqlHelper.getQueryParameters(),
                hqlHelper.getOrderByConditions(), null, null, isDistinct, hqlHelper.getIsEntityFieldSubSelect(),
                entityFieldFetchs, hqlHelper.getFetchFields(), hqlHelper.getJoinDomains());
        if (hqlHelper.getFirstResult() != null) {
            query.setFirstResult(hqlHelper.getFirstResult());
            if (hqlHelper.getMaxResults() == null) {
                throw new HqlHelperException("you must use setMaxResults the same time when use setFirstResult.");
            }
        }
        if (hqlHelper.getMaxResults() != null) {
            query.setMaxResults(hqlHelper.getMaxResults());
            if (hqlHelper.getFirstResult() == null) {
                throw new HqlHelperException("you must use setFirstResult the same time when use setMaxResults.");
            }
        }
        List<Map<String, Object>> results = query.list();
        dealEntityFetchs(entityFieldFetchs, results, hqlHelper.getFromClazz());
        return new Records(results);
    }

    @Override
    public <T extends BaseEntity> Record getRecordGroup(HqlHelper hqlHelper) {
        hqlHelper.setFirstResult(0).setMaxResults(1);
        Records records = getRecordsGroup(hqlHelper);
        return records.size() == 1 ? records.get(0) : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends BaseEntity> Records getRecordsGroup(HqlHelper hqlHelper) {
        if (hqlHelper.getGroupByFunctions().size() == 0) {
            throw new HqlHelperException("please give me some group functions in group by statement.");
        }
        Query<Map<String, Object>> query = getQueryGroup(hqlHelper.getFromClazz(), hqlHelper.getQueryParameters(),
                hqlHelper.getHavingQueryParameters(), hqlHelper.getOrderByConditions(), hqlHelper.getJoinDomains(),
                hqlHelper.getGroupByPropertys(), hqlHelper.getGroupByFunctions());

        if (hqlHelper.getFirstResult() != null) {
            query.setFirstResult(hqlHelper.getFirstResult());
            if (hqlHelper.getMaxResults() == null) {
                throw new HqlHelperException("you must use setMaxResults the same time when use setFirstResult.");
            }
        }
        if (hqlHelper.getMaxResults() != null) {
            query.setMaxResults(hqlHelper.getMaxResults());
            if (hqlHelper.getFirstResult() == null) {
                throw new HqlHelperException("you must use setFirstResult the same time when use setMaxResults.");
            }
        }

        return new Records(query.list());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends BaseEntity> PageModel getRecordsPager(PageModel pageModel, HqlHelper hqlHelper, boolean isDistinct) {
        if (hqlHelper.getFetchFields().size() == 0) {
            throw new HqlHelperException("please give me some select fileds in select statement.");
        }
        List<EntityFieldFetch> entityFieldFetchs = new ArrayList<EntityFieldFetch>();
        if (hqlHelper.getOrderByConditions().size() == 0) {
            hqlHelper.orderBy(pageModel.getOrderBy(), pageModel.getOrder());
        }
        Query<Map<String, Object>> query = getQuery(hqlHelper.getFromClazz(), hqlHelper.getQueryParameters(),
                hqlHelper.getOrderByConditions(), null, null, isDistinct, hqlHelper.getIsEntityFieldSubSelect(),
                entityFieldFetchs, hqlHelper.getFetchFields(), hqlHelper.getJoinDomains());
        query.setFirstResult((pageModel.getPageNumber() - 1) * pageModel.getPageSize());
        query.setMaxResults(pageModel.getPageSize());

        pageModel.setTotalCount(getCountByHql(hqlHelper, isDistinct).intValue());
        pageModel.setRecords(new Records(query.list()));
        dealEntityFetchs(entityFieldFetchs, (List<Map<String, Object>>) pageModel.getResult(), hqlHelper.getFromClazz());
        return pageModel;
    }

    //query chart data
    @Override
    public <T extends BaseEntity> PageModel getChartDataList(PageModel pageModel, HqlHelper helper,
            String ct_one_field, String ct_two_field) {
        if (StringUtils.isNotBlank(ct_one_field)) {
            helper.orderBy(ct_one_field, OrderType.asc);
        }
        if (StringUtils.isNotBlank(ct_two_field)) {
            helper.orderBy(ct_two_field, OrderType.asc);
        }
        return getEntitysPager(helper, pageModel);
    }

    // other
    @Override
    public void flush() {
        getSession().flush();
    }

    @Override
    public void evict(Object OBJ) {
        getSession().evict(OBJ);
    }

    @Override
    public void clear() {
        getSession().clear();
    }

    // private method
    // hql helper
    @SuppressWarnings("unchecked")
    private <T> void dealEntityFetchs(List<EntityFieldFetch> entityFieldFetchs, List<Map<String, Object>> results,
            Class<T> clazz) {
        if (entityFieldFetchs.size() == 0) {
            return;
        }
        Collections.sort(entityFieldFetchs, new Comparator<EntityFieldFetch>() {
            @Override
            public int compare(EntityFieldFetch o1, EntityFieldFetch o2) {
                return (o1.getFieldClass() + "").compareTo(o2.getFieldClass() + "");
            }
        });
        String currFieldName = null;
        Class<?> currFieldCls = null;
        Map<String, String> execSql = new HashMap<String, String>();
        StringBuilder sBuilder = new StringBuilder();
        boolean isFieldSub = false;
        List<EntityFieldFetch> collectionFields = new ArrayList<EntityFieldFetch>();
        for (EntityFieldFetch entityFieldFetch : entityFieldFetchs) {
            if (entityFieldFetch.getFieldClass() == null) {
                collectionFields.add(entityFieldFetch);
                continue;
            }
            isFieldSub = true;
            if (currFieldName != null && !currFieldName.equals(entityFieldFetch.getFieldName())) {
                sBuilder.deleteCharAt(sBuilder.length() - 1).append(") from ").append(currFieldCls.getSimpleName())
                        .append(" t").append(" where ").append(" t.").append(idPropertyName).append("=:id");
                execSql.put(currFieldName, "select new map(" + sBuilder.toString());
                sBuilder.delete(0, sBuilder.length());
            }
            currFieldName = entityFieldFetch.getFieldName();
            currFieldCls = entityFieldFetch.getFieldClass();
            sBuilder.append("t").append(entityFieldFetch.getFetchName().substring(currFieldName.length()))
                    .append(" as ").append(entityFieldFetch.getFetchName().replaceAll("\\.", "_")).append(",");
        }
        /** 如果包含一对一查询 **/
        if (isFieldSub) {
            sBuilder.deleteCharAt(sBuilder.length() - 1).append(") from ").append(currFieldCls.getSimpleName())
                    .append(" t").append(" where ").append(" t.").append(idPropertyName).append("=:id");
            execSql.put(currFieldName, "select new map(" + sBuilder.toString());

            for (Map<String, Object> res : results) {
                for (EntityFieldFetch entityFieldFetch : entityFieldFetchs) {
                    if (entityFieldFetch.getFieldClass() != null) {
                        res.put(entityFieldFetch.getFetchName().replaceAll("\\.", "_"), null);
                    }
                }
            }

            Iterator<String> keyIt = execSql.keySet().iterator();
            String tempName = null;
            List<Map<String, Object>> tempSubRes = null;
            while (keyIt.hasNext()) {
                tempName = (String) keyIt.next();
                Query<Map<String, Object>> query = getSession().createQuery(execSql.get(tempName));
                for (Map<String, Object> res : results) {
                    if (res.get(tempName + "_" + idPropertyName) != null) {
                        query.setParameter("id", res.get(tempName + "_" + idPropertyName));
                        tempSubRes = query.list();
                        if (tempSubRes.size() > 0) {
                            res.putAll(tempSubRes.get(0));
                        }
                    }
                }
            }
        }
        /** 如果包含集合字段查询 **/
        if (collectionFields.size() > 0) {
            Collections.sort(collectionFields, new Comparator<EntityFieldFetch>() {
                @Override
                public int compare(EntityFieldFetch o1, EntityFieldFetch o2) {
                    return o1.getFieldName().compareTo(o2.getFieldName());
                }
            });

            currFieldName = null;
            execSql.clear();
            sBuilder.delete(0, sBuilder.length());
            Boolean isQuerySelf = Boolean.FALSE;
            EntityFieldFetch entityFieldFetch = null;
            int size = collectionFields.size();
            for (int i = 0; i < size; i++) {
                entityFieldFetch = collectionFields.get(i);
                if (currFieldName != null && !currFieldName.equals(entityFieldFetch.getFieldName())) {
                    sBuilder.deleteCharAt(sBuilder.length() - 1);
                    if (!isQuerySelf) {
                        sBuilder.append(")");
                    }
                    sBuilder.append(" from ").append(clazz.getSimpleName()).append(" t join t.").append(currFieldName)
                            .append(" x where ").append(" t.").append(idPropertyName).append("=:id");
                    if (isQuerySelf) {
                        execSql.put(currFieldName, "select " + sBuilder.toString());
                    } else {
                        execSql.put(currFieldName, "select new map(" + sBuilder.toString());
                    }
                    sBuilder.delete(0, sBuilder.length());
                    isQuerySelf = Boolean.FALSE;
                }
                currFieldName = entityFieldFetch.getFieldName();
                if (entityFieldFetch.getIsMapField()) {
                    execSql.put(currFieldName, null);
                    currFieldName = null;
                    sBuilder.delete(0, sBuilder.length());
                    isQuerySelf = Boolean.FALSE;
                    continue;
                }
                if (entityFieldFetch.getFetchName().equals(currFieldName)) {
                    isQuerySelf = Boolean.TRUE;
                }
                sBuilder.append("x")
                        .append(entityFieldFetch.getFetchName().substring(currFieldName.length()))
                        .append(" as ")
                        .append(isQuerySelf ? "this" : entityFieldFetch.getFetchName()
                                .substring(currFieldName.length() + 1).replaceAll("\\.", "_")).append(",");
            }
            if (currFieldName != null) {
                sBuilder.deleteCharAt(sBuilder.length() - 1);
                if (!isQuerySelf) {
                    sBuilder.append(")");
                }
                sBuilder.append(" from ").append(clazz.getSimpleName()).append(" t join t.").append(currFieldName)
                        .append(" x where ").append(" t.").append(idPropertyName).append("=:id");
                if (isQuerySelf) {
                    execSql.put(currFieldName, "select " + sBuilder.toString());
                } else {
                    execSql.put(currFieldName, "select new map(" + sBuilder.toString());
                }
            }

            Iterator<String> keyIt = execSql.keySet().iterator();
            String tempName = null;
            while (keyIt.hasNext()) {
                tempName = (String) keyIt.next();
                if (execSql.get(tempName) != null) {
                    Query<?> query = getSession().createQuery(execSql.get(tempName));
                    for (Map<String, Object> res : results) {
                        if (res.get(idPropertyName) != null) {
                            query.setParameter("id", res.get(idPropertyName));
                            res.put(tempName, query.list());
                        }
                    }
                } else {
                    for (Map<String, Object> res : results) {
                        if (res.get(idPropertyName) != null) {
                            Object object = getSession().get(clazz, (Serializable) res.get(idPropertyName));
                            Map<String, String> valueMap = (Map<String, String>) ReflectUtil.getValueByGetMethod(
                                    object, tempName);
                            if (valueMap.size() > 0) {
                                res.put(tempName, valueMap);
                            } else {
                                res.put(tempName, new HashMap<String, String>(0));
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings({ "unchecked" })
    private void setQueryParameters(Query<?> query, QueryParameters queryParameters) {
        for (QueryParameter parameter : queryParameters.getParameters()) {
            if (parameter.getOperateType().isPropertyCompare()) {
                continue;
            }
            switch (parameter.getOperateType()) {
            case in:
            case notIn:
                if (parameter.getPropertyValues()[0].getClass().isArray()) {
                    query.setParameterList(parameter.getPlaceholder1(), (Object[]) (parameter.getPropertyValues()[0]));
                } else {
                    query.setParameterList(parameter.getPlaceholder1(),
                            (Collection<Object>) (parameter.getPropertyValues()[0]));
                }
                break;
            case between:
            case notBetween:
                query.setParameter(parameter.getPlaceholder1(), parameter.getPropertyValues()[0]);
                query.setParameter(parameter.getPlaceholder2(), parameter.getPropertyValues()[1]);
                break;
            case like:
            case ilike:
            case notLike:
            case notiLike:
                if (parameter.getMatchType() == MatchType.START) {
                    query.setParameter(parameter.getPlaceholder1(), parameter.getPropertyValues()[0] + "%%");
                } else if (parameter.getMatchType() == MatchType.END) {
                    query.setParameter(parameter.getPlaceholder1(), "%%" + parameter.getPropertyValues()[0]);
                } else {
                    query.setParameter(parameter.getPlaceholder1(), "%%" + parameter.getPropertyValues()[0] + "%%");
                }
                break;
            default:
                query.setParameter(parameter.getPlaceholder1(), parameter.getPropertyValues()[0]);
                break;
            }
        }
    }

    // add for having
    @SuppressWarnings({ "unchecked" })
    private void setHavingQueryParameters(Query<?> query, HavingQueryParameters queryParameters) {
        for (HavingQueryParameter parameter : queryParameters.getParameters()) {
            switch (parameter.getOperateType()) {
            case in:
            case notIn:
                if (parameter.getPropertyValues()[0].getClass().isArray()) {
                    query.setParameterList(parameter.getPlaceholder1(), (Object[]) (parameter.getPropertyValues()[0]));
                } else {
                    query.setParameterList(parameter.getPlaceholder1(),
                            (Collection<Object>) (parameter.getPropertyValues()[0]));
                }
                break;
            case between:
            case notBetween:
                query.setParameter(parameter.getPlaceholder1(), parameter.getPropertyValues()[0]);
                query.setParameter(parameter.getPlaceholder2(), parameter.getPropertyValues()[1]);
                break;
            case like:
            case ilike:
            case notLike:
            case notiLike:
                if (parameter.getMatchType() == MatchType.START) {
                    query.setParameter(parameter.getPlaceholder1(), parameter.getPropertyValues()[0] + "%%");
                } else if (parameter.getMatchType() == MatchType.END) {
                    query.setParameter(parameter.getPlaceholder1(), "%%" + parameter.getPropertyValues()[0]);
                } else {
                    query.setParameter(parameter.getPlaceholder1(), "%%" + parameter.getPropertyValues()[0] + "%%");
                }
                break;
            default:
                query.setParameter(parameter.getPlaceholder1(), parameter.getPropertyValues()[0]);
                break;
            }
        }
    }

    private StringBuilder getSetHql(Class<?> clazz, Set<UpdateParameter> updateParameters) {
        StringBuilder setBuilder = new StringBuilder("");
        for (UpdateParameter parameter : updateParameters) {
            if (parameter.getFieldName().equals(idPropertyName)) {
                throw new HqlHelperException("Can't Update identifier property.");
            }
            if (parameter.getFieldName().contains(".")) {
                throw new HqlHelperException("Can't Update reference property.");
            }
            Type type = classMetadatas.get(clazz.getName()).getPropertyType(parameter.getFieldName());
            if (type.isCollectionType()) {
                parameter.setIsCollection(Boolean.TRUE);
                parameter.setIsMapField(type instanceof MapType);
                parameter.setIsSortedMapField(type instanceof SortedMapType);
                continue;
            }
            setBuilder.append(parameter.toHqlString()).append(",");

        }
        if (setBuilder.length() > 0) {
            setBuilder.deleteCharAt(setBuilder.length() - 1);
        }
        return setBuilder;
    }

    private StringBuilder getWhereHql(Class<?> clazz, QueryParameters queryParameters) {
        ClassMetadata classMetadata = classMetadatas.get(clazz.getName());
        StringBuilder whereBuilder = new StringBuilder("");
        StringBuilder exceptionBuilder = new StringBuilder("");
        if (queryParameters != null) {
            List<QueryParameters.BeginOperator> begin = null;
            Integer end = null;
            for (QueryParameter parameter : queryParameters.getParameters()) {
                if (HqlHelper.currTable.equals(parameter.getTableAlias1())) {
                    if (!parameter.isCollectionOperator() && (!parameter.getPropertyName().contains("."))
                            && classMetadata.getPropertyType(parameter.getPropertyName()).isCollectionType()) {
                        throw new HqlHelperException(exceptionBuilder
                                .append("The where statement cannot contain a collection :")
                                .append(parameter.getPropertyName()).append(" when you use ")
                                .append(parameter.getOperateType().name()).toString());
                    }
                    if (parameter.getPropertyName().contains(".")
                            && (classMetadata.getPropertyType(StringUtils.substringBefore(parameter.getPropertyName(),
                                    ".")) instanceof MapType)) {
                        parameter.toMapPropertyName();
                    }
                }
                if (parameter.getPropertyName1() != null
                        && HqlHelper.currTable.equals(parameter.getTableAlias2())
                        && parameter.getPropertyName1().contains(".")
                        && (classMetadata
                                .getPropertyType(StringUtils.substringBefore(parameter.getPropertyName1(), ".")) instanceof MapType)) {
                    parameter.toMapPropertyName1();
                }
                begin = queryParameters.beginStatus(parameter.getSeqNum());
                end = queryParameters.endStatus(parameter.getSeqNum());
                if (begin != null) {
                    if (end != null) {
                        for (int i = 0; i < end; i++) {
                            whereBuilder.append(" ) ");
                        }
                    }
                    whereBuilder.append(" ");
                    whereBuilder.append(begin.get(0).getOperator());
                    whereBuilder.append(" ( ");
                    for (int j = 1; j < begin.size(); j++) {
                        whereBuilder.append(" ( ");
                    }
                } else if (end != null) {
                    for (int i = 0; i < end; i++) {
                        whereBuilder.append(" ) ");
                    }
                    whereBuilder.append(" and  ");
                } else {
                    whereBuilder.append(" and  ");
                }
                whereBuilder.append(parameter.toHqlString());
            }
            end = queryParameters.endStatus(queryParameters.getParameters().size());
            if (end != null) {
                for (int i = 0; i < end; i++) {
                    whereBuilder.append(" ) ");
                }
            }
            if (whereBuilder.length() > 0) {
                whereBuilder.delete(0, 5);
            }
        }
        return whereBuilder;
    }

    // add for having
    private StringBuilder getHavingHql(HavingQueryParameters queryParameters) {
        StringBuilder havingBuilder = new StringBuilder("");
        if (queryParameters != null) {
            List<HavingQueryParameters.BeginOperator> begin = null;
            Integer end = null;
            for (HavingQueryParameter parameter : queryParameters.getParameters()) {
                begin = queryParameters.beginStatus(parameter.getSeqNum());
                end = queryParameters.endStatus(parameter.getSeqNum());
                if (begin != null) {
                    if (end != null) {
                        for (int i = 0; i < end; i++) {
                            havingBuilder.append(" ) ");
                        }
                    }
                    havingBuilder.append(" ");
                    havingBuilder.append(begin.get(0).getOperator());
                    havingBuilder.append(" ( ");
                    for (int j = 1; j < begin.size(); j++) {
                        havingBuilder.append(" ( ");
                    }
                } else if (end != null) {
                    for (int i = 0; i < end; i++) {
                        havingBuilder.append(" ) ");
                    }
                    havingBuilder.append(" and  ");
                } else {
                    havingBuilder.append(" and  ");
                }
                havingBuilder.append(parameter.toHqlString());
            }
            end = queryParameters.endStatus(queryParameters.getParameters().size());
            if (end != null) {
                for (int i = 0; i < end; i++) {
                    havingBuilder.append(" ) ");
                }
            }
            if (havingBuilder.length() > 0) {
                havingBuilder.delete(0, 5);
            }
        }
        return havingBuilder;
    }

    private StringBuilder getOrderByHql(Class<?> clazz, List<OrderByEntry> orderByConditions,
            List<GroupByFunction> groupByFunctions) {
        StringBuilder orderByBuilder = new StringBuilder("");
        if (orderByConditions != null) {
            ClassMetadata classMetadata = classMetadatas.get(clazz.getName());
            for (OrderByEntry entry : orderByConditions) {
                if (HqlHelper.currTable.equals(entry.getTableAlias())) {
                    if (!entry.getFieldName().contains(".")
                            && classMetadata.getPropertyType(entry.getFieldName()).isCollectionType()) {
                        continue;
                    }
                    if (entry.getFieldName().contains(".")
                            && (classMetadata.getPropertyType(StringUtils.substringBefore(entry.getFieldName(), ".")) instanceof MapType)) {
                        entry.toMapFieldName();
                    }
                }
                if (entry.getOrderType() == null) {
                    continue;
                }
                orderByBuilder.append(entry.toHqlString()).append(",");
            }
        }
        if (groupByFunctions != null) {
            String tempStr = null;
            for (GroupByFunction groupByFunction : groupByFunctions) {
                tempStr = groupByFunction.toHqlStringOrderBy();
                if (tempStr == null) {
                    continue;
                }
                orderByBuilder.append(groupByFunction.toHqlStringOrderBy()).append(",");
            }
        }
        if (orderByBuilder.length() > 0) {
            orderByBuilder.deleteCharAt(orderByBuilder.length() - 1);
        }
        return orderByBuilder;
    }

    private StringBuilder getUpdateHql(Class<?> clazz, StringBuilder setBuilder, StringBuilder whereBuilder) {
        StringBuilder updateBuilder = new StringBuilder("update ").append(clazz.getSimpleName()).append(" ")
                .append(HqlHelper.currTable).append(" set ").append(setBuilder);
        if (whereBuilder != null && whereBuilder.length() > 0) {
            updateBuilder.append(" where ").append(whereBuilder);
        }
        return updateBuilder;
    }

    private StringBuilder getDeleteHql(Class<?> clazz, StringBuilder whereBuilder) {
        StringBuilder deleteBuilder = new StringBuilder("delete ").append(clazz.getSimpleName()).append(" ")
                .append(HqlHelper.currTable).append(" ");
        if (whereBuilder != null && whereBuilder.length() > 0) {
            deleteBuilder.append(" where ").append(whereBuilder);
        }
        return deleteBuilder;
    }

    private StringBuilder getQueryHql(Class<?> clazz, StringBuilder whereBuilder, StringBuilder orderByBuilder,
            FunctionType operateType, String propertyName, boolean isDistinct,
            List<EntityFieldFetch> entityFieldFetchs, boolean isEntitySubSelect, Set<FetchField> fetchProperties,
            List<QueryJoin> queryJoins, Set<FetchField> groupByPropertys, List<GroupByFunction> groupByFunctions) {
        return getQueryHql(clazz, whereBuilder, orderByBuilder, operateType, propertyName, isDistinct,
                entityFieldFetchs, isEntitySubSelect, fetchProperties, queryJoins, groupByPropertys, groupByFunctions,
                null);
    }

    private StringBuilder getQueryHql(Class<?> clazz, StringBuilder whereBuilder, StringBuilder orderByBuilder,
            FunctionType operateType, String propertyName, boolean isDistinct,
            List<EntityFieldFetch> entityFieldFetchs, boolean isEntitySubSelect, Set<FetchField> fetchProperties,
            List<QueryJoin> queryJoins, Set<FetchField> groupByPropertys, List<GroupByFunction> groupByFunctions,
            StringBuilder havingBuilder) {
        if (isDistinct && isEntitySubSelect) {
            throw new HqlHelperException("You can't use 'distinct=true' and 'isEntitySubSelect=true' at the same time.");
        }
        if (queryJoins.size() > 0 && isEntitySubSelect) {
            throw new HqlHelperException(
                    "You can't use 'isEntitySubSelect=true' and 'join table query' at the same time.");
        }
        ClassMetadata classMetadata = classMetadatas.get(clazz.getName());
        StringBuilder queryBuilder = new StringBuilder("");
        Type fieldType = null;
        String fieldName = null;
        StringBuilder entityFieldNames = new StringBuilder("");
        if (operateType != null) {
            queryBuilder.append("select ").append(operateType.name()).append("(").append(propertyName).append(") ");
        } else if (fetchProperties != null && fetchProperties.size() > 0) {
            queryBuilder.append("select new map(");
            boolean isFindId = false;
            Set<String> fieldAliasSet = new HashSet<String>();
            Set<String> tableAliasSet = new HashSet<String>(queryJoins.size() + 1);
            tableAliasSet.add(HqlHelper.currTable);
            for (QueryJoin queryJoin : queryJoins) {
                if (tableAliasSet.contains(queryJoin.getDomainAlias())) {
                    throw new HqlHelperException("Duplicate join table alias : " + queryJoin.getDomainAlias());
                }
                tableAliasSet.add(queryJoin.getDomainAlias());
            }
            for (QueryJoin queryJoin : queryJoins) {
                if (queryJoin.getFieldAlias() != null && !tableAliasSet.contains(queryJoin.getFieldAlias())) {
                    throw new HqlHelperException("Unknown join table alias : " + queryJoin.getFieldAlias());
                }
            }
            // 单表查询
            if (queryJoins.size() == 0) {
                for (FetchField fetchField : fetchProperties) {
                    if (!tableAliasSet.contains(fetchField.getTableAlias())) {
                        throw new HqlHelperException("Unknown join table alias : " + fetchField.getTableAlias());
                    }
                    if (fetchField.getFieldNames().equals(idPropertyName)) {
                        isFindId = true;
                    }
                    // 去除重复查询 不能查询集合
                    fieldName = fetchField.getFirstField();
                    fieldType = classMetadata.getPropertyType(fieldName);
                    if (fieldType.isCollectionType()) {
                        if (isDistinct) {
                            throw new HqlHelperException(
                                    "Select property can't be a collection when you set 'distinct=true'");
                        }
                        entityFieldFetchs.add(new EntityFieldFetch(fetchField.getFieldNames(), fieldName, null,
                                fieldType instanceof MapType));
                        continue;
                    }
                    if (isEntitySubSelect && fieldType.isEntityType()) {
                        if (!fetchField.getFieldNames().equals(fieldName + "." + idPropertyName)) {
                            entityFieldFetchs.add(new EntityFieldFetch(fetchField.getFieldNames(), fieldName, fieldType
                                    .getReturnedClass(), false));
                        }
                        if (entityFieldNames.indexOf(fieldName + ",") == -1) {
                            entityFieldNames.append(fieldName).append(",");
                            queryBuilder.append(fetchField.getTableAlias()).append(".").append(fieldName).append(".")
                                    .append(idPropertyName).append(" as ").append(fieldName).append("_")
                                    .append(idPropertyName).append(",");
                        }
                    } else {
                        queryBuilder.append(fetchField.toHqlString()).append(",");
                    }
                }
                if (!isFindId && !isDistinct) {
                    queryBuilder.append(HqlHelper.currTable).append(".").append(idPropertyName).append(" as ")
                            .append(idPropertyName).append(",");
                }
            }
            // 多表查询
            else {
                for (FetchField fetchField : fetchProperties) {
                    if (!tableAliasSet.contains(fetchField.getTableAlias())) {
                        throw new HqlHelperException("Unknown join table alias : " + fetchField.getTableAlias());
                    }
                    //多表查询 不能查询集合
                    if (HqlHelper.currTable.equals(fetchField.getTableAlias())
                            && classMetadata.getPropertyType(fetchField.getFirstField()).isCollectionType()) {
                        throw new HqlHelperException(
                                "Select property can't be a collection when you use 'join table query'");
                    }
                    queryBuilder.append(fetchField.toHqlString()).append(",");
                    if (fieldAliasSet.contains(fetchField.getFieldAlias())) {
                        throw new HqlHelperException("Duplicate fetch field alias : " + fetchField.getFieldAlias());
                    }
                    fieldAliasSet.add(fetchField.getFieldAlias());
                }
            }

            queryBuilder.deleteCharAt(queryBuilder.length() - 1).append(") ");
            if (isDistinct) {
                queryBuilder.insert(7, " distinct ");
            }
        } else if (groupByFunctions != null && groupByFunctions.size() > 0) {
            queryBuilder.append("select new map(");
            Set<String> fieldAliasSet = new HashSet<String>();
            Set<String> tableAliasSet = new HashSet<String>(queryJoins.size() + 1);
            tableAliasSet.add(HqlHelper.currTable);
            for (QueryJoin queryJoin : queryJoins) {
                if (tableAliasSet.contains(queryJoin.getDomainAlias())) {
                    throw new HqlHelperException("Duplicate join table alias : " + queryJoin.getDomainAlias());
                }
                tableAliasSet.add(queryJoin.getDomainAlias());
            }
            for (QueryJoin queryJoin : queryJoins) {
                if (queryJoin.getFieldAlias() != null && !tableAliasSet.contains(queryJoin.getFieldAlias())) {
                    throw new HqlHelperException("Unknown join table alias : " + queryJoin.getFieldAlias());
                }
            }

            // 分组属性
            for (FetchField fetchField : groupByPropertys) {
                if (!tableAliasSet.contains(fetchField.getTableAlias())) {
                    throw new HqlHelperException("Unknown join table alias : " + fetchField.getTableAlias());
                }
                // 不能查询集合
                if (HqlHelper.currTable.equals(fetchField.getTableAlias())
                        && classMetadata.getPropertyType(fetchField.getFirstField()).isCollectionType()) {
                    throw new HqlHelperException(
                            "Select property can't be a collection when you use 'join table query'");
                }
                queryBuilder.append(fetchField.toHqlString()).append(",");
                if (fieldAliasSet.contains(fetchField.getFieldAlias())) {
                    throw new HqlHelperException("Duplicate fetch field alias : " + fetchField.getFieldAlias());
                }
                fieldAliasSet.add(fetchField.getFieldAlias());
            }

            //组函数
            for (GroupByFunction groupByFunction : groupByFunctions) {
                if (!tableAliasSet.contains(groupByFunction.getTableAlias())) {
                    throw new HqlHelperException("Unknown join table alias : " + groupByFunction.getTableAlias());
                }
                // 不能查询集合
                if (HqlHelper.currTable.equals(groupByFunction.getTableAlias())
                        && classMetadata.getPropertyType(groupByFunction.getFieldName()).isCollectionType()) {
                    throw new HqlHelperException(
                            "Select property can't be a collection when you use 'join table query'");
                }
                queryBuilder.append(groupByFunction.toHqlString()).append(",");
                if (fieldAliasSet.contains(groupByFunction.getFieldAlias())) {
                    throw new HqlHelperException("Duplicate fetch field alias : " + groupByFunction.getFieldAlias());
                }
                fieldAliasSet.add(groupByFunction.getFieldAlias());
            }

            queryBuilder.deleteCharAt(queryBuilder.length() - 1).append(") ");
        }

        if (queryBuilder.length() == 0) {
            queryBuilder.append("select ").append(HqlHelper.currTable).append(" ");
        }

        queryBuilder.append(" from ").append(clazz.getSimpleName()).append(" ").append(HqlHelper.currTable).append(" ");
        for (QueryJoin queryJoin : queryJoins) {
            queryBuilder.append(queryJoin.getJoinType().getOperator());
            if (queryJoin.getFieldAlias() != null) {
                queryBuilder.append(queryJoin.getFieldAlias());
                queryBuilder.append(".");
            }
            queryBuilder.append(queryJoin.getDomainName()).append(" ").append(queryJoin.getDomainAlias()).append(" ");
        }

        if (whereBuilder != null && whereBuilder.length() > 0) {
            queryBuilder.append(" where ").append(whereBuilder);
        }
        if (groupByFunctions != null && groupByFunctions.size() > 0 && groupByPropertys != null
                && groupByPropertys.size() > 0) {
            queryBuilder.append("group by ");
            for (FetchField fetchField : groupByPropertys) {
                queryBuilder.append(fetchField.getTableAlias()).append(".").append(fetchField.getFieldNames())
                        .append(",");
            }
            queryBuilder.deleteCharAt(queryBuilder.length() - 1);
        }
        if (havingBuilder != null && havingBuilder.length() > 0) {
            queryBuilder.append(" having ").append(havingBuilder);
        }
        if (orderByBuilder != null && orderByBuilder.length() > 0) {
            queryBuilder.append(" order by ").append(orderByBuilder);
        }
        return queryBuilder;
    }

    @SuppressWarnings("rawtypes")
    private <T extends BaseEntity> Query getQuery(Class<T> clazz, QueryParameters queryParameters,
            List<OrderByEntry> orderByConditions, FunctionType operateType, String propertyName, boolean isDistinct,
            boolean isEntitySubSelect, List<EntityFieldFetch> entityFieldFetchs, Set<FetchField> fetchProperties,
            List<QueryJoin> queryJoins) {
        StringBuilder whereBuilder = getWhereHql(clazz, queryParameters);
        StringBuilder orderByBuilder = getOrderByHql(clazz, operateType == null ? orderByConditions : null, null);
        Query query = getSession().createQuery(
                getQueryHql(clazz, whereBuilder, orderByBuilder, operateType, propertyName, isDistinct,
                        entityFieldFetchs, isEntitySubSelect, fetchProperties, queryJoins, null, null).toString());
        // where 语句赋值
        if (whereBuilder.length() > 0) {
            setQueryParameters(query, queryParameters);
        }
        return query;
    }

    @SuppressWarnings("rawtypes")
    private <T extends BaseEntity> Query getQueryGroup(Class<T> clazz, QueryParameters queryParameters,
            HavingQueryParameters havingQueryParameters, List<OrderByEntry> orderByConditions,
            List<QueryJoin> queryJoins, Set<FetchField> groupByPropertys, List<GroupByFunction> groupByFunctions) {
        StringBuilder whereBuilder = getWhereHql(clazz, queryParameters);
        StringBuilder havingBuilder = getHavingHql(havingQueryParameters);
        StringBuilder orderByBuilder = getOrderByHql(clazz, orderByConditions, groupByFunctions);

        Query query = getSession().createQuery(
                getQueryHql(clazz, whereBuilder, orderByBuilder, null, null, Boolean.FALSE, null, Boolean.FALSE, null,
                        queryJoins, groupByPropertys, groupByFunctions, havingBuilder).toString());
        // where 语句赋值
        if (whereBuilder.length() > 0) {
            setQueryParameters(query, queryParameters);
        }
        // having 语句赋值
        if (havingBuilder.length() > 0) {
            setHavingQueryParameters(query, havingQueryParameters);
        }
        return query;
    }

    private class EntityFieldFetch {
        private String   fetchName;
        private String   fieldName;
        private Class<?> fieldClass;
        private Boolean  isMapField;

        private EntityFieldFetch(String fetchName, String fieldName, Class<?> fieldClass, Boolean isMapField) {
            super();
            this.fetchName = fetchName;
            this.fieldName = fieldName;
            this.fieldClass = fieldClass;
            this.isMapField = isMapField;
        }

        public String getFetchName() {
            return fetchName;
        }

        public String getFieldName() {
            return fieldName;
        }

        public Class<?> getFieldClass() {
            return fieldClass;
        }

        public Boolean getIsMapField() {
            return isMapField;
        }

        @Override
        public String toString() {
            return "EntityFieldFetch [fetchName=" + fetchName + ", fieldName=" + fieldName + ", fieldClass="
                    + fieldClass + ", isMapField=" + isMapField + "]";
        }
    }

    //    private Class<?> getCurrClass() {
    //        return clazzThreadLocal.get();
    //    }

    //    public String getIdPropertyName() {
    //        return idPropertyName;
    //    }

    //    private ClassMetadata getClassMetadata() {
    //        return classMetadatas1.get(clazzThreadLocal.get());
    //    }

    @Override
    public SqlReportLimit getLimitSQL(String sql, String whereClause, final List<Object> whereClauseVars,
            int pageNumber, int pageSize) throws DBException {
        if (pageNumber <= 0) {
            pageNumber = 1;
        }
        if (pageSize <= 0) {
            pageSize = 20;
        }
        if (whereClause == null) {
            whereClause = "";
        }
        int firstRow = (pageNumber - 1) * pageSize;
        Dialect dialect = ((SessionFactoryImplementor) sessionFactory).getJdbcServices().getDialect();
        //AbstractLimitHandler
        AbstractLimitHandler limitHandler = (AbstractLimitHandler) dialect.getLimitHandler();
        RowSelection rowSelection = new RowSelection();
        rowSelection.setFirstRow(firstRow);
        rowSelection.setMaxRows(pageSize);
        if (limitHandler.supportsLimit()) {
            SqlReportLimit limit = new SqlReportLimit();
            limit.setLimitSql(limitHandler.processSql("select * from (" + sql.trim() + ") y where 1=1 " + whereClause,
                    rowSelection));
            limit.setPageNumber(pageNumber);
            limit.setPageSize(pageSize);
            Boolean isNeedBindLimitParameters = limitHandler.supportsVariableLimit()
                    && LimitHelper.hasMaxRows(rowSelection);
            Boolean isNeedBindWhereParameters = whereClauseVars != null && whereClauseVars.size() > 0;
            if (!isNeedBindLimitParameters && !isNeedBindWhereParameters) {
                return limit;
            }
            PreparedStatementSetter preparedStatementSetter = new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement arg0) throws SQLException {
                    int limitVarSeq = 1;
                    if (isNeedBindWhereParameters) {
                        for (Object wObject : whereClauseVars) {
                            arg0.setObject(limitVarSeq++, wObject);
                        }
                    }
                    if (limitHandler.bindLimitParametersFirst()) {
                        limitHandler.bindLimitParametersAtStartOfQuery(rowSelection, arg0, limitVarSeq);
                    } else {
                        limitHandler.bindLimitParametersAtEndOfQuery(rowSelection, arg0, limitVarSeq);
                    }
                }
            };
            limit.setPreparedStatementSetter(preparedStatementSetter);
            return limit;
        }

        throw new DBException("The Database Does not support page query.");
    }

    @Override
    public String getCountSQL(String sql, String whereClause) throws DBException {
        StringBuilder sbBuilder = new StringBuilder();
        if (StringUtils.isBlank(whereClause)) {
            sbBuilder.append("select count(*) from (").append(sql).append(") x").toString();
        } else {
            sbBuilder.append("select count(*) from (select * from (").append(sql).append(") x where 1=1 ")
                    .append(whereClause).append(") y").toString();
        }
        return sbBuilder.toString();
    }
}