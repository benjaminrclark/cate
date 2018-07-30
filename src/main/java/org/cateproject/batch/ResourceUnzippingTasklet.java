package org.cateproject.batch;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;

public class ResourceUnzippingTasklet implements Tasklet
{
	private static Logger logger = LoggerFactory.getLogger(ResourceUnzippingTasklet.class);

	@Value("${temporary.file.directory:#{systemProperties['java.io.tmpdir']}}")
	private FileSystemResource temporaryFileDirectory;

	protected String inputFile;

	static final int BUFFER = 2048;

	public ResourceUnzippingTasklet(String inputFile)
	{
		this.inputFile = inputFile;
	}

        public void setTemporaryFileDirectory(FileSystemResource temporaryFileDirectory) {
            this.temporaryFileDirectory = temporaryFileDirectory;
        }

	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception
	{
		ZipFile zipfile = null;
		try
		{

			String workingDirName = (String)chunkContext.getStepContext().getJobExecutionContext().get("working.dir");
			File workingDir = new File(temporaryFileDirectory.getFile(), workingDirName);
			workingDir.mkdirs();

			BufferedOutputStream bufferedOutputStream = null;
			BufferedInputStream bufferedInputStream = null;
			ZipEntry entry = null;
			zipfile = new ZipFile(temporaryFileDirectory.getFile().getAbsoluteFile() + File.separator + inputFile);
			Enumeration<? extends ZipEntry> entries = zipfile.entries();
			while (entries.hasMoreElements())
			{
				entry = entries.nextElement();
				if (entry.getName().indexOf("/") != -1)
				{
					// entry is in a subdir
					String subDirectoryName = entry.getName().substring(0, entry.getName().indexOf("/"));
					File subDirectory = new File(workingDir, subDirectoryName);
					if (!subDirectory.exists())
					{
						subDirectory.mkdirs();
					}
				}
				logger.debug("Extracting: " + entry + " from " + inputFile);
				bufferedInputStream = new BufferedInputStream(zipfile.getInputStream(entry));
				int count;
				byte[] data = new byte[BUFFER];
				FileOutputStream fileOutputStream = new FileOutputStream(new File(workingDir, entry.getName()));
				bufferedOutputStream = new BufferedOutputStream(fileOutputStream, BUFFER);

				while ((count = bufferedInputStream.read(data, 0, BUFFER)) != -1)
				{
					bufferedOutputStream.write(data, 0, count);
				}
				bufferedOutputStream.flush();
				bufferedOutputStream.close();
				bufferedInputStream.close();
			}
		} finally {
			if (zipfile != null) {
				zipfile.close();
			}
		}
		return RepeatStatus.FINISHED;
	}
}
