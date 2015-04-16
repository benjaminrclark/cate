package org.cateproject.multitenant.event;

import java.util.HashMap;
import java.util.Map;

import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.jms.DynamicJmsTemplate;
import org.springframework.integration.jms.JmsDestinationPollingSource;
import org.springframework.integration.jms.JmsSendingMessageHandler;
import org.springframework.integration.router.ErrorMessageExceptionTypeRouter;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
@EnableIntegration
@IntegrationComponentScan({"org.cateproject.multitenant"})
public class MultitenantEventConfiguration {
	
	@Autowired
	ConnectionFactory connectionFactory;
	
	@Bean
	public MessageChannel errorChannel() {
	   return new DirectChannel();
	}
	
	@Bean
	public MessageChannel defaultErrorChannel() {
	   return new DirectChannel();
	}
	
	@Bean
	public ErrorMessageExceptionTypeRouter exceptionRouter() {
		ErrorMessageExceptionTypeRouter errorMessageExceptionTypeRouter = new ErrorMessageExceptionTypeRouter();
		errorMessageExceptionTypeRouter.setDefaultOutputChannel(defaultErrorChannel());
		return errorMessageExceptionTypeRouter;
	}
	
	@Bean
	public MessageChannel outgoingTenantEvents() {
	   return new DirectChannel();
	}
	
	@Bean
	public MessageChannel localTenantEvents() {
	   return new PublishSubscribeChannel();
	}
	
	
	@Bean
	public MessageChannel incomingTenantEvents() {
	   return new PublishSubscribeChannel();
	}
	
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
