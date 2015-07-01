package org.cateproject.multitenant.event;

import java.util.HashMap;
import java.util.Map;

import javax.jms.ConnectionFactory;

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
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.messaging.MessageHandler;

@Configuration
@Profile("default")
public class MultitenantJMSEventConfiguration {
	
        @Autowired
	private ConnectionFactory connectionFactory;

	@Bean
	@InboundChannelAdapter(value = "incomingTenantEvents", poller = @Poller(fixedRate = "5000"))
	public MessageSource<Object> inboundTenantEventHandler() {
		JmsTemplate jmsTemplate = new DynamicJmsTemplate();
		jmsTemplate.setMessageConverter(messageConverter());
		jmsTemplate.setConnectionFactory(connectionFactory);
		JmsDestinationPollingSource jmsDestinationPollingSource = new JmsDestinationPollingSource(jmsTemplate);
		jmsDestinationPollingSource.setDestinationName("cate.tenantEvents");
		return jmsDestinationPollingSource;
		
	}
	
	@Bean
	public MessageConverter messageConverter() {
		MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
		messageConverter.setTargetType(MessageType.TEXT);
		messageConverter.setTypeIdPropertyName("javaType");
		Map<String, Class<?>> typeIdMappings = new HashMap<String, Class<?>>();
		typeIdMappings.put("MultitenantEvent", MultitenantEvent.class);
		messageConverter.setTypeIdMappings(typeIdMappings);
		return messageConverter;
	}

	@Bean
	@ServiceActivator(inputChannel = "outgoingTenantEvents")
	public MessageHandler outboundTenantEventHandler() {
		JmsTemplate jmsTemplate = new DynamicJmsTemplate();
		jmsTemplate.setConnectionFactory(connectionFactory);
		jmsTemplate.setMessageConverter(messageConverter());
		JmsSendingMessageHandler messageHandler = new JmsSendingMessageHandler(jmsTemplate);
		messageHandler.setDestinationName("cate.tenantEvents");
		return messageHandler;
	}
}
