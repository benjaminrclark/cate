package org.cateproject.web.format;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class FilterQueryAnnotationFormatterFactoryTest {
  
    private FilterQueryAnnotationFormatterFactory filterQueryAnnotationFormatterFactory;

    @Before
    public void setUp() {
        filterQueryAnnotationFormatterFactory = new FilterQueryAnnotationFormatterFactory();
    }

    @Test
    public void testGetParser() {
        assertEquals("getParser should return a filterQueryFormatter",filterQueryAnnotationFormatterFactory.getParser(null, null).getClass(), FilterQueryFormatter.class);
    }

    @Test
    public void testGetPrinter() {
        assertEquals("getPrinter should return a filterQueryFormatter",filterQueryAnnotationFormatterFactory.getPrinter(null, null).getClass(), FilterQueryFormatter.class);
    }
}
