package org.cateproject.repository.jpa;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.cateproject.domain.IdentificationKey;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class IdentificationKeyDataOnDemand {
	    
	    private Random rnd = new SecureRandom();
	    
	    private List<IdentificationKey> data;
	    
	    @Autowired
	    DatasetDataOnDemand datasetDataOnDemand;
	    
	    @Autowired
	    IdentificationKeyRepository identificationKeyRepository;
	    
	    public IdentificationKey getNewTransientIdentificationKey(int index) {
	        IdentificationKey obj = new IdentificationKey();
	        setAccessRights(obj, index);
	        setCompiledKey(obj, index);
	        setContributor(obj, index);
	        setCreated(obj, index);
	        setCreator(obj, index);
	        setDescription(obj, index);
	        setIdentifier(obj, index);
	        setLicense(obj, index);
	        setModified(obj, index);
	        setRights(obj, index);
	        setRightsHolder(obj, index);
	        setTitle(obj, index);
	        return obj;
	    }
	    
	    public void setAccessRights(IdentificationKey obj, int index) {
	        String accessRights = "accessRights_" + index;
	        obj.setAccessRights(accessRights);
	    }
	    
	    public void setCompiledKey(IdentificationKey obj, int index) {
	        String compiledKey = "compiledKey_" + index;
	        obj.setCompiledKey(compiledKey);
	    }
	    
	    public void setContributor(IdentificationKey obj, int index) {
	        String contributor = "contributor_" + index;
	        obj.setContributor(contributor);
	    }
	    
	    public void setCreated(IdentificationKey obj, int index) {
	        DateTime created = null;
	        obj.setCreated(created);
	    }
	    
	    public void setCreator(IdentificationKey obj, int index) {
	        String creator = "creator_" + index;
	        obj.setCreator(creator);
	    }
	    
	    public void setDescription(IdentificationKey obj, int index) {
	        String description = "description_" + index;
	        obj.setDescription(description);
	    }
	    
	    public void setIdentifier(IdentificationKey obj, int index) {
	        String identifier = "identifier_" + index;
	        obj.setIdentifier(identifier);
	    }
	    
	    public void setLicense(IdentificationKey obj, int index) {
	        String license = "license_" + index;
	        obj.setLicense(license);
	    }
	    
	    public void setModified(IdentificationKey obj, int index) {
	        DateTime modified = null;
	        obj.setModified(modified);
	    }
	    
	    public void setRights(IdentificationKey obj, int index) {
	        String rights = "rights_" + index;
	        obj.setRights(rights);
	    }
	    
	    public void setRightsHolder(IdentificationKey obj, int index) {
	        String rightsHolder = "rightsHolder_" + index;
	        obj.setRightsHolder(rightsHolder);
	    }
	    
	    public void setTitle(IdentificationKey obj, int index) {
	        String title = "title_" + index;
	        obj.setTitle(title);
	    }
	    
	    public IdentificationKey getSpecificIdentificationKey(int index) {
	        init();
	        if (index < 0) {
	            index = 0;
	        }
	        if (index > (data.size() - 1)) {
	            index = data.size() - 1;
	        }
	        IdentificationKey obj = data.get(index);
	        Long id = obj.getId();
	        return identificationKeyRepository.findOne(id);
	    }
	    
	    public IdentificationKey getRandomIdentificationKey() {
	        init();
	        IdentificationKey obj = data.get(rnd.nextInt(data.size()));
	        Long id = obj.getId();
	        return identificationKeyRepository.findOne(id);
	    }
	    
	    public boolean modifyIdentificationKey(IdentificationKey obj) {
	        return false;
	    }
	    
	    public void init() {
	        int from = 0;
	        int to = 10;
	        data = identificationKeyRepository.findAll(new org.springframework.data.domain.PageRequest(from / to, to)).getContent();
	        if (data == null) {
	            throw new IllegalStateException("Find entries implementation for 'IdentificationKey' illegally returned null");
	        }
	        if (!data.isEmpty()) {
	            return;
	        }
	        
	        data = new ArrayList<IdentificationKey>();
	        for (int i = 0; i < 10; i++) {
	            IdentificationKey obj = getNewTransientIdentificationKey(i);
	            try {
	                identificationKeyRepository.save(obj);
	            } catch (final ConstraintViolationException e) {
	                final StringBuilder msg = new StringBuilder();
	                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
	                    final ConstraintViolation<?> cv = iter.next();
	                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
	                }
	                throw new IllegalStateException(msg.toString(), e);
	            }
	            identificationKeyRepository.flush();
	            data.add(obj);
	        }
	    }

}
