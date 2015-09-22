package org.cateproject.domain.convert.solr;

import static org.junit.Assert.assertEquals;

import org.apache.solr.common.SolrInputDocument;
import org.cateproject.domain.Dataset;
import org.junit.Before;
import org.junit.Test;

public class DatasetWritingConverterTest {

    public DatasetWritingConverter datasetWritingConverter;

    @Before
    public void setUp() {
        datasetWritingConverter = new DatasetWritingConverter();
    }

    @Test
    public void testConvert() {
       Dataset dataset = new Dataset();
       dataset.setId(1L);
       dataset.setTitle("TITLE");
       dataset.setDescription("DESCRIPTION");

       SolrInputDocument solrDocument = datasetWritingConverter.convert(dataset);

       assertEquals("id should equal 'dataset_1'", solrDocument.getFieldValue("id"), "dataset_1");
       assertEquals("dataset.title_s should equal 'TITLE'", solrDocument.getFieldValue("dataset.title_s"), "TITLE");       
       assertEquals("dataset.description_t should equal 'DESCRIPTION'", solrDocument.getFieldValue("dataset.description_t"), "DESCRIPTION");
       assertEquals("base.class_s should equal 'org.cateproject.domain.Dataset'", solrDocument.getFieldValue("base.class_s"),"org.cateproject.domain.Dataset");  
    }

}
