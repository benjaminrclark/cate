package org.cateproject.batch;

import static org.junit.Assert.*;

import java.io.File;
import java.util.zip.ZipException;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.test.MetaDataInstanceFactory;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

public class ResourceUnzippingTaskletTest {
  
    ResourceUnzippingTasklet resourceUnzippingTasklet;

    FileSystemResource temporaryFileDirectory;

    File tempDir;

    File testResource;

    @Before
    public void setUp() throws Exception {
        tempDir = new File(System.getProperty("java.io.tmpdir"));
	FileSystemResource fileSystemResource = new FileSystemResource("src/test/resources/org/cateproject/batch/test.zip");
        
        File testResource = new File(tempDir,"test.zip");
        FileUtils.copyFile(fileSystemResource.getFile(), testResource);

	FileSystemResource corruptResource = new FileSystemResource("src/test/resources/org/cateproject/batch/corrupt.zip");
        
        File corruptTestResource = new File(tempDir,"corrupt.zip");
        FileUtils.copyFile(corruptResource.getFile(), corruptTestResource);

        resourceUnzippingTasklet = new ResourceUnzippingTasklet("test.zip");

        temporaryFileDirectory = EasyMock.createMock(FileSystemResource.class);
        resourceUnzippingTasklet.setTemporaryFileDirectory(temporaryFileDirectory);

    }


    @Test
    public void testExecute() throws Exception {
        File workingDir = new File(tempDir.getAbsolutePath() + File.separator + "working.dir");
        if(workingDir.exists()) {
           FileUtils.deleteDirectory(workingDir); 
        }
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();
        StepContribution stepContribution = new StepContribution(stepExecution);
        StepContext stepContext = new StepContext(stepExecution);
        stepExecution.getJobExecution().getExecutionContext().putString("working.dir", "working.dir");
        ChunkContext chunkContext = new ChunkContext(stepContext);
        EasyMock.expect(temporaryFileDirectory.getFile()).andReturn(tempDir).times(2);

        EasyMock.replay(temporaryFileDirectory);
        assertEquals("execute should return RepeatStatus.FINISHED", RepeatStatus.FINISHED, resourceUnzippingTasklet.execute(stepContribution, chunkContext));
        EasyMock.verify(temporaryFileDirectory);
    }

    @Test(expected = ZipException.class)
    public void testExecuteFileNotFound() throws Exception {
        resourceUnzippingTasklet.inputFile = "corrupt.zip";
        File workingDir = new File(tempDir.getAbsolutePath() + File.separator + "working.dir");
        if(workingDir.exists()) {
           FileUtils.deleteDirectory(workingDir); 
        }
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();
        StepContribution stepContribution = new StepContribution(stepExecution);
        StepContext stepContext = new StepContext(stepExecution);
        stepExecution.getJobExecution().getExecutionContext().putString("working.dir", "working.dir");
        ChunkContext chunkContext = new ChunkContext(stepContext);
        EasyMock.expect(temporaryFileDirectory.getFile()).andReturn(tempDir).times(2);

        EasyMock.replay(temporaryFileDirectory);
        assertEquals("execute should return RepeatStatus.FINISHED", RepeatStatus.FINISHED, resourceUnzippingTasklet.execute(stepContribution, chunkContext));
        EasyMock.verify(temporaryFileDirectory);
    }
}
