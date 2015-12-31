package org.cateproject.batch;

import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.core.env.ResourceIdResolver;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
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
@EnableSqs
@Configuration
public class BatchAWSEventConfiguration {

    @Autowired
    private ResourceIdResolver resourceIdResolver;

    @Value("${cloudformation.queueName:'CATEQueue'}")
    private String queueName;

    @Autowired
    private AmazonSQS amazonSQS;

    @Autowired
    private MessageConverter messageConverter;

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate() {
        QueueMessagingTemplate queueMessagingTemplate = new QueueMessagingTemplate(amazonSQS);

        String queueArn = resourceIdResolver.resolveToPhysicalResourceId(queueName);
        queueMessagingTemplate.setDefaultDestinationName(queueArn);
        queueMessagingTemplate.setMessageConverter(messageConverter);
        return queueMessagingTemplate;
    }

    @Bean
    @InboundChannelAdapter(value = "incomingJobLaunchRequests", poller = @Poller(fixedRate = "5000"))
    public MessageSource<JobLaunchRequest> inboundJobLaunchRequestHandler() {
        SQSMessagePollingSource<JobLaunchRequest> sqsMessagePollingSource = new SQSMessagePollingSource<JobLaunchRequest>(queueMessagingTemplate(), JobLaunchRequest.class);
	return sqsMessagePollingSource;
    }
	
    @Bean
    @ServiceActivator(inputChannel = "outgoingJobLaunchRequests")
    public MessageHandler outboundJobLaunchRequestHandler() {
        SQSSendingMessageHandler messageHandler = new SQSSendingMessageHandler(queueMessagingTemplate());
        return messageHandler;
    }

}
