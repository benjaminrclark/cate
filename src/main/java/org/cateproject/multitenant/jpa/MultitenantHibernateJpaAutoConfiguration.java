package org.cateproject.multitenant.jpa;

import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.cateproject.multitenant.MultitenantConfiguration;
import org.cateproject.multitenant.jdbc.MultitenantDataSourceMap;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@ConditionalOnClass(MultitenantConfiguration.class)
@EnableJpaRepositories(basePackages = "org.cateproject.repository.jpa", transactionManagerRef = "multitenantTransactionManager")
@EnableSpringConfigured
@EntityScan(basePackages = "org.cateproject.domain")
public class MultitenantHibernateJpaAutoConfiguration extends
		HibernateJpaAutoConfiguration {
	
	@Autowired
    MultitenantDataSourceMap tenantDataSourceMap;
	
	@Autowired
	DataSource defaultDataSource;

	@Override
	protected void customizeVendorProperties(Map<String, Object> vendorProperties) {
		super.customizeVendorProperties(vendorProperties);
		vendorProperties.put("org.hibernate.envers.audit_strategy", "org.hibernate.envers.strategy.ValidityAuditStrategy");
		vendorProperties.put("org.hibernate.envers.audit_strategy_validity_store_revend_timestamp", "true");
		vendorProperties.put("org.hibernate.envers.store_data_at_delete", "true");
		vendorProperties.put("org.hibernate.envers.use_revision_entity_with_native_id", "true");
		vendorProperties.put("org.hibernate.envers.track_entities_changed_in_revision", "true");
		vendorProperties.put("hibernate.multiTenancy", "DATABASE");
		vendorProperties.put("hibernate.multi_tenant_connection_provider", multitenantConnectionProvider());
		vendorProperties.put("hibernate.tenant_identifier_resolver", multitenantIdentifierResolver());
		
	}

	
	public CurrentTenantIdentifierResolver multitenantIdentifierResolver() {
		return new MultiTenantIdentifierResolver();
	}

	@Bean
	public org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider multitenantConnectionProvider() {
		MultiTenantConnectionProvider multiTenantConnectionProvider = new MultiTenantConnectionProvider();
		multiTenantConnectionProvider.setDefaultDataSource(this.defaultDataSource);
		multiTenantConnectionProvider.setDataSources(this.tenantDataSourceMap);
		return multiTenantConnectionProvider;
	}
	
	@Primary
	@Bean(name = "multitenantTransactionManager")
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory){
		JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
	}
}
