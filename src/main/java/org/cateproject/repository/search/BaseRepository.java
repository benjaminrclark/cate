package org.cateproject.repository.search;

import java.util.Map;

import org.cateproject.domain.Base;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.SolrCrudRepository;

public interface BaseRepository extends SolrCrudRepository<Base, Long>, FacetableRepository<Base> {
	
}
