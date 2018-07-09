package org.cateproject.batch;

import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import com.amazonaws.services.sqs.AmazonSQS;

@Profile("aws")
@Configuration
public class BatchAWSEventConfiguration {

    @Value("${cloudformation.queueArn}")
    private String queueArn;

    @Autowired
    private AmazonSQS amazonSQS;

    @Autowired
    private MessageConverter messageConverter;

    @Bean
    public QueueMessagingTemplate jobQueueMessagingTemplate() {
        QueueMessagingTemplate jobQueueMessagingTemplate = new QueueMessagingTemplate(amazonSQS);

        jobQueueMessagingTemplate.setDefaultDestinationName(queueArn);
        jobQueueMessagingTemplate.setMessageConverter(messageConverter);
        return jobQueueMessagingTemplate;
    }

    @Bean
    @InboundChannelAdapter(value = "incomingJobLaunchRequests", poller = @Poller(fixedRate = "5000"))
    public MessageSource<JobLaunchRequest> inboundJobLaunchRequestHandler() {
        SQSMessagePollingSource<JobLaunchRequest> sqsMessagePollingSource = new SQSMessagePollingSource<JobLaunchRequest>(jobQueueMessagingTemplate(), JobLaunchRequest.class);
	return sqsMessagePollingSource;
    }
	
    @Bean
    @ServiceActivator(inputChannel = "outgoingJobLaunchRequests")
    public MessageHandler outboundJobLaunchRequestHandler() {
        SQSSendingMessageHandler messageHandler = new SQSSendingMessageHandler(jobQueueMessagingTemplate());
        return messageHandler;
    }

}
