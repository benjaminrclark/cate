package org.cateproject.repository.search.batch;

import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleQuery;

public class JobExecutionRepositoryImplTest {

    private JobExecutionRepositoryImpl jobExecutionRepository;

    private SolrOperations solrOperations;

    @Before
    public void setUp() {
        jobExecutionRepository = new JobExecutionRepositoryImpl();
        solrOperations = EasyMock.createMock(SolrOperations.class);
        jobExecutionRepository.setSolrOperations(solrOperations);
    }

    @Test
    public void testFindOne() {
        JobExecution expectedJobExecution = MetaDataInstanceFactory.createJobExecution(1L);
        EasyMock.expect(solrOperations.queryForObject(EasyMock.isA(SimpleQuery.class), EasyMock.eq(JobExecution.class))).andReturn(expectedJobExecution);

        EasyMock.replay(solrOperations);
        assertEquals("findOne should return the expected jobExecution",expectedJobExecution, jobExecutionRepository.findOne(1L));
        EasyMock.verify(solrOperations);
    }

    @Test
    public void testFindOneByJobInstanceOrderByStartTimeDesc() {
        JobExecution expectedJobExecution = MetaDataInstanceFactory.createJobExecution(1L);
        JobInstance jobInstance = MetaDataInstanceFactory.createJobInstance("JOB_NAME", 1L);

        EasyMock.expect(solrOperations.queryForObject(EasyMock.isA(SimpleQuery.class), EasyMock.eq(JobExecution.class))).andReturn(expectedJobExecution);

        EasyMock.replay(solrOperations);
        assertEquals("findOneByJobInstanceOrderByStartTimeDesc should return the expected jobExecution",expectedJobExecution, jobExecutionRepository.findOneByJobInstanceOrderByStartTimeDesc(jobInstance));
        EasyMock.verify(solrOperations);
    }

    @Test
    public void testCountByJobInstance() {
        JobInstance jobInstance = MetaDataInstanceFactory.createJobInstance("JOB_NAME", 1L);

        EasyMock.expect(solrOperations.count(EasyMock.isA(SimpleFilterQuery.class))).andReturn(1L);

        EasyMock.replay(solrOperations);
        assertEquals("countByJobInstance should return the expected value",new Long(1L), jobExecutionRepository.countByJobInstance(jobInstance));
        EasyMock.verify(solrOperations);
    }
}
