package org.cateproject.multitenant.event;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.core.convert.ConversionService;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

public class SnsSendingMessageHandlerTest {

  private SnsSendingMessageHandler snsSendingMessageHandler;

  private NotificationMessagingTemplate notificationMessagingTemplate;


  @Before
  public void setUp() {
    notificationMessagingTemplate = EasyMock.createMock(NotificationMessagingTemplate.class);   
    snsSendingMessageHandler = new SnsSendingMessageHandler(notificationMessagingTemplate);
  }

  @Test
  public void testHandleMessage() {
      MultitenantEvent multitenantEvent = new MultitenantEvent();
      Message<?> message = MessageBuilder.withPayload(multitenantEvent).build();
      notificationMessagingTemplate.convertAndSend(EasyMock.eq(message));
      EasyMock.replay(notificationMessagingTemplate);
      snsSendingMessageHandler.handleMessage(message);
      EasyMock.verify(notificationMessagingTemplate); 
  }
}
