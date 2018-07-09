package org.cateproject.repository.search;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.GroupResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.SolrCallback;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.query.FacetQuery;
import org.springframework.data.solr.core.query.SimpleFacetQuery;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.SolrResultPage;

public class FacetableRepositoryImplTest {
    
    private FacetableRepositoryImpl<String> facetableRepository;

    private SolrOperations solrOperations;

    @Before
    public void setUp() {
        facetableRepository = new FacetableRepositoryImpl<String>();
        
        solrOperations = EasyMock.createMock(SolrOperations.class);
        facetableRepository.setSolrOperations(solrOperations);
        facetableRepository.setEntityClass(String.class);
    }

    @Test
    public void testSearch() {
        FacetQuery facetQuery = new SimpleFacetQuery();
        FacetPage<String> facetPage = new SolrResultPage<String>(new ArrayList<String>());
        EasyMock.expect(solrOperations.queryForFacetPage(EasyMock.eq(facetQuery), EasyMock.eq(String.class))).andReturn(facetPage); 

        EasyMock.replay(solrOperations);
        assertEquals("search should return a facetPage",facetableRepository.search(facetQuery), facetPage);
        EasyMock.verify(solrOperations);

    }

    @Test
    public void testExecute() {
        Pageable pageable = new PageRequest(1,1); 
        SolrQuery solrQuery = new SolrQuery();
        QueryResponse queryResponse = new QueryResponse();
        EasyMock.expect(solrOperations.execute((SolrCallback<QueryResponse>)EasyMock.isA(SolrCallback.class))).andReturn(queryResponse);

        EasyMock.replay(solrOperations);
        assertEquals("execute should return a queryResponse",facetableRepository.execute(solrQuery, pageable), queryResponse);
        EasyMock.verify(solrOperations);
    }

    @Test
    public void testAutocomplete() {
        SolrDocument document1 = new SolrDocument();
        document1.put("base.id_l",1L);
        document1.put("autocomplete", "TERM ONE");
        SolrDocument document2 = new SolrDocument();
        document2.put("base.id_l",2L);
        document2.put("autocomplete", "TERM TWO");

        SolrDocumentList solrDocumentList1 = new SolrDocumentList();
        solrDocumentList1.add(document1);
        SolrDocumentList solrDocumentList2 = new SolrDocumentList();
        solrDocumentList2.add(document2);

        Group group1 = new Group("GROUP_ONE", solrDocumentList1);
        Group group2 = new Group("GROUP_TWO", solrDocumentList2);

        GroupCommand groupCommand = new GroupCommand("NAME", 2);
        groupCommand.add(group1);        
        groupCommand.add(group2);        

        List<GroupCommand> groupCommands = new ArrayList<GroupCommand>();
        groupCommands.add(groupCommand);

 
        GroupResponse groupResponse = EasyMock.createMock(GroupResponse.class);
        QueryResponse queryResponse = EasyMock.createMock(QueryResponse.class);
        EasyMock.expect(solrOperations.execute((SolrCallback<QueryResponse>)EasyMock.isA(SolrCallback.class))).andReturn(queryResponse);
        EasyMock.expect(queryResponse.getGroupResponse()).andReturn(groupResponse);
        EasyMock.expect(groupResponse.getValues()).andReturn(groupCommands);

        EasyMock.replay(solrOperations, queryResponse, groupResponse);
        assertEquals("autocomplete should return 2 matches",facetableRepository.autocomplete("TERM", "FILTER_QUERY").size(), 2);
        EasyMock.verify(solrOperations, queryResponse, groupResponse);
    } 

    @Test
    public void testAutocompleteNullQuery() {

        EasyMock.replay(solrOperations);
        assertEquals("autocomplete should return 0 matches",facetableRepository.autocomplete(null, "FILTER_QUERY").size(), 0);
        EasyMock.verify(solrOperations);
    } 

    @Test
    public void testAutocompleteEmptyQuery() {

        EasyMock.replay(solrOperations);
        assertEquals("autocomplete should return 0 matches",facetableRepository.autocomplete("", "FILTER_QUERY").size(), 0);
        EasyMock.verify(solrOperations);
    } 
}
