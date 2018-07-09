package org.cateproject.batch.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.cateproject.batch.InputFileCleanupTasklet;
import org.cateproject.batch.ParameterConvertingTasklet;
import org.cateproject.batch.multimedia.ImageResizeProcessor;
import org.cateproject.batch.multimedia.MultimediaFetchingProcessor;
import org.cateproject.batch.multimedia.MultimediaFileWriter;
import org.cateproject.domain.Base;
import org.cateproject.domain.Multimedia;
import org.cateproject.domain.constants.MultimediaFileType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.OptimisticLockingFailureException;

@Configuration
public class ProcessMultimediaConfiguration
{

	public static final Logger logger = LoggerFactory.getLogger(ProcessMultimediaConfiguration.class);

	@Autowired
	private JobBuilderFactory jobs;

	@Autowired
	private StepBuilderFactory steps;

	@Autowired
	private JobRegistry jobRegistry;

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	@Autowired
	@Qualifier("itemWriter")
	private ItemWriter<Base> itemWriter;

	@Autowired
	@Qualifier("batchListener")
	private JobExecutionListener batchListener;

	@Bean
	public Job processMultimedia()
	{
		return jobs.get("processMultimedia").start(convertBatchParams()).next(processSingleMultimedia()).next(cleanUpResources()).listener(batchListener).build();
	}

	@Bean
	@StepScope
	public ParameterConvertingTasklet convertBatchParamsTasklet(@Value("#{jobParameters}") Map<String, String> jobParameters)
	{
		logger.debug("convertBatchParamsTasklet(JobParameters {})", new Object[] {jobParameters});
		ParameterConvertingTasklet parameterConvertingTasklet = new ParameterConvertingTasklet();
		parameterConvertingTasklet.setJobParameters(jobParameters);
		return parameterConvertingTasklet;
	}

	@Bean
	public Step convertBatchParams()
	{
		return steps.get("convertBatchParams").tasklet(convertBatchParamsTasklet(null)).listener(batchListener).build();
	}

	@Bean
	public Step cleanUpResources()
	{
		return steps.get("cleanupResources").tasklet(inputFileCleanupTasklet(null)).listener(batchListener).build();
	}

	@Bean
	@StepScope
	public InputFileCleanupTasklet inputFileCleanupTasklet(@Value("#{jobExecutionContext['input.file']}") String inputFile)
	{
		logger.debug("inputFileCleanupTasklet({})", new Object[] {inputFile});
		InputFileCleanupTasklet inputFileCleanupTasklet = new InputFileCleanupTasklet();
		inputFileCleanupTasklet.setInputFile(inputFile);
		return inputFileCleanupTasklet;
	}

	@Bean
	public Step processSingleMultimedia()
	{

		return steps.get("processSingleMultimedia").<Multimedia, Multimedia> chunk(1).faultTolerant().retryLimit(5).retry(OptimisticLockingFailureException.class)
		       .reader(multimediaFileReader(null, null))
		       .processor(multimediaFileProcessor())
		       .writer(multimediaWriter()).listener((Object)batchListener).build();
	}


	@Bean(destroyMethod = "")
	@StepScope
	public JpaPagingItemReader<Multimedia> multimediaFileReader(@Value("#{jobExecutionContext['query.string']}") String queryString, @Value("#{jobExecutionContext['query.parameters']}") Map<String, Object> parameterValues)
	{

		logger.debug("multimediaFileReader({},{})", new Object[] {queryString, parameterValues});
		JpaPagingItemReader<Multimedia> multimediaFileReader = new JpaPagingItemReader<Multimedia>();
		multimediaFileReader.setEntityManagerFactory(entityManagerFactory);
		multimediaFileReader.setQueryString(queryString);
		multimediaFileReader.setParameterValues(parameterValues);
		return multimediaFileReader;
	}

	@Bean
	@StepScope
	public ItemProcessor<Multimedia, Multimedia> multimediaFetchingProcessor(@Value("#{jobExecutionContext['input.file']}") String inputFile)
	{
		logger.debug("multimediaFetchingProcessor({})", new Object[] {inputFile});
		MultimediaFetchingProcessor multimediaFetchingProcessor = new MultimediaFetchingProcessor();
		multimediaFetchingProcessor.setUploadedFile(inputFile);
		return multimediaFetchingProcessor;
	}

	@Bean
	public ItemProcessor<Multimedia, Multimedia> multimediaFileProcessor()
	{
		List<ItemProcessor<Multimedia, Multimedia>> delegates = new ArrayList<ItemProcessor<Multimedia, Multimedia>>();
		delegates.add(multimediaFetchingProcessor(null));
		delegates.add(imageLargeProcessor());
		delegates.add(imageThumbnailProcessor());
		CompositeItemProcessor<Multimedia, Multimedia> multimediaFileProcessor = new CompositeItemProcessor<Multimedia, Multimedia>();
		multimediaFileProcessor.setDelegates(delegates);
		return multimediaFileProcessor;
	}

	@Bean
	public ItemProcessor<Multimedia, Multimedia> imageLargeProcessor()
	{
		ImageResizeProcessor imageLargeProcessor = new ImageResizeProcessor();
		imageLargeProcessor.setType(MultimediaFileType.large);
		imageLargeProcessor.setResizeX(1000);
		imageLargeProcessor.setResizeY(1000);
		imageLargeProcessor.setResizeChar('>');
		return imageLargeProcessor;
	}

	@Bean
	public ItemProcessor<Multimedia, Multimedia> imageThumbnailProcessor()
	{
		ImageResizeProcessor imageThumbnailProcessor = new ImageResizeProcessor();
		imageThumbnailProcessor.setType(MultimediaFileType.thumbnail);
		imageThumbnailProcessor.setResizeX(100);
		imageThumbnailProcessor.setResizeY(100);
		imageThumbnailProcessor.setResizeChar('^');
		imageThumbnailProcessor.setGravity("center");
		imageThumbnailProcessor.setExtentX(100);
		imageThumbnailProcessor.setExtentY(100);
		return imageThumbnailProcessor;
	}

	@Bean
	public ItemWriter<Multimedia> multimediaWriter()
	{
		List<ItemWriter<? super Multimedia>> delegates = new ArrayList<ItemWriter<? super Multimedia>>();
		delegates.add(itemWriter);
		delegates.add(multimediaFileWriter());
		CompositeItemWriter<Multimedia> multimediaFileWriter = new CompositeItemWriter<Multimedia>();
		multimediaFileWriter.setDelegates(delegates);
		return multimediaFileWriter;
	}

	@Bean
	public ItemWriter<Multimedia> multimediaFileWriter()
	{
		return new MultimediaFileWriter();
	}
}
