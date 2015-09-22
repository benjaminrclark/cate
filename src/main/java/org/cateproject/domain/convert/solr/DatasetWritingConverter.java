package org.cateproject.domain.convert.solr;

import org.apache.solr.common.SolrInputDocument;
import org.cateproject.domain.Dataset;

public class DatasetWritingConverter extends BaseWritingConverter<Dataset> {

	@Override
	public SolrInputDocument convert(Dataset dataset) {
	    SolrInputDocument solrInputDocument = super.convert(dataset);
            solrInputDocument.addField("id", "dataset_" + dataset.getId());
            solrInputDocument.addField("dataset.title_s", dataset.getTitle());
            solrInputDocument.addField("dataset.description_t",dataset.getDescription());        
         
            solrInputDocument.addField("base.class_s", "org.cateproject.domain.Dataset");
            solrInputDocument.addField("base_solrsummary_t", new StringBuilder().append(dataset.getTitle()).append(" ").append(dataset.getCreator()).append(" ").append(dataset.getDescription()).append(" ").append(dataset.getId()));
         
	    return solrInputDocument;
	}

}
