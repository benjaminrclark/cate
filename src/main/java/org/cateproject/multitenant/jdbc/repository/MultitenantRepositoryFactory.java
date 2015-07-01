package org.cateproject.multitenant.jdbc.repository;

import javax.sql.DataSource;

import org.cateproject.multitenant.MultitenantRepository;
import org.cateproject.multitenant.jdbc.DatabaseTypeDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.incrementer.H2SequenceMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.MySQLMaxValueIncrementer;

public class MultitenantRepositoryFactory {
	
	private static Logger logger = LoggerFactory.getLogger(MultitenantRepositoryFactory.class);
	
	private DataSource dataSource;
	
	private DatabaseTypeDetector databaseTypeDetector = new DatabaseTypeDetector();
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setDatabaseTypeDetector(DatabaseTypeDetector databaseTypeDetector) {
		this.databaseTypeDetector = databaseTypeDetector;
	}

	public MultitenantRepository createRepository() {
		String platform = databaseTypeDetector.getDatabaseType(dataSource);

		MultitenantRepositoryImpl tenantRepository = new MultitenantRepositoryImpl();
		
		if(platform.equals("h2")) {
		    tenantRepository.setTenantIncrementer(new H2SequenceMaxValueIncrementer(dataSource, "tenant_seq"));	
		} else if(platform.equals("mysql")) {
			tenantRepository.setTenantIncrementer(new MySQLMaxValueIncrementer(dataSource, "tenant_seq", "id"));
			
		}
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.setDataSource(dataSource);
		jdbcTemplate.afterPropertiesSet();
		
		tenantRepository.setJdbcTemplate(jdbcTemplate);
		
		return tenantRepository;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public DatabaseTypeDetector getDatabaseTypeDetector() {
		return databaseTypeDetector;
	}
}
