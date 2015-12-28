package org.cateproject.batch;

import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SQSSendingMessageHandler implements MessageHandler {

    private QueueMessagingTemplate queueMessagingTemplate;

    public SQSSendingMessageHandler(QueueMessagingTemplate queueMessagingTemplate) {
        this.queueMessagingTemplate = queueMessagingTemplate;
    }

    @Override
    public void handleMessage(Message<?> message) {
        queueMessagingTemplate.convertAndSend(message);        
    }
}
