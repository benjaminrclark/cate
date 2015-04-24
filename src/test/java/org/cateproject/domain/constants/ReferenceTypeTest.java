package org.cateproject.domain.constants;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ReferenceTypeTest {

	@Test
	public void test() {
		assertEquals("ReferenceType.associations.name() should return 'checklist'", ReferenceType.checklist.name(), "checklist");
	}

}
