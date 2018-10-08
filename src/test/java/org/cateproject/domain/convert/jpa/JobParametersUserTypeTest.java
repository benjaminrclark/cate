package org.cateproject.domain.convert.jpa;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.junit.Before;
import org.junit.Test;

import org.cateproject.batch.convert.JobParametersToStringConverter;
import org.cateproject.batch.convert.StringToJobParametersConverter;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.configuration.JobLocator;

public class JobParametersUserTypeTest {
	
	JobParametersUserType jobParametersUserType;

        JobParametersToStringConverter jobParametersToStringConverter;

        StringToJobParametersConverter stringToJobParametersConverter;

        JobParameters jobParameters;

	@Before
	public void setUp() throws Exception {
		jobParametersUserType = new JobParametersUserType();
                jobParameters = new JobParameters();
                jobParametersToStringConverter = EasyMock.createMock(JobParametersToStringConverter.class);
                stringToJobParametersConverter = EasyMock.createMock(StringToJobParametersConverter.class);
                jobParametersUserType.setJobParametersToStringConverter(jobParametersToStringConverter);
                jobParametersUserType.setStringToJobParametersConverter(stringToJobParametersConverter);
	}

	@Test
	public void testAssemble() throws NoSuchJobException {
		String cached = "CACHED";
		assertTrue("assemble should return the cached object", jobParametersUserType.assemble(cached, new Object()) == cached);
	}

	@Test
	public void testDeepCopy() {
		assertNull("deepCopy should return null for a null jobParameters", jobParametersUserType.deepCopy(null));
		assertEquals("deepCopy should return a copy of the jobParameters", jobParametersUserType.deepCopy(jobParameters), jobParameters);
		assertFalse("deepCopy should return a copy of the jobParameters", jobParametersUserType.deepCopy(jobParameters) == jobParameters);
	}

	@Test
	public void testDisassemble() {
		assertEquals("disassemble should return the jobParameters", jobParametersUserType.disassemble(jobParameters), jobParameters);
	}

	@Test
	public void testEqualsObjectObject() {
                Map<String,JobParameter> parameters = new HashMap<String, JobParameter>();
                parameters.put("name", new JobParameter("value"));
                JobParameters otherJobParameters = new JobParameters(parameters);
		assertTrue("equals should return true if the jobParameters are equal", jobParametersUserType.equals(jobParameters, jobParameters));
		assertFalse("equals should return false if the jobParameters are not equal", jobParametersUserType.equals(jobParameters, otherJobParameters));
	}

	@Test
	public void testHashCodeObject() {
		assertEquals("hashcode should return the hashcode of the jobParameters", jobParametersUserType.hashCode(jobParameters), jobParameters.hashCode());
	}
	
	@Test(expected = AssertionError.class)
	public void testHashCodeNull() {
		jobParametersUserType.hashCode(null);
	}

	@Test
	public void testIsMutable() {
		assertTrue("isMutable should return true", jobParametersUserType.isMutable());
	}

	@Test
	public void testReplace() {
		Object original = new Object();
		assertTrue("replace should return the original", jobParametersUserType.replace(original, new Object(), new Object()) == original);
	}

	@Test
	public void testReturnedClass() {
		assertEquals("returnedClass should be Job.class",jobParametersUserType.returnedClass(), JobParameters.class);
	}

	@Test
	public void testSqlTypes() {
		assertArrayEquals("jobParametersUserType should store the job as a string", jobParametersUserType.sqlTypes(), new int[] {StandardBasicTypes.STRING.sqlType()});
	}
	
	@Test
	public void testNullSafeSetWithValidValue() throws SQLException {
		Type type = EasyMock.createMock(Type.class);
		jobParametersUserType.setType(type);
		PreparedStatement preparedStatement = EasyMock.createMock(PreparedStatement.class);
		SessionImplementor sessionImplementor = EasyMock.createMock(SessionImplementor.class);
	        EasyMock.expect(jobParametersToStringConverter.convert(EasyMock.eq(jobParameters))).andReturn("value");

		type.nullSafeSet(EasyMock.eq(preparedStatement), EasyMock.eq("value"), EasyMock.eq(1), EasyMock.eq(sessionImplementor));
		
		EasyMock.replay(type, preparedStatement, sessionImplementor, jobParametersToStringConverter, stringToJobParametersConverter);
		jobParametersUserType.nullSafeSet(preparedStatement, jobParameters, 1, sessionImplementor);
		EasyMock.verify(type, preparedStatement, sessionImplementor, jobParametersToStringConverter, stringToJobParametersConverter);
	}
	
