package org.cateproject.domain.constants;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DescriptionTypeTest {

	@Test
	public void test() {
		assertEquals("DescriptionType.associations.name() should return 'associations'", DescriptionType.associations.name(), "associations");
	}

}
