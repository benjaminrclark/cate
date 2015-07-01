package org.cateproject.repository.jpa;
import org.cateproject.domain.TypeAndSpecimen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeAndSpecimenRepository extends JpaRepository<TypeAndSpecimen, Long>, JpaSpecificationExecutor<TypeAndSpecimen> {
	@Query("SELECT o FROM TypeAndSpecimen o where o.occurrenceId = ?1")
	TypeAndSpecimen findByOccurrenceId(String occurrenceId);
	
	@Query("SELECT CASE WHEN COUNT(o) > 0 THEN 'true' else 'false' END FROM TypeAndSpecimen o where o.occurrenceId = ?1")
	Boolean existsByOccurrenceId(String occurrenceId);
}
