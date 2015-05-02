package org.cateproject.repository.jpa;
import org.cateproject.domain.Reference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferenceRepository extends JpaRepository<Reference, Long>, JpaSpecificationExecutor<Reference> {
	@Query("SELECT o FROM Reference o where o.identifier = ?1")
	Reference findByIdentifier(String identifier);
	
	@Query("SELECT CASE WHEN COUNT(o) > 0 THEN 'true' else 'false' END FROM Reference o where o.identifier = ?1")
	Boolean existsByIdentifier(String identifier);
}
