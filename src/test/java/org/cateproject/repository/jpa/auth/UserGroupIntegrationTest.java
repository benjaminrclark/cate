package org.cateproject.repository.jpa.auth;
import java.util.Iterator;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.cateproject.Application;
import org.cateproject.domain.auth.UserGroup;
import org.cateproject.domain.auth.UserGroupDataOnDemand;
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
public class UserGroupIntegrationTest {

	@Autowired
    UserGroupDataOnDemand dod;
    
    @Autowired
    UserGroupRepository userGroupRepository;
    
    @Test
    public void testCount() {
        Assert.assertNotNull("Data on demand for 'UserGroup' failed to initialize correctly", dod.getRandomUserGroup());
        long count = userGroupRepository.count();
        Assert.assertTrue("Counter for 'UserGroup' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void testFind() {
        UserGroup obj = dod.getRandomUserGroup();
        Assert.assertNotNull("Data on demand for 'UserGroup' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'UserGroup' failed to provide an identifier", id);
        obj = userGroupRepository.findOne(id);
        Assert.assertNotNull("Find method for 'UserGroup' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'UserGroup' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void testFindAll() {
        Assert.assertNotNull("Data on demand for 'UserGroup' failed to initialize correctly", dod.getRandomUserGroup());
        long count = userGroupRepository.count();
        Assert.assertTrue("Too expensive to perform a find all test for 'UserGroup', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<UserGroup> result = userGroupRepository.findAll();
        Assert.assertNotNull("Find all method for 'UserGroup' illegally returned null", result);
        Assert.assertTrue("Find all method for 'UserGroup' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void testFindEntries() {
        Assert.assertNotNull("Data on demand for 'UserGroup' failed to initialize correctly", dod.getRandomUserGroup());
        long count = userGroupRepository.count();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<UserGroup> result = userGroupRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
        Assert.assertNotNull("Find entries method for 'UserGroup' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'UserGroup' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testFlush() {
        UserGroup obj = dod.getRandomUserGroup();
        Assert.assertNotNull("Data on demand for 'UserGroup' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'UserGroup' failed to provide an identifier", id);
        obj = userGroupRepository.findOne(id);
        Assert.assertNotNull("Find method for 'UserGroup' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyUserGroup(obj);
        Integer currentVersion = obj.getVersion();
        userGroupRepository.flush();
        Assert.assertTrue("Version for 'UserGroup' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testSaveUpdate() {
        UserGroup obj = dod.getRandomUserGroup();
        Assert.assertNotNull("Data on demand for 'UserGroup' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'UserGroup' failed to provide an identifier", id);
        obj = userGroupRepository.findOne(id);
        boolean modified =  dod.modifyUserGroup(obj);
        Integer currentVersion = obj.getVersion();
        UserGroup merged = (UserGroup)userGroupRepository.save(obj);
        userGroupRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'UserGroup' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testSave() {
        Assert.assertNotNull("Data on demand for 'UserGroup' failed to initialize correctly", dod.getRandomUserGroup());
        UserGroup obj = dod.getNewTransientUserGroup(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'UserGroup' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'UserGroup' identifier to be null", obj.getId());
        try {
            userGroupRepository.save(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        userGroupRepository.flush();
        Assert.assertNotNull("Expected 'UserGroup' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void testDelete() {
        UserGroup obj = dod.getRandomUserGroup();
        Assert.assertNotNull("Data on demand for 'UserGroup' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'UserGroup' failed to provide an identifier", id);
        obj = userGroupRepository.findOne(id);
        userGroupRepository.delete(obj);
        userGroupRepository.flush();
        Assert.assertNull("Failed to remove 'UserGroup' with identifier '" + id + "'", userGroupRepository.findOne(id));
    }

}
