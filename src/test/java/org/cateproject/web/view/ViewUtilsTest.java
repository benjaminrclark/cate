package org.cateproject.web.view;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.cateproject.domain.Base;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.core.query.SimpleField;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.SimpleFacetFieldEntry;
import org.springframework.data.solr.core.query.result.SolrResultPage;
import org.springframework.mock.web.MockHttpServletRequest;

public class ViewUtilsTest {

	private ViewUtils viewUtils;
	
	@Before
	public void setUp() throws Exception {
		viewUtils = new ViewUtils();
	}

	@Test
	public void testToCommaSeparatedString() {
		assertEquals("toCommaSeparatedString should return 'one,two,three'","one,two,three",viewUtils.toCommaSeparatedString(new Object[]{"one","two","three"}));
		assertNull("toCommaSeparatedString should return null", viewUtils.toCommaSeparatedString(null));
	}

        @Test
        public void testGetSelectedFacet() {
            FacetFieldEntry facetFieldEntry = new SimpleFacetFieldEntry(new SimpleField("FIELD_NAME"),"FIELD_VALUE", 10L);
            List<FacetFieldEntry> facets = new ArrayList<FacetFieldEntry>();
            facets.add(facetFieldEntry);
            PageImpl<FacetFieldEntry> facetResultPage = new PageImpl<FacetFieldEntry>(facets);  
            PageImpl<FacetFieldEntry> emptyResultPage = new PageImpl<FacetFieldEntry>(new ArrayList<FacetFieldEntry>());  
            SolrResultPage<Base> facetPage = new SolrResultPage<Base>(new ArrayList<Base>());
            facetPage.addFacetResultPage(facetResultPage, new SimpleField("FIELD_NAME")); 
            facetPage.addFacetResultPage(emptyResultPage, new SimpleField("EMPTY_FIELD"));
            String[] filterQueries = new String[] {"FIELD_NAME:FIELD_VALUE","OTHER_FIELD_NAME:OTHER_FIELD_VALUE"};

            assertEquals("getSelectedFacet should return the expected facetFieldEntry",viewUtils.getSelectedFacet(filterQueries,"FIELD_NAME",facetPage), facetFieldEntry);
            assertNull("getSelectedFacet should return the null if the result page has no facets",viewUtils.getSelectedFacet(filterQueries,"EMPTY_FIELD",facetPage));
        }

        @Test
        public void testGetSelectedFacetNull() {
            SolrResultPage<Base> facetPage = new SolrResultPage<Base>(new ArrayList<Base>());
            assertNull("getSelectedFacet should return null if the filter queries are null",viewUtils.getSelectedFacet(null,"FIELD_NAME",facetPage));
        }

        @Test
        public void testGetSelectedFacetEmpty() {
            SolrResultPage<Base> facetPage = new SolrResultPage<Base>(new ArrayList<Base>());
            assertNull("getSelectedFacet should return null if the filter queries are empty",viewUtils.getSelectedFacet(new String[] {},"FIELD_NAME",facetPage));
        }

        @Test
        public void testFacetParams() {
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addParameter("filterQuery","EXCLUDE_FACET");
            request.addParameter("page","2");
            request.addParameter("size","20");
            request.addParameter("sort","PARAM_SORT");
            request.addParameter("view", "PARAM_VIEW");
            request.addParameter("OTHER","OTHER_VALUE");
            MockHttpServletRequest emptyRequest = new MockHttpServletRequest();
            SolrResultPage<Base> facetPage = new SolrResultPage<Base>(new ArrayList<Base>(), new PageRequest(0,10),10L, 1.0f);

            assertEquals("facetParams should return the expected string",viewUtils.facetParams(request, null, null, null, null,facetPage,null,null),"?OTHER=OTHER_VALUE&filterQuery=EXCLUDE_FACET&page=0&size=10&sort=PARAM_SORT&view=PARAM_VIEW");
            assertEquals("facetParams should return the expected string",viewUtils.facetParams(request, "INCLUDE_FACET", "EXCLUDE_FACET", "SORT", "VIEW",facetPage,1,2),"?OTHER=OTHER_VALUE&filterQuery=INCLUDE_FACET&page=1&size=2&sort=SORT&view=VIEW");
            assertEquals("facetParams should return the expected string",viewUtils.facetParams(request, "INCLUDE_FACET", "EXCLUDE_FACET", "relevance", "VIEW",facetPage,1,2),"?OTHER=OTHER_VALUE&filterQuery=INCLUDE_FACET&page=1&size=2&view=VIEW");
            assertEquals("facetParams should return the expected string",viewUtils.facetParams(request, "INCLUDE_FACET", "EXCLUDE_FACET", null, null,facetPage,1,2),"?OTHER=OTHER_VALUE&filterQuery=INCLUDE_FACET&page=1&size=2&sort=PARAM_SORT&view=PARAM_VIEW");
            assertEquals("facetParams should return the expected string",viewUtils.facetParams(emptyRequest, null , null, null, null,facetPage,1,2),"?page=1&size=2");
        }
}
