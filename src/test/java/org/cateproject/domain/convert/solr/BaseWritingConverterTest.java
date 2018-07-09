package org.cateproject.domain.convert.solr;

import static org.junit.Assert.assertEquals; 
import static org.junit.Assert.assertNull; 

import org.apache.solr.common.SolrInputDocument;
import org.cateproject.domain.Taxon;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class BaseWritingConverterTest {

    private BaseWritingConverter<Taxon> baseWritingConverter;

    @Before
    public void setUp() {
        baseWritingConverter = new BaseWritingConverter<Taxon>();
    }

    @Test
    public void testConvert() {
        Taxon taxon = new Taxon();
        taxon.setCreator("CREATOR");
        taxon.setId(1L);
        taxon.setCreated(new DateTime(2000,1,1,1,1,1));
        SolrInputDocument solrDocument = baseWritingConverter.convert(taxon);
        assertEquals("base.created_dt should equal '2000-01-01T01:01:01Z'", solrDocument.getFieldValue("base.created_dt"), "2000-01-01T01:01:01Z");        
        assertEquals("base.creator_s should equal 'CREATOR'", solrDocument.getFieldValue("base.creator_s"), "CREATOR");        
        assertEquals("base.id_l should equal 1L", solrDocument.getFieldValue("base.id_l"), 1L);        
        assertEquals("super.class_s should equal 'org.cateproject.domain.Base'", solrDocument.getFieldValue("super.class_s"), "org.cateproject.domain.Base");        
        assertNull("base.modified_dt should be null", solrDocument.getFieldValue("base.modified_dt"));        
    }

    @Test
    public void testConvertModified() {
        Taxon taxon = new Taxon();
        taxon.setCreator("CREATOR");
        taxon.setId(1L);
        taxon.setCreated(new DateTime(2000,1,1,1,1,1));
        taxon.setModified(new DateTime(2000,1,1,1,2,3));
        SolrInputDocument solrDocument = baseWritingConverter.convert(taxon);
        assertEquals("base.created_dt should equal '2000-01-01T01:01:01Z'", solrDocument.getFieldValue("base.created_dt"), "2000-01-01T01:01:01Z");        
        assertEquals("base.creator_s should equal 'CREATOR'", solrDocument.getFieldValue("base.creator_s"), "CREATOR");        
        assertEquals("base.id_l should equal 1L", solrDocument.getFieldValue("base.id_l"), 1L);        
        assertEquals("super.class_s should equal 'org.cateproject.domain.Base'", solrDocument.getFieldValue("super.class_s"), "org.cateproject.domain.Base");        
        assertEquals("base.modified_dt should equal '2000-01-01T01:02:03Z'", solrDocument.getFieldValue("base.modified_dt"),"2000-01-01T01:02:03Z");        
    }
}
