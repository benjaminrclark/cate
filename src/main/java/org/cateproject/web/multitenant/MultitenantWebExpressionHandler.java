package org.cateproject.web.multitenant;

import org.cateproject.multitenant.MultitenantContextHolder;
import org.cateproject.multitenant.MultitenantAware;
import org.springframework.security.access.expression.AbstractSecurityExpressionHandler;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.WebSecurityExpressionRoot;

public class MultitenantWebExpressionHandler extends AbstractSecurityExpressionHandler<FilterInvocation> {

	private String defaultTenantIdentifier = "default";
	
	public void setDefaultTenantIdentifier(String defaultTenantIdentifier) {
		this.defaultTenantIdentifier = defaultTenantIdentifier;
	}
	
	@Override
	protected SecurityExpressionRoot createSecurityExpressionRoot(Authentication authentication,
			FilterInvocation invocation) {
		String currentTenant = MultitenantContextHolder.getContext().getTenantId();
		TenantWebSecurityExpressionRoot result = new TenantWebSecurityExpressionRoot(authentication, invocation,
				currentTenant, defaultTenantIdentifier);
		result.setPermissionEvaluator(getPermissionEvaluator());
		return result;
	}

	static class TenantWebSecurityExpressionRoot extends WebSecurityExpressionRoot {
		private final String currentTenantId;
		private final String defaultTenantId;

		public TenantWebSecurityExpressionRoot(Authentication a, FilterInvocation fi, String currentTenantId, String defaultTenantId) {
			super(a, fi);
			this.currentTenantId = currentTenantId;
			this.defaultTenantId = defaultTenantId;
		}
		
		public boolean isTenantAllowed() {
			MultitenantAware tenantAware = (MultitenantAware) getPrincipal();
			String usersTenantId = tenantAware.getTenant();
			if (usersTenantId == null) {
				return currentTenantId == null;
			} else if(currentTenantId.equals("localhost")) {
				return usersTenantId.equals(defaultTenantId);
			}
			return usersTenantId.equals(currentTenantId);
		}
	}
}
