package org.cateproject.repository.jpa;
import org.cateproject.domain.Distribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DistributionRepository extends JpaRepository<Distribution, Long>, JpaSpecificationExecutor<Distribution> {
	@Query("SELECT o FROM Distribution o where o.identifier = ?1")
	Distribution findByIdentifier(String identifier);
	
	@Query("SELECT CASE WHEN COUNT(o) > 0 THEN 'true' else 'false' END FROM Distribution o where o.identifier = ?1")
	Boolean existsByIdentifier(String identifier);
}
