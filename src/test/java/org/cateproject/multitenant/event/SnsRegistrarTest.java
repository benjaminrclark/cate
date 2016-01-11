package org.cateproject.multitenant.event;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.SubscribeResult;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;

public class SnsRegistrarTest {

    private SnsRegistrar snsRegistrar;

    private AmazonSNS amazonSns;
  
    private AmazonSQS amazonSqs;

    @Before
    public void setUp() {
        amazonSns = EasyMock.createMock(AmazonSNS.class);
        amazonSqs = EasyMock.createMock(AmazonSQS.class);

        snsRegistrar = new SnsRegistrar(amazonSns, amazonSqs, "TOPIC_ARN");
    }

    @Test
    public void testGetQueueArn() {
        CreateQueueResult createQueueResult = new CreateQueueResult();
        createQueueResult.setQueueUrl("QUEUE_URL");
        GetQueueAttributesResult getQueueAttributesResult = new GetQueueAttributesResult();
        getQueueAttributesResult.addAttributesEntry("QueueArn","QUEUE_ARN");
        SubscribeResult subscribeResult = new SubscribeResult();
        subscribeResult.setSubscriptionArn("SUBSCRIPTION_ARN");
        EasyMock.expect(amazonSqs.createQueue(EasyMock.isA(CreateQueueRequest.class))).andReturn(createQueueResult);
        EasyMock.expect(amazonSqs.getQueueAttributes(EasyMock.eq("QUEUE_URL"), (List<String>)EasyMock.isA(List.class))).andReturn(getQueueAttributesResult);
        EasyMock.expect(amazonSns.subscribe(EasyMock.eq("TOPIC_ARN"), EasyMock.eq("sqs"), EasyMock.eq("QUEUE_ARN"))).andReturn(subscribeResult);
        
        EasyMock.replay(amazonSns, amazonSqs);
        assertEquals("afterPropertiesSet should set the queueArn", "QUEUE_ARN", snsRegistrar.getQueueArn());
        assertEquals("afterPropertiesSet should set the subscriptionArn", "SUBSCRIPTION_ARN", snsRegistrar.getSubscriptionArn());
        EasyMock.verify(amazonSns, amazonSqs);
    }

    @Test
    public void testGetQueueArnAlreadyInitialized() {
        snsRegistrar.setQueueArn("QUEUE_ARN");
        snsRegistrar.setSubscriptionArn("SUBSCRIPTION_ARN");
        EasyMock.replay(amazonSns, amazonSqs);
        assertEquals("afterPropertiesSet should set the queueArn", "QUEUE_ARN", snsRegistrar.getQueueArn());
        assertEquals("afterPropertiesSet should set the subscriptionArn", "SUBSCRIPTION_ARN", snsRegistrar.getSubscriptionArn());
        EasyMock.verify(amazonSns, amazonSqs);
    }

    @Test
    public void testDestroy() {
        snsRegistrar.setQueueArn("QUEUE_ARN");
        snsRegistrar.setSubscriptionArn("SUBSCRIPTION_ARN");
        amazonSqs.deleteQueue(EasyMock.eq("QUEUE_ARN"));
        amazonSns.unsubscribe(EasyMock.eq("SUBSCRIPTION_ARN"));
        
        EasyMock.replay(amazonSns, amazonSqs);
        snsRegistrar.destroy();
        EasyMock.verify(amazonSns, amazonSqs);
    }
}
