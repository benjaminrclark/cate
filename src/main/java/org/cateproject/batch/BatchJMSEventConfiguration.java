package org.cateproject.batch;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.jms.DynamicJmsTemplate;
import org.springframework.integration.jms.JmsDestinationPollingSource;
import org.springframework.integration.jms.JmsSendingMessageHandler;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.messaging.MessageHandler;

@Configuration
@Profile("default")
public class BatchJMSEventConfiguration {

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private ConnectionFactory connectionFactory;

    @Bean
    public Destination jobLaunchRequestQueue() {
        return new ActiveMQQueue("cate.jobLaunchRequests");
    }

    @Bean
    @InboundChannelAdapter(value = "incomingJobLaunchRequests", poller = @Poller(fixedRate = "5000"))
    public MessageSource<Object> inboundJobLaunchRequestHandler() {
	JmsTemplate jmsTemplate = new DynamicJmsTemplate();
	jmsTemplate.setMessageConverter(messageConverter);
	jmsTemplate.setConnectionFactory(connectionFactory);
	JmsDestinationPollingSource jmsDestinationPollingSource = new JmsDestinationPollingSource(jmsTemplate);
	jmsDestinationPollingSource.setDestination(jobLaunchRequestQueue());
	return jmsDestinationPollingSource;
    }
	
    @Bean
    @ServiceActivator(inputChannel = "outgoingJobLaunchRequests")
    public MessageHandler outboundJobLaunchRequestHandler() {
        JmsTemplate jmsTemplate = new DynamicJmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory);
        jmsTemplate.setMessageConverter(messageConverter);
        JmsSendingMessageHandler messageHandler = new JmsSendingMessageHandler(jmsTemplate);
        messageHandler.setDestination(jobLaunchRequestQueue());
        return messageHandler;
    }

}
