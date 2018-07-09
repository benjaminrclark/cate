package org.cateproject.repository.jpa.batch;

import org.cateproject.domain.batch.BatchDataset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchDatasetRepository extends JpaRepository<BatchDataset, Long>, JpaSpecificationExecutor<BatchDataset>{

	@Query("SELECT o FROM BatchDataset o where o.identifier = ?1")
	BatchDataset findByIdentifier(String identifier);
	
	@Query("SELECT CASE WHEN COUNT(o) > 0 THEN 'true' else 'false' END FROM BatchDataset o where o.identifier = ?1")
	Boolean existsByIdentifier(String identifier);
}
