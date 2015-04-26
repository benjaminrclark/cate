package org.cateproject.multitenant.jdbc.repository;

import java.sql.Types;
import java.util.List;

import org.cateproject.multitenant.MultitenantRepository;
import org.cateproject.multitenant.domain.Multitenant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;
import org.springframework.transaction.annotation.Transactional;


public class MultitenantRepositoryImpl implements MultitenantRepository {
	
	private static Logger logger = LoggerFactory.getLogger(MultitenantRepositoryImpl.class);
	
	private JdbcTemplate jdbcTemplate;
	
	private DataFieldMaxValueIncrementer tenantIncrementer;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setTenantIncrementer(DataFieldMaxValueIncrementer tenantIncrementer) {
		this.tenantIncrementer = tenantIncrementer;
	}

	@Override
	@Transactional("dataSourceTransactionManager")
	public Page<Multitenant> findAll(Pageable pageable) {
		List<Multitenant> tenants = jdbcTemplate.query("select * from multitenant limit ? offset ?", new Object[] {pageable.getPageSize(), pageable.getOffset()}, new MultitenantMapper());
		Long numberOfTenants = count();
		return new PageImpl<Multitenant>(tenants, pageable, numberOfTenants);
	}

	@Override
	@Transactional("dataSourceTransactionManager")
	public Multitenant findByIdentifier(String identifier) {
		List<Multitenant> tenants = jdbcTemplate.query("select * from multitenant where identifier = ?", new Object[] { identifier }, new MultitenantMapper());
		if(tenants.size() == 1) {
			return tenants.get(0);
		} else if(tenants.size() == 0) {
			return null;
		} else {
			throw new IncorrectResultSizeDataAccessException(1, tenants.size());
		}
	}

	@Override
	@Transactional("dataSourceTransactionManager")
	public Multitenant findOne(Long id) {
		List<Multitenant> tenants = jdbcTemplate.query("select * from multitenant where id =  ?", new Object []{ id }, new MultitenantMapper());
		if(tenants.size() == 1) {
			return tenants.get(0);
		} else if(tenants.size() == 0) {
			return null;
		} else {
			throw new IncorrectResultSizeDataAccessException(1, tenants.size());
		}
	}

	@Override
	@Transactional("dataSourceTransactionManager")
	public Multitenant save(Multitenant tenant) {
		if(tenant.getId() == null) {
		  long id = tenantIncrementer.nextLongValue();
		  ((Multitenant)tenant).setId(id);
		  jdbcTemplate.update("insert into multitenant(id, identifier, database_username, database_password, database_url, driver_class_name, hostname, title, admin_email, owner_email, version) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", 
                  new SqlParameterValue(Types.BIGINT, id),
                  new SqlParameterValue(Types.VARCHAR, tenant.getIdentifier()),
                  new SqlParameterValue(Types.VARCHAR, tenant.getDatabaseUsername()),
                  new SqlParameterValue(Types.VARCHAR, tenant.getDatabasePassword()),
                  new SqlParameterValue(Types.VARCHAR, tenant.getDatabaseUrl()),
                  new SqlParameterValue(Types.VARCHAR, tenant.getDriverClassName()),
                  new SqlParameterValue(Types.VARCHAR, tenant.getHostname()),
                  new SqlParameterValue(Types.VARCHAR, tenant.getTitle()),
                  new SqlParameterValue(Types.VARCHAR, tenant.getAdminEmail()),
                  new SqlParameterValue(Types.VARCHAR, tenant.getOwnerEmail()),
                  new SqlParameterValue(Types.INTEGER, 1));
		} else {
			int version = tenant.getVersion();
			 tenant.setVersion(version + 1);
			jdbcTemplate.update("update multitenant set identifier=?, database_username=?, database_password=?, database_url=?, driver_class_name=?, hostname=?, title=?, admin_email=?, owner_email=?, version=? where id = ?", 
	                  new SqlParameterValue(Types.VARCHAR, tenant.getIdentifier()),
	                  new SqlParameterValue(Types.VARCHAR, tenant.getDatabaseUsername()),
	                  new SqlParameterValue(Types.VARCHAR, tenant.getDatabasePassword()),
	                  new SqlParameterValue(Types.VARCHAR, tenant.getDatabaseUrl()),
	                  new SqlParameterValue(Types.VARCHAR, tenant.getDriverClassName()),
	                  new SqlParameterValue(Types.VARCHAR, tenant.getHostname()),
	                  new SqlParameterValue(Types.VARCHAR, tenant.getTitle()),
	                  new SqlParameterValue(Types.VARCHAR, tenant.getAdminEmail()),
	                  new SqlParameterValue(Types.VARCHAR, tenant.getOwnerEmail()),
	                  new SqlParameterValue(Types.INTEGER, tenant.getVersion()),
	                  new SqlParameterValue(Types.BIGINT, tenant.getId())
			);
		}
		
		return tenant;
	}

	@Override
	public void flush() {
		
	}

	@Override
	public long count() {
		return jdbcTemplate.queryForObject("select count(id) from multitenant", Long.class);
	}

	@Override
	public List<Multitenant> findAll() {
		return jdbcTemplate.query("select * from multitenant", new MultitenantMapper());
	}

	@Override
	public void delete(Multitenant tenant) {
		jdbcTemplate.update("delete from multitenant where id = ?", new Object[] { tenant.getId() });
	}
}
