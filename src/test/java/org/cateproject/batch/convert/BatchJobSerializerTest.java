package org.cateproject.batch.convert;

import static org.junit.Assert.assertEquals;

import org.cateproject.domain.batch.JobLaunchRequest;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.job.SimpleJob;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class BatchJobSerializerTest {
    
    private JobLaunchRequest jobLaunchRequest;

    private JobLocator jobLocator; 

    private ObjectMapper objectMapper;

    private StringWriter stringWriter;

    @Before
    public void setUp() throws IOException {
        jobLocator = EasyMock.createMock(JobLocator.class);

        Map<String, JobParameter> jobParametersMap = new HashMap<String, JobParameter>();
        jobParametersMap.put("PARAMETER_1", new JobParameter("PARAMETER_VALUE_1"));
        jobParametersMap.put("PARAMETER_2", new JobParameter("PARAMETER_VALUE_2"));
        JobParameters jobParameters = new JobParameters(jobParametersMap);
        jobLaunchRequest = new JobLaunchRequest(new SimpleJob("TEST_JOB"), jobParameters);
        jobLaunchRequest.setJobLocator(jobLocator);
        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("SpringBatchJobModule", new Version(0, 1, 0, "alpha"));
        module.addSerializer(JobLaunchRequest.class, new BatchJobSerializer());
        objectMapper.registerModule(module);
        stringWriter = new StringWriter();
    }

    @Test
    public void testSerialize() throws IOException, NoSuchJobException {
       EasyMock.expect(jobLocator.getJob(EasyMock.eq("TEST_JOB"))).andReturn(new SimpleJob("TEST_JOB"));

       EasyMock.replay(jobLocator);
       objectMapper.writeValue(stringWriter, jobLaunchRequest);
       assertEquals("serialize should return the correct json","{\"job\":\"TEST_JOB\",\"parameters\":{\"PARAMETER_2\":{\"value\":\"PARAMETER_VALUE_2\",\"identifying\":true},\"PARAMETER_1\":{\"value\":\"PARAMETER_VALUE_1\",\"identifying\":true}},\"jobExecution\":null}", stringWriter.toString()); 
       EasyMock.verify(jobLocator);
    }
}
