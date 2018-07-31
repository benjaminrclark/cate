package org.cateproject.domain.batch;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BatchFieldTest {

        BatchField batchField;

        BatchFile file;

	@Before
	public void setUp() throws Exception {
            file = new BatchFile();

            batchField = new BatchField();
            batchField.setId(1L);
            batchField.setVersion(1);
            batchField.setTerm("term");
            batchField.setIndex(1);
            batchField.setIdField(false);
            batchField.setDefaultValue("defaultValue");
            batchField.setFile(file);
        }

	@Test
	public void testGetId() {
		assertEquals("id should equal 1", new Long(1), batchField.getId());
	}

	@Test
	public void testGetVersion() {
		assertEquals("version should equal 1", new Integer(1), batchField.getVersion());
	}

	@Test
	public void testGetIndex() {
		assertEquals("index should equal 1", new Integer(1), batchField.getIndex());
	}

	@Test
	public void testGetFile() {
		assertEquals("file should equal file", file, batchField.getFile());
	}

	@Test
	public void testIsIdField() {
		assertFalse("idField should be false", batchField.isIdField());
	}

	@Test
	public void testGetTerm() {
		assertEquals("term should equal 'term'", "term", batchField.getTerm());
	}

	@Test
	public void testGetDefaultValue() {
		assertEquals("defaultValue should equal 'defaultValue'", "defaultValue", batchField.getDefaultValue());
	}

	@Test
	public void testCompareToOtherNull() {
                BatchField other = new BatchField();
		assertTrue("compareTo should equal 'defaultValue'", 0 > batchField.compareTo(other));
	}

	@Test
	public void testCompareToGreaterIndex() {
                BatchField other = new BatchField();
                other.setIndex(2);
		assertTrue("compareTo should equal 'defaultValue'", 0 > batchField.compareTo(other));
	}

	@Test
	public void testCompareToThisNull() {
                BatchField other = new BatchField();
                other.setIndex(1);
                batchField.setIndex(null);
		assertTrue("compareTo should equal 'defaultValue'", 0 < batchField.compareTo(other));
	}

	@Test
	public void testCompareToBothNull() {
                BatchField other = new BatchField();
                batchField.setIndex(null);
		assertTrue("compareTo should equal 'defaultValue'", 0 == batchField.compareTo(other));
	}
}
