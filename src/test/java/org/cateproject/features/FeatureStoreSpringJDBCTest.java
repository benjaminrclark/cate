package org.cateproject.features;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.sql.DataSource;

import org.cateproject.features.rowmapper.FeatureRowMapper;
import org.cateproject.features.rowmapper.RoleRowMapper;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.ff4j.core.Feature;
import org.ff4j.exception.FeatureAlreadyExistException;
import org.ff4j.exception.FeatureNotFoundException;
import org.ff4j.exception.GroupNotFoundException;
import org.ff4j.store.JdbcStoreConstants;
import org.ff4j.strategy.el.ExpressionFlipStrategy;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;

public class FeatureStoreSpringJDBCTest {

    private FeatureStoreSpringJDBC featureStoreSpringJDBC;

    private JdbcTemplate jdbcTemplate;

    private DataSource dataSource;

    @Before
    public void setUp() {
        featureStoreSpringJDBC = new FeatureStoreSpringJDBC();
        jdbcTemplate = EasyMock.createMock(JdbcTemplate.class);
        dataSource = EasyMock.createMock(DataSource.class);
        featureStoreSpringJDBC.setJdbcTemplate(jdbcTemplate); 
        featureStoreSpringJDBC.setDataSource(dataSource);
    }

    @Test
    public void testRead() {
        List<Feature> features = new ArrayList<Feature>();
        features.add(new Feature("FEATURE_UID"));
        List<String> auths = new ArrayList<String>();
        auths.add("PERMISSION");
        
        EasyMock.expect(jdbcTemplate.query(EasyMock.eq(JdbcStoreConstants.SQLQUERY_GET_FEATURE_BY_ID),EasyMock.isA(FeatureRowMapper.class), EasyMock.eq("FEATURE_UID"))).andReturn(features); 
        EasyMock.expect(jdbcTemplate.query(EasyMock.eq(JdbcStoreConstants.SQL_GET_ROLES), (SingleColumnRowMapper<String>)EasyMock.isA(SingleColumnRowMapper.class), EasyMock.eq("FEATURE_UID"))).andReturn(auths);

        EasyMock.replay(jdbcTemplate,dataSource);
        Feature f = featureStoreSpringJDBC.read("FEATURE_UID");
        assertEquals("read should return a feature with the correct permissions",f.getPermissions(), new TreeSet<String>(auths));

        EasyMock.verify(jdbcTemplate,dataSource);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadNullUid() {
        featureStoreSpringJDBC.read(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadEmptyUid() {
        featureStoreSpringJDBC.read("");
    }

    @Test(expected = FeatureNotFoundException.class)
    public void testReadFeatureNotFound() {
        List<Feature> features = new ArrayList<Feature>();
        
        EasyMock.expect(jdbcTemplate.query(EasyMock.eq(JdbcStoreConstants.SQLQUERY_GET_FEATURE_BY_ID),EasyMock.isA(FeatureRowMapper.class), EasyMock.eq("FEATURE_UID"))).andReturn(features); 

        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.read("FEATURE_UID");
    }

    @Test
    public void testEnable() {
        EasyMock.expect(jdbcTemplate.queryForObject(EasyMock.eq(JdbcStoreConstants.SQL_EXIST), EasyMock.eq(Integer.class), EasyMock.eq("FEATURE_UID"))).andReturn(1);
        EasyMock.expect(jdbcTemplate.update(EasyMock.eq(JdbcStoreConstants.SQL_ENABLE), EasyMock.eq("FEATURE_UID"))).andReturn(1);

        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.enable("FEATURE_UID");
        EasyMock.verify(jdbcTemplate,dataSource);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEnableNullUid() {
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.enable(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEnableEmptyUid() {
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.enable("");
    }

    @Test(expected = FeatureNotFoundException.class)
    public void testEnableFeatureNotFound() {
        EasyMock.expect(jdbcTemplate.queryForObject(EasyMock.eq(JdbcStoreConstants.SQL_EXIST), EasyMock.eq(Integer.class), EasyMock.eq("FEATURE_UID"))).andReturn(0);

        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.enable("FEATURE_UID");
    }

    @Test
    public void testDisable() {
        EasyMock.expect(jdbcTemplate.queryForObject(EasyMock.eq(JdbcStoreConstants.SQL_EXIST), EasyMock.eq(Integer.class), EasyMock.eq("FEATURE_UID"))).andReturn(1);
        EasyMock.expect(jdbcTemplate.update(EasyMock.eq(JdbcStoreConstants.SQL_DISABLE), EasyMock.eq("FEATURE_UID"))).andReturn(1);

        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.disable("FEATURE_UID");
        EasyMock.verify(jdbcTemplate,dataSource);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDisableNullUid() {
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.disable(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDisableEmptyUid() {
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.disable("");
    }

    @Test(expected = FeatureNotFoundException.class)
    public void testDisableFeatureNotFound() {
        EasyMock.expect(jdbcTemplate.queryForObject(EasyMock.eq(JdbcStoreConstants.SQL_EXIST), EasyMock.eq(Integer.class), EasyMock.eq("FEATURE_UID"))).andReturn(0);

        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.disable("FEATURE_UID");
    }

    @Test
    public void testExist() {
        EasyMock.expect(jdbcTemplate.queryForObject(EasyMock.eq(JdbcStoreConstants.SQL_EXIST), EasyMock.eq(Integer.class), EasyMock.eq("FEATURE_UID"))).andReturn(1);

        EasyMock.replay(jdbcTemplate,dataSource);
        assertTrue("exist should return true",featureStoreSpringJDBC.exist("FEATURE_UID"));
        EasyMock.verify(jdbcTemplate,dataSource);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExistNullUid() {
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.exist(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExistEmptyUid() {
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.exist("");
    }

    @Test
    public void testCreate() {
        Feature feature = new Feature("FEATURE_UID");
        feature.setDescription("DESCRIPTION");
        feature.setGroup("GROUPNAME");
        feature.setPermissions(null);
        EasyMock.expect(jdbcTemplate.queryForObject(EasyMock.eq(JdbcStoreConstants.SQL_EXIST), EasyMock.eq(Integer.class), EasyMock.eq("FEATURE_UID"))).andReturn(0);
        EasyMock.expect(jdbcTemplate.update(EasyMock.eq(JdbcStoreConstants.SQL_CREATE),EasyMock.eq("FEATURE_UID"), EasyMock.eq(0), EasyMock.eq("DESCRIPTION"), EasyMock.isNull(), EasyMock.isNull(), EasyMock.eq("GROUPNAME"))).andReturn(1);
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.create(feature);
        EasyMock.verify(jdbcTemplate,dataSource);
    }

    @Test
    public void testCreateWithStrategy() {
        Set<String> permissions = new HashSet<String>();
        permissions.add("ROLE_1");
        permissions.add("ROLE_2");
        Map<String, String> params = new HashMap<String,String>();
        params.put("expression","EXPRESSION_VALUE");
        ExpressionFlipStrategy flipStrategy = new ExpressionFlipStrategy();
        flipStrategy.init("FEATURE_UID", params);
        Feature feature = new Feature("FEATURE_UID");
        feature.setDescription("DESCRIPTION");
        feature.setGroup("GROUPNAME");
        feature.setEnable(true);
        feature.setPermissions(permissions);
        feature.setFlippingStrategy(flipStrategy);
        EasyMock.expect(jdbcTemplate.queryForObject(EasyMock.eq(JdbcStoreConstants.SQL_EXIST), EasyMock.eq(Integer.class), EasyMock.eq("FEATURE_UID"))).andReturn(0);
        EasyMock.expect(jdbcTemplate.update(EasyMock.eq(JdbcStoreConstants.SQL_CREATE),EasyMock.eq("FEATURE_UID"), EasyMock.eq(1), EasyMock.eq("DESCRIPTION"), EasyMock.eq("org.ff4j.strategy.el.ExpressionFlipStrategy"), EasyMock.eq("expression=EXPRESSION_VALUE"), EasyMock.eq("GROUPNAME"))).andReturn(1);
        EasyMock.expect(jdbcTemplate.update(EasyMock.eq(JdbcStoreConstants.SQL_ADD_ROLE), EasyMock.eq("FEATURE_UID"), EasyMock.eq("ROLE_1"))).andReturn(1);
        EasyMock.expect(jdbcTemplate.update(EasyMock.eq(JdbcStoreConstants.SQL_ADD_ROLE), EasyMock.eq("FEATURE_UID"), EasyMock.eq("ROLE_2"))).andReturn(1);
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.create(feature);
        EasyMock.verify(jdbcTemplate,dataSource);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateNullFeature() {
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.create(null);
    }

    @Test(expected = FeatureAlreadyExistException.class)
    public void testCreateFeatureExists() {
        Feature feature = new Feature("FEATURE_UID");
        EasyMock.expect(jdbcTemplate.queryForObject(EasyMock.eq(JdbcStoreConstants.SQL_EXIST), EasyMock.eq(Integer.class), EasyMock.eq("FEATURE_UID"))).andReturn(1);
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.create(feature);
    }

    @Test
    public void testDelete() {
        List<String> auths = new ArrayList<String>();
        Feature feature = new Feature("FEATURE_UID");
        List<Feature> features = new ArrayList<Feature>();
        features.add(feature);
        
        EasyMock.expect(jdbcTemplate.queryForObject(EasyMock.eq(JdbcStoreConstants.SQL_EXIST), EasyMock.eq(Integer.class), EasyMock.eq("FEATURE_UID"))).andReturn(1);
        EasyMock.expect(jdbcTemplate.query(EasyMock.eq(JdbcStoreConstants.SQLQUERY_GET_FEATURE_BY_ID),EasyMock.isA(FeatureRowMapper.class), EasyMock.eq("FEATURE_UID"))).andReturn(features); 
        EasyMock.expect(jdbcTemplate.query(EasyMock.eq(JdbcStoreConstants.SQL_GET_ROLES), (SingleColumnRowMapper<String>)EasyMock.isA(SingleColumnRowMapper.class), EasyMock.eq("FEATURE_UID"))).andReturn(auths);
        EasyMock.expect(jdbcTemplate.update(EasyMock.eq(JdbcStoreConstants.SQL_DELETE), EasyMock.eq("FEATURE_UID"))).andReturn(1);
        
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.delete("FEATURE_UID");
        EasyMock.verify(jdbcTemplate,dataSource);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteNullUid() {
        featureStoreSpringJDBC.delete(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteEmptyUid() {
        featureStoreSpringJDBC.delete("");
    }

    @Test(expected = FeatureNotFoundException.class)
    public void testDeleteFeatureNotFound() {
        EasyMock.expect(jdbcTemplate.queryForObject(EasyMock.eq(JdbcStoreConstants.SQL_EXIST), EasyMock.eq(Integer.class), EasyMock.eq("FEATURE_UID"))).andReturn(0);
        
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.delete("FEATURE_UID");
    }

    @Test
    public void testDeleteWithPermissions() {
        List<String> auths = new ArrayList<String>();
        auths.add("ROLE_1");
        auths.add("ROLE_2");
        Feature feature = new Feature("FEATURE_UID");
        List<Feature> features = new ArrayList<Feature>();
        features.add(feature);
        
        EasyMock.expect(jdbcTemplate.queryForObject(EasyMock.eq(JdbcStoreConstants.SQL_EXIST), EasyMock.eq(Integer.class), EasyMock.eq("FEATURE_UID"))).andReturn(1);
        EasyMock.expect(jdbcTemplate.query(EasyMock.eq(JdbcStoreConstants.SQLQUERY_GET_FEATURE_BY_ID),EasyMock.isA(FeatureRowMapper.class), EasyMock.eq("FEATURE_UID"))).andReturn(features); 
        EasyMock.expect(jdbcTemplate.query(EasyMock.eq(JdbcStoreConstants.SQL_GET_ROLES), (SingleColumnRowMapper<String>)EasyMock.isA(SingleColumnRowMapper.class), EasyMock.eq("FEATURE_UID"))).andReturn(auths);
        EasyMock.expect(jdbcTemplate.update(EasyMock.eq(JdbcStoreConstants.SQL_DELETE), EasyMock.eq("FEATURE_UID"))).andReturn(1);
        EasyMock.expect(jdbcTemplate.update(EasyMock.eq(JdbcStoreConstants.SQL_DELETE_ROLE),EasyMock.eq("FEATURE_UID"),EasyMock.eq("ROLE_1"))).andReturn(1);
        EasyMock.expect(jdbcTemplate.update(EasyMock.eq(JdbcStoreConstants.SQL_DELETE_ROLE),EasyMock.eq("FEATURE_UID"),EasyMock.eq("ROLE_2"))).andReturn(1);
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.delete("FEATURE_UID");
        EasyMock.verify(jdbcTemplate,dataSource);
    }

    @Test
    public void testGrantRoleOnFeature() {
        
        EasyMock.expect(jdbcTemplate.queryForObject(EasyMock.eq(JdbcStoreConstants.SQL_EXIST), EasyMock.eq(Integer.class), EasyMock.eq("FEATURE_UID"))).andReturn(1);
        EasyMock.expect(jdbcTemplate.update(EasyMock.eq(JdbcStoreConstants.SQL_ADD_ROLE), EasyMock.eq("FEATURE_UID"), EasyMock.eq("ROLE_1"))).andReturn(1);
        
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.grantRoleOnFeature("FEATURE_UID", "ROLE_1");
        EasyMock.verify(jdbcTemplate,dataSource);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGrantRoleOnFeatureNullUid() {
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.grantRoleOnFeature(null, "ROLE_1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGrantRoleOnFeatureEmptyUid() {
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.grantRoleOnFeature("", "ROLE_1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGrantRoleOnFeatureNullRoleName() {
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.grantRoleOnFeature("FEATURE_UID", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGrantRoleOnFeatureEmptyRoleName() {
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.grantRoleOnFeature("FEATURE_UID", "");
    }

    @Test(expected = FeatureNotFoundException.class)
    public void testGrantRoleOnFeatureFeatureDoesNotExist() {
        
        EasyMock.expect(jdbcTemplate.queryForObject(EasyMock.eq(JdbcStoreConstants.SQL_EXIST), EasyMock.eq(Integer.class), EasyMock.eq("FEATURE_UID"))).andReturn(0);
        
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.grantRoleOnFeature("FEATURE_UID", "ROLE_1");
    }

    @Test
    public void testRemoveRoleFromFeature() {
        
        EasyMock.expect(jdbcTemplate.queryForObject(EasyMock.eq(JdbcStoreConstants.SQL_EXIST), EasyMock.eq(Integer.class), EasyMock.eq("FEATURE_UID"))).andReturn(1);
        EasyMock.expect(jdbcTemplate.update(EasyMock.eq(JdbcStoreConstants.SQL_DELETE_ROLE), EasyMock.eq("FEATURE_UID"), EasyMock.eq("ROLE_1"))).andReturn(1);
        
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.removeRoleFromFeature("FEATURE_UID", "ROLE_1");
        EasyMock.verify(jdbcTemplate,dataSource);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveRoleFromFeatureNullUid() {
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.removeRoleFromFeature(null, "ROLE_1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveRoleFromFeatureEmptyUid() {
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.removeRoleFromFeature("", "ROLE_1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveRoleFromFeatureNullRoleName() {
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.removeRoleFromFeature("FEATURE_UID", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveRoleFromFeatureEmptyRoleName() {
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.removeRoleFromFeature("FEATURE_UID", "");
    }

    @Test(expected = FeatureNotFoundException.class)
    public void testRemoveRoleFromFeatureFeatureDoesNotExist() {
        
        EasyMock.expect(jdbcTemplate.queryForObject(EasyMock.eq(JdbcStoreConstants.SQL_EXIST), EasyMock.eq(Integer.class), EasyMock.eq("FEATURE_UID"))).andReturn(0);
        
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.removeRoleFromFeature("FEATURE_UID", "ROLE_1");
    }

    @Test
    public void testExistGroup() {
        
        EasyMock.expect(jdbcTemplate.queryForObject(EasyMock.eq(JdbcStoreConstants.SQL_EXIST_GROUP), EasyMock.eq(Integer.class), EasyMock.eq("GROUPNAME"))).andReturn(1);
        EasyMock.expect(jdbcTemplate.queryForObject(EasyMock.eq(JdbcStoreConstants.SQL_EXIST_GROUP), EasyMock.eq(Integer.class), EasyMock.eq("GROUPNAME"))).andReturn(0);
        
        EasyMock.replay(jdbcTemplate,dataSource);
        assertTrue("existGroup should return true",featureStoreSpringJDBC.existGroup("GROUPNAME"));
        assertFalse("existGroup should return false",featureStoreSpringJDBC.existGroup("GROUPNAME"));
        EasyMock.verify(jdbcTemplate,dataSource);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExistGroupNullGroupName() {
        
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.existGroup(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExistGroupEmptyGroupName() {
        
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.existGroup("");
    }

    @Test
    public void testEnableGroup() {
        
        EasyMock.expect(jdbcTemplate.queryForObject(EasyMock.eq(JdbcStoreConstants.SQL_EXIST_GROUP), EasyMock.eq(Integer.class), EasyMock.eq("GROUPNAME"))).andReturn(1);
        EasyMock.expect(jdbcTemplate.update(EasyMock.eq(JdbcStoreConstants.SQL_ENABLE_GROUP), EasyMock.eq("GROUPNAME"))).andReturn(1);
        
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.enableGroup("GROUPNAME");
        EasyMock.verify(jdbcTemplate,dataSource);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEnableGroupNullGroup() {
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.enableGroup(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEnableGroupEmptyGroup() {
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.enableGroup("");
    }

    @Test(expected = GroupNotFoundException.class)
    public void testEnableGroupGroupNotFound() {
        
        EasyMock.expect(jdbcTemplate.queryForObject(EasyMock.eq(JdbcStoreConstants.SQL_EXIST_GROUP), EasyMock.eq(Integer.class), EasyMock.eq("GROUPNAME"))).andReturn(0);
        
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.enableGroup("GROUPNAME");
    }

    @Test
    public void testDisableGroup() {
        
        EasyMock.expect(jdbcTemplate.queryForObject(EasyMock.eq(JdbcStoreConstants.SQL_EXIST_GROUP), EasyMock.eq(Integer.class), EasyMock.eq("GROUPNAME"))).andReturn(1);
        EasyMock.expect(jdbcTemplate.update(EasyMock.eq(JdbcStoreConstants.SQL_DISABLE_GROUP), EasyMock.eq("GROUPNAME"))).andReturn(1);
        
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.disableGroup("GROUPNAME");
        EasyMock.verify(jdbcTemplate,dataSource);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDisableGroupNullGroup() {
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.disableGroup(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDisableGroupEmptyGroup() {
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.disableGroup("");
    }

    @Test(expected = GroupNotFoundException.class)
    public void testDisableGroupGroupNotFound() {
        
        EasyMock.expect(jdbcTemplate.queryForObject(EasyMock.eq(JdbcStoreConstants.SQL_EXIST_GROUP), EasyMock.eq(Integer.class), EasyMock.eq("GROUPNAME"))).andReturn(0);
        
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.disableGroup("GROUPNAME");
    }
    
    @Test
    public void testReadGroup() {
        Feature feature = new Feature("FEATURE_UID");
        List<Feature> features = new ArrayList<Feature>();
        features.add(feature);
        Map<String,Feature> expected = new HashMap<String,Feature>();
        expected.put("FEATURE_UID", feature); 

        EasyMock.expect(jdbcTemplate.queryForObject(EasyMock.eq(JdbcStoreConstants.SQL_EXIST_GROUP), EasyMock.eq(Integer.class), EasyMock.eq("GROUPNAME"))).andReturn(1);
        EasyMock.expect(jdbcTemplate.query(EasyMock.eq(JdbcStoreConstants.SQLQUERY_GET_FEATURE_GROUP), EasyMock.isA(FeatureRowMapper.class), EasyMock.eq("GROUPNAME"))).andReturn(features);
        
        EasyMock.replay(jdbcTemplate,dataSource);
        Map<String, Feature> actual = featureStoreSpringJDBC.readGroup("GROUPNAME");
        assertEquals("readGroup should return the expected results",expected, actual);
        EasyMock.verify(jdbcTemplate,dataSource);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadGroupNullGroup() {
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.readGroup(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadGroupEmptyGroup() {
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.readGroup("");
    }

    @Test(expected = GroupNotFoundException.class)
    public void testReadGroupGroupNotFound() {

        EasyMock.expect(jdbcTemplate.queryForObject(EasyMock.eq(JdbcStoreConstants.SQL_EXIST_GROUP), EasyMock.eq(Integer.class), EasyMock.eq("GROUPNAME"))).andReturn(0);
        
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.readGroup("GROUPNAME");
    }

    @Test
    public void testAddToGroup() {
        EasyMock.expect(jdbcTemplate.queryForObject(EasyMock.eq(JdbcStoreConstants.SQL_EXIST), EasyMock.eq(Integer.class), EasyMock.eq("FEATURE_UID"))).andReturn(1);
        EasyMock.expect(jdbcTemplate.update(EasyMock.eq(JdbcStoreConstants.SQL_ADD_TO_GROUP), EasyMock.eq("GROUPNAME"), EasyMock.eq("FEATURE_UID"))).andReturn(1);
        
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.addToGroup("FEATURE_UID","GROUPNAME");
        EasyMock.verify(jdbcTemplate,dataSource);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddToGroupNullGroup() {
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.addToGroup("FEATURE_UID",null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddToGroupEmptyGroup() {
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.addToGroup("FEATURE_UID","");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddToGroupNullFeature() {
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.addToGroup(null,"GROUPNAME");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddToGroupEmptyFeature() {
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.addToGroup("","GROUPNAME");
    }

    @Test(expected = FeatureNotFoundException.class)
    public void testAddToGroupFeatureNotFound() {

        EasyMock.expect(jdbcTemplate.queryForObject(EasyMock.eq(JdbcStoreConstants.SQL_EXIST), EasyMock.eq(Integer.class), EasyMock.eq("FEATURE_UID"))).andReturn(0);
        
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.addToGroup("FEATURE_UID","GROUPNAME");
    }

    @Test
    public void testRemoveFromGroup() {
        EasyMock.expect(jdbcTemplate.queryForObject(EasyMock.eq(JdbcStoreConstants.SQL_EXIST), EasyMock.eq(Integer.class), EasyMock.eq("FEATURE_UID"))).andReturn(1);
        EasyMock.expect(jdbcTemplate.queryForObject(EasyMock.eq(JdbcStoreConstants.SQL_EXIST_GROUP), EasyMock.eq(Integer.class), EasyMock.eq("GROUPNAME"))).andReturn(1);
        EasyMock.expect(jdbcTemplate.update(EasyMock.eq(JdbcStoreConstants.SQL_ADD_TO_GROUP), EasyMock.eq(""), EasyMock.eq("FEATURE_UID"))).andReturn(1);
        
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.removeFromGroup("FEATURE_UID","GROUPNAME");
        EasyMock.verify(jdbcTemplate,dataSource);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveFromGroupNullGroup() {
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.removeFromGroup("FEATURE_UID",null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveFromGroupEmptyGroup() {
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.removeFromGroup("FEATURE_UID","");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveFromGroupNullFeature() {
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.removeFromGroup(null,"GROUPNAME");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveFromGroupEmptyFeature() {
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.removeFromGroup("","GROUPNAME");
    }

    @Test(expected = FeatureNotFoundException.class)
    public void testRemoveFromGroupFeatureNotFound() {

        EasyMock.expect(jdbcTemplate.queryForObject(EasyMock.eq(JdbcStoreConstants.SQL_EXIST), EasyMock.eq(Integer.class), EasyMock.eq("FEATURE_UID"))).andReturn(0);
        
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.removeFromGroup("FEATURE_UID","GROUPNAME");
    }

    @Test(expected = GroupNotFoundException.class)
    public void testRemoveFromGroupGroupNotFound() {

        EasyMock.expect(jdbcTemplate.queryForObject(EasyMock.eq(JdbcStoreConstants.SQL_EXIST), EasyMock.eq(Integer.class), EasyMock.eq("FEATURE_UID"))).andReturn(1);
        EasyMock.expect(jdbcTemplate.queryForObject(EasyMock.eq(JdbcStoreConstants.SQL_EXIST_GROUP), EasyMock.eq(Integer.class), EasyMock.eq("GROUPNAME"))).andReturn(0);
        
        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.removeFromGroup("FEATURE_UID","GROUPNAME");
    }

    @Test
    public void testReadAll() {
        Set<String> expectedPermissions = new HashSet<String>();
        expectedPermissions.add("ROLE_1");
        expectedPermissions.add("ROLE_2");

        Map<String, Set<String>> roleMap = new HashMap<String, Set<String>>();
        roleMap.put("FEATURE_UID", expectedPermissions);
        roleMap.put("MISSING_FEATURE", new HashSet<String>());

        Capture<RoleRowMapper> captureRoleMapper = new Capture<RoleRowMapper>();
 
        Feature feature1 = new Feature("FEATURE_UID");
        Feature feature2 = new Feature("FEATURE_UID2");
        List<Feature> features = new ArrayList<Feature>();
        features.add(feature1);
        features.add(feature2);
        List<Integer> roles = new ArrayList<Integer>();
        Map<String, Feature> expected = new HashMap<String, Feature>();
        expected.put("FEATURE_UID", feature1);
        expected.put("FEATURE_UID2",feature2);
        EasyMock.expect(jdbcTemplate.query(EasyMock.eq(JdbcStoreConstants.SQLQUERY_ALLFEATURES), EasyMock.isA(FeatureRowMapper.class))).andReturn(features);
        EasyMock.expect(jdbcTemplate.query(EasyMock.eq(JdbcStoreConstants.SQL_GET_ALLROLES), EasyMock.and(EasyMock.isA(RoleRowMapper.class), EasyMock.capture(captureRoleMapper)))).andAnswer(new IAnswer<List<Integer>>() {
            public List<Integer> answer() throws Throwable {
                RoleRowMapper rrm = captureRoleMapper.getValue();
                rrm.setRoles(roleMap);
                return roles;
            }
            
});
        
        EasyMock.replay(jdbcTemplate,dataSource);
        assertEquals("readAll should return a map of features as expected", expected, featureStoreSpringJDBC.readAll());
        assertEquals("feature1 should have the correct permissions",feature1.getPermissions(), expectedPermissions);
        EasyMock.verify(jdbcTemplate,dataSource);
    }

    @Test
    public void testReadAllGroups() {
        List<String> groups = new ArrayList<String>();
        groups.add("GROUP1");
        groups.add("GROUP2");
        groups.add("GROUP1");
        groups.add(null);
        groups.add("");

        Set<String> expected = new HashSet<String>();
        expected.add("GROUP1");
        expected.add("GROUP2");
        EasyMock.expect(jdbcTemplate.query(EasyMock.eq(JdbcStoreConstants.SQLQUERY_ALLGROUPS), (SingleColumnRowMapper<String>)EasyMock.isA(SingleColumnRowMapper.class))).andReturn(groups);
        EasyMock.replay(jdbcTemplate,dataSource);
        assertEquals("readAllGroups should return the expected groups", expected, featureStoreSpringJDBC.readAllGroups());
        EasyMock.verify(jdbcTemplate,dataSource);
    }

    @Test
    public void testUpdateDisable() {
        Map<String,String> params = new HashMap<String, String>();
        params.put("expression", "VALUE");
        ExpressionFlipStrategy expressionFlipStrategy = new ExpressionFlipStrategy();
        expressionFlipStrategy.init("FEATURE_UID", params);

        Set<String> newPermissions = new HashSet<String>();
        newPermissions.add("NEW_ROLE");

        Feature newFeature = new Feature("FEATURE_UID");
        newFeature.setDescription("NEW_DESCRIPTION");
        newFeature.setGroup("NEW_GROUP");
        newFeature.setFlippingStrategy(expressionFlipStrategy);
        newFeature.setPermissions(newPermissions);

        Feature oldFeature = new Feature("FEATURE_UID", true);
        List<Feature> features = new ArrayList<Feature>();
        features.add(oldFeature);
        List<String> auths = new ArrayList<String>();
        auths.add("OLD_ROLE");

        EasyMock.expect(jdbcTemplate.query(EasyMock.eq(JdbcStoreConstants.SQLQUERY_GET_FEATURE_BY_ID),EasyMock.isA(FeatureRowMapper.class), EasyMock.eq("FEATURE_UID"))).andReturn(features); 
        EasyMock.expect(jdbcTemplate.query(EasyMock.eq(JdbcStoreConstants.SQL_GET_ROLES), (SingleColumnRowMapper<String>)EasyMock.isA(SingleColumnRowMapper.class), EasyMock.eq("FEATURE_UID"))).andReturn(auths);

        EasyMock.expect(jdbcTemplate.update(EasyMock.eq(JdbcStoreConstants.SQL_UPDATE), EasyMock.eq("0"), EasyMock.eq("NEW_DESCRIPTION"), EasyMock.eq("org.ff4j.strategy.el.ExpressionFlipStrategy"), EasyMock.eq("expression=VALUE"), EasyMock.eq("NEW_GROUP"), EasyMock.eq("FEATURE_UID"))).andReturn(1);

        EasyMock.expect(jdbcTemplate.queryForObject(EasyMock.eq(JdbcStoreConstants.SQL_EXIST), EasyMock.eq(Integer.class), EasyMock.eq("FEATURE_UID"))).andReturn(1).anyTimes();
        EasyMock.expect(jdbcTemplate.update(EasyMock.eq(JdbcStoreConstants.SQL_DISABLE), EasyMock.eq("FEATURE_UID"))).andReturn(1);

        EasyMock.expect(jdbcTemplate.update(EasyMock.eq(JdbcStoreConstants.SQL_DELETE_ROLE), EasyMock.eq("FEATURE_UID"), EasyMock.eq("OLD_ROLE"))).andReturn(1);
        EasyMock.expect(jdbcTemplate.update(EasyMock.eq(JdbcStoreConstants.SQL_ADD_ROLE), EasyMock.eq("FEATURE_UID"), EasyMock.eq("NEW_ROLE"))).andReturn(1);

        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.update(newFeature);
        EasyMock.verify(jdbcTemplate,dataSource);
    }

    @Test
    public void testUpdateEnable() {
        Map<String,String> params = new HashMap<String, String>();
        params.put("expression", "VALUE");
        ExpressionFlipStrategy expressionFlipStrategy = new ExpressionFlipStrategy();
        expressionFlipStrategy.init("FEATURE_UID", params);

        Set<String> newPermissions = new HashSet<String>();
        newPermissions.add("NEW_ROLE");

        Feature newFeature = new Feature("FEATURE_UID", true);
        newFeature.setDescription("NEW_DESCRIPTION");
        newFeature.setGroup("NEW_GROUP");
        newFeature.setFlippingStrategy(expressionFlipStrategy);
        newFeature.setPermissions(newPermissions);

        Feature oldFeature = new Feature("FEATURE_UID", false);
        List<Feature> features = new ArrayList<Feature>();
        features.add(oldFeature);
        List<String> auths = new ArrayList<String>();
        auths.add("OLD_ROLE");

        EasyMock.expect(jdbcTemplate.query(EasyMock.eq(JdbcStoreConstants.SQLQUERY_GET_FEATURE_BY_ID),EasyMock.isA(FeatureRowMapper.class), EasyMock.eq("FEATURE_UID"))).andReturn(features); 
        EasyMock.expect(jdbcTemplate.query(EasyMock.eq(JdbcStoreConstants.SQL_GET_ROLES), (SingleColumnRowMapper<String>)EasyMock.isA(SingleColumnRowMapper.class), EasyMock.eq("FEATURE_UID"))).andReturn(auths);

        EasyMock.expect(jdbcTemplate.update(EasyMock.eq(JdbcStoreConstants.SQL_UPDATE), EasyMock.eq("1"), EasyMock.eq("NEW_DESCRIPTION"), EasyMock.eq("org.ff4j.strategy.el.ExpressionFlipStrategy"), EasyMock.eq("expression=VALUE"), EasyMock.eq("NEW_GROUP"), EasyMock.eq("FEATURE_UID"))).andReturn(1);

        EasyMock.expect(jdbcTemplate.queryForObject(EasyMock.eq(JdbcStoreConstants.SQL_EXIST), EasyMock.eq(Integer.class), EasyMock.eq("FEATURE_UID"))).andReturn(1).anyTimes();
        EasyMock.expect(jdbcTemplate.update(EasyMock.eq(JdbcStoreConstants.SQL_ENABLE), EasyMock.eq("FEATURE_UID"))).andReturn(1);

        EasyMock.expect(jdbcTemplate.update(EasyMock.eq(JdbcStoreConstants.SQL_DELETE_ROLE), EasyMock.eq("FEATURE_UID"), EasyMock.eq("OLD_ROLE"))).andReturn(1);
        EasyMock.expect(jdbcTemplate.update(EasyMock.eq(JdbcStoreConstants.SQL_ADD_ROLE), EasyMock.eq("FEATURE_UID"), EasyMock.eq("NEW_ROLE"))).andReturn(1);

        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.update(newFeature);
        EasyMock.verify(jdbcTemplate,dataSource);
    }


    @Test
    public void testToString() {
        Set<String> expectedPermissions = new HashSet<String>();
        expectedPermissions.add("ROLE_1");
        expectedPermissions.add("ROLE_2");

        Map<String, Set<String>> roleMap = new HashMap<String, Set<String>>();
        roleMap.put("FEATURE_UID", expectedPermissions);

        Capture<RoleRowMapper> captureRoleMapper = new Capture<RoleRowMapper>();
        List<Integer> roles = new ArrayList<Integer>();

        List<Feature> features = new ArrayList<Feature>();
        features.add(new Feature("FEATURE_UID"));
        features.add(new Feature("FEATURE2_UID"));
        EasyMock.expect(jdbcTemplate.query(EasyMock.eq(JdbcStoreConstants.SQLQUERY_ALLFEATURES), EasyMock.isA(FeatureRowMapper.class))).andReturn(features);
        EasyMock.expect(jdbcTemplate.query(EasyMock.eq(JdbcStoreConstants.SQL_GET_ALLROLES), EasyMock.and(EasyMock.isA(RoleRowMapper.class), EasyMock.capture(captureRoleMapper)))).andAnswer(new IAnswer<List<Integer>>() {
            public List<Integer> answer() throws Throwable {
                RoleRowMapper rrm = captureRoleMapper.getValue();
                rrm.setRoles(roleMap);
                return roles;
            }
            
});

        List<String> groups = new ArrayList<String>();
        groups.add("GROUP_1");
        groups.add("GROUP_2");
        featureStoreSpringJDBC.setDataSource(new JdbcDataSource());
        EasyMock.expect(jdbcTemplate.query(EasyMock.eq(JdbcStoreConstants.SQLQUERY_ALLGROUPS), (SingleColumnRowMapper<String>)EasyMock.isA(SingleColumnRowMapper.class))).andReturn(groups);


        EasyMock.replay(jdbcTemplate,dataSource);
        assertEquals("toString should return the expected string", featureStoreSpringJDBC.toString(), "{\"type\":\"org.cateproject.features.FeatureStoreSpringJDBC\",\"datasource\":\"class org.h2.jdbcx.JdbcDataSource\",\"numberOfFeatures\":2,\"cached\":false,\"features\":[\"FEATURE_UID\",\"FEATURE2_UID\"],\"numberOfGroups\":2,\"groups\":[\"GROUP_1\",\"GROUP_2\"]}");
        EasyMock.verify(jdbcTemplate,dataSource);
    }

    @Test
    public void testToStringWithCache() {
        Set<String> expectedPermissions = new HashSet<String>();
        expectedPermissions.add("ROLE_1");
        expectedPermissions.add("ROLE_2");

        Map<String, Set<String>> roleMap = new HashMap<String, Set<String>>();
        roleMap.put("FEATURE_UID", expectedPermissions);

        Capture<RoleRowMapper> captureRoleMapper = new Capture<RoleRowMapper>();
        List<Integer> roles = new ArrayList<Integer>();

        List<Feature> features = new ArrayList<Feature>();
        features.add(new Feature("FEATURE_UID"));
        features.add(new Feature("FEATURE2_UID"));
        EasyMock.expect(jdbcTemplate.query(EasyMock.eq(JdbcStoreConstants.SQLQUERY_ALLFEATURES), EasyMock.isA(FeatureRowMapper.class))).andReturn(features);
        EasyMock.expect(jdbcTemplate.query(EasyMock.eq(JdbcStoreConstants.SQL_GET_ALLROLES), EasyMock.and(EasyMock.isA(RoleRowMapper.class), EasyMock.capture(captureRoleMapper)))).andAnswer(new IAnswer<List<Integer>>() {
            public List<Integer> answer() throws Throwable {
                RoleRowMapper rrm = captureRoleMapper.getValue();
                rrm.setRoles(roleMap);
                return roles;
            }
            
});

        List<String> groups = new ArrayList<String>();
        groups.add("GROUP_1");
        groups.add("GROUP_2");

        featureStoreSpringJDBC.setDataSource(new JdbcDataSource());
        featureStoreSpringJDBC.setCached(true);
        featureStoreSpringJDBC.setCacheProvider("CACHE_PROVIDER");
        featureStoreSpringJDBC.setCachedTargetStore("CACHED_TARGET_STORE");
        EasyMock.expect(jdbcTemplate.query(EasyMock.eq(JdbcStoreConstants.SQLQUERY_ALLGROUPS), (SingleColumnRowMapper<String>)EasyMock.isA(SingleColumnRowMapper.class))).andReturn(groups);


        EasyMock.replay(jdbcTemplate,dataSource);
        assertEquals("toString should return the expected string", featureStoreSpringJDBC.toString(), "{\"type\":\"org.cateproject.features.FeatureStoreSpringJDBC\",\"datasource\":\"class org.h2.jdbcx.JdbcDataSource\",\"numberOfFeatures\":2,\"cached\":true,\"cacheProvider\":\"CACHE_PROVIDER\",\"cacheStore\":\"CACHED_TARGET_STORE\",\"features\":[\"FEATURE_UID\",\"FEATURE2_UID\"],\"numberOfGroups\":2,\"groups\":[\"GROUP_1\",\"GROUP_2\"]}");
        EasyMock.verify(jdbcTemplate,dataSource);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateNullFeature() {
        featureStoreSpringJDBC.update(null);
    }

    @Test
    public void testGetJdbcTemplate() {
        featureStoreSpringJDBC.setJdbcTemplate(null);
        featureStoreSpringJDBC.setDataSource(new JdbcDataSource());

        EasyMock.replay(jdbcTemplate,dataSource);
        assertNotNull("getJdbcTemplate should create a jdbcTemplate from the dataSource", featureStoreSpringJDBC.getJdbcTemplate());
        EasyMock.verify(jdbcTemplate,dataSource);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetJdbcTemplateWithNullDataSource() {
        featureStoreSpringJDBC.setJdbcTemplate(null);
        featureStoreSpringJDBC.setDataSource(null);

        EasyMock.replay(jdbcTemplate,dataSource);
        featureStoreSpringJDBC.getJdbcTemplate();
    }

    @Test
    public void testIsCached() {
        assertFalse("isCached should return false", featureStoreSpringJDBC.isCached());
    }

    @Test
    public void testGetCacheProvider() {
        assertNull("getCacheProvider should return null", featureStoreSpringJDBC.getCacheProvider());
    }

    @Test
    public void testGetCachedTargetStore() {
        assertNull("getCachedTargetStore should return null", featureStoreSpringJDBC.getCachedTargetStore());
    }
}
