package org.cateproject.batch;

import java.util.Map;

import javax.sql.DataSource;

import org.cateproject.file.GetResourceClient;
import org.cateproject.multitenant.batch.MultitenantAwareJobLauncher;
import org.cateproject.repository.jdbc.batch.JobInstanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.configuration.support.DefaultJobLoader;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    private static Logger logger = LoggerFactory.getLogger(BatchConfiguration.class);

    @Autowired 
    private DataSource dataSource;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    @Qualifier("conversionService")
    private ConversionService conversionService;

    @Autowired
    @Qualifier("batchTaskExecutor")
    TaskExecutor batchTaskExecutor;

    @Autowired
    private JobRegistry jobRegistry; 

    @Autowired
    private StepBuilderFactory steps;

    @Bean
    public GetResourceClient getResourceClient() {
       return new GetResourceClient();
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
   public DefaultJobLoader jobLoader() {
        return new DefaultJobLoader(jobRegistry);
   }

   @Bean
   public JobLauncher jobLauncher() {
        try {
            JobRepository jobRepository = jobRepositoryFactoryBean().getObject();
            MultitenantAwareJobLauncher jobLauncher = new MultitenantAwareJobLauncher();
            jobLauncher.setConversionService(conversionService);
            jobLauncher.setJobRepository(jobRepository);
            jobLauncher.setTaskExecutor(batchTaskExecutor);
            return jobLauncher;
        } catch (Exception e) {
            throw new RuntimeException(e); 
        }
   }

   @Bean
   public org.cateproject.repository.jdbc.batch.JobRepository readOnlyJobRepository() {
       org.cateproject.repository.jdbc.batch.JobRepository readOnlyJobRepository = new org.cateproject.repository.jdbc.batch.JobRepository();
       readOnlyJobRepository.setListableJobLocator(jobRegistry);
       return readOnlyJobRepository;
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
    @StepScope
    public ParameterConvertingTasklet convertBatchParamsTasklet(@Value("#{jobParameters}") Map<String,String> jobParameters) {
        logger.debug("JobParameters {}", new Object[]{jobParameters});
        ParameterConvertingTasklet parameterConvertingTasklet = new ParameterConvertingTasklet();
        parameterConvertingTasklet.setJobParameters(jobParameters);
        return parameterConvertingTasklet;
    }

    @Bean
    public Step convertBatchParams(Tasklet convertBatchParamsTasklet) {
        return steps.get("convertBatchParams").tasklet(convertBatchParamsTasklet).listener(batchListener()).build();
    }

    @Bean
    public Step cleanUpResources(Tasklet inputFileCleanupTasklet) {
      return steps.get("cleanupResources").tasklet(inputFileCleanupTasklet).listener(batchListener()).build();
    }

    @Bean
    @StepScope
    public InputFileCleanupTasklet inputFileCleanupTasklet(@Value("#{jobExecutionContext['input.file']}") String inputFile) {
        InputFileCleanupTasklet inputFileCleanupTasklet = new InputFileCleanupTasklet();
        inputFileCleanupTasklet.setInputFile(inputFile);
        return inputFileCleanupTasklet;
    }

    @Bean
    JobExecutionListener batchListener() {
        return new BatchListener();
    }
}
