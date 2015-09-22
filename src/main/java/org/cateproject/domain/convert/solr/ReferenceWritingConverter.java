package org.cateproject.domain.convert.solr;

import org.apache.solr.common.SolrInputDocument;
import org.cateproject.domain.Reference;

public class ReferenceWritingConverter extends BaseWritingConverter<Reference> {

	@Override
	public SolrInputDocument convert(Reference reference) {
	    SolrInputDocument solrInputDocument = super.convert(reference);
            solrInputDocument.addField("id", "reference_" + reference.getId());
            solrInputDocument.addField("base.label_sort", reference.getBibliographicCitation());
            solrInputDocument.addField("base.class_s", "org.cateproject.domain.Reference");
            solrInputDocument.addField("base.title_s", reference.getTitle());
        
            solrInputDocument.addField("reference.identifier_s", reference.getIdentifier());
            solrInputDocument.addField("reference.bibliographiccitation_s", reference.getBibliographicCitation());
            solrInputDocument.addField("reference.date_t", reference.getDate());
            solrInputDocument.addField("reference.source_s", reference.getSource());
            solrInputDocument.addField("reference.description_s", reference.getDescription());
            solrInputDocument.addField("reference.subject_s", reference.getSubject());
            solrInputDocument.addField("reference.language_t", reference.getLanguage());
            solrInputDocument.addField("reference.rights_s", reference.getRights());
            solrInputDocument.addField("reference.taxonremarks_s", reference.getTaxonRemarks());
            solrInputDocument.addField("reference.type_t", reference.getType());
            // Add summary field to allow searching documents for objects of this type
            solrInputDocument.addField("base_solrsummary_t", new StringBuilder().append(reference.getTitle()).append(" ").append(reference.getCreator()).append(" ").append(reference.getId()).append(" ").append(reference.getIdentifier()).append(" ").append(reference.getBibliographicCitation()).append(" ").append(reference.getDate()).append(" ").append(reference.getSource()).append(" ").append(reference.getDescription()).append(" ").append(reference.getSubject()).append(" ").append(reference.getLanguage()).append(" ").append(reference.getRights()).append(" ").append(reference.getTaxonRemarks()).append(" ").append(reference.getType()));
		return solrInputDocument;
	}

}
