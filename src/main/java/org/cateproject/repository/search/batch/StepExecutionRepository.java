package org.cateproject.repository.search.batch;

import java.util.List;

import org.cateproject.repository.search.FacetableRepository;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.repository.SolrCrudRepository;

public interface StepExecutionRepository extends SolrCrudRepository<StepExecution, Long>, FacetableRepository<StepExecution> {

    Page<StepExecution> findByJobExecutionOrderByStartTimeAsc(JobExecution jobExecution, Pageable pageable);
}
