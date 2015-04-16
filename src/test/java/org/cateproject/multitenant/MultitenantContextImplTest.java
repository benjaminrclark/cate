package org.cateproject.multitenant;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MultitenantContextImplTest {
	
	private MultitenantContextImpl tenantContextImpl;
	
	@Before
	public void setUp() {
		tenantContextImpl = new MultitenantContextImpl();
		tenantContextImpl.putContextProperty("dwcArchiveDir", "DWC_ARCHIVE_DIR");
		tenantContextImpl.putContextProperty("processingImageFile", Boolean.FALSE);
		tenantContextImpl.setTenantId("TENANT_ID");
	}

	@Test
	public void testHashCode() {
		assertEquals("TenantContextImpl.hashCode should be the hashcode of the id", tenantContextImpl.hashCode(),"TENANT_ID".hashCode());
		tenantContextImpl.setTenantId(null);
		assertEquals("TenantContextImpl.hashCode should equal -1 if the tenant id is null", tenantContextImpl.hashCode(), -1);
	}

	@Test
	public void testToString() {
		assertEquals("TenantContextImpl.toString should be the TenantContext<TENANT_ID>", tenantContextImpl.toString(),"TenantContext<TENANT_ID>");
		tenantContextImpl.setTenantId(null);
		assertEquals("TenantContextImpl.toString should equal TenantContext<NULL> if the tenant id is null", tenantContextImpl.toString(), "TenantContext<NULL>");
	}

	@Test
	public void testEqualsObject() {
		MultitenantContextImpl nullIdTenantContextImpl = new MultitenantContextImpl();
		MultitenantContextImpl otherTenantContextImpl = new MultitenantContextImpl();
		otherTenantContextImpl.setTenantId("OTHER_TENANT_ID");
		
		assertFalse("TenantContextImpl.equals should return false if the other object is not an instance of TenantContextImpl", tenantContextImpl.equals(""));
		assertFalse("TenantContextImpl.equals should return false if the other object has a different tenant id", tenantContextImpl.equals(nullIdTenantContextImpl));
		assertTrue("TenantContextImpl.equals should return true if both objects have a null tenant id", nullIdTenantContextImpl.equals(nullIdTenantContextImpl));
		assertTrue("TenantContextImpl.equals should return true if both objects have the same tenant id", tenantContextImpl.equals(tenantContextImpl));
		assertFalse("TenantContextImpl.equals should return false if both objects have different tenant ids", tenantContextImpl.equals(otherTenantContextImpl));
	}
	
	@Test
	public void testGetContextProperty() {
		assertEquals("TenantContextImpl.getContextProperty should return the expected value", tenantContextImpl.getContextProperty("dwcArchiveDir"), "DWC_ARCHIVE_DIR");
	}

	@Test
	public void testPutContextProperty() {
		assertEquals("TenantContextImpl.putContextProperty should return the object", tenantContextImpl.putContextProperty("otherProperty", "propertyValue"), "propertyValue");
		assertEquals("TenantContextImpl.putContextProperty should set the expected value", tenantContextImpl.getContextProperty("otherProperty"), "propertyValue");
	}
	
	@Test
	public void testClearContextProperties() {
		tenantContextImpl.clearContextProperties();
		assertNull("TenantContextImpl.clearContextProperties should clear the context properties", tenantContextImpl.getContextProperty("dwcArchiveDir"));
	}
}
	