package org.cateproject.repository.jpa;
import java.util.Iterator;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.cateproject.Application;
import org.cateproject.domain.Node;
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
public class NodeRepositoryIntegrationTest {

	@Autowired
    NodeDataOnDemand dod;
    
    @Autowired
    NodeRepository nodeRepository;
    
    @Test
    public void testCount() {
        Assert.assertNotNull("Data on demand for 'Node' failed to initialize correctly", dod.getRandomNode());
        long count = nodeRepository.count();
        Assert.assertTrue("Counter for 'Node' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void testFind() {
        Node obj = dod.getRandomNode();
        Assert.assertNotNull("Data on demand for 'Node' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Node' failed to provide an identifier", id);
        obj = nodeRepository.findOne(id);
        Assert.assertNotNull("Find method for 'Node' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Node' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void testFindAll() {
        Assert.assertNotNull("Data on demand for 'Node' failed to initialize correctly", dod.getRandomNode());
        long count = nodeRepository.count();
        Assert.assertTrue("Too expensive to perform a find all test for 'Node', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Node> result = nodeRepository.findAll();
        Assert.assertNotNull("Find all method for 'Node' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Node' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void testFindEntries() {
        Assert.assertNotNull("Data on demand for 'Node' failed to initialize correctly", dod.getRandomNode());
        long count = nodeRepository.count();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Node> result = nodeRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
        Assert.assertNotNull("Find entries method for 'Node' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Node' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testFlush() {
        Node obj = dod.getRandomNode();
        Assert.assertNotNull("Data on demand for 'Node' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Node' failed to provide an identifier", id);
        obj = nodeRepository.findOne(id);
        Assert.assertNotNull("Find method for 'Node' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyNode(obj);
        Integer currentVersion = obj.getVersion();
        nodeRepository.flush();
        Assert.assertTrue("Version for 'Node' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testSaveUpdate() {
        Node obj = dod.getRandomNode();
        Assert.assertNotNull("Data on demand for 'Node' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Node' failed to provide an identifier", id);
        obj = nodeRepository.findOne(id);
        boolean modified =  dod.modifyNode(obj);
        Integer currentVersion = obj.getVersion();
        Node merged = (Node)nodeRepository.save(obj);
        nodeRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Node' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testSave() {
        Assert.assertNotNull("Data on demand for 'Node' failed to initialize correctly", dod.getRandomNode());
        Node obj = dod.getNewTransientNode(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Node' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Node' identifier to be null", obj.getId());
        try {
            nodeRepository.save(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        nodeRepository.flush();
        Assert.assertNotNull("Expected 'Node' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void testDelete() {
        Node obj = dod.getRandomNode();
        Assert.assertNotNull("Data on demand for 'Node' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Node' failed to provide an identifier", id);
        obj = nodeRepository.findOne(id);
        nodeRepository.delete(obj);
        nodeRepository.flush();
        Assert.assertNull("Failed to remove 'Node' with identifier '" + id + "'", nodeRepository.findOne(id));
    }
}
