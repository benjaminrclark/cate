package org.cateproject.multitenant.jpa;

import static org.junit.Assert.*;

import org.cateproject.multitenant.MultitenantContextHolder;
import org.junit.Before;
import org.junit.Test;

public class MultiTenantIdentifierResolverTest {

	private MultiTenantIdentifierResolver multiTenantIdentifierResolver;
	
	@Before
	public void setUp() {
		multiTenantIdentifierResolver = new MultiTenantIdentifierResolver();
		MultitenantContextHolder.getContext().setTenantId(null);
	}
	
	@Test
	public void testSetDefaultTenantIdentifier() {
		multiTenantIdentifierResolver.setDefaultTenantIdentifier("defaultTenantIdentifier");
		assertEquals("Tenant identifier should equal 'defaultTenantIdentifier'", multiTenantIdentifierResolver.resolveCurrentTenantIdentifier(), "defaultTenantIdentifier");
	}

	@Test
	public void testResolveCurrentTenantIdentifier() {
		assertEquals("Tenant identifier should equal 'localhost'", multiTenantIdentifierResolver.resolveCurrentTenantIdentifier(), "localhost");
		MultitenantContextHolder.getContext().setTenantId("tenantId");
		assertEquals("Tenant identifier should equal 'tenantId'", multiTenantIdentifierResolver.resolveCurrentTenantIdentifier(), "tenantId");
	}

	@Test
	public void testValidateExistingCurrentSessions() {
		assertTrue("validateExistingCurrentSessions should return true", multiTenantIdentifierResolver.validateExistingCurrentSessions());
	}

}
