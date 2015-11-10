package org.cateproject.web.edit;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.cateproject.domain.Multimedia;
import org.cateproject.domain.constants.DCMIType;
import org.cateproject.repository.jpa.MultimediaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.convert.ConversionException;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping("/edit/multimedia")
public class MultimediaController {

    private static Logger logger = LoggerFactory.getLogger(MultimediaController.class);
	
    @Autowired
    MultimediaRepository multimediaRepository;
	
    @Autowired
    @Qualifier("mvcConversionService")
    private ConversionService conversionService;
    
    public void setMultimediaRepository(MultimediaRepository multimediaRepository) {
        this.multimediaRepository = multimediaRepository;	
    }
    
    public void setConversionService(ConversionService conversionService) {
		this.conversionService = conversionService;	
	}

    @RequestMapping(produces = "text/html")
    public String list(Pageable pageable, Model uiModel) {        
        Page<Multimedia> results = multimediaRepository.findAll(pageable);
        uiModel.addAttribute("types", Arrays.asList(DCMIType.values()));
        uiModel.addAttribute("results", results);
        return "edit/multimedia/list";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH, produces = "text/html")
    public String partial(@PathVariable("id") Long id,
    		              @RequestParam Map<String, String> params,
    		              Model uiModel, HttpServletRequest httpServletRequest,
    		              RedirectAttributes redirectAttributes) {
    	Multimedia result = multimediaRepository.findOne(id);
    	BeanWrapper beanWrapper = new BeanWrapperImpl(result);
    	for(String property : params.keySet()) {
    	  Class propertyType = beanWrapper.getPropertyType(property);
    	  beanWrapper.setPropertyValue(property, conversionService.convert(params.get(property), propertyType));
    	}
        uiModel.asMap().clear();
        // TODO Validate
        multimediaRepository.save(result);
        String[] codes = new String[] { "entity_updated" };
		Object[] args = new Object[] { result.toString()};
		DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, args);
		redirectAttributes.addFlashAttribute("success", message);
        return "redirect:/edit/multimedia/" + encodeUrlPathSegment(result.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Multimedia result, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            populateEditForm(uiModel, result);
            String[] codes = new String[] { "entity_create_error" };
    		Object[] args = new Object[] { result.toString()};
    		DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, args);
    		uiModel.addAttribute("error", message);
            return "edit/multimedia/create";
        }
        uiModel.asMap().clear();
        multimediaRepository.save(result);
        String[] codes = new String[] { "entity_created" };
		Object[] args = new Object[] { result.toString()};
		DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, args);
		redirectAttributes.addFlashAttribute("success", message);
        return "redirect:/edit/multimedia/" + encodeUrlPathSegment(result.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Multimedia());
        return "edit/multimedia/create";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("result", multimediaRepository.findOne(id));
        return "edit/multimedia/show";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.POST, produces = "text/html")
    public String update(@PathVariable("id") Long id,@Valid @ModelAttribute("result") Multimedia result, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, result);
            String[] codes = new String[] { "entity_update_error" };
    		Object[] args = new Object[] { result.toString()};
    		DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, args);
    		uiModel.addAttribute("error", message);
            return "edit/multimedia/update";
        }
        uiModel.asMap().clear();
        multimediaRepository.save(result);
        String[] codes = new String[] { "entity_updated" };
		Object[] args = new Object[] { result.toString()};
		DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, args);
		redirectAttributes.addFlashAttribute("success", message);
        return "redirect:/edit/multimedia/" + encodeUrlPathSegment(result.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, multimediaRepository.findOne(id));
        return "edit/multimedia/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false, defaultValue = "0") Integer page, @RequestParam(value = "size", required = false, defaultValue = "10") Integer size, Model uiModel, RedirectAttributes redirectAttributes) {
        Multimedia result = multimediaRepository.findOne(id);
        multimediaRepository.delete(result);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", page);
        uiModel.addAttribute("size", size);
        String[] codes = new String[] { "entity_deleted" };
		Object[] args = new Object[] { result.toString()};
		DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, args);
		redirectAttributes.addFlashAttribute("success", message);
        return "redirect:/edit/multimedia";
    }
    
    String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        } try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }

    
    @ExceptionHandler(ConversionException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public  @ResponseBody String handleConversionException(ConversionException ce) {
        return ce.getMessage();
    }

    void populateEditForm(Model uiModel, Multimedia result) {
        uiModel.addAttribute("result", result);
        uiModel.addAttribute("types", Arrays.asList(DCMIType.values()));
    }
}
