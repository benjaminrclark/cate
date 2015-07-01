package org.cateproject.repository.jpa;
import org.cateproject.domain.VernacularName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VernacularNameRepository extends JpaRepository<VernacularName, Long>, JpaSpecificationExecutor<VernacularName> {
	@Query("SELECT o FROM VernacularName o where o.identifier = ?1")
	VernacularName findByIdentifier(String identifier);
	
	@Query("SELECT CASE WHEN COUNT(o) > 0 THEN 'true' else 'false' END FROM VernacularName o where o.identifier = ?1")
	Boolean existsByIdentifier(String identifier);
}
