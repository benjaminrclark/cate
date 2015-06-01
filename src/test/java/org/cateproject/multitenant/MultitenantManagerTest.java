package org.cateproject.multitenant;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.cateproject.domain.admin.Tenant;
import org.cateproject.domain.auth.Permission;
import org.cateproject.domain.auth.UserAccount;
import org.cateproject.multitenant.domain.Multitenant;
import org.cateproject.multitenant.event.MultitenantEvent;
import org.cateproject.multitenant.event.MultitenantEventAwareService;
import org.cateproject.repository.jpa.admin.TenantRepository;
import org.cateproject.repository.jpa.auth.UserAccountRepository;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class MultitenantManagerTest {
	
	private MultitenantManager tenantManager;
	
	private MultitenantEventAwareService remoteTenantEvents;
	
	private MultitenantEventAwareService localTenantEvents;

	private MultitenantRepository multitenantRepository;

	private TenantRepository tenantRepository;

	private UserAccountRepository userAccountRepository;
	
	private Multitenant multitenant;
		
	@Before
	public void setUp() {
		tenantManager = new MultitenantManager();
		remoteTenantEvents = EasyMock.createMock(MultitenantEventAwareService.class);
		localTenantEvents = EasyMock.createMock(MultitenantEventAwareService.class);
		multitenantRepository = EasyMock.createMock(MultitenantRepository.class);
		tenantRepository = EasyMock.createMock(TenantRepository.class);
		userAccountRepository = EasyMock.createMock(UserAccountRepository.class);
		
		tenantManager.setLocalTenantContext(localTenantEvents);
		tenantManager.setTenantEventContext(remoteTenantEvents);
		tenantManager.setMultitenantRepository(multitenantRepository);
		tenantManager.setTenantRepository(tenantRepository);
		tenantManager.setUserAccountRepository(userAccountRepository);
		MultitenantContextHolder.getContext().setTenantId("ORIGINAL_TENANT");
		
		multitenant = new Multitenant();
		multitenant.setIdentifier("IDENTIFIER");
		multitenant.setAdminEmail("ADMIN_EMAIL");
		multitenant.setOwnerEmail("OWNER_EMAIL");
		multitenant.setAdminPassword("ADMIN_PASSWORD");
		multitenant.setOwnerPassword("OWNER_PASSWORD");
		multitenant.setDatabasePassword("DATABASE_PASSWORD");
		multitenant.setDatabaseUrl("DATABASE_URL");
		multitenant.setDatabaseUsername("DATABASE_USERNAME");
		multitenant.setDriverClassName("DRIVER_CLASS_NAME");
		multitenant.setTitle("TITLE");
		multitenant.setHostname("HOSTNAME");
		
		UserAccount currentUser = new UserAccount();
		currentUser.setUsername("CURRENT_USER");
		currentUser.setEmail("CURRENT_EMAIL");
		currentUser.setPassword("CURRENT_PASSWORD");
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(currentUser, currentUser.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
	
	@Test
	public void testPostPersist() throws Exception {	
		localTenantEvents.handle(EasyMock.isA(MultitenantEvent.class));
		remoteTenantEvents.notify(EasyMock.isA(MultitenantEvent.class));
		
		EasyMock.replay(localTenantEvents, remoteTenantEvents, multitenantRepository, tenantRepository, userAccountRepository);
		
		tenantManager.postPersist(multitenant);
		
		EasyMock.verify(localTenantEvents, remoteTenantEvents, multitenantRepository, tenantRepository, userAccountRepository);
	}
	
	@Test
	public void testPostPersistHandlesNullTenantContext() throws Exception {		
		localTenantEvents.handle(EasyMock.isA(MultitenantEvent.class));
		tenantManager.setTenantEventContext(null);
		
		EasyMock.replay(localTenantEvents, remoteTenantEvents, multitenantRepository, tenantRepository, userAccountRepository);
		
		tenantManager.postPersist(multitenant);
		
		EasyMock.verify(localTenantEvents, remoteTenantEvents, multitenantRepository, tenantRepository, userAccountRepository);
	}
	
	@Test
	public void testCreateTenant() {		
		final Capture<Tenant> capturedTenant = Capture.newInstance();
		
		EasyMock.expect(tenantRepository.save(EasyMock.and(EasyMock.capture(capturedTenant), EasyMock.isA(Tenant.class)))).andAnswer(
				new IAnswer<Tenant>() {
					@Override
					public Tenant answer() throws Throwable {
						return capturedTenant.getValue();
					}					
				});
		EasyMock.replay(localTenantEvents, remoteTenantEvents, multitenantRepository, tenantRepository, userAccountRepository);
		
		tenantManager.createTenant(multitenant);
		
		EasyMock.verify(localTenantEvents, remoteTenantEvents, multitenantRepository, tenantRepository, userAccountRepository);
		assertEquals("TenantContext should set to the original tenant id", "ORIGINAL_TENANT", MultitenantContextHolder.getContext().getTenantId());
		assertEquals("Tenant should have the correct identifier", "IDENTIFIER", capturedTenant.getValue().getIdentifier());
		assertEquals("Tenant should have the correct title", "TITLE", capturedTenant.getValue().getTitle());
	}
	
	@Test
	public void testCreateAdminAccountsDefault() {		
		final Capture<UserAccount> capturedAdmin = Capture.newInstance();
		final Capture<UserAccount> capturedOwner = Capture.newInstance();
		
		EasyMock.expect(userAccountRepository.save(EasyMock.and(EasyMock.capture(capturedAdmin), EasyMock.isA(UserAccount.class)))).andAnswer(
				new IAnswer<UserAccount>() {
					@Override
					public UserAccount answer() throws Throwable {
						return capturedAdmin.getValue();
					}					
				});
		EasyMock.expect(userAccountRepository.save(EasyMock.and(EasyMock.capture(capturedOwner), EasyMock.isA(UserAccount.class)))).andAnswer(
				new IAnswer<UserAccount>() {
					@Override
					public UserAccount answer() throws Throwable {
						return capturedOwner.getValue();
					}					
				});
		EasyMock.replay(localTenantEvents, remoteTenantEvents, multitenantRepository, tenantRepository, userAccountRepository);
		
		tenantManager.createAdminAccounts(multitenant, true);
		
		EasyMock.verify(localTenantEvents, remoteTenantEvents, multitenantRepository, tenantRepository, userAccountRepository);
		assertEquals("TenantContext should set to the original tenant id", "ORIGINAL_TENANT", MultitenantContextHolder.getContext().getTenantId());
		
		assertEquals("Admin account should have the correct email address", capturedAdmin.getValue().getEmail(), "ADMIN_EMAIL");
		assertEquals("Admin account should have the correct username", capturedAdmin.getValue().getUsername(), "ADMIN_EMAIL");
		assertFalse("Admin account should have an encoded password", capturedAdmin.getValue().getPassword().equals("ADMIN_PASSWORD"));
		assertTrue("Admin account should be enabled", capturedAdmin.getValue().isEnabled());
		assertTrue("Admin account should not be expired", capturedAdmin.getValue().isAccountNonExpired());
		assertTrue("Admin account should not be locked", capturedAdmin.getValue().isAccountNonLocked());
		assertTrue("Admin credentials should not be expired", capturedAdmin.getValue().isCredentialsNonExpired());
		assertTrue("Admin should be able to administrate", capturedAdmin.getValue().getPermissions().contains(Permission.PERMISSION_ADMINISTRATE));
		assertTrue("Admin should be able to configure tenants", capturedAdmin.getValue().getPermissions().contains(Permission.PERMISSION_CONFIGURE_SYSTEM));
		assertTrue("Admin should be able to edit", capturedAdmin.getValue().getPermissions().contains(Permission.PERMISSION_EDIT));
		
		assertEquals("Owner account should have the correct email address", capturedOwner.getValue().getEmail(), "OWNER_EMAIL");
		assertEquals("Owner account should have the correct username", capturedOwner.getValue().getUsername(), "OWNER_EMAIL");
		assertFalse("Owner account should have an encoded password", capturedOwner.getValue().getPassword().equals("OWNER_PASSWORD"));
		assertTrue("Owner account should be enabled", capturedOwner.getValue().isEnabled());
		assertTrue("Owner account should not be expired", capturedOwner.getValue().isAccountNonExpired());
		assertTrue("Owner account should not be locked", capturedOwner.getValue().isAccountNonLocked());
		assertTrue("Owner credentials should not be expired", capturedOwner.getValue().isCredentialsNonExpired());
		assertTrue("Owner should be able to administrate", capturedOwner.getValue().getPermissions().contains(Permission.PERMISSION_ADMINISTRATE));
		assertFalse("Owner should not be able to configure tenants", capturedOwner.getValue().getPermissions().contains(Permission.PERMISSION_CONFIGURE_SYSTEM));
		assertTrue("Owner should be able to edit", capturedOwner.getValue().getPermissions().contains(Permission.PERMISSION_EDIT));
	}
	
	@Test
	public void testCreateAdminAccountsNonDefault() {		
		final Capture<UserAccount> capturedAdmin = Capture.newInstance();
		final Capture<UserAccount> capturedOwner = Capture.newInstance();
		
		EasyMock.expect(userAccountRepository.save(EasyMock.and(EasyMock.capture(capturedAdmin), EasyMock.isA(UserAccount.class)))).andAnswer(
				new IAnswer<UserAccount>() {
					@Override
					public UserAccount answer() throws Throwable {
						return capturedAdmin.getValue();
					}					
				});
		EasyMock.expect(userAccountRepository.save(EasyMock.and(EasyMock.capture(capturedOwner), EasyMock.isA(UserAccount.class)))).andAnswer(
				new IAnswer<UserAccount>() {
					@Override
					public UserAccount answer() throws Throwable {
						return capturedOwner.getValue();
					}					
				});
		EasyMock.replay(localTenantEvents, remoteTenantEvents, multitenantRepository, tenantRepository, userAccountRepository);
		
		tenantManager.createAdminAccounts(multitenant, false);
		
		EasyMock.verify(localTenantEvents, remoteTenantEvents, multitenantRepository, tenantRepository, userAccountRepository);
		assertEquals("TenantContext should set to the original tenant id", "ORIGINAL_TENANT", MultitenantContextHolder.getContext().getTenantId());
		
		assertEquals("Admin account should have the correct email address", capturedAdmin.getValue().getEmail(), "CURRENT_EMAIL");
		assertEquals("Admin account should have the correct username", capturedAdmin.getValue().getUsername(), "CURRENT_USER");
		assertEquals("Admin account should have the same password as the current user", capturedAdmin.getValue().getPassword(),"CURRENT_PASSWORD");
		assertTrue("Admin account should be enabled", capturedAdmin.getValue().isEnabled());
		assertTrue("Admin account should not be expired", capturedAdmin.getValue().isAccountNonExpired());
		assertTrue("Admin account should not be locked", capturedAdmin.getValue().isAccountNonLocked());
		assertTrue("Admin credentials should not be expired", capturedAdmin.getValue().isCredentialsNonExpired());
		assertTrue("Admin should be able to administrate", capturedAdmin.getValue().getPermissions().contains(Permission.PERMISSION_ADMINISTRATE));
		assertTrue("Admin should be able to configure tenants", capturedAdmin.getValue().getPermissions().contains(Permission.PERMISSION_CONFIGURE_SYSTEM));
		assertTrue("Admin should be able to edit", capturedAdmin.getValue().getPermissions().contains(Permission.PERMISSION_EDIT));
		
		assertEquals("Owner account should have the correct email address", capturedOwner.getValue().getEmail(), "OWNER_EMAIL");
		assertEquals("Owner account should have the correct username", capturedOwner.getValue().getUsername(), "OWNER_EMAIL");
		assertFalse("Owner account should have an encoded password", capturedOwner.getValue().getPassword().equals("OWNER_PASSWORD"));
		assertTrue("Owner account should be enabled", capturedOwner.getValue().isEnabled());
		assertTrue("Owner account should not be expired", capturedOwner.getValue().isAccountNonExpired());
		assertTrue("Owner account should not be locked", capturedOwner.getValue().isAccountNonLocked());
		assertTrue("Owner credentials should not be expired", capturedOwner.getValue().isCredentialsNonExpired());
		assertTrue("Owner should be able to administrate", capturedOwner.getValue().getPermissions().contains(Permission.PERMISSION_ADMINISTRATE));
		assertFalse("Owner should not be able to configure tenants", capturedOwner.getValue().getPermissions().contains(Permission.PERMISSION_CONFIGURE_SYSTEM));
		assertTrue("Owner should be able to edit", capturedOwner.getValue().getPermissions().contains(Permission.PERMISSION_EDIT));
	}
	
	@Test
	public void testSave() {
		
		final Capture<UserAccount> capturedAdmin = Capture.newInstance();
		final Capture<UserAccount> capturedOwner = Capture.newInstance();
        final Capture<Tenant> capturedTenant = Capture.newInstance();
		
        localTenantEvents.handle(EasyMock.isA(MultitenantEvent.class));
		remoteTenantEvents.notify(EasyMock.isA(MultitenantEvent.class));
        EasyMock.expect(multitenantRepository.save(EasyMock.eq(multitenant))).andReturn(multitenant);
		EasyMock.expect(tenantRepository.save(EasyMock.and(EasyMock.capture(capturedTenant), EasyMock.isA(Tenant.class)))).andAnswer(
				new IAnswer<Tenant>() {
					@Override
					public Tenant answer() throws Throwable {
						return capturedTenant.getValue();
					}					
				});
		EasyMock.expect(userAccountRepository.save(EasyMock.and(EasyMock.capture(capturedAdmin), EasyMock.isA(UserAccount.class)))).andAnswer(
				new IAnswer<UserAccount>() {
					@Override
					public UserAccount answer() throws Throwable {
						return capturedAdmin.getValue();
					}					
				});
		EasyMock.expect(userAccountRepository.save(EasyMock.and(EasyMock.capture(capturedOwner), EasyMock.isA(UserAccount.class)))).andAnswer(
				new IAnswer<UserAccount>() {
					@Override
					public UserAccount answer() throws Throwable {
						return capturedOwner.getValue();
					}					
				});
		EasyMock.replay(localTenantEvents, remoteTenantEvents, multitenantRepository, tenantRepository, userAccountRepository);
		
		tenantManager.save(multitenant, true);
		
		EasyMock.verify(localTenantEvents, remoteTenantEvents, multitenantRepository, tenantRepository, userAccountRepository);
	}
}
