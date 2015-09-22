package org.cateproject.domain.convert.solr;

import org.apache.solr.common.SolrInputDocument;
import org.cateproject.domain.IdentificationKey;

public class IdentificationKeyWritingConverter extends BaseWritingConverter<IdentificationKey> {

	@Override
	public SolrInputDocument convert(IdentificationKey identificationKey) {
            SolrInputDocument solrInputDocument = super.convert(identificationKey);
            solrInputDocument.addField("id", "identificationkey_" + identificationKey.getId());
            solrInputDocument.addField("base.label_sort", identificationKey.getTitle());
         
            solrInputDocument.addField("base.class_s", "org.cateproject.domain.IdentificationKey");
            solrInputDocument.addField("base.title_s", identificationKey.getTitle());
            solrInputDocument.addField("identificationKey.description_s", identificationKey.getDescription());
            solrInputDocument.addField("identificationKey.compiledkey_s", identificationKey.getCompiledKey());
            // Add summary field to allow searching documents for objects of this type
            solrInputDocument.addField("base_solrsummary_t", new StringBuilder().append(identificationKey.getTitle()).append(" ").append(identificationKey.getCreator()).append(" ").append(identificationKey.getId()).append(" ").append(identificationKey.getDescription()).append(" ").append(identificationKey.getCompiledKey()));
	    return solrInputDocument;
	}

}
