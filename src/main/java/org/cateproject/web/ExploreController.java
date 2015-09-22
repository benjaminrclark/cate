package org.cateproject.web;

import java.util.ArrayList;
import java.util.List;

import org.cateproject.domain.Base;
import org.cateproject.repository.search.BaseRepository;
import org.cateproject.web.format.annotation.FilterQueryFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FacetOptions;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.SimpleFacetQuery;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ExploreController {

    @Autowired
    private BaseRepository baseRepository;

    public void setBaseRepository(BaseRepository baseRepository) {
        this.baseRepository = baseRepository; 
    }

    public List<String> getFacets() {
    	List<String> facets = new ArrayList<String>();
    	facets.add("base.class_s");
    	facets.add("taxon.genus_s");
    	facets.add("taxon.distribution_TDWG_0_ss");
    	return facets;
    }

    @RequestMapping(value = "/explore", method = RequestMethod.GET, produces = "text/html")
    public String explore(Model uiModel, @RequestParam(value = "query", required = false) String query, Pageable pageable, @RequestParam(value = "filterQuery", required = false) @FilterQueryFormat List<FilterQuery> filterQueries) throws Exception {
			
        SimpleFacetQuery facetQuery = new SimpleFacetQuery();	
	FacetOptions facetOptions = new FacetOptions();
	for(String facet : getFacets()) {
            facetOptions.addFacetOnField(facet);
	}
	facetQuery.setFacetOptions(facetOptions);
	if(query != null && !query.isEmpty()) {
	    facetQuery.addCriteria(Criteria.where("base_solrsummary_t").expression(query));
	} else {
	    facetQuery.addCriteria(Criteria.where("base_solrsummary_t").contains(query));
	}
	facetQuery.setPageRequest(pageable);
	if(filterQueries != null) {
	    for(FilterQuery filterQuery : filterQueries) {
	        facetQuery.addFilterQuery(filterQuery);
	    }
	}
	SimpleFilterQuery filterQuery = new SimpleFilterQuery();
	filterQuery.addCriteria(Criteria.where("super.class_s").contains("org.cateproject.domain.Base"));
	facetQuery.addFilterQuery(filterQuery);
	Page<Base> result = baseRepository.search(facetQuery);
        
        uiModel.addAttribute("result", result);
        return "explore";
    }
}
