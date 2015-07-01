package org.cateproject.web.multitenant;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.cateproject.multitenant.MultitenantRepository;
import org.cateproject.multitenant.domain.Multitenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@Controller
@RequestMapping("/system/multitenant")
public class MultitenantController {
	@Autowired
    MultitenantRepository multitenantRepository;
	
	public void setMultitenantRepository(MultitenantRepository multitenantRepository) {
		this.multitenantRepository = multitenantRepository;		
	}
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Multitenant multitenant, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, multitenant);
            return "system/multitenant/create";
        }
        uiModel.asMap().clear();
        multitenantRepository.save(multitenant);
        return "redirect:/system/multitenant/" + encodeUrlPathSegment(multitenant.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Multitenant());
        return "system/multitenant/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("multitenant", multitenantRepository.findOne(id));
        uiModel.addAttribute("itemId", id);
        return "system/multitenant/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String list(Pageable pageable, Model uiModel) {
       uiModel.addAttribute("multitenants", multitenantRepository.findAll(pageable));
       return "system/multitenant/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Multitenant multitenant, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, multitenant);
            return "system/multitenant/update";
        }
        uiModel.asMap().clear();
        multitenantRepository.save(multitenant);
        return "redirect:/system/multitenant/" + encodeUrlPathSegment(multitenant.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, multitenantRepository.findOne(id));
        return "system/multitenant/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", defaultValue = "0", required = false) Integer page, @RequestParam(value = "size", defaultValue = "10", required = false) Integer size, Model uiModel) {
        Multitenant multitenant = multitenantRepository.findOne(id);
        multitenantRepository.delete(multitenant);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", page.toString());
        uiModel.addAttribute("size", size.toString());
        return "redirect:/system/multitenant";
    }
    
    void populateEditForm(Model uiModel, Multitenant multitenant) {
        uiModel.addAttribute("multitenant", multitenant);
    }
    
    String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
}
