package org.cateproject;

import java.util.HashSet;
import java.util.Set;

import org.cateproject.batch.convert.StringToMapConverter;
import org.cateproject.batch.convert.MapToStringConverter;
import org.cateproject.domain.convert.StringToTermConverter;
import org.cateproject.domain.convert.batch.ArchiveToBatchDatasetConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.web.config.SpringDataWebConfiguration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@EnableAutoConfiguration(exclude = {JpaRepositoriesAutoConfiguration.class, HibernateJpaAutoConfiguration.class,
                                    DataSourceAutoConfiguration.class, SpringDataWebConfiguration.class,
                                    SessionAutoConfiguration.class, ActiveMQAutoConfiguration.class
                                   })
@ComponentScan(basePackages = "org.cateproject", excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = {"org.cateproject.batch.job.*"})})
public class Application
{

	public static void main(String[] args)
	{
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public ConversionServiceFactoryBean conversionService()
	{
		ConversionServiceFactoryBean conversionServiceFactoryBean = new ConversionServiceFactoryBean();
		Set<Converter> converters = new HashSet<Converter>();
		converters.add(new MapToStringConverter());
		converters.add(new StringToMapConverter());
		converters.add(new ArchiveToBatchDatasetConverter());
		converters.add(new StringToTermConverter());
		conversionServiceFactoryBean.setConverters(converters);
		return conversionServiceFactoryBean;
	}

        @Bean
        public LocalValidatorFactoryBean validator() {
                return new LocalValidatorFactoryBean();
        }
}
