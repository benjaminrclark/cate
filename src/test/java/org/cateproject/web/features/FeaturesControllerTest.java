package org.cateproject.web.features;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.ff4j.FF4j;
import org.ff4j.core.Feature;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

public class FeaturesControllerTest {
	
	private FeaturesController featuresController;
	
	private FF4j ff4j;

	@Before
	public void setUp() throws Exception {
		featuresController = new FeaturesController();
		ff4j = EasyMock.createMock(FF4j.class);
		featuresController.setFF4j(ff4j);
	}

	@Test
	public void testList() {
		Map<String, Feature> features =	new HashMap<String,Feature>();
		features.put("feature", new Feature("feature", false));
		EasyMock.expect(ff4j.getFeatures()).andReturn(features);
		EasyMock.expect(ff4j.check(EasyMock.eq("feature"))).andReturn(false);
		Model uiModel = new ExtendedModelMap();		
		EasyMock.replay(ff4j);
		assertEquals("list should return 'system/features/list'","system/features/list", featuresController.list(uiModel));
		assertFalse("uiModel should contain a featuresForm with the features in it", ((FeaturesForm)uiModel.asMap().get("result")).getFeatures().get("feature"));
		EasyMock.verify(ff4j);
	}

	@Test
	public void testUpdateBindingError() {
		Map<String, Boolean> features =	new HashMap<String,Boolean>();
		features.put("feature", false);
		
		FeaturesForm result = new FeaturesForm();
		result.setFeatures(features);
		
		BindingResult bindingResult = EasyMock.createMock(BindingResult.class);
		
		Model uiModel = new ExtendedModelMap();
		
		RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
		
		EasyMock.expect(bindingResult.hasErrors()).andReturn(true);
		
		EasyMock.replay(ff4j, bindingResult);
		
		assertEquals("update should return 'system/features/list' when there are binding errors","system/features/list", featuresController.update(result, bindingResult, uiModel, redirectAttributes));
		
		EasyMock.verify(ff4j, bindingResult);
	}
	
	@Test
	public void testUpdateSuccess() {
		Map<String, Boolean> features =	new HashMap<String,Boolean>();
		features.put("feature1", null);
		features.put("feature2", null);
		features.put("feature3", true);
		features.put("feature4", true);
		features.put("feature5", false);
		
		FeaturesForm result = new FeaturesForm();
		result.setFeatures(features);
		
		BindingResult bindingResult = EasyMock.createMock(BindingResult.class);
		
		Model uiModel = new ExtendedModelMap();
		
		RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
		
		EasyMock.expect(bindingResult.hasErrors()).andReturn(false);
		EasyMock.expect(ff4j.check("feature1")).andReturn(false);
		EasyMock.expect(ff4j.check("feature2")).andReturn(true);
		EasyMock.expect(ff4j.disable("feature2")).andReturn(ff4j);
		EasyMock.expect(ff4j.check("feature3")).andReturn(true);
		EasyMock.expect(ff4j.check("feature4")).andReturn(false);
		EasyMock.expect(ff4j.enable("feature4")).andReturn(ff4j);
		EasyMock.expect(ff4j.check("feature5")).andReturn(true);
		EasyMock.expect(ff4j.disable("feature5")).andReturn(ff4j);
		
		EasyMock.replay(ff4j, bindingResult);
		
		assertEquals("update should return 'redirect:/system/features' when update is successful","redirect:/system/features", featuresController.update(result, bindingResult, uiModel, redirectAttributes));
		assertTrue("uiModel should be cleared when update is successful", uiModel.asMap().isEmpty());
		
		EasyMock.verify(ff4j, bindingResult);
	}

}
