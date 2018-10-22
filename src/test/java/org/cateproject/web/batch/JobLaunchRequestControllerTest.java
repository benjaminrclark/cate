package org.cateproject.web.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.util.HashMap;

import org.cateproject.domain.batch.JobLaunchRequest;
import org.cateproject.repository.jpa.batch.JobLaunchRequestRepository;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.job.SimpleJob;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

public class JobLaunchRequestControllerTest {
    
    private JobLaunchRequestController jobLaunchRequestController;

    private JobLaunchRequestRepository jobLaunchRequestRepository;

    @Before
    public void setUp() {
        jobLaunchRequestController = new JobLaunchRequestController();
        jobLaunchRequestRepository = EasyMock.createMock(JobLaunchRequestRepository.class);        
        jobLaunchRequestController.setJobLaunchRequestRepository(jobLaunchRequestRepository);
    } 

   @Test
   public void testList() {
       Model uiModel = new ExtendedModelMap();
       JobLaunchRequest jobLaunchRequest = new JobLaunchRequest(new SimpleJob("foo"), new JobParameters(new HashMap<String, JobParameter>()));

       EasyMock.expect(jobLaunchRequestRepository.findOne(EasyMock.eq(new Long(1L)))).andReturn(jobLaunchRequest);
       EasyMock.replay(jobLaunchRequestRepository);

       assertEquals("show should return the expected value","batch/launch/show",jobLaunchRequestController.show(1L,uiModel));
       assertEquals("uiModel should contain the result", uiModel.asMap().get("result"), jobLaunchRequest);

       EasyMock.verify(jobLaunchRequestRepository);
   }

   @Test
   public void testListExecutingJob() {
       Model uiModel = new ExtendedModelMap();
       JobLaunchRequest jobLaunchRequest = new JobLaunchRequest(new SimpleJob("foo"), new JobParameters(new HashMap<String, JobParameter>()));
       jobLaunchRequest.setJobExecutionId(1L);

       EasyMock.expect(jobLaunchRequestRepository.findOne(EasyMock.eq(new Long(1L)))).andReturn(jobLaunchRequest);
       EasyMock.replay(jobLaunchRequestRepository);

       assertEquals("show should return the expected value","redirect:/batch/execution/1",jobLaunchRequestController.show(1L,uiModel));
       assertNull("uiModel should not contain the result", uiModel.asMap().get("result"));

       EasyMock.verify(jobLaunchRequestRepository);
   }

   @Test
   public void testGetJson() {
       JobLaunchRequest jobLaunchRequest = new JobLaunchRequest(new SimpleJob("foo"), new JobParameters(new HashMap<String, JobParameter>()));

       EasyMock.expect(jobLaunchRequestRepository.findOne(EasyMock.eq(new Long(1L)))).andReturn(jobLaunchRequest);
       EasyMock.replay(jobLaunchRequestRepository);

       assertEquals("getJson should return the expected value", jobLaunchRequest,jobLaunchRequestController.getJson(1L));

       EasyMock.verify(jobLaunchRequestRepository);
   }
}
