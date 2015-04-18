package org.cateproject.web.multitenant.theme;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.cateproject.domain.admin.Tenant;
import org.cateproject.multitenant.MultitenantContextHolder;
import org.cateproject.multitenant.MultitenantProperties;
import org.cateproject.multitenant.MultitenantRepository;
import org.cateproject.multitenant.domain.Multitenant;
import org.cateproject.multitenant.event.MultitenantEvent;
import org.cateproject.multitenant.event.MultitenantEventType;
import org.cateproject.repository.jpa.admin.TenantRepository;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.ui.context.Theme;
import org.springframework.ui.context.support.SimpleTheme;

public class MultitenantThemeSourceTest {
	
	private MultitenantThemeSource multitenantThemeSource;
	
	private MultitenantRepository multitenantRepository;
	
	private TenantRepository tenantRepository;
	
	@Before
	public void setUp() {
		multitenantRepository = EasyMock.createMock(MultitenantRepository.class);
		tenantRepository = EasyMock.createMock(TenantRepository.class);
		
		multitenantThemeSource = new MultitenantThemeSource();
		Properties properties = new Properties();
		properties.setProperty("tenant.property.one", "TENANT_PROPERTY_ONE");
		properties.setProperty("tenant.property.two", "TENANT_PROPERTY_TWO");
		properties.setProperty("not.tenant.property", "NOT_TENANT_PROPERTY");
		MultitenantProperties multitenantProperties = new MultitenantProperties();
		multitenantProperties.setMultitenantProperties(properties);
		multitenantThemeSource.setMultitenantProperties(multitenantProperties);
		multitenantThemeSource.setMultitenantRepository(multitenantRepository);
		multitenantThemeSource.setTenantRepository(tenantRepository);
		multitenantThemeSource.setThemes(new HashMap<String,Theme>());
	}
	
	@Test
	public void testAfterPropertiesSet() throws Exception {
		List<Multitenant> multitenants = new ArrayList<Multitenant>();
		Multitenant multitenant = new Multitenant();
		multitenant.setIdentifier("OTHER_TENANT");
		multitenants.add(multitenant);	
		Tenant tenant = new Tenant();
		tenant.setIdentifier("OTHER_TENANT");
		multitenantThemeSource.setDefaultTenantIdentifier("DEFAULT_TENANT_IDENTIFIER");
		EasyMock.expect(multitenantRepository.findAll()).andReturn(multitenants);
        EasyMock.expect(tenantRepository.findOne(EasyMock.eq(1L))).andReturn(tenant);
		
		EasyMock.replay(multitenantRepository, tenantRepository);
		multitenantThemeSource.afterPropertiesSet();;
		assertTrue("afterPropertiesSet should create a the default theme",multitenantThemeSource.getThemes().containsKey("DEFAULT_TENANT_IDENTIFIER"));
		assertTrue("afterPropertiesSet should create a theme for each tenant",multitenantThemeSource.getThemes().containsKey("OTHER_TENANT"));
		Theme defaultTheme = multitenantThemeSource.getTheme("DEFAULT_TENANT_IDENTIFIER");
		assertNotNull("the defaultTheme should have property 'one'", defaultTheme.getMessageSource().getMessage("one", new Object[]{}, Locale.getDefault()));
		assertNotNull("the defaultTheme should have property 'two'", defaultTheme.getMessageSource().getMessage("two", new Object[]{}, Locale.getDefault()));
		Theme otherTheme = multitenantThemeSource.getTheme("OTHER_IDENTIFIER");
		
		EasyMock.verify(multitenantRepository, tenantRepository);
	}
	
	@Test
	public void testGetTheme() {
		multitenantThemeSource.setDefaultTenantIdentifier("DEFAULT_TENANT");
		Theme defaultTheme = new SimpleTheme("DEFAULT_TENANT", new StaticMessageSource());
		Theme otherTheme = new SimpleTheme("OTHER_TENANT", new StaticMessageSource());
		multitenantThemeSource.getThemes().put("DEFAULT_TENANT", defaultTheme);
		multitenantThemeSource.getThemes().put("OTHER_TENANT", otherTheme);
		
		assertEquals("null theme should return the default theme", multitenantThemeSource.getTheme(null), defaultTheme);
		assertEquals("themes which are not present should return the default theme", multitenantThemeSource.getTheme("THEME_DOES_NOT_EXIST"), defaultTheme);
		assertEquals("themes which are present should return the theme", multitenantThemeSource.getTheme("OTHER_TENANT"), otherTheme);
	}

	@Test
	public void testNotifyMultitenantEventDefault() {
		MultitenantEvent multitenantEvent = new MultitenantEvent();
		multitenantEvent.setIdentifier("TENANT_IDENTIFIER");
		multitenantEvent.setType(MultitenantEventType.DELETE);
		
		EasyMock.replay(multitenantRepository, tenantRepository);
		multitenantThemeSource.notify(multitenantEvent);
		EasyMock.verify(multitenantRepository, tenantRepository);
	}
	
