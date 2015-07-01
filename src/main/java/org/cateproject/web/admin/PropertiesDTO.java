package org.cateproject.web.admin;

import java.util.HashMap;
import java.util.Map;

public class PropertiesDTO {
	
	private Map<String,String> properties = new HashMap<String,String>();
	
	public void setProperties(Map<String,String> properties) {
		this.properties = properties;
	}
	
	public Map<String,String> getProperties() {
		return properties;
	}
}
