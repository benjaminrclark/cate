package org.cateproject.web.admin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.servlet.http.HttpServletRequest;

import org.cateproject.domain.admin.Tenant;
import org.cateproject.repository.jpa.admin.TenantRepository;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

public class TenantControllerTest {
	
	private TenantController tenantController;
	
	private TenantRepository tenantRepository;

	@Before
	public void setUp() throws Exception {
		tenantController = new TenantController();
		tenantRepository = EasyMock.createMock(TenantRepository.class);
		tenantController.setTenantRepository(tenantRepository);
	}

	@Test
	public void testShow() {
		Model uiModel = new ExtendedModelMap();
		Tenant tenant = new Tenant();
		EasyMock.expect(tenantRepository.findOne(EasyMock.eq(1L))).andReturn(tenant);
		
		EasyMock.replay(tenantRepository);
		assertEquals("show should return 'tenant/show'", "tenant/show", tenantController.show(uiModel));
		assertEquals("uiModel should contain the tenant", tenant, uiModel.asMap().get("tenant"));
		assertEquals("uiModel should contain the itemId", new Long(1L), uiModel.asMap().get("itemId"));
		EasyMock.verify(tenantRepository);
	}

	@Test
	public void testUpdateSuccess() {
		Model uiModel = new ExtendedModelMap();
		Tenant tenant = new Tenant();
		final Capture<Tenant> capturedTenant = Capture.newInstance();
		BindingResult bindingResult = EasyMock.createMock(BindingResult.class);
		HttpServletRequest request = new MockHttpServletRequest();
		
		EasyMock.expect(bindingResult.hasErrors()).andReturn(false);
		EasyMock.expect(tenantRepository.save(EasyMock.and(EasyMock.capture(capturedTenant), EasyMock.isA(Tenant.class)))).andAnswer(
				new IAnswer<Tenant>() {
					@Override
					public Tenant answer() throws Throwable {
						return capturedTenant.getValue();
					}					
				});
		
		EasyMock.replay(tenantRepository, bindingResult);
		assertEquals("update should return 'redirect:/tenant' when update is successful", "redirect:/tenant", tenantController.update(tenant, bindingResult, uiModel, request));
		assertTrue("uiModel should be cleared when update is successful", uiModel.asMap().isEmpty());
		EasyMock.verify(tenantRepository, bindingResult);
	}
	
	@Test
	public void testUpdateBindingErrors() {
		Model uiModel = new ExtendedModelMap();
		Tenant tenant = new Tenant();
		BindingResult bindingResult = EasyMock.createMock(BindingResult.class);
		HttpServletRequest request = new MockHttpServletRequest();
		
		EasyMock.expect(bindingResult.hasErrors()).andReturn(true);
		
		EasyMock.replay(tenantRepository, bindingResult);
		assertEquals("update should return 'tenant/update' when bindingResult.hasErrors", "tenant/update", tenantController.update(tenant, bindingResult, uiModel, request));
		assertEquals("uiModel should contain the submitted tenant with binding errors",uiModel.asMap().get("tenant"), tenant);
		EasyMock.verify(tenantRepository, bindingResult);
	}

	@Test
	public void testUpdateForm() {
		Model uiModel = new ExtendedModelMap();
		Tenant tenant = new Tenant();
		EasyMock.expect(tenantRepository.findOne(EasyMock.eq(1L))).andReturn(tenant);
		
		EasyMock.replay(tenantRepository);
		assertEquals("updateForm should return 'tenant/update'", "tenant/update", tenantController.updateForm(uiModel));
		assertEquals("uiModel should contain the tenant", tenant, uiModel.asMap().get("tenant"));
		EasyMock.verify(tenantRepository);
	}

	@Test
	public void testPopulateEditForm() {
		Model uiModel = new ExtendedModelMap();
		Tenant tenant = new Tenant();
		tenantController.populateEditForm(uiModel, tenant);
		
		assertEquals("uiModel should contain the tenant", tenant, uiModel.asMap().get("tenant"));
	}

}
