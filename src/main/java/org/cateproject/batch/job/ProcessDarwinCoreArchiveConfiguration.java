package org.cateproject.batch.job;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.cateproject.batch.BatchListener;
import org.cateproject.batch.InputFileCleanupTasklet;
import org.cateproject.batch.ParameterConvertingTasklet;
import org.cateproject.batch.ResourceFetchingTasklet;
import org.cateproject.batch.ResourceStoringTasklet;
import org.cateproject.batch.ResourceUnzippingTasklet;
import org.cateproject.batch.job.ResolutionService;
import org.cateproject.batch.job.darwincore.ArchiveMetadataReadingTasklet;
import org.cateproject.batch.job.darwincore.BatchLineMapper;
import org.cateproject.batch.job.darwincore.DecideImportStrategyTasklet;
import org.cateproject.batch.job.darwincore.HaveRemoteDatasetDecider;
import org.cateproject.batch.job.darwincore.SelectNextArchiveFileTasklet;
import org.cateproject.batch.sync.ChangeDumpManifestProcessor;
import org.cateproject.batch.sync.ChangeProcessor;
import org.cateproject.batch.sync.ChangeWriter;
import org.cateproject.domain.batch.BatchLine;
import org.cateproject.domain.convert.sync.DateTimeConverter;
import org.cateproject.domain.sync.ChangeManifestChange;
import org.cateproject.domain.sync.ChangeManifestUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;

@Configuration
public class ProcessDarwinCoreArchiveConfiguration
{

	public static final Logger logger = LoggerFactory.getLogger(ProcessDarwinCoreArchiveConfiguration.class);

	@Autowired
	private JobBuilderFactory jobs;

	@Autowired
	private StepBuilderFactory steps;

	@Autowired
	JobRegistry jobRegistry;

	@Autowired
	@Qualifier("batchListener")
	private BatchListener batchListener;

        @Autowired
        @Qualifier("itemWriter")
        private ItemWriter itemWriter;

        @Autowired
        private EntityManagerFactory entityManagerFactory;

        @Autowired
        private ResolutionService resolutionService;
      
	@Bean
	public Job processDarwinCoreArchive()
	{
		return jobs.get("processDarwinCoreArchive") // TODO validate parameters
		       .start(convertBatchParams())
		       .next(haveRemoteDataset())  // Decision
		       .on("Remote Dataset").to(fetchRemoteDataset()).next(processLocalDataset())
		       .from(haveRemoteDataset()).on("Local Dataset").to(processLocalDataset())
		       .end()
		       .listener(batchListener).build();
	}

	@Bean
	public JobExecutionDecider haveRemoteDataset()
	{
		return new HaveRemoteDatasetDecider("input.file");
	}

	@Bean
	public Flow fetchRemoteDataset()
	{
		return new FlowBuilder<Flow>("fetchRemoteDataset").start(fetchResource()).next(storeDataset()).end() ;
	}

	@Bean
	public Flow processLocalDataset()
	{
		return new FlowBuilder<Flow>("processLocalDataset").start(fetchResource()).next(unzipResource()).next(readArchiveMetadata()).next(decideImportStrategy()).on("VALID CHANGE DUMP MANIFEST").to(importChangeDump()).next(cleanUpResources()).from(decideImportStrategy()).on("INVALID CHANGE DUMP MANIFEST").to(cleanUpResources()).from(decideImportStrategy()).on("DATASET NOT FOUND").to(cleanUpResources()).end();
	}

        @Bean
        public Step decideImportStrategy() {
            return steps.get("decideImportStrategy").tasklet(decideImportStrategyTasklet(null)).listener(batchListener).build();
        }

        @Bean
	@StepScope
        public Tasklet decideImportStrategyTasklet(@Value("#{jobExecutionContext['dataset.identifier']}") String datasetIdentifier) {
            return new DecideImportStrategyTasklet(datasetIdentifier);
        }

        @Bean
        public Flow importChangeDump() {
	    return new FlowBuilder<Flow>("importChangeDump").start(readFiles()).next(processChangeDumpManifest()).next(processChanges()).next(cleanUpResources()).end();
        }

