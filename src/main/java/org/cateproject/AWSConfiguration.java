package org.cateproject;

import org.springframework.cloud.aws.context.config.annotation.EnableContextCredentials;
import org.springframework.cloud.aws.context.config.annotation.EnableContextInstanceData;
import org.springframework.cloud.aws.context.config.annotation.EnableContextRegion;
import org.springframework.cloud.aws.context.config.annotation.EnableStackConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("aws")
@Configuration
@EnableContextCredentials(instanceProfile=true)
@EnableContextRegion(autoDetect=true)
@EnableContextInstanceData
@EnableStackConfiguration
public class AWSConfiguration {
}
