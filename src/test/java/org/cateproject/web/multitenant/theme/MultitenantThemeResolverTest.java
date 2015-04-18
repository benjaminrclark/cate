package org.cateproject.web.multitenant.theme;

import static org.junit.Assert.*;

import org.cateproject.multitenant.MultitenantContextHolder;
import org.cateproject.multitenant.MultitenantStatus;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class MultitenantThemeResolverTest {
	
	private MultitenantThemeResolver multitenantThemeResolver;
	
	private MultitenantStatus multitenantStatus;
	
	@Before
	public void setUp() {
		multitenantThemeResolver = new MultitenantThemeResolver();

		multitenantStatus = new MultitenantStatus();
		multitenantThemeResolver.setMultitenantStatus(multitenantStatus);
		multitenantThemeResolver.setDefaultTenantIdentifier("DEFAULT_TENANT_IDENTIFIER");
	}

	@Test
	public void testResolveThemeNameStatusIsUninitialized() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		multitenantStatus.setInitialized(false);
		assertEquals("Uninitialized applications should return the default tenant identifier", "DEFAULT_TENANT_IDENTIFIER", multitenantThemeResolver.resolveThemeName(request));
	}
	
	@Test
	public void testResolveThemeNameStatusIsInitializedTenantIdNull() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MultitenantContextHolder.getContext().setTenantId(null);
		multitenantStatus.setInitialized(true);
		assertEquals("Initialized applications should return the default tenant identifier if the tenant id is not set", "DEFAULT_TENANT_IDENTIFIER", multitenantThemeResolver.resolveThemeName(request));
	}
	
	@Test
	public void testResolveThemeNameStatusIsInitializedTenantIdSet() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MultitenantContextHolder.getContext().setTenantId("TENANT_IDENTIFIER");
		multitenantStatus.setInitialized(true);
		assertEquals("Initialized applications should return the tenant identifier if it is set", "TENANT_IDENTIFIER", multitenantThemeResolver.resolveThemeName(request));
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testSetThemeName() {
		multitenantThemeResolver.setThemeName(new MockHttpServletRequest(), new MockHttpServletResponse(), "THEME_NAME");
	}
}
