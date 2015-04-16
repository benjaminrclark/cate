package org.cateproject.multitenant.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import liquibase.integration.spring.SpringLiquibase;

import org.cateproject.multitenant.MultitenantRepository;
import org.cateproject.multitenant.NoSuchTenantException;
import org.cateproject.multitenant.domain.MultitenantDataOnDemand;
import org.cateproject.multitenant.event.MultitenantEvent;
import org.cateproject.multitenant.event.MultitenantEventType;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.data.domain.PageRequest;

public class MultitenantDataSourceMapTest {
	
	private MultitenantDataSourceMap tenantDataSourceMap;
	
	private MultitenantRepository tenantRepository;
	
	private LiquibaseProperties liquibaseProperties;
	
	private SpringLiquibase liquibase;
	
	private DataSourceBuilder dataSourceBuilder;
	
	private DataSource dataSource;
	
	private MultitenantDataOnDemand tenantDataOnDemand = new MultitenantDataOnDemand();
	
	@Before
	public void setUp() {
	    tenantDataSourceMap = new MultitenantDataSourceMap();
	    tenantRepository = EasyMock.createMock(MultitenantRepository.class);
	    liquibaseProperties = EasyMock.createMock(LiquibaseProperties.class);
	    liquibase = EasyMock.createMock(SpringLiquibase.class);
	    dataSourceBuilder = EasyMock.createMock(DataSourceBuilder.class);
	    tenantDataSourceMap.setTenantRepository(tenantRepository);
	    tenantDataSourceMap.setLiquibaseProperties(liquibaseProperties);
	    tenantDataSourceMap.setLiquibase(liquibase);
	    tenantDataSourceMap.setDataSourceBuilder(dataSourceBuilder);
	    dataSource = EasyMock.createMock(DataSource.class);
	}

	@Test
	public void testAfterPropertiesSet() throws Exception {
		
		EasyMock.expect(liquibaseProperties.getChangeLog()).andReturn("CHANGELOG");
		EasyMock.expect(liquibaseProperties.getDefaultSchema()).andReturn("DEFAULT_SCHEMA");
		EasyMock.expect(liquibaseProperties.getContexts()).andReturn("CONTEXTS");
		EasyMock.expect(liquibaseProperties.isDropFirst()).andReturn(false);
		EasyMock.expect(liquibaseProperties.isEnabled()).andReturn(true);
		
		liquibase.setChangeLog(EasyMock.eq("CHANGELOG"));
		liquibase.setContexts(EasyMock.eq("CONTEXTS"));
		liquibase.setDefaultSchema(EasyMock.eq("DEFAULT_SCHEMA"));
		liquibase.setDropFirst(EasyMock.eq(false));
		liquibase.setShouldRun(EasyMock.eq(true));
		
		EasyMock.expect(tenantRepository.findAll(EasyMock.isA(PageRequest.class))).andReturn(tenantDataOnDemand.getNewTransientTenants(11, 0, 10));
		EasyMock.expect(tenantRepository.findAll(EasyMock.isA(PageRequest.class))).andReturn(tenantDataOnDemand.getNewTransientTenants(11, 1, 10));
		for(int i = 0; i < 11; i++) {			
			EasyMock.expect(dataSourceBuilder.driverClassName(EasyMock.eq("driverClassName_" + i))).andReturn(dataSourceBuilder);
			EasyMock.expect(dataSourceBuilder.url(EasyMock.eq("databaseUrl_" + i))).andReturn(dataSourceBuilder);
			EasyMock.expect(dataSourceBuilder.password(EasyMock.eq("databasePassword_" + i))).andReturn(dataSourceBuilder);
			EasyMock.expect(dataSourceBuilder.username(EasyMock.eq("databaseUsername_" + i))).andReturn(dataSourceBuilder);
			EasyMock.expect(dataSourceBuilder.build()).andReturn(dataSource);
			
			liquibase.setDataSource(EasyMock.eq(dataSource));
			liquibase.afterPropertiesSet();
	    }
		EasyMock.replay(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder);
		tenantDataSourceMap.afterPropertiesSet();
		EasyMock.verify(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder);
	}
	
