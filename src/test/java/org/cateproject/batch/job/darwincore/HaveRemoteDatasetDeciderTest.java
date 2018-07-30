package org.cateproject.batch.job.darwincore;

import static org.junit.Assert.*;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.test.MetaDataInstanceFactory;

public class HaveRemoteDatasetDeciderTest {

    HaveRemoteDatasetDecider decider;

    @Before
    public void setUp() throws Exception {
        decider = new HaveRemoteDatasetDecider("resourceName");
    }

    @Test
    public void testDecideRemote() {
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();
        JobExecution jobExecution = MetaDataInstanceFactory.createJobExecution();
        FlowExecutionStatus expectedStatus = new FlowExecutionStatus("Remote Dataset");

        jobExecution.getExecutionContext().putString("resourceName", "s3://resource.zip");  
        assertEquals("decide should return the expected status", expectedStatus, decider.decide(jobExecution, stepExecution));
    }

    @Test
    public void testDecideLocalUpload() {
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();
        JobExecution jobExecution = MetaDataInstanceFactory.createJobExecution();
        FlowExecutionStatus expectedStatus = new FlowExecutionStatus("Local Dataset");

        jobExecution.getExecutionContext().putString("resourceName", "upload://resource.zip");  
        assertEquals("decide should return the expected status", expectedStatus, decider.decide(jobExecution, stepExecution));
    }

    @Test
    public void testDecideLocalStatic() {
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();
        JobExecution jobExecution = MetaDataInstanceFactory.createJobExecution();
        FlowExecutionStatus expectedStatus = new FlowExecutionStatus("Local Dataset");

        jobExecution.getExecutionContext().putString("resourceName", "static://resource.zip");  
        assertEquals("decide should return the expected status", expectedStatus, decider.decide(jobExecution, stepExecution));
    }
}
