package org.cateproject.web.format;

import java.util.HashSet;
import java.util.Set;

import org.cateproject.web.format.annotation.FilterQueryFormat;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

public class FilterQueryAnnotationFormatterFactory implements AnnotationFormatterFactory<FilterQueryFormat> {

    private static final Set<Class<?>> FIELD_TYPES = new HashSet<Class<?>>();

    static {
        FIELD_TYPES.add(FilterQuery.class);
    }

    public final Set<Class<?>> getFieldTypes() {
        return FIELD_TYPES;
    }

    public final Parser<?> getParser(final FilterQueryFormat facetRequestFormat, final Class<?> fieldType) {
        return new FilterQueryFormatter();
    }

    public final Printer<?> getPrinter(final FilterQueryFormat arg0, final Class<?> fieldType) {
        return new FilterQueryFormatter();
    }
}