        @Bean
        public Step processChangeDumpManifest() {
            return steps.get("processChangeDumpManifest").listener(batchListener).chunk(10).reader(changeDumpManifestReader(null,null)).processor(changeDumpManifestProcessor(null)).writer(itemWriter).build();
        }

        @Bean
        public Step processChanges() {
            ChangeProcessor changeProcessor = changeProcessor(null);
            return steps.get("processChanges").listener(batchListener).listener(resolutionService).listener(changeProcessor).chunk(10).reader(changeReader(null)).processor((ItemProcessor)changeProcessor).writer(changeWriter()).build();
        }

        @Bean
        public ItemWriter changeWriter() {
           return new ChangeWriter();
        }

        @Bean
        @StepScope
        public ChangeProcessor changeProcessor(@Value("#{jobExecutionContext['dataset.identifier']}") String datasetIdentifier) {
            return new ChangeProcessor(datasetIdentifier);
        }

        @Bean
        @StepScope 
        public JpaPagingItemReader<BatchLine> changeReader(@Value("#{jobExecutionContext['dataset.identifier']}") String datasetIdentifier) {
             JpaPagingItemReader<BatchLine> itemReader = new JpaPagingItemReader<BatchLine>();
             itemReader.setQueryString("select l from BatchLine l join l.file as f join f.dataset as d join l.changeManifestUrl as u where d.identifier = :datasetIdentifier order by u.lastmod asc");
             itemReader.setEntityManagerFactory(entityManagerFactory);
             Map<String,Object> parameterValues = new HashMap<String,Object>();
             parameterValues.put("datasetIdentifier", datasetIdentifier);
             itemReader.setParameterValues(parameterValues);
             return itemReader;
        }

        @Bean
        @StepScope
        public ItemProcessor changeDumpManifestProcessor(@Value("#{jobExecutionContext['dataset.identifier']}") String datasetIdentifier) {
            return new ChangeDumpManifestProcessor(datasetIdentifier);
        }

        @Bean
        @StepScope
        public StaxEventItemReader<ChangeManifestUrl> changeDumpManifestReader(
	           @Value("${temporary.file.directory:#{systemProperties['java.io.tmpdir']}}") FileSystemResource temporaryFileDirectory,
                   @Value("#{jobExecutionContext['working.dir']}") String workingDir) {
            File workingDirectory = new File(temporaryFileDirectory.getFile(), workingDir);
            String changeDumpManifestFileName = workingDirectory.getAbsolutePath() +  File.separator + "manifest.xml";
            logger.debug("Reading file {}", changeDumpManifestFileName); 
            StaxEventItemReader<ChangeManifestUrl> staxEventItemReader = new StaxEventItemReader();
            staxEventItemReader.setFragmentRootElementName("url");
            staxEventItemReader.setResource(new FileSystemResource(changeDumpManifestFileName));
            staxEventItemReader.setUnmarshaller(unmarshaller());
            return staxEventItemReader;
        }

        @Bean
        public Unmarshaller unmarshaller() {
            XStreamMarshaller unmarshaller = new XStreamMarshaller();
            
            Map<String,String> aliases = new HashMap<String,String>();
            aliases.put("url","org.cateproject.domain.sync.ChangeManifestUrl");
            unmarshaller.setAliases(aliases); 
            
            unmarshaller.setConverters(new DateTimeConverter());

            List<String> changeManifestChangeAttributes = new ArrayList<String>();
            changeManifestChangeAttributes.add("change");
            changeManifestChangeAttributes.add("hash");
            changeManifestChangeAttributes.add("length");
            changeManifestChangeAttributes.add("path");
            changeManifestChangeAttributes.add("type");
            Map<Class,Object> attributeAliases = new HashMap<Class,Object>();
            attributeAliases.put(ChangeManifestChange.class,changeManifestChangeAttributes);
            unmarshaller.setUseAttributeFor(attributeAliases);

            return unmarshaller;
        }   

        @Bean
        public Flow readFiles() {
		return new FlowBuilder<Flow>("readFiles").start(selectNextArchiveFile()).on("CONTINUE").to(processDarwinCoreArchiveFile()).next(selectNextArchiveFile()).from(selectNextArchiveFile()).on("COMPLETED").end().end();
        }

