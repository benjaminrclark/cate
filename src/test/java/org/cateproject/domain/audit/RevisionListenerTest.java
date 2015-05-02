package org.cateproject.domain.audit;

import static org.junit.Assert.*;

import org.cateproject.domain.auth.UserAccount;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class RevisionListenerTest {
	
	private RevisionListener revisionListener;

	@Before
	public void setUp() throws Exception {
		revisionListener = new RevisionListener();
		SecurityContextHolder.clearContext();
	}

	@Test
	public void testNewRevisionWithoutAuthentication() {
		RevisionInfo revisionInfo = new RevisionInfo();
		revisionListener.newRevision(revisionInfo);
		assertNull("userName should be null", revisionInfo.getUserName());
	}
	
	@Test
	public void testNewRevisionWithAuthentication() {
		RevisionInfo revisionInfo = new RevisionInfo();
		UserAccount userAccount = new UserAccount();
		userAccount.setUsername("USER_NAME");
        Authentication authentication = new UsernamePasswordAuthenticationToken(userAccount,null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
		revisionListener.newRevision(revisionInfo);
		assertEquals("userName should equal 'USER_NAME'", "USER_NAME", revisionInfo.getUserName());
	}

}
