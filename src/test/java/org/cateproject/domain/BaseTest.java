package org.cateproject.domain;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class BaseTest {

	private Base base;
	
	@Before
	public void setUp() throws Exception {
		base = new Base();
		base.setIdentifier("IDENTIFIER");
		base.setId(1L);
		base.setVersion(1);
		base.setAccessRights("ACCESS_RIGHTS");
		base.setContributor("CONTRIBUTOR");
		base.setCreator("CREATOR");
		base.setCreated(new DateTime(2000, 1, 1, 1, 1, 1));
		base.setLicense("LICENSE");
		base.setLine("LINE");
		base.setLineNumber(1);
		base.setModified(new DateTime(2000, 1, 1, 1, 1, 2));
		base.setRights("RIGHTS");
		base.setRightsHolder("RIGHTS_HOLDER");
	}

	@Test
	public void testGetIdentifier() {
		assertNull("identifier should equal null", base.getIdentifier());
	}

	@Test
	public void testToString() {
		assertEquals("toString should return Base<null>","Base<null>", base.toString());
	}

	@Test
	public void testGetId() {
		assertEquals("id should equal 1",new Long(1), base.getId());
	}

	@Test
	public void testGetVersion() {
		assertEquals("version should equal 1",new Integer(1), base.getVersion());
	}

	@Test
	public void testGetCreator() {
		assertEquals("creator should equal 'CREATOR'","CREATOR", base.getCreator());
	}

	@Test
	public void testGetAccessRights() {
		assertEquals("accessRights should equal 'ACCESS_RIGHTS'","ACCESS_RIGHTS", base.getAccessRights());
	}

	@Test
	public void testGetCreated() {
		assertEquals("created should equal 2000-01-01T01:01:01",new DateTime(2000, 1, 1, 1, 1, 1), base.getCreated());
	}

	@Test
	public void testGetModified() {
		assertEquals("modified should equal 2000-01-01T01:01:02",new DateTime(2000, 1, 1, 1, 1, 2), base.getModified());
	}

	@Test
	public void testGetLicense() {
		assertEquals("license should equal 'LICENSE'","LICENSE", base.getLicense());
	}

	@Test
	public void testGetRights() {
		assertEquals("rights should equal 'RIGHTS'","RIGHTS", base.getRights());
	}

	@Test
	public void testGetRightsHolder() {
		assertEquals("rightsHolder should equal 'RIGHTS_HOLDER'","RIGHTS_HOLDER", base.getRightsHolder());
	}

	@Test
	public void testGetContributor() {
		assertEquals("contributor should equal 'CONTRIBUTOR'","CONTRIBUTOR", base.getContributor());
	}

	@Test
	public void testGetLine() {
		assertEquals("line should equal 'LINE'","LINE", base.getLine());
	}

	@Test
	public void testGetLineNumber() {
		assertEquals("lineNumber should equal 1",new Integer(1), base.getLineNumber());
	}

}
