package org.cateproject.batch.convert;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class MapToStringConverterTest {

    private MapToStringConverter mapToStringConverter;

    @Before
    public void setUp() {
        mapToStringConverter = new MapToStringConverter();
    }

    @Test
    public void testConvert() {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("KEY_1", "VALUE_1");
        map.put("KEY_2", true);

        assertEquals("Convert should return the expected string", "[KEY_1:VALUE_1,KEY_2_bool:true]", mapToStringConverter.convert(map));
    } 
}
