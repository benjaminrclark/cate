package org.cateproject.web.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.cateproject.repository.jdbc.batch.JobInstanceRepository;
import org.cateproject.repository.jdbc.batch.JobRepository;
import org.cateproject.repository.search.batch.JobExecutionRepository;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

public class JobControllerTest {

    private JobController jobController;

    private JobRepository jobRepository;

    private JobInstanceRepository jobInstanceRepository;

    private JobExecutionRepository jobExecutionRepository;

    @Before
    public void setUp() {
        jobController = new JobController();
        jobRepository = EasyMock.createMock(JobRepository.class);
        jobInstanceRepository = EasyMock.createMock(JobInstanceRepository.class);
        jobExecutionRepository = EasyMock.createMock(JobExecutionRepository.class);
        jobController.setJobRepository(jobRepository);
        jobController.setJobInstanceRepository(jobInstanceRepository);
        jobController.setJobExecutionRepository(jobExecutionRepository);
    }

    @Test
    public void testShow() {
        Job job = new SimpleJob("JOB_NAME");    
        Model uiModel = new ExtendedModelMap();
        Pageable pageable = new PageRequest(1,1);
        JobInstance jobInstance = MetaDataInstanceFactory.createJobInstance("JOB_NAME", 1L);
        List<JobInstance> jobInstances = new ArrayList<JobInstance>();
        jobInstances.add(jobInstance);
        Page<JobInstance> results = new PageImpl<JobInstance>(jobInstances, new PageRequest(1,1), 1L);
        JobExecution jobExecution = MetaDataInstanceFactory.createJobExecution("JOB_NAME",1L,1L);


        EasyMock.expect(jobRepository.findOne(EasyMock.eq("JOB_NAME"))).andReturn(job);
        EasyMock.expect(jobInstanceRepository.findAll(EasyMock.eq(pageable), EasyMock.eq("JOB_NAME"))).andReturn(results);
        EasyMock.expect(jobExecutionRepository.findOneByJobInstanceOrderByStartTimeDesc(EasyMock.eq(jobInstance))).andReturn(jobExecution);
        EasyMock.expect(jobExecutionRepository.countByJobInstance(EasyMock.eq(jobInstance))).andReturn(1L);

        EasyMock.replay(jobRepository, jobInstanceRepository, jobExecutionRepository);
        assertEquals("show should return the expected view name", "batch/job/show",jobController.show("JOB_NAME", uiModel, pageable));
        assertEquals("model should contain the job", uiModel.asMap().get("result"),job);
        assertTrue("model should contain a page of job instances", uiModel.containsAttribute("jobInstances"));
        EasyMock.verify(jobRepository, jobInstanceRepository, jobExecutionRepository);
    }

    @Test
    public void testList() {
        Page<Job> results = new PageImpl<Job>(new ArrayList<Job>());
        Model uiModel = new ExtendedModelMap();
        Pageable pageable = new PageRequest(1,1);

        EasyMock.expect(jobRepository.findAll(pageable)).andReturn(results);

        EasyMock.replay(jobRepository, jobInstanceRepository, jobExecutionRepository);
        assertEquals("list should return the expected view name", "batch/job/list",jobController.list(pageable, uiModel));
        assertEquals("model should contain the page of jobs", uiModel.asMap().get("results"),results);
        EasyMock.verify(jobRepository, jobInstanceRepository, jobExecutionRepository);
    }
}
