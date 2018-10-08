package org.cateproject.domain.convert.jpa;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.ObjectUtils;
import org.cateproject.batch.convert.JobParametersToStringConverter;
import org.cateproject.batch.convert.StringToJobParametersConverter;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.hibernate.usertype.UserType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.batch.core.JobParameters;

public class JobParametersUserType implements UserType {

	private Type type = StandardBasicTypes.STRING;

        private StringToJobParametersConverter stringToJobParametersConverter = new StringToJobParametersConverter();

        private JobParametersToStringConverter jobParametersToStringConverter = new JobParametersToStringConverter();

        public void setStringToJobParametersConverter(StringToJobParametersConverter stringToJobParametersConverter) {
            this.stringToJobParametersConverter = stringToJobParametersConverter;
        }

        public void setJobParametersToStringConverter(JobParametersToStringConverter jobParametersToStringConverter) {
            this.jobParametersToStringConverter = jobParametersToStringConverter;
        }

	protected void setType(Type type) {
		this.type = type;
	}

	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return cached;
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		return value != null ? new JobParameters(((JobParameters)value).getParameters()) : null; }

	@Override
	public Serializable disassemble(Object object) throws HibernateException {
		return (Serializable) object;
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		return ObjectUtils.equals(x, y);
	}

	@Override
	public int hashCode(Object x) throws HibernateException {
        assert x != null;
		return x.hashCode();
	}

	@Override
	public boolean isMutable() {
		return true;
	}

	@Override
	public Object nullSafeGet(ResultSet resultSet, String[] names, SessionImplementor sessionImplementor, Object owner) throws HibernateException,
			SQLException {
		String value = (String) type.nullSafeGet(resultSet, names[0], sessionImplementor, owner);
                if(value != null) {
                    try {
                        return stringToJobParametersConverter.convert(value);
                    } catch (IllegalArgumentException iae) {
                        throw new HibernateException("Could not convert'" + value + "' into JobParameters", iae);
                    }
                } else {
                    return null;
                }
	}

	@Override
	public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index, SessionImplementor sessionImplementor) throws HibernateException, SQLException {
                if(value != null) {
                    try {
                        String stringValue = jobParametersToStringConverter.convert(((JobParameters)value));
		        type.nullSafeSet(preparedStatement,  stringValue, index, sessionImplementor);
                    } catch(IllegalArgumentException iae) {
                        throw new HibernateException("Could not convert'" + value + "' into JobParameters", iae);
                    }
                } else {
		    type.nullSafeSet(preparedStatement,  null, index, sessionImplementor);
                }

	}

	@Override
	public Object replace(Object original, Object target, Object owner)
			throws HibernateException {
		return original;
	}

	@Override
	public Class returnedClass() {
		return JobParameters.class;
	}

	@Override
	public int[] sqlTypes() {
		return new int[] { StandardBasicTypes.STRING.sqlType() };
	}
}
