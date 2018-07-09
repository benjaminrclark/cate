package org.cateproject.web.batch;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.test.MetaDataInstanceFactory;

public class JobInstanceInfoTest {

    private JobInstanceInfo jobInstanceInfo;
 
    private JobExecutionInfo jobExecutionInfo;

    @Before
    public void setUp() {
        jobInstanceInfo = new JobInstanceInfo();
        jobExecutionInfo = new JobExecutionInfo(MetaDataInstanceFactory.createJobExecution("JOB_NAME", 1L, 1L));
        jobInstanceInfo.setLastJobExecution(jobExecutionInfo);
        jobInstanceInfo.setJobExecutionCount(1L);
    }

    @Test
    public void testGetLastJobExecution() {
        assertEquals("getLastJobExecution should return the expected value", jobExecutionInfo, jobInstanceInfo.getLastJobExecution());
    }

    @Test
    public void testGetJobExecutionCount() {
        assertEquals("getJobExecutionCount should return the expected value", new Long(1L), jobInstanceInfo.getJobExecutionCount());
    }
}
