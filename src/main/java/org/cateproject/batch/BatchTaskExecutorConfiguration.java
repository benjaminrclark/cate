package org.cateproject.batch;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class BatchTaskExecutorConfiguration {

    @Bean
    public TaskExecutor batchTaskExecutor() {
        ThreadPoolTaskExecutor batchTaskExecutor = new ThreadPoolTaskExecutor();
        batchTaskExecutor.setMaxPoolSize(1);
        batchTaskExecutor.setQueueCapacity(0);    
        batchTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return batchTaskExecutor;
    }
}
