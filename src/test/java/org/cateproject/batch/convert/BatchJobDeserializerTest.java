package org.cateproject.batch.convert;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.integration.launch.JobLaunchRequest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class BatchJobDeserializerTest {

    private ObjectMapper objectMapper;

    private StringReader stringReader;

    private JobLocator jobLocator;

    @Before
    public void setUp() throws IOException {
        BatchJobDeserializer batchJobDeserializer = new BatchJobDeserializer();
        jobLocator = EasyMock.createMock(JobLocator.class);
        batchJobDeserializer.setJobLocator(jobLocator);
        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("SpringBatchJobModule", new Version(0, 1, 0, "alpha"));
        module.addDeserializer(JobLaunchRequest.class, batchJobDeserializer);
        objectMapper.registerModule(module);
        stringReader = new StringReader("{\"job\":\"TEST_JOB\",\"parameters\":{\"PARAMETER_2\":{\"value\":\"PARAMETER_VALUE_2\",\"identifying\":true},\"PARAMETER_1\":{\"value\":\"PARAMETER_VALUE_1\",\"identifying\":true}}}");
    }

    @Test
    public void testDeserialize() throws IOException, NoSuchJobException {
        Job expectedJob = new SimpleJob("TEST_JOB");
        EasyMock.expect(jobLocator.getJob(EasyMock.eq("TEST_JOB"))).andReturn(expectedJob);
        Map<String, JobParameter> expectedJobParametersMap = new HashMap<String, JobParameter>();
        expectedJobParametersMap.put("PARAMETER_1", new JobParameter("PARAMETER_VALUE_1"));
        expectedJobParametersMap.put("PARAMETER_2", new JobParameter("PARAMETER_VALUE_2"));
        JobParameters expectedJobParameters = new JobParameters(expectedJobParametersMap);

        EasyMock.replay(jobLocator);

        JobLaunchRequest jobLaunchRequest = objectMapper.readValue(stringReader, JobLaunchRequest.class);
        assertEquals("deserialize should create the correct job",jobLaunchRequest.getJob(), expectedJob);
        assertEquals("deserialize should create the correct jobParameters",jobLaunchRequest.getJobParameters(),expectedJobParameters);
        EasyMock.verify(jobLocator);
    }

    @Test(expected = JsonParseException.class)
    public void testDeserializeThrowsNoSuchJobException() throws IOException, NoSuchJobException {
        EasyMock.expect(jobLocator.getJob(EasyMock.eq("TEST_JOB"))).andThrow(new NoSuchJobException("TEST_JOB"));
        EasyMock.replay(jobLocator);

        objectMapper.readValue(stringReader, JobLaunchRequest.class);
    }
}
