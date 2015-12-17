package org.cateproject.multitenant.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.core.env.ResourceIdResolver;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSns;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

import com.amazonaws.services.sns.AmazonSNS;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@Profile("aws")
@EnableSns
public class MultitenantAWSEventConfiguration {

        @Autowired
	private AmazonSNS amazonSNS;

        @Autowired
        private ResourceIdResolver resourceIdResolver;

        @Autowired
        private ObjectMapper objectMapper;

        @Value("${cloudformation.topic.logicalName:'CATETopic'}")
        private String logicalTopicName;

        @Bean
        public MappingJackson2MessageConverter messageConverter() {
            MappingJackson2MessageConverter messageConverter =  new MappingJackson2MessageConverter();
            messageConverter.setObjectMapper(objectMapper);
            return messageConverter;
        }

	@Bean
	@ServiceActivator(inputChannel = "outgoingTenantEvents")
	public MessageHandler outboundTenantEventHandler() {
		NotificationMessagingTemplate snsTemplate = new NotificationMessagingTemplate(amazonSNS);
                String topicArn = resourceIdResolver.resolveToPhysicalResourceId(logicalTopicName);
                snsTemplate.setDefaultDestinationName(topicArn);
                snsTemplate.setMessageConverter(messageConverter());
		SnsSendingMessageHandler messageHandler = new SnsSendingMessageHandler(snsTemplate);
		return messageHandler;
	}
}
