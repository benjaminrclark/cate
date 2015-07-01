package org.cateproject.repository.jpa;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.cateproject.domain.MeasurementOrFact;
import org.cateproject.domain.Taxon;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class MeasurementOrFactDataOnDemand {
	
	@Autowired
	private TaxonDataOnDemand taxonDataOnDemand;
	
    private Random rnd = new SecureRandom();
    
    private List<MeasurementOrFact> data;
    
    @Autowired
    DatasetDataOnDemand datasetDataOnDemand;
    
    @Autowired
    TermDataOnDemand termDataOnDemand;
    
    @Autowired
    MeasurementOrFactRepository measurementOrFactRepository;
    
    public MeasurementOrFact getNewTransientMeasurementOrFact(int index) {
        MeasurementOrFact obj = new MeasurementOrFact();
        setAccessRights(obj, index);
        setAccuracy(obj, index);
        setContributor(obj, index);
        setCreated(obj, index);
        setCreator(obj, index);
        setDeterminedBy(obj, index);
        setDeterminedDate(obj, index);
        setIdentifier(obj, index);
        setLicense(obj, index);
        setModified(obj, index);
        setMethod(obj, index);
        setRemarks(obj, index);
        setRights(obj, index);
        setRightsHolder(obj, index);
        setTaxon(obj, index);
        setUnit(obj, index);
        setValue(obj, index);
        return obj;
    }
    
    public void setAccessRights(MeasurementOrFact obj, int index) {
        String accessRights = "accessRights_" + index;
        obj.setAccessRights(accessRights);
    }
    
    public void setAccuracy(MeasurementOrFact obj, int index) {
        String accuracy = "accuracy_" + index;
        obj.setAccuracy(accuracy);
    }
    
    public void setContributor(MeasurementOrFact obj, int index) {
        String contributor = "contributor_" + index;
        obj.setContributor(contributor);
    }
    
    public void setCreated(MeasurementOrFact obj, int index) {
        DateTime created = null;
        obj.setCreated(created);
    }
    
    public void setCreator(MeasurementOrFact obj, int index) {
        String creator = "creator_" + index;
        obj.setCreator(creator);
    }
    
    public void setDeterminedBy(MeasurementOrFact obj, int index) {
        String determinedBy = "determinedBy_" + index;
        obj.setDeterminedBy(determinedBy);
    }
    
    public void setDeterminedDate(MeasurementOrFact obj, int index) {
        DateTime determinedDate = null;
        obj.setDeterminedDate(determinedDate);
    }
    
    public void setIdentifier(MeasurementOrFact obj, int index) {
        String identifier = "identifier_" + index;
        obj.setIdentifier(identifier);
    }
    
    public void setLicense(MeasurementOrFact obj, int index) {
        String license = "license_" + index;
        obj.setLicense(license);
    }
    
    public void setModified(MeasurementOrFact obj, int index) {
        DateTime modified = null;
        obj.setModified(modified);
    }
    
    public void setMethod(MeasurementOrFact obj, int index) {
        String method = "method_" + index;
        obj.setMethod(method);
    }
    
    public void setRemarks(MeasurementOrFact obj, int index) {
        String remarks = "remarks_" + index;
        obj.setRemarks(remarks);
    }
    
    public void setRights(MeasurementOrFact obj, int index) {
        String rights = "rights_" + index;
        obj.setRights(rights);
    }
    
    public void setRightsHolder(MeasurementOrFact obj, int index) {
        String rightsHolder = "rightsHolder_" + index;
        obj.setRightsHolder(rightsHolder);
    }
    
    public void setUnit(MeasurementOrFact obj, int index) {
        String unit = "unit_" + index;
        obj.setUnit(unit);
    }
    
    public void setValue(MeasurementOrFact obj, int index) {
        String value = "value_" + index;
        obj.setValue(value);
    }
    
    public MeasurementOrFact getSpecificMeasurementOrFact(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        MeasurementOrFact obj = data.get(index);
        Long id = obj.getId();
        return measurementOrFactRepository.findOne(id);
    }
    
    public MeasurementOrFact getRandomMeasurementOrFact() {
        init();
        MeasurementOrFact obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return measurementOrFactRepository.findOne(id);
    }
    
    public boolean modifyMeasurementOrFact(MeasurementOrFact obj) {
        return false;
    }
    
    public void init() {
        int from = 0;
        int to = 10;
        data = measurementOrFactRepository.findAll(new org.springframework.data.domain.PageRequest(from / to, to)).getContent();
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'MeasurementOrFact' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<MeasurementOrFact>();
        for (int i = 0; i < 10; i++) {
            MeasurementOrFact obj = getNewTransientMeasurementOrFact(i);
            try {
                measurementOrFactRepository.save(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            measurementOrFactRepository.flush();
            data.add(obj);
        }
    }
	
	public void setTaxon(MeasurementOrFact obj, int index) {
        Taxon taxon = taxonDataOnDemand.getRandomTaxon();
        
        obj.setTaxon(taxon);
    }
	
    public void tearDown() {
    	measurementOrFactRepository.delete(measurementOrFactRepository.findAll());
    	data = new ArrayList<MeasurementOrFact>();
    	taxonDataOnDemand.tearDown();
    }
}
