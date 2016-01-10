package org.cateproject;

import org.cateproject.multitenant.event.SnsRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.context.config.annotation.EnableContextCredentials;
import org.springframework.cloud.aws.context.config.annotation.EnableContextInstanceData;
import org.springframework.cloud.aws.context.config.annotation.EnableContextRegion;
import org.springframework.cloud.aws.context.config.annotation.EnableStackConfiguration;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSns;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sqs.AmazonSQS;
import com.fasterxml.jackson.databind.ObjectMapper;

@Profile("aws")
@Configuration
@EnableContextCredentials(instanceProfile=true)
@EnableContextRegion(autoDetect=true)
@EnableContextInstanceData
@EnableStackConfiguration
@EnableSqs
@EnableSns
public class AWSConfiguration {

        @Autowired
	private AmazonSNS amazonSNS;

        @Autowired
        private AmazonSQS amazonSQS;

        @Value("${cloudformation.topicArn}")
        private String topicArn;

        @Autowired
        private ObjectMapper objectMapper;

        @Bean
        public SnsRegistrar snsRegistrar() {
            return new SnsRegistrar(amazonSNS, amazonSQS, topicArn);
        }

        @Bean
        public MappingJackson2MessageConverter messageConverter() {
            MappingJackson2MessageConverter messageConverter =  new MappingJackson2MessageConverter();
            messageConverter.setObjectMapper(objectMapper);
            return messageConverter;
        }
}
