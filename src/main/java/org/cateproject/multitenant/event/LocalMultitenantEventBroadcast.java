package org.cateproject.multitenant.event;

import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(name = "localTenantContext", defaultRequestChannel = "localTenantEvents")
public interface LocalMultitenantEventBroadcast extends MultitenantEventAwareService {
	
	public void handle(MultitenantEvent tenantEvent);

}
