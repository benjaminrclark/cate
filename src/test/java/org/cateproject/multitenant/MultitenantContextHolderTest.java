package org.cateproject.multitenant;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class MultitenantContextHolderTest {
	
	MultitenantContext tenantContext;
	
	@Before
	public void setUp() {
		tenantContext = new MultitenantContextImpl();
		MultitenantContextHolder.setContext(tenantContext);
	}

	@Test
	public void testGetContext() {
		assertEquals("getContext should return the expected tenant context", MultitenantContextHolder.getContext(), tenantContext);
	}
	
	@Test
	public void testSetContext() {
		MultitenantContext otherContext = new MultitenantContextImpl();
		MultitenantContextHolder.setContext(otherContext);
		assertEquals("setContext should set the expected context", MultitenantContextHolder.getContext(), otherContext);
	}
}
