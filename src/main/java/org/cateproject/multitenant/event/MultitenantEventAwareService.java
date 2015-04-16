package org.cateproject.multitenant.event;

public interface MultitenantEventAwareService {
	
	public void handle(MultitenantEvent tenantEvent);
	
	public void notify(MultitenantEvent tenantEvent);

}
