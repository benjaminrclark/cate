package org.cateproject.web.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/login")
public class LoginController {
	
	@RequestMapping(method = RequestMethod.GET, produces = "text/html")
    public String createForm(Model uiModel) {
        return "login";
    }

}
