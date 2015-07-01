package org.cateproject.multitenant.jdbc.repository;

import static org.junit.Assert.*;

import javax.sql.DataSource;

import org.cateproject.multitenant.jdbc.DatabaseTypeDetector;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

public class TenantRepositoryFactoryTest {
	
	private DataSource dataSource;
	
	private DatabaseTypeDetector databaseTypeDetector;
	
	private MultitenantRepositoryFactory tenantRepositoryFactory;
	
	@Before
	public void setUp() {
		tenantRepositoryFactory = new MultitenantRepositoryFactory();
		dataSource = EasyMock.createMock(DataSource.class);
		databaseTypeDetector = EasyMock.createMock(DatabaseTypeDetector.class);
		tenantRepositoryFactory.setDataSource(dataSource);
		tenantRepositoryFactory.setDatabaseTypeDetector(databaseTypeDetector);
	}

	@Test
	public void testSetDataSource() {
		EasyMock.replay(dataSource,databaseTypeDetector);
		tenantRepositoryFactory.setDataSource(null);
		assertNull("DataSource should be null", tenantRepositoryFactory.getDataSource());
		tenantRepositoryFactory.setDataSource(dataSource);
		assertEquals("DataSource should equal dataSource", dataSource, tenantRepositoryFactory.getDataSource());
		EasyMock.verify(dataSource,databaseTypeDetector);
	}
	
	@Test
	public void testGetDataSource() {
		EasyMock.replay(dataSource,databaseTypeDetector);
		assertEquals("DataSource should equal dataSource", dataSource, tenantRepositoryFactory.getDataSource());
		EasyMock.verify(dataSource,databaseTypeDetector);
	}

	@Test
	public void testSetDatabaseTypeDetector() {
		EasyMock.replay(dataSource,databaseTypeDetector);
		tenantRepositoryFactory.setDatabaseTypeDetector(null);
		assertNull("DatabaseTypeDetector should be null", tenantRepositoryFactory.getDatabaseTypeDetector());
		tenantRepositoryFactory.setDatabaseTypeDetector(databaseTypeDetector);
		assertEquals("DatabaseTypeDetector should equal databaseTypeDetector", databaseTypeDetector, tenantRepositoryFactory.getDatabaseTypeDetector());
		EasyMock.verify(dataSource,databaseTypeDetector);
	}
	
	@Test
	public void testGetDatabaseTypeDetector() {
		EasyMock.replay(dataSource,databaseTypeDetector);
		assertEquals("DatabaseTypeDetector should equal databaseTypeDetector", databaseTypeDetector, tenantRepositoryFactory.getDatabaseTypeDetector());
		EasyMock.verify(dataSource,databaseTypeDetector);
	}

	@Test
	public void testCreateRepository() {
		EasyMock.expect(databaseTypeDetector.getDatabaseType(EasyMock.eq(dataSource))).andReturn("DATABASE_TYPE");
		EasyMock.replay(dataSource,databaseTypeDetector);
		tenantRepositoryFactory.createRepository();
		EasyMock.verify(dataSource,databaseTypeDetector);
	}

}
