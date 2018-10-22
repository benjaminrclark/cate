package org.cateproject.batch;

import static org.junit.Assert.assertNull;

import java.util.ArrayList;

import org.cateproject.domain.Taxon;
import org.cateproject.repository.search.batch.JobExecutionRepository;
import org.cateproject.repository.search.batch.StepExecutionRepository;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.MetaDataInstanceFactory;

public class BatchListenerTest {

    private BatchListener batchListener;

    private JobExecutionRepository jobExecutionRepository;

    private StepExecutionRepository stepExecutionRepository;

    @Before
    public void setUp() {
        batchListener = new BatchListener();
        jobExecutionRepository = EasyMock.createMock(JobExecutionRepository.class);
        stepExecutionRepository = EasyMock.createMock(StepExecutionRepository.class);
        batchListener.setJobExecutionRepository(jobExecutionRepository);
        batchListener.setStepExecutionRepository(stepExecutionRepository);
    }

    @Test
    public void testOnProcessError() {
        EasyMock.replay(jobExecutionRepository,stepExecutionRepository);
        batchListener.onProcessError(new Taxon(), new RuntimeException("EXCEPTION"));
        EasyMock.verify(jobExecutionRepository,stepExecutionRepository);
    }

    @Test
    public void testOnReadError() {
        EasyMock.replay(jobExecutionRepository,stepExecutionRepository);
        batchListener.onReadError(new RuntimeException("EXCEPTION"));
        EasyMock.verify(jobExecutionRepository,stepExecutionRepository);
    }

    @Test
    public void testOnWriteError() {
        EasyMock.replay(jobExecutionRepository,stepExecutionRepository);
        batchListener.onWriteError(new RuntimeException("EXCEPTION"), new ArrayList<Taxon>());
        EasyMock.verify(jobExecutionRepository,stepExecutionRepository);
    }

    @Test
    public void testBeforeWrite() {
        EasyMock.replay(jobExecutionRepository,stepExecutionRepository);
        batchListener.beforeWrite(new ArrayList<Taxon>());
        EasyMock.verify(jobExecutionRepository,stepExecutionRepository);
    }

    @Test
    public void testBeforeJob() {
        JobExecution jobExecution = MetaDataInstanceFactory.createJobExecution();
        EasyMock.expect(jobExecutionRepository.save(EasyMock.eq(jobExecution))).andReturn(jobExecution);        

        EasyMock.replay(jobExecutionRepository,stepExecutionRepository);
        batchListener.beforeJob(jobExecution);
        EasyMock.verify(jobExecutionRepository,stepExecutionRepository);
    }

    @Test
    public void testAfterJob() {
        JobExecution jobExecution = MetaDataInstanceFactory.createJobExecution();
        EasyMock.expect(jobExecutionRepository.save(EasyMock.eq(jobExecution))).andReturn(jobExecution);        

        EasyMock.replay(jobExecutionRepository,stepExecutionRepository);
        batchListener.afterJob(jobExecution);
        EasyMock.verify(jobExecutionRepository,stepExecutionRepository);
    }

    @Test
    public void testAfterJobFailed() {
        JobExecution jobExecution = MetaDataInstanceFactory.createJobExecution();
        jobExecution.addFailureException(new RuntimeException("EXCEPTION"));
        EasyMock.expect(jobExecutionRepository.save(EasyMock.eq(jobExecution))).andReturn(jobExecution);        

        EasyMock.replay(jobExecutionRepository,stepExecutionRepository);
        batchListener.afterJob(jobExecution);
        EasyMock.verify(jobExecutionRepository,stepExecutionRepository);
    }

    @Test
    public void testAfterJobFailedNestedException() {
        JobExecution jobExecution = MetaDataInstanceFactory.createJobExecution();
        jobExecution.addFailureException(new RuntimeException("EXCEPTION", new RuntimeException("NESTED_EXCEPTION")));
        EasyMock.expect(jobExecutionRepository.save(EasyMock.eq(jobExecution))).andReturn(jobExecution);        

        EasyMock.replay(jobExecutionRepository,stepExecutionRepository);
        batchListener.afterJob(jobExecution);
        EasyMock.verify(jobExecutionRepository,stepExecutionRepository);
    }

    @Test
    public void testAfterStep() {
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();
        EasyMock.expect(stepExecutionRepository.save(EasyMock.eq(stepExecution))).andReturn(stepExecution);
        EasyMock.replay(jobExecutionRepository,stepExecutionRepository);
        assertNull("afterStep should return NULL",batchListener.afterStep(stepExecution));
        EasyMock.verify(jobExecutionRepository,stepExecutionRepository);
    }

    @Test
    public void testBeforeStep() {
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();
        EasyMock.expect(stepExecutionRepository.save(EasyMock.eq(stepExecution))).andReturn(stepExecution);
        EasyMock.replay(jobExecutionRepository,stepExecutionRepository);
        batchListener.beforeStep(stepExecution);
        EasyMock.verify(jobExecutionRepository,stepExecutionRepository);
    }
}
