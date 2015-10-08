package org.cateproject.batch.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.cateproject.batch.BatchListener;
import org.cateproject.batch.ParameterConvertingTasklet;
import org.cateproject.batch.multimedia.MultimediaFileService;
import org.cateproject.domain.Base;
import org.cateproject.domain.Multimedia;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.OptimisticLockingFailureException;

@Configuration
public class ProcessImageConfiguration {
   
    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Bean
    public Job processImage() {
        return jobs.get("ProcessImage").start(convertBatchParams()).next(processSingleImage()).next(cleanUpResources()).listener(batchListener()).build();
    }

    @Bean
    public Tasklet convertBatchParamsTasklet() {
        return new ParameterConvertingTasklet();
    }

    @Bean
    JobExecutionListener batchListener() {
        return new BatchListener();
    }

    @Bean
    public Step convertBatchParams() {
        return steps.get("convertBatchParams").tasklet(convertBatchParamsTasklet()).listener(batchListener()).build();
    }

    @Bean
    public MultimediaFileService multimediaFileService() {
        return new MultimediaFileService();
    }

    @Bean
    @StepScope
    public ItemReader<Multimedia> multimediaFileReader(@Value("#{jobExecutionContext['query.string']}") String queryString, @Value("#{jobExecutionContext['query.parameters_map']}") Map<String, Object> parameterValues) {
        JpaPagingItemReader<Multimedia> multimediaFileReader = new JpaPagingItemReader<Multimedia>();
        multimediaFileReader.setEntityManagerFactory(entityManagerFactory);
        multimediaFileReader.setQueryString(queryString);
        multimediaFileReader.setParameterValues(parameterValues);
        return multimediaFileReader;
    }

	/*
	      <ref bean="imageMetadataProcessor"/>
              <ref bean="imageLargeProcessor"/>
	      <ref bean="imageThumbnailProcessor"/>
	*/

    @Bean
    public ItemProcessor<Multimedia,Multimedia> multimediaFetchingProcessor() {
        return null;
    }

    @Bean
    public ItemProcessor<Multimedia,Multimedia> multimediaFileProcessor() {
        List<ItemProcessor<Multimedia,Multimedia>> delegates = new ArrayList<ItemProcessor<Multimedia,Multimedia>>();
        delegates.add(multimediaFetchingProcessor());
        CompositeItemProcessor<Multimedia,Multimedia> multimediaFileProcessor = new CompositeItemProcessor<Multimedia,Multimedia>();
        multimediaFileProcessor.setDelegates(delegates);
        return multimediaFileProcessor;
    }
/*	  
          <ref bean="imageOriginalFileWriter"/>
	      <ref bean="imageLargeFileWriter"/>
	      <ref bean="imageThumbnailFileWriter"/>
*/

    @Bean
    public ItemWriter<Base> itemWriter() {
        JpaItemWriter<Base> itemWriter = new JpaItemWriter();
        itemWriter.setEntityManagerFactory(entityManagerFactory);
        return itemWriter;
    }

    @Bean
    public ItemWriter<Multimedia> multimediaFileWriter() {
        List<ItemWriter<? super Multimedia>> delegates = new ArrayList<ItemWriter<? super Multimedia>>();
        delegates.add(itemWriter());
        CompositeItemWriter<Multimedia> multimediaFileWriter = new CompositeItemWriter<Multimedia>();
        multimediaFileWriter.setDelegates(delegates);
        return multimediaFileWriter;
    }

    @Bean
    public Step processSingleImage() {
       return steps.get("processSingleImage").<Multimedia,Multimedia> chunk(1).faultTolerant().retryLimit(5).retry(OptimisticLockingFailureException.class)
                   .reader(multimediaFileReader(null, null))
                   .processor(multimediaFileProcessor())
                   .writer(multimediaFileWriter()).listener(batchListener()).build();
    }

    @Bean
    public Step cleanUpResources() {
      return null;
    }
}
