package org.cateproject.web;

import java.util.List;

import org.cateproject.domain.Dataset;
import org.cateproject.domain.Reference;
import org.cateproject.domain.Taxon;
import org.cateproject.repository.jpa.DatasetRepository;
import org.cateproject.repository.jpa.ReferenceRepository;
import org.cateproject.repository.jpa.TaxonRepository;
import org.cateproject.web.batch.JobController;
import org.cateproject.web.batch.JobExecutionController;
import org.cateproject.web.edit.MultimediaController;
import org.cateproject.web.edit.TaxonController;
import org.cateproject.web.edit.EditController;
import org.cateproject.web.format.FilterQueryAnnotationFormatterFactory;
import org.cateproject.web.form.UploadDtoToJobLaunchRequestConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.geo.format.DistanceFormatter;
import org.springframework.data.geo.format.PointFormatter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ConditionalOnWebApplication
public class WebConfig extends WebMvcConfigurerAdapter {
	
	@Autowired
	private TaxonRepository taxonRepository;

        @Autowired
        private DatasetRepository datasetRepository;

        @Autowired
        private ReferenceRepository referenceRepository;

        @Value("${max.upload.size:100000000}")
        private Integer maxUploadSize;

        public void setTaxonRepository(TaxonRepository taxonRepository) {
            this.taxonRepository = taxonRepository;
        }

        public void setReferenceRepository(ReferenceRepository referenceRepository) {
            this.referenceRepository = referenceRepository;
        }

        public void setDatasetRepository(DatasetRepository datasetRepository) {
            this.datasetRepository = datasetRepository;
        }
	
	@Bean
	public TaxonController editTaxonController() {
		return new TaxonController();
	}
 
        @Bean
        public MultimediaController editMultimediaController() {
                return new MultimediaController();
        }

        @Bean
        public JobController jobController() {
            return new JobController();
        }

        @Bean
        public JobExecutionController jobExecutionController() {
            return new JobExecutionController();
        }

        @Bean
        public EditController editController() {
            return new EditController();
        }

        @Bean
        public UploadDtoToJobLaunchRequestConverter uploadDtoToJobLaunchRequestConverter() {
            return new UploadDtoToJobLaunchRequestConverter();
        }

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		super.addViewControllers(registry);
		registry.addViewController("/").setViewName("index");
		registry.addViewController("/edit").setViewName("edit/show");
		registry.addViewController("/admin").setViewName("admin/show");
		registry.addViewController("/system").setViewName("system/show");
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		super.addFormatters(registry);
                registry.addFormatterForFieldAnnotation(new FilterQueryAnnotationFormatterFactory()); 
                registry.addFormatter(DistanceFormatter.INSTANCE);
                registry.addFormatter(PointFormatter.INSTANCE);
		registry.addConverter(getLongToTaxonConverter());
		registry.addConverter(getStringToTaxonConverter());
		registry.addConverter(getLongToDatasetConverter());
		registry.addConverter(getStringToDatasetConverter());
		registry.addConverter(getStringToReferenceConverter());
		registry.addConverter(getStringToReferenceConverter());
	}

        /**
         * Because we need to exclude SpringDataWebConfiguration (due to DomainClassConverter not auto-wiring)
         */ 
        @Bean
        PageableHandlerMethodArgumentResolver pageableResolver() {
            return new PageableHandlerMethodArgumentResolver(sortResolver());
        }

        @Bean
        SortHandlerMethodArgumentResolver sortResolver() {
            return new SortHandlerMethodArgumentResolver();
        }

        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
            argumentResolvers.add(pageableResolver());
            argumentResolvers.add(sortResolver());
        }

	public Converter<Long, Taxon> getLongToTaxonConverter() {
	    return new org.springframework.core.convert.converter.Converter<java.lang.Long, org.cateproject.domain.Taxon>() {
                public org.cateproject.domain.Taxon convert(java.lang.Long id) {
                    return taxonRepository.findOne(id);
                }
            };
	}
	
	public Converter<String, Taxon> getStringToTaxonConverter() {
	    return new org.springframework.core.convert.converter.Converter<java.lang.String, org.cateproject.domain.Taxon>() {
                public org.cateproject.domain.Taxon convert(java.lang.String id) {
                    if(id == null || id.isEmpty()) {
                        return null;
                    } else {
                        return taxonRepository.findOne(Long.parseLong(id, 10));
                    }
                }
            };
	}

	public Converter<Long, Dataset> getLongToDatasetConverter() {
	    return new org.springframework.core.convert.converter.Converter<java.lang.Long, org.cateproject.domain.Dataset>() {
                public org.cateproject.domain.Dataset convert(java.lang.Long id) {
                    return datasetRepository.findOne(id);
                }
            };
	}
	
	public Converter<String, Dataset> getStringToDatasetConverter() {
	    return new org.springframework.core.convert.converter.Converter<java.lang.String, org.cateproject.domain.Dataset>() {
                public org.cateproject.domain.Dataset convert(java.lang.String id) {
                    if(id == null || id.isEmpty()) {
                        return null;
                    } else {
                        return datasetRepository.findOne(Long.parseLong(id, 10));
                    }
                }
            };
	}

	public Converter<Long, Reference> getLongToReferenceConverter() {
	    return new org.springframework.core.convert.converter.Converter<java.lang.Long, org.cateproject.domain.Reference>() {
                public org.cateproject.domain.Reference convert(java.lang.Long id) {
                    return referenceRepository.findOne(id);
                }
            };
	}
	
	public Converter<String, Reference> getStringToReferenceConverter() {
	    return new org.springframework.core.convert.converter.Converter<java.lang.String, org.cateproject.domain.Reference>() {
                public org.cateproject.domain.Reference convert(java.lang.String id) {
                    if(id == null || id.isEmpty()) {
                        return null;
                    } else {
                        return referenceRepository.findOne(Long.parseLong(id, 10));
                    }
                }
            };
	}
}
