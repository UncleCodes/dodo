package com.dodo.common.sqlreport;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.dodo.utils.CommonUtil;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class SqlReportResultSetExtractor implements ResultSetExtractor<SqlReportQueryResult> {
    @Override
    public SqlReportQueryResult extractData(ResultSet arg0) throws SQLException, DataAccessException {
        SqlReportQueryResult queryResult = new SqlReportQueryResult();
        List<SqlReportQueryField> queryFields = new ArrayList<SqlReportQueryField>();
        List<List<String>> queryDatas = new ArrayList<List<String>>();
        ResultSetMetaData metaData = arg0.getMetaData();
        int columnCount = metaData.getColumnCount();
        int sqlType = -1;
        String columnName = null;
        int[] sqlTypes = new int[columnCount + 1];
        for (int i = 1; i <= columnCount; i++) {
            SqlReportQueryField queryField = new SqlReportQueryField();
            columnName = metaData.getColumnName(i);
            sqlType = metaData.getColumnType(i);
            queryField.setFieldName(CommonUtil.escapeHtml(columnName));
            queryField.setFieldType(SqlReportFactory.convertSQLType2ReportFieldType(sqlType).name());
            queryFields.add(queryField);
            sqlTypes[i] = sqlType;
        }

        while (arg0.next()) {
            List<String> dataList = new ArrayList<String>(columnCount);
            for (int i = 1; i <= columnCount; i++) {
                dataList.add(SqlReportFactory.convertData2String(arg0, i, sqlTypes[i]));
            }
            queryDatas.add(dataList);
        }
        queryResult.setQueryFields(queryFields);
        queryResult.setQueryDatas(queryDatas);
        return queryResult;
    }
}