	@Test
	public void testHandle() throws Exception {
		MultitenantEvent tenantEvent = new MultitenantEvent();
		tenantEvent.setType(MultitenantEventType.OTHER);
		tenantEvent.setIdentifier("identifier_0");
		
		EasyMock.replay(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder);
		tenantDataSourceMap.handle(tenantEvent);
		EasyMock.verify(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder);
	}

	@Test
	public void testNotifyTenantEventDefault() throws Exception {
		MultitenantEvent tenantEvent = new MultitenantEvent();
		tenantEvent.setType(MultitenantEventType.OTHER);
		tenantEvent.setIdentifier("identifier_0");
		
		EasyMock.replay(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder);
		tenantDataSourceMap.notify(tenantEvent);
		EasyMock.verify(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder);
	}
	
	@Test
	public void testNotifyTenantEventCreate() throws Exception {
		MultitenantEvent tenantEvent = new MultitenantEvent();
		tenantEvent.setType(MultitenantEventType.CREATE);
		tenantEvent.setIdentifier("identifier_0");
		
		EasyMock.expect(tenantRepository.findByIdentifier(EasyMock.eq("identifier_0"))).andReturn(tenantDataOnDemand.getNewTransientTenant(0));
		
		EasyMock.expect(dataSourceBuilder.driverClassName(EasyMock.eq("driverClassName_0"))).andReturn(dataSourceBuilder);
		EasyMock.expect(dataSourceBuilder.url(EasyMock.eq("databaseUrl_0"))).andReturn(dataSourceBuilder);
		EasyMock.expect(dataSourceBuilder.password(EasyMock.eq("databasePassword_0"))).andReturn(dataSourceBuilder);
		EasyMock.expect(dataSourceBuilder.username(EasyMock.eq("databaseUsername_0"))).andReturn(dataSourceBuilder);
		EasyMock.expect(dataSourceBuilder.build()).andReturn(dataSource);
		
		liquibase.setDataSource(EasyMock.eq(dataSource));
		liquibase.afterPropertiesSet();
		
		EasyMock.replay(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder);
		tenantDataSourceMap.notify(tenantEvent);
		EasyMock.verify(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder);
	}

	@Test
	public void testClose() {
		Map<String, DataSource> dataSources = new HashMap<String, DataSource>();
		org.apache.tomcat.jdbc.pool.DataSource dataSource1 = EasyMock.createMock(org.apache.tomcat.jdbc.pool.DataSource.class);
		dataSources.put("identifier_0", dataSource1);
		tenantDataSourceMap.setDataSources(dataSources);
		
		dataSource1.close();
		
		EasyMock.replay(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder, dataSource1);
		tenantDataSourceMap.close();
		EasyMock.verify(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder, dataSource1);

	}

	@Test
	public void testClear() {
		Map<String, DataSource> dataSources = new HashMap<String, DataSource>();
		org.apache.tomcat.jdbc.pool.DataSource dataSource1 = EasyMock.createMock(org.apache.tomcat.jdbc.pool.DataSource.class);
		dataSources.put("identifier_0", dataSource1);
		tenantDataSourceMap.setDataSources(dataSources);
		
		dataSource1.close();
		
		EasyMock.replay(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder, dataSource1);
		tenantDataSourceMap.clear();
		EasyMock.verify(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder, dataSource1);
	}

	@Test
	public void testContainsKey() {
		Map<String, DataSource> dataSources = new HashMap<String, DataSource>();
		org.apache.tomcat.jdbc.pool.DataSource dataSource = EasyMock.createMock(org.apache.tomcat.jdbc.pool.DataSource.class);
		dataSources.put("identifier_0", dataSource);
		tenantDataSourceMap.setDataSources(dataSources);
		
		EasyMock.replay(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder,dataSource);
		assertTrue("tenantDataSourceMap contains 'identifier_0'",tenantDataSourceMap.containsKey("identifier_0"));
		assertFalse("tenantDataSourceMap contains 'identifier_0'",tenantDataSourceMap.containsKey("identifier_1"));
		EasyMock.verify(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder,dataSource);
	}

