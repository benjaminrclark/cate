package org.cateproject.features.rowmapper;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.easymock.EasyMock;
import org.ff4j.core.Feature;
import org.ff4j.exception.FeatureAccessException;
import org.ff4j.store.JdbcStoreConstants;
import org.ff4j.strategy.el.ExpressionFlipStrategy;
import org.junit.Before;
import org.junit.Test;

public class FeatureRowMapperTest {

    private FeatureRowMapper featureRowMapper;

    private ResultSet resultSet;

    @Before
    public void setUp() {
        featureRowMapper = new FeatureRowMapper(); 
        resultSet = EasyMock.createMock(ResultSet.class);
    }

    @Test
    public void testMapRowNullStrategy() throws SQLException {
        EasyMock.expect(resultSet.getString(EasyMock.eq(JdbcStoreConstants.COL_FEAT_UID))).andReturn("FEATURE_UID");
        EasyMock.expect(resultSet.getInt(EasyMock.eq(JdbcStoreConstants.COL_FEAT_ENABLE))).andReturn(1);
        EasyMock.expect(resultSet.getString(EasyMock.eq(JdbcStoreConstants.COL_FEAT_DESCRIPTION))).andReturn("DESCRIPTION");
        EasyMock.expect(resultSet.getString(EasyMock.eq(JdbcStoreConstants.COL_FEAT_GROUPNAME))).andReturn("GROUPNAME");
        EasyMock.expect(resultSet.getString(EasyMock.eq(JdbcStoreConstants.COL_FEAT_STRATEGY))).andReturn(null);

        EasyMock.replay(resultSet);

        Feature feature = featureRowMapper.mapRow(resultSet,0);
        assertTrue("Feature should be enabled",feature.isEnable());
        assertEquals("Feature should have a uid 'FEATURE_UID'",feature.getUid(),"FEATURE_UID");
        assertEquals("Feature should have a description 'DESCRIPTION'",feature.getDescription(),"DESCRIPTION");
        assertEquals("Feature should have a group 'GROUPNAME'",feature.getGroup(),"GROUPNAME");
        assertNull("Feature should have a null flippingStrategy",feature.getFlippingStrategy());

        EasyMock.verify(resultSet);
    }

    @Test
    public void testMapRowEmptyStrategy() throws SQLException {
        EasyMock.expect(resultSet.getString(EasyMock.eq(JdbcStoreConstants.COL_FEAT_UID))).andReturn("FEATURE_UID");
        EasyMock.expect(resultSet.getInt(EasyMock.eq(JdbcStoreConstants.COL_FEAT_ENABLE))).andReturn(1);
        EasyMock.expect(resultSet.getString(EasyMock.eq(JdbcStoreConstants.COL_FEAT_DESCRIPTION))).andReturn("DESCRIPTION");
        EasyMock.expect(resultSet.getString(EasyMock.eq(JdbcStoreConstants.COL_FEAT_GROUPNAME))).andReturn("GROUPNAME");
        EasyMock.expect(resultSet.getString(EasyMock.eq(JdbcStoreConstants.COL_FEAT_STRATEGY))).andReturn("");

        EasyMock.replay(resultSet);

        Feature feature = featureRowMapper.mapRow(resultSet,0);
        assertTrue("Feature should be enabled",feature.isEnable());
        assertEquals("Feature should have a uid 'FEATURE_UID'",feature.getUid(),"FEATURE_UID");
        assertEquals("Feature should have a description 'DESCRIPTION'",feature.getDescription(),"DESCRIPTION");
        assertEquals("Feature should have a group 'GROUPNAME'",feature.getGroup(),"GROUPNAME");
        assertNull("Feature should have a null flippingStrategy",feature.getFlippingStrategy());

        EasyMock.verify(resultSet);
    }

