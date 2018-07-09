package org.cateproject.repository.jdbc.batch;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.ListableJobLocator;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class JobRepositoryTest {

    private JobRepository jobRepository;

    private ListableJobLocator listableJobLocator;

    @Before
    public void setUp() {
        jobRepository = new JobRepository();
        listableJobLocator = EasyMock.createMock(ListableJobLocator.class);
        jobRepository.setListableJobLocator(listableJobLocator);
    }

    @Test
    public void testFindAllWherePagableExceedsJobNames() {
	Pageable pageable = new PageRequest(1,10);
        List<String> jobNames = new ArrayList<String>();
        jobNames.add("JOB_1");
        jobNames.add("JOB_2");

        EasyMock.expect(listableJobLocator.getJobNames()).andReturn(jobNames);
        
        EasyMock.replay(listableJobLocator);
        assertEquals("findAll should return an empty page where the pageable exceeds the number of jobs",new PageImpl<Job>(new ArrayList<Job>(), pageable, jobNames.size()), jobRepository.findAll(pageable));
        EasyMock.verify(listableJobLocator);
    }

    @Test
    public void testFindAllWherePagableContainsJobNames() throws Exception {
	Pageable pageable = new PageRequest(0,10);
        Job job1 = new SimpleJob("JOB_1");
        Job job2 = new SimpleJob("JOB_2"); 
        List<Job> jobs = new ArrayList<Job>();
        jobs.add(job1);
        jobs.add(job2);
        List<String> jobNames = new ArrayList<String>();
        jobNames.add("JOB_1");
        jobNames.add("JOB_2");

        EasyMock.expect(listableJobLocator.getJobNames()).andReturn(jobNames);
        EasyMock.expect(listableJobLocator.getJob(EasyMock.eq("JOB_1"))).andReturn(job1);
        EasyMock.expect(listableJobLocator.getJob(EasyMock.eq("JOB_2"))).andReturn(job2);
        
        EasyMock.replay(listableJobLocator);
        assertEquals("findAll should return the expected page",new PageImpl<Job>(jobs, pageable, jobNames.size()), jobRepository.findAll(pageable));
        EasyMock.verify(listableJobLocator);
    }

    @Test
    public void testFindAllWherePagableLessThanJobNames() throws Exception {
	Pageable pageable = new PageRequest(0,2);
        Job job1 = new SimpleJob("JOB_1");
        Job job2 = new SimpleJob("JOB_2"); 
        List<Job> jobs = new ArrayList<Job>();
        jobs.add(job1);
        jobs.add(job2);
        List<String> jobNames = new ArrayList<String>();
        jobNames.add("JOB_1");
        jobNames.add("JOB_2");
        jobNames.add("JOB_3");
        jobNames.add("JOB_4");

        EasyMock.expect(listableJobLocator.getJobNames()).andReturn(jobNames);
        EasyMock.expect(listableJobLocator.getJob(EasyMock.eq("JOB_1"))).andReturn(job1);
        EasyMock.expect(listableJobLocator.getJob(EasyMock.eq("JOB_2"))).andReturn(job2);
        
        EasyMock.replay(listableJobLocator);
        assertEquals("findAll should return the expected page",new PageImpl<Job>(jobs, pageable, jobNames.size()), jobRepository.findAll(pageable));
        EasyMock.verify(listableJobLocator);
    }

    @Test(expected = RuntimeException.class)
    public void testFindAllThrowsNoSuchJobException() throws Exception {
	Pageable pageable = new PageRequest(0,2);
        List<String> jobNames = new ArrayList<String>();
        jobNames.add("JOB_1");

        EasyMock.expect(listableJobLocator.getJobNames()).andReturn(jobNames);
        EasyMock.expect(listableJobLocator.getJob(EasyMock.eq("JOB_1"))).andThrow(new NoSuchJobException("EXPECTED"));
        
        EasyMock.replay(listableJobLocator);
        jobRepository.findAll(pageable);
    }

    @Test
    public void testFindOne() throws Exception {
        Job job = new SimpleJob("JOB_NAME");

        EasyMock.expect(listableJobLocator.getJob(EasyMock.eq("JOB_NAME"))).andReturn(job);
        
        EasyMock.replay(listableJobLocator);
        assertEquals("findOne should return the expected job",job, jobRepository.findOne("JOB_NAME"));
        EasyMock.verify(listableJobLocator);
    }

    @Test(expected = RuntimeException.class)
    public void testFindOneThrowsNoSuchJobException() throws Exception {
        EasyMock.expect(listableJobLocator.getJob(EasyMock.eq("JOB_NAME"))).andThrow(new NoSuchJobException("EXPECTED"));
        
        EasyMock.replay(listableJobLocator);
        jobRepository.findOne("JOB_NAME");
    }
}
