package org.cateproject.web.batch;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.Duration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.MetaDataInstanceFactory;

public class JobExecutionInfoTest {

    private JobExecutionInfo jobExecutionInfo;

    private JobExecution jobExecution;

    @Before
    public void setUp() {
        Map<String,JobParameter> jobParametersMap = new HashMap<String, JobParameter>();
        jobParametersMap.put("JOB_PARAMETER_1", new JobParameter("JOB_PARAMETER_VALUE"));
        jobExecution = MetaDataInstanceFactory.createJobExecution("JOB_NAME", 1L, 1L, new JobParameters(jobParametersMap));
        List<StepExecution> stepExecutions = new ArrayList<StepExecution>();
        stepExecutions.add(MetaDataInstanceFactory.createStepExecution(jobExecution, "STEP_1", 1L));
        stepExecutions.add(MetaDataInstanceFactory.createStepExecution(jobExecution, "STEP_2", 2L));
        jobExecution.addStepExecutions(stepExecutions);
        jobExecution.setCreateTime(new Date(1000));
        jobExecution.setStartTime(new Date(2000));
        jobExecution.setEndTime(new Date(3000));
        jobExecution.setStatus(BatchStatus.COMPLETED);
        jobExecution.setExitStatus(new ExitStatus("EXIT_CODE","EXIT_DESCRIPTION"));
        jobExecutionInfo = new JobExecutionInfo(jobExecution);
        DateTimeUtils.setCurrentMillisFixed(5000);
    }

    @After
    public void tearDown() {
        DateTimeUtils.setCurrentMillisSystem();
    }

    @Test
    public void testGetJobExecution() {
        assertEquals("getJobExecution should return the expected value", jobExecution, jobExecutionInfo.getJobExecution());
    }

    @Test
    public void testGetDuration() {
        assertEquals("getDuration should return the expected value",  new Duration(1000), jobExecutionInfo.getDuration());
    }

    @Test
    public void testGetDurationWithRunningExecution() {
        jobExecution.setEndTime(null);
        assertEquals("getDuration should return the expected value",  new Duration(3000), jobExecutionInfo.getDuration());
    }

    @Test
    public void testGetStartDateTime() {
        assertEquals("getStartDateTime should return the expected value", new DateTime(2000), jobExecutionInfo.getStartDateTime());
    }

    @Test
    public void testGetStepExecutionCount() {
        assertEquals("getStepExecutionCount should return the expected value", new Integer(2), jobExecutionInfo.getStepExecutionCount());
    }

    @Test
    public void testGetJobName() {
        assertEquals("getJobName should return the expected value", "JOB_NAME", jobExecutionInfo.getJobName());
    }

    @Test
    public void testGetJobInstance() {
        assertEquals("getJobInstance should return the expected value", new Long(1L), jobExecutionInfo.getJobInstance());
    }

    @Test
    public void testGetExitCode() {
        assertEquals("getExitCode should return the expected value", "EXIT_CODE", jobExecutionInfo.getExitCode());
    }

    @Test
    public void testGetExitDescription() {
        assertEquals("getExitDescription should return the expected value", "EXIT_DESCRIPTION", jobExecutionInfo.getExitDescription());
    }

    @Test
    public void testGetId() {
        assertEquals("getId should return the expected value", new Long(1L), jobExecutionInfo.getId());
    }

    @Test
    public void testGetJobParameters() {
        Map<String, JobParameter> jobParametersMap = new HashMap<String, JobParameter>();
        jobParametersMap.put("JOB_PARAMETER_1", new JobParameter("JOB_PARAMETER_VALUE"));
        assertEquals("getJobParameters should return the expected value", new JobParameters(jobParametersMap), jobExecutionInfo.getJobParameters());
    }

    @Test
    public void testGetStatus() {
        assertEquals("getStatus should return the expected value", BatchStatus.COMPLETED, jobExecutionInfo.getStatus());
    }
}
