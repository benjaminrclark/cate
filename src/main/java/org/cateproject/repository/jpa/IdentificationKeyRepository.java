package org.cateproject.repository.jpa;
import org.cateproject.domain.IdentificationKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IdentificationKeyRepository extends JpaRepository<IdentificationKey, Long>, JpaSpecificationExecutor<IdentificationKey> {
	@Query("SELECT o FROM IdentificationKey o where o.identifier = ?1")
	IdentificationKey findByIdentifier(String identifier);
	
	@Query("SELECT CASE WHEN COUNT(o) > 0 THEN 'true' else 'false' END FROM IdentificationKey o where o.identifier = ?1")
	Boolean existsByIdentifier(String identifier);
	
	@Query("SELECT o FROM IdentificationKey o join o.dataset d where d.id = ?1")
	IdentificationKey findByDatasetId(Long id);
}
