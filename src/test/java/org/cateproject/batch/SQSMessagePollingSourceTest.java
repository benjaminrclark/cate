package org.cateproject.batch;

import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;

public class SQSMessagePollingSourceTest {

    private SQSMessagePollingSource<String> sqsMessagePollingSource;

    private QueueMessagingTemplate queueMessagingTemplate;

    @Before
    public void setUp() {
        queueMessagingTemplate = EasyMock.createMock(QueueMessagingTemplate.class);
        sqsMessagePollingSource = new SQSMessagePollingSource<String>(queueMessagingTemplate, String.class);
    }

    @Test
    public void testDoReceive() {
        EasyMock.expect(queueMessagingTemplate.receiveAndConvert(EasyMock.eq(String.class))).andReturn("STRING");

        EasyMock.replay(queueMessagingTemplate);
        assertEquals("doReceive should return the expected value", "STRING", sqsMessagePollingSource.doReceive());
        EasyMock.verify(queueMessagingTemplate);   
    }

    @Test
    public void getComponentType() {
        assertEquals("getComponentType should return the component type","org.cateproject.batch.SQSMessagePollingSource", sqsMessagePollingSource.getComponentType());
    }

}
