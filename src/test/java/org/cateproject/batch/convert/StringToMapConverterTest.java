package org.cateproject.batch.convert;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class StringToMapConverterTest {

    private StringToMapConverter stringToMapConverter;

    @Before
    public void setUp() {
        stringToMapConverter = new StringToMapConverter();
    }

    @Test
    public void testConvert() {
        Map<String,Object> expectedMap = new HashMap<String,Object>();
        expectedMap.put("KEY_1", "VALUE_1");
        expectedMap.put("KEY_2", true);

        assertEquals("Convert should return the expected map", expectedMap, stringToMapConverter.convert("[KEY_1:VALUE_1,KEY_2_bool:true]"));
    }

    @Test
    public void testConvertEmptyMap() {
        Map<String,Object> expectedMap = new HashMap<String,Object>();

        assertEquals("Convert should return the expected map", expectedMap, stringToMapConverter.convert("[]"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertInvalidBeginningChar() {
        stringToMapConverter.convert("KEY_1:VALUE_1,KEY_2_bool:true]");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertInvalidEndChar() {
        stringToMapConverter.convert("[KEY_1:VALUE_1,KEY_2_bool:true");
    }
}
