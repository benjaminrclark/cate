package org.cateproject.web.multitenant;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cateproject.multitenant.MultitenantContextHolder;
import org.cateproject.multitenant.MultitenantRepository;
import org.cateproject.multitenant.domain.Multitenant;
import org.cateproject.multitenant.event.MultitenantEvent;
import org.cateproject.multitenant.event.MultitenantEventType;
import org.cateproject.web.multitenant.MultitenantAwareResourceResolver;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.resource.ResourceResolverChain;

public class MultitenantAwareResourceResolverTest {
	
	private MultitenantAwareResourceResolver multitenantAwareResourceResolver;
	
	private ResourceResolverChain resourceResolverChain;
	
	private ApplicationContext applicationContext;
	
	private MultitenantRepository multitenantRepository;
	
	private List<Resource> locations;
	
	private Multitenant multitenant;
	
	private List<Multitenant> multitenants;
	
	private Map<String, List<Resource>> resourceMap;
	
	private Resource resource1;
    private Resource resource2;
	
	@Before
	public void setUp() {
		resource1 = new ClassPathResource("/org/cateproject/multitenant/web/");
		resource2 = new ClassPathResource("/bar/");
		
		resourceResolverChain = EasyMock.createMock(ResourceResolverChain.class);
		applicationContext = EasyMock.createMock(ApplicationContext.class);
		multitenantRepository = EasyMock.createMock(MultitenantRepository.class);
		locations = new ArrayList<Resource>();
		
		List<String> resourcePatterns =  new ArrayList<String>();
	    resourcePatterns.add("/directory/%{tenant}/directory");
	    resourcePatterns.add("/directory1/directory2");
	    
		multitenantAwareResourceResolver = new MultitenantAwareResourceResolver();
		multitenantAwareResourceResolver.setApplicationContext(applicationContext);
		multitenantAwareResourceResolver.setResourcePatterns(resourcePatterns);
		multitenantAwareResourceResolver.setMultitenantRepository(multitenantRepository);
		
		multitenant = new Multitenant();
	    multitenant.setIdentifier("TENANT_IDENTIFIER");
	    
	    Multitenant multitenant1 = new Multitenant();
	    multitenant1.setIdentifier("TENANT_IDENTIFIER_1");
	    
	    multitenants = new ArrayList<Multitenant>();
	    multitenants.add(multitenant);
	    multitenants.add(multitenant1);
	    MultitenantContextHolder.getContext().setTenantId("ORIGINAL_CONTEXT");
	    
	    resourceMap = new HashMap<String, List<Resource>>();
	    List<Resource> resources = new ArrayList<Resource>();
	    resources.add(resource1);
	    resources.add(resource2);
	    resourceMap.put("TENANT_IDENTIFIER", resources);
	}

	@Test
	public void testGetResources() {
		multitenantAwareResourceResolver.setResourceMap(resourceMap);
		multitenantAwareResourceResolver.setDefaultTenantIdentifier("TENANT_IDENTIFIER");
		
        EasyMock.replay(resourceResolverChain, applicationContext, multitenantRepository);
		MultitenantContextHolder.getContext().setTenantId("TENANT_IDENTIFIER");
		assertTrue("Multitenant aware resource resolver should contain resource 1 for tenant 'TENANT_IDENTIFIER'", multitenantAwareResourceResolver.getResources().contains(resource1));
		assertTrue("Multitenant aware resource resolver should contain resource 2 for tenant 'TENANT_IDENTIFIER'", multitenantAwareResourceResolver.getResources().contains(resource2));
		MultitenantContextHolder.getContext().setTenantId("TENANT_IDENTIFIER_1");
		assertTrue("Multitenant aware resource resolver should contain resource 1 for tenant 'TENANT_IDENTIFIER_1'", multitenantAwareResourceResolver.getResources().contains(resource1));
		assertTrue("Multitenant aware resource resolver should contain resource 2 for tenant 'TENANT_IDENTIFIER_1'", multitenantAwareResourceResolver.getResources().contains(resource2));
		
		EasyMock.verify(resourceResolverChain, applicationContext, multitenantRepository);
		
	}

