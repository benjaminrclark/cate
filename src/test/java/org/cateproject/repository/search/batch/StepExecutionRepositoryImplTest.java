package org.cateproject.repository.search.batch;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.SolrResultPage;

public class StepExecutionRepositoryImplTest {

    private StepExecutionRepositoryImpl stepExecutionRepository;

    private SolrOperations solrOperations;

    @Before
    public void setUp() {
        stepExecutionRepository = new StepExecutionRepositoryImpl();
        solrOperations = EasyMock.createMock(SolrOperations.class);
        stepExecutionRepository.setSolrOperations(solrOperations);
    }

    @Test
    public void testFindByJobExecutionOrderByStartTimeAsc() {
        PageRequest pageable = new PageRequest(0, 10);
        JobExecution jobExecution = MetaDataInstanceFactory.createJobExecution(1L);
        SolrResultPage<StepExecution> result = new SolrResultPage<StepExecution>(new ArrayList<StepExecution>(), pageable, 0L, 1.0F);

        EasyMock.expect(solrOperations.queryForPage(EasyMock.isA(SimpleQuery.class), EasyMock.eq(StepExecution.class))).andReturn(result);

        EasyMock.replay(solrOperations);
        assertEquals("findByJobExecutionOrderByStartTimeAsc should return the expected result",result, stepExecutionRepository.findByJobExecutionOrderByStartTimeAsc(jobExecution, pageable));
        EasyMock.verify(solrOperations);
    }
}
