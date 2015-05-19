package org.cateproject.features;

import javax.sql.DataSource;

import org.ff4j.FF4j;
import org.ff4j.store.JdbcFeatureStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class FeaturesConfiguration {

	@Autowired
	@Qualifier("dataSource")
	private DataSource dataSource;

	/*@Bean
	public FF4j ff4j() {
		FF4j ff4j = new FF4j();
		ff4j.setFeatureStore(featureStore());
		return ff4j;
	}

	@Bean
	@DependsOn("springLiquibase")
	public JdbcFeatureStore featureStore() {
		JdbcFeatureStore jdbcFeatureStore = new JdbcFeatureStore();
		jdbcFeatureStore.setDataSource(dataSource);
		return jdbcFeatureStore;
	}*/

}
