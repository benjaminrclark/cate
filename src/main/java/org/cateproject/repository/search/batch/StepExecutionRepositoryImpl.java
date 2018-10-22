package org.cateproject.repository.search.batch;

import java.util.List;

import org.cateproject.repository.search.FacetableRepositoryImpl;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleQuery;

public class StepExecutionRepositoryImpl extends FacetableRepositoryImpl<StepExecution> implements StepExecutionRepository {
	
    public StepExecutionRepositoryImpl() {
        this.setEntityClass(StepExecution.class);
        this.setIdFieldName("id");
    }

    public Page<StepExecution> findByJobExecutionOrderByStartTimeAsc(JobExecution jobExecution, Pageable pageable) {
        SimpleQuery query = new SimpleQuery();
        query.setPageRequest(pageable);
        query.addSort(new Sort(Sort.Direction.ASC,"stepexecution.createtime_dt"));
        query.addCriteria(Criteria.where("stepexecution.jobexecution_l").expression(jobExecution.getId().toString()));
        return super.getSolrOperations().queryForPage(query,StepExecution.class);
    }
}
