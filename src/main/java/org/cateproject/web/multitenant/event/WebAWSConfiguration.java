package org.cateproject.web.multitenant.event;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Profile("aws")
@Configuration
public class WebAWSConfiguration extends WebMvcConfigurerAdapter{

    @Bean
    public SnsEndpointController snsEndpointController() {
        return new SnsEndpointController();
    }

    @Bean
    public SnsSubscriptionRegistrar snsSubscriptionRegistrar() {
        return new SnsSubscriptionRegistrar();
    }
}
