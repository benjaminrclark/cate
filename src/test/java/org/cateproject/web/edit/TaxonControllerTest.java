package org.cateproject.web.edit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.cateproject.domain.Taxon;
import org.cateproject.repository.jpa.TaxonRepository;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.gbif.ecat.voc.NomenclaturalCode;
import org.gbif.ecat.voc.NomenclaturalStatus;
import org.gbif.ecat.voc.Rank;
import org.gbif.ecat.voc.TaxonomicStatus;
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

public class TaxonControllerTest {
	
	private TaxonController taxonController;
	
	private TaxonRepository taxonRepository;
	
	private ConversionService conversionService;

	@Before
	public void setUp() throws Exception {
		taxonController = new TaxonController();
		taxonRepository = EasyMock.createMock(TaxonRepository.class);
		conversionService = EasyMock.createMock(ConversionService.class);
		taxonController.setTaxonRepository(taxonRepository);
		taxonController.setConversionService(conversionService);
	}

	@Test
	public void testList() {
		Model uiModel = new ExtendedModelMap();
		Pageable pageable = new PageRequest(0, 10);
		Page<Taxon> results = new PageImpl<Taxon>(new ArrayList<Taxon>(), pageable, 0);
		EasyMock.expect(taxonRepository.findAll(EasyMock.eq(pageable))).andReturn(results);
		
		EasyMock.replay(taxonRepository, conversionService);
		
		assertEquals("list should return 'edit/taxon/list'", "edit/taxon/list", taxonController.list(pageable, uiModel));
		assertEquals("uiModel should contain the results", uiModel.asMap().get("results"),results);
		assertEquals("uiModel should contain a list of nomenclatural codes", uiModel.asMap().get("nomenclaturalcodes"), Arrays.asList(NomenclaturalCode.values()));
		assertEquals("uiModel should contain a list of ranks", uiModel.asMap().get("ranks"), Arrays.asList(Rank.values()));
		assertEquals("uiModel should contain a list of taxonomic statuses", uiModel.asMap().get("taxonomicstatuses"), Arrays.asList(TaxonomicStatus.values()));
		assertEquals("uiModel should contain a list of nomenclatural statuses", uiModel.asMap().get("nomenclaturalstatuses"), Arrays.asList(NomenclaturalStatus.values()));
		
		EasyMock.verify(taxonRepository, conversionService);
	}

	@Test
	public void testPartial() {
		Model uiModel = new ExtendedModelMap();
		MockHttpServletRequest request = new MockHttpServletRequest();
		RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
		Map<String, String> params = new HashMap<String, String>();
		params.put("identifier", "new_value");
		Taxon taxon = new Taxon();
		taxon.setId(1L);
		taxon.setIdentifier("old_value");
		final Capture<Taxon> capturedTaxon = Capture.newInstance();
		
		EasyMock.expect(taxonRepository.findOne(EasyMock.eq(1L))).andReturn(taxon);
		EasyMock.expect(conversionService.convert(EasyMock.eq("new_value"), EasyMock.eq(String.class))).andReturn("new_value");
		EasyMock.expect(taxonRepository.save(EasyMock.and(EasyMock.capture(capturedTaxon), EasyMock.isA(Taxon.class)))).andAnswer(new IAnswer<Taxon>() {
					@Override
					public Taxon answer() throws Throwable {
						return capturedTaxon.getValue();
					}					
				});
		EasyMock.replay(taxonRepository, conversionService);
		
		assertEquals("partial should return 'redirect:/edit/taxon/1'", "redirect:/edit/taxon/1", taxonController.partial(1L, params, uiModel, request, redirectAttributes));
		assertTrue("uiModel should be empty", uiModel.asMap().isEmpty());
		assertEquals("capturedTaxon should have its identifier set to 'new_value'","new_value", capturedTaxon.getValue().getIdentifier());
		assertEquals("success message code should be 'entity_updated'", "entity_updated", ((MessageSourceResolvable)redirectAttributes.getFlashAttributes().get("success")).getCodes()[0]);
		assertEquals("success message argument should be 'Taxon<new_value>'", "Taxon<new_value>", ((MessageSourceResolvable)redirectAttributes.getFlashAttributes().get("success")).getArguments()[0]);
		EasyMock.verify(taxonRepository, conversionService);
	}
	
