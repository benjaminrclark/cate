package org.cateproject.multitenant;

import java.util.Map;

public interface MultitenantContext {
	
	String getTenantId();
	
	void setTenantId(String tenantId);
	
	Object getContextProperty(String propertyName);

        boolean getContextBoolean(String propertyName);
	
	Object putContextProperty(String propertyName, Object property);
	
        void clearContextProperties();

        Map<String, Object> copyContextProperties();
}