	@Test
	public void testNullSafeSetWithNullValue() throws SQLException {
		Type type = EasyMock.createMock(Type.class);
		jobParametersUserType.setType(type);
		PreparedStatement preparedStatement = EasyMock.createMock(PreparedStatement.class);

		SessionImplementor sessionImplementor = EasyMock.createMock(SessionImplementor.class);
		
		type.nullSafeSet(EasyMock.eq(preparedStatement), EasyMock.isNull(), EasyMock.eq(1), EasyMock.eq(sessionImplementor));
		
		EasyMock.replay(type, preparedStatement, sessionImplementor);
		jobParametersUserType.nullSafeSet(preparedStatement, null, 1, sessionImplementor);
		EasyMock.verify(type, preparedStatement, sessionImplementor);
	}

	@Test(expected = HibernateException.class)
	public void testNullSafeSetWithInvalidValue() throws SQLException {
		Type type = EasyMock.createMock(Type.class);
		jobParametersUserType.setType(type);
		PreparedStatement preparedStatement = EasyMock.createMock(PreparedStatement.class);
		SessionImplementor sessionImplementor = EasyMock.createMock(SessionImplementor.class);
	        EasyMock.expect(jobParametersToStringConverter.convert(EasyMock.eq(jobParameters))).andThrow(new IllegalArgumentException("exception"));

		type.nullSafeSet(EasyMock.eq(preparedStatement), EasyMock.eq("value"), EasyMock.eq(1), EasyMock.eq(sessionImplementor));
		
		EasyMock.replay(type, preparedStatement, sessionImplementor, jobParametersToStringConverter, stringToJobParametersConverter);
		jobParametersUserType.nullSafeSet(preparedStatement, jobParameters, 1, sessionImplementor);
	}
	
	@Test
	public void testNullSafeGetWithValidValue() throws SQLException, NoSuchJobException {
		Type type = EasyMock.createMock(Type.class);
		jobParametersUserType.setType(type);
		ResultSet resultSet = EasyMock.createMock(ResultSet.class);
		String[] names = new String[]{ "jobParameters" };
		SessionImplementor sessionImplementor = EasyMock.createMock(SessionImplementor.class);
		Object owner = new Object();
		
		EasyMock.expect(type.nullSafeGet(EasyMock.eq(resultSet), EasyMock.eq("jobParameters"), EasyMock.eq(sessionImplementor), EasyMock.eq(owner))).andReturn("value");
                EasyMock.expect(stringToJobParametersConverter.convert(EasyMock.eq("value"))).andReturn(jobParameters);
		
		EasyMock.replay(type, jobParametersToStringConverter, stringToJobParametersConverter);
		assertEquals("nullSafeGet should return a valid jobParameters",jobParameters,jobParametersUserType.nullSafeGet(resultSet, names, sessionImplementor, owner));
		EasyMock.verify(type, jobParametersToStringConverter, stringToJobParametersConverter);
	}

        @Test
	public void testNullSafeGetWithNullValue() throws SQLException {
		Type type = EasyMock.createMock(Type.class);
		jobParametersUserType.setType(type);
		ResultSet resultSet = EasyMock.createMock(ResultSet.class);
		String[] names = new String[]{ "jobParameters" };
		SessionImplementor sessionImplementor = EasyMock.createMock(SessionImplementor.class);
		Object owner = new Object();
		
		EasyMock.expect(type.nullSafeGet(EasyMock.eq(resultSet), EasyMock.eq("jobParameters"), EasyMock.eq(sessionImplementor), EasyMock.eq(owner))).andReturn(null);
		
		EasyMock.replay(type, resultSet, sessionImplementor);
		assertNull("nullSafeGet should return a null",jobParametersUserType.nullSafeGet(resultSet, names, sessionImplementor, owner));
		EasyMock.verify(type, resultSet, sessionImplementor);
	}	

        @Test(expected = HibernateException.class)
	public void testNullSafeGetWithConversionFailedException() throws SQLException {
		Type type = EasyMock.createMock(Type.class);
		jobParametersUserType.setType(type);
		ResultSet resultSet = EasyMock.createMock(ResultSet.class);
		String[] names = new String[]{ "jobParameters" };
		SessionImplementor sessionImplementor = EasyMock.createMock(SessionImplementor.class);
		Object owner = new Object();
		
		EasyMock.expect(type.nullSafeGet(EasyMock.eq(resultSet), EasyMock.eq("jobParameters"), EasyMock.eq(sessionImplementor), EasyMock.eq(owner))).andReturn("value");
                EasyMock.expect(stringToJobParametersConverter.convert(EasyMock.eq("value"))).andThrow(new IllegalArgumentException("exception"));
		
		EasyMock.replay(type, resultSet, sessionImplementor, jobParametersToStringConverter, stringToJobParametersConverter);
		jobParametersUserType.nullSafeGet(resultSet, names, sessionImplementor, owner);
	}	
}
