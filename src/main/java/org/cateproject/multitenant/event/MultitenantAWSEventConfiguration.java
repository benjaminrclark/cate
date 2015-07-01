package org.cateproject.multitenant.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSns;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

import com.amazonaws.services.sns.AmazonSNS;

@Configuration
@Profile("aws")
@EnableSns
public class MultitenantAWSEventConfiguration {

        @Autowired
	AmazonSNS amazonSNS;

	@Value("${aws.sns.default.topicName}")
        String defaultDestinationName;

        @Bean
        public MappingJackson2MessageConverter messageConverter() {
            return new MappingJackson2MessageConverter();
        }

	@Bean
	@ServiceActivator(inputChannel = "outgoingTenantEvents")
	public MessageHandler outboundTenantEventHandler() {
		NotificationMessagingTemplate snsTemplate = new NotificationMessagingTemplate(amazonSNS);
                snsTemplate.setDefaultDestinationName(defaultDestinationName);
                snsTemplate.setMessageConverter(messageConverter());
		SnsSendingMessageHandler messageHandler = new SnsSendingMessageHandler(snsTemplate);
		return messageHandler;
	}
}
