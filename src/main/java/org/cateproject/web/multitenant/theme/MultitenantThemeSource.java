package org.cateproject.web.multitenant.theme;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.cateproject.domain.admin.Tenant;
import org.cateproject.multitenant.MultitenantContextHolder;
import org.cateproject.multitenant.MultitenantProperties;
import org.cateproject.multitenant.MultitenantRepository;
import org.cateproject.multitenant.domain.Multitenant;
import org.cateproject.multitenant.event.MultitenantEvent;
import org.cateproject.multitenant.event.MultitenantEventAwareService;
import org.cateproject.repository.jpa.admin.TenantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.context.Theme;
import org.springframework.ui.context.ThemeSource;
import org.springframework.ui.context.support.SimpleTheme;

public class MultitenantThemeSource implements ThemeSource, MultitenantEventAwareService, InitializingBean {
	
	private Logger logger = LoggerFactory.getLogger(MultitenantThemeSource.class);
	
	@Autowired
	private MultitenantRepository multitenantRepository;
	
	@Autowired
	private MultitenantProperties multitenantProperties;
	
	@Autowired
	private TenantRepository tenantRepository;
	
	private Map<String,Theme> themes = new HashMap<String,Theme>();
    
    private String defaultTenantIdentifier = "default";
    
	public void setMultitenantRepository(MultitenantRepository multitenantRepository) {
		this.multitenantRepository = multitenantRepository;
	}

    public void setDefaultTenantIdentifier(String defaultTenantIdentifier) {
		this.defaultTenantIdentifier = defaultTenantIdentifier;
	}
    
    public void setMultitenantProperties(MultitenantProperties multitenantProperties) {
		this.multitenantProperties = multitenantProperties;
	}
    
    public void setTenantRepository(TenantRepository tenantRepository) {
		this.tenantRepository = tenantRepository;	
	}
    
    protected Map<String, Theme> getThemes() {
		return themes;
	}

	protected void setThemes(Map<String, Theme> themes) {
		this.themes = themes;
	}

	public Theme getTheme(String themeName) {
		if(themeName == null || !themes.containsKey(themeName)) {
			return themes.get(defaultTenantIdentifier);
		} else {
		    return themes.get(themeName);
		}
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		initDefaultTheme();
		for(Multitenant m : multitenantRepository.findAll()) {
	        logger.debug("Setting theme for " + m.getIdentifier());
            String tenantId = m.getIdentifier();
		    addTenant(tenantId);	
		}
	}
	
	private void initDefaultTheme() {
		Map<String, String> defaultThemeMap = new HashMap<String, String>(); 
        for(String tenantPropertyName : multitenantProperties.getDefaultTenantPropertyNames()) {
        	defaultThemeMap.put(tenantPropertyName, multitenantProperties.getDefaultTenantProperty(tenantPropertyName));
        }
        MessageSource defaultMessageSource = new MapBackedMessageSource(defaultThemeMap);
        Theme defaultTheme = new SimpleTheme(defaultTenantIdentifier, defaultMessageSource);
        themes.put(defaultTenantIdentifier, defaultTheme);
	}

	public void notify(MultitenantEvent tenantEvent) {
		logger.info("TenantEvent recieved");
		switch(tenantEvent.getType()) {
		case CREATE:
			logger.info("Tenant " + tenantEvent.getIdentifier() + " has been created");
			addTenant(tenantEvent.getIdentifier());
			break;
		case DELETE:
			logger.info("Tenant " + tenantEvent.getIdentifier() + " will be deleted");
			removeTenant(tenantEvent.getIdentifier());
			break;
		default:
			 logger.info("Recieved TenantEvent about " + tenantEvent.getIdentifier());
			 break;
		}
	}

	@Override
	public void handle(MultitenantEvent tenantEvent) {
		logger.debug("Recieved TenantEvent about " + tenantEvent.getIdentifier());
		switch(tenantEvent.getType()) {
		case CREATE:
		    String originalTenantId = MultitenantContextHolder.getContext().getTenantId();
            MultitenantContextHolder.getContext().setTenantId(tenantEvent.getIdentifier());
            Tenant tenant = tenantRepository.findOne(1L);
            for(String tenantPropertyName : multitenantProperties.getDefaultTenantPropertyNames()) {
        	    tenant.getProperties().put(tenantPropertyName, multitenantProperties.getDefaultTenantProperty(tenantPropertyName));
            }
            tenant.getProperties().put("tenant_name", tenant.getTitle());
            tenantRepository.save(tenant);
	        MultitenantContextHolder.getContext().setTenantId(originalTenantId);
	        break;
		case DELETE:
			logger.info("Tenant " + tenantEvent.getIdentifier() + " will be deleted");
			break;
	    default:
	    	logger.info("Recieved TenantEvent about " + tenantEvent.getIdentifier());
	    	break;
		}
	}
		
	private void addTenant(String tenantId) {
		String originalTenantId = MultitenantContextHolder.getContext().getTenantId();
        MultitenantContextHolder.getContext().setTenantId(tenantId);
        Tenant tenant = tenantRepository.findOne(1L);
        MessageSource messageSource = new MapBackedMessageSource(tenant.getProperties());
	    Theme theme = new SimpleTheme(tenant.getIdentifier(), messageSource);
	    themes.put(tenant.getIdentifier(),theme);
	    MultitenantContextHolder.getContext().setTenantId(originalTenantId);
	}
	
	private void removeTenant(String identifier) {
		if(themes.containsKey(identifier)) {
			themes.remove(identifier);
		}
	}

	public Map<String,String> getTenantProperties() throws IOException {
        Tenant tenant = tenantRepository.findOne(1L);
	    return tenant.getProperties();
	}
}
