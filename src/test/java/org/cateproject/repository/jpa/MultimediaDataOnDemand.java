package org.cateproject.repository.jpa;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.cateproject.domain.Multimedia;
import org.cateproject.domain.constants.CharacterType;
import org.cateproject.domain.constants.DCMIType;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class MultimediaDataOnDemand {
		
    private Random rnd = new SecureRandom();
    
    private List<Multimedia> data;
    
    @Autowired
    DatasetDataOnDemand datasetDataOnDemand;
    
    @Autowired
    TermDataOnDemand termDataOnDemand;
    
    @Autowired
    MultimediaRepository multimediaRepository;
    
    public Multimedia getNewTransientMultimedia(int index) {
        Multimedia obj = new Multimedia();
        setAccessRights(obj, index);
        setAudience(obj, index);
        setContributor(obj, index);
        setCreated(obj, index);
        setCreator(obj, index);
        setDescription(obj, index);
        setFormat(obj, index);
        setHash(obj, index);
        setHeight(obj, index);
        setIdentifier(obj, index);
        setLatitude(obj, index);
        setLicense(obj, index);
        setLocalFileName(obj, index);
        setSpatial(obj, index);
        setLongitude(obj, index);
        setModified(obj, index);
        setPublisher(obj, index);
        setReferences(obj, index);
        setRights(obj, index);
        setRightsHolder(obj, index);
        setSize(obj, index);
        setTitle(obj, index);
        setType(obj, index);
        setWidth(obj, index);
        return obj;
    }
    
    public void setFormat(Multimedia obj, int index) {
        String format = "image/jpeg";
        obj.setFormat(format);
    }
    
    public void setType(Multimedia obj, int index) {
    	DCMIType type = DCMIType.class.getEnumConstants()[0];
        obj.setType(type);
    }
    
    public void setAccessRights(Multimedia obj, int index) {
        String accessRights = "accessRights_" + index;
        obj.setAccessRights(accessRights);
    }
    
    public void setAudience(Multimedia obj, int index) {
        String audience = "audience_" + index;
        obj.setAudience(audience);
    }
    
    public void setContributor(Multimedia obj, int index) {
        String contributor = "contributor_" + index;
        obj.setContributor(contributor);
    }
    
    public void setCreated(Multimedia obj, int index) {
        DateTime created = null;
        obj.setCreated(created);
    }
    
    public void setCreator(Multimedia obj, int index) {
        String creator = "creator_" + index;
        obj.setCreator(creator);
    }
    
    public void setDescription(Multimedia obj, int index) {
        String description = "description_" + index;
        obj.setDescription(description);
    }
    
    public void setHash(Multimedia obj, int index) {
        String hash = "hash_" + index;
        obj.setHash(hash);
    }
    
    public void setHeight(Multimedia obj, int index) {
        Integer height = new Integer(index);
        obj.setHeight(height);
    }
    
    public void setIdentifier(Multimedia obj, int index) {
        String identifier = "identifier_" + index;
        obj.setIdentifier(identifier);
    }
    
    public void setLatitude(Multimedia obj, int index) {
        Double latitude = new Integer(index).doubleValue();
        obj.setLatitude(latitude);
    }
    
    public void setLicense(Multimedia obj, int index) {
        String license = "license_" + index;
        obj.setLicense(license);
    }
    
    public void setLocalFileName(Multimedia obj, int index) {
        String localFileName = "localFileName_" + index;
        obj.setLocalFileName(localFileName);
    }
    
    public void setSpatial(Multimedia obj, int index) {
        String spatial = "spatial_" + index;
        obj.setSpatial(spatial);
    }
    
    public void setLongitude(Multimedia obj, int index) {
        Double longitude = new Integer(index).doubleValue();
        obj.setLongitude(longitude);
    }
    
    public void setModified(Multimedia obj, int index) {
        DateTime modified = null;
        obj.setModified(modified);
    }
    
    public void setPublisher(Multimedia obj, int index) {
        String publisher = "publisher_" + index;
        obj.setPublisher(publisher);
    }
    
    public void setReferences(Multimedia obj, int index) {
        String references = "references_" + index;
        obj.setReferences(references);
    }
    
    public void setRights(Multimedia obj, int index) {
        String rights = "rights_" + index;
        obj.setRights(rights);
    }
    
    public void setRightsHolder(Multimedia obj, int index) {
        String rightsHolder = "rightsHolder_" + index;
        obj.setRightsHolder(rightsHolder);
    }
    
    public void setSize(Multimedia obj, int index) {
        Long size = new Integer(index).longValue();
        obj.setSize(size);
    }
    
    public void setTitle(Multimedia obj, int index) {
        String title = "title_" + index;
        obj.setTitle(title);
    }
    
    public void setWidth(Multimedia obj, int index) {
        Integer width = new Integer(index);
        obj.setWidth(width);
    }
    
    public Multimedia getSpecificMultimedia(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Multimedia obj = data.get(index);
        Long id = obj.getId();
        return multimediaRepository.findOne(id);
    }
    
    public Multimedia getRandomMultimedia() {
        init();
        Multimedia obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return multimediaRepository.findOne(id);
    }
    
    public boolean modifyMultimedia(Multimedia obj) {
        return false;
    }
    
    public void init() {
        int from = 0;
        int to = 10;
        data = multimediaRepository.findAll(new org.springframework.data.domain.PageRequest(from / to, to)).getContent();
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Multimedia' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Multimedia>();
        for (int i = 0; i < 10; i++) {
			Multimedia obj = getNewTransientMultimedia(i);
            try {
                multimediaRepository.save(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            multimediaRepository.flush();
            data.add(obj);
        }
    }
}
