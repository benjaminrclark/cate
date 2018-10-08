package org.cateproject.batch.convert;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;

public class StringToJobParametersConverterTest {

    StringToJobParametersConverter stringToJobParametersConverter;

    StringToMapConverter stringToMapConverter;

    JobParameters jobParameters;

    Map<String, Object> map;

    @Before
    public void setUp() {
        stringToJobParametersConverter = new StringToJobParametersConverter();

        stringToMapConverter = EasyMock.createMock(StringToMapConverter.class);
        stringToJobParametersConverter.setStringToMapConverter(stringToMapConverter);
   
        Date date = new Date(); 
        map = new HashMap<String,Object>();
        map.put("KEY_1", "VALUE_1");
        map.put("KEY_2", new Long(1));
        map.put("KEY_3", new Double(1.0));
        map.put("KEY_4", date);

        Map<String, JobParameter> params = new HashMap<String, JobParameter>();
        params.put("KEY_1", new JobParameter("VALUE_1"));
        params.put("KEY_2", new JobParameter(new Long(1)));
        params.put("KEY_3", new JobParameter(new Double(1)));
        params.put("KEY_4", new JobParameter(date));

        jobParameters = new JobParameters(params);
    }

    @Test
    public void testConvert() {
        EasyMock.expect(stringToMapConverter.convert(EasyMock.eq("value"))).andReturn(map);

        EasyMock.replay(stringToMapConverter);
        assertEquals("Convert should return the expected jobParameters", jobParameters, stringToJobParametersConverter.convert("value"));
        EasyMock.verify(stringToMapConverter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertIllegalArgument() {
        map.put("KEY_5", new Integer(1));
        EasyMock.expect(stringToMapConverter.convert(EasyMock.eq("value"))).andReturn(map);

        EasyMock.replay(stringToMapConverter);
        stringToJobParametersConverter.convert("value");
    }
}
