package org.cateproject.batch.job.darwincore;

import org.cateproject.domain.Base;
import org.cateproject.domain.Dataset;
import org.gbif.dwc.terms.Term;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseFieldSetMapper<T extends Base> extends DarwinCoreFieldSetMapper<T> {
	
    private Logger logger = LoggerFactory.getLogger(BaseFieldSetMapper.class);
	
    public BaseFieldSetMapper(Class<T> newType) {
	super(newType);		
    }
	
    @Override
    public void mapField(T object, String fieldName, String value) {
	Term term = getTermFactory().findTerm(fieldName);
        //logger.info("Mapping " + fieldName + " " + " " + value + " to " + object);
        if (term instanceof DcTerm) {
            DcTerm dcTerm = (DcTerm) term;
            switch (dcTerm) {
            case accessRights:
            	object.setAccessRights(value);
            	break;
            case created:
                object.setCreated(conversionService.convert(value, DateTime.class));
                break;
            case license:
            	object.setLicense(value);
            	break;
            case modified:
                object.setModified(conversionService.convert(value,DateTime.class));
                break;
            case rights:
            	object.setRights(value);
            	break;
            case rightsHolder:
            	object.setRightsHolder(value);
            	break;
            default:
                break;
            }
        }
        
	if (term instanceof DwcTerm) {
     	    DwcTerm dwcTerm = (DwcTerm) term;
	    switch (dwcTerm) {
            case datasetID:
                if(value != null && value.trim().length() > 0) {
	            Dataset dataset = new Dataset();
	            dataset.setIdentifier(value);
	            object.setDataset(dataset);
	        }
                break;
            default:
                break;
            }
 	}
    }
}
