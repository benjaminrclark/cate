package org.cateproject.multitenant;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class MultitenantProperties implements InitializingBean {
	
	private static Logger logger = LoggerFactory.getLogger(MultitenantProperties.class);
	
	private Properties multitenantProperties = new Properties();

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

	public String getDatabaseUsername() {
		return databaseUsername;
	}

	public String getDatabasePassword() {
		return databasePassword;
	}

	public String getDatabaseUrl() {
		return databaseUrl;
	}

	public String getDefaultAdminUsername() {
		return defaultAdminUsername;
	}

	public String getDefaultOwnerUsername() {
		return defaultOwnerUsername;
	}

	public String getDefaultIdentifier() {
		return defaultIdentifier;
	}

	public String getDefaultTitle() {
		return defaultTitle;
	}

	public String getDatabaseDriverClassname() {
		return databaseDriverClassname;
	}
	
	public void setDatabaseUsername(String databaseUsername) {
		this.databaseUsername = databaseUsername;
	}

	public void setDatabasePassword(String databasePassword) {
		this.databasePassword = databasePassword;
	}

	public void setDatabaseUrl(String databaseUrl) {
		this.databaseUrl = databaseUrl;
	}

	public void setDatabaseDriverClassname(String databaseDriverClassname) {
		this.databaseDriverClassname = databaseDriverClassname;
	}

	public void setDefaultAdminUsername(String defaultAdminUsername) {
		this.defaultAdminUsername = defaultAdminUsername;
	}

	public void setDefaultOwnerUsername(String defaultOwnerUsername) {
		this.defaultOwnerUsername = defaultOwnerUsername;
	}

	public void setDefaultIdentifier(String defaultIdentifier) {
		this.defaultIdentifier = defaultIdentifier;
	}

	public void setDefaultTitle(String defaultTitle) {
		this.defaultTitle = defaultTitle;
	}
	
	public String getDefaultTenantProperty(String propertyName) {
		return multitenantProperties.getProperty("tenant.property.".concat(propertyName));
	}
	
	public void setMultitenantProperties(Properties multitenantProperties) {
		this.multitenantProperties = multitenantProperties;
	}
	
	public Set<String> getDefaultTenantPropertyNames() {
		Set<String> defaultTenantProperyNames = new HashSet<String>();
		Enumeration<?> propertyNames = multitenantProperties.propertyNames();
		while(propertyNames.hasMoreElements()) {
			Object o = propertyNames.nextElement();
			if(o.toString().startsWith("tenant.property.")) {
				defaultTenantProperyNames.add(o.toString().substring("tenant.property.".length()));
			}
		}
		return defaultTenantProperyNames;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		ClassPathResource applicationPropertiesResource = new ClassPathResource("config/tenant.properties");
		this.multitenantProperties.load(applicationPropertiesResource.getInputStream());
		
	}
}
