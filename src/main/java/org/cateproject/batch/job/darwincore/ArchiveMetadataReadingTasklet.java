package org.cateproject.batch.job.darwincore;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cateproject.domain.batch.BatchDataset;
import org.cateproject.repository.jpa.batch.BatchDatasetRepository;
import org.gbif.dwc.text.Archive;
import org.gbif.dwc.text.ArchiveFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.FileSystemResource;

public class ArchiveMetadataReadingTasklet implements Tasklet
{
        private static final Logger logger = LoggerFactory.getLogger(ArchiveMetadataReadingTasklet.class);

	private String archiveDir;

	@Autowired
	private BatchDatasetRepository batchDatasetRepository;

	@Autowired
	private ConversionService conversionService;

	@Value("${temporary.file.directory:#{systemProperties['java.io.tmpdir']}}")
	private FileSystemResource temporaryFileDirectory;

	public ArchiveMetadataReadingTasklet(String archiveDir)
	{
		this.archiveDir = archiveDir;
	}

	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception
	{
		File archiveDirectory = new File(temporaryFileDirectory.getFile().getAbsolutePath() + File.separator + archiveDir);
		Archive archive = ArchiveFactory.openArchive(archiveDirectory);
		BatchDataset batchDataset = conversionService.convert(archive, BatchDataset.class);
                detectChangeDumpManifest(batchDataset, archiveDirectory);
		batchDatasetRepository.save(batchDataset);
                logger.debug("Saved Batch Dataset with identifier {}", new Object[]{batchDataset.getIdentifier()});
		chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().putString("dataset.identifier", batchDataset.getIdentifier());
		return RepeatStatus.FINISHED;
	}

        public void detectChangeDumpManifest(BatchDataset batchDataset, File archiveDirectory) {
            File changeDumpManifest = new File(archiveDirectory, "manifest.xml");
            if(changeDumpManifest.exists() && changeDumpManifest.canRead()) {
                batchDataset.setChangeDumpManifestPresent(true);
            }
        }

}
