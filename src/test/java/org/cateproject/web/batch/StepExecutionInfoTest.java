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
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.MetaDataInstanceFactory;

public class StepExecutionInfoTest {

    private StepExecutionInfo stepExecutionInfo;

    private StepExecution stepExecution;

    @Before
    public void setUp() {
        stepExecution = MetaDataInstanceFactory.createStepExecution("STEP_NAME", 1L);
        stepExecution.setCommitCount(1);
        stepExecution.setReadCount(1);
        stepExecution.setWriteCount(1);
        stepExecution.setRollbackCount(1);
        stepExecution.setStartTime(new Date(2000));
        stepExecution.setEndTime(new Date(3000));
        stepExecution.setStatus(BatchStatus.COMPLETED);
        stepExecution.setExitStatus(new ExitStatus("EXIT_CODE","EXIT_DESCRIPTION"));
        stepExecutionInfo = new StepExecutionInfo(stepExecution);
        stepExecutionInfo.setMessage("MESSAGE");
        DateTimeUtils.setCurrentMillisFixed(5000);
    }

    @After
    public void tearDown() {
        DateTimeUtils.setCurrentMillisSystem();
    }

    @Test
    public void testGetCommitCount() {
        assertEquals("getCommitCount should return the expected value", new Integer(1), stepExecutionInfo.getCommitCount());
    }

    @Test
    public void testGetReadCount() {
        assertEquals("getReadCount should return the expected value", new Integer(1), stepExecutionInfo.getReadCount());
    }

    @Test
    public void testGetWriteCount() {
        assertEquals("getWriteCount should return the expected value", new Integer(1), stepExecutionInfo.getWriteCount());
    }

    @Test
    public void testGetRollbackCount() {
        assertEquals("getRollbackCount should return the expected value", new Integer(1), stepExecutionInfo.getRollbackCount());
    }

    @Test
    public void testGetDuration() {
        assertEquals("getDuration should return the expected value",  new Duration(1000), stepExecutionInfo.getDuration());
    }

    @Test
    public void testGetDurationWithRunningExecution() {
        stepExecution.setEndTime(null);
        assertEquals("getDuration should return the expected value",  new Duration(3000), stepExecutionInfo.getDuration());
    }

    @Test
    public void testGetStartDateTime() {
        assertEquals("getStartDateTime should return the expected value", new DateTime(2000), stepExecutionInfo.getStartDateTime());
    }

    @Test
    public void testGetStepName() {
        assertEquals("getStepName should return the expected value", "STEP_NAME", stepExecutionInfo.getStepName());
    }

    @Test
    public void testGetExitCode() {
        assertEquals("getExitCode should return the expected value", "EXIT_CODE", stepExecutionInfo.getExitCode());
    }

    @Test
    public void testGetExitDescription() {
        assertEquals("getExitDescription should return the expected value", "EXIT_DESCRIPTION", stepExecutionInfo.getExitDescription());
    }

    @Test
    public void testGetId() {
        assertEquals("getId should return the expected value", new Long(1L), stepExecutionInfo.getId());
    }

    @Test
    public void testGetStatus() {
        assertEquals("getStatus should return the expected value", BatchStatus.COMPLETED, stepExecutionInfo.getStatus());
    }

    @Test
    public void testGetStepExecution() {
        assertEquals("getStepExecution should return the expected value", stepExecution, stepExecutionInfo.getStepExecution());
    }

    @Test
    public void testGetMessage() {
        assertEquals("getMessage should return the expected value", "MESSAGE", stepExecutionInfo.getMessage());
    }
}
