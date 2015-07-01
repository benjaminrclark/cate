package org.cateproject.web.multitenant.event;

import java.io.IOException;

import org.cateproject.multitenant.event.MultitenantEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.aws.messaging.config.annotation.NotificationMessage;
import org.springframework.cloud.aws.messaging.config.annotation.NotificationSubject;
import org.springframework.cloud.aws.messaging.endpoint.NotificationStatus;
import org.springframework.cloud.aws.messaging.endpoint.annotation.NotificationMessageMapping;
import org.springframework.cloud.aws.messaging.endpoint.annotation.NotificationSubscriptionMapping;
import org.springframework.cloud.aws.messaging.endpoint.annotation.NotificationUnsubscribeConfirmationMapping;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.ConversionService;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Profile("AWS")
@RequestMapping("/multitenant/event")
public class MultitenantAWSNotificationController {

        @Autowired
        @Qualifier("incomingTenantEvents")
        private MessageChannel incomingTenantEvents;

        @Autowired
        private ConversionService conversionService;

        public void setConversionService(ConversionService conversionService) {
            this.conversionService = conversionService;
        }

        public void setIncomingTenantEvents(MessageChannel incomingTenantEvents) {
           this.incomingTenantEvents = incomingTenantEvents;
        }

        @NotificationSubscriptionMapping
	public void handleSubscriptionMessage(NotificationStatus status) throws IOException {
		status.confirmSubscription();
	}
		
        @NotificationMessageMapping
	public void handleNotificationMessage(@NotificationSubject String subject, @NotificationMessage String message) {
            MultitenantEvent multitenantEvent = conversionService.convert(message, MultitenantEvent.class);
            incomingTenantEvents.send(MessageBuilder.withPayload(multitenantEvent).build());
	}
	
	@NotificationUnsubscribeConfirmationMapping
        public void handleUnsubscribeMessage(NotificationStatus status) {
	    status.confirmSubscription();
        }
}
