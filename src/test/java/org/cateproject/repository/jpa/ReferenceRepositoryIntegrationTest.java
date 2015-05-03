package org.cateproject.repository.jpa;
import java.util.Iterator;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.cateproject.Application;
import org.cateproject.domain.Reference;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@IntegrationTest
public class ReferenceRepositoryIntegrationTest {

	@Autowired
    ReferenceDataOnDemand dod;
    
    @Autowired
    ReferenceRepository referenceRepository;
    
    @Test
    public void testCount() {
        Assert.assertNotNull("Data on demand for 'Reference' failed to initialize correctly", dod.getRandomReference());
        long count = referenceRepository.count();
        Assert.assertTrue("Counter for 'Reference' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void testFind() {
        Reference obj = dod.getRandomReference();
        Assert.assertNotNull("Data on demand for 'Reference' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Reference' failed to provide an identifier", id);
        obj = referenceRepository.findOne(id);
        Assert.assertNotNull("Find method for 'Reference' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Reference' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void testFindAll() {
        Assert.assertNotNull("Data on demand for 'Reference' failed to initialize correctly", dod.getRandomReference());
        long count = referenceRepository.count();
        Assert.assertTrue("Too expensive to perform a find all test for 'Reference', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Reference> result = referenceRepository.findAll();
        Assert.assertNotNull("Find all method for 'Reference' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Reference' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void testFindEntries() {
        Assert.assertNotNull("Data on demand for 'Reference' failed to initialize correctly", dod.getRandomReference());
        long count = referenceRepository.count();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Reference> result = referenceRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
        Assert.assertNotNull("Find entries method for 'Reference' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Reference' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testFlush() {
        Reference obj = dod.getRandomReference();
        Assert.assertNotNull("Data on demand for 'Reference' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Reference' failed to provide an identifier", id);
        obj = referenceRepository.findOne(id);
        Assert.assertNotNull("Find method for 'Reference' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyReference(obj);
        Integer currentVersion = obj.getVersion();
        referenceRepository.flush();
        Assert.assertTrue("Version for 'Reference' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testSaveUpdate() {
        Reference obj = dod.getRandomReference();
        Assert.assertNotNull("Data on demand for 'Reference' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Reference' failed to provide an identifier", id);
        obj = referenceRepository.findOne(id);
        boolean modified =  dod.modifyReference(obj);
        Integer currentVersion = obj.getVersion();
        Reference merged = (Reference)referenceRepository.save(obj);
        referenceRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Reference' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testSave() {
        Assert.assertNotNull("Data on demand for 'Reference' failed to initialize correctly", dod.getRandomReference());
        Reference obj = dod.getNewTransientReference(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Reference' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Reference' identifier to be null", obj.getId());
        try {
            referenceRepository.save(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        referenceRepository.flush();
        Assert.assertNotNull("Expected 'Reference' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void testDelete() {
        Reference obj = dod.getRandomReference();
        Assert.assertNotNull("Data on demand for 'Reference' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Reference' failed to provide an identifier", id);
        obj = referenceRepository.findOne(id);
        referenceRepository.delete(obj);
        referenceRepository.flush();
        Assert.assertNull("Failed to remove 'Reference' with identifier '" + id + "'", referenceRepository.findOne(id));
    }
}
