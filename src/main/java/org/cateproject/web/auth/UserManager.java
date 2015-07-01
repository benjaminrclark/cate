package org.cateproject.web.auth;

import org.cateproject.domain.auth.UserAccount;
import org.cateproject.repository.jpa.auth.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


public class UserManager implements UserDetailsService {
	
	private UserCache userCache;
	
	private UserAccountRepository userAccountRepository;
	
	@Autowired(required = false)
    public void setUserCache(UserCache userCache) {
        Assert.notNull(userCache, "userCache cannot be null");
        this.userCache = userCache;
    }
	
	@Autowired
	public void setUserAccountRepository(UserAccountRepository userAccountRepository) {
		this.userAccountRepository = userAccountRepository;
	}
	
	public UserManager() {
        userCache = new NullUserCache();
    }

    @Transactional(value = "transactionManager", readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        try {
            Assert.hasText(username);
        } catch (IllegalArgumentException iae) {
            throw new UsernameNotFoundException(username, iae);
        }
        UserAccount user = userAccountRepository.findByUsername(username);
        userCache.putUserInCache(user);
        return user;
    }
}
