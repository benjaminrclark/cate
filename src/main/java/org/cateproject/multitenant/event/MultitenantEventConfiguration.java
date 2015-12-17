package org.cateproject.multitenant.event;

import org.cateproject.batch.convert.BatchJobDeserializer;
import org.cateproject.batch.convert.BatchJobSerializer;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.router.ErrorMessageExceptionTypeRouter;
import org.springframework.messaging.MessageChannel;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Configuration
@EnableIntegration
@IntegrationComponentScan({"org.cateproject.multitenant","org.cateproject.batch"})
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

        @Bean
        public BatchJobSerializer batchJobSerializer() {
            return new BatchJobSerializer();
        }

        @Bean
        public BatchJobDeserializer batchJobDeserializer() {
            return new BatchJobDeserializer();
        }

        @Bean
        public ObjectMapper objectMapper() {
            ObjectMapper objectMapper = new ObjectMapper();
            SimpleModule module = new SimpleModule("SpringBatchJobModule", new Version(0, 1, 0, "alpha"));
            module.addSerializer(JobLaunchRequest.class, batchJobSerializer());
            module.addDeserializer(JobLaunchRequest.class, batchJobDeserializer());
            objectMapper.registerModule(module);
            return objectMapper;
        }
}
