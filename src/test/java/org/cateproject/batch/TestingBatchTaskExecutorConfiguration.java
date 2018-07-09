package org.cateproject.batch;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
@Profile("integration-test")
public class TestingBatchTaskExecutorConfiguration extends BatchTaskExecutorConfiguration {

    @Override
    @Bean
    public TaskExecutor batchTaskExecutor() {
        return new SyncTaskExecutor();
    }

}
