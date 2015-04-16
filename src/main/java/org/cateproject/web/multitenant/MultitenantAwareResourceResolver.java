package org.cateproject.web.multitenant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.cateproject.multitenant.MultitenantContextHolder;
import org.cateproject.multitenant.MultitenantRepository;
import org.cateproject.multitenant.domain.Multitenant;
import org.cateproject.multitenant.event.MultitenantEvent;
import org.cateproject.multitenant.event.MultitenantEventAwareService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

public class MultitenantAwareResourceResolver implements ResourceResolver, MultitenantEventAwareService, ApplicationContextAware {
	
	private static Logger logger = LoggerFactory.getLogger(MultitenantAwareResourceResolver.class);
	
	private List<String> resourcePatterns = new ArrayList<String>();
	
	private String defaultTenantIdentifier = "default";
	
	private MultitenantRepository tenantRepository;
	
	@Autowired
	public void setMultitenantRepository(MultitenantRepository tenantRepository) {
		this.tenantRepository = tenantRepository;
	}
	
	public void setDefaultTenantIdentifier(String defaultTenantIdentifier) {
		this.defaultTenantIdentifier = defaultTenantIdentifier;
	}

	public void setResourcePatterns(List<String> resourcePatterns) {
		this.resourcePatterns = resourcePatterns;
	}
	
	private Map<String,List<Resource>> resourceMap = new HashMap<String,List<Resource>>();

	private ApplicationContext applicationContext;
	
	public Map<String, List<Resource>> getResourceMap() {
		return resourceMap;
	}
		
	public List<Resource> getResources() {
		init();
		String tenantId = MultitenantContextHolder.getContext().getTenantId();
		if(resourceMap.get(tenantId) == null) {
			return resourceMap.get(defaultTenantIdentifier);
		}
		return resourceMap.get(tenantId);
		
	}
	
	public void init() {
		if(resourceMap.isEmpty()) {
		    logger.debug("init");
		    String currentTenantId = MultitenantContextHolder.getContext().getTenantId(); 
            MultitenantContextHolder.getContext().setTenantId(defaultTenantIdentifier);
        
            for(Multitenant t : tenantRepository.findAll()) {
                logger.debug("Setting resource handler for " + t.getIdentifier());
                createTenantResourceMap(t);
            }

            MultitenantContextHolder.getContext().setTenantId(currentTenantId);
		}
    }

	public void createTenantResourceMap(Multitenant t) {
		List<Resource> resources = new ArrayList<Resource>();
        for(String resourcePattern : resourcePatterns) {
        	resourcePattern = resourcePattern.replace("%{tenant}", t.getIdentifier());
        	Resource r = applicationContext.getResource(resourcePattern);
        	resources.add(r);
        	logger.debug("Adding " + r + " for tenant " + t.getIdentifier());
        }
        logger.debug("Returning " + resources);
        resourceMap.put(t.getIdentifier(), resources);
		
	}
	
	private void removeTenantResourceMap(Multitenant tenant) {
		if(resourceMap.containsKey(tenant.getIdentifier())) {
		  resourceMap.remove(tenant.getIdentifier());
		}
	}
	
	public void notify(MultitenantEvent tenantEvent) {
		logger.info("TenantEvent recieved");
		String originalTenantIdentifier = MultitenantContextHolder.getContext().getTenantId();
		MultitenantContextHolder.getContext().setTenantId(defaultTenantIdentifier);
		switch(tenantEvent.getType()) {
		case CREATE:
			logger.info("Tenant " + tenantEvent.getIdentifier() + " has been created");
			Multitenant tenant = tenantRepository.findByIdentifier(tenantEvent.getIdentifier());
			createTenantResourceMap(tenant);
			break;
		case DELETE:
			logger.info("Tenant " + tenantEvent.getIdentifier() + " will be deleted");
			tenant = tenantRepository.findByIdentifier(tenantEvent.getIdentifier());
			removeTenantResourceMap(tenant);
			break;
		default:
			 logger.info("Recieved TenantEvent about " + tenantEvent.getIdentifier());
			 break;
		}
		MultitenantContextHolder.getContext().setTenantId(originalTenantIdentifier);
	}

	@Override
	public void handle(MultitenantEvent tenantEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Resource resolveResource(HttpServletRequest request,
			String requestPath, List<? extends Resource> locations,
			ResourceResolverChain chain) {

		if (!StringUtils.hasText(requestPath) || requestPath.contains("WEB-INF") || requestPath.contains("META-INF")) {
			logger.debug("Ignoring invalid resource path [{}]",requestPath);
			return null;
		}

		List<Resource> resources = getResources();
		if (resources != null) {
			for (Resource location : resources) {
				try {
					logger.debug("Trying relative path [{}] against base location: {}", requestPath, location);
					Resource resource = location.createRelative(requestPath);
					if (resource.exists() && resource.isReadable()) {
						logger.debug("Found matching resource: {}",resource);
						return resource;
					} else {
						logger.trace("Relative resource '{}' doesn't exist or isn't readable", resource);
					}
				} catch (IOException ex) {
					logger.debug("Failed to create relative resource - trying next resource location", ex);
				}
			}
		}
		logger.debug("Unable to find {}", requestPath);
		return chain.resolveResource(request, requestPath, locations);
	}

	@Override
	public String resolveUrlPath(String resourcePath,
			List<? extends Resource> locations, ResourceResolverChain chain) {
		logger.debug("resolveUrlPath " + resourcePath);
		return chain.resolveUrlPath(resourcePath, locations);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	public void setResourceMap(Map<String, List<Resource>> resourceMap) {
		this.resourceMap = resourceMap;
	}


}
