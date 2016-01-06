package org.cateproject.web.multitenant.event;

import org.cateproject.multitenant.event.MultitenantEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.aws.messaging.config.annotation.NotificationMessage;
import org.springframework.cloud.aws.messaging.config.annotation.NotificationSubject;
import org.springframework.cloud.aws.messaging.endpoint.NotificationStatus;
import org.springframework.cloud.aws.messaging.endpoint.annotation.NotificationMessageMapping;
import org.springframework.cloud.aws.messaging.endpoint.annotation.NotificationSubscriptionMapping;
import org.springframework.cloud.aws.messaging.endpoint.annotation.NotificationUnsubscribeConfirmationMapping;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

@RequestMapping("/multitenant/event")
public class SnsEndpointController {

    private static Logger logger = LoggerFactory.getLogger(SnsEndpointController.class);

    @Autowired
    @Qualifier("incomingTenantEvents")
    private MessageChannel incomingTenantEvents;

    @Autowired
    private ObjectMapper objectMapper;

    public void setIncomingTenantEvents(MessageChannel incomingTenantEvents) {
        this.incomingTenantEvents = incomingTenantEvents;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @NotificationMessageMapping
    public void receiveNotification(@NotificationMessage String message, @NotificationSubject String subject) {
        MultitenantEvent multitenantEvent = objectMapper.convertValue(message, MultitenantEvent.class);  
        incomingTenantEvents.send(new GenericMessage<MultitenantEvent>(multitenantEvent));
    }

    @NotificationSubscriptionMapping
    public void confirmSubscription(NotificationStatus notificationStatus){
        logger.info("Confirm Subscription" + notificationStatus);
        notificationStatus.confirmSubscription();
    }

    @NotificationUnsubscribeConfirmationMapping
    public void confirmUnsubscribe(NotificationStatus notificationStatus) {
        notificationStatus.confirmSubscription();
    }
}
