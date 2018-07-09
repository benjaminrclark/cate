package org.cateproject.web.format;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleFilterQuery;

public class FilterQueryFormatterTest {
    
    private FilterQueryFormatter filterQueryFormatter;

    @Before
    public void setUp() {
        filterQueryFormatter = new FilterQueryFormatter();
    }

    @Test
    public void testParse() throws ParseException {
        assertNotNull("parse should return the expected filterQuery", filterQueryFormatter.parse("FIELD:VALUE", null));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidString() throws ParseException {
        filterQueryFormatter.parse("INVALID_STRING", null);
    }

    @Test
    public void testPrint() {
        SimpleFilterQuery filterQuery = new SimpleFilterQuery();
        filterQuery.addCriteria(Criteria.where("FIELD").expression("EXPRESSION"));
        assertEquals("print should return a string",filterQueryFormatter.print(filterQuery,null).getClass(), String.class);
    }
}