	@Test
	public void testContainsValue() {
		Map<String, DataSource> dataSources = new HashMap<String, DataSource>();
		org.apache.tomcat.jdbc.pool.DataSource dataSource1 = EasyMock.createMock(org.apache.tomcat.jdbc.pool.DataSource.class);
		dataSources.put("identifier_0", dataSource1);
		tenantDataSourceMap.setDataSources(dataSources);
		
		EasyMock.replay(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder,dataSource1);
		assertTrue("tenantDataSourceMap contains dataSource1",tenantDataSourceMap.containsValue(dataSource1));
		assertFalse("tenantDataSourceMap contains 'identifier_0'",tenantDataSourceMap.containsValue(dataSource));
		EasyMock.verify(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder,dataSource1);
	}

	@Test
	public void testIsEmpty() {
		Map<String, DataSource> dataSources1 = new HashMap<String, DataSource>();
		Map<String, DataSource> dataSources2 = new HashMap<String, DataSource>();
		org.apache.tomcat.jdbc.pool.DataSource dataSource1 = EasyMock.createMock(org.apache.tomcat.jdbc.pool.DataSource.class);
		dataSources1.put("identifier_0", dataSource1);
		tenantDataSourceMap.setDataSources(dataSources2);
		
		EasyMock.replay(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder,dataSource1);
		assertTrue("tenantDataSourceMap is empty",tenantDataSourceMap.isEmpty());
		tenantDataSourceMap.setDataSources(dataSources1);
		assertFalse("tenantDataSourceMap is not empty",tenantDataSourceMap.isEmpty());
		EasyMock.verify(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder,dataSource1);
	}

	@Test
	public void testGet() {
		Map<String, DataSource> dataSources = new HashMap<String, DataSource>();
		org.apache.tomcat.jdbc.pool.DataSource dataSource1 = EasyMock.createMock(org.apache.tomcat.jdbc.pool.DataSource.class);
		dataSources.put("identifier_0", dataSource1);
		tenantDataSourceMap.setDataSources(dataSources);
		
		EasyMock.replay(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder,dataSource1);
		assertEquals("tenantDataSourceMap contains dataSource1 under key identifier_0",tenantDataSourceMap.get("identifier_0"), dataSource1);
		EasyMock.verify(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder,dataSource1);
	}
	
	@Test(expected = NoSuchTenantException.class)
	public void testGetThrowsNoSuchTenantException() {
		Map<String, DataSource> dataSources = new HashMap<String, DataSource>();
		org.apache.tomcat.jdbc.pool.DataSource dataSource1 = EasyMock.createMock(org.apache.tomcat.jdbc.pool.DataSource.class);
		dataSources.put("identifier_0", dataSource1);
		tenantDataSourceMap.setDataSources(dataSources);
		
		EasyMock.replay(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder,dataSource1);
		tenantDataSourceMap.get("identifier_1");
		EasyMock.verify(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder,dataSource1);
	}

	@Test
	public void testEntrySet() {
		Map<String, DataSource> dataSources = new HashMap<String, DataSource>();
		org.apache.tomcat.jdbc.pool.DataSource dataSource = EasyMock.createMock(org.apache.tomcat.jdbc.pool.DataSource.class);
		dataSources.put("identifier_0", dataSource);		
		tenantDataSourceMap.setDataSources(dataSources);
		
		EasyMock.replay(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder,dataSource);
		assertEquals("tenantDataSourceMap should contain the expected entrySet",tenantDataSourceMap.entrySet(), dataSources.entrySet());
		EasyMock.verify(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder,dataSource);
	}

