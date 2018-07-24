package org.cateproject.domain.convert.sync;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class DateTimeConverter implements Converter {

    private DateTimeFormatter dateTimeParser = ISODateTimeFormat.dateOptionalTimeParser().withZone(DateTimeZone.UTC);

    private DateTimeFormatter dateTimePrinter = ISODateTimeFormat.dateTime().withZone(DateTimeZone.UTC);

    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        DateTime dateTime = (DateTime) source;
        writer.setValue(dateTimePrinter.print(dateTime)); 
    } 

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        return dateTimeParser.parseDateTime(reader.getValue());
    }

    public boolean canConvert(Class type) {
        return type.equals(DateTime.class);
    }
}
