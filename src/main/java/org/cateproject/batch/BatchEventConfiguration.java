package org.cateproject.batch;

import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.handler.ServiceActivatingHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class BatchEventConfiguration {
  
    @Autowired
    private JobLauncher jobLauncher;
    
    @Bean
    public MessageChannel outgoingJobLaunchRequests() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel incomingJobLaunchRequests() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel="incomingJobLaunchRequests")
    public MessageHandler incomingJobLaunchRequestHandler() {
        JobLaunchRequestHandler jobLaunchRequestHandler = new JobLaunchingMessageHandler(jobLauncher);
	return new ServiceActivatingHandler(jobLaunchRequestHandler, "launch");
    }
}
