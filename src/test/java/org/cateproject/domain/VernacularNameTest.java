package org.cateproject.domain;

import static org.junit.Assert.*;

import java.util.Locale;

import org.cateproject.domain.constants.Location;
import org.cateproject.domain.constants.Sex;
import org.gbif.ecat.voc.LifeStage;
import org.junit.Before;
import org.junit.Test;

public class VernacularNameTest {
	
	private VernacularName vernacularName;
	
	private Taxon taxon;

	@Before
	public void setUp() throws Exception {
		taxon = new Taxon();
		vernacularName = new VernacularName();
		vernacularName.setTaxon(taxon);
		vernacularName.setIdentifier("IDENTIFIER");
		vernacularName.setVernacularName("VERNACULAR_NAME");
		vernacularName.setSource("SOURCE");
		vernacularName.setLang(Locale.ENGLISH);
		vernacularName.setTemporal("TEMPORAL");
		vernacularName.setLocation(Location.EUROPE);
		vernacularName.setLocality("LOCALITY");
		vernacularName.setCountryCode("UK");
		vernacularName.setSex(Sex.female);
		vernacularName.setPlural(false);
		vernacularName.setPreferredName(true);
		vernacularName.setOrganismPart("ORGANISM_PART");
		vernacularName.setTaxonRemarks("TAXON_REMARKS");
		vernacularName.setLifeStage(LifeStage.Adult);
	}

	@Test
	public void testGetIdentifier() {
		assertEquals("identifier should equal 'IDENTIFIER'","IDENTIFIER", vernacularName.getIdentifier());
	}

	@Test
	public void testGetVernacularName() {
		assertEquals("vernacularName should equal 'VERNACULAR_NAME'","VERNACULAR_NAME", vernacularName.getVernacularName());
	}

	@Test
	public void testGetSource() {
		assertEquals("source should equal 'SOURCE'","SOURCE", vernacularName.getSource());
	}

	@Test
	public void testGetLang() {
		assertEquals("lang should equal Locale.ENGLISH",Locale.ENGLISH, vernacularName.getLang());
	}

	@Test
	public void testGetTemporal() {
		assertEquals("temporal should equal 'TEMPORAL'","TEMPORAL", vernacularName.getTemporal());
	}

	@Test
	public void testGetLocation() {
		assertEquals("location should equal Location.EUROPE",Location.EUROPE, vernacularName.getLocation());
	}

	@Test
	public void testGetLocality() {
		assertEquals("locality should equal 'LOCALITY'","LOCALITY", vernacularName.getLocality());
	}

	@Test
	public void testGetCountryCode() {
		assertEquals("countryCode should equal 'UK'","UK", vernacularName.getCountryCode());
	}

	@Test
	public void testGetSex() {
		assertEquals("sex should equal Sex.female",Sex.female, vernacularName.getSex());
	}

	@Test
	public void testGetPlural() {
		assertFalse("plural should be false",vernacularName.getPlural());
	}

	@Test
	public void testGetPreferredName() {
		assertTrue("preferredName should be true",vernacularName.getPreferredName());
	}

	@Test
	public void testGetOrganismPart() {
		assertEquals("organismPart should equal 'ORGANISM_PART'","ORGANISM_PART", vernacularName.getOrganismPart());
	}

	@Test
	public void testGetTaxonRemarks() {
		assertEquals("taxonRemarks should equal 'TAXON_REMARKS'","TAXON_REMARKS", vernacularName.getTaxonRemarks());
	}

	@Test
	public void testGetLifeStage() {
		assertEquals("lifeStage should equal LifeStage.Adult",LifeStage.Adult, vernacularName.getLifeStage());
	}

	@Test
	public void testGetTaxon() {
		assertEquals("taxon should equal taxon",taxon, vernacularName.getTaxon());
	}

}
