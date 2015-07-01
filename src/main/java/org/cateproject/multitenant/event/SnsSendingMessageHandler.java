package org.cateproject.multitenant.event;

import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;


public class SnsSendingMessageHandler implements MessageHandler {

    private NotificationMessagingTemplate notificationMessagingTemplate;


    public SnsSendingMessageHandler(NotificationMessagingTemplate notificationMessagingTemplate) {
        this.notificationMessagingTemplate = notificationMessagingTemplate;
    }

    @Override
    public void handleMessage(Message<?> message) {
        this.notificationMessagingTemplate.convertAndSend(message); 
    }
}
