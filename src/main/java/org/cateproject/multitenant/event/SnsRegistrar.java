package org.cateproject.multitenant.event;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.SubscribeResult;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;

public class SnsRegistrar implements InitializingBean, DisposableBean {

    private static Logger logger = LoggerFactory.getLogger(SnsRegistrar.class);

    private AmazonSNS amazonSns;

    private AmazonSQS amazonSqs;

    private String topicArn;

    private String subscriptionArn;

    private String queueArn;

    public SnsRegistrar(AmazonSNS amazonSns, AmazonSQS amazonSqs, String topicArn) {
        this.amazonSns = amazonSns;
        this.amazonSqs = amazonSqs;
        this.topicArn = topicArn;
    }

    public String getQueueArn() {
        logger.info("Calling getQueueArn, arn is {}", new Object[]{queueArn});
        if(queueArn == null) {
            afterPropertiesSet();
        }
        return queueArn;
    }

    protected void setQueueArn(String queueArn) {
        this.queueArn = queueArn;
    }
   
    protected void setSubscriptionArn(String subscriptionArn) {
        this.subscriptionArn = subscriptionArn;
    }
 
    protected String getSubscriptionArn() {
        return subscriptionArn;
    }

    public void destroy() {
        amazonSns.unsubscribe(subscriptionArn);
        logger.info("Unsubscribed from ARN: {}, Subscription ARN {}", new Object[]{topicArn, subscriptionArn});
        amazonSqs.deleteQueue(queueArn);
        logger.info("Deleted Queue ARN: {}", new Object[]{queueArn});
    }

    public void afterPropertiesSet() {
        CreateQueueRequest createQueueRequest = new CreateQueueRequest();
        createQueueRequest.setQueueName("cateMultitenantEvent-" + UUID.randomUUID().toString());
        CreateQueueResult createQueueResult = amazonSqs.createQueue(createQueueRequest);
        logger.info("Successfully created queue with url {}", new Object[]{createQueueResult.getQueueUrl()});
        queueArn = createQueueResult.getQueueUrl();
        
        SubscribeResult subscribeResult = amazonSns.subscribe(topicArn, "sqs", queueArn);
        subscriptionArn = subscribeResult.getSubscriptionArn();
        logger.info("Subscription successful, subscribed queue {} to topic {}, subscription arn: {}", new Object[]{queueArn, topicArn, subscriptionArn});
    }
}
