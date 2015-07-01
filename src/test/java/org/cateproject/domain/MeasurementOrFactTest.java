package org.cateproject.domain;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class MeasurementOrFactTest {
	
	private MeasurementOrFact measurementOrFact;
	
	private Term type;
	
	private Taxon taxon;

	@Before
	public void setUp() throws Exception {
		type = new Term();
		taxon = new Taxon();
		measurementOrFact = new MeasurementOrFact();
		measurementOrFact.setIdentifier("IDENTIFIER");
		measurementOrFact.setType(type);
		measurementOrFact.setTaxon(taxon);
		measurementOrFact.setAccuracy("ACCURACY");
		measurementOrFact.setUnit("UNIT");
		measurementOrFact.setDeterminedDate(new DateTime(2000, 1, 1, 1, 1, 1));
		measurementOrFact.setDeterminedBy("DETERMINED_BY");
		measurementOrFact.setRemarks("REMARKS");
		measurementOrFact.setValue("VALUE");
		measurementOrFact.setMethod("METHOD");
	}

	@Test
	public void testGetIdentifier() {
		assertEquals("identifier should equal 'IDENTIFIER'","IDENTIFIER", measurementOrFact.getIdentifier());
	}

	@Test
	public void testGetType() {
		assertEquals("type should equal type",type, measurementOrFact.getType());
	}

	@Test
	public void testGetTaxon() {
		assertEquals("taxon should equal taxon",taxon, measurementOrFact.getTaxon());
	}

	@Test
	public void testGetAccuracy() {
		assertEquals("accuracy should equal 'ACCURACY'","ACCURACY", measurementOrFact.getAccuracy());
	}

	@Test
	public void testGetUnit() {
		assertEquals("unit should equal 'UNIT'","UNIT", measurementOrFact.getUnit());
	}

	@Test
	public void testGetDeterminedDate() {
		assertEquals("determinedDate should equal 2000-01-01T01:01:01",new DateTime(2000, 1, 1, 1, 1, 1), measurementOrFact.getDeterminedDate());
	}

	@Test
	public void testGetDeterminedBy() {
		assertEquals("determinedBy should equal 'DETERMINED_BY'","DETERMINED_BY", measurementOrFact.getDeterminedBy());
	}

	@Test
	public void testGetMethod() {
		assertEquals("method should equal 'METHOD'","METHOD", measurementOrFact.getMethod());
	}

	@Test
	public void testGetRemarks() {
		assertEquals("remarks should equal 'REMARKS'","REMARKS", measurementOrFact.getRemarks());
	}

	@Test
	public void testGetValue() {
		assertEquals("value should equal 'VALUE'","VALUE", measurementOrFact.getValue());
	}

}
