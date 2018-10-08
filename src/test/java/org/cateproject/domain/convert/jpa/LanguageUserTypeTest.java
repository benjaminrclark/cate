package org.cateproject.domain.convert.jpa;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

import org.easymock.EasyMock;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.junit.Before;
import org.junit.Test;

public class LanguageUserTypeTest {
	
	LanguageUserType languageUserType;

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
	
	@Test(expected = AssertionError.class)
	public void testHashCodeNull() {
		languageUserType.hashCode(null);
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
	
	@Test
	public void testNullSafeSetWithValidValue() throws SQLException {
		Type type = EasyMock.createMock(Type.class);
		languageUserType.setType(type);
		PreparedStatement preparedStatement = EasyMock.createMock(PreparedStatement.class);
		SessionImplementor sessionImplementor = EasyMock.createMock(SessionImplementor.class);
		
		type.nullSafeSet(EasyMock.eq(preparedStatement), EasyMock.eq("en"), EasyMock.eq(1), EasyMock.eq(sessionImplementor));
		
		EasyMock.replay(type, preparedStatement, sessionImplementor);
		languageUserType.nullSafeSet(preparedStatement, Locale.ENGLISH, 1, sessionImplementor);
		EasyMock.verify(type, preparedStatement, sessionImplementor);
	}
	
	@Test
	public void testNullSafeSetWithNullValue() throws SQLException {
		Type type = EasyMock.createMock(Type.class);
		languageUserType.setType(type);
		PreparedStatement preparedStatement = EasyMock.createMock(PreparedStatement.class);
		SessionImplementor sessionImplementor = EasyMock.createMock(SessionImplementor.class);
		
		type.nullSafeSet(EasyMock.eq(preparedStatement), EasyMock.isNull(), EasyMock.eq(1), EasyMock.eq(sessionImplementor));
		
		EasyMock.replay(type, preparedStatement, sessionImplementor);
		languageUserType.nullSafeSet(preparedStatement, null, 1, sessionImplementor);
		EasyMock.verify(type, preparedStatement, sessionImplementor);
	}
	
	@Test
	public void testNullSafeGetWithValidValue() throws SQLException {
		Type type = EasyMock.createMock(Type.class);
		languageUserType.setType(type);
		ResultSet resultSet = EasyMock.createMock(ResultSet.class);
		String[] names = new String[]{ "language" };
		SessionImplementor sessionImplementor = EasyMock.createMock(SessionImplementor.class);
		Object owner = new Object();
		
		EasyMock.expect(type.nullSafeGet(EasyMock.eq(resultSet), EasyMock.eq("language"), EasyMock.eq(sessionImplementor), EasyMock.eq(owner))).andReturn("en");
		
		EasyMock.replay(type);
		assertEquals("nullSafeGet should return a valid locale",Locale.ENGLISH,languageUserType.nullSafeGet(resultSet, names, sessionImplementor, owner));
		EasyMock.verify(type);
	}

        @Test
	public void testNullSafeGetWithNullValue() throws SQLException {
		Type type = EasyMock.createMock(Type.class);
		languageUserType.setType(type);
		ResultSet resultSet = EasyMock.createMock(ResultSet.class);
		String[] names = new String[]{ "language" };
		SessionImplementor sessionImplementor = EasyMock.createMock(SessionImplementor.class);
		Object owner = new Object();
		
		EasyMock.expect(type.nullSafeGet(EasyMock.eq(resultSet), EasyMock.eq("language"), EasyMock.eq(sessionImplementor), EasyMock.eq(owner))).andReturn(null);
		
		EasyMock.replay(type, resultSet, sessionImplementor);
		assertNull("nullSafeGet should return a null",languageUserType.nullSafeGet(resultSet, names, sessionImplementor, owner));
		EasyMock.verify(type, resultSet, sessionImplementor);
	}	
}
