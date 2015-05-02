package org.cateproject.repository.jpa;
import org.cateproject.domain.Multimedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MultimediaRepository extends JpaRepository<Multimedia, Long>, JpaSpecificationExecutor<Multimedia> {
	@Query("SELECT o FROM Multimedia o where o.identifier = ?1")
	Multimedia findByIdentifier(String identifier);
	
	@Query("SELECT CASE WHEN COUNT(o) > 0 THEN 'true' else 'false' END FROM Multimedia o where o.identifier = ?1")
	Boolean existsByIdentifier(String identifier);
}
