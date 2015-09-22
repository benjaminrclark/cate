package org.cateproject.domain.convert.solr;

import org.apache.solr.common.SolrDocument;
import org.cateproject.domain.Base;
import org.cateproject.repository.jpa.DatasetRepository;
import org.cateproject.repository.jpa.IdentificationKeyRepository;
import org.cateproject.repository.jpa.MultimediaRepository;
import org.cateproject.repository.jpa.ReferenceRepository;
import org.cateproject.repository.jpa.TaxonRepository;
import org.cateproject.repository.jpa.TermRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

public class BaseReadingConverter implements Converter<SolrDocument, Base> {
	
	@Autowired
	private DatasetRepository datasetRepository;
	
	@Autowired
	private TermRepository termRepository;
	
	@Autowired
	private MultimediaRepository multimediaRepository;
	
	@Autowired
	private ReferenceRepository referenceRepository;
	
	@Autowired
	private TaxonRepository taxonRepository;
	
	@Autowired
	private IdentificationKeyRepository identificationKeyRepository;

        public void setDatasetRepository(DatasetRepository datasetRepository) {
            this.datasetRepository = datasetRepository;
        }

        public void setTermRepository(TermRepository termRepository) {
            this.termRepository = termRepository;
        }

        public void setMultimediaRepository(MultimediaRepository multimediaRepository) {
            this.multimediaRepository = multimediaRepository;
        }

        public void setReferenceRepository(ReferenceRepository referenceRepository) {
            this.referenceRepository = referenceRepository;
        }

        public void setTaxonRepository(TaxonRepository taxonRepository) {
            this.taxonRepository = taxonRepository;
        }
 
        public void setIdentificationKeyRepository(IdentificationKeyRepository identificationKeyRepository) {
            this.identificationKeyRepository = identificationKeyRepository;
        }

	@Override
	public Base convert(SolrDocument solrDocument) {
	    Long id = (Long)solrDocument.getFieldValue("base.id_l");
            String className = (String)solrDocument.getFieldValue("base.class_s");
            if(className.equals("org.cateproject.domain.Dataset")) {
                return datasetRepository.findOne(id);
            } else if(className.equals("org.cateproject.domain.Multimedia")) {
                return multimediaRepository.findOne(id);
            } else if(className.equals("org.cateproject.domain.Reference")) {
                return referenceRepository.findOne(id);
            } else if(className.equals("org.cateproject.domain.Taxon")) {
                return taxonRepository.findOne(id);
            } else if(className.equals("org.cateproject.domain.Term")) {
                return termRepository.findOne(id);
            } else if(className.equals("org.cateproject.domain.IdentificationKey")) {
                return identificationKeyRepository.findOne(id);
            } else {
                throw new RuntimeException("Could not instantiate bean" + className);
            }
	}
}
