package org.cateproject.web.multitenant;

import static org.junit.Assert.*;

import org.cateproject.domain.auth.UserAccount;
import org.cateproject.multitenant.MultitenantContextHolder;
import org.cateproject.web.multitenant.MultitenantWebExpressionHandler;
import org.cateproject.web.multitenant.MultitenantWebExpressionHandler.TenantWebSecurityExpressionRoot;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.FilterInvocation;

public class MultitenantWebExpressionHandlerTest {
	
	private MultitenantWebExpressionHandler multitenantWebExpressionHandler;

	@Before
	public void setUp() {
		multitenantWebExpressionHandler = new MultitenantWebExpressionHandler();
	}
	
	@Test
	public void testIsTenantAllowedNullTenant() {
		multitenantWebExpressionHandler.setDefaultTenantIdentifier("DEFAULT_TENANT");
		MultitenantContextHolder.getContext().setTenantId("CURRENT_TENANT");
		UserAccount currentUser = new UserAccount();
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(currentUser, currentUser.getAuthorities());
		FilterInvocation filterInvocation = new FilterInvocation(new MockHttpServletRequest(), new MockHttpServletResponse(), new MockFilterChain());
		
		TenantWebSecurityExpressionRoot securityExpressionRoot = (TenantWebSecurityExpressionRoot)multitenantWebExpressionHandler.createSecurityExpressionRoot(authentication, filterInvocation);
		assertFalse("isTenantAllowed should return false when the users tenant is null", securityExpressionRoot.isTenantAllowed());
	}

	@Test
	public void testIsTenantAllowedDifferentTenants() {
		multitenantWebExpressionHandler.setDefaultTenantIdentifier("DEFAULT_TENANT");
		MultitenantContextHolder.getContext().setTenantId("CURRENT_TENANT");
		UserAccount currentUser = new UserAccount();
		currentUser.setTenant("OTHER_TENANT");
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(currentUser, currentUser.getAuthorities());
		FilterInvocation filterInvocation = new FilterInvocation(new MockHttpServletRequest(), new MockHttpServletResponse(), new MockFilterChain());
		
		TenantWebSecurityExpressionRoot securityExpressionRoot = (TenantWebSecurityExpressionRoot)multitenantWebExpressionHandler.createSecurityExpressionRoot(authentication, filterInvocation);
		assertFalse("isTenantAllowed should return false when the users tenant is different", securityExpressionRoot.isTenantAllowed());
	}
	
	@Test
	public void testIsTenantAllowedLocalhost() {
		multitenantWebExpressionHandler.setDefaultTenantIdentifier("DEFAULT_TENANT");
		MultitenantContextHolder.getContext().setTenantId("localhost");
		UserAccount currentUser = new UserAccount();
		currentUser.setTenant("DEFAULT_TENANT");
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(currentUser, currentUser.getAuthorities());
		FilterInvocation filterInvocation = new FilterInvocation(new MockHttpServletRequest(), new MockHttpServletResponse(), new MockFilterChain());
		
		TenantWebSecurityExpressionRoot securityExpressionRoot = (TenantWebSecurityExpressionRoot)multitenantWebExpressionHandler.createSecurityExpressionRoot(authentication, filterInvocation);
		assertTrue("isTenantAllowed should compare the default tenant id to the users tenant id when the current host is 'localhost'", securityExpressionRoot.isTenantAllowed());
	}
	

}
