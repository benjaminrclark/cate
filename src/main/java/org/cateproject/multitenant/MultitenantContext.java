package org.cateproject.multitenant;

public interface MultitenantContext {
	
	String getTenantId();
	
	void setTenantId(String tenantId);
	
	Object getContextProperty(String propertyName);
	
	Object putContextProperty(String propertyName, Object property);
	
    void clearContextProperties();
}
