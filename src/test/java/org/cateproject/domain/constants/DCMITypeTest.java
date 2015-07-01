package org.cateproject.domain.constants;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DCMITypeTest {


	@Test
	public void test() {
		assertEquals("DCMIType.Sound.name() should return 'DCMIType.Sound'", DCMIType.Sound.name(), "Sound");
	}

}
