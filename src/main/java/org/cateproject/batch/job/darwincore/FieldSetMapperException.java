package org.cateproject.batch.job.darwincore;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.convert.ConversionFailedException;

public class FieldSetMapperException extends RuntimeException {
	
    private Map<String,ConversionFailedException> conversionExceptions = new HashMap<String, ConversionFailedException>();

    public Map<String, ConversionFailedException> getConversionExceptions() {
        return conversionExceptions;
    }
	
    public void addConversionException(String field, ConversionFailedException cfe) {
        this.conversionExceptions.put(field, cfe);
    }
	
    public boolean hasConversionExceptions() {
        return !conversionExceptions.isEmpty();
    }
}
