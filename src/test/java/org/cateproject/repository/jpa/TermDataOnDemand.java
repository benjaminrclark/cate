package org.cateproject.repository.jpa;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.cateproject.domain.Term;
import org.cateproject.domain.constants.CharacterType;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class TermDataOnDemand {
private Random rnd = new SecureRandom();
    
    private List<Term> data;
    
    @Autowired
    DatasetDataOnDemand datasetDataOnDemand;
    
    @Autowired
    TermRepository termRepository;
    
    public Term getNewTransientTerm(int index) {
        Term obj = new Term();
        setAccessRights(obj, index);
        setCharacter(obj, index);
        setContributor(obj, index);
        setCreated(obj, index);
        setCreator(obj, index);
        setDescription(obj, index);
        setIdentifier(obj, index);
        setLicense(obj, index);
        setModified(obj, index);
        setOrder(obj, index);
        setRights(obj, index);
        setRightsHolder(obj, index);
        setTitle(obj, index);
        setType(obj, index);
        setUnit(obj, index);
        return obj;
    }
    
    public void setAccessRights(Term obj, int index) {
        String accessRights = "accessRights_" + index;
        obj.setAccessRights(accessRights);
    }
    
    public void setCharacter(Term obj, int index) {
        Term character = obj;
        obj.setCharacter(character);
    }
    
    public void setContributor(Term obj, int index) {
        String contributor = "contributor_" + index;
        obj.setContributor(contributor);
    }
    
    public void setCreated(Term obj, int index) {
        DateTime created = null;
        obj.setCreated(created);
    }
    
    public void setCreator(Term obj, int index) {
        String creator = "creator_" + index;
        obj.setCreator(creator);
    }
    
    public void setDescription(Term obj, int index) {
        String description = "description_" + index;
        obj.setDescription(description);
    }
    
    public void setIdentifier(Term obj, int index) {
        String identifier = "identifier_" + index;
        obj.setIdentifier(identifier);
    }
    
    public void setLicense(Term obj, int index) {
        String license = "license_" + index;
        obj.setLicense(license);
    }
    
    public void setModified(Term obj, int index) {
        DateTime modified = null;
        obj.setModified(modified);
    }
    
    public void setOrder(Term obj, int index) {
        Integer order = new Integer(index);
        obj.setOrder(order);
    }
    
    public void setRights(Term obj, int index) {
        String rights = "rights_" + index;
        obj.setRights(rights);
    }
    
    public void setRightsHolder(Term obj, int index) {
        String rightsHolder = "rightsHolder_" + index;
        obj.setRightsHolder(rightsHolder);
    }
    
    public void setTitle(Term obj, int index) {
        String title = "title_" + index;
        obj.setTitle(title);
    }
    
    public void setType(Term obj, int index) {
        CharacterType type = CharacterType.class.getEnumConstants()[0];
        obj.setType(type);
    }
    
    public void setUnit(Term obj, int index) {
        String unit = "unit_" + index;
        obj.setUnit(unit);
    }
    
    public Term getSpecificTerm(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Term obj = data.get(index);
        Long id = obj.getId();
        return termRepository.findOne(id);
    }
    
    public Term getRandomTerm() {
        init();
        Term obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return termRepository.findOne(id);
    }
    
    public boolean modifyTerm(Term obj) {
        return false;
    }
    
    public void init() {
        int from = 0;
        int to = 10;
        data = termRepository.findAll(new org.springframework.data.domain.PageRequest(from / to, to)).getContent();
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Term' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Term>();
        for (int i = 0; i < 10; i++) {
            Term obj = getNewTransientTerm(i);
            try {
                termRepository.save(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            termRepository.flush();
            data.add(obj);
        }
    }
}
