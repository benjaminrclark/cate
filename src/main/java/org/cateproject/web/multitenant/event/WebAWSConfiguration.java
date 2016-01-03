package org.cateproject.web.multitenant.event;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.endpoint.NotificationMessageHandlerMethodArgumentResolver;
import org.springframework.cloud.aws.messaging.endpoint.NotificationStatusHandlerMethodArgumentResolver;
import org.springframework.cloud.aws.messaging.endpoint.NotificationSubjectHandlerMethodArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.amazonaws.services.sns.AmazonSNS;

@Profile("aws")
@Configuration
public class WebAWSConfiguration extends WebMvcConfigurerAdapter{

    @Autowired
    private AmazonSNS amazonSns;

    @Bean
    public SnsEndpointController snsEndpointController() {
        return new SnsEndpointController();
    }

    @Bean
    public SnsSubscriptionRegistrar snsSubscriptionRegistrar() {
        return new SnsSubscriptionRegistrar();
    }

    @Bean
    public NotificationStatusHandlerMethodArgumentResolver notificationStatusHandlerMethodArgumentResolver() {
        return new NotificationStatusHandlerMethodArgumentResolver(amazonSns);
    }

    @Bean
    public NotificationMessageHandlerMethodArgumentResolver notificationMessageHandlerMethodArgumentResolver() {
        return new NotificationMessageHandlerMethodArgumentResolver();
    }

    @Bean
    public NotificationSubjectHandlerMethodArgumentResolver notificationSubjectHandlerMethodArgumentResolver() {
        return new NotificationSubjectHandlerMethodArgumentResolver();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(notificationStatusHandlerMethodArgumentResolver());
        argumentResolvers.add(notificationMessageHandlerMethodArgumentResolver());
        argumentResolvers.add(notificationSubjectHandlerMethodArgumentResolver());
    }
}
