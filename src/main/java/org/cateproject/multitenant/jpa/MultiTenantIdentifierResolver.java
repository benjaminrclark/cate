package org.cateproject.multitenant.jpa;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cateproject.multitenant.MultitenantContextHolder;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

public class MultiTenantIdentifierResolver implements CurrentTenantIdentifierResolver {
	
	protected static final Log logger = LogFactory.getLog(MultiTenantIdentifierResolver.class);
	
	private String defaultTenantIdentifier = "localhost";
	
	public void setDefaultTenantIdentifier(String defaultTenantIdentifier) {
		this.defaultTenantIdentifier = defaultTenantIdentifier;
	}

	@Override
	public String resolveCurrentTenantIdentifier() {
		String tenantIdentifier = MultitenantContextHolder.getContext().getTenantId();
		if(tenantIdentifier == null) {
			return defaultTenantIdentifier;
		} else {
		    return tenantIdentifier;
		}
	}
	
	@Override
	public boolean 	validateExistingCurrentSessions() {
		return true;
	}

	
}
