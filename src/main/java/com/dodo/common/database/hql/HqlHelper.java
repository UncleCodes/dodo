package com.dodo.common.database.hql;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.dodo.common.database.hql.QueryParameters.BeginOperator;
import com.dodo.common.database.hql.UpdateParameter.HelperUpdateType;
import com.dodo.common.framework.bean.pager.PageModel.OrderType;
import com.dodo.common.framework.entity.BaseEntity;

/**
 * Map 字段已经实现 更新、做为查询条件、做为排序条件、查找回来全部 <br/>
 * 待实现 <br/>
 * 1、字符串连接符...||... or concat(...,...) <br/>
 * 2、"简单的" case, case ... when ... then ... else ... end,和 "搜索" case, case when
 * ... then ... else ... end <br/>
 * 3、second(...), minute(...), hour(...), day(...), month(...), year(...),<br/>
 * 4、substring(), trim(), lower(), upper(), length(), locate(), abs(), sqrt(),
 * bit_length() <br/>
 * 5、coalesce() 和 nullif() <br/>
 * 6、sign(), trunc(), rtrim(), sin() <br/>
 * 
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class HqlHelper {
    /**
     * 模糊查询的匹配类型
     */
    public enum MatchType {
        /**
         * 匹配全部
         */
        ANYWHERE,
        /**
         * 匹配开头
         */
        START,
        /**
         * 匹配结尾
         */
        END
    }

    /**
     * 表关联类型
     */
    public enum JoinType {
        /**
         * 内连接
         */
        INNER(" inner join "),
        /**
         * 外连接
         */
        LEFT_JOIN(" left join "), RIGHT_JOIN(" right join "),
        /**
         * 全连接
         */
        ENTITY_JOIN(" , ");
        private String operator;

        private JoinType(String operator) {
            this.operator = operator;
        }

        public String getOperator() {
            return operator;
        }
    }

    /**
     * 起始查询实体类别名
     */
    public final static String          currTable              = "_s_e_l_f_";
    /**
     * Update绑定变量的后缀
     */
    public final static String          setLast                = "_Set";
    /**
     * Where条件的绑定变量的后缀
     */
    public final static String          whereLast              = "_Where";
    /**
     * between 起始值的绑定变量后缀
     */
    public final static String          betweenLow             = "_Low";
    /**
     * between 结束值的绑定变量后缀
     */
    public final static String          betweenHign            = "_High";
    /**
     * 当查询的字段是实体类时，是否启用子查询，默认不启用
     */
    private Boolean                     isEntityFieldSubSelect = Boolean.FALSE;
    /**
     * 分页起始序号
     */
    private Integer                     firstResult;
    /**
     * 分页读取条数
     */
    private Integer                     maxResults;
    /**
     * 查询条件
     */
    private QueryParameters             queryParameters        = QueryParameters.newInstance();
    /**
     * having条件
     */
    private HavingQueryParameters       havingQueryParameters  = HavingQueryParameters.newInstance();
    /**
     * 排序条件
     */
    private List<OrderByEntry>          orderByConditions      = new LinkedList<OrderByEntry>();
    /**
     * Update 参数
     */
    private Set<UpdateParameter>        updateParameters       = new HashSet<UpdateParameter>();
    /**
     * 查询字段
     */
    private Set<FetchField>             fetchFields            = new HashSet<FetchField>();
    /**
     * 表关联
     */
    private List<QueryJoin>             joinDomains            = new LinkedList<QueryJoin>();
    /**
     * 组函数 count avg sum min max，时使用
     */
    private FetchField                  functionField          = null;
    /**
     * 分组字段
     */
    private Set<FetchField>             groupByPropertys       = new HashSet<FetchField>();
    /**
     * 分组函数
     */
    private List<GroupByFunction>       groupByFunctions       = new LinkedList<GroupByFunction>();
    /**
     * 起始查询实体类
     */
    private Class<? extends BaseEntity> fromClazz;

    private HqlHelper(Class<? extends BaseEntity> fromClazz) {
        updateParameters.add(new UpdateParameter("modifyDate", new Date(), HelperUpdateType.BINDVAR));
        this.fromClazz = fromClazz;
    }

    /**
     * 初始化，以 fromClazz 为起始查询实体类
     */
    public static HqlHelper queryFrom(Class<? extends BaseEntity> fromClazz) {
        HqlHelper helper = new HqlHelper(fromClazz);
        return helper;
    }

    public QueryParameters getQueryParameters() {
        return queryParameters;
    }

    public List<OrderByEntry> getOrderByConditions() {
        return orderByConditions;
    }

    public Set<UpdateParameter> getUpdateParameters() {
        return updateParameters;
    }

    public Set<FetchField> getFetchFields() {
        return fetchFields;
    }

    public List<QueryJoin> getJoinDomains() {
        return joinDomains;
    }

    public FetchField getFunctionField() {
        return functionField;
    }

    public Set<FetchField> getGroupByPropertys() {
        return groupByPropertys;
    }

    public List<GroupByFunction> getGroupByFunctions() {
        return groupByFunctions;
    }

    public HavingQueryParameters getHavingQueryParameters() {
        return havingQueryParameters;
    }

    public Class<? extends BaseEntity> getFromClazz() {
        return fromClazz;
    }

    public HqlHelper resetFunctionField() {
        functionField = null;
        return this;
    }

    public HqlHelper resetJoins() {
        joinDomains.clear();
        return this;
    }

    public HqlHelper resetQueryParameters() {
        queryParameters.clear();
        return this;
    }

    public HqlHelper resetOrderByConditions() {
        orderByConditions.clear();
        for (GroupByFunction groupByFunction : groupByFunctions) {
            groupByFunction.setOrderType(null);
        }
        return this;
    }

    public HqlHelper resetFetchFields() {
        this.isEntityFieldSubSelect = Boolean.FALSE;
        fetchFields.clear();
        return this;
    }

    public HqlHelper resetUpdateFields() {
        updateParameters.clear();
        updateParameters.add(new UpdateParameter("modifyDate", new Date(), HelperUpdateType.BINDVAR));
        return this;
    }

    public HqlHelper resetPageInfo() {
        this.firstResult = null;
        this.maxResults = null;
        return this;
    }

    public HqlHelper resetHavingQueryParameters() {
        havingQueryParameters.clear();
        return this;
    }

    public HqlHelper resetGroupBy() {
        groupByFunctions.clear();
        groupByPropertys.clear();
        havingQueryParameters.clear();
        return this;
    }

    /**
     * 重新初始化，以 fromClazz 为起始查询实体类
     */
    public HqlHelper resetQueryFrom(Class<? extends BaseEntity> fromClazz) {
        resetQueryParameters();
        resetOrderByConditions();
        resetFetchFields();
        resetUpdateFields();
        resetPageInfo();
        resetJoins();
        resetFunctionField();
        resetGroupBy();
        resetHavingQueryParameters();
        this.fromClazz = fromClazz;
        return this;
    }

    /**
     * 查询条件 - fromClazz.fromClazzFieldName = value
     */
    public HqlHelper eq(String fromClazzFieldName, Object value) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName, OperateType.eq,
                value));
        return this;
    }

    /**
     * 查询条件 - fromClazz.fromClazzFieldName != value
     */
    public HqlHelper ne(String fromClazzFieldName, Object value) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName, OperateType.ne,
                value));
        return this;
    }

    /**
     * 查询条件 - fromClazz.fromClazzFieldName >= value
     */
    public HqlHelper ge(String fromClazzFieldName, Object value) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName, OperateType.ge,
                value));
        return this;
    }

    /**
     * 查询条件 - fromClazz.fromClazzFieldName > value
     */
    public HqlHelper gt(String fromClazzFieldName, Object value) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName, OperateType.gt,
                value));
        return this;
    }

    /**
     * 查询条件 - fromClazz.fromClazzFieldName < value
     */
    public HqlHelper lt(String fromClazzFieldName, Object value) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName, OperateType.lt,
                value));
        return this;
    }

    /**
     * 查询条件 - fromClazz.fromClazzFieldName <= value
     */
    public HqlHelper le(String fromClazzFieldName, Object value) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName, OperateType.le,
                value));
        return this;
    }

    /**
     * 查询条件 - tableAlias.fieldName = value
     */
    public HqlHelper eq(String tableAlias, String fieldName, Object value) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.eq, value));
        return this;
    }

    /**
     * 查询条件 - tableAlias.fieldName != value
     */
    public HqlHelper ne(String tableAlias, String fieldName, Object value) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.ne, value));
        return this;
    }

    /**
     * 查询条件 - tableAlias.fieldName >= value
     */
    public HqlHelper ge(String tableAlias, String fieldName, Object value) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.ge, value));
        return this;
    }

    /**
     * 查询条件 - tableAlias.fieldName > value
     */
    public HqlHelper gt(String tableAlias, String fieldName, Object value) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.gt, value));
        return this;
    }

    /**
     * 查询条件 - tableAlias.fieldName < value
     */
    public HqlHelper lt(String tableAlias, String fieldName, Object value) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.lt, value));
        return this;
    }

    /**
     * 查询条件 - tableAlias.fieldName <= value
     */
    public HqlHelper le(String tableAlias, String fieldName, Object value) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.le, value));
        return this;
    }

    /**
     * 查询条件 - fromClazz.fromClazzFieldName in values
     */
    public HqlHelper in(String fromClazzFieldName, Object... values) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName, OperateType.in,
                values));
        return this;
    }

    /**
     * 查询条件 - fromClazz.fromClazzFieldName not in values
     */
    public HqlHelper notIn(String fromClazzFieldName, Object... values) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName,
                OperateType.notIn, values));
        return this;
    }

    /**
     * 查询条件 - tableAlias.fieldName in values
     */
    public HqlHelper in(String tableAlias, String fieldName, Object... values) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.in, values));
        return this;
    }

    /**
     * 查询条件 - tableAlias.fieldName not in values
     */
    public HqlHelper notIn(String tableAlias, String fieldName, Object... values) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.notIn, values));
        return this;
    }

    /**
     * 查询条件 - fromClazz.fromClazzFieldName like matchType:value
     */
    public HqlHelper like(String fromClazzFieldName, String value, MatchType matchType) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName, OperateType.like,
                matchType, value));
        return this;
    }

    /**
     * 查询条件 - upper(fromClazz.fromClazzFieldName) like upper(matchType:value)
     */
    public HqlHelper ilike(String fromClazzFieldName, String value, MatchType matchType) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName,
                OperateType.ilike, matchType, value));
        return this;
    }

    /**
     * 查询条件 - fromClazz.fromClazzFieldName not like matchType:value
     */
    public HqlHelper notLike(String fromClazzFieldName, String value, MatchType matchType) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName,
                OperateType.notLike, matchType, value));
        return this;
    }

    /**
     * 查询条件 - upper(fromClazz.fromClazzFieldName) not like
     * upper(matchType:value)
     */
    public HqlHelper notiLike(String fromClazzFieldName, String value, MatchType matchType) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName,
                OperateType.notiLike, matchType, value));
        return this;
    }

    /**
     * 查询条件 - tableAlias.fieldName like matchType:value
     */
    public HqlHelper like(String tableAlias, String fieldName, String value, MatchType matchType) {
        queryParameters
                .addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.like, matchType, value));
        return this;
    }

    /**
     * 查询条件 - upper(tableAlias.fieldName) like upper(matchType:value)
     */
    public HqlHelper ilike(String tableAlias, String fieldName, String value, MatchType matchType) {
        queryParameters
                .addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.ilike, matchType, value));
        return this;
    }

    /**
     * 查询条件 - tableAlias.fieldName not like matchType:value
     */
    public HqlHelper notLike(String tableAlias, String fieldName, String value, MatchType matchType) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.notLike, matchType,
                value));
        return this;
    }

    /**
     * 查询条件 - upper(tableAlias.fieldName) not like upper(matchType:value)
     */
    public HqlHelper notiLike(String tableAlias, String fieldName, String value, MatchType matchType) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.notiLike, matchType,
                value));
        return this;
    }

    /**
     * 查询条件 - fromClazz.fromClazzFieldName like value{matchType=ANYWHERE}
     */
    public HqlHelper like(String fromClazzFieldName, String value) {
        return like(fromClazzFieldName, value, MatchType.ANYWHERE);
    }

    /**
     * 查询条件 - upper(fromClazz.fromClazzFieldName) like
     * upper(value{matchType=ANYWHERE})
     */
    public HqlHelper ilike(String fromClazzFieldName, String value) {
        return ilike(fromClazzFieldName, value, MatchType.ANYWHERE);
    }

    /**
     * 查询条件 - fromClazz.fromClazzFieldName not like value{matchType=ANYWHERE}
     */
    public HqlHelper notLike(String fromClazzFieldName, String value) {
        return notLike(fromClazzFieldName, value, MatchType.ANYWHERE);
    }

    /**
     * 查询条件 - upper(fromClazz.fromClazzFieldName) not like
     * upper(value{matchType=ANYWHERE})
     */
    public HqlHelper notiLike(String fromClazzFieldName, String value) {
        return notiLike(fromClazzFieldName, value, MatchType.ANYWHERE);
    }

    /**
     * 查询条件 - tableAlias.fieldName like value{matchType=ANYWHERE}
     */
    public HqlHelper like(String tableAlias, String fieldName, String value) {
        return like(tableAlias, fieldName, value, MatchType.ANYWHERE);
    }

    /**
     * 查询条件 - upper(tableAlias.fieldName) like upper(value{matchType=ANYWHERE})
     */
    public HqlHelper ilike(String tableAlias, String fieldName, String value) {
        return ilike(tableAlias, fieldName, value, MatchType.ANYWHERE);
    }

    /**
     * 查询条件 - tableAlias.fieldName not like value{matchType=ANYWHERE}
     */
    public HqlHelper notLike(String tableAlias, String fieldName, String value) {
        return notLike(tableAlias, fieldName, value, MatchType.ANYWHERE);
    }

    /**
     * 查询条件 - upper(tableAlias.fieldName) not like
     * upper(value{matchType=ANYWHERE})
     */
    public HqlHelper notiLike(String tableAlias, String fieldName, String value) {
        return notiLike(tableAlias, fieldName, value, MatchType.ANYWHERE);
    }

    /**
     * 查询条件 - fromClazz.fromClazzFieldName is null
     */
    public HqlHelper isNull(String fromClazzFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName,
                OperateType.isNull));
        return this;
    }

    /**
     * 查询条件 - fromClazz.fromClazzFieldName is not null
     */
    public HqlHelper isNotNull(String fromClazzFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName,
                OperateType.isNotNull));
        return this;
    }

    /**
     * 查询条件 - tableAlias.fieldName is null
     */
    public HqlHelper isNull(String tableAlias, String fieldName) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.isNull));
        return this;
    }

    /**
     * 查询条件 - tableAlias.fieldName is not null
     */
    public HqlHelper isNotNull(String tableAlias, String fieldName) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.isNotNull));
        return this;
    }

    /**
     * 查询条件 - fromClazz.fromClazzFieldName between value1 and value2
     */
    public HqlHelper between(String fromClazzFieldName, Object value1, Object value2) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName,
                OperateType.between, value1, value2));
        return this;
    }

    /**
     * 查询条件 - fromClazz.fromClazzFieldName not between value1 and value2
     */
    public HqlHelper notBetween(String fromClazzFieldName, Object value1, Object value2) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName,
                OperateType.notBetween, value1, value2));
        return this;
    }

    /**
     * 查询条件 - tableAlias.fieldName between value1 and value2
     */
    public HqlHelper between(String tableAlias, String fieldName, Object value1, Object value2) {
        queryParameters
                .addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.between, value1, value2));
        return this;
    }

    /**
     * 查询条件 - tableAlias.fieldName not between value1 and value2
     */
    public HqlHelper notBetween(String tableAlias, String fieldName, Object value1, Object value2) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.notBetween, value1,
                value2));
        return this;
    }

    //property compare
    /**
     * 查询条件 - fromClazz.fromClazzFieldName = fromClazz.fromClazzOtherFieldName
     */
    public HqlHelper eqProperty(String fromClazzFieldName, String fromClazzOtherFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName,
                OperateType.eqProperty, HqlHelper.currTable, fromClazzOtherFieldName));
        return this;
    }

    /**
     * 查询条件 - fromClazz.fromClazzFieldName != fromClazz.fromClazzOtherFieldName
     */
    public HqlHelper neProperty(String fromClazzFieldName, String fromClazzOtherFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName,
                OperateType.neProperty, HqlHelper.currTable, fromClazzOtherFieldName));
        return this;
    }

    /**
     * 查询条件 - fromClazz.fromClazzFieldName > fromClazz.fromClazzOtherFieldName
     */
    public HqlHelper gtProperty(String fromClazzFieldName, String fromClazzOtherFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName,
                OperateType.gtProperty, HqlHelper.currTable, fromClazzOtherFieldName));
        return this;
    }

    /**
     * 查询条件 - fromClazz.fromClazzFieldName < fromClazz.fromClazzOtherFieldName
     */
    public HqlHelper ltProperty(String fromClazzFieldName, String fromClazzOtherFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName,
                OperateType.ltProperty, HqlHelper.currTable, fromClazzOtherFieldName));
        return this;
    }

    /**
     * 查询条件 - fromClazz.fromClazzFieldName >= fromClazz.fromClazzOtherFieldName
     */
    public HqlHelper geProperty(String fromClazzFieldName, String fromClazzOtherFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName,
                OperateType.geProperty, HqlHelper.currTable, fromClazzOtherFieldName));
        return this;
    }

    /**
     * 查询条件 - fromClazz.fromClazzFieldName <= fromClazz.fromClazzOtherFieldName
     */
    public HqlHelper leProperty(String fromClazzFieldName, String fromClazzOtherFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName,
                OperateType.leProperty, HqlHelper.currTable, fromClazzOtherFieldName));
        return this;
    }

    /**
     * 查询条件 - tableAlias.fieldName = fromClazz.fromClazzFieldName
     */
    public HqlHelper eqProperty(String tableAlias, String fieldName, String fromClazzFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.eqProperty,
                HqlHelper.currTable, fromClazzFieldName));
        return this;
    }

    /**
     * 查询条件 - tableAlias.fieldName != fromClazz.fromClazzFieldName
     */
    public HqlHelper neProperty(String tableAlias, String fieldName, String fromClazzFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.neProperty,
                HqlHelper.currTable, fromClazzFieldName));
        return this;
    }

    /**
     * 查询条件 - tableAlias.fieldName > fromClazz.fromClazzFieldName
     */
    public HqlHelper gtProperty(String tableAlias, String fieldName, String fromClazzFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.gtProperty,
                HqlHelper.currTable, fromClazzFieldName));
        return this;
    }

    /**
     * 查询条件 - tableAlias.fieldName < fromClazz.fromClazzFieldName
     */
    public HqlHelper ltProperty(String tableAlias, String fieldName, String fromClazzFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.ltProperty,
                HqlHelper.currTable, fromClazzFieldName));
        return this;
    }

    /**
     * 查询条件 - tableAlias.fieldName >= fromClazz.fromClazzFieldName
     */
    public HqlHelper geProperty(String tableAlias, String fieldName, String fromClazzFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.geProperty,
                HqlHelper.currTable, fromClazzFieldName));
        return this;
    }

    /**
     * 查询条件 - tableAlias.fieldName <= fromClazz.fromClazzFieldName
     */
    public HqlHelper leProperty(String tableAlias, String fieldName, String fromClazzFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.leProperty,
                HqlHelper.currTable, fromClazzFieldName));
        return this;
    }

    /**
     * 查询条件 - tableAlias.fieldName = otherTableAlias.otherFieldName
     */
    public HqlHelper eqProperty(String tableAlias, String fieldName, String otherTableAlias, String otherFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.eqProperty,
                otherTableAlias, otherFieldName));
        return this;
    }

    /**
     * 查询条件 - tableAlias.fieldName != otherTableAlias.otherFieldName
     */
    public HqlHelper neProperty(String tableAlias, String fieldName, String otherTableAlias, String otherFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.neProperty,
                otherTableAlias, otherFieldName));
        return this;
    }

    /**
     * 查询条件 - tableAlias.fieldName > otherTableAlias.otherFieldName
     */
    public HqlHelper gtProperty(String tableAlias, String fieldName, String otherTableAlias, String otherFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.gtProperty,
                otherTableAlias, otherFieldName));
        return this;
    }

    /**
     * 查询条件 - tableAlias.fieldName < otherTableAlias.otherFieldName
     */
    public HqlHelper ltProperty(String tableAlias, String fieldName, String otherTableAlias, String otherFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.ltProperty,
                otherTableAlias, otherFieldName));
        return this;
    }

    /**
     * 查询条件 - tableAlias.fieldName >= otherTableAlias.otherFieldName
     */
    public HqlHelper geProperty(String tableAlias, String fieldName, String otherTableAlias, String otherFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.geProperty,
                otherTableAlias, otherFieldName));
        return this;
    }

    /**
     * 查询条件 - tableAlias.fieldName <= otherTableAlias.otherFieldName
     */
    public HqlHelper leProperty(String tableAlias, String fieldName, String otherTableAlias, String otherFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.leProperty,
                otherTableAlias, otherFieldName));
        return this;
    }

    /**
     * 查询条件 - fromClazz.fromClazzFieldName is empty
     */
    public HqlHelper isEmpty(String fromClazzFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName,
                OperateType.isEmpty));
        return this;
    }

    /**
     * 查询条件 - fromClazz.fromClazzFieldName is not empty
     */
    public HqlHelper isNotEmpty(String fromClazzFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName,
                OperateType.isNotEmpty));
        return this;
    }

    /**
     * 查询条件 - tableAlias.fieldName is empty
     */
    public HqlHelper isEmpty(String tableAlias, String fieldName) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.isEmpty));
        return this;
    }

    /**
     * 查询条件 - tableAlias.fieldName is not empty
     */
    public HqlHelper isNotEmpty(String tableAlias, String fieldName) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.isNotEmpty));
        return this;
    }

    /**
     * 查询条件 - size(fromClazz.fromClazzFieldName) = size
     */
    public HqlHelper sizeEq(String fromClazzFieldName, Integer size) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName,
                OperateType.sizeEq, size));
        return this;
    }

    /**
     * 查询条件 - size(fromClazz.fromClazzFieldName) >= size
     */
    public HqlHelper sizeGe(String fromClazzFieldName, Integer size) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName,
                OperateType.sizeGe, size));
        return this;
    }

    /**
     * 查询条件 - size(fromClazz.fromClazzFieldName) > size
     */
    public HqlHelper sizeGt(String fromClazzFieldName, Integer size) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName,
                OperateType.sizeGt, size));
        return this;
    }

    /**
     * 查询条件 - size(fromClazz.fromClazzFieldName) <= size
     */
    public HqlHelper sizeLe(String fromClazzFieldName, Integer size) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName,
                OperateType.sizeLe, size));
        return this;
    }

    /**
     * 查询条件 - size(fromClazz.fromClazzFieldName) < size
     */
    public HqlHelper sizeLt(String fromClazzFieldName, Integer size) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName,
                OperateType.sizeLt, size));
        return this;
    }

    /**
     * 查询条件 - size(fromClazz.fromClazzFieldName) != size
     */
    public HqlHelper sizeNe(String fromClazzFieldName, Integer size) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName,
                OperateType.sizeNe, size));
        return this;
    }

    /**
     * 查询条件 - size(tableAlias.fieldName) = size
     */
    public HqlHelper sizeEq(String tableAlias, String fieldName, Integer size) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.sizeEq, size));
        return this;
    }

    /**
     * 查询条件 - size(tableAlias.fieldName) >= size
     */
    public HqlHelper sizeGe(String tableAlias, String fieldName, Integer size) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.sizeGe, size));
        return this;
    }

    /**
     * 查询条件 - size(tableAlias.fieldName) > size
     */
    public HqlHelper sizeGt(String tableAlias, String fieldName, Integer size) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.sizeGt, size));
        return this;
    }

    /**
     * 查询条件 - size(tableAlias.fieldName) <= size
     */
    public HqlHelper sizeLe(String tableAlias, String fieldName, Integer size) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.sizeLe, size));
        return this;
    }

    /**
     * 查询条件 - size(tableAlias.fieldName) < size
     */
    public HqlHelper sizeLt(String tableAlias, String fieldName, Integer size) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.sizeLt, size));
        return this;
    }

    /**
     * 查询条件 - size(tableAlias.fieldName) != size
     */
    public HqlHelper sizeNe(String tableAlias, String fieldName, Integer size) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.sizeNe, size));
        return this;
    }

    /**
     * 查询条件 - length(fromClazz.fromClazzFieldName) = length
     */
    public HqlHelper lengthEq(String fromClazzFieldName, Integer length) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName,
                OperateType.lengthEq, length));
        return this;
    }

    /**
     * 查询条件 - length(fromClazz.fromClazzFieldName) >= length
     */
    public HqlHelper lengthGe(String fromClazzFieldName, Integer length) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName,
                OperateType.lengthGe, length));
        return this;
    }

    /**
     * 查询条件 - length(fromClazz.fromClazzFieldName) > length
     */
    public HqlHelper lengthGt(String fromClazzFieldName, Integer length) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName,
                OperateType.lengthGt, length));
        return this;
    }

    /**
     * 查询条件 - length(fromClazz.fromClazzFieldName) <= length
     */
    public HqlHelper lengthLe(String fromClazzFieldName, Integer length) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName,
                OperateType.lengthLe, length));
        return this;
    }

    /**
     * 查询条件 - length(fromClazz.fromClazzFieldName) < length
     */
    public HqlHelper lengthLt(String fromClazzFieldName, Integer length) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName,
                OperateType.lengthLt, length));
        return this;
    }

    /**
     * 查询条件 - length(fromClazz.fromClazzFieldName) != length
     */
    public HqlHelper lengthNe(String fromClazzFieldName, Integer length) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName,
                OperateType.lengthNe, length));
        return this;
    }

    /**
     * 查询条件 - length(tableAlias.fieldName) = length
     */
    public HqlHelper lengthEq(String tableAlias, String fieldName, Integer length) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.lengthEq, length));
        return this;
    }

    /**
     * 查询条件 - length(tableAlias.fieldName) >= length
     */
    public HqlHelper lengthGe(String tableAlias, String fieldName, Integer length) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.lengthGe, length));
        return this;
    }

    /**
     * 查询条件 - length(tableAlias.fieldName) > length
     */
    public HqlHelper lengthGt(String tableAlias, String fieldName, Integer length) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.lengthGt, length));
        return this;
    }

    /**
     * 查询条件 - length(tableAlias.fieldName) <= length
     */
    public HqlHelper lengthLe(String tableAlias, String fieldName, Integer length) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.lengthLe, length));
        return this;
    }

    /**
     * 查询条件 - length(tableAlias.fieldName) < length
     */
    public HqlHelper lengthLt(String tableAlias, String fieldName, Integer length) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.lengthLt, length));
        return this;
    }

    /**
     * 查询条件 - length(tableAlias.fieldName) != length
     */
    public HqlHelper lengthNe(String tableAlias, String fieldName, Integer length) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.lengthNe, length));
        return this;
    }

    /**
     * 查询条件 - length(fromClazz.fromClazzFieldName) =
     * length(fromClazz.fromClazzOtherFieldName)
     */
    public HqlHelper lengthEqProperty(String fromClazzFieldName, String fromClazzOtherFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName,
                OperateType.lengthEqProperty, HqlHelper.currTable, fromClazzOtherFieldName));
        return this;
    }

    /**
     * 查询条件 - length(fromClazz.fromClazzFieldName) >=
     * length(fromClazz.fromClazzOtherFieldName)
     */
    public HqlHelper lengthGeProperty(String fromClazzFieldName, String fromClazzOtherFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName,
                OperateType.lengthGeProperty, HqlHelper.currTable, fromClazzOtherFieldName));
        return this;
    }

    /**
     * 查询条件 - length(fromClazz.fromClazzFieldName) >
     * length(fromClazz.fromClazzOtherFieldName)
     */
    public HqlHelper lengthGtProperty(String fromClazzFieldName, String fromClazzOtherFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName,
                OperateType.lengthGtProperty, HqlHelper.currTable, fromClazzOtherFieldName));
        return this;
    }

    /**
     * 查询条件 - length(fromClazz.fromClazzFieldName) <=
     * length(fromClazz.fromClazzOtherFieldName)
     */
    public HqlHelper lengthLeProperty(String fromClazzFieldName, String fromClazzOtherFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName,
                OperateType.lengthLeProperty, HqlHelper.currTable, fromClazzOtherFieldName));
        return this;
    }

    /**
     * 查询条件 - length(fromClazz.fromClazzFieldName) <
     * length(fromClazz.fromClazzOtherFieldName)
     */
    public HqlHelper lengthLtProperty(String fromClazzFieldName, String fromClazzOtherFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName,
                OperateType.lengthLtProperty, HqlHelper.currTable, fromClazzOtherFieldName));
        return this;
    }

    /**
     * 查询条件 - length(fromClazz.fromClazzFieldName) !=
     * length(fromClazz.fromClazzOtherFieldName)
     */
    public HqlHelper lengthNeProperty(String fromClazzFieldName, String fromClazzOtherFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(HqlHelper.currTable, fromClazzFieldName,
                OperateType.lengthNeProperty, HqlHelper.currTable, fromClazzOtherFieldName));
        return this;
    }

    /**
     * 查询条件 - length(tableAlias.fieldName) =
     * length(fromClazz.fromClazzFieldName)
     */
    public HqlHelper lengthEqProperty(String tableAlias, String fieldName, String fromClazzFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.lengthEqProperty,
                HqlHelper.currTable, fromClazzFieldName));
        return this;
    }

    /**
     * 查询条件 - length(tableAlias.fieldName) >=
     * length(fromClazz.fromClazzFieldName)
     */
    public HqlHelper lengthGeProperty(String tableAlias, String fieldName, String fromClazzFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.lengthGeProperty,
                HqlHelper.currTable, fromClazzFieldName));
        return this;
    }

    /**
     * 查询条件 - length(tableAlias.fieldName) >
     * length(fromClazz.fromClazzFieldName)
     */
    public HqlHelper lengthGtProperty(String tableAlias, String fieldName, String fromClazzFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.lengthGtProperty,
                HqlHelper.currTable, fromClazzFieldName));
        return this;
    }

    /**
     * 查询条件 - length(tableAlias.fieldName) <=
     * length(fromClazz.fromClazzFieldName)
     */
    public HqlHelper lengthLeProperty(String tableAlias, String fieldName, String fromClazzFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.lengthLeProperty,
                HqlHelper.currTable, fromClazzFieldName));
        return this;
    }

    /**
     * 查询条件 - length(tableAlias.fieldName) <
     * length(fromClazz.fromClazzFieldName)
     */
    public HqlHelper lengthLtProperty(String tableAlias, String fieldName, String fromClazzFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.lengthLtProperty,
                HqlHelper.currTable, fromClazzFieldName));
        return this;
    }

    /**
     * 查询条件 - length(tableAlias.fieldName) !=
     * length(fromClazz.fromClazzFieldName)
     */
    public HqlHelper lengthNeProperty(String tableAlias, String fieldName, String fromClazzFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.lengthNeProperty,
                HqlHelper.currTable, fromClazzFieldName));
        return this;
    }

    /**
     * 查询条件 - length(tableAlias.fieldName) =
     * length(otherTableAlias.otherFieldName)
     */
    public HqlHelper lengthEqProperty(String tableAlias, String fieldName, String otherTableAlias, String otherFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.lengthEqProperty,
                otherTableAlias, otherFieldName));
        return this;
    }

    /**
     * 查询条件 - length(tableAlias.fieldName) >=
     * length(otherTableAlias.otherFieldName)
     */
    public HqlHelper lengthGeProperty(String tableAlias, String fieldName, String otherTableAlias, String otherFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.lengthGeProperty,
                otherTableAlias, otherFieldName));
        return this;
    }

    /**
     * 查询条件 - length(tableAlias.fieldName) >
     * length(otherTableAlias.otherFieldName)
     */
    public HqlHelper lengthGtProperty(String tableAlias, String fieldName, String otherTableAlias, String otherFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.lengthGtProperty,
                otherTableAlias, otherFieldName));
        return this;
    }

    /**
     * 查询条件 - length(tableAlias.fieldName) <=
     * length(otherTableAlias.otherFieldName)
     */
    public HqlHelper lengthLeProperty(String tableAlias, String fieldName, String otherTableAlias, String otherFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.lengthLeProperty,
                otherTableAlias, otherFieldName));
        return this;
    }

    /**
     * 查询条件 - length(tableAlias.fieldName) <
     * length(otherTableAlias.otherFieldName)
     */
    public HqlHelper lengthLtProperty(String tableAlias, String fieldName, String otherTableAlias, String otherFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.lengthLtProperty,
                otherTableAlias, otherFieldName));
        return this;
    }

    /**
     * 查询条件 - length(tableAlias.fieldName) !=
     * length(otherTableAlias.otherFieldName)
     */
    public HqlHelper lengthNeProperty(String tableAlias, String fieldName, String otherTableAlias, String otherFieldName) {
        queryParameters.addQueryParameter(new QueryParameter(tableAlias, fieldName, OperateType.lengthNeProperty,
                otherTableAlias, otherFieldName));
        return this;
    }

    /**
     * 查询条件 - or ( ,需要 end()结尾
     */
    public HqlHelper or() {
        queryParameters.begin(BeginOperator.OR);
        return this;
    }

    /**
     * 查询条件 - and (,需要 end()结尾
     */
    public HqlHelper and() {
        queryParameters.begin(BeginOperator.AND);
        return this;
    }

    /**
     * 查询条件 - )，与or() and()配合使用
     */
    public HqlHelper end() {
        queryParameters.end();
        return this;
    }

    /**
     * 排序 - order by fromClazz.fromClazzFieldName orderType
     */
    public HqlHelper orderBy(String fromClazzFieldName, OrderType orderType) {
        orderByConditions.add(new OrderByEntry(HqlHelper.currTable, fromClazzFieldName, orderType));
        return this;
    }

    /**
     * 排序 - order by tableAlias.fieldName orderType
     */
    public HqlHelper orderBy(String tableAlias, String fieldName, OrderType orderType) {
        orderByConditions.add(new OrderByEntry(tableAlias, fieldName, orderType));
        return this;
    }

    /**
     * 更新字段 - <br/>
     * update ... <br/>
     * set fromClazz.fromClazzFieldName = value <br/>
     * where ...
     */
    public HqlHelper update(String fromClazzFieldName, Object value) {
        updateParameters.add(new UpdateParameter(fromClazzFieldName, value, HelperUpdateType.BINDVAR));
        return this;
    }

    /**
     * 更新字段 - <br/>
     * update ... <br/>
     * set fromClazz.fromClazzFieldName = hqlSegment <br/>
     * where ...
     */
    public HqlHelper updateHqlSegment(String fromClazzFieldName, String hqlSegment) {
        updateParameters.add(new UpdateParameter(fromClazzFieldName, hqlSegment, HelperUpdateType.SQLSEGMENT));
        return this;
    }

    /**
     * 查询字段 - <br/>
     * select fromClazz.fieldName1,fromClazz.fieldName2 ... fromClazz.fieldNamen <br/>
     * from ...
     */
    public HqlHelper fetch(String... fromClazzFieldNames) {
        for (String fieldName : fromClazzFieldNames) {
            if (StringUtils.isNotBlank(fieldName)) {
                fetchFields.add(new FetchField(HqlHelper.currTable, fieldName, fieldName.replaceAll("\\.", "_")));
            }
        }
        return this;
    }

    /**
     * 查询字段 - <br/>
     * select tableAlias.fieldName as alias <br/>
     * from ...
     */
    public HqlHelper fetchOther(String tableAlias, String fieldName, String alias) {
        fetchFields.add(new FetchField(tableAlias, fieldName, alias));
        return this;
    }

    /**
     * 查询字段 - select count(tableAlias.fieldName) from ... <br/>
     * 查询字段 - select sum(tableAlias.fieldName) from ... <br/>
     * 查询字段 - select max(tableAlias.fieldName) from ... <br/>
     * 查询字段 - select min(tableAlias.fieldName) from ... <br/>
     * 查询字段 - select avg(tableAlias.fieldName) from ... <br/>
     */
    public HqlHelper function(String tableAlias, String fieldName) {
        functionField = new FetchField(tableAlias, fieldName, null);
        return this;
    }

    public Integer getFirstResult() {
        return firstResult;
    }

    public Integer getMaxResults() {
        return maxResults;
    }

    /**
     * limit firstResult,maxResults
     */
    public HqlHelper setFirstResult(Integer firstResult) {
        this.firstResult = firstResult;
        return this;
    }

    /**
     * limit firstResult,maxResults
     */
    public HqlHelper setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    public Boolean getIsEntityFieldSubSelect() {
        return isEntityFieldSubSelect;
    }

    /**
     * 当查询的字段是实体类时，是否启用子查询，true=启用 false=不启用
     */
    public HqlHelper setIsEntityFieldSubSelect(Boolean isEntityFieldSubSelect) {
        this.isEntityFieldSubSelect = isEntityFieldSubSelect;
        return this;
    }

    // table join
    /**
     * 表关联 - <br/>
     * select ... <br/>
     * from fromClazz , joinClazz tableAlias <br/>
     * where ...
     */
    public HqlHelper join(Class<? extends BaseEntity> joinClazz, String tableAlias) {
        joinDomains.add(new QueryJoin(joinClazz.getSimpleName(), tableAlias, JoinType.ENTITY_JOIN, null));
        return this;
    }

    /**
     * 表关联 - <br/>
     * select ... <br/>
     * from fromClazz inner join fieldAlias.entityField tableAlias <br/>
     * where ...
     */
    public HqlHelper join(String fieldAlias, String entityField, String tableAlias) {
        joinDomains.add(new QueryJoin(entityField, tableAlias, JoinType.INNER, fieldAlias));
        return this;
    }

    /**
     * 表关联 - <br/>
     * select ... <br/>
     * from fromClazz inner left join fieldAlias.entityField tableAlias <br/>
     * where ...
     */
    public HqlHelper leftJoin(String fieldAlias, String entityField, String tableAlias) {
        joinDomains.add(new QueryJoin(entityField, tableAlias, JoinType.LEFT_JOIN, fieldAlias));
        return this;
    }

    /**
     * 表关联 - <br/>
     * select ... <br/>
     * from fromClazz inner right join fieldAlias.entityField tableAlias <br/>
     * where ...
     */
    public HqlHelper rightJoin(String fieldAlias, String entityField, String tableAlias) {
        joinDomains.add(new QueryJoin(entityField, tableAlias, JoinType.RIGHT_JOIN, fieldAlias));
        return this;
    }

    // group by 
    /**
     * 分组查询 - <br/>
     * select fromClazz.fromClazzFieldName <br/>
     * from ... <br/>
     * where ... <br/>
     * group by fromClazz.fromClazzFieldName
     */
    public HqlHelper groupBy(String fromClazzFieldName) {
        groupByPropertys.add(new FetchField(HqlHelper.currTable, fromClazzFieldName, fromClazzFieldName.replaceAll(
                "\\.", "_")));
        return this;
    }

    /**
     * 分组查询 - <br/>
     * select tableAlias.fieldName as alias<br/>
     * from ... <br/>
     * where ... <br/>
     * group by tableAlias.fieldName
     */
    public HqlHelper groupByOther(String tableAlias, String fieldName, String alias) {
        groupByPropertys.add(new FetchField(tableAlias, fieldName, alias));
        return this;
    }

    /**
     * 组函数 - <br/>
     * select ..., avg(fromClazz.formClzzFieldName) as avgAlias<br/>
     * from ... <br/>
     * where ... <br/>
     * group by ...
     */
    public HqlHelper avg(String field, String avgAlias) {
        groupByFunctions.add(new GroupByFunction(GroupByType.AVG, HqlHelper.currTable, field, avgAlias));
        return this;
    }

    /**
     * 组函数 - <br/>
     * select ..., avg(tableAlias.fieldName) as avgAlias<br/>
     * from ... <br/>
     * where ... <br/>
     * group by ...
     */
    public HqlHelper avg(String tableAlias, String fieldName, String avgAlias) {
        groupByFunctions.add(new GroupByFunction(GroupByType.AVG, tableAlias, fieldName, avgAlias));
        return this;
    }

    /**
     * 组函数 - <br/>
     * select ..., count(fromClazz.formClzzFieldName) as countAlias<br/>
     * from ... <br/>
     * where ... <br/>
     * group by ...
     */
    public HqlHelper count(String formClzzFieldName, String countAlias) {
        groupByFunctions
                .add(new GroupByFunction(GroupByType.COUNT, HqlHelper.currTable, formClzzFieldName, countAlias));
        return this;
    }

    /**
     * 组函数 - <br/>
     * select ..., count(tableAlias.fieldName) as countAlias<br/>
     * from ... <br/>
     * where ... <br/>
     * group by ...
     */
    public HqlHelper count(String tableAlias, String fieldName, String countAlias) {
        groupByFunctions.add(new GroupByFunction(GroupByType.COUNT, tableAlias, fieldName, countAlias));
        return this;
    }

    /**
     * 组函数 - <br/>
     * select ..., count(distinct fromClazz.formClzzFieldName) as countAlias<br/>
     * from ... <br/>
     * where ... <br/>
     * group by ...
     */
    public HqlHelper countDistinct(String formClzzFieldName, String countAlias) {
        groupByFunctions.add(new GroupByFunction(GroupByType.COUNTDISTINCT, HqlHelper.currTable, formClzzFieldName,
                countAlias));
        return this;
    }

    /**
     * 组函数 - <br/>
     * select ..., count(distinct tableAlias.fieldName) as countAlias<br/>
     * from ... <br/>
     * where ... <br/>
     * group by ...
     */
    public HqlHelper countDistinct(String tableAlias, String fieldName, String countAlias) {
        groupByFunctions.add(new GroupByFunction(GroupByType.COUNTDISTINCT, tableAlias, fieldName, countAlias));
        return this;
    }

    /**
     * 组函数 - <br/>
     * select ..., max(fromClazz.formClzzFieldName) as maxAlias<br/>
     * from ... <br/>
     * where ... <br/>
     * group by ...
     */
    public HqlHelper max(String formClzzFieldName, String maxAlias) {
        groupByFunctions.add(new GroupByFunction(GroupByType.MAX, HqlHelper.currTable, formClzzFieldName, maxAlias));
        return this;
    }

    /**
     * 组函数 - <br/>
     * select ..., max(tableAlias.fieldName) as maxAlias<br/>
     * from ... <br/>
     * where ... <br/>
     * group by ...
     */
    public HqlHelper max(String tableAlias, String fieldName, String maxAlias) {
        groupByFunctions.add(new GroupByFunction(GroupByType.MAX, tableAlias, fieldName, maxAlias));
        return this;
    }

    /**
     * 组函数 - <br/>
     * select ..., min(fromClazz.formClzzFieldName) as minAlias<br/>
     * from ... <br/>
     * where ... <br/>
     * group by ...
     */
    public HqlHelper min(String formClzzFieldName, String minAlias) {
        groupByFunctions.add(new GroupByFunction(GroupByType.MIN, HqlHelper.currTable, formClzzFieldName, minAlias));
        return this;
    }

    /**
     * 组函数 - <br/>
     * select ..., min(tableAlias.fieldName) as minAlias<br/>
     * from ... <br/>
     * where ... <br/>
     * group by ...
     */
    public HqlHelper min(String tableAlias, String fieldName, String minAlias) {
        groupByFunctions.add(new GroupByFunction(GroupByType.MIN, tableAlias, fieldName, minAlias));
        return this;
    }

    /**
     * 组函数 - <br/>
     * select ..., sum(fromClazz.formClzzFieldName) as sumAlias<br/>
     * from ... <br/>
     * where ... <br/>
     * group by ...
     */
    public HqlHelper sum(String formClzzFieldName, String sumAlias) {
        groupByFunctions.add(new GroupByFunction(GroupByType.SUM, HqlHelper.currTable, formClzzFieldName, sumAlias));
        return this;
    }

    /**
     * 组函数 - <br/>
     * select ..., sum(tableAlias.fieldName) as sumAlias<br/>
     * from ... <br/>
     * where ... <br/>
     * group by ...
     */
    public HqlHelper sum(String tableAlias, String fieldName, String sumAlias) {
        groupByFunctions.add(new GroupByFunction(GroupByType.SUM, tableAlias, fieldName, sumAlias));
        return this;
    }

    // having 
    /**
     * 分组过滤 - <br/>
     * select ...<br/>
     * from ... <br/>
     * where ... <br/>
     * group by ...<br/>
     * having {current group function} = value
     */
    public HqlHelper having_eq(Object value) {
        havingQueryParameters.addQueryParameter(new HavingQueryParameter(
                groupByFunctions.get(groupByFunctions.size() - 1), HavingOperateType.eq, value));
        return this;
    }

    /**
     * 分组过滤 - <br/>
     * select ...<br/>
     * from ... <br/>
     * where ... <br/>
     * group by ...<br/>
     * having {current group function} != value
     */
    public HqlHelper having_ne(Object value) {
        havingQueryParameters.addQueryParameter(new HavingQueryParameter(
                groupByFunctions.get(groupByFunctions.size() - 1), HavingOperateType.ne, value));
        return this;
    }

    /**
     * 分组过滤 - <br/>
     * select ...<br/>
     * from ... <br/>
     * where ... <br/>
     * group by ...<br/>
     * having {current group function} >= value
     */
    public HqlHelper having_ge(Object value) {
        havingQueryParameters.addQueryParameter(new HavingQueryParameter(
                groupByFunctions.get(groupByFunctions.size() - 1), HavingOperateType.ge, value));
        return this;
    }

    /**
     * 分组过滤 - <br/>
     * select ...<br/>
     * from ... <br/>
     * where ... <br/>
     * group by ...<br/>
     * having {current group function} > value
     */
    public HqlHelper having_gt(Object value) {
        havingQueryParameters.addQueryParameter(new HavingQueryParameter(
                groupByFunctions.get(groupByFunctions.size() - 1), HavingOperateType.gt, value));
        return this;
    }

    /**
     * 分组过滤 - <br/>
     * select ...<br/>
     * from ... <br/>
     * where ... <br/>
     * group by ...<br/>
     * having {current group function} < value
     */
    public HqlHelper having_lt(Object value) {
        havingQueryParameters.addQueryParameter(new HavingQueryParameter(
                groupByFunctions.get(groupByFunctions.size() - 1), HavingOperateType.lt, value));
        return this;
    }

    /**
     * 分组过滤 - <br/>
     * select ...<br/>
     * from ... <br/>
     * where ... <br/>
     * group by ...<br/>
     * having {current group function} <= value
     */
    public HqlHelper having_le(Object value) {
        havingQueryParameters.addQueryParameter(new HavingQueryParameter(
                groupByFunctions.get(groupByFunctions.size() - 1), HavingOperateType.le, value));
        return this;
    }

    /**
     * 分组过滤 - <br/>
     * select ...<br/>
     * from ... <br/>
     * where ... <br/>
     * group by ...<br/>
     * having {current group function} between value1 and value2
     */
    public HqlHelper having_between(Object value1, Object value2) {
        havingQueryParameters.addQueryParameter(new HavingQueryParameter(
                groupByFunctions.get(groupByFunctions.size() - 1), HavingOperateType.between, value1, value2));
        return this;
    }

    /**
     * 分组过滤 - <br/>
     * select ...<br/>
     * from ... <br/>
     * where ... <br/>
     * group by ...<br/>
     * having {current group function} not between value1 and value2
     */
    public HqlHelper having_notBetween(Object value1, Object value2) {
        havingQueryParameters.addQueryParameter(new HavingQueryParameter(
                groupByFunctions.get(groupByFunctions.size() - 1), HavingOperateType.notBetween, value1, value2));
        return this;
    }

    /**
     * 分组过滤 - <br/>
     * select ...<br/>
     * from ... <br/>
     * where ... <br/>
     * group by ...<br/>
     * having {current group function} in values
     */
    public HqlHelper having_in(Object... values) {
        havingQueryParameters.addQueryParameter(new HavingQueryParameter(
                groupByFunctions.get(groupByFunctions.size() - 1), HavingOperateType.in, values));
        return this;
    }

    /**
     * 分组过滤 - <br/>
     * select ...<br/>
     * from ... <br/>
     * where ... <br/>
     * group by ...<br/>
     * having {current group function} not in values
     */
    public HqlHelper having_notIn(Object... values) {
        havingQueryParameters.addQueryParameter(new HavingQueryParameter(
                groupByFunctions.get(groupByFunctions.size() - 1), HavingOperateType.notIn, values));
        return this;
    }

    /**
     * 分组过滤 - <br/>
     * select ...<br/>
     * from ... <br/>
     * where ... <br/>
     * group by ...<br/>
     * having {current group function} like matchType:value
     */
    public HqlHelper having_like(String value, MatchType matchType) {
        havingQueryParameters.addQueryParameter(new HavingQueryParameter(
                groupByFunctions.get(groupByFunctions.size() - 1), HavingOperateType.like, matchType, value));
        return this;
    }

    /**
     * 分组过滤 - <br/>
     * select ...<br/>
     * from ... <br/>
     * where ... <br/>
     * group by ...<br/>
     * having upper({current group function}) like upper(matchType:value)
     */
    public HqlHelper having_ilike(String value, MatchType matchType) {
        havingQueryParameters.addQueryParameter(new HavingQueryParameter(
                groupByFunctions.get(groupByFunctions.size() - 1), HavingOperateType.ilike, matchType, value));
        return this;
    }

    /**
     * 分组过滤 - <br/>
     * select ...<br/>
     * from ... <br/>
     * where ... <br/>
     * group by ...<br/>
     * having {current group function} not like matchType:value
     */
    public HqlHelper having_notLike(String value, MatchType matchType) {
        havingQueryParameters.addQueryParameter(new HavingQueryParameter(
                groupByFunctions.get(groupByFunctions.size() - 1), HavingOperateType.notLike, matchType, value));
        return this;
    }

    /**
     * 分组过滤 - <br/>
     * select ...<br/>
     * from ... <br/>
     * where ... <br/>
     * group by ...<br/>
     * having upper({current group function}) not like upper(matchType:value)
     */
    public HqlHelper having_notiLike(String value, MatchType matchType) {
        havingQueryParameters.addQueryParameter(new HavingQueryParameter(
                groupByFunctions.get(groupByFunctions.size() - 1), HavingOperateType.notiLike, matchType, value));
        return this;
    }

    /**
     * 分组过滤 - <br/>
     * select ...<br/>
     * from ... <br/>
     * where ... <br/>
     * group by ...<br/>
     * having length({current group function}) = length
     */
    public HqlHelper having_lengthEq(Integer length) {
        havingQueryParameters.addQueryParameter(new HavingQueryParameter(
                groupByFunctions.get(groupByFunctions.size() - 1), HavingOperateType.lengthEq, length));
        return this;
    }

    /**
     * 分组过滤 - <br/>
     * select ...<br/>
     * from ... <br/>
     * where ... <br/>
     * group by ...<br/>
     * having length({current group function}) >= length
     */
    public HqlHelper having_lengthGe(Integer length) {
        havingQueryParameters.addQueryParameter(new HavingQueryParameter(
                groupByFunctions.get(groupByFunctions.size() - 1), HavingOperateType.lengthGe, length));
        return this;
    }

    /**
     * 分组过滤 - <br/>
     * select ...<br/>
     * from ... <br/>
     * where ... <br/>
     * group by ...<br/>
     * having length({current group function}) > length
     */
    public HqlHelper having_lengthGt(Integer length) {
        havingQueryParameters.addQueryParameter(new HavingQueryParameter(
                groupByFunctions.get(groupByFunctions.size() - 1), HavingOperateType.lengthGt, length));
        return this;
    }

    /**
     * 分组过滤 - <br/>
     * select ...<br/>
     * from ... <br/>
     * where ... <br/>
     * group by ...<br/>
     * having length({current group function}) <= length
     */
    public HqlHelper having_lengthLe(Integer length) {
        havingQueryParameters.addQueryParameter(new HavingQueryParameter(
                groupByFunctions.get(groupByFunctions.size() - 1), HavingOperateType.lengthLe, length));
        return this;
    }

    /**
     * 分组过滤 - <br/>
     * select ...<br/>
     * from ... <br/>
     * where ... <br/>
     * group by ...<br/>
     * having length({current group function}) < length
     */
    public HqlHelper having_lengthLt(Integer length) {
        havingQueryParameters.addQueryParameter(new HavingQueryParameter(
                groupByFunctions.get(groupByFunctions.size() - 1), HavingOperateType.lengthLt, length));
        return this;
    }

    /**
     * 分组过滤 - <br/>
     * select ...<br/>
     * from ... <br/>
     * where ... <br/>
     * group by ...<br/>
     * having length({current group function}) != length
     */
    public HqlHelper having_lengthNe(Integer length) {
        havingQueryParameters.addQueryParameter(new HavingQueryParameter(
                groupByFunctions.get(groupByFunctions.size() - 1), HavingOperateType.lengthNe, length));
        return this;
    }

    /**
     * having ... or ( ...,需要 having_end()结尾
     */
    public HqlHelper having_or() {
        havingQueryParameters.begin(HavingQueryParameters.BeginOperator.OR);
        return this;
    }

    /**
     * having ... and ( ...,需要 having_end()结尾
     */
    public HqlHelper having_and() {
        havingQueryParameters.begin(HavingQueryParameters.BeginOperator.AND);
        return this;
    }

    /**
     * having ... )，与having_or() having_and()配合使用
     */
    public HqlHelper having_end() {
        havingQueryParameters.end();
        return this;
    }

    /**
     * 对分组结果进行排序
     */
    public HqlHelper group_orderBy(OrderType orderType) {
        groupByFunctions.get(groupByFunctions.size() - 1).setOrderType(orderType);
        return this;
    }

    /**
     * 拼接链式字段，比如：city.province.country.name
     */
    public static String makeFieldChain(String... fields) {
        return String.join(".", fields);
    }
}
