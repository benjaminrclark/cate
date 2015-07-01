package org.cateproject.domain.constants;

import static org.junit.Assert.*;

import org.junit.Test;

public class SexTest {

	@Test
	public void test() {
		assertEquals("Sex.male.name() should return 'male'", Sex.male.name(), "male");
	}

}
