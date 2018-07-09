package org.cateproject.web.edit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.cateproject.domain.Multimedia;
import org.cateproject.domain.constants.DCMIType;
import org.cateproject.repository.jpa.MultimediaRepository;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.core.convert.ConversionException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

public class MultimediaControllerTest {
	
	private MultimediaController multimediaController;
	
	private MultimediaRepository multimediaRepository;
	
	private ConversionService conversionService;

	@Before
	public void setUp() throws Exception {
		multimediaController = new MultimediaController();
		multimediaRepository = EasyMock.createMock(MultimediaRepository.class);
		conversionService = EasyMock.createMock(ConversionService.class);
		multimediaController.setMultimediaRepository(multimediaRepository);
		multimediaController.setConversionService(conversionService);
	}

	@Test
	public void testList() {
		Model uiModel = new ExtendedModelMap();
		Pageable pageable = new PageRequest(0, 10);
		Page<Multimedia> results = new PageImpl<Multimedia>(new ArrayList<Multimedia>(), pageable, 0);
		EasyMock.expect(multimediaRepository.findAll(EasyMock.eq(pageable))).andReturn(results);
		
		EasyMock.replay(multimediaRepository, conversionService);
		
		assertEquals("list should return 'edit/multimedia/list'", "edit/multimedia/list", multimediaController.list(pageable, uiModel));
		assertEquals("uiModel should contain the results", uiModel.asMap().get("results"),results);
		
		EasyMock.verify(multimediaRepository, conversionService);
	}

	@Test
	public void testPartial() {
		Model uiModel = new ExtendedModelMap();
		MockHttpServletRequest request = new MockHttpServletRequest();
		RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
		Map<String, String> params = new HashMap<String, String>();
		params.put("identifier", "new_value");
		Multimedia multimedia = new Multimedia();
		multimedia.setId(1L);
		multimedia.setIdentifier("old_value");
		final Capture<Multimedia> capturedMultimedia = Capture.newInstance();
		
		EasyMock.expect(multimediaRepository.findOne(EasyMock.eq(1L))).andReturn(multimedia);
		EasyMock.expect(conversionService.convert(EasyMock.eq("new_value"), EasyMock.eq(String.class))).andReturn("new_value");
		EasyMock.expect(multimediaRepository.save(EasyMock.and(EasyMock.capture(capturedMultimedia), EasyMock.isA(Multimedia.class)))).andAnswer(new IAnswer<Multimedia>() {
					@Override
					public Multimedia answer() throws Throwable {
						return capturedMultimedia.getValue();
					}					
				});
		EasyMock.replay(multimediaRepository, conversionService);
		
		assertEquals("partial should return 'redirect:/edit/multimedia/1'", "redirect:/edit/multimedia/1", multimediaController.partial(1L, params, uiModel, request, redirectAttributes));
		assertTrue("uiModel should be empty", uiModel.asMap().isEmpty());
		assertEquals("capturedMultimedia should have its identifier set to 'new_value'","new_value", capturedMultimedia.getValue().getIdentifier());
		assertEquals("success message code should be 'entity_updated'", "entity_updated", ((MessageSourceResolvable)redirectAttributes.getFlashAttributes().get("success")).getCodes()[0]);
		assertEquals("success message argument should be 'Multimedia<new_value>'", "Multimedia<new_value>", ((MessageSourceResolvable)redirectAttributes.getFlashAttributes().get("success")).getArguments()[0]);
		EasyMock.verify(multimediaRepository, conversionService);
	}
	
	@Test
	public void testCreateSuccess() {
		Model uiModel = new ExtendedModelMap();
		MockHttpServletRequest request = new MockHttpServletRequest();
		RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
		BindingResult bindingResult = EasyMock.createMock(BindingResult.class);
		Multimedia multimedia = new Multimedia();
		multimedia.setIdentifier("IDENTIFIER");
		final Capture<Multimedia> capturedMultimedia = Capture.newInstance();
	
		EasyMock.expect(bindingResult.hasErrors()).andReturn(false);
		EasyMock.expect(multimediaRepository.save(EasyMock.and(EasyMock.capture(capturedMultimedia), EasyMock.isA(Multimedia.class)))).andAnswer(new IAnswer<Multimedia>() {
					@Override
					public Multimedia answer() throws Throwable {
						capturedMultimedia.getValue().setId(1L);
						return capturedMultimedia.getValue();
					}					
				});
		EasyMock.replay(multimediaRepository, conversionService, bindingResult);
		
		assertEquals("create should return 'redirect:/edit/multimedia/1'", "redirect:/edit/multimedia/1", multimediaController.create(multimedia, bindingResult, uiModel, request, redirectAttributes));
		assertTrue("uiModel should be empty", uiModel.asMap().isEmpty());
		assertEquals("success message code should be 'entity_created'", "entity_created", ((MessageSourceResolvable)redirectAttributes.getFlashAttributes().get("success")).getCodes()[0]);
		assertEquals("success message argument should be 'Multimedia<IDENTIFIER>'", "Multimedia<IDENTIFIER>", ((MessageSourceResolvable)redirectAttributes.getFlashAttributes().get("success")).getArguments()[0]);
		EasyMock.verify(multimediaRepository, conversionService, bindingResult);
	}

