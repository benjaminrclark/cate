package org.cateproject.domain.auth;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PermissionTest {

	@Test
	public void testGetAuthority() {
		assertEquals("authority is based on the enum name",Permission.PERMISSION_ADMINISTRATE.getAuthority(), "PERMISSION_ADMINISTRATE");
	}
}
