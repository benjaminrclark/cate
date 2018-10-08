package org.cateproject.repository.jpa.batch;

import org.cateproject.domain.batch.JobLaunchRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface JobLaunchRequestRepository extends JpaRepository<JobLaunchRequest, Long>, JpaSpecificationExecutor<JobLaunchRequest> {

	@Query("SELECT o FROM JobLaunchRequest o where o.identifier = ?1")
	JobLaunchRequest findByIdentifier(String identifier);

}
