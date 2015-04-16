package org.cateproject.multitenant.jpa;

import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;

public class MultiTenantConnectionProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected static final Log logger = LogFactory.getLog(MultiTenantConnectionProvider.class);
	
	private DataSource defaultDataSource;

	private Map<String, DataSource> dataSources;

	private Object defaultTenantIdentifier = "localhost";
	
	public void setDefaultTenantIdentifier(String defaultTenantIdentifier) {
		this.defaultTenantIdentifier = defaultTenantIdentifier;
	}
	
	public void setDefaultDataSource(DataSource defaultDataSource) {
		this.defaultDataSource = defaultDataSource;
	}
	
	public void setDataSources(Map<String, DataSource> dataSources) {
		this.dataSources = dataSources;
	}
	
	@Override
	public DataSource selectAnyDataSource() {
		return defaultDataSource;
	}
	
	@Override
	public DataSource selectDataSource(String tenantIdentifier) {
		if(tenantIdentifier.equals(defaultTenantIdentifier )) {
		  return defaultDataSource;	
		} else {
	      return dataSources.get(tenantIdentifier);
		}
	}
}