	@Test
	public void testInit() {
		EasyMock.expect(multitenantRepository.findAll()).andReturn(multitenants);
		EasyMock.expect(applicationContext.getResource("/directory/TENANT_IDENTIFIER/directory")).andReturn(resource1);
	    EasyMock.expect(applicationContext.getResource("/directory1/directory2")).andReturn(resource2);
	    EasyMock.expect(applicationContext.getResource("/directory/TENANT_IDENTIFIER_1/directory")).andReturn(resource2);
	    EasyMock.expect(applicationContext.getResource("/directory1/directory2")).andReturn(resource1);
		
        EasyMock.replay(resourceResolverChain, applicationContext, multitenantRepository);
		
		multitenantAwareResourceResolver.init();
		
		EasyMock.verify(resourceResolverChain, applicationContext, multitenantRepository);
		assertTrue("Multitenant aware resource resolver should contain resource 1 for tenant 'TENANT_IDENTIFIER'", multitenantAwareResourceResolver.getResourceMap().get("TENANT_IDENTIFIER").contains(resource1));
		assertTrue("Multitenant aware resource resolver should contain resource 2 for tenant 'TENANT_IDENTIFIER'", multitenantAwareResourceResolver.getResourceMap().get("TENANT_IDENTIFIER").contains(resource2));
		assertTrue("Multitenant aware resource resolver should contain resource 2 for tenant 'TENANT_IDENTIFIER_1'", multitenantAwareResourceResolver.getResourceMap().get("TENANT_IDENTIFIER_1").contains(resource2));
		assertTrue("Multitenant aware resource resolver should contain resource 1 for tenant 'TENANT_IDENTIFIER_1'", multitenantAwareResourceResolver.getResourceMap().get("TENANT_IDENTIFIER_1").contains(resource1));
		assertEquals("MultitenantContext should equal the original context", "ORIGINAL_CONTEXT", MultitenantContextHolder.getContext().getTenantId());
	}
	
	@Test
	public void testInitAlreadyInitialized() {
		multitenantAwareResourceResolver.setResourceMap(resourceMap);
        EasyMock.replay(resourceResolverChain, applicationContext, multitenantRepository);
		
		multitenantAwareResourceResolver.init();
		
		EasyMock.verify(resourceResolverChain, applicationContext, multitenantRepository);
		assertTrue("Multitenant aware resource resolver should contain resource 1 for tenant 'TENANT_IDENTIFIER'", multitenantAwareResourceResolver.getResourceMap().get("TENANT_IDENTIFIER").contains(resource1));
		assertTrue("Multitenant aware resource resolver should contain resource 2 for tenant 'TENANT_IDENTIFIER'", multitenantAwareResourceResolver.getResourceMap().get("TENANT_IDENTIFIER").contains(resource2));
		assertNull("Multitenant aware resource resolver should not contain any resources for tenant 'TENANT_IDENTIFIER_1'", multitenantAwareResourceResolver.getResourceMap().get("TENANT_IDENTIFIER_1"));
		assertEquals("MultitenantContext should equal the original context", "ORIGINAL_CONTEXT", MultitenantContextHolder.getContext().getTenantId());
	}

	@Test
	public void testNotifyMultitenantEventDefault() {
		MultitenantEvent tenantEvent = new MultitenantEvent();
		tenantEvent.setIdentifier("NEW_TENANT");
		tenantEvent.setType(MultitenantEventType.OTHER);
		EasyMock.replay(resourceResolverChain, applicationContext, multitenantRepository);
		MultitenantContextHolder.getContext().setTenantId("ORIGINAL_CONTEXT");
		multitenantAwareResourceResolver.notify(tenantEvent);
		assertEquals("MultitenantContext should equal the original context", "ORIGINAL_CONTEXT", MultitenantContextHolder.getContext().getTenantId());
		
		EasyMock.verify(resourceResolverChain, applicationContext, multitenantRepository);
	}
	
	@Test
	public void testNotifyMultitenantEventCreate() {
		MultitenantEvent tenantEvent = new MultitenantEvent();
		tenantEvent.setIdentifier("NEW_TENANT");
		tenantEvent.setType(MultitenantEventType.CREATE);
		Multitenant multitenant = new Multitenant();
		multitenant.setIdentifier("NEW_TENANT");
		EasyMock.expect(multitenantRepository.findByIdentifier(EasyMock.eq("NEW_TENANT"))).andReturn(multitenant);
		EasyMock.expect(applicationContext.getResource("/directory/NEW_TENANT/directory")).andReturn(resource1);
	    EasyMock.expect(applicationContext.getResource("/directory1/directory2")).andReturn(resource2);
		EasyMock.replay(resourceResolverChain, applicationContext, multitenantRepository);
		MultitenantContextHolder.getContext().setTenantId("ORIGINAL_CONTEXT");
		multitenantAwareResourceResolver.notify(tenantEvent);
		assertEquals("MultitenantContext should equal the original context", "ORIGINAL_CONTEXT", MultitenantContextHolder.getContext().getTenantId());
		
		EasyMock.verify(resourceResolverChain, applicationContext, multitenantRepository);
	}

