package org.cateproject.batch;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

public class SQSSendingMessageHandlerTest {

    private SQSSendingMessageHandler sqsSendingMessageHandler;

    private QueueMessagingTemplate queueMessagingTemplate;

    @Before
    public void setUp() {
        queueMessagingTemplate = EasyMock.createMock(QueueMessagingTemplate.class);
        sqsSendingMessageHandler = new SQSSendingMessageHandler(queueMessagingTemplate);
    }

    @Test
    public void testHandleMessage() {
        Message message = new GenericMessage<String>("MESSAGE");
        queueMessagingTemplate.convertAndSend(EasyMock.eq(message));

        EasyMock.replay(queueMessagingTemplate);
        sqsSendingMessageHandler.handleMessage(message);
        EasyMock.verify(queueMessagingTemplate);
    }
}
