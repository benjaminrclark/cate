package org.cateproject.batch.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.cateproject.batch.BatchListener;
import org.cateproject.batch.InputFileCleanupTasklet;
import org.cateproject.batch.ParameterConvertingTasklet;
import org.cateproject.batch.multimedia.MultimediaFetchingProcessor;
import org.cateproject.batch.multimedia.MultimediaFileService;
import org.cateproject.batch.multimedia.MultimediaFileWriter;
import org.cateproject.domain.Base;
import org.cateproject.domain.Multimedia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.configuration.support.ReferenceJobFactory;
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
public class ProcessMultimediaConfiguration {
 
    public static final Logger logger = LoggerFactory.getLogger(ProcessMultimediaConfiguration.class);
 
    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    JobRegistry jobRegistry;

    @Bean
    public Job processMultimedia(Step convertBatchParams, Step processSingleMultimedia, Step cleanUpResources) {
        try {
            Job job =  jobs.get("processMultimedia").start(convertBatchParams).next(processSingleMultimedia).next(cleanUpResources).listener(batchListener()).build();
            jobRegistry.register(new ReferenceJobFactory(job));
            return job;
        } catch (DuplicateJobException dje) {
            throw new RuntimeException(dje);
        }
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
    JobExecutionListener batchListener() {
        return new BatchListener();
    }

    @Bean
    public Step convertBatchParams(Tasklet convertBatchParamsTasklet) {
        return steps.get("convertBatchParams").tasklet(convertBatchParamsTasklet).listener(batchListener()).build();
    }

    @Bean
    public MultimediaFileService multimediaFileService() {
        return new MultimediaFileService();
    }

    @Bean(destroyMethod="")
    @StepScope
    public JpaPagingItemReader<Multimedia> multimediaFileReader(@Value("#{jobExecutionContext['query.string']}") String queryString, @Value("#{jobExecutionContext['query.parameters']}") Map<String, Object> parameterValues) {
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
    @StepScope
    public MultimediaFetchingProcessor multimediaFetchingProcessor(@Value("#{jobExecutionContext['input.file']}") String inputFile) {
        MultimediaFetchingProcessor multimediaFetchingProcessor = new MultimediaFetchingProcessor();
        multimediaFetchingProcessor.setUploadedFile(inputFile);
        return multimediaFetchingProcessor;
    }

    @Bean
    public ItemProcessor<Multimedia,Multimedia> multimediaFileProcessor(MultimediaFetchingProcessor multimediaFetchingProcessor) {
        List<ItemProcessor<Multimedia,Multimedia>> delegates = new ArrayList<ItemProcessor<Multimedia,Multimedia>>();
        delegates.add(multimediaFetchingProcessor);
        CompositeItemProcessor<Multimedia,Multimedia> multimediaFileProcessor = new CompositeItemProcessor<Multimedia,Multimedia>();
        multimediaFileProcessor.setDelegates(delegates);
        return multimediaFileProcessor;
    }

    @Bean
    public ItemWriter<Base> itemWriter() {
        JpaItemWriter<Base> itemWriter = new JpaItemWriter();
        itemWriter.setEntityManagerFactory(entityManagerFactory);
        return itemWriter;
    }

    @Bean
    public ItemWriter<Multimedia> multimediaWriter() {
        List<ItemWriter<? super Multimedia>> delegates = new ArrayList<ItemWriter<? super Multimedia>>();
        delegates.add(itemWriter());
        delegates.add(multimediaFileWriter());
        CompositeItemWriter<Multimedia> multimediaFileWriter = new CompositeItemWriter<Multimedia>();
        multimediaFileWriter.setDelegates(delegates);
        return multimediaFileWriter;
    }

    @Bean
    public MultimediaFileWriter multimediaFileWriter() {
        return new MultimediaFileWriter();
    }

    @Bean
    public Step processSingleMultimedia(ItemProcessor<Multimedia,Multimedia> multimediaFileProcessor) {
       return steps.get("processSingleMultimedia").<Multimedia,Multimedia> chunk(1).faultTolerant().retryLimit(5).retry(OptimisticLockingFailureException.class)
                   .reader(multimediaFileReader(null, null))
                   .processor(multimediaFileProcessor)
                   .writer(multimediaWriter()).listener(batchListener()).build();
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
}
