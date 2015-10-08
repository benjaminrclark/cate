package org.cateproject.repository.search.batch;

import org.cateproject.repository.search.FacetableRepository;
import org.springframework.batch.core.JobExecution;
import org.springframework.data.solr.repository.SolrCrudRepository;

public interface JobExecutionRepository extends SolrCrudRepository<JobExecution, Long>, FacetableRepository<JobExecution> {

}
