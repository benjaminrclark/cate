package org.cateproject.web.view;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ViewUtilsTest {

	private ViewUtils viewUtils;
	
	@Before
	public void setUp() throws Exception {
		viewUtils = new ViewUtils();
	}

	@Test
	public void testToCommaSeparatedString() {
		assertEquals("toCommaSeparatedString should return 'one,two,three'","one,two,three",viewUtils.toCommaSeparatedString(new Object[]{"one","two","three"}));
		assertNull("toCommaSeparatedString should return null", viewUtils.toCommaSeparatedString(null));
	}

}
