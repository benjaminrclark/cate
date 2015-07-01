package org.cateproject.repository.jpa;
import org.cateproject.domain.Description;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DescriptionRepository extends JpaRepository<Description, Long>, JpaSpecificationExecutor<Description> {
	@Query("SELECT o FROM Description o where o.identifier = ?1")
	Description findByIdentifier(String identifier);
	
	@Query("SELECT CASE WHEN COUNT(o) > 0 THEN 'true' else 'false' END FROM Description o where o.identifier = ?1")
	Boolean existsByIdentifier(String identifier);
}
