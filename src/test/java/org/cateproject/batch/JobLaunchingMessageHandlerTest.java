package org.cateproject.batch;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.batch.test.MetaDataInstanceFactory;

public class JobLaunchingMessageHandlerTest {

    private JobLaunchingMessageHandler jobLaunchingMessageHandler;

    private JobLauncher jobLauncher;

    @Before
    public void setUp() {
        jobLauncher = EasyMock.createMock(JobLauncher.class);
        jobLaunchingMessageHandler = new JobLaunchingMessageHandler(jobLauncher);
    }

    @Test
    public void testLaunch() throws Exception {
        Job job = new SimpleJob("JOB_NAME");
        JobParameters jobParameters = new JobParameters();
        JobLaunchRequest jobLaunchRequest = new JobLaunchRequest(job, jobParameters);

        EasyMock.expect(jobLauncher.run(EasyMock.eq(job), EasyMock.eq(jobParameters))).andReturn(MetaDataInstanceFactory.createJobExecution(1L));
        EasyMock.replay(jobLauncher);

        jobLaunchingMessageHandler.launch(jobLaunchRequest);
        EasyMock.verify(jobLauncher);
    }

    @Test(expected=RuntimeException.class)
    public void testLaunchWithJobExecutionException() throws Exception {
        Job job = new SimpleJob("JOB_NAME");
        JobParameters jobParameters = new JobParameters();
        JobLaunchRequest jobLaunchRequest = new JobLaunchRequest(job, jobParameters);

        EasyMock.expect(jobLauncher.run(EasyMock.eq(job), EasyMock.eq(jobParameters))).andThrow(new JobExecutionAlreadyRunningException("EXPECTED"));
        EasyMock.replay(jobLauncher);

        jobLaunchingMessageHandler.launch(jobLaunchRequest);
    }
}
