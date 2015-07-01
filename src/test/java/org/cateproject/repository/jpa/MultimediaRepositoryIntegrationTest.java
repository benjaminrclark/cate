package org.cateproject.repository.jpa;
import java.util.Iterator;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.cateproject.Application;
import org.cateproject.domain.Multimedia;
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
public class MultimediaRepositoryIntegrationTest {
	
	@Autowired
    MultimediaDataOnDemand dod;
    
    @Autowired
    MultimediaRepository multimediaRepository;
    
    @Test
    public void testCount() {
        Assert.assertNotNull("Data on demand for 'Multimedia' failed to initialize correctly", dod.getRandomMultimedia());
        long count = multimediaRepository.count();
        Assert.assertTrue("Counter for 'Multimedia' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void testFind() {
        Multimedia obj = dod.getRandomMultimedia();
        Assert.assertNotNull("Data on demand for 'Multimedia' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Multimedia' failed to provide an identifier", id);
        obj = multimediaRepository.findOne(id);
        Assert.assertNotNull("Find method for 'Multimedia' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Multimedia' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void testFindAll() {
        Assert.assertNotNull("Data on demand for 'Multimedia' failed to initialize correctly", dod.getRandomMultimedia());
        long count = multimediaRepository.count();
        Assert.assertTrue("Too expensive to perform a find all test for 'Multimedia', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Multimedia> result = multimediaRepository.findAll();
        Assert.assertNotNull("Find all method for 'Multimedia' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Multimedia' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void testFindEntries() {
        Assert.assertNotNull("Data on demand for 'Multimedia' failed to initialize correctly", dod.getRandomMultimedia());
        long count = multimediaRepository.count();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Multimedia> result = multimediaRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
        Assert.assertNotNull("Find entries method for 'Multimedia' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Multimedia' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testFlush() {
        Multimedia obj = dod.getRandomMultimedia();
        Assert.assertNotNull("Data on demand for 'Multimedia' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Multimedia' failed to provide an identifier", id);
        obj = multimediaRepository.findOne(id);
        Assert.assertNotNull("Find method for 'Multimedia' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyMultimedia(obj);
        Integer currentVersion = obj.getVersion();
        multimediaRepository.flush();
        Assert.assertTrue("Version for 'Multimedia' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testSaveUpdate() {
        Multimedia obj = dod.getRandomMultimedia();
        Assert.assertNotNull("Data on demand for 'Multimedia' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Multimedia' failed to provide an identifier", id);
        obj = multimediaRepository.findOne(id);
        boolean modified =  dod.modifyMultimedia(obj);
        Integer currentVersion = obj.getVersion();
        Multimedia merged = (Multimedia)multimediaRepository.save(obj);
        multimediaRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Multimedia' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testSave() {
        Assert.assertNotNull("Data on demand for 'Multimedia' failed to initialize correctly", dod.getRandomMultimedia());
        Multimedia obj = dod.getNewTransientMultimedia(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Multimedia' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Multimedia' identifier to be null", obj.getId());
        try {
            multimediaRepository.save(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        multimediaRepository.flush();
        Assert.assertNotNull("Expected 'Multimedia' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void testDelete() {
        Multimedia obj = dod.getRandomMultimedia();
        Assert.assertNotNull("Data on demand for 'Multimedia' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Multimedia' failed to provide an identifier", id);
        obj = multimediaRepository.findOne(id);
        multimediaRepository.delete(obj);
        multimediaRepository.flush();
        Assert.assertNull("Failed to remove 'Multimedia' with identifier '" + id + "'", multimediaRepository.findOne(id));
    }

}