        @Bean
        public Step processDarwinCoreArchiveFile() {
            return steps.get("processDarwinCoreArchiveFile").listener(batchListener).chunk(10).reader(darwinCoreFileReader(null, null, null, null)).writer(itemWriter).build();
        }

        @Bean
        @StepScope
        public FlatFileItemReader<BatchLine> darwinCoreFileReader(@Value("#{jobExecutionContext['dwca.encoding']}") String encoding,
	           @Value("${temporary.file.directory:#{systemProperties['java.io.tmpdir']}}") FileSystemResource temporaryFileDirectory,
                   @Value("#{jobExecutionContext['working.dir']}") String workingDir,
                   @Value("#{jobExecutionContext['dwca.file']}") String file) {
            FlatFileItemReader<BatchLine> darwinCoreFileReader = new FlatFileItemReader<BatchLine>();
            darwinCoreFileReader.setEncoding(encoding);
            File workingDirectory = new File(temporaryFileDirectory.getFile(), workingDir);
            String darwinCoreFileName = workingDirectory.getAbsolutePath() +  File.separator + file;
            logger.debug("Reading file {}", darwinCoreFileName); 
            darwinCoreFileReader.setResource(new FileSystemResource(darwinCoreFileName));
            darwinCoreFileReader.setLineMapper(batchLineMapper(null));
            return darwinCoreFileReader;
        }

        @Bean
        @StepScope
        public LineMapper<BatchLine> batchLineMapper(@Value("#{jobExecutionContext['dwca.file.id']}") Long id) {
            return new BatchLineMapper(id);
        }

	@Bean
	public Step selectNextArchiveFile()
	{
		return steps.get("selectNextArchiveFile").tasklet(selectNextArchiveFileTasklet(null, null)).listener(batchListener).build();
	}

	@Bean
	@StepScope
	public Tasklet selectNextArchiveFileTasklet(@Value("#{jobExecutionContext['dataset.identifier']}") String datasetIdentifier, @Value("#{jobExecutionContext['archive.file.index']}") Integer archiveIndex)
	{
		return new SelectNextArchiveFileTasklet(datasetIdentifier, archiveIndex);
	}

	@Bean
	public Step readArchiveMetadata()
	{
		return steps.get("readArchiveMetadata").tasklet(readArchiveMetadataTasklet(null)).listener(batchListener).build();
	}

	@Bean
	@StepScope
	public Tasklet readArchiveMetadataTasklet(@Value("#{jobExecutionContext['working.dir']}") String workingDir)
	{
		return new ArchiveMetadataReadingTasklet(workingDir);
	}

	@Bean
	public Step storeDataset()
	{
		return steps.get("storeDataset").tasklet(resourceStoringTasklet(null)).listener(batchListener).build();
	}

	@Bean
	@StepScope
	public Tasklet resourceStoringTasklet(@Value("#{jobExecutionContext['local.file']}") String localFile)
	{
		return new ResourceStoringTasklet(localFile);
	}

	@Bean
	@StepScope
	public Tasklet resourceUnzippingTasklet(@Value("#{jobExecutionContext['local.file']}") String localFile)
	{
		return new ResourceUnzippingTasklet(localFile);
	}

	@Bean
	public Step fetchResource()
	{
		return steps.get("fetchResource").tasklet(resourceFetchingTasklet(null)).listener(batchListener).build();
	}

	@Bean
	@StepScope
	public Tasklet resourceFetchingTasklet(@Value("#{jobExecutionContext['input.file']}") String inputFile)
	{
		return new ResourceFetchingTasklet(inputFile);
	}

	@Bean
	public Step unzipResource()
	{
		return steps.get("unzipResource").tasklet(resourceUnzippingTasklet(null)).listener(batchListener).build();
	}

	@Bean
	@StepScope
	public ParameterConvertingTasklet convertBatchParamsTasklet(@Value("#{jobParameters}") Map<String, String> jobParameters)
	{
		logger.debug("JobParameters {}", new Object[] {jobParameters});
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
		InputFileCleanupTasklet inputFileCleanupTasklet = new InputFileCleanupTasklet();
		inputFileCleanupTasklet.setInputFile(inputFile);
		return inputFileCleanupTasklet;
	}
}
