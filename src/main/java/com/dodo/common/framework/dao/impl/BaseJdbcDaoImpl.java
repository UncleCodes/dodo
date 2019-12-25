package com.dodo.common.framework.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.dodo.common.framework.dao.BaseJdbcDao;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class BaseJdbcDaoImpl implements BaseJdbcDao {
    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int[] batchUpdate(String sql, BatchPreparedStatementSetter pss) throws DataAccessException {
        return jdbcTemplate.batchUpdate(sql, pss);
    }

    public <T> int[][] batchUpdate(String sql, Collection<T> batchArgs, int batchSize,
            ParameterizedPreparedStatementSetter<T> pss) {
        return jdbcTemplate.batchUpdate(sql, batchArgs, batchSize, pss);
    }

    public int[] batchUpdate(String sql, List<Object[]> batchArgs, int[] argTypes) {
        return jdbcTemplate.batchUpdate(sql, batchArgs, argTypes);
    }

    public int[] batchUpdate(String sql, List<Object[]> batchArgs) {
        return jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    public int[] batchUpdate(String[] sql) throws DataAccessException {
        return jdbcTemplate.batchUpdate(sql);
    }

    public Map<String, Object> call(CallableStatementCreator arg0, List<SqlParameter> arg1) throws DataAccessException {
        return jdbcTemplate.call(arg0, arg1);
    }

    public <T> T execute(CallableStatementCreator arg0, CallableStatementCallback<T> arg1) throws DataAccessException {
        return jdbcTemplate.execute(arg0, arg1);
    }

    public <T> T execute(ConnectionCallback<T> arg0) throws DataAccessException {
        return jdbcTemplate.execute(arg0);
    }

    public <T> T execute(PreparedStatementCreator arg0, PreparedStatementCallback<T> arg1) throws DataAccessException {
        return jdbcTemplate.execute(arg0, arg1);
    }

    public <T> T execute(StatementCallback<T> arg0) throws DataAccessException {
        return jdbcTemplate.execute(arg0);
    }

    public <T> T execute(String callString, CallableStatementCallback<T> action) throws DataAccessException {
        return jdbcTemplate.execute(callString, action);
    }

    public <T> T execute(String sql, PreparedStatementCallback<T> action) throws DataAccessException {
        return jdbcTemplate.execute(sql, action);
    }

    public void execute(String sql) throws DataAccessException {
        jdbcTemplate.execute(sql);
    }

    public <T> T query(PreparedStatementCreator psc, PreparedStatementSetter pss, ResultSetExtractor<T> rse)
            throws DataAccessException {
        return jdbcTemplate.query(psc, pss, rse);
    }

    public <T> T query(PreparedStatementCreator psc, ResultSetExtractor<T> rse) throws DataAccessException {
        return jdbcTemplate.query(psc, rse);
    }

    public void query(PreparedStatementCreator psc, RowCallbackHandler rch) throws DataAccessException {
        jdbcTemplate.query(psc, rch);
    }

    public <T> List<T> query(PreparedStatementCreator psc, RowMapper<T> rowMapper) throws DataAccessException {
        return jdbcTemplate.query(psc, rowMapper);
    }

    public <T> T query(String sql, Object[] args, int[] argTypes, ResultSetExtractor<T> rse) throws DataAccessException {
        return jdbcTemplate.query(sql, args, argTypes, rse);
    }

    public void query(String sql, Object[] args, int[] argTypes, RowCallbackHandler rch) throws DataAccessException {
        jdbcTemplate.query(sql, args, argTypes, rch);
    }

    public <T> List<T> query(String sql, Object[] args, int[] argTypes, RowMapper<T> rowMapper)
            throws DataAccessException {
        return jdbcTemplate.query(sql, args, argTypes, rowMapper);
    }

    public <T> T query(String sql, Object[] args, ResultSetExtractor<T> rse) throws DataAccessException {
        return jdbcTemplate.query(sql, args, rse);
    }

    public void query(String sql, Object[] args, RowCallbackHandler rch) throws DataAccessException {
        jdbcTemplate.query(sql, args, rch);
    }

    public <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException {
        return jdbcTemplate.query(sql, args, rowMapper);
    }

    public <T> T query(String sql, PreparedStatementSetter pss, ResultSetExtractor<T> rse) throws DataAccessException {
        return jdbcTemplate.query(sql, pss, rse);
    }

    public void query(String sql, PreparedStatementSetter pss, RowCallbackHandler rch) throws DataAccessException {
        jdbcTemplate.query(sql, pss, rch);
    }

    public <T> List<T> query(String sql, PreparedStatementSetter pss, RowMapper<T> rowMapper)
            throws DataAccessException {
        return jdbcTemplate.query(sql, pss, rowMapper);
    }

    public <T> T query(String sql, ResultSetExtractor<T> rse, Object... args) throws DataAccessException {
        return jdbcTemplate.query(sql, rse, args);
    }

    public <T> T query(String sql, ResultSetExtractor<T> rse) throws DataAccessException {
        return jdbcTemplate.query(sql, rse);
    }

    public void query(String sql, RowCallbackHandler rch, Object... args) throws DataAccessException {
        jdbcTemplate.query(sql, rch, args);
    }

    public void query(String sql, RowCallbackHandler rch) throws DataAccessException {
        jdbcTemplate.query(sql, rch);
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args) throws DataAccessException {
        return jdbcTemplate.query(sql, rowMapper, args);
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper) throws DataAccessException {
        return jdbcTemplate.query(sql, rowMapper);
    }

    public int queryForInt(String sql, Object... args) throws DataAccessException {
        return jdbcTemplate.queryForObject(sql, Integer.class, args);
    }

    public int queryForInt(String sql, Object[] args, int[] argTypes) throws DataAccessException {
        return jdbcTemplate.queryForObject(sql, args, argTypes, Integer.class);
    }

    public int queryForInt(String sql) throws DataAccessException {
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public <T> List<T> queryForList(String sql, Class<T> elementType, Object... args) throws DataAccessException {
        return jdbcTemplate.queryForList(sql, elementType, args);
    }

    public <T> List<T> queryForList(String sql, Class<T> elementType) throws DataAccessException {
        return jdbcTemplate.queryForList(sql, elementType);
    }

    public List<Map<String, Object>> queryForList(String sql, Object... args) throws DataAccessException {
        return jdbcTemplate.queryForList(sql, args);
    }

    public <T> List<T> queryForList(String sql, Object[] args, Class<T> elementType) throws DataAccessException {
        return jdbcTemplate.queryForList(sql, args, elementType);
    }

    public <T> List<T> queryForList(String sql, Object[] args, int[] argTypes, Class<T> elementType)
            throws DataAccessException {
        return jdbcTemplate.queryForList(sql, args, argTypes, elementType);
    }

    public List<Map<String, Object>> queryForList(String sql, Object[] args, int[] argTypes) throws DataAccessException {
        return jdbcTemplate.queryForList(sql, args, argTypes);
    }

    public List<Map<String, Object>> queryForList(String sql) throws DataAccessException {
        return jdbcTemplate.queryForList(sql);
    }

    public long queryForLong(String sql, Object... args) throws DataAccessException {
        return jdbcTemplate.queryForObject(sql, Long.class, args);
    }

    public long queryForLong(String sql, Object[] args, int[] argTypes) throws DataAccessException {
        return jdbcTemplate.queryForObject(sql, args, argTypes, Long.class);
    }

    public long queryForLong(String sql) throws DataAccessException {
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    public Map<String, Object> queryForMap(String sql, Object... args) throws DataAccessException {
        return jdbcTemplate.queryForMap(sql, args);
    }

    public Map<String, Object> queryForMap(String sql, Object[] args, int[] argTypes) throws DataAccessException {
        return jdbcTemplate.queryForMap(sql, args, argTypes);
    }

    public Map<String, Object> queryForMap(String sql) throws DataAccessException {
        return jdbcTemplate.queryForMap(sql);
    }

    public <T> T queryForObject(String sql, Class<T> requiredType, Object... args) throws DataAccessException {
        return jdbcTemplate.queryForObject(sql, requiredType, args);
    }

    public <T> T queryForObject(String sql, Class<T> requiredType) throws DataAccessException {
        return jdbcTemplate.queryForObject(sql, requiredType);
    }

    public <T> T queryForObject(String sql, Object[] args, Class<T> requiredType) throws DataAccessException {
        return jdbcTemplate.queryForObject(sql, args, requiredType);
    }

    public <T> T queryForObject(String sql, Object[] args, int[] argTypes, Class<T> requiredType)
            throws DataAccessException {
        return jdbcTemplate.queryForObject(sql, args, argTypes, requiredType);
    }

    public <T> T queryForObject(String sql, Object[] args, int[] argTypes, RowMapper<T> rowMapper)
            throws DataAccessException {
        return jdbcTemplate.queryForObject(sql, args, argTypes, rowMapper);
    }

    public <T> T queryForObject(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException {
        return jdbcTemplate.queryForObject(sql, args, rowMapper);
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... args) throws DataAccessException {
        return jdbcTemplate.queryForObject(sql, rowMapper, args);
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper) throws DataAccessException {
        return jdbcTemplate.queryForObject(sql, rowMapper);
    }

    public SqlRowSet queryForRowSet(String sql, Object... args) throws DataAccessException {
        return jdbcTemplate.queryForRowSet(sql, args);
    }

    public SqlRowSet queryForRowSet(String sql, Object[] args, int[] argTypes) throws DataAccessException {
        return jdbcTemplate.queryForRowSet(sql, args, argTypes);
    }

    public SqlRowSet queryForRowSet(String sql) throws DataAccessException {
        return jdbcTemplate.queryForRowSet(sql);
    }

    public int update(PreparedStatementCreator psc, KeyHolder generatedKeyHolder) throws DataAccessException {
        return jdbcTemplate.update(psc, generatedKeyHolder);
    }

    public int update(PreparedStatementCreator psc) throws DataAccessException {
        return jdbcTemplate.update(psc);
    }

    public int update(String sql, Object... args) throws DataAccessException {
        return jdbcTemplate.update(sql, args);
    }

    public int update(String sql, Object[] args, int[] argTypes) throws DataAccessException {
        return jdbcTemplate.update(sql, args, argTypes);
    }

    public int update(String sql, PreparedStatementSetter pss) throws DataAccessException {
        return jdbcTemplate.update(sql, pss);
    }

    public int update(String sql) throws DataAccessException {
        return jdbcTemplate.update(sql);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return jdbcTemplate.getDataSource().getConnection();
    }
}
