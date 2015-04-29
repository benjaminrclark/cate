package org.cateproject.domain.convert.jpa;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

import org.apache.commons.lang.ObjectUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.hibernate.usertype.UserType;

public class LanguageUserType implements UserType {
	
	private Type type = StandardBasicTypes.STRING;
	
	protected void setType(Type type) {
		this.type = type;
	}

	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return cached;
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		return value != null ? ((Locale)value).clone() : null;
	}

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
		return value != null ? new Locale(value) : null;
	}

	@Override
	public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index, SessionImplementor sessionImplementor) throws HibernateException, SQLException {
		type.nullSafeSet(preparedStatement, 
          value != null ? ((Locale)value).getLanguage() : null, index, sessionImplementor);

	}

	@Override
	public Object replace(Object original, Object target, Object owner)
			throws HibernateException {
		return original;
	}

	@Override
	public Class returnedClass() {
		return Locale.class;
	}

	@Override
	public int[] sqlTypes() {
		return new int[] { StandardBasicTypes.STRING.sqlType() };
	}

}
