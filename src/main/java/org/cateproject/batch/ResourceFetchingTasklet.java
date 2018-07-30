package org.cateproject.batch;

import java.io.File;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.cateproject.file.FileTransferService;
import org.cateproject.file.GetResourceClient;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceFetchingTasklet implements Tasklet
{
	private Logger logger = LoggerFactory.getLogger(ResourceFetchingTasklet.class);

	protected String resource;

	@Autowired
	private FileTransferService fileTransferService;

	@Autowired
	private GetResourceClient getResourceClient;

	@Value("${temporary.file.directory:#{systemProperties['java.io.tmpdir']}}")
	private FileSystemResource temporaryFileDirectory;

        public void setTemporaryFileDirectory(FileSystemResource temporaryFileDirectory) {
            this.temporaryFileDirectory = temporaryFileDirectory;
        }

        public void setFileTransferService(FileTransferService fileTransferService) {
            this.fileTransferService = fileTransferService;
        }

        public void setGetResourceClient(GetResourceClient getResourceClient) {
            this.getResourceClient = getResourceClient;
        }

	public ResourceFetchingTasklet(String resource)
	{
		this.resource = resource;
	}

	/**
	 *  Copies this file to the working directory specified in 'working.dir' which is assumed to be relative to
	 *  the temporary directory. Creates a new random filename for the created file with the same file extension
	 *  as the input file. Puts the relative path of the input file (relative to the temporary directory), in 'local.file'
	 *  in the job execution context
	 */
	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception
	{
		String extension = FilenameUtils.getExtension(resource);

		String workingDirName = (String)chunkContext.getStepContext().getJobExecutionContext().get("working.dir");
		File workingDir = new File(temporaryFileDirectory.getFile(), workingDirName);
		workingDir.mkdirs();

		String localFileName = UUID.randomUUID().toString() + "." + extension;
		File localFile = new File(workingDir, localFileName);
		if (resource.startsWith("upload://") || resource.startsWith("static://")) {
			fileTransferService.copyFileIn(resource, localFile);
		} else {
			getResourceClient.getResource(resource, localFile);
		}

		chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("local.file", temporaryFileDirectory.getFile().toPath().relativize(localFile.toPath()).toString());
		return RepeatStatus.FINISHED;
	}
}
