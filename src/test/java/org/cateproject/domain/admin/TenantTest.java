package org.cateproject.domain.admin;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class TenantTest {
	
	private Tenant tenant;
	
	private Map<String,String> properties;
	
	private Map<String,String> theme;

	@Before
	public void setUp() throws Exception {
		tenant = new Tenant();
		tenant.setId(1L);
		tenant.setVersion(1);
		tenant.setIdentifier("IDENTIFIER");
		tenant.setProperties(properties);
		tenant.setTheme(theme);
		tenant.setTitle("TITLE");
	}

	@Test
	public void testGetId() {
		assertEquals("id should equal 1", new Long(1L), tenant.getId());
	}

	@Test
	public void testGetVersion() {
		assertEquals("version should equal 1", new Integer(1), tenant.getVersion());
	}

	@Test
	public void testGetIdentifier() {
		assertEquals("identifier should equal 'IDENTIFIER'", "IDENTIFIER", tenant.getIdentifier());
	}

	@Test
	public void testGetTitle() {
		assertEquals("title should equal 'TITLE'", "TITLE", tenant.getTitle());
	}

	@Test
	public void testGetProperties() {
		assertEquals("properties should equal properties", properties, tenant.getProperties());
	}

	@Test
	public void testGetTheme() {
		assertEquals("theme should equal theme", theme, tenant.getTheme());
	}

}
