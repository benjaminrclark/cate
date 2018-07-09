package org.cateproject.batch;

import static org.junit.Assert.assertEquals;

import org.cateproject.file.FileTransferService;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.test.MetaDataInstanceFactory;

public class InputFileCleanupTaskletTest {

    private InputFileCleanupTasklet inputFileCleanupTasklet;

    private FileTransferService fileTransferService;

    private ChunkContext chunkContext;

    private StepContribution stepContribution;

    @Before
    public void setUp() {
        inputFileCleanupTasklet = new InputFileCleanupTasklet();
        fileTransferService = EasyMock.createMock(FileTransferService.class);
        inputFileCleanupTasklet.setFileTransferService(fileTransferService);

        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();
        chunkContext = new ChunkContext(new StepContext(stepExecution));
        stepContribution = new StepContribution(stepExecution);
    }

    @Test
    public void testExecute() throws Exception {

        EasyMock.expect(fileTransferService.delete(EasyMock.eq("INPUT_FILE"))).andReturn(true);
        inputFileCleanupTasklet.setInputFile("INPUT_FILE");

        EasyMock.replay(fileTransferService);
        assertEquals("execute should return 'FINISHED'", RepeatStatus.FINISHED, inputFileCleanupTasklet.execute(stepContribution, chunkContext));
        EasyMock.verify(fileTransferService);
    }
}
