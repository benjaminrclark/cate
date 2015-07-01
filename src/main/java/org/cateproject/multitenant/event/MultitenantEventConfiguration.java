package org.cateproject.multitenant.event;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.router.ErrorMessageExceptionTypeRouter;
import org.springframework.messaging.MessageChannel;

@Configuration
@EnableIntegration
@IntegrationComponentScan({"org.cateproject.multitenant"})
public class MultitenantEventConfiguration {
	
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
}
