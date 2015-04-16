package org.cateproject.multitenant;

import javax.sql.DataSource;

import org.cateproject.multitenant.jdbc.repository.MultitenantRepositoryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.handler.ServiceActivatingHandler;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.messaging.MessageHandler;

@Configuration
public class MultitenantConfiguration {
	
	@Autowired
	@Qualifier("dataSource")
	private DataSource dataSource;
	
	@Autowired
	private MultitenantRepository multitenantRepository;
	
	@Bean(name = "dataSourceTransactionManager")
	public DataSourceTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource);
	}
	
	@Bean
	public MultitenantStatus tenantStatus() {
		MultitenantStatus tenantStatus = new MultitenantStatus();
		if(multitenantRepository.count() > 0) {
		  tenantStatus.setInitialized(true);
		}
		return tenantStatus;
	}
	
	@Bean
	@ServiceActivator(inputChannel="localTenantEvents")
	public MessageHandler tenantStatusHandler() {
		return new ServiceActivatingHandler(tenantStatus(), "handle");
	}
	
	@Bean
	@ServiceActivator(inputChannel="incomingTenantEvents")
	public MessageHandler tenantStatusNotifier() {
		return new ServiceActivatingHandler(tenantStatus(), "notify");
	}
	
	
	

}
