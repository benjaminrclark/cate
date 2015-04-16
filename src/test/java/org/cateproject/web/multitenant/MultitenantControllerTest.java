package org.cateproject.web.multitenant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.cateproject.multitenant.MultitenantRepository;
import org.cateproject.multitenant.domain.Multitenant;
import org.cateproject.web.multitenant.MultitenantController;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

public class MultitenantControllerTest {
	
	private MultitenantController multitenantController;
	
	private MultitenantRepository multitenantRepository;
	
	@Before
	public void setUp() {
		multitenantController = new MultitenantController();
		multitenantRepository = EasyMock.createMock(MultitenantRepository.class);
		multitenantController.setMultitenantRepository(multitenantRepository);
	}

	@Test
	public void testCreateBindingErrors() {
		Multitenant multitenant = new Multitenant();
		BindingResult bindingResult = EasyMock.createMock(BindingResult.class);
		Model uiModel = new ExtendedModelMap();
		MockHttpServletRequest request = new MockHttpServletRequest();
		
		EasyMock.expect(bindingResult.hasErrors()).andReturn(true);
		EasyMock.replay(bindingResult, multitenantRepository);
		assertEquals("create should return 'multitenant/create' when bindingResult.hasErrors", "multitenant/create", multitenantController.create(multitenant, bindingResult, uiModel, request));
		assertEquals("uiModel should contain the submitted multitenant with binding errors",uiModel.asMap().get("multitenant"), multitenant);
		EasyMock.verify(bindingResult, multitenantRepository);
	}
	
	@Test
	public void testCreateSuccess() {
		Multitenant multitenant = new Multitenant();
		BindingResult bindingResult = EasyMock.createMock(BindingResult.class);
		Model uiModel = new ExtendedModelMap();
		MockHttpServletRequest request = new MockHttpServletRequest();
		final Capture<Multitenant> capturedTenant = Capture.newInstance();
		
		EasyMock.expect(bindingResult.hasErrors()).andReturn(false);
		EasyMock.expect(multitenantRepository.save(EasyMock.and(EasyMock.capture(capturedTenant), EasyMock.isA(Multitenant.class)))).andAnswer(
				new IAnswer<Multitenant>() {
					@Override
					public Multitenant answer() throws Throwable {
						capturedTenant.getValue().setId(1234L);
						return capturedTenant.getValue();
					}					
				});
		EasyMock.replay(bindingResult, multitenantRepository);
		assertEquals("create should return 'redirect:/multitenant/1234' when create is successful", "redirect:/multitenant/1234", multitenantController.create(multitenant, bindingResult, uiModel, request));
		assertTrue("uiModel should be cleared when create is successful", uiModel.asMap().isEmpty());
		
		EasyMock.verify(bindingResult, multitenantRepository);
	}

	@Test
	public void testCreateForm() {
		Model uiModel = new ExtendedModelMap();
		
		EasyMock.replay(multitenantRepository);
		assertEquals("createForm should return 'multitenant/create'", "multitenant/create", multitenantController.createForm(uiModel));
		EasyMock.verify(multitenantRepository);
		assertNotNull("uiModel should contain a multitenant",uiModel.asMap().get("multitenant"));
	}

	@Test
	public void testShow() {
		Multitenant multitenant = new Multitenant();
		Model uiModel = new ExtendedModelMap();
		EasyMock.expect(multitenantRepository.findOne(EasyMock.eq(1L))).andReturn(multitenant);
		
		EasyMock.replay(multitenantRepository);
		assertEquals("show should return 'multitenant/show'", "multitenant/show", multitenantController.show(1L, uiModel));
		EasyMock.verify(multitenantRepository);
		assertEquals("uiModel should contain the multitenant",uiModel.asMap().get("multitenant"), multitenant);
		assertEquals("uiModel should contain the id",uiModel.asMap().get("itemId"), 1L);
	}

