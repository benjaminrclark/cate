package org.cateproject.web.multitenant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.cateproject.multitenant.MultitenantManager;
import org.cateproject.multitenant.MultitenantProperties;
import org.cateproject.multitenant.domain.Multitenant;
import org.cateproject.web.multitenant.InitController;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

public class InitControllerTest {
	
	private InitController initController;
	
	private MultitenantProperties multitenantProperties;
	
	private MultitenantManager multitenantManager;
	
	@Before
	public void setUp() {
		initController = new InitController();
		multitenantProperties = new MultitenantProperties();
		multitenantProperties.setDefaultIdentifier("DEFAULT_IDENTIFIER");
		multitenantProperties.setDefaultTitle("DEFAULT_TITLE");
		multitenantProperties.setDefaultAdminUsername("DEFAULT_ADMIN");
		multitenantProperties.setDefaultOwnerUsername("DEFAULT_OWNER");
		multitenantProperties.setDatabasePassword("DATABASE_PASSWORD");
		multitenantProperties.setDatabaseUsername("DATABASE_USERNAME");
		multitenantProperties.setDatabaseUrl("DATABASE_URL");
		multitenantProperties.setDatabaseDriverClassname("DATABASE_DRIVER_CLASSNAME");
		initController.setMultitenantProperties(multitenantProperties);
		
		multitenantManager = EasyMock.createMock(MultitenantManager.class);
		initController.setMultitenantManager(multitenantManager);
		
	}
	
	@Test
	public void testCreateBindingError() {
		Multitenant multitenant = new Multitenant();
		BindingResult bindingResult = EasyMock.createMock(BindingResult.class);
		Model uiModel = new ExtendedModelMap();
		MockHttpServletRequest request = new MockHttpServletRequest();
		RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
		
		EasyMock.expect(bindingResult.hasErrors()).andReturn(true);
		EasyMock.replay(multitenantManager, bindingResult);
		
		assertEquals("create should return 'init/create' when bindingResult.hasErrors", "init/create", initController.create(multitenant, bindingResult, uiModel, request, redirectAttributes));
		
		EasyMock.verify(multitenantManager, bindingResult);
		
	}

	@Test
	public void testCreateSuccess() {
		Multitenant multitenant = new Multitenant();
		multitenant.setTitle("TENANT_TITLE");
		BindingResult bindingResult = EasyMock.createMock(BindingResult.class);
		Model uiModel = new ExtendedModelMap();
		MockHttpServletRequest request = new MockHttpServletRequest();
		RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
		
		EasyMock.expect(bindingResult.hasErrors()).andReturn(false);
		multitenantManager.save(EasyMock.eq(multitenant), EasyMock.eq(true));
		EasyMock.replay(multitenantManager, bindingResult);
		
		assertEquals("create should return 'redirect:/tenant' when create is successful", "redirect:/tenant", initController.create(multitenant, bindingResult, uiModel, request, redirectAttributes));
		assertTrue("uiModel should be cleared when create is successful", uiModel.asMap().isEmpty());
		MessageSourceResolvable message = (MessageSourceResolvable)redirectAttributes.getFlashAttributes().get("success");
		assertEquals("The success message code should be 'tenant_created'",message.getCodes()[0], "tenant_created");
		assertEquals("The success message argument should be 'TENANT_TITLE'",message.getArguments()[0], "TENANT_TITLE");
		EasyMock.verify(multitenantManager, bindingResult);
		
	}

	@Test
	public void testCreateForm() {
		Model uiModel = new ExtendedModelMap();
		
		assertEquals("createForm should return 'init/create'", "init/create", initController.createForm(uiModel));
		Multitenant multitenant = (Multitenant)uiModel.asMap().get("multitenant");
		assertEquals("The tenant's identifier should be 'DEFAULT_IDENTIFIER'",multitenant.getIdentifier(), "DEFAULT_IDENTIFIER");
		assertEquals("The tenant's title should be 'DEFAULT_TITLE'",multitenant.getTitle(), "DEFAULT_TITLE");
		assertEquals("The tenant's admin email should be 'DEFAULT_ADMIN'",multitenant.getAdminEmail(), "DEFAULT_ADMIN");
		assertEquals("The tenant's owner email should be 'DEFAULT_OWNER'",multitenant.getOwnerEmail(), "DEFAULT_OWNER");
		assertEquals("The tenant's database password should be 'DATABASE_PASSWORD'",multitenant.getDatabasePassword(), "DATABASE_PASSWORD");
		assertEquals("The tenant's database username should be 'DATABASE_USERNAME'",multitenant.getDatabaseUsername(), "DATABASE_USERNAME");
		assertEquals("The tenant's database url should be 'DATABASE_URL",multitenant.getDatabaseUrl(), "DATABASE_URL");
		assertEquals("The tenant's database driver class should be 'DATABASE_DRIVER_CLASS'",multitenant.getDriverClassName(), "DATABASE_DRIVER_CLASSNAME");
		MessageSourceResolvable message = (MessageSourceResolvable)uiModel.asMap().get("info");
		assertEquals("The info message code should be 'initialize_cate'",message.getCodes()[0], "initialize_cate");
	}

	@Test
	public void testPopulateEditForm() {
		Model uiModel = new ExtendedModelMap();
		Multitenant multitenant = new Multitenant();
		
		initController.populateEditForm(uiModel, multitenant);
		assertEquals("The model should contain the multitenant", uiModel.asMap().get("multitenant"), multitenant);
		
	}

}
