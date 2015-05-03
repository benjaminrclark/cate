package org.cateproject.repository.jpa;
import java.util.Iterator;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.cateproject.Application;
import org.cateproject.domain.Distribution;
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
public class DistributionRepositoryIntegrationTest {

	@Autowired
    DistributionDataOnDemand dod;
    
    @Autowired
    DistributionRepository distributionRepository;
    
    @Test
    public void testCount() {
        Assert.assertNotNull("Data on demand for 'Distribution' failed to initialize correctly", dod.getRandomDistribution());
        long count = distributionRepository.count();
        Assert.assertTrue("Counter for 'Distribution' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void testFind() {
        Distribution obj = dod.getRandomDistribution();
        Assert.assertNotNull("Data on demand for 'Distribution' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Distribution' failed to provide an identifier", id);
        obj = distributionRepository.findOne(id);
        Assert.assertNotNull("Find method for 'Distribution' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Distribution' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void testFindAll() {
        Assert.assertNotNull("Data on demand for 'Distribution' failed to initialize correctly", dod.getRandomDistribution());
        long count = distributionRepository.count();
        Assert.assertTrue("Too expensive to perform a find all test for 'Distribution', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Distribution> result = distributionRepository.findAll();
        Assert.assertNotNull("Find all method for 'Distribution' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Distribution' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void testFindEntries() {
        Assert.assertNotNull("Data on demand for 'Distribution' failed to initialize correctly", dod.getRandomDistribution());
        long count = distributionRepository.count();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Distribution> result = distributionRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
        Assert.assertNotNull("Find entries method for 'Distribution' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Distribution' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testFlush() {
        Distribution obj = dod.getRandomDistribution();
        Assert.assertNotNull("Data on demand for 'Distribution' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Distribution' failed to provide an identifier", id);
        obj = distributionRepository.findOne(id);
        Assert.assertNotNull("Find method for 'Distribution' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyDistribution(obj);
        Integer currentVersion = obj.getVersion();
        distributionRepository.flush();
        Assert.assertTrue("Version for 'Distribution' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testSaveUpdate() {
        Distribution obj = dod.getRandomDistribution();
        Assert.assertNotNull("Data on demand for 'Distribution' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Distribution' failed to provide an identifier", id);
        obj = distributionRepository.findOne(id);
        boolean modified =  dod.modifyDistribution(obj);
        Integer currentVersion = obj.getVersion();
        Distribution merged = (Distribution)distributionRepository.save(obj);
        distributionRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Distribution' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testSave() {
        Assert.assertNotNull("Data on demand for 'Distribution' failed to initialize correctly", dod.getRandomDistribution());
        Distribution obj = dod.getNewTransientDistribution(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Distribution' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Distribution' identifier to be null", obj.getId());
        try {
            distributionRepository.save(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        distributionRepository.flush();
        Assert.assertNotNull("Expected 'Distribution' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void testDelete() {
        Distribution obj = dod.getRandomDistribution();
        Assert.assertNotNull("Data on demand for 'Distribution' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Distribution' failed to provide an identifier", id);
        obj = distributionRepository.findOne(id);
        distributionRepository.delete(obj);
        distributionRepository.flush();
        Assert.assertNull("Failed to remove 'Distribution' with identifier '" + id + "'", distributionRepository.findOne(id));
    }
}