	@Test
	public void testCreateSuccess() {
		Model uiModel = new ExtendedModelMap();
		MockHttpServletRequest request = new MockHttpServletRequest();
		RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
		BindingResult bindingResult = EasyMock.createMock(BindingResult.class);
		Taxon taxon = new Taxon();
		taxon.setIdentifier("IDENTIFIER");
		final Capture<Taxon> capturedTaxon = Capture.newInstance();
	
		EasyMock.expect(bindingResult.hasErrors()).andReturn(false);
		EasyMock.expect(taxonRepository.save(EasyMock.and(EasyMock.capture(capturedTaxon), EasyMock.isA(Taxon.class)))).andAnswer(new IAnswer<Taxon>() {
					@Override
					public Taxon answer() throws Throwable {
						capturedTaxon.getValue().setId(1L);
						return capturedTaxon.getValue();
					}					
				});
		EasyMock.replay(taxonRepository, conversionService, bindingResult);
		
		assertEquals("create should return 'redirect:/edit/taxon/1'", "redirect:/edit/taxon/1", taxonController.create(taxon, bindingResult, uiModel, request, redirectAttributes));
		assertTrue("uiModel should be empty", uiModel.asMap().isEmpty());
		assertEquals("success message code should be 'entity_created'", "entity_created", ((MessageSourceResolvable)redirectAttributes.getFlashAttributes().get("success")).getCodes()[0]);
		assertEquals("success message argument should be 'Taxon<IDENTIFIER>'", "Taxon<IDENTIFIER>", ((MessageSourceResolvable)redirectAttributes.getFlashAttributes().get("success")).getArguments()[0]);
		EasyMock.verify(taxonRepository, conversionService, bindingResult);
	}

	@Test
	public void testCreateError() {
		Model uiModel = new ExtendedModelMap();
		MockHttpServletRequest request = new MockHttpServletRequest();
		RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
		BindingResult bindingResult = EasyMock.createMock(BindingResult.class);
		Taxon taxon = new Taxon();
		taxon.setIdentifier("IDENTIFIER");
		final Capture<Taxon> capturedTaxon = Capture.newInstance();
	
		EasyMock.expect(bindingResult.hasErrors()).andReturn(true);
		EasyMock.replay(taxonRepository, conversionService, bindingResult);
		
		assertEquals("create should return 'edit/taxon/create'", "edit/taxon/create", taxonController.create(taxon, bindingResult, uiModel, request, redirectAttributes));
		assertEquals("uiModel should contain the taxon", uiModel.asMap().get("result"), taxon);
		assertEquals("error message code should be 'entity_create_error'", "entity_create_error", ((MessageSourceResolvable)uiModel.asMap().get("error")).getCodes()[0]);
		assertEquals("error message argument should be 'Taxon<IDENTIFIER>'", "Taxon<IDENTIFIER>", ((MessageSourceResolvable)uiModel.asMap().get("error")).getArguments()[0]);
		EasyMock.verify(taxonRepository, conversionService, bindingResult);
	}

	@Test
	public void testCreateForm() {
		Model uiModel = new ExtendedModelMap();		
		EasyMock.replay(taxonRepository, conversionService);
		
		assertEquals("createForm should return 'edit/taxon/create'", "edit/taxon/create", taxonController.createForm(uiModel));
		assertEquals("uiModel should contain the result", uiModel.asMap().get("result").getClass(), Taxon.class);
		assertEquals("uiModel should contain a list of nomenclatural codes", uiModel.asMap().get("nomenclaturalcodes"), Arrays.asList(NomenclaturalCode.values()));
		assertEquals("uiModel should contain a list of ranks", uiModel.asMap().get("ranks"), Arrays.asList(Rank.values()));
		assertEquals("uiModel should contain a list of taxonomic statuses", uiModel.asMap().get("taxonomicstatuses"), Arrays.asList(TaxonomicStatus.values()));
		assertEquals("uiModel should contain a list of nomenclatural statuses", uiModel.asMap().get("nomenclaturalstatuses"), Arrays.asList(NomenclaturalStatus.values()));
		
		EasyMock.verify(taxonRepository, conversionService);
	}

