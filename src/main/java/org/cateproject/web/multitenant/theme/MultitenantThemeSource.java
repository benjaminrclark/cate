package org.cateproject.web.multitenant.theme;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.ui.context.Theme;
import org.springframework.ui.context.ThemeSource;
import org.springframework.ui.context.support.SimpleTheme;

public class MultitenantThemeSource implements ThemeSource, MultitenantEventAwareService {
	
	private Logger logger = LoggerFactory.getLogger(MultitenantThemeSource.class);
	
	@Autowired
	private MultitenantRepository multitenantRepository;
	
	@Autowired
	private MultitenantProperties multitenantProperties;
	
	@Autowired
	private TenantRepository tenantRepository;
	
	private Map<String,Theme> themes = new HashMap<String,Theme>();

    private Map<String, MapBackedMessageSource> messageSources = new HashMap<String, MapBackedMessageSource>();
    
    private String defaultTenantIdentifier = "default";

    public void setDefaultTenantIdentifier(String defaultTenantIdentifier) {
		this.defaultTenantIdentifier = defaultTenantIdentifier;
	}
    
    public Theme getTheme(String themeName) {
    	init();
		if(themeName == null || !themes.containsKey(themeName)) {
			return themes.get(defaultTenantIdentifier);
		} else {
		    return themes.get(themeName);
		}
	}
	
	private void init() {
		if(messageSources.isEmpty()) {
            logger.debug("init");
            initDefaultTheme();
            String originalTenantId = MultitenantContextHolder.getContext().getTenantId();
            MultitenantContextHolder.getContext().setTenantId(defaultTenantIdentifier);            

	        for(Multitenant m : multitenantRepository.findAll()) {
	    	    logger.debug("Setting theme for " + m.getIdentifier());
                String tenantId = m.getIdentifier();
		        addTenant(tenantId);	
	        }
 
            MultitenantContextHolder.getContext().setTenantId(originalTenantId);
		}
	}
	
	private void initDefaultTheme() {
		Map<String, String> defaultThemeMap = new HashMap<String, String>(); 
        for(String tenantPropertyName : multitenantProperties.getDefaultTenantPropertyNames()) {
        	defaultThemeMap.put(tenantPropertyName, multitenantProperties.getDefaultTenantProperty(tenantPropertyName));
        }
        MapBackedMessageSource defaultMessageSource = new MapBackedMessageSource(defaultThemeMap);
        messageSources.put("default", defaultMessageSource);
        Theme defaultTheme = new SimpleTheme("default", defaultMessageSource);
        themes.put("default", defaultTheme);
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
		String originalTenantId = MultitenantContextHolder.getContext().getTenantId();
        MultitenantContextHolder.getContext().setTenantId(tenantEvent.getIdentifier());
        Tenant tenant = tenantRepository.findOne(1L);
        for(String tenantPropertyName : multitenantProperties.getDefaultTenantPropertyNames()) {
        	tenant.getProperties().put(tenantPropertyName, multitenantProperties.getDefaultTenantProperty(tenantPropertyName));
        }
        tenant.getProperties().put("tenant_name", tenant.getTitle());
        tenantRepository.save(tenant);
	    MultitenantContextHolder.getContext().setTenantId(originalTenantId);
	}
		
	private void addTenant(String tenantId) {
		String originalTenantId = MultitenantContextHolder.getContext().getTenantId();
        MultitenantContextHolder.getContext().setTenantId(tenantId);
        Tenant tenant = tenantRepository.findOne(1L);
        MapBackedMessageSource messageSource = new MapBackedMessageSource(tenant.getProperties());
        messageSources.put(tenantId,messageSource);
	    Theme theme = new SimpleTheme(tenantId, messageSource);
	    themes.put(tenantId,theme);
	    MultitenantContextHolder.getContext().setTenantId(originalTenantId);
	}
	
	private void removeTenant(String identifier) {
		if(messageSources.containsKey(identifier)) {
			messageSources.remove(identifier);
		}
		if(themes.containsKey(identifier)) {
			themes.remove(identifier);
		}
	}

	public Map<String,String> getTenantProperties() throws IOException {
        Tenant tenant = tenantRepository.findOne(1L);
	    return tenant.getProperties();
	}
	
	public void setTenantProperties(Map<String,String> propertyMap) throws IOException {
		Tenant tenant = tenantRepository.findOne(1L);
        tenant.setProperties(propertyMap);
        tenantRepository.save(tenant);
        String tenantId = MultitenantContextHolder.getContext().getTenantId();
        MapBackedMessageSource messageSource = null;
        if(!messageSources.containsKey(tenantId)) {
            messageSource = messageSources.get(defaultTenantIdentifier);
        } else {
 		    messageSource = messageSources.get(tenantId);
        }
        messageSource.setMessages(propertyMap);
	}

    class MapBackedMessageSource extends AbstractMessageSource {
    	private Logger logger = LoggerFactory.getLogger(MapBackedMessageSource.class);
    	
        Map<String,String> messages = new HashMap<String,String>();
            
        public MapBackedMessageSource(Map<String,String> messages) {
            this.messages = new HashMap<String,String>(messages);
        }

        public void setMessages(Map<String,String> messages) {
            this.messages = new HashMap<String,String>(messages);
        }

        protected MessageFormat resolveCode(String code, Locale locale) {
        	
            MessageFormat messageFormat = null;
            if(messages.containsKey(code)) {
                messageFormat = new MessageFormat(messages.get(code),locale);
            }
            return messageFormat;
        }
    }
}
