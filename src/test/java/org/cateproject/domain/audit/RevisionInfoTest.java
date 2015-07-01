package org.cateproject.domain.audit;

import static org.junit.Assert.*;

import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class RevisionInfoTest {
	
	private RevisionInfo revisionInfo;

	@Before
	public void setUp() throws Exception {
		revisionInfo = new RevisionInfo();
		revisionInfo.setRevision(1L);
		revisionInfo.setTimestamp(new Date(2000, 1, 1, 1, 1, 1));
		revisionInfo.setUserName("USER_NAME");
	}

	@Test
	public void testGetRevision() {
		assertEquals("revision should equal 1L", new Long(1L), revisionInfo.getRevision());
	}

	@Test
	public void testGetTimestamp() {
		assertEquals("timestamp should equal 2000-01-01T01:01:01", new Date(2000, 1, 1, 1, 1, 1), revisionInfo.getTimestamp());
	}

	@Test
	public void testGetUserName() {
		assertEquals("userName should equal 'USER_NAME'", "USER_NAME", revisionInfo.getUserName());
	}

}
