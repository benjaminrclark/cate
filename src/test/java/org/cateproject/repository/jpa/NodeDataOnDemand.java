package org.cateproject.repository.jpa;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.cateproject.domain.Node;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class NodeDataOnDemand {
    
	private Random rnd = new SecureRandom();
    
    private List<Node> data;
    
    @Autowired
    DatasetDataOnDemand datasetDataOnDemand;
    
    @Autowired
    TermDataOnDemand termDataOnDemand;
    
    @Autowired
    NodeRepository nodeRepository;
    
    public Node getNewTransientNode(int index) {
        Node obj = new Node();
        setAccessRights(obj, index);
        setContributor(obj, index);
        setCreated(obj, index);
        setCreator(obj, index);
        setIdentifier(obj, index);
        setLicense(obj, index);
        setModified(obj, index);
        setParent(obj, index);
        setRights(obj, index);
        setRightsHolder(obj, index);
        return obj;
    }
    
    public void setAccessRights(Node obj, int index) {
        String accessRights = "accessRights_" + index;
        obj.setAccessRights(accessRights);
    }
    
    public void setContributor(Node obj, int index) {
        String contributor = "contributor_" + index;
        obj.setContributor(contributor);
    }
    
    public void setCreated(Node obj, int index) {
        DateTime created = null;
        obj.setCreated(created);
    }
    
    public void setCreator(Node obj, int index) {
        String creator = "creator_" + index;
        obj.setCreator(creator);
    }
    
    public void setIdentifier(Node obj, int index) {
        String identifier = "identifier_" + index;
        obj.setIdentifier(identifier);
    }
    
    public void setLicense(Node obj, int index) {
        String license = "license_" + index;
        obj.setLicense(license);
    }
    
    public void setModified(Node obj, int index) {
        DateTime modified = null;
        obj.setModified(modified);
    }
    
    public void setParent(Node obj, int index) {
        Node parent = obj;
        obj.setParent(parent);
    }
    
    public void setRights(Node obj, int index) {
        String rights = "rights_" + index;
        obj.setRights(rights);
    }
    
    public void setRightsHolder(Node obj, int index) {
        String rightsHolder = "rightsHolder_" + index;
        obj.setRightsHolder(rightsHolder);
    }
    
    public Node getSpecificNode(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Node obj = data.get(index);
        Long id = obj.getId();
        return nodeRepository.findOne(id);
    }
    
    public Node getRandomNode() {
        init();
        Node obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return nodeRepository.findOne(id);
    }
    
    public boolean modifyNode(Node obj) {
        return false;
    }
    
    public void init() {
        int from = 0;
        int to = 10;
        data = nodeRepository.findAll(new org.springframework.data.domain.PageRequest(from / to, to)).getContent();
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Node' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Node>();
        for (int i = 0; i < 10; i++) {
            Node obj = getNewTransientNode(i);
            try {
                nodeRepository.save(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            nodeRepository.flush();
            data.add(obj);
        }
    }
}