	@Test
	public void testCreateError() {
		Model uiModel = new ExtendedModelMap();
		MockHttpServletRequest request = new MockHttpServletRequest();
		RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
		BindingResult bindingResult = EasyMock.createMock(BindingResult.class);
		Multimedia multimedia = new Multimedia();
		multimedia.setIdentifier("IDENTIFIER");
		final Capture<Multimedia> capturedMultimedia = Capture.newInstance();
	
		EasyMock.expect(bindingResult.hasErrors()).andReturn(true);
		EasyMock.replay(multimediaRepository, conversionService, bindingResult);
		
		assertEquals("create should return 'edit/multimedia/create'", "edit/multimedia/create", multimediaController.create(multimedia, bindingResult, uiModel, request, redirectAttributes));
		assertEquals("uiModel should contain the multimedia", uiModel.asMap().get("result"), multimedia);
		assertEquals("error message code should be 'entity_create_error'", "entity_create_error", ((MessageSourceResolvable)uiModel.asMap().get("error")).getCodes()[0]);
		assertEquals("error message argument should be 'Multimedia<IDENTIFIER>'", "Multimedia<IDENTIFIER>", ((MessageSourceResolvable)uiModel.asMap().get("error")).getArguments()[0]);
		assertEquals("uiModel should contain a list of types", uiModel.asMap().get("types"), Arrays.asList(DCMIType.values()));
		EasyMock.verify(multimediaRepository, conversionService, bindingResult);
	}

	@Test
	public void testCreateForm() {
		Model uiModel = new ExtendedModelMap();		
		EasyMock.replay(multimediaRepository, conversionService);
		
		assertEquals("createForm should return 'edit/multimedia/create'", "edit/multimedia/create", multimediaController.createForm(uiModel));
		assertEquals("uiModel should contain the result", uiModel.asMap().get("result").getClass(), Multimedia.class);
		assertEquals("uiModel should contain a list of types", uiModel.asMap().get("types"), Arrays.asList(DCMIType.values()));
		
		EasyMock.verify(multimediaRepository, conversionService);
	}

	@Test
	public void testShow() {
		Model uiModel = new ExtendedModelMap();
		Multimedia multimedia = new Multimedia();
		multimedia.setIdentifier("IDENTIFIER");
		EasyMock.expect(multimediaRepository.findOne(EasyMock.eq(1L))).andReturn(multimedia);
		
		EasyMock.replay(multimediaRepository, conversionService);
		
		assertEquals("show should return 'edit/multimedia/show'", "edit/multimedia/show", multimediaController.show(1L, uiModel));
		assertEquals("uiModel should contain the result", uiModel.asMap().get("result"),multimedia);
		
		EasyMock.verify(multimediaRepository, conversionService);
	}

	@Test
	public void testUpdateSuccess() {
		Model uiModel = new ExtendedModelMap();
		MockHttpServletRequest request = new MockHttpServletRequest();
		RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
		BindingResult bindingResult = EasyMock.createMock(BindingResult.class);
		Multimedia multimedia = new Multimedia();
		multimedia.setId(1L);
		multimedia.setIdentifier("IDENTIFIER");
		final Capture<Multimedia> capturedMultimedia = Capture.newInstance();
	
		EasyMock.expect(bindingResult.hasErrors()).andReturn(false);
		EasyMock.expect(multimediaRepository.save(EasyMock.and(EasyMock.capture(capturedMultimedia), EasyMock.isA(Multimedia.class)))).andAnswer(new IAnswer<Multimedia>() {
					@Override
					public Multimedia answer() throws Throwable {
						return capturedMultimedia.getValue();
					}					
				});
		EasyMock.replay(multimediaRepository, conversionService, bindingResult);
		
		assertEquals("update should return 'redirect:/edit/multimedia/1'", "redirect:/edit/multimedia/1", multimediaController.update(1L, multimedia, bindingResult, uiModel, request, redirectAttributes));
		assertTrue("uiModel should be empty", uiModel.asMap().isEmpty());
		assertEquals("success message code should be 'entity_updated'", "entity_updated", ((MessageSourceResolvable)redirectAttributes.getFlashAttributes().get("success")).getCodes()[0]);
		assertEquals("success message argument should be 'Multimedia<IDENTIFIER>'", "Multimedia<IDENTIFIER>", ((MessageSourceResolvable)redirectAttributes.getFlashAttributes().get("success")).getArguments()[0]);
		EasyMock.verify(multimediaRepository, conversionService, bindingResult);
	}
	
