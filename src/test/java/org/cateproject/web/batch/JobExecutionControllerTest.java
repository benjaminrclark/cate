package org.cateproject.web.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.cateproject.repository.search.batch.JobExecutionRepository;
import org.cateproject.repository.search.batch.StepExecutionRepository;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.FacetQuery;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.SimpleField;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.SolrResultPage;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

public class JobExecutionControllerTest {
    
    private JobExecutionController jobExecutionController;

    private JobExecutionRepository jobExecutionRepository;

    private StepExecutionRepository stepExecutionRepository;

    @Before
    public void setUp() {
        jobExecutionController = new JobExecutionController();
        jobExecutionRepository = EasyMock.createMock(JobExecutionRepository.class);        
        stepExecutionRepository = EasyMock.createMock(StepExecutionRepository.class);        
        jobExecutionController.setJobExecutionRepository(jobExecutionRepository);
        jobExecutionController.setStepExecutionRepository(stepExecutionRepository);
    } 

   @Test
   public void testList() {
       Pageable pageable = new PageRequest(0,1);
       Model uiModel = new ExtendedModelMap();
       List<FilterQuery> filterQueries = new ArrayList<FilterQuery>();
       filterQueries.add(new SimpleFilterQuery());
       List<JobExecution> jobExecutions = new ArrayList<JobExecution>();
       jobExecutions.add(MetaDataInstanceFactory.createJobExecution(1L));
       SolrResultPage<JobExecution> solrResultPage = new SolrResultPage<JobExecution>(jobExecutions, pageable, 1L, 1.0F);
       Page<FacetFieldEntry> facetPage = new PageImpl<FacetFieldEntry>(new ArrayList<FacetFieldEntry>());
       solrResultPage.addFacetResultPage(facetPage, new SimpleField("FIELD"));

       EasyMock.expect(jobExecutionRepository.search(EasyMock.isA(FacetQuery.class))).andReturn(solrResultPage);
       EasyMock.replay(jobExecutionRepository, stepExecutionRepository);

       assertEquals("list should return the expected value","batch/execution/list",jobExecutionController.list(pageable,uiModel, "QUERY", filterQueries));
       assertTrue("model should contain a result", uiModel.containsAttribute("results"));
       assertEquals("result should contain one jobExecutionInfo", new Long(1L), (Long)((Page<JobExecutionInfo>)uiModel.asMap().get("results")).getTotalElements());

       EasyMock.verify(jobExecutionRepository, stepExecutionRepository);
   }

   @Test
   public void testListWithNullQuery() {
       Pageable pageable = new PageRequest(0,1);
       Model uiModel = new ExtendedModelMap();
       List<JobExecution> jobExecutions = new ArrayList<JobExecution>();
       jobExecutions.add(MetaDataInstanceFactory.createJobExecution(1L));
       SolrResultPage<JobExecution> solrResultPage = new SolrResultPage<JobExecution>(jobExecutions, pageable, 1L, 1.0F);
       Page<FacetFieldEntry> facetPage = new PageImpl<FacetFieldEntry>(new ArrayList<FacetFieldEntry>());
       solrResultPage.addFacetResultPage(facetPage, new SimpleField("FIELD"));

       EasyMock.expect(jobExecutionRepository.search(EasyMock.isA(FacetQuery.class))).andReturn(solrResultPage);
       EasyMock.replay(jobExecutionRepository, stepExecutionRepository);

       assertEquals("list should return the expected value","batch/execution/list",jobExecutionController.list(pageable,uiModel, null, null));
       assertTrue("model should contain a result", uiModel.containsAttribute("results"));
       assertEquals("result should contain one jobExecutionInfo", new Long(1L), (Long)((Page<JobExecutionInfo>)uiModel.asMap().get("results")).getTotalElements());

       EasyMock.verify(jobExecutionRepository, stepExecutionRepository);
   }

   @Test
   public void testListWithEmptyQuery() {
       Pageable pageable = new PageRequest(0,1);
       Model uiModel = new ExtendedModelMap();
       List<JobExecution> jobExecutions = new ArrayList<JobExecution>();
       jobExecutions.add(MetaDataInstanceFactory.createJobExecution(1L));
       SolrResultPage<JobExecution> solrResultPage = new SolrResultPage<JobExecution>(jobExecutions, pageable, 1L, 1.0F);
       Page<FacetFieldEntry> facetPage = new PageImpl<FacetFieldEntry>(new ArrayList<FacetFieldEntry>());
       solrResultPage.addFacetResultPage(facetPage, new SimpleField("FIELD"));

       EasyMock.expect(jobExecutionRepository.search(EasyMock.isA(FacetQuery.class))).andReturn(solrResultPage);
       EasyMock.replay(jobExecutionRepository, stepExecutionRepository);

       assertEquals("list should return the expected value","batch/execution/list",jobExecutionController.list(pageable,uiModel, "", null));
       assertTrue("model should contain a result", uiModel.containsAttribute("results"));
       assertEquals("result should contain one jobExecutionInfo", new Long(1L), (Long)((Page<JobExecutionInfo>)uiModel.asMap().get("results")).getTotalElements());

       EasyMock.verify(jobExecutionRepository, stepExecutionRepository);
   }

   @Test
   public void testShow() {
       Model uiModel = new ExtendedModelMap();
       JobExecution jobExecution = MetaDataInstanceFactory.createJobExecution(1L);
       Pageable pageable = new PageRequest(0, 50);
       List<StepExecution> stepExecutionList = new ArrayList<StepExecution>();
       Page<StepExecution> stepExecutions = new PageImpl<StepExecution>(stepExecutionList, pageable, 0L);

       EasyMock.expect(jobExecutionRepository.findOne(EasyMock.eq(1L))).andReturn(jobExecution);
        
       EasyMock.expect(stepExecutionRepository.findByJobExecutionOrderByIdAsc(EasyMock.eq(jobExecution), EasyMock.eq(pageable))).andReturn(stepExecutions);
       EasyMock.replay(jobExecutionRepository, stepExecutionRepository);

       assertEquals("show should return the expected value","batch/execution/show",jobExecutionController.show(1L, uiModel, Locale.getDefault()));
       assertTrue("model should contain a result", uiModel.containsAttribute("result"));
       assertEquals("result should contain the expected JobExecutionInfo", jobExecution, ((JobExecutionInfo)uiModel.asMap().get("result")).getJobExecution());

       EasyMock.verify(jobExecutionRepository, stepExecutionRepository);
   }
}
