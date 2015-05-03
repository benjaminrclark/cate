package org.cateproject.repository.jpa;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.cateproject.domain.Description;
import org.cateproject.domain.Taxon;
import org.cateproject.domain.constants.DescriptionType;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class DescriptionDataOnDemand {
	@Autowired
	private TaxonDataOnDemand taxonDataOnDemand;
	
	public void setTaxon(Description obj, int index) {
        Taxon taxon = taxonDataOnDemand.getRandomTaxon();
        obj.setTaxon(taxon);
    }
	    
    private Random rnd = new SecureRandom();
    
    private List<Description> data;
    
    @Autowired
    DatasetDataOnDemand datasetDataOnDemand;
    
    @Autowired
    DescriptionRepository descriptionRepository;
    
    public Description getNewTransientDescription(int index) {
        Description obj = new Description();
        setAccessRights(obj, index);
        setAudience(obj, index);
        setContributor(obj, index);
        setCreated(obj, index);
        setCreator(obj, index);
        setDescription(obj, index);
        setIdentifier(obj, index);
        setLanguage(obj, index);
        setLicense(obj, index);
        setModified(obj, index);
        setRights(obj, index);
        setRightsHolder(obj, index);
        setSource(obj, index);
        setTaxon(obj, index);
        setType(obj, index);
        return obj;
    }
    
    public void setAccessRights(Description obj, int index) {
        String accessRights = "accessRights_" + index;
        obj.setAccessRights(accessRights);
    }
    
    public void setAudience(Description obj, int index) {
        String audience = "audience_" + index;
        obj.setAudience(audience);
    }
    
    public void setContributor(Description obj, int index) {
        String contributor = "contributor_" + index;
        obj.setContributor(contributor);
    }
    
    public void setCreated(Description obj, int index) {
        DateTime created = null;
        obj.setCreated(created);
    }
    
    public void setCreator(Description obj, int index) {
        String creator = "creator_" + index;
        obj.setCreator(creator);
    }
    
    public void setDescription(Description obj, int index) {
        String description = "description_" + index;
        obj.setDescription(description);
    }
    
    public void setIdentifier(Description obj, int index) {
        String identifier = "identifier_" + index;
        obj.setIdentifier(identifier);
    }
    
    public void setLanguage(Description obj, int index) {
        Locale language = null;
        obj.setLanguage(language);
    }
    
    public void setLicense(Description obj, int index) {
        String license = "license_" + index;
        obj.setLicense(license);
    }
    
    public void setModified(Description obj, int index) {
        DateTime modified = null;
        obj.setModified(modified);
    }
    
    public void setRights(Description obj, int index) {
        String rights = "rights_" + index;
        obj.setRights(rights);
    }
    
    public void setRightsHolder(Description obj, int index) {
        String rightsHolder = "rightsHolder_" + index;
        obj.setRightsHolder(rightsHolder);
    }
    
    public void setSource(Description obj, int index) {
        String source = "source_" + index;
        obj.setSource(source);
    }
    
    public void setType(Description obj, int index) {
        DescriptionType type = DescriptionType.class.getEnumConstants()[0];
        obj.setType(type);
    }
    
    public Description getSpecificDescription(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Description obj = data.get(index);
        Long id = obj.getId();
        return descriptionRepository.findOne(id);
    }
    
    public Description getRandomDescription() {
        init();
        Description obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return descriptionRepository.findOne(id);
    }
    
    public boolean modifyDescription(Description obj) {
        return false;
    }
    
    public void init() {
        int from = 0;
        int to = 10;
        data = descriptionRepository.findAll(new org.springframework.data.domain.PageRequest(from / to, to)).getContent();
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Description' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Description>();
        for (int i = 0; i < 10; i++) {
            Description obj = getNewTransientDescription(i);
            try {
                descriptionRepository.save(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            descriptionRepository.flush();
            data.add(obj);
        }
    }
}
