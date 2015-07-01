package org.cateproject.multitenant;

import java.util.HashMap;
import java.util.Map;

public class MultitenantContextImpl implements MultitenantContext {

	private String tenantId;
	
	private Map<String, Object> contextProperties = new HashMap<String, Object>();

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();

		if (this.tenantId == null) {
			stringBuffer.append("TenantContext<NULL>");
		} else {
			stringBuffer.append("TenantContext<");
			stringBuffer.append(this.tenantId);
			stringBuffer.append(">");
		}

		return stringBuffer.toString();
	}

	public int hashCode() {
		if (this.tenantId == null) {
			return -1;
		} else {
			return this.tenantId.hashCode();
		}
	}

	public boolean equals(Object obj) {
		if (obj instanceof MultitenantContextImpl) {
			MultitenantContextImpl test = (MultitenantContextImpl) obj;

			if ((this.getTenantId() == null) && (test.getTenantId() == null)) {
				return true;
			}

			if ((this.getTenantId() != null) && (test.getTenantId() != null)
					&& this.getTenantId().equals(test.getTenantId())) {
				return true;
			}
		}

		return false;
	}

	@Override
	public Object getContextProperty(String propertyName) {
		return contextProperties.get(propertyName);
	}

	@Override
	public Object putContextProperty(String propertyName, Object property) {
		contextProperties.put(propertyName, property);
		return property;
	}

	@Override
	public void clearContextProperties() {
		contextProperties.clear();
	}
}
