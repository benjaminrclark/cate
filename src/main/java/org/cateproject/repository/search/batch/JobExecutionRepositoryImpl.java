package org.cateproject.repository.search.batch;

import org.cateproject.repository.search.FacetableRepositoryImpl;
import org.springframework.batch.core.JobExecution;

public class JobExecutionRepositoryImpl extends FacetableRepositoryImpl<JobExecution> implements JobExecutionRepository {
	
    public JobExecutionRepositoryImpl() {
        this.setEntityClass(JobExecution.class);
        this.setIdFieldName("id");
    }
	
}
