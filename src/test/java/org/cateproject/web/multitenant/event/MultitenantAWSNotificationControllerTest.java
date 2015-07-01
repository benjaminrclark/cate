package org.cateproject.web.multitenant.event;

import static org.junit.Assert.assertEquals;
import java.io.IOException;

import org.cateproject.multitenant.event.MultitenantEvent;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.cloud.aws.messaging.endpoint.NotificationStatus;
import org.springframework.core.convert.ConversionService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

public class MultitenantAWSNotificationControllerTest {

    private MultitenantAWSNotificationController multitenantAWSNotificationController;

    private NotificationStatus notificationStatus;

    private ConversionService conversionService;
  
    private MessageChannel incomingTenantEvents;

    private MultitenantEvent multitenantEvent;

    @Before
    public void setUp() {
        multitenantAWSNotificationController = new MultitenantAWSNotificationController();
        notificationStatus = EasyMock.createMock(NotificationStatus.class);
        conversionService = EasyMock.createMock(ConversionService.class);
        incomingTenantEvents = EasyMock.createMock(MessageChannel.class);
        
        multitenantAWSNotificationController.setConversionService(conversionService);
        multitenantAWSNotificationController.setIncomingTenantEvents(incomingTenantEvents);       

        multitenantEvent = new MultitenantEvent();
    }

    @Test 
    public void testHandleSubscriptionMessage() throws IOException {
         notificationStatus.confirmSubscription();

         EasyMock.replay(notificationStatus, conversionService, incomingTenantEvents);
         multitenantAWSNotificationController.handleSubscriptionMessage(notificationStatus);
         EasyMock.verify(notificationStatus, conversionService, incomingTenantEvents);
    }

    @Test
    public void testHandleUnsubscribeMessage() {
         notificationStatus.confirmSubscription();

         EasyMock.replay(notificationStatus, conversionService, incomingTenantEvents);
         multitenantAWSNotificationController.handleUnsubscribeMessage(notificationStatus);
         EasyMock.verify(notificationStatus, conversionService, incomingTenantEvents);
    }

    @Test
    public void testHandleNotificationMessage() {
         Capture<Message<MultitenantEvent>> capturedMessage = new Capture<Message<MultitenantEvent>>();
         EasyMock.expect(conversionService.convert(EasyMock.eq("MESSAGE"), EasyMock.eq(MultitenantEvent.class))).andReturn(multitenantEvent);
         EasyMock.expect(incomingTenantEvents.send(EasyMock.and(EasyMock.capture(capturedMessage), EasyMock.isA(Message.class)))).andReturn(true);

         EasyMock.replay(notificationStatus, conversionService, incomingTenantEvents);
         multitenantAWSNotificationController.handleNotificationMessage("SUBJECT","MESSAGE");
         EasyMock.verify(notificationStatus, conversionService, incomingTenantEvents);
         assertEquals("The payload of the message sent should be the multitenantEvent", multitenantEvent, capturedMessage.getValue().getPayload());
    }
}
