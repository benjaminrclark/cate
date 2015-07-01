package org.cateproject.multitenant.domain;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.cateproject.multitenant.MultitenantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@Configurable
public class MultitenantDataOnDemand {
	
	private static Logger logger = LoggerFactory.getLogger(MultitenantDataOnDemand.class);
	
    private Random rnd = new SecureRandom();
    
    private List<Multitenant> data;
    
    @Autowired
    private MultitenantRepository multitenantRepository;
    
    public Page<Multitenant> getNewTransientTenants(int number, int page, int size) {
    	Pageable pageable = new PageRequest(page, size);
    	List<Multitenant> tenants = new ArrayList<Multitenant>();
    	int start = page * size;
    	int end = Math.min(start + size, number);
    	for(int i = start; i < end; i++) {
    		tenants.add(getNewTransientTenant(i));
    	}
    	return new PageImpl<Multitenant>(tenants, pageable, number);
    }
    
    public Multitenant getNewTransientTenant(int index) {
        Multitenant obj = new Multitenant();
        setAdminEmail(obj, index);
        setAdminPassword(obj, index);
        setDatabasePassword(obj, index);
        setDatabaseUrl(obj, index);
        setDatabaseUsername(obj, index);
        setDriverClassName(obj, index);
        setHostname(obj, index);
        setIdentifier(obj, index);
        setTitle(obj, index);
        return obj;
    }

	private void setTitle(Multitenant obj, int index) {
		String title = "title_" + index;
        obj.setTitle(title);
	}

	private void setIdentifier(Multitenant obj, int index) {
		String identifier = "identifier_" + index;
        obj.setIdentifier(identifier);
	}

	private void setHostname(Multitenant obj, int index) {
		String hostname = "hostname_" + index;
        obj.setHostname(hostname);
	}

	private void setDriverClassName(Multitenant obj, int index) {
		String driverClassName = "driverClassName_" + index;
        obj.setDriverClassName(driverClassName);
	}

	private void setDatabaseUsername(Multitenant obj, int index) {
		String databaseUsername = "databaseUsername_" + index;
        obj.setDatabaseUsername(databaseUsername);
	}

	private void setDatabaseUrl(Multitenant obj, int index) {
		String databaseUrl = "databaseUrl_" + index;
        obj.setDatabaseUrl(databaseUrl);
	}

	private void setAdminPassword(Multitenant obj, int index) {
		String adminPassword = "adminPassword_" + index;
        obj.setAdminPassword(adminPassword);
	}
	
	private void setOwnerPassword(Multitenant obj, int index) {
		String ownerPassword = "ownerPassword_" + index;
        obj.setOwnerPassword(ownerPassword);
	}

	private void setDatabasePassword(Multitenant obj, int index) {
		String databasePassword = "databasePassword_" + index;
        obj.setDatabasePassword(databasePassword);
	}

	private void setAdminEmail(Multitenant obj, int index) {
		String adminEmail = "adminEmail_" + index;
        obj.setAdminEmail(adminEmail);
	}
	
	public Multitenant getSpecificTenant(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Multitenant obj = data.get(index);
        Long id = obj.getId();
        return multitenantRepository.findOne(id);
    }
    
    public Multitenant getRandomTenant() {
        init();
        Multitenant obj = data.get(rnd.nextInt(data.size()));
        return multitenantRepository.findOne(obj.getId());
    }
    
    public boolean modifyTenant(Multitenant obj) {
        return false;
    }
    
    public void init() {
        int from = 0;
        int to = 10;
        data = multitenantRepository.findAll(new org.springframework.data.domain.PageRequest(from / to, to)).getContent();
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Tenant' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Multitenant>();
        for (int i = 0; i < 10; i++) {
            Multitenant obj = getNewTransientTenant(i);
            try {
                multitenantRepository.save(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            multitenantRepository.flush();
            data.add(obj);
        }
    }

	public void tearDown() {
		for(Multitenant m : data) {
			multitenantRepository.delete(m);
		}
		
	}
}
