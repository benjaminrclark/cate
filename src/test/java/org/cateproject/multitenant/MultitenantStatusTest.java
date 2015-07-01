package org.cateproject.multitenant;

import static org.junit.Assert.*;

import org.cateproject.multitenant.event.MultitenantEvent;
import org.cateproject.multitenant.event.MultitenantEventType;
import org.junit.Before;
import org.junit.Test;

public class MultitenantStatusTest {
	
	private MultitenantStatus multitenantStatus;
	
	@Before
	public void setUp() {
		multitenantStatus = new MultitenantStatus();
	}

	@Test
	public void testIsInitialized() {
		assertFalse("multitenantStatus should be uninitialized", multitenantStatus.isInitialized());
	}

	@Test
	public void testSetInitialized() {
		assertFalse("multitenantStatus should be uninitialized", multitenantStatus.isInitialized());
		multitenantStatus.setInitialized(true);
		assertTrue("multitenantStatus should be initialized", multitenantStatus.isInitialized());
	}

	@Test
	public void testNotifyMultitenantEventCreate() {
		assertFalse("multitenantStatus should be uninitialized", multitenantStatus.isInitialized());
		MultitenantEvent tenantEvent = new MultitenantEvent();
		tenantEvent.setType(MultitenantEventType.CREATE);
		multitenantStatus.notify(tenantEvent);
		assertTrue("multitenantStatus should be initialized", multitenantStatus.isInitialized());
	}
	
	@Test
	public void testNotifyMultitenantEventHandle() {
		assertFalse("multitenantStatus should be uninitialized", multitenantStatus.isInitialized());
		MultitenantEvent tenantEvent = new MultitenantEvent();
		tenantEvent.setType(MultitenantEventType.CREATE);
		multitenantStatus.handle(tenantEvent);
		assertTrue("multitenantStatus should be initialized", multitenantStatus.isInitialized());
	}
	
	@Test
	public void testNotifyMultitenantEventOther() {
		assertFalse("multitenantStatus should be uninitialized", multitenantStatus.isInitialized());
		MultitenantEvent tenantEvent = new MultitenantEvent();
		tenantEvent.setType(MultitenantEventType.OTHER);
		multitenantStatus.notify(tenantEvent);
		assertFalse("multitenantStatus should not be initialized", multitenantStatus.isInitialized());
	}

}
