package org.cateproject.repository.jpa;
import org.cateproject.domain.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TermRepository extends JpaRepository<Term, Long>, JpaSpecificationExecutor<Term> {
	@Query("SELECT o FROM Term o where o.identifier = ?1")
	Term findByIdentifier(String identifier);
	
	@Query("SELECT CASE WHEN COUNT(o) > 0 THEN 'true' else 'false' END FROM Term o where o.identifier = ?1")
	Boolean existsByIdentifier(String identifier);

	@Query("SELECT o FROM Term o where o.title = ?1")
	Term findByTitle(String title);
}
