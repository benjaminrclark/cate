package org.cateproject.web.multitenant;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.cateproject.multitenant.MultitenantManager;
import org.cateproject.multitenant.MultitenantProperties;
import org.cateproject.multitenant.domain.Multitenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Component
@RequestMapping("/init")
public class InitController {
	
	@Autowired
	MultitenantProperties multitenantProperties;
	
	@Autowired
	MultitenantManager multitenantManager;
	
	public void setMultitenantManager(MultitenantManager multitenantManager) {
		this.multitenantManager = multitenantManager;
	}
	
	public void setMultitenantProperties(MultitenantProperties multitenantProperties) {
		this.multitenantProperties = multitenantProperties;
		
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Multitenant multitenant, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, multitenant);
            return "init/create";
        }
        uiModel.asMap().clear();
        multitenantManager.save(multitenant, true);
        String[] codes = new String[] { "tenant_created" };
		Object[] args = new Object[] { multitenant.getTitle()};
		DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, args);
		redirectAttributes.addFlashAttribute("success", message);
        return "redirect:/tenant";
    }	

	@RequestMapping(produces = "text/html")
	public String createForm(Model uiModel) {
		Multitenant multitenant = new Multitenant();
		multitenant.setIdentifier(multitenantProperties.getDefaultIdentifier());
		multitenant.setTitle(multitenantProperties.getDefaultTitle());
		multitenant.setAdminEmail(multitenantProperties.getDefaultAdminUsername());
		multitenant.setOwnerEmail(multitenantProperties.getDefaultOwnerUsername());
		multitenant.setDatabasePassword(multitenantProperties.getDatabasePassword());
		multitenant.setDatabaseUsername(multitenantProperties.getDatabaseUsername());
		multitenant.setDatabaseUrl(multitenantProperties.getDatabaseUrl());
		multitenant.setDriverClassName(multitenantProperties.getDatabaseDriverClassname());
		populateEditForm(uiModel, multitenant);
		String[] codes = new String[] { "initialize_cate" };
		Object[] args = new Object[] {};
		DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, args);
		uiModel.addAttribute("info", message);
		return "init/create";
	}
	
	void populateEditForm(Model uiModel, Multitenant multitentant) {
        uiModel.addAttribute("multitenant", multitentant);
    }
}
