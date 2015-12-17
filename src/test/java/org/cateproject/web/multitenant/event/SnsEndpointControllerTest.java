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

import com.fasterxml.jackson.databind.ObjectMapper;

public class SnsEndpointControllerTest {

    private SnsEndpointController snsEndpointController;

    private NotificationStatus notificationStatus;

    private ObjectMapper objectMapper;
  
    private MessageChannel incomingTenantEvents;

    private MultitenantEvent multitenantEvent;

    @Before
    public void setUp() {
        snsEndpointController = new SnsEndpointController();
        notificationStatus = EasyMock.createMock(NotificationStatus.class);
        objectMapper = EasyMock.createMock(ObjectMapper.class);
        incomingTenantEvents = EasyMock.createMock(MessageChannel.class);
        
        snsEndpointController.setObjectMapper(objectMapper);
        snsEndpointController.setIncomingTenantEvents(incomingTenantEvents);       

        multitenantEvent = new MultitenantEvent();
    }

    @Test 
    public void testConfirmSubscription() throws IOException {
         notificationStatus.confirmSubscription();

         EasyMock.replay(notificationStatus, objectMapper, incomingTenantEvents);
         snsEndpointController.confirmSubscription(notificationStatus);
         EasyMock.verify(notificationStatus, objectMapper, incomingTenantEvents);
    }

    @Test
    public void testConfirmUnsubscribe() {
         notificationStatus.confirmSubscription();

         EasyMock.replay(notificationStatus, objectMapper, incomingTenantEvents);
         snsEndpointController.confirmUnsubscribe(notificationStatus);
         EasyMock.verify(notificationStatus, objectMapper, incomingTenantEvents);
    }

    @Test
    public void testReceiveNotification() {
         Capture<Message<MultitenantEvent>> capturedMessage = new Capture<Message<MultitenantEvent>>();
         EasyMock.expect(objectMapper.convertValue(EasyMock.eq("MESSAGE"), EasyMock.eq(MultitenantEvent.class))).andReturn(multitenantEvent);
         EasyMock.expect(incomingTenantEvents.send(EasyMock.and(EasyMock.capture(capturedMessage), EasyMock.isA(Message.class)))).andReturn(true);

         EasyMock.replay(notificationStatus, objectMapper, incomingTenantEvents);
         snsEndpointController.receiveNotification("MESSAGE","SUBJECT");
         EasyMock.verify(notificationStatus, objectMapper, incomingTenantEvents);
         assertEquals("The payload of the message sent should be the multitenantEvent", multitenantEvent, capturedMessage.getValue().getPayload());
    }
}
