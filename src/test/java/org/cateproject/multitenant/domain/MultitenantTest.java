package org.cateproject.multitenant.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.easymock.EasyMock;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.junit.Before;
import org.junit.Test;

public class MultitenantTest {
	
	Multitenant tenant;
	
	@Before
	public void setUp() {
		tenant = new Multitenant();
		tenant.setId(1L);
		tenant.setAdminPassword("ADMIN_PASSWORD");
		tenant.setOwnerPassword("OWNER_PASSWORD");
		tenant.setIdentifier("identifier_0");
	}

	@Test
	public void testHashCode() {
		assertEquals("The hashCode should be determined by the surrogate key", tenant.hashCode(), "identifier_0".hashCode());
	}
	
	@Test
	public void testGetAdminPassword() {
		assertEquals("getAdminPassword should return the admin password", tenant.getAdminPassword(), "ADMIN_PASSWORD");
	}
	
	@Test
	public void testGetOwnerPassword() {
		assertEquals("getOwnerPassword should return the owner password", tenant.getOwnerPassword(), "OWNER_PASSWORD");
	}

	@Test
	public void testEqualsObject() {
		Multitenant tenant1 = new Multitenant();
		tenant1.setIdentifier("identifier_0");
		Multitenant tenant2 = new Multitenant();
		tenant2.setIdentifier("identifier_1");
		Multitenant tenant3 = new Multitenant();
		Multitenant tenant4 = new Multitenant();
		TenantProxy tenant5 = new TenantProxy();
		LazyInitializer lazyInitializer5 = EasyMock.createMock(LazyInitializer.class);
		tenant5.setHibernateLazyInitializer(lazyInitializer5);
		TenantProxy tenant6 = new TenantProxy();
		LazyInitializer lazyInitializer6 = EasyMock.createMock(LazyInitializer.class);
		tenant6.setIdentifier("identifier_0");
		tenant6.setHibernateLazyInitializer(lazyInitializer6);
		TenantProxy tenant7 = new TenantProxy();
		LazyInitializer lazyInitializer7 = EasyMock.createMock(LazyInitializer.class);
		tenant7.setHibernateLazyInitializer(lazyInitializer7);
		Multitenant tenant8 = new Multitenant();
		tenant8.setId(1L);
		Multitenant tenant9 = new Multitenant();
		tenant9.setId(1L);
		
		EasyMock.expect(lazyInitializer5.getPersistentClass()).andReturn(Long.class);
		EasyMock.expect(lazyInitializer6.getPersistentClass()).andReturn(Multitenant.class);
		EasyMock.expect(lazyInitializer7.getPersistentClass()).andReturn(Multitenant.class);
		
		
		EasyMock.replay(lazyInitializer5, lazyInitializer6, lazyInitializer7);
		assertTrue("Equals should return true when the objects are equal", tenant.equals(tenant));
		assertFalse("Equals should return false when the other object is null", tenant.equals(null));
		assertFalse("Equals should return false when the other object is has a null identifier and a null id", tenant1.equals(tenant2));
		assertFalse("Equals should return false when the other object is not a Tenant", tenant.equals(new Long(1)));
		assertTrue("Equals should return true when the other object has the same identifier", tenant.equals(tenant1));
		assertFalse("Equals should return false when the other object has a different identifier", tenant.equals(tenant2));
		assertFalse("Equals should return false when the other object has a different identifier", tenant3.equals(tenant4));
		assertFalse("Equals should return false when the other other object is proxied and is a different class", tenant.equals(tenant5));
		assertTrue("Equals should return true when the other other object is proxied and is the same tenant", tenant.equals(tenant6));
		assertFalse("Equals should return false when the other other object is proxied and neither tenants have the same identifier", tenant3.equals(tenant7));
		assertTrue("Equals should return true when neither object have an identifier and they have the same id", tenant8.equals(tenant9));
		EasyMock.verify(lazyInitializer5, lazyInitializer6, lazyInitializer7);
	}
	
	class TenantProxy extends Multitenant implements HibernateProxy {
		
		private LazyInitializer lazyInitializer;

		@Override
		public Object writeReplace() {
			return null;
		}

		@Override
		public LazyInitializer getHibernateLazyInitializer() {
			return lazyInitializer;
		}
		
		public void setHibernateLazyInitializer(LazyInitializer lazyInitializer) {
			this.lazyInitializer = lazyInitializer;
		}
	}
	
	

}
