package org.cateproject.repository.jpa;
import java.util.Iterator;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.cateproject.Application;
import org.cateproject.domain.VernacularName;
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
public class VernacularNameRepositoryIntegrationTest {

	@Autowired
    VernacularNameDataOnDemand dod;
    
    @Autowired
    VernacularNameRepository vernacularNameRepository;
    
    @Test
    public void testCount() {
        Assert.assertNotNull("Data on demand for 'VernacularName' failed to initialize correctly", dod.getRandomVernacularName());
        long count = vernacularNameRepository.count();
        Assert.assertTrue("Counter for 'VernacularName' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void testFind() {
        VernacularName obj = dod.getRandomVernacularName();
        Assert.assertNotNull("Data on demand for 'VernacularName' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'VernacularName' failed to provide an identifier", id);
        obj = vernacularNameRepository.findOne(id);
        Assert.assertNotNull("Find method for 'VernacularName' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'VernacularName' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void testFindAll() {
        Assert.assertNotNull("Data on demand for 'VernacularName' failed to initialize correctly", dod.getRandomVernacularName());
        long count = vernacularNameRepository.count();
        Assert.assertTrue("Too expensive to perform a find all test for 'VernacularName', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<VernacularName> result = vernacularNameRepository.findAll();
        Assert.assertNotNull("Find all method for 'VernacularName' illegally returned null", result);
        Assert.assertTrue("Find all method for 'VernacularName' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void testFindEntries() {
        Assert.assertNotNull("Data on demand for 'VernacularName' failed to initialize correctly", dod.getRandomVernacularName());
        long count = vernacularNameRepository.count();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<VernacularName> result = vernacularNameRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
        Assert.assertNotNull("Find entries method for 'VernacularName' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'VernacularName' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testFlush() {
        VernacularName obj = dod.getRandomVernacularName();
        Assert.assertNotNull("Data on demand for 'VernacularName' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'VernacularName' failed to provide an identifier", id);
        obj = vernacularNameRepository.findOne(id);
        Assert.assertNotNull("Find method for 'VernacularName' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyVernacularName(obj);
        Integer currentVersion = obj.getVersion();
        vernacularNameRepository.flush();
        Assert.assertTrue("Version for 'VernacularName' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testSaveUpdate() {
        VernacularName obj = dod.getRandomVernacularName();
        Assert.assertNotNull("Data on demand for 'VernacularName' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'VernacularName' failed to provide an identifier", id);
        obj = vernacularNameRepository.findOne(id);
        boolean modified =  dod.modifyVernacularName(obj);
        Integer currentVersion = obj.getVersion();
        VernacularName merged = (VernacularName)vernacularNameRepository.save(obj);
        vernacularNameRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'VernacularName' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testSave() {
        Assert.assertNotNull("Data on demand for 'VernacularName' failed to initialize correctly", dod.getRandomVernacularName());
        VernacularName obj = dod.getNewTransientVernacularName(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'VernacularName' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'VernacularName' identifier to be null", obj.getId());
        try {
            vernacularNameRepository.save(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        vernacularNameRepository.flush();
        Assert.assertNotNull("Expected 'VernacularName' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void testDelete() {
        VernacularName obj = dod.getRandomVernacularName();
        Assert.assertNotNull("Data on demand for 'VernacularName' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'VernacularName' failed to provide an identifier", id);
        obj = vernacularNameRepository.findOne(id);
        vernacularNameRepository.delete(obj);
        vernacularNameRepository.flush();
        Assert.assertNull("Failed to remove 'VernacularName' with identifier '" + id + "'", vernacularNameRepository.findOne(id));
    }
    
    @After
    public void tearDown() {
    	dod.tearDown();
    }
}
