package org.cateproject.multitenant.event;

import org.cateproject.batch.SQSMessagePollingSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.core.MessageSource;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.converter.MessageConverter;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sqs.AmazonSQS;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@Profile("aws")
public class MultitenantAWSEventConfiguration {

        @Autowired
	private AmazonSNS amazonSNS;

        @Autowired
        private AmazonSQS amazonSQS;

        @Autowired
        private ObjectMapper objectMapper;

        @Value("${cloudformation.topicArn}")
        private String topicArn;

        @Autowired
        MessageConverter messageConverter;

        @Bean
        public SnsRegistrar snsRegistrar() {
            return new SnsRegistrar(amazonSNS, amazonSQS, topicArn);
        }

	@Bean
	@ServiceActivator(inputChannel = "outgoingTenantEvents")
	public MessageHandler outboundTenantEventHandler() {
		NotificationMessagingTemplate snsTemplate = new NotificationMessagingTemplate(amazonSNS);
                snsTemplate.setDefaultDestinationName(topicArn);
                snsTemplate.setMessageConverter(messageConverter);
		SnsSendingMessageHandler messageHandler = new SnsSendingMessageHandler(snsTemplate);
		return messageHandler;
	}

        @Bean
        public QueueMessagingTemplate tenantQueueMessagingTemplate() {
            QueueMessagingTemplate tenantQueueMessagingTemplate = new QueueMessagingTemplate(amazonSQS);

            tenantQueueMessagingTemplate.setDefaultDestinationName(snsRegistrar().getQueueArn());
            tenantQueueMessagingTemplate.setMessageConverter(messageConverter);
            return tenantQueueMessagingTemplate;
        }

        @Bean
        @InboundChannelAdapter(value = "incomingTenantEvents", poller = @Poller(fixedRate = "5000"))
        public MessageSource<MultitenantEvent> inboundMultitenantEventHandler() {
            SQSMessagePollingSource<MultitenantEvent> sqsMessagePollingSource = new SQSMessagePollingSource<MultitenantEvent>(tenantQueueMessagingTemplate(), MultitenantEvent.class);
	    return sqsMessagePollingSource;
        }
}