	@Test
	public void testShow() {
		Model uiModel = new ExtendedModelMap();
		Taxon taxon = new Taxon();
		taxon.setIdentifier("IDENTIFIER");
		EasyMock.expect(taxonRepository.findOne(EasyMock.eq(1L))).andReturn(taxon);
		
		EasyMock.replay(taxonRepository, conversionService);
		
		assertEquals("show should return 'edit/taxon/show'", "edit/taxon/show", taxonController.show(1L, uiModel));
		assertEquals("uiModel should contain the result", uiModel.asMap().get("result"),taxon);
		
		EasyMock.verify(taxonRepository, conversionService);
	}

	@Test
	public void testUpdateSuccess() {
		Model uiModel = new ExtendedModelMap();
		MockHttpServletRequest request = new MockHttpServletRequest();
		RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
		BindingResult bindingResult = EasyMock.createMock(BindingResult.class);
		Taxon taxon = new Taxon();
		taxon.setId(1L);
		taxon.setIdentifier("IDENTIFIER");
		final Capture<Taxon> capturedTaxon = Capture.newInstance();
	
		EasyMock.expect(bindingResult.hasErrors()).andReturn(false);
		EasyMock.expect(taxonRepository.save(EasyMock.and(EasyMock.capture(capturedTaxon), EasyMock.isA(Taxon.class)))).andAnswer(new IAnswer<Taxon>() {
					@Override
					public Taxon answer() throws Throwable {
						return capturedTaxon.getValue();
					}					
				});
		EasyMock.replay(taxonRepository, conversionService, bindingResult);
		
		assertEquals("update should return 'redirect:/edit/taxon/1'", "redirect:/edit/taxon/1", taxonController.update(1L, taxon, bindingResult, uiModel, request, redirectAttributes));
		assertTrue("uiModel should be empty", uiModel.asMap().isEmpty());
		assertEquals("success message code should be 'entity_updated'", "entity_updated", ((MessageSourceResolvable)redirectAttributes.getFlashAttributes().get("success")).getCodes()[0]);
		assertEquals("success message argument should be 'Taxon<IDENTIFIER>'", "Taxon<IDENTIFIER>", ((MessageSourceResolvable)redirectAttributes.getFlashAttributes().get("success")).getArguments()[0]);
		EasyMock.verify(taxonRepository, conversionService, bindingResult);
	}
	
	@Test
	public void testUpdateError() {
		Model uiModel = new ExtendedModelMap();
		MockHttpServletRequest request = new MockHttpServletRequest();
		RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
		BindingResult bindingResult = EasyMock.createMock(BindingResult.class);
		Taxon taxon = new Taxon();
		taxon.setIdentifier("IDENTIFIER");
		final Capture<Taxon> capturedTaxon = Capture.newInstance();
	
		EasyMock.expect(bindingResult.hasErrors()).andReturn(true);
		EasyMock.replay(taxonRepository, conversionService, bindingResult);
		
		assertEquals("update should return 'edit/taxon/update'", "edit/taxon/update", taxonController.update(1L, taxon, bindingResult, uiModel, request, redirectAttributes));
		assertEquals("uiModel should contain the taxon", uiModel.asMap().get("result"), taxon);
		assertEquals("error message code should be 'entity_create_error'", "entity_update_error", ((MessageSourceResolvable)uiModel.asMap().get("error")).getCodes()[0]);
		assertEquals("error message argument should be 'Taxon<IDENTIFIER>'", "Taxon<IDENTIFIER>", ((MessageSourceResolvable)uiModel.asMap().get("error")).getArguments()[0]);
		EasyMock.verify(taxonRepository, conversionService, bindingResult);
	}

