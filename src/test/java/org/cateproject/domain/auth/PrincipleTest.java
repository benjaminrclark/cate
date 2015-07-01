package org.cateproject.domain.auth;

import static org.junit.Assert.*;

import org.cateproject.multitenant.MultitenantContextHolder;
import org.junit.Before;
import org.junit.Test;

public class PrincipleTest {
	
	private UserAccount userAccount;

	@Before
	public void setUp() throws Exception {
		userAccount = new UserAccount();
		userAccount.setId(1L);
		userAccount.setTenant("TENANT");
		userAccount.setVersion(1);
	}

	@Test
	public void testGetId() {
		assertEquals("id should equal 1L", new Long(1L), userAccount.getId());
	}

	@Test
	public void testGetVersion() {
		assertEquals("version should equal 1", new Integer(1), userAccount.getVersion());
	}

	@Test
	public void testGetTenant() {
		assertEquals("tenant should equal 'TENANT'", "TENANT", userAccount.getTenant());
	}

	@Test
	public void testPrePersist() {
		userAccount.setTenant(null);
		assertNull("tenant should equal null", userAccount.getTenant());
		MultitenantContextHolder.getContext().setTenantId("TENANT");
		userAccount.prePersist();
		assertEquals("tenant should equal 'TENANT'", "TENANT", userAccount.getTenant());
	}

}
