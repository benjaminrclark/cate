package org.cateproject.batch.job.darwincore.reference;

import java.util.Locale;

import org.cateproject.domain.Reference;
import org.cateproject.domain.constants.ReferenceType;
import org.cateproject.batch.job.darwincore.NonOwnedFieldSetMapper;
import org.gbif.dwc.terms.Term;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author ben
 * 
 */
public class FieldSetMapper extends NonOwnedFieldSetMapper<Reference> {

	public FieldSetMapper() {
		super(Reference.class);
	}

	private Logger logger = LoggerFactory.getLogger(FieldSetMapper.class);

	@Override
	public final void mapField(final Reference object, final String fieldName, final String value) {
	    super.mapField(object, fieldName, value);
	    Term term = getTermFactory().findTerm(fieldName);

	    if (term instanceof DcTerm) {
		DcTerm dcTerm = (DcTerm) term;
		switch (dcTerm) {
		case bibliographicCitation:
                    object.setBibliographicCitation(value);
                    break;
                case date:
                    object.setDate(value);
                    break;
                case description:
                    object.setDescription(value);
                    break;
                case identifier:
                    object.setIdentifier(value);
                    break;
                case language:
                    object.setLanguage(conversionService.convert(value, Locale.class));
                    break;
                case source:
                    object.setSource(value);
                    break;
                case subject:
                    object.setSubject(value);
                    break;
                case title:
                    object.setTitle(value);
                    break;
                case type:
            	    object.setType(conversionService.convert(value, ReferenceType.class));                
                    break;
                default:
                    break;
                }
            }

	    // DwcTerms
            if (term instanceof DwcTerm) {
		DwcTerm dwcTerm = (DwcTerm) term;
		switch (dwcTerm) {
	        case taxonRemarks:
                    object.setTaxonRemarks(value);
		    break;
		default:
		    break;
		}
	    }
	}
}
