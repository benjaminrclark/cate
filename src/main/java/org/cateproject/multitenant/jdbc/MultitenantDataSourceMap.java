package org.cateproject.multitenant.jdbc;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cateproject.multitenant.MultitenantRepository;
import org.cateproject.multitenant.NoSuchTenantException;
import org.cateproject.multitenant.domain.Multitenant;
import org.cateproject.multitenant.event.MultitenantEvent;
import org.cateproject.multitenant.event.MultitenantEventAwareService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class MultitenantDataSourceMap implements Map<String, DataSource>, InitializingBean, MultitenantEventAwareService {
	
    private static final Logger logger = LoggerFactory.getLogger(MultitenantDataSourceMap.class);
    private static final Log log = LogFactory.getLog(MultitenantDataSourceMap.class);

    private MultitenantRepository tenantRepository;
    
    private SpringLiquibase liquibase = new SpringLiquibase();
    
    private DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
	
    private Map<String, DataSource> dataSources = new HashMap<String, DataSource>();

    private LiquibaseProperties liquibaseProperties;
		
    public void setLiquibaseProperties(LiquibaseProperties liquibaseProperties) {
	this.liquibaseProperties = liquibaseProperties;	
    }
    
    public void setDataSourceBuilder(DataSourceBuilder dataSourceBuilder) {
    	this.dataSourceBuilder = dataSourceBuilder;
    }
    
    public void setLiquibase(SpringLiquibase liquibase) {
    	this.liquibase = liquibase;
    }
	
	public void setTenantRepository(MultitenantRepository tenantRepository) {
		this.tenantRepository = tenantRepository;
	}
	
	public void afterPropertiesSet() {
		liquibase.setChangeLog(this.liquibaseProperties.getChangeLog());
		liquibase.setContexts(this.liquibaseProperties.getContexts());
		liquibase.setDefaultSchema(this.liquibaseProperties.getDefaultSchema());
		liquibase.setDropFirst(this.liquibaseProperties.isDropFirst());
		liquibase.setShouldRun(this.liquibaseProperties.isEnabled());
		createDatasources(tenantRepository.findAll(new PageRequest(0, 10)));
	}
	
	private void createDatasources(Page<Multitenant> page) {
		for(Multitenant tenant : page) {
			createDatasource(tenant);
	    }
		if(page.hasNext()) {
			createDatasources(tenantRepository.findAll(page.nextPageable()));
		}
	}
	
	private void createDatasource(Multitenant tenant) {
		dataSourceBuilder.driverClassName(tenant.getDriverClassName());
		dataSourceBuilder.password(tenant.getDatabasePassword());
		dataSourceBuilder.url(tenant.getDatabaseUrl());
		dataSourceBuilder.username(tenant.getDatabaseUsername());
		DataSource dataSource = dataSourceBuilder.build();
                dataSources.put(tenant.getIdentifier(), dataSource);
                logger.info("Datasource for " + tenant.getIdentifier() + " created successfully ");
       
	    try {
			liquibase.setDataSource(dataSource);
			liquibase.afterPropertiesSet();
		} catch (LiquibaseException e) {
			logger.error("Unable to successfully upgrade tenant " + tenant.getIdentifier() + " nested exception is:  " + e.getMessage());
                        for(StackTraceElement ste : e.getStackTrace()) {
                           logger.error(ste.toString());
                        }
		}
	}
	
	private void removeDatasource(Multitenant tenant) {
		if(dataSources.containsKey(tenant.getIdentifier())) {
		    DataSource dataSource = dataSources.remove(tenant.getIdentifier());
		    if(dataSource instanceof org.apache.tomcat.jdbc.pool.DataSource) {
				logger.info("calling close()");
		      ((org.apache.tomcat.jdbc.pool.DataSource)dataSource).close();
			}
		}		
	}
	
	public void notify(MultitenantEvent tenantEvent) {
		logger.info("TenantEvent recieved");
		switch(tenantEvent.getType()) {
		case CREATE:
			Multitenant tenant = tenantRepository.findByIdentifier(tenantEvent.getIdentifier());
			logger.info("Tenant " + tenantEvent.getIdentifier() + " has been created");
			createDatasource(tenant);
			break;
		case DELETE:	
			logger.info("Tenant " + tenantEvent.getIdentifier() + " will be deleted");
			tenant = new Multitenant();
			tenant.setIdentifier(tenantEvent.getIdentifier());
			removeDatasource(tenant);
			break;
		default:
			 logger.info("Recieved TenantEvent about " + tenantEvent.getIdentifier());
			 break;
		}
	}

	public void close() {
		for(Object tenantId : dataSources.keySet()) {
			logger.info("closing " + tenantId);
			Object dataSource = dataSources.get(tenantId);
			if(dataSource instanceof org.apache.tomcat.jdbc.pool.DataSource) {
				logger.info("calling close()");
		      ((org.apache.tomcat.jdbc.pool.DataSource)dataSource).close();
			}			
		}
	}
	
	public void clear() {
		close();
		dataSources.clear();
	}
	
	public boolean containsKey(Object key) {
		return dataSources.containsKey(key);
	}
	
	public boolean containsValue(Object value) {
		return dataSources.containsValue(value);
	}
	
	public boolean equals(Object o) {
		return dataSources.equals(o);
	}
	
	public DataSource get(Object key) {
        if(!dataSources.containsKey(key)) {
            throw new NoSuchTenantException(key);
        }
		return dataSources.get(key);
	}
	
	public Set<Map.Entry<String, DataSource>> entrySet() {
		return dataSources.entrySet();
	}
	
	public int hashCode() {
		return dataSources.hashCode();
	}
	
	public boolean isEmpty() {
		return dataSources.isEmpty();
	}
	
	public Set<String> keySet() {
		return dataSources.keySet();
	}
	
	public DataSource put(String key, DataSource value) {
		return dataSources.put(key, value);
	}
	
	public void putAll(Map<? extends String, ? extends DataSource> m) {
		dataSources.putAll(m);
	}
	
	public DataSource remove(Object key) {
		return dataSources.remove(key);
	}
	
	public int size() {
		return dataSources.size();
	}
	
	public Collection<DataSource> values() {
		return dataSources.values();
	}

	public Map<String, DataSource> getDataSources() {
		return dataSources;
	}

	public void setDataSources(Map<String, DataSource> dataSources) {
		this.dataSources = dataSources;
	}

	@Override
	public void handle(MultitenantEvent tenantEvent) {
		notify(tenantEvent);
	}
}
