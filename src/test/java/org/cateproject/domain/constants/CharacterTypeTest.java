package org.cateproject.domain.constants;

import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterTypeTest {

	@Test
	public void test() {
		assertEquals("CharacterType.IntegerNumeric.name() should return 'IntegerNumeric'", CharacterType.IntegerNumeric.name(), "IntegerNumeric");
	}

}
