package org.cateproject.batch;

import org.cateproject.batch.job.ProcessDarwinCoreArchiveConfiguration;
import org.cateproject.batch.job.ProcessMultimediaConfiguration;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.ApplicationContextFactory;
import org.springframework.batch.core.configuration.support.GenericApplicationContextFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing(modular = true)
public class BatchConfiguration {

    @Bean
    public ApplicationContextFactory processDarwinCoreArchiveConfiguration() {
        return new GenericApplicationContextFactory(ProcessDarwinCoreArchiveConfiguration.class);
    }

    @Bean
    public ApplicationContextFactory processMultimediaConfiguration() {
        return new GenericApplicationContextFactory(ProcessMultimediaConfiguration.class);
    }
}
