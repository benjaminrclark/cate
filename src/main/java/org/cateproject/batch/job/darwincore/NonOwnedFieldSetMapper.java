package org.cateproject.batch.job.darwincore;

import org.cateproject.domain.NonOwnedEntity;
import org.cateproject.domain.Taxon;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.Term;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NonOwnedFieldSetMapper<T extends NonOwnedEntity> extends BaseFieldSetMapper<T> {
	
	private Logger logger = LoggerFactory.getLogger(NonOwnedFieldSetMapper.class);

	public NonOwnedFieldSetMapper(Class<T> newType) {
		super(newType);
	}
	
	@Override
	public void mapField(T object, String fieldName, String value) {
	    super.mapField(object, fieldName, value);
	    Term term = getTermFactory().findTerm(fieldName);
		
	    // DwcTerms
            if (term instanceof DwcTerm) {
                DwcTerm dwcTerm = (DwcTerm) term;
                switch (dwcTerm) {
                    case taxonID:
            	        if (value != null && !value.isEmpty()) {
			    Taxon taxon = new Taxon();
			    taxon.setIdentifier(value);
			    object.getTaxa().add(taxon);
		        }            	
                    break;
                    default:
                        break;
                }
            }
	}
}
