package org.cateproject.multitenant.jdbc.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.cateproject.multitenant.domain.Multitenant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class MultitenantMapper implements RowMapper<Multitenant> {
	private static Logger logger = LoggerFactory.getLogger(MultitenantMapper.class);
    public Multitenant mapRow(ResultSet resultSet,int rowNumber) throws SQLException {
    	logger.debug("TenantMapper " + resultSet + " " + rowNumber + " id " + resultSet.getBigDecimal("id").longValue());
        Multitenant Tenant = new Multitenant();
        Tenant.setId(resultSet.getBigDecimal("id").longValue());
        Tenant.setIdentifier(resultSet.getString("identifier"));
        Tenant.setTitle(resultSet.getString("title"));
        Tenant.setAdminEmail(resultSet.getString("admin_email"));
        Tenant.setOwnerEmail(resultSet.getString("owner_email"));
        Tenant.setDatabaseUsername(resultSet.getString("database_username"));
        Tenant.setDatabasePassword(resultSet.getString("database_password"));
        Tenant.setDatabaseUrl(resultSet.getString("database_url"));
        Tenant.setHostname(resultSet.getString("hostname"));
        Tenant.setDriverClassName(resultSet.getString("driver_class_name"));
        Tenant.setVersion(resultSet.getInt("version"));
        return Tenant;
    }
}
