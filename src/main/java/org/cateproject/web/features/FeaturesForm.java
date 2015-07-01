package org.cateproject.web.features;

import java.util.HashMap;
import java.util.Map;

public class FeaturesForm {
	private Map<String, Boolean> features = new HashMap<String,Boolean>();

	public Map<String, Boolean> getFeatures() {
		return features;
	}

	public void setFeatures(Map<String, Boolean> features) {
		this.features = features;
	}
}
