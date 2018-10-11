package org.cateproject.web.edit;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.cateproject.domain.batch.DarwinCorePropertyMap;
import org.cateproject.web.form.Delimiter;
import org.cateproject.web.form.UploadDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.validation.BindingResult;


@Controller
@RequestMapping("/edit")
public class EditController {

    private Logger logger = LoggerFactory.getLogger(EditController.class);

    @RequestMapping(method = RequestMethod.GET, produces = "text/html")
    public String uploadForm(Model uiModel) {
        populateUploadForm(uiModel, new UploadDto());
        logger.info("GET");
        return "edit/show";
    }

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String upload(@Valid UploadDto uploadDto, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {
       logger.info("POST");
       return "redirect:/edit";
    }

    void populateUploadForm(Model uiModel, UploadDto uploadDto) {
        uiModel.addAttribute("uploadDto", uploadDto);
        uiModel.addAttribute("delimiters",Arrays.asList(Delimiter.values()));
        uiModel.addAttribute("extensions", DarwinCorePropertyMap.getAllowedExtensions());
    }
}
