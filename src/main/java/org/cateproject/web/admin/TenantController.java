package org.cateproject.web.admin;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.cateproject.domain.admin.Tenant;
import org.cateproject.repository.jpa.admin.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/tenant")
public class TenantController {
    
    @Autowired
    TenantRepository tenantRepository;
	
    public void setTenantRepository(TenantRepository tenantRepository) {
	this.tenantRepository = tenantRepository;
    }
        
    @RequestMapping(produces = "text/html")
    public String show(Model uiModel) {
        uiModel.addAttribute("tenant", tenantRepository.findOne(1L));
        uiModel.addAttribute("itemId", 1L);
        return "tenant/show";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Tenant tenant, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, tenant);
            return "tenant/update";
        }
        uiModel.asMap().clear();
        tenantRepository.save(tenant);
        return "redirect:/tenant";
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String updateForm(Model uiModel) {
        populateEditForm(uiModel, tenantRepository.findOne(1L));
        return "tenant/update";
    }
    
    void populateEditForm(Model uiModel, Tenant tenant) {
        uiModel.addAttribute("tenant", tenant);
    }
}
