package org.cateproject.multitenant.event;

import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(name = "remoteTenantContext", defaultRequestChannel = "outgoingTenantEvents")
public interface RemoteMultitenantEventBroadcast extends MultitenantEventAwareService {
	
	public void notify(MultitenantEvent tenantEvent);

}
