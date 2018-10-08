package org.cateproject.batch.convert;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;

public class StringToJobParametersConverter implements Converter<String, JobParameters> {

    private StringToMapConverter stringToMapConverter = new StringToMapConverter();

    public void setStringToMapConverter(StringToMapConverter stringToMapConverter) {
        this.stringToMapConverter = stringToMapConverter;
    }

    public JobParameters convert(String values) {
        Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
        Map<String, Object> map = stringToMapConverter.convert(values);
      
        for(String name : map.keySet()) {
            Object object = map.get(name);
            if(object.getClass().equals(String.class)) {
                parameters.put(name, new JobParameter((String)object));
            } else if(object.getClass().equals(Long.class)) {
                parameters.put(name, new JobParameter((Long)object));
            } else if(object.getClass().equals(Double.class)) {
                parameters.put(name, new JobParameter((Double)object));
            }  else if(object.getClass().equals(Date.class)) {
                parameters.put(name, new JobParameter((Date)object));
            } else {
                throw new IllegalArgumentException("Cannot convert objects of class " + object.getClass().getName() + " to JobParameters");
            }
        }
        return new JobParameters(parameters);
    }
}
