package org.cateproject.batch.convert;

import java.util.Map;

import org.springframework.core.convert.converter.Converter;

public class MapToStringConverter implements Converter<Map<String,Object>,String>{
     public String convert(Map<String,Object> map) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        boolean first = true;
        for(String key : map.keySet()) {
            if(!first) {
                stringBuilder.append(",");
            } else {
                first = false;
            }
            Object value = map.get(key);
            if(value instanceof Boolean) {
                key = key + "_bool";
            }
            stringBuilder.append(key);
            stringBuilder.append(":");
            stringBuilder.append(value);
        }       
        stringBuilder.append("]");
        return stringBuilder.toString();
     }
}