	@Test
	public void testList() {
		Pageable pageable = new PageRequest(0, 10);
		Page<Multitenant> multitenants = new PageImpl<Multitenant>(new ArrayList<Multitenant>(), pageable, 0L);
		Model uiModel = new ExtendedModelMap();
		EasyMock.expect(multitenantRepository.findAll(EasyMock.eq(pageable))).andReturn(multitenants);
		
		EasyMock.replay(multitenantRepository);
		assertEquals("list should return 'multitenant/list'", "multitenant/list", multitenantController.list(pageable, uiModel));
		assertEquals("uiModel should contain the list", uiModel.asMap().get("multitenants"), multitenants);
		EasyMock.verify(multitenantRepository);
	}

	@Test
	public void testUpdateBindingErrors() {
		Multitenant multitenant = new Multitenant();
		BindingResult bindingResult = EasyMock.createMock(BindingResult.class);
		Model uiModel = new ExtendedModelMap();
		MockHttpServletRequest request = new MockHttpServletRequest();
		
		EasyMock.expect(bindingResult.hasErrors()).andReturn(true);
		EasyMock.replay(bindingResult, multitenantRepository);
		assertEquals("create should return 'multitenant/update' when bindingResult.hasErrors", "multitenant/update", multitenantController.update(multitenant, bindingResult, uiModel, request));
		assertEquals("uiModel should contain the submitted multitenant with binding errors",uiModel.asMap().get("multitenant"), multitenant);
		EasyMock.verify(bindingResult, multitenantRepository);
	}
	
	@Test
	public void testUpdateSuccess() {
		Multitenant multitenant = new Multitenant();
		multitenant.setId(4321L);
		BindingResult bindingResult = EasyMock.createMock(BindingResult.class);
		Model uiModel = new ExtendedModelMap();
		MockHttpServletRequest request = new MockHttpServletRequest();
		final Capture<Multitenant> capturedTenant = Capture.newInstance();
		
		EasyMock.expect(bindingResult.hasErrors()).andReturn(false);
		EasyMock.expect(multitenantRepository.save(EasyMock.and(EasyMock.capture(capturedTenant), EasyMock.isA(Multitenant.class)))).andAnswer(
				new IAnswer<Multitenant>() {
					@Override
					public Multitenant answer() throws Throwable {
						return capturedTenant.getValue();
					}					
				});
		EasyMock.replay(bindingResult, multitenantRepository);
		assertEquals("update should return 'redirect:/multitenant/4321' when update is successful", "redirect:/multitenant/4321", multitenantController.update(multitenant, bindingResult, uiModel, request));
		assertTrue("uiModel should be cleared when update is successful", uiModel.asMap().isEmpty());
		
		EasyMock.verify(bindingResult, multitenantRepository);
	}

	@Test
	public void testUpdateForm() {
		Multitenant multitenant = new Multitenant();
		Model uiModel = new ExtendedModelMap();
		EasyMock.expect(multitenantRepository.findOne(EasyMock.eq(1L))).andReturn(multitenant);
		
		EasyMock.replay(multitenantRepository);
		assertEquals("updateForm should return 'multitenant/update'", "multitenant/update", multitenantController.updateForm(1L, uiModel));
		EasyMock.verify(multitenantRepository);
		assertEquals("uiModel should contain the multitenant",uiModel.asMap().get("multitenant"), multitenant);
	}

	@Test
	public void testDelete() {
		Multitenant multitenant = new Multitenant();
		Model uiModel = new ExtendedModelMap();
		EasyMock.expect(multitenantRepository.findOne(EasyMock.eq(1L))).andReturn(multitenant);
		multitenantRepository.delete(EasyMock.eq(multitenant));
		
		EasyMock.replay(multitenantRepository);
		assertEquals("delete should return 'redirect:/multitenant'", "redirect:/multitenant", multitenantController.delete(1L, 2, 20, uiModel));
		assertEquals("uiModel should contain the page", uiModel.asMap().get("page"), "2");
		assertEquals("uiModel should contain the page size", uiModel.asMap().get("size"), "20");
		EasyMock.verify(multitenantRepository);
	}

	@Test
	public void testPopulateEditForm() {
		Multitenant multitenant = new Multitenant();
		Model uiModel = new ExtendedModelMap();
		
		multitenantController.populateEditForm(uiModel, multitenant);
		assertEquals("uiModel should contain the multitenant",uiModel.asMap().get("multitenant"), multitenant);
	}

}
