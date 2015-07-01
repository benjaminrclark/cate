package org.cateproject.multitenant;

import org.cateproject.multitenant.event.MultitenantEvent;
import org.cateproject.multitenant.event.MultitenantEventAwareService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultitenantStatus implements MultitenantEventAwareService {
	
	private static Logger logger = LoggerFactory.getLogger(MultitenantStatus.class);
	
	private boolean initialized;

	public boolean isInitialized() {
		return initialized;
	}
	
	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	@Override
	public void handle(MultitenantEvent tenantEvent) {
		notify(tenantEvent);
	}

	@Override
	public void notify(MultitenantEvent tenantEvent) {
		logger.info("TenantEvent recieved");
		switch(tenantEvent.getType()) {
		case CREATE:
			initialized = true;
			break;
		default:
			 logger.info("Recieved TenantEvent about " + tenantEvent.getIdentifier());
			 break;
		}
	}

}
