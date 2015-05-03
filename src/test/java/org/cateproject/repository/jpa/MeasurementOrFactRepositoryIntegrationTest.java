package org.cateproject.repository.jpa;
import java.util.Iterator;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.cateproject.Application;
import org.cateproject.domain.MeasurementOrFact;
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
public class MeasurementOrFactRepositoryIntegrationTest {

	@Autowired
    MeasurementOrFactDataOnDemand dod;
    
    @Autowired
    MeasurementOrFactRepository measurementOrFactRepository;
    
    @Test
    public void testCount() {
        Assert.assertNotNull("Data on demand for 'MeasurementOrFact' failed to initialize correctly", dod.getRandomMeasurementOrFact());
        long count = measurementOrFactRepository.count();
        Assert.assertTrue("Counter for 'MeasurementOrFact' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void testFind() {
        MeasurementOrFact obj = dod.getRandomMeasurementOrFact();
        Assert.assertNotNull("Data on demand for 'MeasurementOrFact' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'MeasurementOrFact' failed to provide an identifier", id);
        obj = measurementOrFactRepository.findOne(id);
        Assert.assertNotNull("Find method for 'MeasurementOrFact' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'MeasurementOrFact' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void testFindAll() {
        Assert.assertNotNull("Data on demand for 'MeasurementOrFact' failed to initialize correctly", dod.getRandomMeasurementOrFact());
        long count = measurementOrFactRepository.count();
        Assert.assertTrue("Too expensive to perform a find all test for 'MeasurementOrFact', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<MeasurementOrFact> result = measurementOrFactRepository.findAll();
        Assert.assertNotNull("Find all method for 'MeasurementOrFact' illegally returned null", result);
        Assert.assertTrue("Find all method for 'MeasurementOrFact' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void testFindEntries() {
        Assert.assertNotNull("Data on demand for 'MeasurementOrFact' failed to initialize correctly", dod.getRandomMeasurementOrFact());
        long count = measurementOrFactRepository.count();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<MeasurementOrFact> result = measurementOrFactRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
        Assert.assertNotNull("Find entries method for 'MeasurementOrFact' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'MeasurementOrFact' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testFlush() {
        MeasurementOrFact obj = dod.getRandomMeasurementOrFact();
        Assert.assertNotNull("Data on demand for 'MeasurementOrFact' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'MeasurementOrFact' failed to provide an identifier", id);
        obj = measurementOrFactRepository.findOne(id);
        Assert.assertNotNull("Find method for 'MeasurementOrFact' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyMeasurementOrFact(obj);
        Integer currentVersion = obj.getVersion();
        measurementOrFactRepository.flush();
        Assert.assertTrue("Version for 'MeasurementOrFact' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testSaveUpdate() {
        MeasurementOrFact obj = dod.getRandomMeasurementOrFact();
        Assert.assertNotNull("Data on demand for 'MeasurementOrFact' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'MeasurementOrFact' failed to provide an identifier", id);
        obj = measurementOrFactRepository.findOne(id);
        boolean modified =  dod.modifyMeasurementOrFact(obj);
        Integer currentVersion = obj.getVersion();
        MeasurementOrFact merged = (MeasurementOrFact)measurementOrFactRepository.save(obj);
        measurementOrFactRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'MeasurementOrFact' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testSave() {
        Assert.assertNotNull("Data on demand for 'MeasurementOrFact' failed to initialize correctly", dod.getRandomMeasurementOrFact());
        MeasurementOrFact obj = dod.getNewTransientMeasurementOrFact(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'MeasurementOrFact' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'MeasurementOrFact' identifier to be null", obj.getId());
        try {
            measurementOrFactRepository.save(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        measurementOrFactRepository.flush();
        Assert.assertNotNull("Expected 'MeasurementOrFact' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void testDelete() {
        MeasurementOrFact obj = dod.getRandomMeasurementOrFact();
        Assert.assertNotNull("Data on demand for 'MeasurementOrFact' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'MeasurementOrFact' failed to provide an identifier", id);
        obj = measurementOrFactRepository.findOne(id);
        measurementOrFactRepository.delete(obj);
        measurementOrFactRepository.flush();
        Assert.assertNull("Failed to remove 'MeasurementOrFact' with identifier '" + id + "'", measurementOrFactRepository.findOne(id));
    }
}
