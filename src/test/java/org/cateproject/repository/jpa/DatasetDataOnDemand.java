package org.cateproject.repository.jpa;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.cateproject.domain.Dataset;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class DatasetDataOnDemand {
    
    private Random rnd = new SecureRandom();
    
    private List<Dataset> data;
    
    @Autowired
    DatasetRepository datasetRepository;
    
    public Dataset getNewTransientDataset(int index) {
        Dataset obj = new Dataset();
        setAccessRights(obj, index);
        setContributor(obj, index);
        setCreated(obj, index);
        setCreator(obj, index);
        setDataset(obj, index);
        setDescription(obj, index);
        setIdentifier(obj, index);
        setLicense(obj, index);
        setModified(obj, index);
        setRights(obj, index);
        setRightsHolder(obj, index);
        setTitle(obj, index);
        return obj;
    }
    
    public void setAccessRights(Dataset obj, int index) {
        String accessRights = "accessRights_" + index;
        obj.setAccessRights(accessRights);
    }
    
    public void setContributor(Dataset obj, int index) {
        String contributor = "contributor_" + index;
        obj.setContributor(contributor);
    }
    
    public void setCreated(Dataset obj, int index) {
        DateTime created = null;
        obj.setCreated(created);
    }
    
    public void setCreator(Dataset obj, int index) {
        String creator = "creator_" + index;
        obj.setCreator(creator);
    }
    
    public void setDataset(Dataset obj, int index) {
        Dataset dataset = obj;
        obj.setDataset(dataset);
    }
    
    public void setDescription(Dataset obj, int index) {
        String description = "description_" + index;
        obj.setDescription(description);
    }
    
    public void setIdentifier(Dataset obj, int index) {
        String identifier = "identifier_" + index;
        obj.setIdentifier(identifier);
    }
    
    public void setLicense(Dataset obj, int index) {
        String license = "license_" + index;
        obj.setLicense(license);
    }
    
    public void setModified(Dataset obj, int index) {
        DateTime modified = null;
        obj.setModified(modified);
    }
    
    public void setRights(Dataset obj, int index) {
        String rights = "rights_" + index;
        obj.setRights(rights);
    }
    
    public void setRightsHolder(Dataset obj, int index) {
        String rightsHolder = "rightsHolder_" + index;
        obj.setRightsHolder(rightsHolder);
    }
    
    public void setTitle(Dataset obj, int index) {
        String title = "title_" + index;
        obj.setTitle(title);
    }
    
    public Dataset getSpecificDataset(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Dataset obj = data.get(index);
        Long id = obj.getId();
        return datasetRepository.findOne(id);
    }
    
    public Dataset getRandomDataset() {
        init();
        Dataset obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return datasetRepository.findOne(id);
    }
    
    public boolean modifyDataset(Dataset obj) {
        return false;
    }
    
    public void init() {
        int from = 0;
        int to = 10;
        data = datasetRepository.findAll(new org.springframework.data.domain.PageRequest(from / to, to)).getContent();
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Dataset' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Dataset>();
        for (int i = 0; i < 10; i++) {
            Dataset obj = getNewTransientDataset(i);
            try {
                datasetRepository.save(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            datasetRepository.flush();
            data.add(obj);
        }
    }
}