	@Test
	public void testUpdateError() {
		Model uiModel = new ExtendedModelMap();
		MockHttpServletRequest request = new MockHttpServletRequest();
		RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
		BindingResult bindingResult = EasyMock.createMock(BindingResult.class);
		Multimedia multimedia = new Multimedia();
		multimedia.setIdentifier("IDENTIFIER");
		final Capture<Multimedia> capturedMultimedia = Capture.newInstance();
	
		EasyMock.expect(bindingResult.hasErrors()).andReturn(true);
		EasyMock.replay(multimediaRepository, conversionService, bindingResult);
		
		assertEquals("update should return 'edit/multimedia/update'", "edit/multimedia/update", multimediaController.update(1L, multimedia, bindingResult, uiModel, request, redirectAttributes));
		assertEquals("uiModel should contain the multimedia", uiModel.asMap().get("result"), multimedia);
		assertEquals("error message code should be 'entity_create_error'", "entity_update_error", ((MessageSourceResolvable)uiModel.asMap().get("error")).getCodes()[0]);
		assertEquals("error message argument should be 'Multimedia<IDENTIFIER>'", "Multimedia<IDENTIFIER>", ((MessageSourceResolvable)uiModel.asMap().get("error")).getArguments()[0]);
		assertEquals("uiModel should contain a list of types", uiModel.asMap().get("types"), Arrays.asList(DCMIType.values()));
		EasyMock.verify(multimediaRepository, conversionService, bindingResult);
	}

	@Test
	public void testUpdateForm() {
		Model uiModel = new ExtendedModelMap();	
		Multimedia multimedia = new Multimedia();
		multimedia.setIdentifier("IDENTIFIER");
		EasyMock.expect(multimediaRepository.findOne(1L)).andReturn(multimedia);
		EasyMock.replay(multimediaRepository, conversionService);
		
		assertEquals("updateForm should return 'edit/multimedia/update'", "edit/multimedia/update", multimediaController.updateForm(1L, uiModel));
		assertEquals("uiModel should contain the result", uiModel.asMap().get("result"), multimedia);
		assertEquals("uiModel should contain a list of types", uiModel.asMap().get("types"), Arrays.asList(DCMIType.values()));
		
		EasyMock.verify(multimediaRepository, conversionService);
	}

	@Test
	public void testDelete() {
		Model uiModel = new ExtendedModelMap();
		MockHttpServletRequest request = new MockHttpServletRequest();
		RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
		Multimedia multimedia = new Multimedia();
		multimedia.setIdentifier("IDENTIFIER");
		EasyMock.expect(multimediaRepository.findOne(EasyMock.eq(1L))).andReturn(multimedia);
		multimediaRepository.delete(EasyMock.eq(multimedia));
		EasyMock.replay(multimediaRepository, conversionService);
		
		assertEquals("delete should return 'redirect:/edit/multimedia'", "redirect:/edit/multimedia", multimediaController.delete(1L, 2, 20, uiModel, redirectAttributes));
		assertEquals("uiModel should contain 'page' parameter", uiModel.asMap().get("page"), 2);
		assertEquals("uiModel should contain 'size' parameter", uiModel.asMap().get("size"), 20);
		assertEquals("success message code should be 'entity_deleted'", "entity_deleted", ((MessageSourceResolvable)redirectAttributes.getFlashAttributes().get("success")).getCodes()[0]);
		assertEquals("success message argument should be 'Multimedia<IDENTIFIER>'", "Multimedia<IDENTIFIER>", ((MessageSourceResolvable)redirectAttributes.getFlashAttributes().get("success")).getArguments()[0]);
		EasyMock.verify(multimediaRepository, conversionService);
	}

	@Test
	public void testEncodeUrlPathSegment() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletRequest requestWithNullEncoding = new MockHttpServletRequest();
		requestWithNullEncoding.setCharacterEncoding(null);
		MockHttpServletRequest requestWithInvalidEncoding = new MockHttpServletRequest();
		requestWithInvalidEncoding.setCharacterEncoding("INVALID_ENCODING");
		assertEquals("encoded path segment should equal '%2Fpath%2F1' for valid encoding", "%2Fpath%2F1", multimediaController.encodeUrlPathSegment("/path/1", request));
		assertEquals("encoded path segment should equal '%2Fpath%2F1' for null encoding", "%2Fpath%2F1", multimediaController.encodeUrlPathSegment("/path/1", requestWithNullEncoding));
		assertEquals("encoded path segment should equal '/path/1' for invalid encoding", "/path/1", multimediaController.encodeUrlPathSegment("/path/1", requestWithInvalidEncoding));
	}

	@Test
	public void testHandleConversionException() {
		ConversionException ce = new ConversionException("MESSAGE") {};
		assertEquals("handleConversionException should return 'MESSAGE'", "MESSAGE", multimediaController.handleConversionException(ce));
	}

	@Test
	public void testPopulateEditForm() {
		Model uiModel = new ExtendedModelMap();
		Multimedia multimedia = new Multimedia();		
		multimediaController.populateEditForm(uiModel, multimedia);
		assertEquals("uiModel should contain the result", uiModel.asMap().get("result"), multimedia);
		assertEquals("uiModel should contain a list of types", uiModel.asMap().get("types"), Arrays.asList(DCMIType.values()));
	}

}
