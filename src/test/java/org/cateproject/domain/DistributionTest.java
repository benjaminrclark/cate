package org.cateproject.domain;

import static org.junit.Assert.*;

import org.cateproject.domain.constants.Location;
import org.gbif.ecat.voc.EstablishmentMeans;
import org.gbif.ecat.voc.LifeStage;
import org.gbif.ecat.voc.OccurrenceStatus;
import org.junit.Before;
import org.junit.Test;

public class DistributionTest {
	
	private Distribution distribution;
	
	private Taxon taxon;

	@Before
	public void setUp() throws Exception {
		taxon = new Taxon();
		distribution = new Distribution();
		distribution.setIdentifier("IDENTIFIER");
		distribution.setCountryCode("UK");
		distribution.setLocation(Location.EUROPE);
		distribution.setLocality("LOCALITY");
		distribution.setLifeStage(LifeStage.Adult);
		distribution.setOccurrenceStatus(OccurrenceStatus.Present);
		distribution.setOccurrenceRemarks("OCCURRENCE_REMARKS");
		distribution.setEstablishmentMeans(EstablishmentMeans.Native);
		distribution.setTaxon(taxon);
	}

	@Test
	public void testGetIdentifier() {
		assertEquals("identifier should equal 'IDENTIFIER'","IDENTIFIER", distribution.getIdentifier());
	}

	@Test
	public void testGetLocation() {
		assertEquals("location should equal Location.EUROPE",Location.EUROPE, distribution.getLocation());
	}

	@Test
	public void testGetLocality() {
		assertEquals("locality should equal 'LOCALITY'","LOCALITY", distribution.getLocality());
	}

	@Test
	public void testGetCountryCode() {
		assertEquals("countryCode should equal 'UK'","UK", distribution.getCountryCode());
	}

	@Test
	public void testGetLifeStage() {
		assertEquals("lifeStage should equal LifeStage.Adult",LifeStage.Adult, distribution.getLifeStage());
	}

	@Test
	public void testGetOccurrenceStatus() {
		assertEquals("occurrenceStatus should equal OccurrenceStatus.Present",OccurrenceStatus.Present, distribution.getOccurrenceStatus());
	}

	@Test
	public void testGetOccurrenceRemarks() {
		assertEquals("occurrenceRemarks should equal 'OCCURRENCE_REMARKS'","OCCURRENCE_REMARKS", distribution.getOccurrenceRemarks());
	}

	@Test
	public void testGetEstablishmentMeans() {
		assertEquals("establishmentMeans should equal EstablishmentMeans.Native",EstablishmentMeans.Native, distribution.getEstablishmentMeans());
	}

	@Test
	public void testGetTaxon() {
		assertEquals("taxon should equal taxon",taxon, distribution.getTaxon());
	}

}
