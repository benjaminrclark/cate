package org.cateproject.domain.batch;

import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobLocator;

public class JobLaunchRequestTest {

    JobLaunchRequest jobLaunchRequest;

    Job job;

    JobParameters jobParameters;    

    JobLocator jobLocator;

    @Before
    public void setUp() {
        job = new SimpleJob("name");
        jobParameters = new JobParameters();
        jobLocator = EasyMock.createMock(JobLocator.class);

        jobLaunchRequest = new JobLaunchRequest(job, jobParameters);
        jobLaunchRequest.setJobLocator(jobLocator);
        jobLaunchRequest.setId(1L);
        jobLaunchRequest.setVersion(1);
        
    }

    @Test
    public void testGetJob() throws Exception {
        EasyMock.expect(jobLocator.getJob(EasyMock.eq("name"))).andReturn(job);
        EasyMock.replay(jobLocator);
        assertEquals("job should equal job", job, jobLaunchRequest.getJob());
        EasyMock.verify(jobLocator);
    }

    @Test
    public void testGetJobParameters() {
        assertEquals("jobParameters should equal jobParameters", jobParameters, jobLaunchRequest.getJobParameters());
    }

    @Test
    public void testGetId() {
        assertEquals("id should equal 1", new Long(1), jobLaunchRequest.getId());
    }

    @Test
    public void testGetVersion() {
        assertEquals("version should equal 1", new Integer(1), jobLaunchRequest.getVersion());
    }
}
