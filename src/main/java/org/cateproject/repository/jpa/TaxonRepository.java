package org.cateproject.repository.jpa;
import org.cateproject.domain.Taxon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxonRepository extends JpaRepository<Taxon, Long>, JpaSpecificationExecutor<Taxon> {
	@Query("SELECT o FROM Taxon o where o.taxonId = ?1")
	Taxon findByTaxonId(String taxonId);
	
	@Query("SELECT CASE WHEN COUNT(o) > 0 THEN 'true' else 'false' END FROM Taxon o where o.taxonId = ?1")
	Boolean existsByTaxonId(String taxonId);
}
