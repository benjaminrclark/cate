package org.cateproject.domain.auth;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PermissionTest {

	@Test
	public void testGetAuthority() {
		assertEquals("authority is based on the enum name",Permission.ROLE_ADMINISTRATE.getAuthority(), "ROLE_ADMINISTRATE");
	}
}
