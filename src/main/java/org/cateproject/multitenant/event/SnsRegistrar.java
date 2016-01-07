package org.cateproject.multitenant.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.SubscribeResult;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.CreateQueueResult;

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
        CreateQueueResult createQueueResult = amazonSqs.createQueue(new CreateQueueRequest());
        queueArn = createQueueResult.getQueueUrl();
        SubscribeResult subscribeResult = amazonSns.subscribe(topicArn, "sqs", queueArn);
        subscriptionArn = subscribeResult.getSubscriptionArn();
        logger.info("Subscription successful, ARN: {}", new Object[]{subscriptionArn});
    }
}
