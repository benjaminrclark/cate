package org.cateproject.web.multitenant.event;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.core.env.ResourceIdResolver;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.SubscribeResult;

public class SnsSubscriptionRegistrar implements ServletContextListener, DisposableBean {

    private static Logger logger = LoggerFactory.getLogger(SnsSubscriptionRegistrar.class);

    @Value("${public-hostname:N/A}") 
    private String hostname;

    @Value("${cloudformation.topic.logicalName:'CATETopic'}")
    private String logicalTopicName;

    @Autowired
    private ResourceIdResolver resourceIdResolver;

    @Autowired
    AmazonSNS amazonSNS;

    String subscriptionArn = null;

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        String topicArn = resourceIdResolver.resolveToPhysicalResourceId(logicalTopicName);
        SubscribeResult subscribeResult = amazonSNS.subscribe(topicArn, "http", "http://" + hostname + "/multitenant/event");
        subscriptionArn = subscribeResult.getSubscriptionArn();
        logger.info("Subscription successful, ARN: {}", new Object[]{subscriptionArn});
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    public void destroy() {
        if(subscriptionArn != null) {
            amazonSNS.unsubscribe(subscriptionArn);
            logger.info("Unsubscribed from ARN: {}", new Object[]{subscriptionArn});
        }

    }
}
