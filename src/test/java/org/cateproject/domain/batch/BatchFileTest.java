package org.cateproject.domain.batch;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsArrayContainingInOrder.arrayContaining;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class BatchFileTest {

        BatchFile batchFile;

        BatchDataset dataset;

        Set<BatchField> fields;
 
        Set<BatchLine> lines;

        private BatchField makeField(Integer index, boolean idField, String term, String defaultValue) {
            BatchField batchField = new BatchField();
            batchField.setIndex(index);
            batchField.setIdField(idField);
            batchField.setTerm(term);
            batchField.setDefaultValue(defaultValue);
            return batchField;
        } 

	@Before
	public void setUp() throws Exception {
            dataset = new BatchDataset();

            fields = new HashSet<BatchField>();
            
            fields.add(makeField(0, true, "http://rs.tdwg.org/dwc/terms/taxonID", null));
            fields.add(makeField(0, false, "http://rs.tdwg.org/dwc/terms/taxonID", null));
            fields.add(makeField(1, false, "http://rs.tdwg.org/dwc/terms/acceptedNameUsageID", "value"));
            fields.add(makeField(null, false, "http://purl.org/dc/terms/bibliographicCitation", "value"));
            lines = new HashSet<BatchLine>();

            batchFile = new BatchFile();
            batchFile.setId(1L);
            batchFile.setVersion(1);
            batchFile.setLocation("location");
            batchFile.setCore(false);
            batchFile.setDataset(dataset);
            batchFile.setFields(fields);
            batchFile.setLines(lines);
            batchFile.setDateFormat("dateFormat");
            batchFile.setEncoding("encoding");
            batchFile.setFieldsEnclosedBy('c');
            batchFile.setFieldsTerminatedBy("fieldsTerminatedBy");
            batchFile.setIgnoreHeaderLines(0);
            batchFile.setLinesTerminatedBy("linesTerminatedBy");
            batchFile.setRowType("rowType");
        }

	@Test
	public void testGetId() {
		assertEquals("id should equal 1", new Long(1), batchFile.getId());
	}

	@Test
	public void testGetVersion() {
		assertEquals("version should equal 1", new Integer(1), batchFile.getVersion());
	}

	@Test
	public void testGetLocation() {
		assertEquals("location should equal 'location'", "location", batchFile.getLocation());
	}

	@Test
	public void testIsCore() {
		assertFalse("core should be false", batchFile.isCore());
	}

	@Test
	public void testGetDataset() {
		assertEquals("dataset should equal dataset", dataset, batchFile.getDataset());
	}

	@Test
	public void testGetFields() {
		assertEquals("fields should equal fields", fields, batchFile.getFields());
	}

	@Test
	public void testGetLines() {
		assertEquals("lines should equal lines", lines, batchFile.getLines());
	}

	@Test
	public void testGetDateFormat() {
		assertEquals("dateFormat should equal 'dateFormat'", "dateFormat", batchFile.getDateFormat());
	}

	@Test
	public void testGetEncoding() {
		assertEquals("encoding should equal 'encoding'", "encoding", batchFile.getEncoding());
	}

	@Test
	public void testGetFieldsEnclosedBy() {
		assertEquals("fieldsEnclosedBy should equal 'c'", new Character('c'), batchFile.getFieldsEnclosedBy());
	}

	@Test
	public void testGetFieldsTerminatedBy() {
		assertEquals("fieldsTerminatedBy should equal 'fieldsTerminatedBy'", "fieldsTerminatedBy", batchFile.getFieldsTerminatedBy());
	}

	@Test
	public void testGetIgnoreHeaderLines() {
		assertEquals("ignoreHeaderLines should equal 0", new Integer(0), batchFile.getIgnoreHeaderLines());
	}

	@Test
	public void testGetLinesTerminatedBy() {
		assertEquals("linesTerminatedBy should equal 'linesTerminatedBy'", "linesTerminatedBy", batchFile.getLinesTerminatedBy());
	}

	@Test
	public void testGetRowType() {
		assertEquals("rowType should equal 'rowType'", "rowType", batchFile.getRowType());
	}

        @Test
        public void testGetFieldNames() {
            assertThat("getFieldNames should return the expected field names", 
                       batchFile.getFieldNames(2),
                       arrayContaining(
                         "http://rs.tdwg.org/dwc/terms/taxonID",
                         "http://rs.tdwg.org/dwc/terms/acceptedNameUsageID"));
        }

        @Test
        public void testGetFieldNamesWithMissingColumns() {
            assertThat("getFieldNames should return the expected field names", 
                       batchFile.getFieldNames(3),
                       arrayContaining(
                         "http://rs.tdwg.org/dwc/terms/taxonID",
                         "http://rs.tdwg.org/dwc/terms/acceptedNameUsageID",
                         ""));
        }

        @Test
        public void testGetDefaultValues() {
            Map<String, String> expectedValues = new HashMap<String, String>();
            expectedValues.put("http://purl.org/dc/terms/bibliographicCitation", "value");
            assertEquals("getDefaultValues should return the expected default values", expectedValues, batchFile.getDefaultValues());
        }

        @Test
        public void testCompareTo() {
            BatchFile other = new BatchFile();
            other.setLocation("otherLocation");
            assertTrue("compareTo should return a number greater than zero",  0 > batchFile.compareTo(other));
        }
}
