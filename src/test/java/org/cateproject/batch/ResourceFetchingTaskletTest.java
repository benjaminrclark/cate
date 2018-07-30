package org.cateproject.batch;

import static org.junit.Assert.*;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.cateproject.file.FileTransferService;
import org.cateproject.file.GetResourceClient;
import org.joda.time.DateTime;
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

public class ResourceFetchingTaskletTest {
  
    ResourceFetchingTasklet resourceFetchingTasklet;

    FileSystemResource temporaryFileDirectory;

    FileTransferService fileTransferService;

    GetResourceClient getResourceClient;
 
    File tempDir;

    @Before
    public void setUp() throws Exception {
        tempDir = new File(System.getProperty("java.io.tmpdir"));

        resourceFetchingTasklet = new ResourceFetchingTasklet("resource.txt");

        temporaryFileDirectory = EasyMock.createMock(FileSystemResource.class);
        resourceFetchingTasklet.setTemporaryFileDirectory(temporaryFileDirectory);

        fileTransferService = EasyMock.createMock(FileTransferService.class);
        resourceFetchingTasklet.setFileTransferService(fileTransferService);

        getResourceClient = EasyMock.createMock(GetResourceClient.class);
        resourceFetchingTasklet.setGetResourceClient(getResourceClient);
    }


    @Test
    public void testExecuteRemoteResource() throws Exception {
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
        EasyMock.expect(getResourceClient.getResource(EasyMock.eq("resource.txt"), EasyMock.isA(File.class))).andReturn(new DateTime());

        EasyMock.replay(temporaryFileDirectory, fileTransferService, getResourceClient);
        assertEquals("execute should return RepeatStatus.FINISHED", RepeatStatus.FINISHED, resourceFetchingTasklet.execute(stepContribution, chunkContext));

        assertTrue("execute set the value of 'local.file'", chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().containsKey("local.file"));
        EasyMock.verify(temporaryFileDirectory, fileTransferService, getResourceClient);
    }

    @Test
    public void testExecuteLocalResourceStatic() throws Exception {
        resourceFetchingTasklet.resource = "static://resource.txt";
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
        fileTransferService.copyFileIn(EasyMock.eq("static://resource.txt"), EasyMock.isA(File.class));

        EasyMock.replay(temporaryFileDirectory, fileTransferService, getResourceClient);
        assertEquals("execute should return RepeatStatus.FINISHED", RepeatStatus.FINISHED, resourceFetchingTasklet.execute(stepContribution, chunkContext));

        assertTrue("execute set the value of 'local.file'", chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().containsKey("local.file"));
        EasyMock.verify(temporaryFileDirectory, fileTransferService, getResourceClient);
    }

    @Test
    public void testExecuteLocalResourceUpload() throws Exception {
        resourceFetchingTasklet.resource = "upload://resource.txt";
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
        fileTransferService.copyFileIn(EasyMock.eq("upload://resource.txt"), EasyMock.isA(File.class));

        EasyMock.replay(temporaryFileDirectory, fileTransferService, getResourceClient);
        assertEquals("execute should return RepeatStatus.FINISHED", RepeatStatus.FINISHED, resourceFetchingTasklet.execute(stepContribution, chunkContext));

        assertTrue("execute set the value of 'local.file'", chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().containsKey("local.file"));
        EasyMock.verify(temporaryFileDirectory, fileTransferService, getResourceClient);
    }
}
