package org.cateproject.web.admin;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class PropertiesDTOTest {
    
    private PropertiesDTO propertiesDTO;

    private Map<String,String> properties = new HashMap<String,String>();
 
    @Before
    public void setUp() {
        propertiesDTO = new PropertiesDTO();
        propertiesDTO.setProperties(properties);
    }

    @Test
    public void testGetProperties() {
        assertEquals("getProperties should return the expected properties", properties, propertiesDTO.getProperties());
    }
}
