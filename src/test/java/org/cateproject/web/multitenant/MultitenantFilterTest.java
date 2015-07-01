package org.cateproject.web.multitenant;

import static org.junit.Assert.*;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cateproject.multitenant.MultitenantContextHolder;
import org.cateproject.multitenant.MultitenantStatus;
import org.cateproject.web.multitenant.MultitenantFilter;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class MultitenantFilterTest {

	public MultitenantFilter tenantFilter;
	
	public MultitenantStatus multitenantStatus;
	
	public FilterChain filterChain;
	
	@Before
	public void setUp() {
		tenantFilter = new MultitenantFilter();
		multitenantStatus = new MultitenantStatus();
		filterChain = EasyMock.createMock(FilterChain.class);
		tenantFilter.setMultitenantStatus(multitenantStatus);
	}
	
	@Test
	public void testGetTenantId() {
		MockHttpServletRequest hostWithoutPort = new MockHttpServletRequest();
		hostWithoutPort.addHeader("Host", "HOSTNAME");
		MockHttpServletRequest hostWithPort = new MockHttpServletRequest();
		hostWithPort.addHeader("Host", "HOSTNAME:PORT");
		assertEquals(tenantFilter.getTenantId(hostWithoutPort), "HOSTNAME");
		assertEquals(tenantFilter.getTenantId(hostWithPort), "HOSTNAME");
	}
	
	@Test
	public void testDoFilterInternalNotInitialized() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setContextPath("");
		filterChain.doFilter(EasyMock.eq(request), EasyMock.eq(response));
		filterChain.doFilter(EasyMock.eq(request), EasyMock.eq(response));
		filterChain.doFilter(EasyMock.eq(request), EasyMock.eq(response));
		EasyMock.replay(filterChain);
		request.setRequestURI("/init/with/path");
		tenantFilter.doFilter(request, response, filterChain);
		request.setRequestURI("/static/with/path");
		tenantFilter.doFilter(request, response, filterChain);
		request.setRequestURI("/webjars/with/path");
		tenantFilter.doFilter(request, response, filterChain);
		EasyMock.verify(filterChain);
	}
	
	@Test
	public void testDoFilterInternalNotInitializedRedirect() throws Exception {
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
	
		EasyMock.expect(request.getRequestURI()).andReturn("/other/path").anyTimes();
		response.sendRedirect(EasyMock.eq("/init"));
		
		EasyMock.replay(filterChain, request, response);
		tenantFilter.doFilterInternal(request, response, filterChain);
		EasyMock.verify(filterChain, request, response);
	}
	
	@Test
	public void testDoFilterInternalInitializedInitNotFound() throws Exception {
		multitenantStatus.setInitialized(true);
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setContextPath("");
		request.setRequestURI("/init/other/path");
	
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		response.sendError(EasyMock.eq(HttpServletResponse.SC_NOT_FOUND));
		
		EasyMock.replay(filterChain, response);
		tenantFilter.doFilterInternal(request, response, filterChain);
		EasyMock.verify(filterChain, response);
	}
	
	@Test
	public void testDoFilterInternalInitialized() throws Exception {
		multitenantStatus.setInitialized(true);
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Host", "HOSTNAME");
		request.setContextPath("");
		request.setRequestURI("/path/other/than/init");
		MockHttpServletResponse response = new MockHttpServletResponse();
		MultitenantContextHolder.getContext().setTenantId("ORIGINAL_TENANT");
	    filterChain.doFilter(EasyMock.eq(request), EasyMock.eq(response));
		
		EasyMock.replay(filterChain);
		tenantFilter.doFilterInternal(request, response, filterChain);
		EasyMock.verify(filterChain);
		assertEquals("Tenant id should be the original tenant id", "ORIGINAL_TENANT", MultitenantContextHolder.getContext().getTenantId());
	}

}
