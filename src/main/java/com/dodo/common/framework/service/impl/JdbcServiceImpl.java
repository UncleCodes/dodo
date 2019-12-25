package com.dodo.common.framework.service.impl;

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
import org.springframework.transaction.annotation.Transactional;

import com.dodo.common.framework.dao.BaseJdbcDao;
import com.dodo.common.framework.service.JdbcService;

/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class JdbcServiceImpl implements JdbcService{
	private BaseJdbcDao jdbcDao;
	
	public BaseJdbcDao getJdbcDao() {
		return jdbcDao;
	}

	public void setJdbcDao(BaseJdbcDao jdbcDao) {
		this.jdbcDao = jdbcDao;
	}

	public int[] batchUpdate(String sql, BatchPreparedStatementSetter pss)
			throws DataAccessException {
		return this.jdbcDao.batchUpdate(sql, pss);
	}
	
	public <T> int[][] batchUpdate(String sql, Collection<T> batchArgs,
			int batchSize, ParameterizedPreparedStatementSetter<T> pss) {
		return this.jdbcDao.batchUpdate(sql, batchArgs, batchSize, pss);
	}

	public int[] batchUpdate(String sql, List<Object[]> batchArgs,
			int[] argTypes) {
		return this.jdbcDao.batchUpdate(sql, batchArgs, argTypes);
	}

	public int[] batchUpdate(String sql, List<Object[]> batchArgs) {
		return this.jdbcDao.batchUpdate(sql, batchArgs);
	}

	public int[] batchUpdate(String[] sql) throws DataAccessException {
		return this.jdbcDao.batchUpdate(sql);
	}

	public Map<String, Object> call(CallableStatementCreator arg0,
			List<SqlParameter> arg1) throws DataAccessException {
		return this.jdbcDao.call(arg0, arg1);
	}

	public <T> T execute(CallableStatementCreator arg0,
			CallableStatementCallback<T> arg1) throws DataAccessException {
		return this.jdbcDao.execute(arg0, arg1);
	}

	public <T> T execute(ConnectionCallback<T> arg0) throws DataAccessException {
		return this.jdbcDao.execute(arg0);
	}

	public <T> T execute(PreparedStatementCreator arg0,
			PreparedStatementCallback<T> arg1) throws DataAccessException {
		return this.jdbcDao.execute(arg0, arg1);
	}
	
	public <T> T execute(StatementCallback<T> arg0) throws DataAccessException {
		return this.jdbcDao.execute(arg0);
	}
	
	public <T> T execute(String callString, CallableStatementCallback<T> action)
			throws DataAccessException {
		return this.jdbcDao.execute(callString, action);
	}
	
	public <T> T execute(String sql, PreparedStatementCallback<T> action)
			throws DataAccessException {
		return this.jdbcDao.execute(sql, action);
	}
	
	public void execute(String sql) throws DataAccessException {
		this.jdbcDao.execute(sql);
	}

	@Transactional(readOnly=true)
	public <T> T query(PreparedStatementCreator psc,
			PreparedStatementSetter pss, ResultSetExtractor<T> rse)
			throws DataAccessException {
		return this.jdbcDao.query(psc, pss, rse);
	}
	
	@Transactional(readOnly=true)
	public <T> T query(PreparedStatementCreator psc, ResultSetExtractor<T> rse)
			throws DataAccessException {
		return this.jdbcDao.query(psc, rse);
	}

	@Transactional(readOnly=true)
	public void query(PreparedStatementCreator psc, RowCallbackHandler rch)
			throws DataAccessException {
		this.jdbcDao.query(psc, rch);
	}
	
	@Transactional(readOnly=true)
	public <T> List<T> query(PreparedStatementCreator psc,
			RowMapper<T> rowMapper) throws DataAccessException {
		return this.jdbcDao.query(psc, rowMapper);
	}
	
	@Transactional(readOnly=true)
	public <T> T query(String sql, Object[] args, int[] argTypes,
			ResultSetExtractor<T> rse) throws DataAccessException {
		return this.jdbcDao.query(sql, args, argTypes, rse);
	}
	
	@Transactional(readOnly=true)
	public void query(String sql, Object[] args, int[] argTypes,
			RowCallbackHandler rch) throws DataAccessException {
		this.jdbcDao.query(sql, args, argTypes, rch);
	}
	
	@Transactional(readOnly=true)
	public <T> List<T> query(String sql, Object[] args, int[] argTypes,
			RowMapper<T> rowMapper) throws DataAccessException {
		return this.jdbcDao.query(sql, args, argTypes, rowMapper);
	}
	
	@Transactional(readOnly=true)
	public <T> T query(String sql, Object[] args, ResultSetExtractor<T> rse)
			throws DataAccessException {
		return this.jdbcDao.query(sql, args, rse);
	}
	
	@Transactional(readOnly=true)
	public void query(String sql, Object[] args, RowCallbackHandler rch)
			throws DataAccessException {
		this.jdbcDao.query(sql, args, rch);
	}
	
	@Transactional(readOnly=true)
	public <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper)
			throws DataAccessException {
		return this.jdbcDao.query(sql, args, rowMapper);
	}
	
	@Transactional(readOnly=true)
	public <T> T query(String sql, PreparedStatementSetter pss,
			ResultSetExtractor<T> rse) throws DataAccessException {
		return this.jdbcDao.query(sql, pss, rse);
	}

	@Transactional(readOnly=true)
	public void query(String sql, PreparedStatementSetter pss,
			RowCallbackHandler rch) throws DataAccessException {
		this.jdbcDao.query(sql, pss, rch);
	}
	
	@Transactional(readOnly=true)
	public <T> List<T> query(String sql, PreparedStatementSetter pss,
			RowMapper<T> rowMapper) throws DataAccessException {
		return this.jdbcDao.query(sql, pss, rowMapper);
	}

	@Transactional(readOnly=true)
	public <T> T query(String sql, ResultSetExtractor<T> rse, Object... args)
			throws DataAccessException {
		return this.jdbcDao.query(sql, rse, args);
	}
	
	@Transactional(readOnly=true)
	public <T> T query(String sql, ResultSetExtractor<T> rse)
			throws DataAccessException {
		return this.jdbcDao.query(sql, rse);
	}
	
	@Transactional(readOnly=true)
	public void query(String sql, RowCallbackHandler rch, Object... args)
			throws DataAccessException {
		this.jdbcDao.query(sql, rch, args);
	}
	
	@Transactional(readOnly=true)
	public void query(String sql, RowCallbackHandler rch)
			throws DataAccessException {
		this.jdbcDao.query(sql, rch);
	}

	@Transactional(readOnly=true)
	public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args)
			throws DataAccessException {
		return this.jdbcDao.query(sql, rowMapper, args);
	}

	@Transactional(readOnly=true)
	public <T> List<T> query(String sql, RowMapper<T> rowMapper)
			throws DataAccessException {
		return this.jdbcDao.query(sql, rowMapper);
	}

	@Transactional(readOnly=true)
	public int queryForInt(String sql, Object... args)
			throws DataAccessException {
		return this.jdbcDao.queryForInt(sql, args);
	}

	@Transactional(readOnly=true)
	public int queryForInt(String sql, Object[] args, int[] argTypes)
			throws DataAccessException {
		return this.jdbcDao.queryForInt(sql, args, argTypes);
	}

	@Transactional(readOnly=true)
	public int queryForInt(String sql) throws DataAccessException {
		return this.jdbcDao.queryForInt(sql);
	}

	@Transactional(readOnly=true)
	public <T> List<T> queryForList(String sql, Class<T> elementType,
			Object... args) throws DataAccessException {
		return this.jdbcDao.queryForList(sql, elementType, args);
	}
	
	@Transactional(readOnly=true)
	public <T> List<T> queryForList(String sql, Class<T> elementType)
			throws DataAccessException {
		return this.jdbcDao.queryForList(sql, elementType);
	}
	
	@Transactional(readOnly=true)
	public List<Map<String, Object>> queryForList(String sql, Object... args)
			throws DataAccessException {
		return this.jdbcDao.queryForList(sql, args);
	}
	
	@Transactional(readOnly=true)
	public <T> List<T> queryForList(String sql, Object[] args,
			Class<T> elementType) throws DataAccessException {
		return this.jdbcDao.queryForList(sql, args, elementType);
	}

	@Transactional(readOnly=true)
	public <T> List<T> queryForList(String sql, Object[] args, int[] argTypes,
			Class<T> elementType) throws DataAccessException {
		return this.jdbcDao.queryForList(sql, args, argTypes, elementType);
	}
	
	@Transactional(readOnly=true)
	public List<Map<String, Object>> queryForList(String sql, Object[] args,
			int[] argTypes) throws DataAccessException {
		return this.jdbcDao.queryForList(sql, args, argTypes);
	}

	@Transactional(readOnly=true)
	public List<Map<String, Object>> queryForList(String sql)
			throws DataAccessException {
		return this.jdbcDao.queryForList(sql);
	}

	@Transactional(readOnly=true)
	public long queryForLong(String sql, Object... args)
			throws DataAccessException {
		return this.jdbcDao.queryForLong(sql, args);
	}
	
	@Transactional(readOnly=true)
	public long queryForLong(String sql, Object[] args, int[] argTypes)
			throws DataAccessException {
		return this.jdbcDao.queryForLong(sql, args, argTypes);
	}

	@Transactional(readOnly=true)
	public long queryForLong(String sql) throws DataAccessException {
		return this.jdbcDao.queryForLong(sql);
	}

	@Transactional(readOnly=true)
	public Map<String, Object> queryForMap(String sql, Object... args)
			throws DataAccessException {
		return this.jdbcDao.queryForMap(sql, args);
	}
	
	@Transactional(readOnly=true)
	public Map<String, Object> queryForMap(String sql, Object[] args,
			int[] argTypes) throws DataAccessException {
		return this.jdbcDao.queryForMap(sql, args, argTypes);
	}

	@Transactional(readOnly=true)
	public Map<String, Object> queryForMap(String sql)
			throws DataAccessException {
		return this.jdbcDao.queryForMap(sql);
	}

	@Transactional(readOnly=true)
	public <T> T queryForObject(String sql, Class<T> requiredType,
			Object... args) throws DataAccessException {
		return this.jdbcDao.queryForObject(sql, requiredType, args);
	}

	@Transactional(readOnly=true)
	public <T> T queryForObject(String sql, Class<T> requiredType)
			throws DataAccessException {
		return this.jdbcDao.queryForObject(sql, requiredType);
	}

	@Transactional(readOnly=true)
	public <T> T queryForObject(String sql, Object[] args, Class<T> requiredType)
			throws DataAccessException {
		return this.jdbcDao.queryForObject(sql, args, requiredType);
	}
	
	@Transactional(readOnly=true)
	public <T> T queryForObject(String sql, Object[] args, int[] argTypes,
			Class<T> requiredType) throws DataAccessException {
		return this.jdbcDao.queryForObject(sql, args, argTypes, requiredType);
	}
	
	@Transactional(readOnly=true)
	public <T> T queryForObject(String sql, Object[] args, int[] argTypes,
			RowMapper<T> rowMapper) throws DataAccessException {
		return this.jdbcDao.queryForObject(sql, args, argTypes, rowMapper);
	}
	
	@Transactional(readOnly=true)
	public <T> T queryForObject(String sql, Object[] args,
			RowMapper<T> rowMapper) throws DataAccessException {
		return this.jdbcDao.queryForObject(sql, args, rowMapper);
	}
	
	@Transactional(readOnly=true)
	public <T> T queryForObject(String sql, RowMapper<T> rowMapper,
			Object... args) throws DataAccessException {
		return this.jdbcDao.queryForObject(sql, rowMapper, args);
	}
	
	@Transactional(readOnly=true)
	public <T> T queryForObject(String sql, RowMapper<T> rowMapper)
			throws DataAccessException {
		return this.jdbcDao.queryForObject(sql, rowMapper);
	}

	@Transactional(readOnly=true)
	public SqlRowSet queryForRowSet(String sql, Object... args)
			throws DataAccessException {
		return this.jdbcDao.queryForRowSet(sql, args);
	}

	@Transactional(readOnly=true)
	public SqlRowSet queryForRowSet(String sql, Object[] args, int[] argTypes)
			throws DataAccessException {
		return this.jdbcDao.queryForRowSet(sql, args, argTypes);
	}

	@Transactional(readOnly=true)
	public SqlRowSet queryForRowSet(String sql) throws DataAccessException {
		return this.jdbcDao.queryForRowSet(sql);
	}

	public int update(PreparedStatementCreator psc, KeyHolder generatedKeyHolder)
			throws DataAccessException {
		return this.jdbcDao.update(psc, generatedKeyHolder);
	}

	public int update(PreparedStatementCreator psc) throws DataAccessException {
		return this.jdbcDao.update(psc);
	}

	public int update(String sql, Object... args) throws DataAccessException {
		return this.jdbcDao.update(sql, args);
	}

	public int update(String sql, Object[] args, int[] argTypes)
			throws DataAccessException {
		return this.jdbcDao.update(sql, args, argTypes);
	}

	public int update(String sql, PreparedStatementSetter pss)
			throws DataAccessException {
		return this.jdbcDao.update(sql, pss);
	}

	public int update(String sql) throws DataAccessException {
		return this.jdbcDao.update(sql);
	}

	@Override
	public Connection getConnection() throws SQLException{
		return this.jdbcDao.getConnection();
	}
}
