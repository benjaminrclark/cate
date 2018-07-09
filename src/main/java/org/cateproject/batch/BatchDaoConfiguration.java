package org.cateproject.batch;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.cateproject.batch.job.ResolutionService;
import org.cateproject.domain.Base;
import org.cateproject.domain.batch.TermFactory;
import org.cateproject.file.GetResourceClient;
import org.cateproject.multitenant.batch.MultitenantAwareJobLauncher;
import org.cateproject.repository.jdbc.batch.JobInstanceRepository;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchDaoConfiguration {

   @Autowired
   private JobRegistry jobRegistry;

   @Autowired
   private DataSource dataSource; 

   @Autowired
   private PlatformTransactionManager transactionManager;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    @Qualifier("conversionService")
    private ConversionService conversionService;

    @Autowired
    @Qualifier("batchTaskExecutor")
    TaskExecutor batchTaskExecutor;

   @Bean
   public JobLauncher jobLauncher(JobRepository jobRepository) {
            MultitenantAwareJobLauncher jobLauncher = new MultitenantAwareJobLauncher();
            jobLauncher.setConversionService(conversionService);
            jobLauncher.setJobRepository(jobRepository);
            jobLauncher.setTaskExecutor(batchTaskExecutor);
            return jobLauncher;
   }

    @Bean
    public ItemWriter<?> itemWriter() {
        JpaItemWriter<?> itemWriter = new JpaItemWriter<Base>();
        itemWriter.setEntityManagerFactory(entityManagerFactory);
        return itemWriter;
    }

    @Bean
    public GetResourceClient getResourceClient() {
       return new GetResourceClient();
    }

   @Bean
   JobExecutionListener batchListener() {
       return new BatchListener();
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
   public org.cateproject.repository.jdbc.batch.JobRepository readOnlyJobRepository() {
       org.cateproject.repository.jdbc.batch.JobRepository readOnlyJobRepository = new org.cateproject.repository.jdbc.batch.JobRepository();
       readOnlyJobRepository.setListableJobLocator(jobRegistry);
       return readOnlyJobRepository;
   }

    @Bean
    public JobExplorerFactoryBean jobExplorerFactoryBean() {
        JobExplorerFactoryBean jobExplorerFactoryBean = new JobExplorerFactoryBean();
        jobExplorerFactoryBean.setDataSource(dataSource);
        return jobExplorerFactoryBean;
   }

   @Bean JobInstanceDao jobInstanceDao() {
       return jobRepositoryFactoryBean().getJobInstanceDao();
   }

   @Bean
   public JobInstanceRepository readOnlyJobInstanceRepository() {
       JobInstanceRepository readOnlyJobInstanceRepository = new JobInstanceRepository();
       readOnlyJobInstanceRepository.setJobInstanceDao(jobInstanceDao());
       return readOnlyJobInstanceRepository;
   }

   @Bean
   public TermFactory termFactory() {
       return new TermFactory();
   }

   @Bean
   public ResolutionService resolutionService() {
       return new ResolutionService();
   }
}
