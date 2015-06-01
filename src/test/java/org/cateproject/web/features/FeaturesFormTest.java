package org.cateproject.web.features;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class FeaturesFormTest {
	
	private FeaturesForm featuresForm;
	
	private Map<String,Boolean> features;

	@Before
	public void setUp() throws Exception {
		features = new HashMap<String,Boolean>();
		features.put("FEATURE", true);
		featuresForm = new FeaturesForm();
		featuresForm.setFeatures(features);
	}

	@Test
	public void testGetFeatures() {
		assertEquals("getFeatures should return the expected features", features, featuresForm.getFeatures());
	}

}
