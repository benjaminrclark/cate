package org.cateproject.batch;

import static org.junit.Assert.*;

import java.io.File;

import org.cateproject.file.FileTransferService;
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

public class ResourceStoringTaskletTest {
  
    ResourceStoringTasklet resourceStoringTasklet;

    FileSystemResource temporaryFileDirectory;

    FileTransferService fileTransferService;
 
    File tempDir;

    @Before
    public void setUp() throws Exception {
        tempDir = new File(System.getProperty("java.io.tmpdir"));

        resourceStoringTasklet = new ResourceStoringTasklet("resource.txt");

        temporaryFileDirectory = EasyMock.createMock(FileSystemResource.class);
        resourceStoringTasklet.setTemporaryFileDirectory(temporaryFileDirectory);

        fileTransferService = EasyMock.createMock(FileTransferService.class);
        resourceStoringTasklet.setFileTransferService(fileTransferService);
    }


    @Test
    public void testExecute() throws Exception {
        File expectedFile = new File(tempDir.getAbsolutePath() + File.separator + "resource.txt");
        if(!expectedFile.exists()) {
            assertTrue("We should be able to create a new file", expectedFile.createNewFile());
        }
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();
        StepContribution stepContribution = new StepContribution(stepExecution);
        StepContext stepContext = new StepContext(stepExecution);
        ChunkContext chunkContext = new ChunkContext(stepContext);
        EasyMock.expect(temporaryFileDirectory.getFile()).andReturn(tempDir);
        EasyMock.expect(fileTransferService.copyFileOut(EasyMock.eq(expectedFile), EasyMock.anyString())).andReturn("string");

        EasyMock.replay(temporaryFileDirectory, fileTransferService);
        assertEquals("execute should return RepeatStatus.FINISHED", RepeatStatus.FINISHED, resourceStoringTasklet.execute(stepContribution, chunkContext));

        assertTrue("execute set the value of 'input.file'", chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().containsKey("input.file"));
        EasyMock.verify(temporaryFileDirectory, fileTransferService);
    }
}
