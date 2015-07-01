package org.cateproject.web.auth;

import static org.junit.Assert.*;

import org.cateproject.domain.auth.UserAccount;
import org.cateproject.repository.jpa.auth.UserAccountRepository;
import org.cateproject.web.auth.UserManager;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserManagerTest {
	
	private UserManager userManager;
	
	private UserCache userCache;
	
	private UserAccountRepository userAccountRepository;

	@Before
	public void setUp() throws Exception {
		userManager = new UserManager();
		
		userCache = EasyMock.createMock(UserCache.class);
		userAccountRepository = EasyMock.createMock(UserAccountRepository.class);
		
		userManager.setUserCache(userCache);
		userManager.setUserAccountRepository(userAccountRepository);
	}

	@Test
	public void testLoadUserByUsername() {
		UserAccount userAccount = new UserAccount();
		EasyMock.expect(userAccountRepository.findByUsername(EasyMock.eq("USERNAME"))).andReturn(userAccount);
		userCache.putUserInCache(EasyMock.eq(userAccount));
		EasyMock.replay(userCache,userAccountRepository);
		userManager.loadUserByUsername("USERNAME");
		EasyMock.verify(userCache,userAccountRepository);
	}
	
	@Test(expected = UsernameNotFoundException.class)
	public void testLoadUserByUsernameNoText() {
		userManager.loadUserByUsername("");
	}

}