	@Test
	public void testNotifyMultitenantEventDeleteTenantPresent() {
		MultitenantEvent multitenantEvent = new MultitenantEvent();
		multitenantEvent.setIdentifier("TENANT_IDENTIFIER");
		multitenantEvent.setType(MultitenantEventType.DELETE);
		multitenantThemeSource.getThemes().put("OTHER_TENANT", new SimpleTheme("OTHER_TENANT", new StaticMessageSource()));
		multitenantThemeSource.getThemes().put("TENANT_IDENTIFIER", new SimpleTheme("TENANT_IDENTIFIER", new StaticMessageSource()));
		
		EasyMock.replay(multitenantRepository, tenantRepository);
		multitenantThemeSource.notify(multitenantEvent);
		assertEquals("notify('DELETE') should not delete unrelated themes", 1, multitenantThemeSource.getThemes().size());
		assertFalse("notify('DELETE') should delete the related theme, if present",multitenantThemeSource.getThemes().containsKey("TENANT_IDENTIFIER"));
		EasyMock.verify(multitenantRepository, tenantRepository);
	}
	
	@Test
	public void testNotifyMultitenantEventDeleteTenantAbsent() {
		MultitenantEvent multitenantEvent = new MultitenantEvent();
		multitenantEvent.setIdentifier("TENANT_IDENTIFIER");
		multitenantEvent.setType(MultitenantEventType.OTHER);
		multitenantThemeSource.getThemes().put("OTHER_TENANT", new SimpleTheme("OTHER_TENANT", new StaticMessageSource()));
		
		EasyMock.replay(multitenantRepository, tenantRepository);
		multitenantThemeSource.notify(multitenantEvent);
		assertEquals("notify('DELETE') should not delete unrelated themes", 1, multitenantThemeSource.getThemes().size());
		EasyMock.verify(multitenantRepository, tenantRepository);
	}
	
	@Test
	public void testNotifyMultitenantEventCreate() {
		MultitenantEvent multitenantEvent = new MultitenantEvent();
		multitenantEvent.setIdentifier("TENANT_IDENTIFIER");
		multitenantEvent.setType(MultitenantEventType.CREATE);
		
		Tenant tenant = new Tenant();
		tenant.setIdentifier("OTHER_TENANT");
		EasyMock.expect(tenantRepository.findOne(EasyMock.eq(1L))).andReturn(tenant);
		
		EasyMock.replay(multitenantRepository, tenantRepository);
		multitenantThemeSource.notify(multitenantEvent);
		assertTrue("notify('CREATE') should create a theme based on the identifier of the tenant returned",multitenantThemeSource.getThemes().containsKey("OTHER_TENANT"));
		assertFalse("notify('CREATE') should not create a theme based on the identifier of the tenantEvent",multitenantThemeSource.getThemes().containsKey("TENANT_IDENTIFIER"));
		EasyMock.verify(multitenantRepository, tenantRepository);
	}

	@Test
	public void testHandleDefault() {
		MultitenantEvent multitenantEvent = new MultitenantEvent();
		multitenantEvent.setIdentifier("TENANT_IDENTIFIER");
		multitenantEvent.setType(MultitenantEventType.OTHER);
		
		EasyMock.replay(multitenantRepository, tenantRepository);
		multitenantThemeSource.handle(multitenantEvent);
		EasyMock.verify(multitenantRepository, tenantRepository);
	}
	
	@Test
	public void testHandleDelete() {
		MultitenantEvent multitenantEvent = new MultitenantEvent();
		multitenantEvent.setIdentifier("TENANT_IDENTIFIER");
		multitenantEvent.setType(MultitenantEventType.DELETE);
		
		EasyMock.replay(multitenantRepository, tenantRepository);
		multitenantThemeSource.handle(multitenantEvent);
		EasyMock.verify(multitenantRepository, tenantRepository);
	}
	
	@Test
	public void testHandleCreate() {
		MultitenantEvent multitenantEvent = new MultitenantEvent();
		multitenantEvent.setIdentifier("TENANT_IDENTIFIER");
		multitenantEvent.setType(MultitenantEventType.CREATE);
		MultitenantContextHolder.getContext().setTenantId("ORIGINAL_TENANT_ID");
		Tenant tenant = new Tenant();
		tenant.setTitle("TENANT_TITLE");
		tenant.setIdentifier("TENANT_IDENTIFIER");
		EasyMock.expect(tenantRepository.findOne(EasyMock.eq(1L))).andReturn(tenant);
		EasyMock.expect(tenantRepository.save(tenant)).andReturn(tenant);
		
		EasyMock.replay(multitenantRepository, tenantRepository);
		multitenantThemeSource.handle(multitenantEvent);
		assertEquals("The tenant context should be 'ORIGINAL_TENANT_ID'", "ORIGINAL_TENANT_ID", MultitenantContextHolder.getContext().getTenantId());
		assertEquals("The tenant should have a property 'tenant_name' set to its title", "TENANT_TITLE", tenant.getProperties().get("tenant_name"));
		EasyMock.verify(multitenantRepository, tenantRepository);
	}

	@Test
	public void testGetTenantProperties() throws IOException {
		Tenant tenant = new Tenant();
		Map<String, String> properties = new HashMap<String, String>();
		tenant.setProperties(properties);
		EasyMock.expect(tenantRepository.findOne(EasyMock.eq(1L))).andReturn(tenant);
		
		EasyMock.replay(multitenantRepository, tenantRepository);
		assertEquals("getTenantProperties should return properties as expected", properties, multitenantThemeSource.getTenantProperties());
		EasyMock.verify(multitenantRepository, tenantRepository);
	}

}
