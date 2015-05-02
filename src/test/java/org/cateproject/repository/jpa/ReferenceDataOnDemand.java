package org.cateproject.repository.jpa;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.cateproject.domain.Reference;
import org.cateproject.domain.constants.ReferenceType;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class ReferenceDataOnDemand {
    
    private Random rnd = new SecureRandom();
    
    private List<Reference> data;
    
    @Autowired
    DatasetDataOnDemand datasetDataOnDemand;
    
    @Autowired
    ReferenceRepository referenceRepository;
    
    public Reference getNewTransientReference(int index) {
        Reference obj = new Reference();
        setAccessRights(obj, index);
        setBibliographicCitation(obj, index);
        setContributor(obj, index);
        setCreated(obj, index);
        setCreator(obj, index);
        setDate(obj, index);
        setDescription(obj, index);
        setIdentifier(obj, index);
        setLanguage(obj, index);
        setLicense(obj, index);
        setModified(obj, index);
        setRights(obj, index);
        setRightsHolder(obj, index);
        setSource(obj, index);
        setSubject(obj, index);
        setTaxonRemarks(obj, index);
        setTitle(obj, index);
        setType(obj, index);
        return obj;
    }
    
    public void setAccessRights(Reference obj, int index) {
        String accessRights = "accessRights_" + index;
        obj.setAccessRights(accessRights);
    }
    
    public void setBibliographicCitation(Reference obj, int index) {
        String bibliographicCitation = "bibliographicCitation_" + index;
        obj.setBibliographicCitation(bibliographicCitation);
    }
    
    public void setContributor(Reference obj, int index) {
        String contributor = "contributor_" + index;
        obj.setContributor(contributor);
    }
    
    public void setCreated(Reference obj, int index) {
        DateTime created = null;
        obj.setCreated(created);
    }
    
    public void setCreator(Reference obj, int index) {
        String creator = "creator_" + index;
        obj.setCreator(creator);
    }
    
    public void setDate(Reference obj, int index) {
        String date = "date_" + index;
        obj.setDate(date);
    }
    
    public void setDescription(Reference obj, int index) {
        String description = "description_" + index;
        obj.setDescription(description);
    }
    
    public void setIdentifier(Reference obj, int index) {
        String identifier = "identifier_" + index;
        obj.setIdentifier(identifier);
    }
    
    public void setLanguage(Reference obj, int index) {
        Locale language = Locale.getDefault();
        obj.setLanguage(language);
    }
    
    public void setLicense(Reference obj, int index) {
        String license = "license_" + index;
        obj.setLicense(license);
    }
    
    public void setModified(Reference obj, int index) {
        DateTime modified = null;
        obj.setModified(modified);
    }
    
    public void setRights(Reference obj, int index) {
        String rights = "rights_" + index;
        obj.setRights(rights);
    }
    
    public void setRightsHolder(Reference obj, int index) {
        String rightsHolder = "rightsHolder_" + index;
        obj.setRightsHolder(rightsHolder);
    }
    
    public void setSource(Reference obj, int index) {
        String source = "source_" + index;
        obj.setSource(source);
    }
    
    public void setSubject(Reference obj, int index) {
        String subject = "subject_" + index;
        obj.setSubject(subject);
    }
    
    public void setTaxonRemarks(Reference obj, int index) {
        String taxonRemarks = "taxonRemarks_" + index;
        obj.setTaxonRemarks(taxonRemarks);
    }
    
    public void setTitle(Reference obj, int index) {
        String title = "title_" + index;
        obj.setTitle(title);
    }
    
    public void setType(Reference obj, int index) {
        ReferenceType type = ReferenceType.class.getEnumConstants()[0];
        obj.setType(type);
    }
    
    public Reference getSpecificReference(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Reference obj = data.get(index);
        Long id = obj.getId();
        return referenceRepository.findOne(id);
    }
    
    public Reference getRandomReference() {
        init();
        Reference obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return referenceRepository.findOne(id);
    }
    
    public boolean modifyReference(Reference obj) {
        return false;
    }
    
    public void init() {
        int from = 0;
        int to = 10;
        data = referenceRepository.findAll(new org.springframework.data.domain.PageRequest(from / to, to)).getContent();
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Reference' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Reference>();
        for (int i = 0; i < 10; i++) {
            Reference obj = getNewTransientReference(i);
            try {
                referenceRepository.save(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            referenceRepository.flush();
            data.add(obj);
        }
    }
}
