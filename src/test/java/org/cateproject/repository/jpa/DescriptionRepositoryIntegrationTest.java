package org.cateproject.repository.jpa;
import java.util.Iterator;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.cateproject.Application;
import org.cateproject.domain.Description;
import org.junit.After;
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
public class DescriptionRepositoryIntegrationTest {

    @Autowired
    DescriptionDataOnDemand dod;
    
    @Autowired
    DescriptionRepository descriptionRepository;
    
    @Test
    public void testCount() {
        Assert.assertNotNull("Data on demand for 'Description' failed to initialize correctly", dod.getRandomDescription());
        long count = descriptionRepository.count();
        Assert.assertTrue("Counter for 'Description' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void testFind() {
        Description obj = dod.getRandomDescription();
        Assert.assertNotNull("Data on demand for 'Description' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Description' failed to provide an identifier", id);
        obj = descriptionRepository.findOne(id);
        Assert.assertNotNull("Find method for 'Description' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Description' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void testFindAll() {
        Assert.assertNotNull("Data on demand for 'Description' failed to initialize correctly", dod.getRandomDescription());
        long count = descriptionRepository.count();
        Assert.assertTrue("Too expensive to perform a find all test for 'Description', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Description> result = descriptionRepository.findAll();
        Assert.assertNotNull("Find all method for 'Description' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Description' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void testFindEntries() {
        Assert.assertNotNull("Data on demand for 'Description' failed to initialize correctly", dod.getRandomDescription());
        long count = descriptionRepository.count();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Description> result = descriptionRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
        Assert.assertNotNull("Find entries method for 'Description' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Description' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testFlush() {
        Description obj = dod.getRandomDescription();
        Assert.assertNotNull("Data on demand for 'Description' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Description' failed to provide an identifier", id);
        obj = descriptionRepository.findOne(id);
        Assert.assertNotNull("Find method for 'Description' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyDescription(obj);
        Integer currentVersion = obj.getVersion();
        descriptionRepository.flush();
        Assert.assertTrue("Version for 'Description' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testSaveUpdate() {
        Description obj = dod.getRandomDescription();
        Assert.assertNotNull("Data on demand for 'Description' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Description' failed to provide an identifier", id);
        obj = descriptionRepository.findOne(id);
        boolean modified =  dod.modifyDescription(obj);
        Integer currentVersion = obj.getVersion();
        Description merged = (Description)descriptionRepository.save(obj);
        descriptionRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Description' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testSave() {
        Assert.assertNotNull("Data on demand for 'Description' failed to initialize correctly", dod.getRandomDescription());
        Description obj = dod.getNewTransientDescription(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Description' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Description' identifier to be null", obj.getId());
        try {
            descriptionRepository.save(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        descriptionRepository.flush();
        Assert.assertNotNull("Expected 'Description' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void testDelete() {
        Description obj = dod.getRandomDescription();
        Assert.assertNotNull("Data on demand for 'Description' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Description' failed to provide an identifier", id);
        obj = descriptionRepository.findOne(id);
        descriptionRepository.delete(obj);
        descriptionRepository.flush();
        Assert.assertNull("Failed to remove 'Description' with identifier '" + id + "'", descriptionRepository.findOne(id));
    }
    
    @After
    public void tearDown() {
    	dod.tearDown();
    }
}
