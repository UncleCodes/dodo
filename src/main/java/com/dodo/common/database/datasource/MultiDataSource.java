package com.dodo.common.database.datasource;


import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
/**
 * <p>Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class MultiDataSource extends AbstractRoutingDataSource {
	@Override
	protected Object determineCurrentLookupKey() {
		return MultiDataSourceSelector.get();
	}
}
