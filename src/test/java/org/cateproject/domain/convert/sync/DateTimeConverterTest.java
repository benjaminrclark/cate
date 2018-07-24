package org.cateproject.domain.convert.sync;

import static org.junit.Assert.*;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

public class DateTimeConverterTest {

    DateTimeConverter dateTimeConverter;

    @Before
    public void setUp() throws Exception {
        dateTimeConverter = new DateTimeConverter();
    }

    @Test
    public void testCanConvert() {
        assertTrue("dateTimeConverter can convert DateTime", dateTimeConverter.canConvert(DateTime.class));
        assertFalse("dateTimeConverter cannot convert other classes", dateTimeConverter.canConvert(String.class));
    }
    
    @Test
    public void testMarshal() {
        HierarchicalStreamWriter writer = EasyMock.createMock(HierarchicalStreamWriter.class);
        MarshallingContext context = EasyMock.createMock(MarshallingContext.class);
        DateTime dateTime = new DateTime(2018,7,20,16,10,39, DateTimeZone.UTC);

        writer.setValue(EasyMock.eq("2018-07-20T16:10:39.000Z"));

        EasyMock.replay(writer, context);
        dateTimeConverter.marshal(dateTime, writer, context);
        EasyMock.verify(writer, context);
    }

    @Test
    public void testUnmarshal() {
        HierarchicalStreamReader reader = EasyMock.createMock(HierarchicalStreamReader.class);
        UnmarshallingContext context = EasyMock.createMock(UnmarshallingContext.class);
        DateTime dateTime = new DateTime(2018,7,20,16,10,39, DateTimeZone.UTC);

        EasyMock.expect(reader.getValue()).andReturn("2018-07-20T16:10:39.000Z");

        EasyMock.replay(reader, context);
        assertEquals("unmarshl should return the expected dateTime", dateTime, dateTimeConverter.unmarshal(reader, context));
        EasyMock.verify(reader, context);
    }
}
