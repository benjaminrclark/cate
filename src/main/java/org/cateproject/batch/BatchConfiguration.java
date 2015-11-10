package org.cateproject.batch;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.sql.DataSource;

import org.apache.activemq.command.ActiveMQQueue;
import org.cateproject.multitenant.batch.MultitenantAwareJobLauncher;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.DefaultJobLoader;
import org.springframework.batch.core.configuration.support.JobLoader;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.task.TaskExecutor;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.handler.ServiceActivatingHandler;
import org.springframework.integration.jms.DynamicJmsTemplate;
import org.springframework.integration.jms.JmsDestinationPollingSource;
import org.springframework.integration.jms.JmsSendingMessageHandler;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private MessageConverter messageConverter;

    @Autowired 
    private DataSource dataSource;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    @Qualifier("conversionService")
    private ConversionService conversionService;

    @Autowired
    private JobRegistry jobRegistry; 

    @Bean
    public TaskExecutor batchTaskExecutor() {
        ThreadPoolTaskExecutor batchTaskExecutor = new ThreadPoolTaskExecutor();
        batchTaskExecutor.setMaxPoolSize(1);
        batchTaskExecutor.setQueueCapacity(0);    
        batchTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
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
            jobLauncher.setTaskExecutor(batchTaskExecutor());
            return jobLauncher;
        } catch (Exception e) {
            throw new RuntimeException(e); 
        }
   }

    @Bean
    public MessageChannel outgoingJobLaunchRequests() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel incomingJobLaunchRequests() {
        return new DirectChannel();
    }

    @Bean
    public Destination jobLaunchRequestQueue() {
        return new ActiveMQQueue("cate.jobLaunchRequests");
    }

    @Bean
    @InboundChannelAdapter(value = "incomingJobLaunchRequests", poller = @Poller(fixedRate = "5000"))
    public MessageSource<Object> inboundJobLaunchRequestHandler() {
	JmsTemplate jmsTemplate = new DynamicJmsTemplate();
	jmsTemplate.setMessageConverter(messageConverter);
	jmsTemplate.setConnectionFactory(connectionFactory);
	JmsDestinationPollingSource jmsDestinationPollingSource = new JmsDestinationPollingSource(jmsTemplate);
	jmsDestinationPollingSource.setDestination(jobLaunchRequestQueue());
	return jmsDestinationPollingSource;
    }
	
    @Bean
    @ServiceActivator(inputChannel = "outgoingJobLaunchRequests")
    public MessageHandler outboundJobLaunchRequestHandler() {
        JmsTemplate jmsTemplate = new DynamicJmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory);
        jmsTemplate.setMessageConverter(messageConverter);
        JmsSendingMessageHandler messageHandler = new JmsSendingMessageHandler(jmsTemplate);
        messageHandler.setDestination(jobLaunchRequestQueue());
        return messageHandler;
    }

    @Bean
    @ServiceActivator(inputChannel="incomingJobLaunchRequests")
    public MessageHandler incomingJobLaunchRequestHandler() {
        JobLaunchRequestHandler jobLaunchRequestHandler = new JobLaunchingMessageHandler(jobLauncher());
	return new ServiceActivatingHandler(jobLaunchRequestHandler, "launch");
    }
}
