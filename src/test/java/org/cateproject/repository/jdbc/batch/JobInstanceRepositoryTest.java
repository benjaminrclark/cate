package org.cateproject.repository.jdbc.batch;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class JobInstanceRepositoryTest {

    private JobInstanceRepository jobInstanceRepository;

    private JobInstanceDao jobInstanceDao;

    @Before
    public void setUp() {
        jobInstanceRepository = new JobInstanceRepository();
        jobInstanceDao = EasyMock.createMock(JobInstanceDao.class);
        jobInstanceRepository.setJobInstanceDao(jobInstanceDao);
    }

    @Test
    public void findAll() throws Exception {
        Pageable pageable = new PageRequest(0,10);
        List<JobInstance> jobInstances = new ArrayList<JobInstance>();
        jobInstances.add(MetaDataInstanceFactory.createJobInstance("JOB_NAME",1L));

        EasyMock.expect(jobInstanceDao.getJobInstanceCount(EasyMock.eq("JOB_NAME"))).andReturn(1);
        EasyMock.expect(jobInstanceDao.getJobInstances(EasyMock.eq("JOB_NAME"), EasyMock.eq(0), EasyMock.eq(10))).andReturn(jobInstances);

        EasyMock.replay(jobInstanceDao);
        assertEquals("findAll should return the expected page", new PageImpl<JobInstance>(jobInstances,pageable,1), jobInstanceRepository.findAll(pageable, "JOB_NAME"));
        EasyMock.verify(jobInstanceDao);
    }

    @Test(expected = RuntimeException.class)
    public void findAllThrowsNoSuchJobException() throws Exception {
        Pageable pageable = new PageRequest(0,10);
        EasyMock.expect(jobInstanceDao.getJobInstanceCount(EasyMock.eq("JOB_NAME"))).andThrow(new NoSuchJobException("EXPECTED"));

        EasyMock.replay(jobInstanceDao);
        jobInstanceRepository.findAll(pageable, "JOB_NAME");
    }
}
