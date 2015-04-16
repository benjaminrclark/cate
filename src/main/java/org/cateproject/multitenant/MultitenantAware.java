package org.cateproject.multitenant;


public interface MultitenantAware {

	String getTenant();

	void setTenant(String tenant);
}
