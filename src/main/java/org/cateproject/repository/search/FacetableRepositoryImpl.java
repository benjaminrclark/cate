package org.cateproject.repository.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.SolrCallback;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.query.FacetQuery;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.repository.support.SimpleSolrRepository;

public class FacetableRepositoryImpl<T extends Object> extends SimpleSolrRepository<T, Long> implements FacetableRepository<T> {

	@Override
	public FacetPage<T> search(FacetQuery facetQuery) {
		SolrOperations solrOperations = this.getSolrOperations();
		Class<T> entityClass = getEntityClass();
		return solrOperations.queryForFacetPage(facetQuery, entityClass);
	}
	
	private QueryResponse execute(final SolrQuery solrQuery, Pageable pageable) {
		SolrOperations solrOperations = this.getSolrOperations();		
	        return solrOperations.execute(new SolrCallback<QueryResponse> () {
			@Override
			public QueryResponse doInSolr(SolrServer solrServer) throws SolrServerException, IOException {
				return solrServer.query(solrQuery);
			}			
		});
	}

	@Override
	public List<Match> autocomplete(String term, String filterQuery) {
	    Pageable pageable = new PageRequest(0,10);
	    final SolrQuery solrQuery = new SolrQuery();

	    if (term != null && !term.trim().equals("")) {            
                solrQuery.setQuery(term);
            } else {
                return new ArrayList<Match>();
            }

	    solrQuery.set("defType","edismax");
            solrQuery.set("qf", "autocomplete^3 autocompleteng");
            solrQuery.set("pf", "autocompletenge");
            solrQuery.set("fl","autocomplete,id,base.id_l");
            solrQuery.set("group","true");
            solrQuery.set("group.field", "autocomplete");
            solrQuery.set("fq",filterQuery);
		
	    QueryResponse queryResponse = execute(solrQuery, pageable);
		
	    List<Match> results = new ArrayList<Match>();
            Map<String,Match> matchMap = new HashMap<String,Match>();

            for(GroupCommand groupCommand : queryResponse.getGroupResponse().getValues()) {
                for (Group group : groupCommand.getValues()) {
		    for (SolrDocument solrDocument : group.getResult()) {
		        Match match = new Match();
			match.setText((String) solrDocument.get("autocomplete"));
			match.setValue(solrDocument.get("base.id_l"));
			matchMap.put((String) solrDocument.get("id"), match);
			results.add(match);
		    }
		}
            }
            return results;
	}

}
