package org.cateproject.domain.convert.solr;

import org.apache.solr.common.SolrInputDocument;
import org.cateproject.domain.Base;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.core.convert.converter.Converter;

public class BaseWritingConverter<T extends Base> implements Converter<T, SolrInputDocument> {
	
	protected static DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

	@Override
	public SolrInputDocument convert(T t) {
	    SolrInputDocument solrInputDocument = new SolrInputDocument();	
            solrInputDocument.addField("base.created_dt", dateTimeFormatter.print(t.getCreated()));
         
            if(t.getModified() != null) {
                solrInputDocument.addField("base.modified_dt", dateTimeFormatter.print(t.getModified()));
            }
         
            solrInputDocument.addField("base.creator_s", t.getCreator());
            solrInputDocument.addField("base.id_l", t.getId());
            solrInputDocument.addField("super.class_s", "org.cateproject.domain.Base");
	    return solrInputDocument;
	}

}
