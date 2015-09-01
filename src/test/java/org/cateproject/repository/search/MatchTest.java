package org.cateproject.repository.search;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class MatchTest {

    private Match match;

    @Before
    public void setUp() {
        match = new Match("STRING", "STRING");
        match.setText("TEXT");
        match.setValue("VALUE");
    }

    @Test
    public void testGetText() {
        assertEquals("getText should return 'TEXT'", match.getText(), "TEXT");
    }

    @Test
    public void testGetValue() {
        assertEquals("getValue should return 'VALUE'", match.getValue(), "VALUE");
    }
}
