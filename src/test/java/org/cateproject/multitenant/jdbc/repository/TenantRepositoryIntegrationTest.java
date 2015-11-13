package org.cateproject.multitenant.jdbc.repository;

import static org.junit.Assert.assertNotNull;

import java.util.Iterator;
 	
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.cateproject.Application;
import org.cateproject.batch.TestingBatchTaskExecutorConfiguration;
import org.cateproject.multitenant.MultitenantRepository;
import org.cateproject.multitenant.domain.Multitenant;
import org.cateproject.multitenant.domain.MultitenantDataOnDemand;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class, TestingBatchTaskExecutorConfiguration.class})
@ActiveProfiles({"default", "integration-test"})
@IntegrationTest
public class TenantRepositoryIntegrationTest {
	
	@Autowired
	MultitenantRepository tenantRepository;
	
	@Autowired
	MultitenantDataOnDemand dod;

	@Test
	public void testInit() {
		assertNotNull(tenantRepository);
	}
	
	@Test
	public void testSave() {
		Assert.assertNotNull("Data on demand for 'Tenant' failed to initialize correctly", dod.getRandomTenant());
        Multitenant obj = dod.getNewTransientTenant(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Tenant' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Tenant' identifier to be null", obj.getId());
        try {
            tenantRepository.save(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        tenantRepository.flush();
        Assert.assertNotNull("Expected 'Tenant' identifier to no longer be null", obj.getId());
	}
	
	@Test
	public void testFlush() {
		tenantRepository.flush(); // No-Op
	}
	
	@Test
    public void testCount() {
        Assert.assertNotNull("Data on demand for 'Tenant' failed to initialize correctly", dod.getRandomTenant());
        long count = tenantRepository.count();
        Assert.assertTrue("Counter for 'Tenant' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void testFind() {
        Multitenant obj = dod.getRandomTenant();
        Assert.assertNotNull("Data on demand for 'Tenant' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Tenant' failed to provide an identifier", id);
        obj = tenantRepository.findOne(id);
        Assert.assertNotNull("Find method for 'Tenant' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Tenant' returned the incorrect identifier", id, obj.getId());
        obj = tenantRepository.findOne(Long.MAX_VALUE);
        Assert.assertNull("Find method for 'Tenant' returned tenant for id '" + Long.MAX_VALUE + "'", obj);
    }
    
    @Test
    public void testFindByIdentifier() {
        Multitenant obj = dod.getRandomTenant();
        Assert.assertNotNull("Data on demand for 'Tenant' failed to initialize correctly", obj);
        String identifier = obj.getIdentifier();
        Assert.assertNotNull("Data on demand for 'Tenant' failed to provide an identifier", identifier);
        obj = tenantRepository.findByIdentifier(identifier);
        Assert.assertNotNull("Find method for 'Tenant' illegally returned null for identifier '" + identifier + "'", obj);
        Assert.assertEquals("Find method for 'Tenant' returned the incorrect identifier", identifier, obj.getIdentifier());
        obj = tenantRepository.findByIdentifier("identifierDoesNotExist");
        Assert.assertNull("Find method for 'Tenant' returned null for non-existent identifier 'identifierDoesNotExist'", obj);
    }
    
    @Test
    public void testFindAll() {
        Assert.assertNotNull("Data on demand for 'Tenant' failed to initialize correctly", dod.getRandomTenant());
        long count = tenantRepository.count();
        Assert.assertTrue("Too expensive to perform a find all test for 'Tenant', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Multitenant> result = tenantRepository.findAll();
        Assert.assertNotNull("Find all method for 'Tenant' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Tenant' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void testFindEntries() {
        Assert.assertNotNull("Data on demand for 'Tenant' failed to initialize correctly", dod.getRandomTenant());
        long count = tenantRepository.count();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Multitenant> result = tenantRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
        Assert.assertNotNull("Find entries method for 'Tenant' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Tenant' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testSaveUpdate() {
        Multitenant obj = dod.getRandomTenant();
        Assert.assertNotNull("Data on demand for 'Tenant' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Tenant' failed to provide an identifier", id);
        obj = tenantRepository.findOne(id);
        boolean modified =  dod.modifyTenant(obj);
        Integer currentVersion = obj.getVersion();
        Multitenant merged = (Multitenant)tenantRepository.save(obj);
        tenantRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Tenant' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testDelete() {
        Multitenant obj = dod.getRandomTenant();
        Assert.assertNotNull("Data on demand for 'Tenant' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Tenant' failed to provide an identifier", id);
        obj = tenantRepository.findOne(id);
        tenantRepository.delete(obj);
        tenantRepository.flush();
        Assert.assertNull("Failed to remove 'Tenant' with identifier '" + id + "'", tenantRepository.findOne(id));
    }
    
    @After
	public void tearDown() {
		dod.tearDown();
	}

}
