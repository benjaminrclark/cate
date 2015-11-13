package org.cateproject.repository.jpa;
import java.util.Iterator;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.cateproject.Application;
import org.cateproject.batch.TestingBatchTaskExecutorConfiguration;
import org.cateproject.domain.IdentificationKey;
import org.cateproject.multitenant.MultitenantContextHolder;
import org.junit.Assert;
import org.junit.Before;
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
public class IdentificationKeyRepositoryIntegrationTest {

    @Autowired
    IdentificationKeyDataOnDemand dod;
    
    @Autowired
    IdentificationKeyRepository identificationKeyRepository;
    
    @Before
    public void setUp() {
        MultitenantContextHolder.getContext().setTenantId(null);
    }

    @Test
    public void testCount() {
        Assert.assertNotNull("Data on demand for 'IdentificationKey' failed to initialize correctly", dod.getRandomIdentificationKey());
        long count = identificationKeyRepository.count();
        Assert.assertTrue("Counter for 'IdentificationKey' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void testFind() {
        IdentificationKey obj = dod.getRandomIdentificationKey();
        Assert.assertNotNull("Data on demand for 'IdentificationKey' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'IdentificationKey' failed to provide an identifier", id);
        obj = identificationKeyRepository.findOne(id);
        Assert.assertNotNull("Find method for 'IdentificationKey' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'IdentificationKey' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void testFindAll() {
        Assert.assertNotNull("Data on demand for 'IdentificationKey' failed to initialize correctly", dod.getRandomIdentificationKey());
        long count = identificationKeyRepository.count();
        Assert.assertTrue("Too expensive to perform a find all test for 'IdentificationKey', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<IdentificationKey> result = identificationKeyRepository.findAll();
        Assert.assertNotNull("Find all method for 'IdentificationKey' illegally returned null", result);
        Assert.assertTrue("Find all method for 'IdentificationKey' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void testFindEntries() {
        Assert.assertNotNull("Data on demand for 'IdentificationKey' failed to initialize correctly", dod.getRandomIdentificationKey());
        long count = identificationKeyRepository.count();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<IdentificationKey> result = identificationKeyRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
        Assert.assertNotNull("Find entries method for 'IdentificationKey' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'IdentificationKey' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testFlush() {
        IdentificationKey obj = dod.getRandomIdentificationKey();
        Assert.assertNotNull("Data on demand for 'IdentificationKey' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'IdentificationKey' failed to provide an identifier", id);
        obj = identificationKeyRepository.findOne(id);
        Assert.assertNotNull("Find method for 'IdentificationKey' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyIdentificationKey(obj);
        Integer currentVersion = obj.getVersion();
        identificationKeyRepository.flush();
        Assert.assertTrue("Version for 'IdentificationKey' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testSaveUpdate() {
        IdentificationKey obj = dod.getRandomIdentificationKey();
        Assert.assertNotNull("Data on demand for 'IdentificationKey' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'IdentificationKey' failed to provide an identifier", id);
        obj = identificationKeyRepository.findOne(id);
        boolean modified =  dod.modifyIdentificationKey(obj);
        Integer currentVersion = obj.getVersion();
        IdentificationKey merged = (IdentificationKey)identificationKeyRepository.save(obj);
        identificationKeyRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'IdentificationKey' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testSave() {
        Assert.assertNotNull("Data on demand for 'IdentificationKey' failed to initialize correctly", dod.getRandomIdentificationKey());
        IdentificationKey obj = dod.getNewTransientIdentificationKey(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'IdentificationKey' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'IdentificationKey' identifier to be null", obj.getId());
        try {
            identificationKeyRepository.save(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        identificationKeyRepository.flush();
        Assert.assertNotNull("Expected 'IdentificationKey' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void testDelete() {
        IdentificationKey obj = dod.getRandomIdentificationKey();
        Assert.assertNotNull("Data on demand for 'IdentificationKey' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'IdentificationKey' failed to provide an identifier", id);
        obj = identificationKeyRepository.findOne(id);
        identificationKeyRepository.delete(obj);
        identificationKeyRepository.flush();
        Assert.assertNull("Failed to remove 'IdentificationKey' with identifier '" + id + "'", identificationKeyRepository.findOne(id));
    }
}
