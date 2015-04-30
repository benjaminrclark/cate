package org.cateproject.domain;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.cateproject.domain.constants.Location;
import org.cateproject.domain.constants.Sex;
import org.cateproject.domain.constants.TypeDesignationType;
import org.cateproject.domain.constants.TypeStatus;
import org.gbif.ecat.voc.Rank;
import org.junit.Before;
import org.junit.Test;

public class TypeAndSpecimenTest {
	
	private TypeAndSpecimen typeAndSpecimen;
	
	private List<Taxon> taxa;

	@Before
	public void setUp() throws Exception {
		taxa = new ArrayList<Taxon>();
		typeAndSpecimen = new TypeAndSpecimen();
		typeAndSpecimen.setTaxa(taxa);
		typeAndSpecimen.setIdentifier("IDENTIFIER");
		typeAndSpecimen.setTypeStatus(TypeStatus.notatype);
		typeAndSpecimen.setTypeDesignationType(TypeDesignationType.originalDesignation);
		typeAndSpecimen.setTypeDesignatedBy("TYPE_DESIGNATED_BY");
		typeAndSpecimen.setScientificName("SCIENTIFIC_NAME");
		typeAndSpecimen.setTaxonRank(Rank.SPECIES);
		typeAndSpecimen.setBibliographicCitation("BIBLIOGRAPHIC_CITATION");
		typeAndSpecimen.setInstitutionCode("INSTITUTION_CODE");
		typeAndSpecimen.setCollectionCode("COLLECTION_CODE");
		typeAndSpecimen.setDecimalLatitude(1.0);
		typeAndSpecimen.setDecimalLongitude(1.0);
		typeAndSpecimen.setCatalogNumber("CATALOG_NUMBER");
		typeAndSpecimen.setLocality("LOCALITY");
		typeAndSpecimen.setLocation(Location.EUROPE);
		typeAndSpecimen.setSex(Sex.female);
		typeAndSpecimen.setRecordedBy("RECORDED_BY");
		typeAndSpecimen.setSource("SOURCE");
		typeAndSpecimen.setVerbatimEventDate("VERBATIM_EVENT_DATE");
		typeAndSpecimen.setVerbatimLatitude("VERBATIM_LATITUDE");
		typeAndSpecimen.setVerbatimLongitude("VERBATIM_LONGITUDE");
		typeAndSpecimen.setVerbatimLabel("VERBATIM_LABEL");
	}

	@Test
	public void testGetIdentifier() {
		assertEquals("identifier should equal 'IDENTIFIER'","IDENTIFIER", typeAndSpecimen.getIdentifier());
	}

	@Test
	public void testGetTypeStatus() {
		assertEquals("typeStatus should equal TypeStatus.notatype",TypeStatus.notatype, typeAndSpecimen.getTypeStatus());
	}

	@Test
	public void testGetTypeDesignationType() {
		assertEquals("typeDesignationType should equal TypeDesignationType.originalDesignation",TypeDesignationType.originalDesignation, typeAndSpecimen.getTypeDesignationType());
	}

	@Test
	public void testGetTypeDesignatedBy() {
		assertEquals("typeDesignatedBy should equal 'TYPE_DESIGNATED_BY'","TYPE_DESIGNATED_BY", typeAndSpecimen.getTypeDesignatedBy());
	}

	@Test
	public void testGetScientificName() {
		assertEquals("scientificName should equal 'SCIENTIFIC_NAME'","SCIENTIFIC_NAME", typeAndSpecimen.getScientificName());
	}

	@Test
	public void testGetTaxonRank() {
		assertEquals("taxonRank should equal Rank.SPECIES",Rank.SPECIES, typeAndSpecimen.getTaxonRank());
	}

	@Test
	public void testGetBibliographicCitation() {
		assertEquals("bibliographicCitation should equal 'BIBLIOGRAPHIC_CITATION'","BIBLIOGRAPHIC_CITATION", typeAndSpecimen.getBibliographicCitation());
	}

	@Test
	public void testGetOccurrenceId() {
		assertEquals("occurrenceId should equal 'IDENTIFIER'","IDENTIFIER", typeAndSpecimen.getOccurrenceId());
	}

	@Test
	public void testGetInstitutionCode() {
		assertEquals("institutionCode should equal 'INSTITUTION_CODE'","INSTITUTION_CODE", typeAndSpecimen.getInstitutionCode());
	}

	@Test
	public void testGetCollectionCode() {
		assertEquals("collectionCode should equal 'COLLECTION_CODE'","COLLECTION_CODE", typeAndSpecimen.getCollectionCode());
	}

	@Test
	public void testGetDecimalLatitude() {
		assertEquals("decimalLatitude should equal 1.0",new Double(1.0), typeAndSpecimen.getDecimalLatitude());
	}

	@Test
	public void testGetDecimalLongitude() {
		assertEquals("decimalLongitude should equal 1.0",new Double(1.0), typeAndSpecimen.getDecimalLongitude());
	}

	@Test
	public void testGetCatalogNumber() {
		assertEquals("catalogNumber should equal 'CATALOG_NUMBER'","CATALOG_NUMBER", typeAndSpecimen.getCatalogNumber());
	}

	@Test
	public void testGetLocality() {
		assertEquals("locality should equal 'LOCALITY'","LOCALITY", typeAndSpecimen.getLocality());
	}

	@Test
	public void testGetLocation() {
		assertEquals("location should equal Location.EUROPE",Location.EUROPE, typeAndSpecimen.getLocation());
	}

	@Test
	public void testGetSex() {
		assertEquals("sex should equal Sex.female",Sex.female, typeAndSpecimen.getSex());
	}

	@Test
	public void testGetRecordedBy() {
		assertEquals("recordedBy should equal 'RECORDED_BY'","RECORDED_BY", typeAndSpecimen.getRecordedBy());
	}

	@Test
	public void testGetSource() {
		assertEquals("source should equal 'SOURCE'","SOURCE", typeAndSpecimen.getSource());
	}

	@Test
	public void testGetVerbatimEventDate() {
		assertEquals("verbatimEventDate should equal 'VERBATIM_EVENT_DATE'","VERBATIM_EVENT_DATE", typeAndSpecimen.getVerbatimEventDate());
	}

	@Test
	public void testGetVerbatimLongitude() {
		assertEquals("verbatimLongitude should equal 'VERBATIM_LONGITUDE'","VERBATIM_LONGITUDE", typeAndSpecimen.getVerbatimLongitude());
	}

	@Test
	public void testGetVerbatimLatitude() {
		assertEquals("verbatimLatitude should equal 'VERBATIM_LATITUDE'","VERBATIM_LATITUDE", typeAndSpecimen.getVerbatimLatitude());
	}

	@Test
	public void testGetVerbatimLabel() {
		assertEquals("verbatimLabel should equal 'VERBATIM_LABEL'","VERBATIM_LABEL", typeAndSpecimen.getVerbatimLabel());
	}

	@Test
	public void testGetTaxa() {
		assertEquals("taxa should equal taxa",taxa, typeAndSpecimen.getTaxa());
	}

}
