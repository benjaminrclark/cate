package org.cateproject.web;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

import org.cateproject.domain.Base;
import org.cateproject.repository.search.BaseRepository;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.core.query.FacetQuery;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.SolrResultPage;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

public class ExploreControllerTest {
    
    private ExploreController exploreController;

    private BaseRepository baseRepository;

    @Before
    public void setUp() {
        exploreController = new ExploreController();
        baseRepository = EasyMock.createMock(BaseRepository.class);
        exploreController.setBaseRepository(baseRepository);
    }

    @Test
    public void testGetFacets() {
        List<String> expectedFacets = new ArrayList<String>();
        expectedFacets.add("base.class_s");
        expectedFacets.add("taxon.genus_s");
        expectedFacets.add("taxon.distribution_TDWG_0_ss");
        assertEquals("getFacets should return the expected facets", exploreController.getFacets(), expectedFacets);
    }

    @Test
    public void testExplore() throws Exception {
        Model model = new ExtendedModelMap();
        PageRequest pageRequest = new PageRequest(0,10);
        List<FilterQuery> filterQueries = new ArrayList<FilterQuery>();
        filterQueries.add(new SimpleFilterQuery());
        FacetPage<Base> result = new SolrResultPage<Base>(new ArrayList<Base>());

        EasyMock.expect(baseRepository.search(EasyMock.isA(FacetQuery.class))).andReturn(result);

        EasyMock.replay(baseRepository);
        assertEquals("explore should return 'explore'", exploreController.explore(model,"QUERY",pageRequest,filterQueries), "explore");
        assertEquals("model should contain the result object",model.asMap().get("result"),result);
        EasyMock.verify(baseRepository);
    }

    @Test
    public void testExploreNullQuery() throws Exception {
        Model model = new ExtendedModelMap();
        PageRequest pageRequest = new PageRequest(0,10);
        List<FilterQuery> filterQueries = new ArrayList<FilterQuery>();
        FacetPage<Base> result = new SolrResultPage<Base>(new ArrayList<Base>());

        EasyMock.expect(baseRepository.search(EasyMock.isA(FacetQuery.class))).andReturn(result);

        EasyMock.replay(baseRepository);
        assertEquals("explore should return 'explore'", exploreController.explore(model,null,pageRequest,null), "explore");
        assertEquals("model should contain the result object",model.asMap().get("result"),result);
        EasyMock.verify(baseRepository);
    }

    @Test
    public void testExploreEmptyQuery() throws Exception {
        Model model = new ExtendedModelMap();
        PageRequest pageRequest = new PageRequest(0,10);
        List<FilterQuery> filterQueries = new ArrayList<FilterQuery>();
        FacetPage<Base> result = new SolrResultPage<Base>(new ArrayList<Base>());

        EasyMock.expect(baseRepository.search(EasyMock.isA(FacetQuery.class))).andReturn(result);

        EasyMock.replay(baseRepository);
        assertEquals("explore should return 'explore'", exploreController.explore(model,"",pageRequest,null), "explore");
        assertEquals("model should contain the result object",model.asMap().get("result"),result);
        EasyMock.verify(baseRepository);
    }
}
