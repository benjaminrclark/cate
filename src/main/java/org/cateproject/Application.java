package org.cateproject;

import java.util.HashSet;
import java.util.Set;

import org.cateproject.batch.convert.StringToMapConverter;
import org.cateproject.batch.convert.MapToStringConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.web.config.SpringDataWebConfiguration;

@Configuration
@EnableAutoConfiguration(exclude = {JpaRepositoriesAutoConfiguration.class, HibernateJpaAutoConfiguration.class, DataSourceAutoConfiguration.class, SpringDataWebConfiguration.class})
@ComponentScan("org.cateproject")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ConversionServiceFactoryBean conversionService() {
        ConversionServiceFactoryBean conversionServiceFactoryBean = new ConversionServiceFactoryBean();
        Set<Converter> converters = new HashSet<Converter>();
        converters.add(new MapToStringConverter());
        converters.add(new StringToMapConverter());
        conversionServiceFactoryBean.setConverters(converters);
        return conversionServiceFactoryBean;
    }
}