	@Test
	public void testEquals() {
		Map<String, DataSource> dataSources1 = new HashMap<String, DataSource>();
		Map<String, DataSource> dataSources2 = new HashMap<String, DataSource>();
		org.apache.tomcat.jdbc.pool.DataSource dataSource1 = EasyMock.createMock(org.apache.tomcat.jdbc.pool.DataSource.class);
		dataSources1.put("identifier_0", dataSource1);
		tenantDataSourceMap.setDataSources(dataSources1);
		
		EasyMock.replay(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder,dataSource1);
		assertTrue("tenantDataSourceMap equals dataSources1",tenantDataSourceMap.equals(dataSources1));
		assertFalse("tenantDataSourceMap does not equal dataSources2",tenantDataSourceMap.equals(dataSources2));
		EasyMock.verify(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder,dataSource1);
	}
	
	@Test
	public void testHashcode() {
		Map<String, DataSource> dataSources1 = new HashMap<String, DataSource>();
		Map<String, DataSource> dataSources2 = new HashMap<String, DataSource>();
		org.apache.tomcat.jdbc.pool.DataSource dataSource1 = EasyMock.createMock(org.apache.tomcat.jdbc.pool.DataSource.class);
		dataSources1.put("identifier_0", dataSource1);
		tenantDataSourceMap.setDataSources(dataSources1);
		
		EasyMock.replay(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder,dataSource1);
		assertEquals("tenantDataSourceMap hashcode is equal to dataSources1",tenantDataSourceMap.hashCode(),dataSources1.hashCode()	);
		assertFalse("tenantDataSourceMap does not equal dataSources2",tenantDataSourceMap.equals(dataSources2));
		EasyMock.verify(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder,dataSource1);
	}

	@Test
	public void testKeySet() {
		Map<String, DataSource> dataSources = new HashMap<String, DataSource>();
		org.apache.tomcat.jdbc.pool.DataSource dataSource = EasyMock.createMock(org.apache.tomcat.jdbc.pool.DataSource.class);
		dataSources.put("identifier_0", dataSource);
		tenantDataSourceMap.setDataSources(dataSources);
		EasyMock.replay(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder,dataSource);
		assertEquals("tenantDataSourceMap should contain the expected keySet",tenantDataSourceMap.keySet(), dataSources.keySet());
		EasyMock.verify(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder,dataSource);
	}

	@Test
	public void testPutAll() {
		Map<String, DataSource> dataSources = new HashMap<String, DataSource>();
		Map<String, DataSource> dataSourceMap = new HashMap<String, DataSource>();
		org.apache.tomcat.jdbc.pool.DataSource dataSource1 = EasyMock.createMock(org.apache.tomcat.jdbc.pool.DataSource.class);
		org.apache.tomcat.jdbc.pool.DataSource dataSource2 = EasyMock.createMock(org.apache.tomcat.jdbc.pool.DataSource.class);
		dataSources.put("identifier_0", dataSource1);
		dataSourceMap.put("identifier_0", dataSource1);
		dataSourceMap.put("identifier_1", dataSource2);
		tenantDataSourceMap.setDataSources(dataSources);
		
		EasyMock.replay(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder,dataSource);
		tenantDataSourceMap.putAll(dataSourceMap);
		assertEquals("The underlying map should contain the added datasources", dataSources, dataSourceMap);
		EasyMock.verify(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder,dataSource);
	}

