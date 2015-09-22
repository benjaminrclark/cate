package org.cateproject.domain.convert.solr;

import org.apache.solr.common.SolrInputDocument;
import org.cateproject.domain.Term;

public class TermWritingConverter extends BaseWritingConverter<Term> {

	@Override
	public SolrInputDocument convert(Term term) {
	    SolrInputDocument solrInputDocument = super.convert(term);
            solrInputDocument.addField("id", "term_" + term.getId());
            solrInputDocument.addField("base.label_sort", term.getTitle());
         
            solrInputDocument.addField("base.class_s", "org.cateproject.domain.Term");
            solrInputDocument.addField("base.title_s", term.getTitle());
            solrInputDocument.addField("term.description_s", term.getDescription());
            solrInputDocument.addField("term.charactr_t", term.getCharacter());
            solrInputDocument.addField("term.unit_s", term.getUnit());
            solrInputDocument.addField("term.type_s", term.getType());
            solrInputDocument.addField("base_solrsummary_t", new StringBuilder().append(term.getTitle()).append(" ").append(term.getCreator()).append(" ").append(term.getId()).append(" ").append(term.getDescription()).append(" ").append(term.getCharacter()).append(" ").append(term.getUnit()).append(" ").append(term.getType()));
	    return solrInputDocument;
	}

}
