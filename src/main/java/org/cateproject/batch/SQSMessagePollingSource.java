package org.cateproject.batch;

import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.integration.endpoint.AbstractMessageSource;

public class SQSMessagePollingSource<T> extends AbstractMessageSource<T> {

   private QueueMessagingTemplate queueMessagingTemplate;

   private Class<T> messageClass;

   public SQSMessagePollingSource(QueueMessagingTemplate queueMessagingTemplate, Class<T> messageClass) {
       this.queueMessagingTemplate = queueMessagingTemplate;
       this.messageClass = messageClass;
   }

   protected T doReceive() {
       return queueMessagingTemplate.receiveAndConvert(messageClass);
   }

   public String getComponentType() {
       return SQSMessagePollingSource.class.getName();
   }
}
