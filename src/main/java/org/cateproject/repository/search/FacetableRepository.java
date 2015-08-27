package org.cateproject.repository.search;

import java.util.List;

import org.springframework.data.solr.core.query.FacetQuery;
import org.springframework.data.solr.core.query.result.FacetPage;

public interface FacetableRepository<T extends Object> {
	
	public List<Match> autocomplete(String term, String filterQuery);
	
	public FacetPage<T> search(FacetQuery facetQuery);

}
