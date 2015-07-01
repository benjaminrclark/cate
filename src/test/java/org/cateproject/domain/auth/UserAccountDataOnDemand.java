package org.cateproject.domain.auth;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.cateproject.domain.auth.UserAccount;
import org.cateproject.repository.jpa.auth.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class UserAccountDataOnDemand {
    
    private Random rnd = new SecureRandom();
    
    private List<UserAccount> data;
    
    @Autowired
    UserAccountRepository userAccountRepository;
    
    public UserAccount getNewTransientUserAccount(int index) {
        UserAccount obj = new UserAccount();
        setAccountNonExpired(obj, index);
        setAccountNonLocked(obj, index);
        setCredentialsNonExpired(obj, index);
        setEmail(obj, index);
        setEnabled(obj, index);
        setFirstName(obj, index);
        setLastName(obj, index);
        setPassword(obj, index);
        setPasswordEncoder(obj, index);
        setTenant(obj, index);
        setUsername(obj, index);
        return obj;
    }
    
	public void setPasswordEncoder(UserAccount obj, int index) {
		// NO-OP
    }
    
    public void setAccountNonExpired(UserAccount obj, int index) {
        Boolean accountNonExpired = true;
        obj.setAccountNonExpired(accountNonExpired);
    }
    
    public void setAccountNonLocked(UserAccount obj, int index) {
        Boolean accountNonLocked = true;
        obj.setAccountNonLocked(accountNonLocked);
    }
    
    public void setCredentialsNonExpired(UserAccount obj, int index) {
        Boolean credentialsNonExpired = true;
        obj.setCredentialsNonExpired(credentialsNonExpired);
    }
    
    public void setEmail(UserAccount obj, int index) {
        String email = "foo" + index + "@bar.com";
        obj.setEmail(email);
    }
    
    public void setEnabled(UserAccount obj, int index) {
        Boolean enabled = true;
        obj.setEnabled(enabled);
    }
    
    public void setFirstName(UserAccount obj, int index) {
        String firstName = "firstName_" + index;
        obj.setFirstName(firstName);
    }
    
    public void setLastName(UserAccount obj, int index) {
        String lastName = "lastName_" + index;
        obj.setLastName(lastName);
    }
    
    public void setPassword(UserAccount obj, int index) {
        String password = "password_" + index;
        obj.setPassword(password);
    }
    
    public void setTenant(UserAccount obj, int index) {
        String tenant = "tenant_" + index;
        obj.setTenant(tenant);
    }
    
    public void setUsername(UserAccount obj, int index) {
        String username = "username_" + index;
        obj.setUsername(username);
    }
    
    public UserAccount getSpecificUserAccount(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        UserAccount obj = data.get(index);
        Long id = obj.getId();
        return userAccountRepository.findOne(id);
    }
    
    public UserAccount getRandomUserAccount() {
        init();
        UserAccount obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return userAccountRepository.findOne(id);
    }
    
    public boolean modifyUserAccount(UserAccount obj) {
        return false;
    }
    
    public void init() {
        int from = 0;
        int to = 10;
        data = userAccountRepository.findAll(new org.springframework.data.domain.PageRequest(from / to, to)).getContent();
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'UserAccount' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<UserAccount>();
        for (int i = 0; i < 10; i++) {
            UserAccount obj = getNewTransientUserAccount(i);
            try {
                userAccountRepository.save(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            userAccountRepository.flush();
            data.add(obj);
        }
    }
    
}
