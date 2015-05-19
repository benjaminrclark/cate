package org.cateproject.web;

import org.cateproject.domain.Taxon;
import org.cateproject.repository.jpa.TaxonRepository;
import org.cateproject.web.edit.TaxonController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ConditionalOnWebApplication
public class WebConfig extends WebMvcConfigurerAdapter {
	
	@Autowired
	private TaxonRepository taxonRepository;
	
	@Bean
	public TaxonController editTaxonController() {
		return new TaxonController();
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		super.addViewControllers(registry);
		registry.addViewController("/").setViewName("index");
		registry.addViewController("/edit").setViewName("edit/show");
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		super.addFormatters(registry);
		registry.addConverter(getIdToTaxonConverter());
	}

	private Converter<Long, Taxon> getIdToTaxonConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Long, org.cateproject.domain.Taxon>() {
            public org.cateproject.domain.Taxon convert(java.lang.Long id) {
                return taxonRepository.findOne(id);
            }
        };
	}
	
	

}
