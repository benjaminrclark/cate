package org.cateproject.batch.job.darwincore;

import static org.junit.Assert.*;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import org.cateproject.domain.batch.BatchDataset;
import org.cateproject.repository.jpa.batch.BatchDatasetRepository;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.test.MetaDataInstanceFactory;

public class DecideImportStrategyTaskletTest {

    DecideImportStrategyTasklet tasklet;

    BatchDatasetRepository batchDatasetRepository;

    BatchDataset batchDataset;

    @Before
    public void setUp() throws Exception {
        tasklet = new DecideImportStrategyTasklet("identifier");

        batchDatasetRepository = EasyMock.createMock(BatchDatasetRepository.class);
        tasklet.setBatchDatasetRepository(batchDatasetRepository);

        batchDataset = new BatchDataset();
        batchDataset.setIdentifier("identifier");
    }

    @Test
    public void testExecuteValidChangeDumpManifest() throws Exception {
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();
        StepContribution stepContribution = new StepContribution(stepExecution);
        StepContext stepContext = new StepContext(stepExecution);
        ChunkContext chunkContext = new ChunkContext(stepContext);
        ExitStatus expectedExitStatus = new ExitStatus("VALID CHANGE DUMP MANIFEST");

        EasyMock.expect(batchDatasetRepository.findByIdentifier("identifier")).andReturn(batchDataset);

        EasyMock.replay(batchDatasetRepository);
        assertEquals("execute should return RepeatStatus.FINISHED", RepeatStatus.FINISHED, tasklet.execute(stepContribution, chunkContext));
        assertEquals("execute should set the exitStatus to 'VALID CHANGE DUMP MANIFEST'", expectedExitStatus, stepContribution.getExitStatus());
        EasyMock.verify(batchDatasetRepository);
    }

    @Test
    public void testExecuteDatasetNotFound() throws Exception {
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();
        StepContribution stepContribution = new StepContribution(stepExecution);
        StepContext stepContext = new StepContext(stepExecution);
        ChunkContext chunkContext = new ChunkContext(stepContext);
        ExitStatus expectedExitStatus = new ExitStatus("DATASET NOT FOUND");

        EasyMock.expect(batchDatasetRepository.findByIdentifier("identifier")).andReturn(null);

        EasyMock.replay(batchDatasetRepository);
        assertEquals("execute should return RepeatStatus.FINISHED", RepeatStatus.FINISHED, tasklet.execute(stepContribution, chunkContext));
        assertEquals("execute should set the exitStatus to 'DATASET NOT FOUND'", expectedExitStatus, stepContribution.getExitStatus());
        EasyMock.verify(batchDatasetRepository);
    }
}
