package org.cateproject.repository.jpa;
import java.util.Iterator;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.cateproject.Application;
import org.cateproject.domain.Taxon;
import org.cateproject.multitenant.MultitenantContextHolder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@IntegrationTest
public class TaxonRepositoryIntegrationTest {
	    
	    @Autowired
	    TaxonDataOnDemand dod;
	    
	    @Autowired
	    TaxonRepository taxonRepository;
	    
            @Before
            public void setUp() {
                MultitenantContextHolder.getContext().setTenantId(null);
            }

	    @Test
	    public void testCount() {
	        Assert.assertNotNull("Data on demand for 'Taxon' failed to initialize correctly", dod.getRandomTaxon());
	        long count = taxonRepository.count();
	        Assert.assertTrue("Counter for 'Taxon' incorrectly reported there were no entries", count > 0);
	    }
	    
	    @Test
	    public void testFind() {
	        Taxon obj = dod.getRandomTaxon();
	        Assert.assertNotNull("Data on demand for 'Taxon' failed to initialize correctly", obj);
	        Long id = obj.getId();
	        Assert.assertNotNull("Data on demand for 'Taxon' failed to provide an identifier", id);
	        obj = taxonRepository.findOne(id);
	        Assert.assertNotNull("Find method for 'Taxon' illegally returned null for id '" + id + "'", obj);
	        Assert.assertEquals("Find method for 'Taxon' returned the incorrect identifier", id, obj.getId());
	    }
	    
	    @Test
	    public void testFindAll() {
	        Assert.assertNotNull("Data on demand for 'Taxon' failed to initialize correctly", dod.getRandomTaxon());
	        long count = taxonRepository.count();
	        Assert.assertTrue("Too expensive to perform a find all test for 'Taxon', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
	        List<Taxon> result = taxonRepository.findAll();
	        Assert.assertNotNull("Find all method for 'Taxon' illegally returned null", result);
	        Assert.assertTrue("Find all method for 'Taxon' failed to return any data", result.size() > 0);
	    }
	    
	    @Test
	    public void testFindEntries() {
	        Assert.assertNotNull("Data on demand for 'Taxon' failed to initialize correctly", dod.getRandomTaxon());
	        long count = taxonRepository.count();
	        if (count > 20) count = 20;
	        int firstResult = 0;
	        int maxResults = (int) count;
	        List<Taxon> result = taxonRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
	        Assert.assertNotNull("Find entries method for 'Taxon' illegally returned null", result);
	        Assert.assertEquals("Find entries method for 'Taxon' returned an incorrect number of entries", count, result.size());
	    }
	    
	    @Test
	    public void testFlush() {
	        Taxon obj = dod.getRandomTaxon();
	        Assert.assertNotNull("Data on demand for 'Taxon' failed to initialize correctly", obj);
	        Long id = obj.getId();
	        Assert.assertNotNull("Data on demand for 'Taxon' failed to provide an identifier", id);
	        obj = taxonRepository.findOne(id);
	        Assert.assertNotNull("Find method for 'Taxon' illegally returned null for id '" + id + "'", obj);
	        boolean modified =  dod.modifyTaxon(obj);
	        Integer currentVersion = obj.getVersion();
	        taxonRepository.flush();
	        Assert.assertTrue("Version for 'Taxon' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
	    }
	    
	    @Test
	    public void testSaveUpdate() {
	        Taxon obj = dod.getRandomTaxon();
	        Assert.assertNotNull("Data on demand for 'Taxon' failed to initialize correctly", obj);
	        Long id = obj.getId();
	        Assert.assertNotNull("Data on demand for 'Taxon' failed to provide an identifier", id);
	        obj = taxonRepository.findOne(id);
	        boolean modified =  dod.modifyTaxon(obj);
	        Integer currentVersion = obj.getVersion();
	        Taxon merged = (Taxon)taxonRepository.save(obj);
	        taxonRepository.flush();
	        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
	        Assert.assertTrue("Version for 'Taxon' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
	    }
	    
	    @Test
	    public void testSave() {
	        Assert.assertNotNull("Data on demand for 'Taxon' failed to initialize correctly", dod.getRandomTaxon());
	        Taxon obj = dod.getNewTransientTaxon(Integer.MAX_VALUE);
	        Assert.assertNotNull("Data on demand for 'Taxon' failed to provide a new transient entity", obj);
	        Assert.assertNull("Expected 'Taxon' identifier to be null", obj.getId());
	        try {
	            taxonRepository.save(obj);
	        } catch (final ConstraintViolationException e) {
	            final StringBuilder msg = new StringBuilder();
	            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
	                final ConstraintViolation<?> cv = iter.next();
	                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
	            }
	            throw new IllegalStateException(msg.toString(), e);
	        }
	        taxonRepository.flush();
	        Assert.assertNotNull("Expected 'Taxon' identifier to no longer be null", obj.getId());
	    }
	    
	    @Test
	    public void testDelete() {
	        Taxon obj = dod.getRandomTaxon();
	        Assert.assertNotNull("Data on demand for 'Taxon' failed to initialize correctly", obj);
	        Long id = obj.getId();
	        Assert.assertNotNull("Data on demand for 'Taxon' failed to provide an identifier", id);
	        obj = taxonRepository.findOne(id);
	        taxonRepository.delete(obj);
	        taxonRepository.flush();
	        Assert.assertNull("Failed to remove 'Taxon' with identifier '" + id + "'", taxonRepository.findOne(id));
	    }
	    
	    @After
	    public void tearDown() {
	    	dod.tearDown();
	    }
}
