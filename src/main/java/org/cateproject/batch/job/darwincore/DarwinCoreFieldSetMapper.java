package org.cateproject.batch.job.darwincore;

import java.util.Map;

import org.cateproject.domain.batch.TermFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.ConversionService;

/**
 *
 * @author ben
 * @param <T> the type of object which this class maps
 */
public abstract class DarwinCoreFieldSetMapper<T> implements FieldSetMapper<T> {

    private Logger logger = LoggerFactory.getLogger(DarwinCoreFieldSetMapper.class);
            
    private String[] fieldNames;

    private Map<String, String> defaultValues;

    protected Class<T> type;
   
    @Autowired 
    protected ConversionService conversionService;
   
    @Autowired 
    protected TermFactory termFactory;

   /**
    *
    * @param object the object to map onto
    * @param fieldName the name of the field
    * @param value the value to map
    * @throws BindException if there is a problem mapping
    *         the value to the object
    */
    public abstract void mapField(T object, String fieldName,
            String value) throws ConversionFailedException;

    public void setConversionService(ConversionService conversionService) {
	this.conversionService = conversionService;
    }

    public void setTermFactory(TermFactory termFactory) {
        this.termFactory = termFactory;
    }

    public void setFieldNames(String[] newFieldNames) {
        this.fieldNames = newFieldNames;
    }

    public void setDefaultValues(
            Map<String,String> newDefaultValues) {
		this.defaultValues = newDefaultValues;
    }

    public DarwinCoreFieldSetMapper(Class<T> newType) {
        this.type = newType;
    }

    public T mapFieldSet(FieldSet fieldSet) throws BindException {
        T t;
        try {
            t = type.newInstance();
        } catch (InstantiationException ie) {
            BindException be = new BindException(null, "target");
            be.reject("could not instantiate", ie.getMessage());
            throw be;
        } catch (IllegalAccessException iae) {
            BindException be = new BindException(null, "target");
            be.reject("could not instantiate", iae.getMessage());
            throw be;
        }

        logger.debug("Mapping object " + t);
        FieldSetMapperException fsme = new FieldSetMapperException();
        for (int i = 0; i < fieldNames.length; i++) {
            try {
                logger.debug("Mapping " + fieldNames[i] + " " + fieldSet.readString(i));
                mapField(t, fieldNames[i], fieldSet.readString(i));
            } catch(ConversionFailedException cfe) {
                logger.debug(fieldNames[i] + " has an error " + cfe.getMessage());
                fsme.addConversionException(fieldNames[i], cfe);
            }
        }

        for (String defaultTerm : defaultValues.keySet()) {
            mapField(t, defaultTerm, defaultValues.get(defaultTerm));
        }
        
        if(fsme.hasConversionExceptions()) {
            logger.debug("Object has conversion exceptions, throwing error");
       	    throw fsme;
        }
        logger.debug("Returning object " + t);
        return t;
    }

    public TermFactory getTermFactory() {
        return termFactory;
    }
}
