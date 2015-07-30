package org.cateproject.features;

import javax.sql.DataSource;

import org.ff4j.FF4j;
import org.ff4j.core.FeatureStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class FeaturesConfiguration {

	@Autowired
	@Qualifier("multitenantDataSource")
	private DataSource dataSource;

	@Bean
	public FF4j ff4j() {
		FF4j ff4j = new FF4j();
		ff4j.setFeatureStore(featureStore());
		return ff4j;
	}

	@Bean
	@DependsOn("liquibase")
	public FeatureStore featureStore() {
		FeatureStoreSpringJDBC featureStore = new FeatureStoreSpringJDBC();
		featureStore.setDataSource(dataSource);
		return featureStore;
	}

}
