package org.cateproject.web.batch;

import java.util.ArrayList;
import java.util.List;

import org.cateproject.repository.search.batch.JobExecutionRepository;
import org.cateproject.web.format.annotation.FilterQueryFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FacetOptions;
import org.springframework.data.solr.core.query.Field;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.SimpleFacetQuery;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.result.SolrResultPage;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/batch/execution")
public class JobExecutionController {

    private static Logger logger = LoggerFactory.getLogger(JobExecutionController.class);
	
    @Autowired
    JobExecutionRepository jobExecutionRepository;

    public void setJobExecutionRepository(JobExecutionRepository jobExecutionRepository) {
        this.jobExecutionRepository = jobExecutionRepository;
    }

    public List<String> getFacets() {
    	List<String> facets = new ArrayList<String>();
        facets.add("jobexecution.jobname_t");
    	return facets;
    }

    @RequestMapping(produces = "text/html")
    public String list(Pageable pageable, Model uiModel, @RequestParam(value = "query", required = false) String query, @RequestParam(value = "filterQuery", required = false) @FilterQueryFormat List<FilterQuery> filterQueries) {        
        // TODO Should this be in the repository
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
	filterQuery.addCriteria(Criteria.where("base.class_s").contains("org.springframework.batch.core.JobExecution"));
	facetQuery.addFilterQuery(filterQuery);

        SolrResultPage<JobExecution> results = (SolrResultPage<JobExecution>)jobExecutionRepository.search(facetQuery);
        List<JobExecutionInfo> jobExecutionInfos = new ArrayList<JobExecutionInfo>();
        for(JobExecution jobExecution : results.getContent()) {
            jobExecutionInfos.add(new JobExecutionInfo(jobExecution));
        }
        SolrResultPage<JobExecutionInfo> solrResultPage = new SolrResultPage<JobExecutionInfo>(jobExecutionInfos,pageable, results.getTotalElements(), 1.0F);
        for(Field field : results.getFacetFields()) {
            solrResultPage.addFacetResultPage(results.getFacetResultPage(field),field);
        }
        uiModel.addAttribute("results", solrResultPage);
        return "batch/execution/list";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("result", new JobExecutionInfo(jobExecutionRepository.findOne(id)));
        return "batch/execution/show";
    }
}