	@Test
	public void testUpdateForm() {
		Model uiModel = new ExtendedModelMap();	
		Taxon taxon = new Taxon();
		taxon.setIdentifier("IDENTIFIER");
		EasyMock.expect(taxonRepository.findOne(1L)).andReturn(taxon);
		EasyMock.replay(taxonRepository, conversionService);
		
		assertEquals("updateForm should return 'edit/taxon/update'", "edit/taxon/update", taxonController.updateForm(1L, uiModel));
		assertEquals("uiModel should contain the result", uiModel.asMap().get("result"), taxon);
		assertEquals("uiModel should contain a list of nomenclatural codes", uiModel.asMap().get("nomenclaturalcodes"), Arrays.asList(NomenclaturalCode.values()));
		assertEquals("uiModel should contain a list of ranks", uiModel.asMap().get("ranks"), Arrays.asList(Rank.values()));
		assertEquals("uiModel should contain a list of taxonomic statuses", uiModel.asMap().get("taxonomicstatuses"), Arrays.asList(TaxonomicStatus.values()));
		assertEquals("uiModel should contain a list of nomenclatural statuses", uiModel.asMap().get("nomenclaturalstatuses"), Arrays.asList(NomenclaturalStatus.values()));
		
		EasyMock.verify(taxonRepository, conversionService);
	}

	@Test
	public void testDelete() {
		Model uiModel = new ExtendedModelMap();
		MockHttpServletRequest request = new MockHttpServletRequest();
		RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
		Taxon taxon = new Taxon();
		taxon.setIdentifier("IDENTIFIER");
		EasyMock.expect(taxonRepository.findOne(EasyMock.eq(1L))).andReturn(taxon);
		taxonRepository.delete(EasyMock.eq(taxon));
		EasyMock.replay(taxonRepository, conversionService);
		
		assertEquals("delete should return 'redirect:/edit/taxon'", "redirect:/edit/taxon", taxonController.delete(1L, 2, 20, uiModel, redirectAttributes));
		assertEquals("uiModel should contain 'page' parameter", uiModel.asMap().get("page"), 2);
		assertEquals("uiModel should contain 'size' parameter", uiModel.asMap().get("size"), 20);
		assertEquals("success message code should be 'entity_deleted'", "entity_deleted", ((MessageSourceResolvable)redirectAttributes.getFlashAttributes().get("success")).getCodes()[0]);
		assertEquals("success message argument should be 'Taxon<IDENTIFIER>'", "Taxon<IDENTIFIER>", ((MessageSourceResolvable)redirectAttributes.getFlashAttributes().get("success")).getArguments()[0]);
		EasyMock.verify(taxonRepository, conversionService);
	}

	@Test
	public void testEncodeUrlPathSegment() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletRequest requestWithNullEncoding = new MockHttpServletRequest();
		requestWithNullEncoding.setCharacterEncoding(null);
		MockHttpServletRequest requestWithInvalidEncoding = new MockHttpServletRequest();
		requestWithInvalidEncoding.setCharacterEncoding("INVALID_ENCODING");
		assertEquals("encoded path segment should equal '%2Fpath%2F1' for valid encoding", "%2Fpath%2F1", taxonController.encodeUrlPathSegment("/path/1", request));
		assertEquals("encoded path segment should equal '%2Fpath%2F1' for null encoding", "%2Fpath%2F1", taxonController.encodeUrlPathSegment("/path/1", requestWithNullEncoding));
		assertEquals("encoded path segment should equal '/path/1' for invalid encoding", "/path/1", taxonController.encodeUrlPathSegment("/path/1", requestWithInvalidEncoding));
	}

	@Test
	public void testHandleConversionException() {
		ConversionException ce = new ConversionException("MESSAGE") {};
		assertEquals("handleConversionException should return 'MESSAGE'", "MESSAGE", taxonController.handleConversionException(ce));
	}

	@Test
	public void testPopulateEditForm() {
		Model uiModel = new ExtendedModelMap();
		Taxon taxon = new Taxon();		
		taxonController.populateEditForm(uiModel, taxon);
		assertEquals("uiModel should contain the result", uiModel.asMap().get("result"), taxon);
		assertEquals("uiModel should contain a list of nomenclatural codes", uiModel.asMap().get("nomenclaturalcodes"), Arrays.asList(NomenclaturalCode.values()));
		assertEquals("uiModel should contain a list of ranks", uiModel.asMap().get("ranks"), Arrays.asList(Rank.values()));
		assertEquals("uiModel should contain a list of taxonomic statuses", uiModel.asMap().get("taxonomicstatuses"), Arrays.asList(TaxonomicStatus.values()));
		assertEquals("uiModel should contain a list of nomenclatural statuses", uiModel.asMap().get("nomenclaturalstatuses"), Arrays.asList(NomenclaturalStatus.values()));
	}

}
