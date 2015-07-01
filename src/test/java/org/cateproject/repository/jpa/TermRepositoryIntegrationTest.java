package org.cateproject.repository.jpa;
import java.util.Iterator;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.cateproject.Application;
import org.cateproject.domain.Term;
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
public class TermRepositoryIntegrationTest {

	@Autowired
    TermDataOnDemand dod;
    
    @Autowired
    TermRepository termRepository;
    
    @Test
    public void testCount() {
        Assert.assertNotNull("Data on demand for 'Term' failed to initialize correctly", dod.getRandomTerm());
        long count = termRepository.count();
        Assert.assertTrue("Counter for 'Term' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void testFind() {
        Term obj = dod.getRandomTerm();
        Assert.assertNotNull("Data on demand for 'Term' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Term' failed to provide an identifier", id);
        obj = termRepository.findOne(id);
        Assert.assertNotNull("Find method for 'Term' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Term' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void testFindAll() {
        Assert.assertNotNull("Data on demand for 'Term' failed to initialize correctly", dod.getRandomTerm());
        long count = termRepository.count();
        Assert.assertTrue("Too expensive to perform a find all test for 'Term', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Term> result = termRepository.findAll();
        Assert.assertNotNull("Find all method for 'Term' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Term' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void testFindEntries() {
        Assert.assertNotNull("Data on demand for 'Term' failed to initialize correctly", dod.getRandomTerm());
        long count = termRepository.count();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Term> result = termRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
        Assert.assertNotNull("Find entries method for 'Term' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Term' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testFlush() {
        Term obj = dod.getRandomTerm();
        Assert.assertNotNull("Data on demand for 'Term' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Term' failed to provide an identifier", id);
        obj = termRepository.findOne(id);
        Assert.assertNotNull("Find method for 'Term' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyTerm(obj);
        Integer currentVersion = obj.getVersion();
        termRepository.flush();
        Assert.assertTrue("Version for 'Term' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testSaveUpdate() {
        Term obj = dod.getRandomTerm();
        Assert.assertNotNull("Data on demand for 'Term' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Term' failed to provide an identifier", id);
        obj = termRepository.findOne(id);
        boolean modified =  dod.modifyTerm(obj);
        Integer currentVersion = obj.getVersion();
        Term merged = (Term)termRepository.save(obj);
        termRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Term' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testSave() {
        Assert.assertNotNull("Data on demand for 'Term' failed to initialize correctly", dod.getRandomTerm());
        Term obj = dod.getNewTransientTerm(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Term' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Term' identifier to be null", obj.getId());
        try {
            termRepository.save(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        termRepository.flush();
        Assert.assertNotNull("Expected 'Term' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void testDelete() {
        Term obj = dod.getRandomTerm();
        Assert.assertNotNull("Data on demand for 'Term' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Term' failed to provide an identifier", id);
        obj = termRepository.findOne(id);
        termRepository.delete(obj);
        termRepository.flush();
        Assert.assertNull("Failed to remove 'Term' with identifier '" + id + "'", termRepository.findOne(id));
    }
}
