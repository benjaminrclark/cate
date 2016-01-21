package org.cateproject.batch.convert;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

public class StringToMapConverter implements Converter<String, Map<String, Object>>{

    private static final Logger logger = LoggerFactory.getLogger(StringToMapConverter.class);

    public Map<String,Object> convert(String values) {
        logger.debug("Converting {}", new Object[]{values});
        if(values.startsWith("[") && values.endsWith("]")) {
            Map<String, Object> map = new HashMap<String, Object>();
            values = values.substring(1,values.length() - 1);
            if(!values.isEmpty()){
                for(String value : values.split(",")) {
                    logger.debug("Converting keyvalue {}", new Object[]{value});
                    String[] keyValue = value.split(":", 2);
                    String key = keyValue[0];
                    Object val = keyValue[1];
                    if(key.endsWith("_bool")) {
                        val = Boolean.parseBoolean(val.toString());
                        key = key.substring(0, key.length() - "_bool".length());
                    }
                    map.put(key,val);
                }
            }
            return map;
        } else {
            throw new IllegalArgumentException("String is not of the correct format");
        }
    }
}