	@Test
	public void testPut() {
		Map<String, DataSource> dataSources = new HashMap<String, DataSource>();
		org.apache.tomcat.jdbc.pool.DataSource dataSource1 = EasyMock.createMock(org.apache.tomcat.jdbc.pool.DataSource.class);
		org.apache.tomcat.jdbc.pool.DataSource dataSource2 = EasyMock.createMock(org.apache.tomcat.jdbc.pool.DataSource.class);
		dataSources.put("identifier_0", dataSource2);
		tenantDataSourceMap.setDataSources(dataSources);
		
		EasyMock.replay(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder,dataSource);
		assertEquals("tenantDataSourceMap should return the old entry if an entry for a given key already exists",tenantDataSourceMap.put("identifier_0", dataSource1), dataSource2);
		assertNull("tenantDataSourceMap should return null if there was no entry for a given key",tenantDataSourceMap.put("identifier_1", dataSource2));
		assertEquals("The underlying map should contain the added datasource", dataSource1, dataSources.get("identifier_0"));
		assertEquals("The underlying map should contain the added datasource", dataSource2, dataSources.get("identifier_1"));
		EasyMock.verify(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder,dataSource);
	}

	@Test
	public void testRemove() {
		Map<String, DataSource> dataSources = new HashMap<String, DataSource>();
		org.apache.tomcat.jdbc.pool.DataSource dataSource = EasyMock.createMock(org.apache.tomcat.jdbc.pool.DataSource.class);
		dataSources.put("identifier_0", dataSource);
		tenantDataSourceMap.setDataSources(dataSources);
		
		EasyMock.replay(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder,dataSource);
		assertEquals("tenantDataSourceMap should allow you to remove an existing entry",tenantDataSourceMap.remove("identifier_0"), dataSource);
		assertFalse("The underlying map should not contain the datasource we just removed", dataSources.containsValue(dataSource));
		EasyMock.verify(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder,dataSource);
	}

	@Test
	public void testSize() {
		Map<String, DataSource> dataSources = new HashMap<String, DataSource>();
		org.apache.tomcat.jdbc.pool.DataSource dataSource = EasyMock.createMock(org.apache.tomcat.jdbc.pool.DataSource.class);
		dataSources.put("identifier_0", dataSource);
		tenantDataSourceMap.setDataSources(dataSources);
		EasyMock.replay(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder,dataSource);
		assertEquals("tenantDataSourceMap has a size of 1",tenantDataSourceMap.size() ,1);
		EasyMock.verify(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder,dataSource);
	}

	@Test
	public void testValues() {
		Map<String, DataSource> dataSources = new HashMap<String, DataSource>();
		org.apache.tomcat.jdbc.pool.DataSource dataSource = EasyMock.createMock(org.apache.tomcat.jdbc.pool.DataSource.class);
		dataSources.put("identifier_0", dataSource);
		tenantDataSourceMap.setDataSources(dataSources);
		EasyMock.replay(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder,dataSource);
		assertEquals("tenantDataSourceMap should contain the expected values",tenantDataSourceMap.values(), dataSources.values());
		EasyMock.verify(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder,dataSource);
	}
	
	@Test
	public void testGetDataSources() {
		Map<String, DataSource> dataSources = new HashMap<String, DataSource>();
		org.apache.tomcat.jdbc.pool.DataSource dataSource = EasyMock.createMock(org.apache.tomcat.jdbc.pool.DataSource.class);
		dataSources.put("identifier_0", dataSource);
		tenantDataSourceMap.setDataSources(dataSources);
		EasyMock.replay(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder,dataSource);
		assertEquals("tenantDataSourceMap should contain the expected map of data sources",tenantDataSourceMap.getDataSources(), dataSources);
		EasyMock.verify(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder,dataSource);
	}
	
	@Test
	public void testSetDataSources() {
		Map<String, DataSource> dataSources = new HashMap<String, DataSource>();
		org.apache.tomcat.jdbc.pool.DataSource dataSource = EasyMock.createMock(org.apache.tomcat.jdbc.pool.DataSource.class);
		dataSources.put("identifier_0", dataSource);
		
		EasyMock.replay(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder,dataSource);
		tenantDataSourceMap.setDataSources(dataSources);
		assertEquals("tenantDataSourceMap should contain the expected map of data sources",tenantDataSourceMap.getDataSources(), dataSources);
		EasyMock.verify(tenantRepository, liquibaseProperties, liquibase, dataSourceBuilder,dataSource);
	}

}
