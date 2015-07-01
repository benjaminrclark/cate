package org.cateproject.multitenant.jdbc;

import javax.sql.DataSource;

import org.springframework.batch.support.DatabaseType;
import org.springframework.jdbc.support.MetaDataAccessException;

public class DatabaseTypeDetector {
	public String getDatabaseType(DataSource dataSource) {
		try {
			return DatabaseType.fromMetaData(dataSource).toString().toLowerCase();
		} catch (MetaDataAccessException ex) {
			throw new IllegalStateException("Unable to detect database type", ex);
		}
	}
}
