package org.cateproject.web.multitenant.event;

import static org.junit.Assert.assertEquals;

import javax.servlet.ServletContextEvent;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.cloud.aws.core.env.ResourceIdResolver;
import org.springframework.mock.web.MockServletContext;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.SubscribeResult;

public class SnsSubscriptionRegistrarTest {

    private SnsSubscriptionRegistrar snsSubscriptionRegistrar;

    private ResourceIdResolver resourceIdResolver;

    private AmazonSNS amazonSns;


    @Before
    public void setUp() {
        snsSubscriptionRegistrar = new SnsSubscriptionRegistrar();
        resourceIdResolver = EasyMock.createMock(ResourceIdResolver.class);
        amazonSns = EasyMock.createMock(AmazonSNS.class);
        snsSubscriptionRegistrar.setResourceIdResolver(resourceIdResolver);
        snsSubscriptionRegistrar.setAmazonSNS(amazonSns); 
        snsSubscriptionRegistrar.setHostname("HOSTNAME");
        snsSubscriptionRegistrar.setTopicName("TOPIC_NAME");
    }

    @Test
    public void testContextInitialized() {
        ServletContextEvent servletContextEvent = new ServletContextEvent(new MockServletContext());
        SubscribeResult subscribeResult = new SubscribeResult();
        subscribeResult.setSubscriptionArn("SUBSCRIPTION_ARN");
        EasyMock.expect(resourceIdResolver.resolveToPhysicalResourceId(EasyMock.eq("TOPIC_NAME"))).andReturn("TOPIC_ARN");
        EasyMock.expect(amazonSns.subscribe(EasyMock.eq("TOPIC_ARN"),EasyMock.eq("http"),EasyMock.eq("http://HOSTNAME/multitenant/event"))).andReturn(subscribeResult);

        EasyMock.replay(resourceIdResolver,amazonSns);
        snsSubscriptionRegistrar.contextInitialized(servletContextEvent);
        assertEquals("contextInitialized should set the subscription arn", "SUBSCRIPTION_ARN", snsSubscriptionRegistrar.getSubscriptionArn());
        EasyMock.verify(resourceIdResolver,amazonSns);
    }

    @Test
    public void testContextDestroyed() {
        ServletContextEvent servletContextEvent = new ServletContextEvent(new MockServletContext());
        EasyMock.replay(resourceIdResolver,amazonSns);
        snsSubscriptionRegistrar.contextDestroyed(servletContextEvent);
        EasyMock.verify(resourceIdResolver,amazonSns);
    }
  

    @Test
    public void testDestroy() {
        snsSubscriptionRegistrar.setSubscriptionArn("SUBSCRIPTION_ARN");
        amazonSns.unsubscribe(EasyMock.eq("SUBSCRIPTION_ARN"));

        EasyMock.replay(resourceIdResolver,amazonSns);
        snsSubscriptionRegistrar.destroy();
        EasyMock.verify(resourceIdResolver,amazonSns);
    } 

    @Test
    public void testDestroyWithNullTopicArn() {
        EasyMock.replay(resourceIdResolver,amazonSns);
        snsSubscriptionRegistrar.destroy();
        EasyMock.verify(resourceIdResolver,amazonSns);
    } 
}
