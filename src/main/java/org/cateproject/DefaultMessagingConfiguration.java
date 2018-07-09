package org.cateproject;

import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Profile("default")
@Configuration
@Import({ActiveMQAutoConfiguration.class})
public class DefaultMessagingConfiguration {
}
