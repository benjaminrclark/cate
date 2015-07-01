package org.cateproject.multitenant.jdbc;

import java.util.Map;

import javax.sql.DataSource;

import liquibase.integration.spring.SpringLiquibase;
import liquibase.servicelocator.ServiceLocator;

import org.cateproject.multitenant.MultitenantConfiguration;
import org.cateproject.multitenant.MultitenantProperties;
import org.cateproject.multitenant.MultitenantRepository;
import org.cateproject.multitenant.jdbc.repository.MultitenantRepositoryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.liquibase.CommonsLoggingLiquibaseLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.handler.ServiceActivatingHandler;
import org.springframework.messaging.MessageHandler;

@Configuration
@ConditionalOnClass(MultitenantConfiguration.class)
@EnableConfigurationProperties(LiquibaseProperties.class)
public class MultitenantJdbcAutoConfiguration {
	private static Logger logger = LoggerFactory.getLogger(MultitenantJdbcAutoConfiguration.class);
	
	@Autowired
	private LiquibaseProperties properties = new LiquibaseProperties();
	
	@Autowired
	private MultitenantProperties multitenantProperties = new MultitenantProperties();
	
	@Bean
	@Primary
	@ConfigurationProperties(prefix = DataSourceProperties.PREFIX)
	public DataSource dataSource() {
		DataSource dataSource = DataSourceBuilder.create().build();
	    return dataSource;
	}
	
	@Bean
	public SpringLiquibase liquibase() {
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setChangeLog(this.properties.getChangeLog());
		liquibase.setContexts(this.properties.getContexts());
		liquibase.setDataSource(dataSource());
		liquibase.setDefaultSchema(this.properties.getDefaultSchema());
		liquibase.setDropFirst(this.properties.isDropFirst());
		liquibase.setShouldRun(this.properties.isEnabled());
		ServiceLocator serviceLocator = ServiceLocator.getInstance();
		serviceLocator.addPackageToScan(CommonsLoggingLiquibaseLogger.class.getPackage().getName());
		return liquibase;
	}
	
	@Bean
	public Map<String, DataSource> tenantDataSourceMap() {
		MultitenantDataSourceMap tenantDataSourceMap = new MultitenantDataSourceMap();
		tenantDataSourceMap.setLiquibase(liquibase());
		tenantDataSourceMap.setLiquibaseProperties(properties);
		tenantDataSourceMap.setTenantRepository(multitenantRepository());
		return tenantDataSourceMap;
	}
	
	@Bean
	@ServiceActivator(inputChannel="localTenantEvents")
	public MessageHandler tenantDataSourceMapHandle() {
		return new ServiceActivatingHandler(tenantDataSourceMap(), "handle");
	}
	
	@Bean
	@ServiceActivator(inputChannel="incomingTenantEvents")
	public MessageHandler tenantDataSourceMapNotifier() {
		return new ServiceActivatingHandler(tenantDataSourceMap(), "notify");
	}
	
	@Bean(name = "multitenantDataSource")
	public DataSource multitenantDataSource() {
		MultitenantRoutingDataSource tenantRoutingDataSource = new MultitenantRoutingDataSource();
		tenantRoutingDataSource.setDefaultTargetDataSource(dataSource());
		tenantRoutingDataSource.setTargetDataSources((Map)tenantDataSourceMap());
		return tenantRoutingDataSource;
	}
	
	@Bean
	@DependsOn("liquibase")
	MultitenantRepository multitenantRepository() {
		MultitenantRepositoryFactory tenantRepositoryFactory = new MultitenantRepositoryFactory();
		tenantRepositoryFactory.setDataSource(dataSource());
		return tenantRepositoryFactory.createRepository();
	}
	

}
