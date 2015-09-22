package org.cateproject.web.format;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.format.Formatter;

public class FilterQueryFormatter implements Formatter<FilterQuery> {

    public String print(FilterQuery filterQuery, Locale locale) {
        return filterQuery.toString();
    }

    public FilterQuery parse(String filterQuery, Locale locale) throws ParseException {
        if (-1 == filterQuery.indexOf(":")) {
	    throw new ParseException(filterQuery + " is not a valid facet request", 0);
	} else {
	    String fieldName = filterQuery.substring(0, filterQuery.indexOf(":"));
	    String value = filterQuery.substring(filterQuery.indexOf(":") + 1);
	    SimpleFilterQuery result = new SimpleFilterQuery();
	    result.addCriteria(Criteria.where(fieldName).expression(value));
	    return result;
	}
    }
}
