package org.cateproject.repository.jpa;
import org.cateproject.domain.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NodeRepository extends JpaRepository<Node, Long>, JpaSpecificationExecutor<Node> {
	@Query("SELECT o FROM Node o where o.identifier = ?1")
	Node findByIdentifier(String identifier);
	
	@Query("SELECT CASE WHEN COUNT(o) > 0 THEN 'true' else 'false' END FROM Node o where o.identifier = ?1")
	Boolean existsByIdentifier(String identifier);
}
