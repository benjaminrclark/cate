package org.cateproject.multitenant.jpa;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

public class MultiTenantConnectionProviderTest {
	
	private MultiTenantConnectionProvider multiTenantConnectionProvider;
	
	private DataSource defaultDataSource;
	
	private DataSource dataSource1;
	
	private DataSource dataSource2;
	
	@Before
	public void setUp() {
		multiTenantConnectionProvider = new MultiTenantConnectionProvider();
		defaultDataSource = EasyMock.createMock(DataSource.class);
		dataSource1 = EasyMock.createMock(DataSource.class);
		dataSource2 = EasyMock.createMock(DataSource.class);
		
		Map<String, DataSource> dataSourceMap = new HashMap<String, DataSource>();
		dataSourceMap.put("tenant1", dataSource1);
		dataSourceMap.put("tenant2", dataSource2);
		multiTenantConnectionProvider.setDataSources(dataSourceMap);
		multiTenantConnectionProvider.setDefaultDataSource(defaultDataSource);
		
	}

	@Test
	public void testSelectDataSource() {
		assertEquals("selectDataSource should return dataSource1 for tenant identifier 'tenant1'",multiTenantConnectionProvider.selectDataSource("tenant1"), dataSource1);
		assertNull("selectDataSource should return null for tenant identifier 'tenantDoesNotExist'",multiTenantConnectionProvider.selectDataSource("tenantDoesNotExist"));
	}
	
	@Test
	public void testSelectAnyDataSource() {
		assertEquals("selecAnytDataSource should return defaultDataSource", multiTenantConnectionProvider.selectAnyDataSource(), defaultDataSource);
	}

}
