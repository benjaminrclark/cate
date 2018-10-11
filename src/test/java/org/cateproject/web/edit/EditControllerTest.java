package org.cateproject.web.edit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.cateproject.domain.batch.DarwinCorePropertyMap;
import org.cateproject.domain.batch.JobLaunchRequest;
import org.cateproject.web.form.Delimiter;
import org.cateproject.batch.JobLaunchRequestHandler;
import org.cateproject.repository.jpa.batch.JobLaunchRequestRepository;
import org.cateproject.web.form.UploadDto;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.Before;
import org.junit.Test;

import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.JobParameters;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.core.convert.ConversionException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.ConversionService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

public class EditControllerTest {
	
	private EditController editController;

        private ConversionService conversionService;

        private JobLaunchRequestHandler jobLaunchRequestHandler;

        private JobLaunchRequestRepository jobLaunchRequestRepository;

	@Before
	public void setUp() throws Exception {
            editController = new EditController();
            conversionService = EasyMock.createMock(ConversionService.class);
            jobLaunchRequestHandler = EasyMock.createMock(JobLaunchRequestHandler.class);
            jobLaunchRequestRepository = EasyMock.createMock(JobLaunchRequestRepository.class);

            editController.setConversionService(conversionService);
            editController.setJobLaunchRequestHandler(jobLaunchRequestHandler);
            editController.setJobLaunchRequestRepository(jobLaunchRequestRepository);
	}

	@Test
	public void testUploadForm() {
		Model uiModel = new ExtendedModelMap();
		
		assertEquals("list should return 'edit/show'", "edit/show", editController.uploadForm(uiModel));
		assertEquals("uiModel should contain an upload dto", uiModel.asMap().get("uploadDto").getClass(), UploadDto.class);
		
		assertEquals("uiModel should contain a list of allowed extensions", uiModel.asMap().get("extensions"), DarwinCorePropertyMap.getAllowedExtensions());
		assertEquals("uiModel should contain a list of allowed delimiters", uiModel.asMap().get("delimiters"), Arrays.asList(Delimiter.values()));
	}

	@Test
	public void testUploadSuccess() {
		Model uiModel = new ExtendedModelMap();
		MockHttpServletRequest request = new MockHttpServletRequest();
		RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
		BindingResult bindingResult = EasyMock.createMock(BindingResult.class);
		UploadDto uploadDto = new UploadDto();
                JobLaunchRequest jobLaunchRequest = new JobLaunchRequest(new SimpleJob("job"), new JobParameters());
	
		EasyMock.expect(bindingResult.hasErrors()).andReturn(false);
                EasyMock.expect(conversionService.convert(EasyMock.eq(uploadDto), EasyMock.eq(JobLaunchRequest.class))).andReturn(jobLaunchRequest);
                EasyMock.expect(jobLaunchRequestRepository.save(EasyMock.eq(jobLaunchRequest))).andReturn(jobLaunchRequest);
                jobLaunchRequestHandler.launch(jobLaunchRequest);
                
		EasyMock.replay(bindingResult, conversionService, jobLaunchRequestHandler, jobLaunchRequestRepository);
		
		assertEquals("upload should return 'redirect:/edit'", "redirect:/edit", editController.upload(uploadDto, bindingResult, uiModel, request, redirectAttributes));
		assertTrue("uiModel should be empty", uiModel.asMap().isEmpty());
		assertEquals("success message code should be 'upload_accepted'", "upload_accepted", ((MessageSourceResolvable)redirectAttributes.getFlashAttributes().get("success")).getCodes()[0]);
		EasyMock.verify(bindingResult, conversionService, jobLaunchRequestHandler, jobLaunchRequestRepository);
	}

	@Test
	public void testUploadValidationError() {
		Model uiModel = new ExtendedModelMap();
		MockHttpServletRequest request = new MockHttpServletRequest();
		RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
		BindingResult bindingResult = EasyMock.createMock(BindingResult.class);
		UploadDto uploadDto = new UploadDto();
		EasyMock.expect(bindingResult.hasErrors()).andReturn(true);

		EasyMock.replay(bindingResult, conversionService, jobLaunchRequestHandler, jobLaunchRequestRepository);
		
		assertEquals("upload should return 'edit/show'", "edit/show", editController.upload(uploadDto, bindingResult, uiModel, request, redirectAttributes));
		assertEquals("uiModel should contain the uploadDto", uiModel.asMap().get("uploadDto"), uploadDto);
		assertEquals("error message code should be 'upload_error'", "upload_error", ((MessageSourceResolvable)uiModel.asMap().get("error")).getCodes()[0]);
		assertEquals("uiModel should contain a list of allowed extensions", uiModel.asMap().get("extensions"), DarwinCorePropertyMap.getAllowedExtensions());
		assertEquals("uiModel should contain a list of allowed delimiters", uiModel.asMap().get("delimiters"), Arrays.asList(Delimiter.values()));
		EasyMock.verify(bindingResult, conversionService, jobLaunchRequestHandler, jobLaunchRequestRepository);
	}
}
