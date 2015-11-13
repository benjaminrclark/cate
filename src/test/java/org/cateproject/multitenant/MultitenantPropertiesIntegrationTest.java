package org.cateproject.multitenant;

import static org.junit.Assert.*;

import org.cateproject.Application;
import org.cateproject.batch.TestingBatchTaskExecutorConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class, TestingBatchTaskExecutorConfiguration.class})
@ActiveProfiles({"default", "integration-test"})
@IntegrationTest
public class MultitenantPropertiesIntegrationTest {
	
	@Autowired
	private MultitenantProperties multitenantProperties;
	
	@Value("${spring.datasource.username}")
	private String databaseUsername;
	
	@Value("${spring.datasource.password}")
	private String databasePassword;
	
	@Value("${spring.datasource.url}")
	private String databaseUrl;
	
	@Value("${spring.datasource.driver-class-name}")
	private String databaseDriverClassname;
	
	@Value("${tenant.default.admin.username}")
	private String defaultAdminUsername;
	
	@Value("${tenant.default.owner.username}")
	private String defaultOwnerUsername;
	
	@Value("${tenant.default.identifier}")
	private String defaultIdentifier;
	
	@Value("${tenant.default.title}")
	private String defaultTitle;

	@Test
	public void testGetDatabaseUsername() {
		assertEquals("databaseUsername should equal ${spring.datasource.username}",databaseUsername, multitenantProperties.getDatabaseUsername());
	}

	@Test
	public void testGetDatabasePassword() {
		assertEquals("databasePassword should equal ${spring.datasource.password}",databasePassword, multitenantProperties.getDatabasePassword());
	}

	@Test
	public void testGetDatabaseUrl() {
		assertEquals("databaseUrl should equal ${spring.datasource.url}",databaseUrl, multitenantProperties.getDatabaseUrl());
	}

	@Test
	public void testGetDefaultAdminUsername() {
		assertEquals("defaultAdminUsername should equal ${tenant.default.admin.username}",defaultAdminUsername, multitenantProperties.getDefaultAdminUsername());
	}

	@Test
	public void testGetDefaultOwnerUsername() {
		assertEquals("defaultOwnerUsername should equal ${tenant.default.owner.username}",defaultOwnerUsername, multitenantProperties.getDefaultOwnerUsername());
	}

	@Test
	public void testGetDefaultIdentifier() {
		assertEquals("defaultIdentifier should equal ${tenant.default.identifier}",defaultIdentifier, multitenantProperties.getDefaultIdentifier());
	}

	@Test
	public void testGetDefaultTitle() {
		assertEquals("defaultTitle should equal ${tenant.default.title}",defaultTitle, multitenantProperties.getDefaultTitle());
	}

	@Test
	public void testGetDatabaseDriverClassname() {
		assertEquals("databaseDriverClassname should equal ${spring.datasource.driver-class-name}",databaseDriverClassname, multitenantProperties.getDatabaseDriverClassname());
	}

}
