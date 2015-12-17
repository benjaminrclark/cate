package org.cateproject;

import org.springframework.cloud.aws.context.config.annotation.EnableContextInstanceData;
import org.springframework.cloud.aws.context.config.annotation.EnableStackConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("aws")
@Configuration
@EnableContextInstanceData
@EnableStackConfiguration
public class AWSConfiguration {
}
