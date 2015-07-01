package org.cateproject.multitenant.jdbc;

import org.cateproject.multitenant.MultitenantContextHolder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class MultitenantRoutingDataSource extends AbstractRoutingDataSource {
	
	@Override
	protected Object determineCurrentLookupKey() {
		return MultitenantContextHolder.getContext().getTenantId();
	}
}
