package org.cateproject.domain.auth;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.cateproject.domain.auth.UserGroup;
import org.cateproject.repository.jpa.auth.UserGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class UserGroupDataOnDemand {
	
private Random rnd = new SecureRandom();
    
    private List<UserGroup> data;
    
    @Autowired
    UserGroupRepository userGroupRepository;
    
    public UserGroup getNewTransientUserGroup(int index) {
        UserGroup obj = new UserGroup();
        setName(obj, index);
        setTenant(obj, index);
        return obj;
    }
    
    public void setName(UserGroup obj, int index) {
        String name = "name_" + index;
        obj.setName(name);
    }
    
    public void setTenant(UserGroup obj, int index) {
        String tenant = "tenant_" + index;
        obj.setTenant(tenant);
    }
    
    public UserGroup getSpecificUserGroup(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        UserGroup obj = data.get(index);
        Long id = obj.getId();
        return userGroupRepository.findOne(id);
    }
    
    public UserGroup getRandomUserGroup() {
        init();
        UserGroup obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return userGroupRepository.findOne(id);
    }
    
    public boolean modifyUserGroup(UserGroup obj) {
        return false;
    }
    
    public void init() {
        int from = 0;
        int to = 10;
        data = userGroupRepository.findAll(new org.springframework.data.domain.PageRequest(from / to, to)).getContent();
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'UserGroup' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<UserGroup>();
        for (int i = 0; i < 10; i++) {
            UserGroup obj = getNewTransientUserGroup(i);
            try {
                userGroupRepository.save(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            userGroupRepository.flush();
            data.add(obj);
        }
    }

}
