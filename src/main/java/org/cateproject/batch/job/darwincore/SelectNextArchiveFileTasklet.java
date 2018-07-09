package org.cateproject.batch.job.darwincore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;

import org.cateproject.domain.batch.BatchDataset;
import org.cateproject.domain.batch.BatchField;
import org.cateproject.domain.batch.BatchFile;
import org.cateproject.repository.jpa.batch.BatchDatasetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;

public class SelectNextArchiveFileTasklet implements Tasklet
{
        private static final Logger logger = LoggerFactory.getLogger(SelectNextArchiveFileTasklet.class);

	private String datasetIdentifier;

	private Integer batchFileIndex;

	@Autowired
	private ConversionService conversionService;

	@Autowired
	private BatchDatasetRepository batchDatasetRepository;

	public SelectNextArchiveFileTasklet(String datasetIdentifier, Integer batchFileIndex)
	{
		this.datasetIdentifier = datasetIdentifier;
		this.batchFileIndex = batchFileIndex;
	}

	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception
	{
                logger.debug("Selecting dataset with identifier {}", new Object[]{datasetIdentifier});
		BatchDataset batchDataset = batchDatasetRepository.findByIdentifier(datasetIdentifier);
		if (batchFileIndex == null)
		{
			// we've not set things up yet
			BatchFile core = batchDataset.getCore();
			chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("archive.file.index", 0);
			setCurrentProcessingContext(core, chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext());
			stepContribution.setExitStatus(new ExitStatus("CONTINUE"));
		} else {
			List<BatchFile> extensions = new ArrayList<BatchFile>(batchDataset.getExtensions());
			Collections.sort(extensions);			
			
			Integer index = (Integer)chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().get("archive.file.index");
			if(index >= extensions.size()) {
				chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().remove("archive.file.index");
				stepContribution.setExitStatus(new ExitStatus("COMPLETED"));
			} else {
				setCurrentProcessingContext(extensions.get(index),chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext());
				chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("archive.file.index", index + 1);
				stepContribution.setExitStatus(new ExitStatus("CONTINUE"));
			}
                }
		return RepeatStatus.FINISHED;
	}

	private void setCurrentProcessingContext(BatchFile batchFile, ExecutionContext executionContext)
	{
		executionContext.put("dwca.file", batchFile.getLocation());
		executionContext.put("dwca.file.id", batchFile.getId());
		executionContext.put("dwca.extension", batchFile.getRowType());
		executionContext.put("dwca.encoding", batchFile.getEncoding());

		if (batchFile.getIgnoreHeaderLines() != null)
		{
			executionContext.put("dwca.ignoreHeaderLines", batchFile.getIgnoreHeaderLines());
		}
		else
		{
			executionContext.put("dwca.ignoreHeaderLines", 0);
		}

		executionContext.put("dwca.fieldsTerminatedBy", batchFile.getFieldsTerminatedBy());

		if (batchFile.getFieldsEnclosedBy() != null)
		{
			executionContext.put("dwca.fieldsEnclosedBy", batchFile.getFieldsEnclosedBy());
		}
		else
		{
			executionContext.put("dwca.fieldsEnclosedBy", '\u0000');
		}

		SortedSet<BatchField> archiveFields = new TreeSet<BatchField>(batchFile.getFields());
		Integer maxIndex = Integer.MIN_VALUE; 
		for (BatchField field : archiveFields)
		{
			if (field.getIndex() != null)    // fields with default values don't necessarily have an index
			{
				maxIndex = Math.max(maxIndex, field.getIndex());
			}
		}
		List<String> fieldNames = new ArrayList<String>(maxIndex + 1);
		for (int i = 0; i <= maxIndex; i++)
		{
			fieldNames.add("");
		}

		for (BatchField archiveField : archiveFields)
		{
			logger.info("Archive contains field {} {}", new Object[]{archiveField.getTerm(), archiveField.getIndex()});
			if (archiveField.getIndex() != null)
			{
				if (archiveField.getDefaultValue() != null)
				{
					fieldNames.set(archiveField.getIndex(), archiveField.getTerm().toString() + " " + archiveField.getDefaultValue());
				}
				else
				{
					fieldNames.set(archiveField.getIndex(), archiveField.getTerm().toString());
				}
			}
		}
		logger.info("Archive contains field names " + fieldNames);

		executionContext.put("dwca.fieldNames_array", fieldNames.toArray(new String[fieldNames.size()]));

		Map<String, String> defaultValues = new HashMap<String, String>();
		for (BatchField archiveField : archiveFields)
		{
			if (archiveField.getDefaultValue() != null && archiveField.getIndex() == null)
			{
				defaultValues.put(archiveField.getTerm().toString(),
				                  archiveField.getDefaultValue());
			}
		}

		executionContext.put("dwca.defaultValues_map", defaultValues);

		for (Entry<String, Object> entry : executionContext.entrySet())
		{
			logger.debug("ExecutionContext contains " + entry.getKey() + " " + entry.getValue());
		}
	}
}
