package org.cateproject.web.multitenant;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cateproject.multitenant.MultitenantStatus;
import org.cateproject.multitenant.MultitenantContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class MultitenantFilter extends OncePerRequestFilter {
	
	private static Logger logger = LoggerFactory.getLogger(MultitenantFilter.class);
	
	@Autowired
	private MultitenantStatus status;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if(status.isInitialized()) {
		  if(request.getRequestURI().startsWith("/init")) {			  
			  response.sendError(HttpServletResponse.SC_NOT_FOUND);
		  } else {
			  String tenantId = getTenantId(request);
			  String originalTenantId = MultitenantContextHolder.getContext().getTenantId();
			  try {				
				MultitenantContextHolder.getContext().setTenantId(tenantId);
				filterChain.doFilter(request, response);
			  } finally {
				MultitenantContextHolder.getContext().setTenantId(originalTenantId);
			  }
		  }
		} else if(request.getRequestURI().startsWith("/init") || request.getRequestURI().startsWith("/static") || request.getRequestURI().startsWith("/webjars")|| request.getRequestURI().startsWith("/multitenant/event")) {
			filterChain.doFilter(request, response);
		} else {
			response.sendRedirect("/init");
		}
	}
	
	public String getTenantId(HttpServletRequest request) {
		String host = request.getHeader("Host");
		
        if(host.indexOf(":") != -1) {
			host = host.substring(0,host.indexOf(":"));
		}
		return host;
	}

	public void setMultitenantStatus(MultitenantStatus multitenantStatus) {
		this.status = multitenantStatus;
	}
}