	@Test
	public void testHandle() {
        EasyMock.replay(resourceResolverChain, applicationContext, multitenantRepository);
		multitenantAwareResourceResolver.handle(new MultitenantEvent());
		
		EasyMock.verify(resourceResolverChain, applicationContext, multitenantRepository);
	}

	@Test
	public void testResolveResource() throws Exception {
		Resource mockResource = EasyMock.createMock(Resource.class);
		Resource throwsExceptionResource = EasyMock.createMock(Resource.class);
        Resource notReadableResource = EasyMock.createMock(Resource.class);
        resourceMap.get("TENANT_IDENTIFIER").add(mockResource);
        resourceMap.get("TENANT_IDENTIFIER").add(throwsExceptionResource);
		multitenantAwareResourceResolver.setResourceMap(resourceMap);
		MockHttpServletRequest request1 = new MockHttpServletRequest();
		EasyMock.expect(resourceResolverChain.resolveResource(EasyMock.eq(request1), EasyMock.eq("doesnotexist.txt"), EasyMock.eq(locations))).andReturn(null).anyTimes();
		EasyMock.expect(mockResource.createRelative(EasyMock.eq("doesnotexist.txt"))).andReturn(notReadableResource);
		EasyMock.expect(notReadableResource.exists()).andReturn(true).anyTimes();
		EasyMock.expect(notReadableResource.isReadable()).andReturn(false).anyTimes();
		EasyMock.expect(throwsExceptionResource.createRelative(EasyMock.eq("doesnotexist.txt"))).andThrow(new IOException()).anyTimes();
		
		EasyMock.replay(resourceResolverChain, applicationContext, multitenantRepository,mockResource, notReadableResource, throwsExceptionResource);
		assertNull("multitenantAwareResource resolver should return null for a null path", multitenantAwareResourceResolver.resolveResource(request1, null, locations, resourceResolverChain));
		assertNull("multitenantAwareResource resolver should return null for a path that contains 'WEB-INF'", multitenantAwareResourceResolver.resolveResource(request1, "/path/contains/WEB-INF", locations, resourceResolverChain));
		assertNull("multitenantAwareResource resolver should return null for a path that contains 'META-INF'", multitenantAwareResourceResolver.resolveResource(request1, "/path/contains/META-INF", locations, resourceResolverChain));
		assertNull("multitenantAwareResource resolver should return null for a path that contains 'META-INF'", multitenantAwareResourceResolver.resolveResource(request1, "/path/contains/META-INF", locations, resourceResolverChain));
		MultitenantContextHolder.getContext().setTenantId("TENANT_IDENTIFIER");
		assertNotNull("multitenantAwareResource resolver should return a resource for a path that exists relative to one of the existing resource paths", multitenantAwareResourceResolver.resolveResource(request1, "resource.txt", locations, resourceResolverChain));
		assertNull("multitenantAwareResource resolver should return null for a path that does not exist relative to one of the existing resource paths", multitenantAwareResourceResolver.resolveResource(request1, "doesnotexist.txt", locations, resourceResolverChain));
		
		EasyMock.verify(resourceResolverChain, applicationContext, multitenantRepository,mockResource, notReadableResource, throwsExceptionResource);
	}
	
	@Test
	public void testCreateTenantResourceMap() {  	    	
	    
	    EasyMock.expect(applicationContext.getResource("/directory/TENANT_IDENTIFIER/directory")).andReturn(resource1);
	    EasyMock.expect(applicationContext.getResource("/directory1/directory2")).andReturn(resource2);
		EasyMock.replay(resourceResolverChain, applicationContext, multitenantRepository);
		
		multitenantAwareResourceResolver.createTenantResourceMap(multitenant);
		
		
		EasyMock.verify(resourceResolverChain, applicationContext, multitenantRepository);
		
		assertTrue("Multitenant aware resource resolver should contain resource 1", multitenantAwareResourceResolver.getResourceMap().get("TENANT_IDENTIFIER").contains(resource1));
		assertTrue("Multitenant aware resource resolver should contain resource 2", multitenantAwareResourceResolver.getResourceMap().get("TENANT_IDENTIFIER").contains(resource2));
		
		
	}

	@Test
	public void testResolveUrlPath() {
		
		EasyMock.expect(resourceResolverChain.resolveUrlPath(EasyMock.eq("PATH"), EasyMock.eq(locations))).andReturn("RESOLVED");
		EasyMock.replay(resourceResolverChain, applicationContext, multitenantRepository);
		
		assertEquals("Multitenant resource resolver should use the chain to resolve the url", multitenantAwareResourceResolver.resolveUrlPath("PATH", locations, resourceResolverChain), "RESOLVED");
		
		EasyMock.verify(resourceResolverChain, applicationContext, multitenantRepository);
	}

}
