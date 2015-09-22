package org.cateproject.domain.convert.solr;

import org.apache.solr.common.SolrInputDocument;
import org.cateproject.domain.Multimedia;

public class MultimediaWritingConverter extends BaseWritingConverter<Multimedia> {

	@Override
	public SolrInputDocument convert(Multimedia multimedia) {
            SolrInputDocument solrInputDocument = super.convert(multimedia);
            solrInputDocument.addField("id", "multimedia_" + multimedia.getId());
            solrInputDocument.addField("base.label_sort", multimedia.getTitle());
            solrInputDocument.addField("base.class_s", "org.cateproject.domain.Image");
            solrInputDocument.addField("base.title_s", multimedia.getTitle());        
        
            solrInputDocument.addField("multimedia.identifier_s", multimedia.getIdentifier());
            solrInputDocument.addField("multimedia.refrences_s", multimedia.getReferences());
            solrInputDocument.addField("multimedia.description_s", multimedia.getDescription());
            solrInputDocument.addField("multimedia.spatial_s", multimedia.getSpatial());
            solrInputDocument.addField("multimedia.latitude_d", multimedia.getLatitude());
            solrInputDocument.addField("multimedia.longitude_d", multimedia.getLongitude());
            solrInputDocument.addField("multimedia.format_s", multimedia.getFormat());
            solrInputDocument.addField("multimedia.created_t", multimedia.getCreated());
            solrInputDocument.addField("multimedia.contributor_s", multimedia.getContributor());
            solrInputDocument.addField("multimedia.publisher_s", multimedia.getPublisher());
            solrInputDocument.addField("multimedia.audience_s", multimedia.getAudience());
            solrInputDocument.addField("multimedia.license_s", multimedia.getLicense());
            solrInputDocument.addField("multimedia.rightsholder_s", multimedia.getRightsHolder());
            // Add summary field to allow searching documents for objects of this type
            solrInputDocument.addField("base_solrsummary_t", new StringBuilder().append(multimedia.getTitle()).append(" ").append(multimedia.getCreator()).append(" ").append(multimedia.getId()).append(" ").append(multimedia.getIdentifier()).append(" ").append(multimedia.getReferences()).append(" ").append(multimedia.getDescription()).append(" ").append(multimedia.getSpatial()).append(" ").append(multimedia.getLatitude()).append(" ").append(multimedia.getLongitude()).append(" ").append(multimedia.getFormat()).append(" ").append(multimedia.getCreated()).append(" ").append(multimedia.getContributor()).append(" ").append(multimedia.getPublisher()).append(" ").append(multimedia.getAudience()).append(" ").append(multimedia.getLicense()).append(" ").append(multimedia.getRightsHolder()));
	    return solrInputDocument;
	}

}
