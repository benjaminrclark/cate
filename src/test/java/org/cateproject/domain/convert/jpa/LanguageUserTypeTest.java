package org.cateproject.domain.convert.jpa;

import static org.junit.Assert.*;

import java.util.Locale;

import org.hibernate.type.StandardBasicTypes;
import org.hibernate.usertype.UserType;
import org.junit.Before;
import org.junit.Test;

public class LanguageUserTypeTest {
	
	UserType languageUserType;

	@Before
	public void setUp() throws Exception {
		languageUserType = new LanguageUserType();
	}

	@Test
	public void testAssemble() {
		String cached = "CACHED";
		assertTrue("assemble should return the cached object", languageUserType.assemble(cached, new Object()) == cached);
	}

	@Test
	public void testDeepCopy() {
		assertNull("deepCopy should return null for a null locale", languageUserType.deepCopy(null));
		assertEquals("deepCopy should return a copy of the locale", languageUserType.deepCopy(Locale.ENGLISH), Locale.ENGLISH);
		assertFalse("deepCopy should return a copy of the locale", languageUserType.deepCopy(Locale.ENGLISH) == Locale.ENGLISH);
	}

	@Test
	public void testDisassemble() {
		assertEquals("disassemble should return the object", languageUserType.disassemble(Locale.ENGLISH), Locale.ENGLISH);
	}

	@Test
	public void testEqualsObjectObject() {
		assertTrue("equals should return true if the locales are equal", languageUserType.equals(Locale.ENGLISH, Locale.ENGLISH));
		assertFalse("equals should return false if the locales are not equal", languageUserType.equals(Locale.ENGLISH, Locale.UK));
	}

	@Test
	public void testHashCodeObject() {
		assertEquals("hashcode should return the hashcode of the locale", languageUserType.hashCode(Locale.ENGLISH), Locale.ENGLISH.hashCode());
	}

	@Test
	public void testIsMutable() {
		assertTrue("isMutable should return true", languageUserType.isMutable());
	}

	@Test
	public void testReplace() {
		Object original = new Object();
		assertTrue("replace should return the original", languageUserType.replace(original, new Object(), new Object()) == original);
	}

	@Test
	public void testReturnedClass() {
		assertEquals("returnedClass should be Locale.class",languageUserType.returnedClass(), Locale.class);
	}

	@Test
	public void testSqlTypes() {
		assertArrayEquals("languageUserType should store the locale as a string", languageUserType.sqlTypes(), new int[] {StandardBasicTypes.STRING.sqlType()});
	}

}
