package org.cateproject.web.multitenant.theme;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cateproject.multitenant.MultitenantContextHolder;
import org.cateproject.multitenant.MultitenantStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ThemeResolver;

public class MultitenantThemeResolver implements ThemeResolver {
	
	private static Logger logger = LoggerFactory.getLogger(MultitenantThemeResolver.class);
	
	@Autowired
	private MultitenantStatus multitenantStatus;
	
	private String defaultTenantIdentifier = "default";
	
	public void setDefaultTenantIdentifier(String defaultTenantIdentifier) {
		this.defaultTenantIdentifier = defaultTenantIdentifier;
	}

	public String resolveThemeName(HttpServletRequest request) {
		
		if(multitenantStatus.isInitialized()) {
		    if(MultitenantContextHolder.getContext().getTenantId() == null) {
			    return defaultTenantIdentifier;
		    } else {
		        return MultitenantContextHolder.getContext().getTenantId();
		    }
		} else {
			return defaultTenantIdentifier;
		}
	}
	
	public void setThemeName(HttpServletRequest request, HttpServletResponse response, String themeName) {
		throw new UnsupportedOperationException("setThemeName is not supported");
	}
}
