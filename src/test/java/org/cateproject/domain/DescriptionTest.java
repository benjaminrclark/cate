package org.cateproject.domain;

import static org.junit.Assert.*;

import java.util.Locale;

import org.cateproject.domain.constants.DescriptionType;
import org.junit.Before;
import org.junit.Test;

public class DescriptionTest {
	
	private Description description;
	
	private Taxon taxon;

	@Before
	public void setUp() throws Exception {
		taxon = new Taxon();
		description = new Description();
		description.setTaxon(taxon);
		description.setIdentifier("IDENTIFIER");
		description.setType(DescriptionType.general);
		description.setDescription("DESCRIPTION");
		description.setSource("SOURCE");
		description.setAudience("AUDIENCE");
		description.setLanguage(Locale.ENGLISH);
	}

	@Test
	public void testGetIdentifier() {
		assertEquals("identifier should equal 'IDENTIFIER'","IDENTIFIER", description.getIdentifier());
	}

	@Test
	public void testGetDescription() {
		assertEquals("description should equal 'DESCRIPTION'","DESCRIPTION", description.getDescription());
	}

	@Test
	public void testGetType() {
		assertEquals("type should equal DescriptionType.general",DescriptionType.general, description.getType());
	}

	@Test
	public void testGetSource() {
		assertEquals("source should equal 'SOURCE'","SOURCE", description.getSource());
	}

	@Test
	public void testGetAudience() {
		assertEquals("audience should equal 'AUDIENCE'","AUDIENCE", description.getAudience());
	}

	@Test
	public void testGetLanguage() {
		assertEquals("language should equal Locale.ENGLISH",Locale.ENGLISH, description.getLanguage());
	}

	@Test
	public void testGetTaxon() {
		assertEquals("taxon should equal taxon",taxon, description.getTaxon());
	}
}
