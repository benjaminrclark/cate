package org.cateproject.web.multitenant;

import java.util.ArrayList;
import java.util.List;

import org.cateproject.multitenant.MultitenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.handler.ServiceActivatingHandler;
import org.springframework.messaging.MessageHandler;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolver;

@Configuration
public class MultitenantWebConfiguration extends WebMvcConfigurerAdapter {
	
	@Autowired
	MultitenantRepository multitenantRepository;
	
	@Value("tenant.default.identifier")
	private String defaultTenantIdentifier;
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/images/**").addResourceLocations("classpath:/static/images/").resourceChain(false).addResolver(imagesResourceResolver()).addResolver(new PathResourceResolver());
		registry.addResourceHandler("/static/styles/**").addResourceLocations("classpath:/static/styles/").resourceChain(false).addResolver(stylesResourceResolver()).addResolver(new PathResourceResolver());
		if (!registry.hasMappingForPattern("/webjars/**")) {
	        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	    }
	}

	@Bean
	public ResourceResolver imagesResourceResolver() {
		MultitenantAwareResourceResolver imagesResourceResolver = new MultitenantAwareResourceResolver();
		imagesResourceResolver.setMultitenantRepository(multitenantRepository);
		imagesResourceResolver.setDefaultTenantIdentifier(defaultTenantIdentifier);
		List<String> resourcePatterns = new ArrayList<String>();
		resourcePatterns.add("file:/var/www/html/cate/%{tenant}/images/");
		imagesResourceResolver.setResourcePatterns(resourcePatterns);
		return imagesResourceResolver;
	}
	
	@Bean
	@ServiceActivator(inputChannel="localTenantEvents")
	public MessageHandler imagesResourceResolverHandle() {
		return new ServiceActivatingHandler(imagesResourceResolver(), "handle");
	}
	
	@Bean
	@ServiceActivator(inputChannel="incomingTenantEvents")
	public MessageHandler imagesResourceResolverNotifier() {
		return new ServiceActivatingHandler(imagesResourceResolver(), "notify");
	}
	
	@Bean
	public ResourceResolver stylesResourceResolver() {
		MultitenantAwareResourceResolver stylesResourceResolver = new MultitenantAwareResourceResolver();
		stylesResourceResolver.setMultitenantRepository(multitenantRepository);
		stylesResourceResolver.setDefaultTenantIdentifier(defaultTenantIdentifier);
		List<String> resourcePatterns = new ArrayList<String>();
		resourcePatterns.add("file:/var/www/html/cate/%{tenant}/styles/");
		stylesResourceResolver.setResourcePatterns(resourcePatterns);
		return stylesResourceResolver;
	}
	
	@Bean
	@ServiceActivator(inputChannel="localTenantEvents")
	public MessageHandler stylesResourceResolverHandle() {
		return new ServiceActivatingHandler(stylesResourceResolver(), "handle");
	}
	
	@Bean
	@ServiceActivator(inputChannel="incomingTenantEvents")
	public MessageHandler stylesResourceResolverNotifier() {
		return new ServiceActivatingHandler(stylesResourceResolver(), "notify");
	}

}
