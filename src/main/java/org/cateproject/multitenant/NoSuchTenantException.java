package org.cateproject.multitenant;

public class NoSuchTenantException extends RuntimeException {

	public NoSuchTenantException(Object key) {
		super("Tenant " + key + " does not exist");
	}

}
