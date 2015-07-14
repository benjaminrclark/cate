package org.cateproject.batch;

import javax.sql.DataSource;

import org.cateproject.multitenant.batch.MultitenantAwareJobLauncher;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired 
    private DataSource dataSource;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    @Qualifier("integrationConversionService")
    private ConversionService conversionService;

    @Bean
    public TaskExecutor batchTaskExecutor() {
        ThreadPoolTaskExecutor batchTaskExecutor = new ThreadPoolTaskExecutor();
        batchTaskExecutor.setMaxPoolSize(1);
        batchTaskExecutor.setQueueCapacity(0);    
        return batchTaskExecutor;
}

    @Bean
    public JobRepositoryFactoryBean jobRepositoryFactoryBean() {
        JobRepositoryFactoryBean jobRepositoryFactoryBean = new JobRepositoryFactoryBean();
        jobRepositoryFactoryBean.setDataSource(dataSource);
        jobRepositoryFactoryBean.setTransactionManager(transactionManager);
        jobRepositoryFactoryBean.setIsolationLevelForCreate("ISOLATION_DEFAULT");
        return jobRepositoryFactoryBean;
    }

    @Bean
    public JobExplorerFactoryBean jobExplorerFactoryBean() {
        JobExplorerFactoryBean jobExplorerFactoryBean = new JobExplorerFactoryBean();
        jobExplorerFactoryBean.setDataSource(dataSource);
        return jobExplorerFactoryBean;
   }

   @Bean
   public JobLauncher jobLauncher() throws Exception {
       MultitenantAwareJobLauncher jobLauncher = new MultitenantAwareJobLauncher();
       jobLauncher.setConversionService(conversionService);
       jobLauncher.setJobRepository(jobRepositoryFactoryBean().getObject());
       jobLauncher.setTaskExecutor(batchTaskExecutor());
       return jobLauncher;
   }

   
}
