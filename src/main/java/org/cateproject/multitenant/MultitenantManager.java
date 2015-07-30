package org.cateproject.multitenant;

import org.cateproject.domain.admin.Tenant;
import org.cateproject.domain.auth.Permission;
import org.cateproject.domain.auth.UserAccount;
import org.cateproject.multitenant.domain.Multitenant;
import org.cateproject.multitenant.event.MultitenantEvent;
import org.cateproject.multitenant.event.MultitenantEventAwareService;
import org.cateproject.multitenant.event.MultitenantEventType;
import org.cateproject.repository.jpa.admin.TenantRepository;
import org.cateproject.repository.jpa.auth.UserAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MultitenantManager {
	
	private static Logger logger = LoggerFactory.getLogger(MultitenantManager.class);
	
	@Autowired
	private MultitenantRepository multitenantRepository;
	
	@Autowired
	private TenantRepository tenantRepository;
	
	@Autowired
	private UserAccountRepository userAccountRepository;
	
	@Autowired(required = false)
	@Qualifier("remoteTenantContext")
	private MultitenantEventAwareService remoteTenantContext;
	
	@Autowired
	@Qualifier("localTenantContext")
	private MultitenantEventAwareService localTenantContext;

	
	public void setLocalTenantContext(MultitenantEventAwareService localTenantContext) {
		this.localTenantContext = localTenantContext;
	}
	
	public void setTenantEventContext(MultitenantEventAwareService remoteTenantContext) {
		this.remoteTenantContext = remoteTenantContext;
	}
	
	public void setMultitenantRepository(MultitenantRepository multitenantRepository) {
		this.multitenantRepository = multitenantRepository;
	}

	public void setTenantRepository(TenantRepository tenantRepository) {
		this.tenantRepository = tenantRepository;
	}

	public void setUserAccountRepository(UserAccountRepository userAccountRepository) {
		this.userAccountRepository = userAccountRepository;
	}

	public void save(Multitenant multitenant, boolean isDefault) {
		multitenantRepository.save(multitenant);
		createAdminAccounts(multitenant, isDefault);
        createTenant(multitenant);
		postPersist(multitenant);		
	}
	
	public void postPersist(Multitenant tenant) {
		MultitenantEvent tenantEvent = new MultitenantEvent(tenant.getIdentifier(), MultitenantEventType.CREATE);
		logger.info("Sending TenantEvent about " + tenant.getIdentifier());
		
		localTenantContext.handle(tenantEvent);
		
		try {
		    remoteTenantContext.notify(tenantEvent);
		} catch(NullPointerException npe) {
			logger.warn("tenantEventAware not set");
		}
	}
	
	public void preRemove(Multitenant tenant) {
		MultitenantEvent tenantEvent = new MultitenantEvent(tenant.getIdentifier(), MultitenantEventType.DELETE);
		logger.info("Sending TenantEvent about " + tenant.getIdentifier());
		
		localTenantContext.handle(tenantEvent);
		
		try {
		    remoteTenantContext.notify(tenantEvent);
		} catch(NullPointerException npe) {
			logger.warn("tenantEventAware not set");
		}
	}

	public void createAdminAccounts(Multitenant tenant, boolean isDefault) {
		String originalTenantId = MultitenantContextHolder.getContext().getTenantId();
		MultitenantContextHolder.getContext().setTenantId(tenant.getIdentifier());
		UserAccount admin = new UserAccount();
		admin.setTenant(tenant.getIdentifier());
		admin.setAccountNonExpired(true);
		admin.setAccountNonLocked(true);
		admin.setCredentialsNonExpired(true);
		admin.setEnabled(true);
		admin.getPermissions().add(Permission.ROLE_ADMINISTRATE);
		admin.getPermissions().add(Permission.ROLE_CONFIGURE_SYSTEM);
		admin.getPermissions().add(Permission.ROLE_EDIT);
		
		if(isDefault) {		  
		  admin.setEmail(tenant.getAdminEmail());
		  admin.setUsername(tenant.getAdminEmail());
		  admin.setPassword(tenant.getAdminPassword());
		  admin.encodePassword();
		} else {
		  UserAccount currentUserAccount = (UserAccount)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		  admin.setEmail(currentUserAccount.getEmail());
		  admin.setUsername(currentUserAccount.getUsername());
		  admin.setPassword(currentUserAccount.getPassword());
		}
		  
		userAccountRepository.save(admin);
		
		UserAccount owner = new UserAccount();
		owner.setEmail(tenant.getOwnerEmail());
		owner.setUsername(tenant.getOwnerEmail());
		owner.setPassword(tenant.getOwnerPassword());
		owner.encodePassword();
		owner.setTenant(tenant.getIdentifier());
		owner.setAccountNonExpired(true);
		owner.setAccountNonLocked(true);
		owner.setCredentialsNonExpired(true);
		owner.setEnabled(true);
		owner.getPermissions().add(Permission.ROLE_ADMINISTRATE);
		owner.getPermissions().add(Permission.ROLE_EDIT);
		userAccountRepository.save(owner);
		MultitenantContextHolder.getContext().setTenantId(originalTenantId);
	}

	public void createTenant(Multitenant multitenant) {
		String originalTenantId = MultitenantContextHolder.getContext().getTenantId();
		MultitenantContextHolder.getContext().setTenantId(multitenant.getIdentifier());
		Tenant tenant = new Tenant();
		tenant.setIdentifier(multitenant.getIdentifier());
		tenant.setTitle(multitenant.getTitle());
		tenantRepository.save(tenant);
		MultitenantContextHolder.getContext().setTenantId(originalTenantId);
		
	}

	public void delete(Multitenant multitenant) {
		preRemove(multitenant);
		multitenantRepository.delete(multitenant);
	}
}
