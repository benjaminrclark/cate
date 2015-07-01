package org.cateproject.web.multitenant.theme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.handler.ServiceActivatingHandler;
import org.springframework.messaging.MessageHandler;
import org.springframework.ui.context.ThemeSource;
import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MultitenantThemeWebConfiguration extends WebMvcConfigurerAdapter {
	
	@Bean
	public ThemeResolver themeResolver() {
		return new MultitenantThemeResolver();
	}
	
	@Bean
	ThemeSource themeSource() {
		return new MultitenantThemeSource();	
	}
	
	@Bean
	@ServiceActivator(inputChannel="localTenantEvents")
	public MessageHandler themeSourceHandle() {
		return new ServiceActivatingHandler(themeSource(), "handle");
	}
	
	@Bean
	@ServiceActivator(inputChannel="incomingTenantEvents")
	public MessageHandler themeSourceNotifier() {
		return new ServiceActivatingHandler(themeSource(), "notify");
	}
}
