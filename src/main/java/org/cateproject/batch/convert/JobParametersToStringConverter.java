package org.cateproject.batch.convert;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.core.convert.converter.Converter;

public class JobParametersToStringConverter implements Converter<JobParameters,String>   {

     private MapToStringConverter mapToStringConverter = new MapToStringConverter();

     public void setMapToStringConverter(MapToStringConverter mapToStringConverter) {
         this.mapToStringConverter = mapToStringConverter;
     }

     public String convert(JobParameters jobParameters) {
        Map<String, JobParameter> parameters = jobParameters.getParameters();
        Map<String, Object> map = new HashMap<String, Object>();

        for(String name: parameters.keySet()) {
            JobParameter parameter = parameters.get(name);
            map.put(name, parameter.getValue());
        }

        return mapToStringConverter.convert(map);
     }
}
