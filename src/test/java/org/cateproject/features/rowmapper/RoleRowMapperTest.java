package org.cateproject.features.rowmapper;

import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.easymock.EasyMock;
import org.ff4j.store.JdbcStoreConstants;
import org.junit.Before;
import org.junit.Test;

public class RoleRowMapperTest {

    private RoleRowMapper roleRowMapper;

    private ResultSet resultSet;

    @Before
    public void setUp() {
        roleRowMapper = new RoleRowMapper();
 
        resultSet = EasyMock.createMock(ResultSet.class); 
    }

    @Test
    public void testMapRow() throws SQLException {
        EasyMock.expect(resultSet.getString(JdbcStoreConstants.COL_ROLE_FEATID)).andReturn("FEATID").times(2);
        EasyMock.expect(resultSet.getString(JdbcStoreConstants.COL_ROLE_ROLENAME)).andReturn("ROLE_1");
        EasyMock.expect(resultSet.getString(JdbcStoreConstants.COL_ROLE_ROLENAME)).andReturn("ROLE_2");

        Set<String> roleSet = new HashSet<String>();
        roleSet.add("ROLE_1");
        roleSet.add("ROLE_2");
        Map<String, Set<String>> roles = new HashMap<String, Set<String>>();
        roles.put("FEATID", roleSet);
        EasyMock.replay(resultSet);
        assertEquals("roleRowMapper.mapRow should return 1",roleRowMapper.mapRow(resultSet, 1), new Integer(1));
        assertEquals("roleRowMapper.mapRow should return 2",roleRowMapper.mapRow(resultSet, 2), new Integer(2));
        assertEquals("roleRowMapper.getRoles should return the expected roles", roleRowMapper.getRoles(), roles);
        EasyMock.verify(resultSet);
    }
}
