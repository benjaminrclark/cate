package org.cateproject.domain.convert.solr;

import org.apache.solr.common.SolrInputDocument;
import org.cateproject.domain.Description;
import org.cateproject.domain.Distribution;
import org.cateproject.domain.Taxon;
import org.cateproject.domain.constants.Location;

public class TaxonWritingConverter extends BaseWritingConverter<Taxon> {

	@Override
	public SolrInputDocument convert(Taxon taxon) {
            SolrInputDocument solrInputDocument = super.convert(taxon);
	    StringBuilder summary = new StringBuilder();
            solrInputDocument.addField("id", "taxon_" + taxon.getId());
            solrInputDocument.addField("base.label_sort", taxon.getScientificName());
            solrInputDocument.addField("base.class_s", "org.cateproject.domain.Taxon");
            summary.append(taxon.getCreator());
            solrInputDocument.addField("taxon.taxonid_s", taxon.getTaxonId());
            summary.append(" ").append(taxon.getTaxonId());
            solrInputDocument.addField("taxon.scientificnameid_s", taxon.getScientificNameID());
            summary.append(" ").append(taxon.getScientificNameID());
            solrInputDocument.addField("taxon.namepublishedinyear_i", taxon.getNamePublishedInYear());
            summary.append(" ").append(taxon.getNamePublishedInYear());
            solrInputDocument.addField("taxon.higherclassification_s", taxon.getHigherClassification());
            summary.append(" ").append(taxon.getHigherClassification());
            solrInputDocument.addField("taxon.kingdom_s", taxon.getKingdom());
            summary.append(" ").append(taxon.getKingdom());
            solrInputDocument.addField("taxon.phylum_s", taxon.getPhylum());
            summary.append(" ").append(taxon.getPhylum());
            solrInputDocument.addField("taxon.clazz_s", taxon.getClazz());
            summary.append(" ").append(taxon.getClazz());
            solrInputDocument.addField("taxon.order_s", taxon.getOrder());
            summary.append(" ").append(taxon.getOrder());
            solrInputDocument.addField("taxon.family_s", taxon.getFamily());
            summary.append(" ").append(taxon.getFamily());
            solrInputDocument.addField("taxon.genus_s", taxon.getGenus());
            summary.append(" ").append(taxon.getGenus());
            solrInputDocument.addField("taxon.subgenus_s", taxon.getSubgenus());
            summary.append(" ").append(taxon.getSubgenus());
            solrInputDocument.addField("taxon.specificepithet_s", taxon.getSpecificEpithet());
            summary.append(" ").append(taxon.getSpecificEpithet());
            solrInputDocument.addField("taxon.infraspecificepithet_s", taxon.getInfraspecificEpithet());
            summary.append(" ").append(taxon.getInfraspecificEpithet());
            solrInputDocument.addField("taxon.taxonrank_t", taxon.getTaxonRank());
            summary.append(" ").append(taxon.getTaxonRank());
            solrInputDocument.addField("taxon.nomenclaturalstatus_t", taxon.getNomenclaturalStatus());
            summary.append(" ").append(taxon.getNomenclaturalStatus());
            solrInputDocument.addField("taxon.verbatimtaxonrank_s", taxon.getVerbatimTaxonRank());
            summary.append(" ").append(taxon.getVerbatimTaxonRank());
            solrInputDocument.addField("taxon.scientificnameauthorship_s", taxon.getScientificNameAuthorship());
            summary.append(" ").append(taxon.getScientificNameAuthorship());
            solrInputDocument.addField("taxon.nomenclaturalcode_t", taxon.getNomenclaturalCode());
            summary.append(" ").append(taxon.getNomenclaturalCode());
            solrInputDocument.addField("taxon.taxonomicstatus_t", taxon.getTaxonomicStatus());
            summary.append(" ").append(taxon.getTaxonomicStatus());
            solrInputDocument.addField("taxon.taxonremarks_s", taxon.getTaxonRemarks());
            summary.append(" ").append(taxon.getTaxonRemarks());
            solrInputDocument.addField("taxon.parentnameusage_t", taxon.getParentNameUsage());
            summary.append(" ").append(taxon.getParentNameUsage());
            solrInputDocument.addField("taxon.acceptednameusage_t", taxon.getAcceptedNameUsage());
            summary.append(" ").append(taxon.getAcceptedNameUsage());
            solrInputDocument.addField("taxon.originalnameusage_t", taxon.getOriginalNameUsage());
            summary.append(" ").append(taxon.getOriginalNameUsage());
            solrInputDocument.addField("taxon.namepublishedin_t", taxon.getNamePublishedIn());
            summary.append(" ").append(taxon.getNamePublishedIn());
            solrInputDocument.addField("taxon.nameaccordingto_t", taxon.getNameAccordingTo());
            summary.append(" ").append(taxon.getNameAccordingTo());
            solrInputDocument.addField("taxon.scientificname_s", taxon.getScientificName());
            summary.append(" ").append(taxon.getScientificName());
                
            for (Distribution d : taxon.getDistribution()) {
                Location  l = d.getLocation();
                switch(l.getLevel()) {
                    case 0:
                        for (Location r : l.getChildren()) {
                            for (Location c : r.getChildren()) {
                                indexLocation(c, solrInputDocument);
                            }
                        }
                        break;
                    case 1:
                        for (Location c : l.getChildren()) {
                            indexLocation(c, solrInputDocument);
                        }
                        break;
                    case 2:
                        default:
                        indexLocation(l, solrInputDocument);
                        break;
                }
            }
         
            for(Description d : taxon.getDescriptions()) {
	        summary.append(" ").append(d.getDescription());
	    }
         
            solrInputDocument.addField("base_solrsummary_t", summary);
         
            return solrInputDocument;
	}
	
	public static String indexLocation(Location l, SolrInputDocument sid) {
            String facet = null;
            if (l.getParent() != null) {
                facet = indexLocation(l.getParent(), sid) + "_" + l.name();
            } else {
                facet = l.name();
            }
            sid.addField("taxon.distribution_" + l.getPrefix() + "_" + l.getLevel() + "_ss", facet);
            return facet;
        }
}
