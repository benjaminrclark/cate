package org.cateproject.web.edit;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.cateproject.domain.batch.DarwinCorePropertyMap;
import org.cateproject.domain.batch.JobLaunchRequest;
import org.cateproject.batch.JobLaunchRequestHandler;
import org.cateproject.repository.jpa.batch.JobLaunchRequestRepository;
import org.cateproject.web.form.Delimiter;
import org.cateproject.web.form.UploadDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;


@Controller
@RequestMapping("/edit")
public class EditController {

    private Logger logger = LoggerFactory.getLogger(EditController.class);

    @Autowired
    private ConversionService conversionService;

    @Autowired
    @Qualifier("remoteJobLaunchRequests")
    private JobLaunchRequestHandler jobLaunchRequestHandler;

    @Autowired
    private JobLaunchRequestRepository jobLaunchRequestRepository;

    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    public void setJobLaunchRequestHandler(JobLaunchRequestHandler jobLaunchRequestHandler) {
        this.jobLaunchRequestHandler = jobLaunchRequestHandler;
    }

    public void setJobLaunchRequestRepository(JobLaunchRequestRepository jobLaunchRequestRepository) {
        this.jobLaunchRequestRepository = jobLaunchRequestRepository;
    }

    @RequestMapping(method = RequestMethod.GET, produces = "text/html")
    public String uploadForm(Model uiModel) {
        populateUploadForm(uiModel, new UploadDto());
        return "edit/show";
    }

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String upload(@Valid UploadDto result, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            populateUploadForm(uiModel, result);
            String[] codes = new String[] { "upload_error" };
    	    DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, new Object[] {});
    	    uiModel.addAttribute("error", message);
            return "edit/show";
        }
       JobLaunchRequest jobLaunchRequest = conversionService.convert(result, JobLaunchRequest.class);
       jobLaunchRequestRepository.save(jobLaunchRequest);
       jobLaunchRequestHandler.launch(jobLaunchRequest);
       String[] codes = new String[] { "upload_accepted" };
       DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, new Object[] {});
       redirectAttributes.addFlashAttribute("success", message);
       return "redirect:/edit";
    }

    void populateUploadForm(Model uiModel, UploadDto uploadDto) {
        uiModel.addAttribute("uploadDto", uploadDto);
        uiModel.addAttribute("delimiters",Arrays.asList(Delimiter.values()));
        uiModel.addAttribute("extensions", DarwinCorePropertyMap.getAllowedExtensions());
    }
}
