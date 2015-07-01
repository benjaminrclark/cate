package org.cateproject.repository.jpa;
import org.cateproject.domain.Dataset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DatasetRepository extends JpaRepository<Dataset, Long>, JpaSpecificationExecutor<Dataset> {
	@Query("SELECT o FROM Dataset o where o.identifier = ?1")
	Dataset findByIdentifier(String identifier);
	
	@Query("SELECT CASE WHEN COUNT(o) > 0 THEN 'true' else 'false' END FROM Dataset o where o.identifier = ?1")
	Boolean existsByIdentifier(String identifier);
}
