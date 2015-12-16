package org.cateproject.repository.search.batch;

import org.cateproject.repository.search.FacetableRepositoryImpl;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleQuery;

public class JobExecutionRepositoryImpl extends FacetableRepositoryImpl<JobExecution> implements JobExecutionRepository {
	
    public JobExecutionRepositoryImpl() {
        this.setEntityClass(JobExecution.class);
        this.setIdFieldName("id");
    }

    public JobExecution findOne(Long id) {
        SimpleQuery query = new SimpleQuery();
        query.addCriteria(Criteria.where("base.class_s").expression("org.springframework.batch.core.JobExecution"));
        return super.getSolrOperations().queryForObject(query,JobExecution.class);
    }
	
    public JobExecution findOneByJobInstanceOrderByStartTimeDesc(JobInstance jobInstance) {
        SimpleQuery query = new SimpleQuery();
        query.setPageRequest(new PageRequest(0,1));
        query.addSort(new Sort(Sort.Direction.DESC,"jobexecution.createtime_dt"));
        query.addCriteria(Criteria.where("jobexecution.jobinstance_l").expression(jobInstance.getId().toString()));
        return super.getSolrOperations().queryForObject(query,JobExecution.class);
    }

    public Long countByJobInstance(JobInstance jobInstance) {
        SimpleFilterQuery filterQuery = new SimpleFilterQuery();
        filterQuery.addCriteria(Criteria.where("jobexecution.jobinstance_l").expression(jobInstance.getId().toString()));
        return super.getSolrOperations().count(filterQuery);
    }
}
