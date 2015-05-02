package org.cateproject.repository.jpa;
import org.cateproject.domain.MeasurementOrFact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasurementOrFactRepository extends JpaRepository<MeasurementOrFact, Long>, JpaSpecificationExecutor<MeasurementOrFact> {
	@Query("SELECT o FROM MeasurementOrFact o where o.identifier = ?1")
	MeasurementOrFact findByIdentifier(String identifier);
	
	@Query("SELECT CASE WHEN COUNT(o) > 0 THEN 'true' else 'false' END FROM MeasurementOrFact o where o.identifier = ?1")
	Boolean existsByIdentifier(String identifier);
}