    @Test
    public void testMapRowExpressionFlipStrategy() throws SQLException {
        EasyMock.expect(resultSet.getString(EasyMock.eq(JdbcStoreConstants.COL_FEAT_UID))).andReturn("FEATURE_UID");
        EasyMock.expect(resultSet.getInt(EasyMock.eq(JdbcStoreConstants.COL_FEAT_ENABLE))).andReturn(1);
        EasyMock.expect(resultSet.getString(EasyMock.eq(JdbcStoreConstants.COL_FEAT_DESCRIPTION))).andReturn("DESCRIPTION");
        EasyMock.expect(resultSet.getString(EasyMock.eq(JdbcStoreConstants.COL_FEAT_GROUPNAME))).andReturn("GROUPNAME");
        EasyMock.expect(resultSet.getString(EasyMock.eq(JdbcStoreConstants.COL_FEAT_STRATEGY))).andReturn("org.ff4j.strategy.el.ExpressionFlipStrategy");
        EasyMock.expect(resultSet.getString(EasyMock.eq(JdbcStoreConstants.COL_FEAT_EXPRESSION))).andReturn("expression=value");

        EasyMock.replay(resultSet);

        Feature feature = featureRowMapper.mapRow(resultSet,0);
        assertTrue("Feature should be enabled",feature.isEnable());
        assertEquals("Feature should have a uid 'FEATURE_UID'",feature.getUid(),"FEATURE_UID");
        assertEquals("Feature should have a description 'DESCRIPTION'",feature.getDescription(),"DESCRIPTION");
        assertEquals("Feature should have a group 'GROUPNAME'",feature.getGroup(),"GROUPNAME");
        assertEquals("Feature should have an ExpressionFlipStrategy",feature.getFlippingStrategy().getClass(), ExpressionFlipStrategy.class);

        EasyMock.verify(resultSet);
    }

    @Test(expected = FeatureAccessException.class)
    public void testMapRowClassNotFound() throws SQLException {
        EasyMock.expect(resultSet.getString(EasyMock.eq(JdbcStoreConstants.COL_FEAT_UID))).andReturn("FEATURE_UID");
        EasyMock.expect(resultSet.getInt(EasyMock.eq(JdbcStoreConstants.COL_FEAT_ENABLE))).andReturn(1);
        EasyMock.expect(resultSet.getString(EasyMock.eq(JdbcStoreConstants.COL_FEAT_DESCRIPTION))).andReturn("DESCRIPTION");
        EasyMock.expect(resultSet.getString(EasyMock.eq(JdbcStoreConstants.COL_FEAT_GROUPNAME))).andReturn("GROUPNAME");
        EasyMock.expect(resultSet.getString(EasyMock.eq(JdbcStoreConstants.COL_FEAT_STRATEGY))).andReturn("org.cateproject.ClassDoesNotExist");

        EasyMock.replay(resultSet);

        featureRowMapper.mapRow(resultSet,0);

    }

    @Test(expected = FeatureAccessException.class)
    public void testMapRowNoVisibleConstructor() throws SQLException {
        EasyMock.expect(resultSet.getString(EasyMock.eq(JdbcStoreConstants.COL_FEAT_UID))).andReturn("FEATURE_UID");
        EasyMock.expect(resultSet.getInt(EasyMock.eq(JdbcStoreConstants.COL_FEAT_ENABLE))).andReturn(1);
        EasyMock.expect(resultSet.getString(EasyMock.eq(JdbcStoreConstants.COL_FEAT_DESCRIPTION))).andReturn("DESCRIPTION");
        EasyMock.expect(resultSet.getString(EasyMock.eq(JdbcStoreConstants.COL_FEAT_GROUPNAME))).andReturn("GROUPNAME");
        EasyMock.expect(resultSet.getString(EasyMock.eq(JdbcStoreConstants.COL_FEAT_STRATEGY))).andReturn("org.cateproject.features.rowmapper.NoVisibleConstructorFlipStrategy");

        EasyMock.replay(resultSet);

        featureRowMapper.mapRow(resultSet,0);

    }

    @Test(expected = FeatureAccessException.class)
    public void testMapRowNoDefaultConstructor() throws SQLException {
        EasyMock.expect(resultSet.getString(EasyMock.eq(JdbcStoreConstants.COL_FEAT_UID))).andReturn("FEATURE_UID");
        EasyMock.expect(resultSet.getInt(EasyMock.eq(JdbcStoreConstants.COL_FEAT_ENABLE))).andReturn(1);
        EasyMock.expect(resultSet.getString(EasyMock.eq(JdbcStoreConstants.COL_FEAT_DESCRIPTION))).andReturn("DESCRIPTION");
        EasyMock.expect(resultSet.getString(EasyMock.eq(JdbcStoreConstants.COL_FEAT_GROUPNAME))).andReturn("GROUPNAME");
        EasyMock.expect(resultSet.getString(EasyMock.eq(JdbcStoreConstants.COL_FEAT_STRATEGY))).andReturn("org.cateproject.features.rowmapper.NoDefaultConstructorFlipStrategy");

        EasyMock.replay(resultSet);

        featureRowMapper.mapRow(resultSet,0);

    }
}
