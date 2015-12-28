package org.cateproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.context.config.annotation.EnableContextCredentials;
import org.springframework.cloud.aws.context.config.annotation.EnableContextInstanceData;
import org.springframework.cloud.aws.context.config.annotation.EnableContextRegion;
import org.springframework.cloud.aws.context.config.annotation.EnableStackConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

import com.fasterxml.jackson.databind.ObjectMapper;

@Profile("aws")
@Configuration
@EnableContextCredentials(instanceProfile=true)
@EnableContextRegion(autoDetect=true)
@EnableContextInstanceData
@EnableStackConfiguration
public class AWSConfiguration {

        @Autowired
        private ObjectMapper objectMapper;

        @Bean
        public MappingJackson2MessageConverter messageConverter() {
            MappingJackson2MessageConverter messageConverter =  new MappingJackson2MessageConverter();
            messageConverter.setObjectMapper(objectMapper);
            return messageConverter;
        }
}
