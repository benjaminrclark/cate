package org.cateproject.web.features;

import javax.validation.Valid;

import org.ff4j.FF4j;
import org.ff4j.core.Feature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/system/features")
public class FeaturesController {
	
	private static Logger logger = LoggerFactory.getLogger(FeaturesController.class);
	
	@Autowired
	private FF4j ff4j;
	
	public void setFF4j(FF4j ff4j) {
		this.ff4j = ff4j;
	}
	
	
	@RequestMapping(produces = "text/html")
    public String list(Model uiModel) {
       FeaturesForm result = new FeaturesForm();
       for(String key : ff4j.getFeatures().keySet()) {
    	   result.getFeatures().put(key, ff4j.check(key));
       }
       uiModel.addAttribute("result", result);
       return "system/features/list";
    }
	
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String update(@Valid @ModelAttribute("result") FeaturesForm result, BindingResult bindingResult, Model uiModel, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
            uiModel.addAttribute("result", result);
            String[] codes = new String[] { "features_update_error" };
    		DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, new Object[]{});
    		uiModel.addAttribute("error", message);
            return "system/features/list";
        }
        uiModel.asMap().clear();
        for(String key : result.getFeatures().keySet()) {
        	Boolean value = result.getFeatures().get(key);
        	Boolean enabled = ff4j.check(key);
        	if(value != null && value != enabled) {
        		if(enabled) {
        		    ff4j.disable(key);
        		} else {
        			ff4j.enable(key);
        		}
        		
        	} else if(value == null && enabled) {
        		ff4j.disable(key);
        	}
        }
        String[] codes = new String[] { "features_updated" };
		DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, new Object[] {});
		redirectAttributes.addFlashAttribute("success", message);
        return "redirect:/system/features";
    }
}
