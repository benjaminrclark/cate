package org.cateproject.domain;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.gbif.ecat.voc.NomenclaturalCode;
import org.gbif.ecat.voc.NomenclaturalStatus;
import org.gbif.ecat.voc.Rank;
import org.gbif.ecat.voc.TaxonomicStatus;
import org.junit.Before;
import org.junit.Test;

public class TaxonTest {
	
	private Taxon taxon;
	
	private List<Taxon> taxa;
	
	private List<Taxon> higherTaxa;
	
	private Set<Taxon> children;
	
	private Set<Taxon> synonyms;
	
	private Taxon parentNameUsage;
	
	private Taxon acceptedNameUsage;
	
	private Taxon originalNameUsage;
	
	private Set<Description> descriptions;
	
	private List<Multimedia> multimedia;
	
	private Set<Distribution> distribution;
	
	private List<Reference> references;
	
	private List<TypeAndSpecimen> typesAndSpecimens;
	
	private List<Term> terms;
	
	private Set<MeasurementOrFact> measurementsOrFacts;
	
	private Set<VernacularName> vernacularNames;
	
	private Reference namePublishedIn;
	
	private Reference nameAccordingTo;

	@Before
	public void setUp() throws Exception {
		higherTaxa = new ArrayList<Taxon>();
		children = new HashSet<Taxon>();
		synonyms = new HashSet<Taxon>();
		taxa = new ArrayList<Taxon>();
		parentNameUsage = new Taxon();
		acceptedNameUsage = new Taxon();
		originalNameUsage = new Taxon();
		descriptions = new HashSet<Description>();
		multimedia = new ArrayList<Multimedia>();
		distribution = new HashSet<Distribution>();
		references = new ArrayList<Reference>();
		typesAndSpecimens = new ArrayList<TypeAndSpecimen>();
		terms = new ArrayList<Term>();
		measurementsOrFacts = new HashSet<MeasurementOrFact>();
		vernacularNames = new HashSet<VernacularName>();
		namePublishedIn = new Reference();
		nameAccordingTo = new Reference();
		
		taxon = new Taxon();
		taxon.setHigherTaxa(higherTaxa);
		taxon.setChildren(children);
		taxon.setSynonyms(synonyms);
		taxon.setParentNameUsage(parentNameUsage);
		taxon.setAcceptedNameUsage(acceptedNameUsage);
		taxon.setOriginalNameUsage(originalNameUsage);
		taxon.setDescriptions(descriptions);
		taxon.setMultimedia(multimedia);
		taxon.setDistribution(distribution);
		taxon.setReferences(references);
		taxon.setTypesAndSpecimens(typesAndSpecimens);
		taxon.setTerms(terms);
		taxon.setMeasurementsOrFacts(measurementsOrFacts);
		taxon.setVernacularNames(vernacularNames);
		taxon.setNamePublishedIn(namePublishedIn);
		taxon.setNameAccordingTo(nameAccordingTo);
		taxon.setIdentifier("IDENTIFIER");
		taxon.setScientificNameID("SCIENTIFIC_NAME_ID");
		taxon.setScientificName("SCIENTIFIC_NAME");
		taxon.setNamePublishedInYear(1);
		taxon.setHigherClassification("HIGHER_CLASSIFICATION");
		taxon.setKingdom("KINGDOM");
		taxon.setPhylum("PHYLUM");
		taxon.setClazz("CLAZZ");
		taxon.setOrder("ORDER");	
		taxon.setFamily("FAMILY");
		taxon.setGenus("GENUS");
		taxon.setSubgenus("SUBGENUS");
		taxon.setSpecificEpithet("SPECIFIC_EPITHET");
		taxon.setInfraspecificEpithet("INFRASPECIFIC_EPITHET");
		taxon.setTaxonRank(Rank.SPECIES);
		taxon.setVerbatimTaxonRank("VERBATIM_TAXON_RANK");
		taxon.setScientificNameAuthorship("SCIENTIFIC_NAME_AUTHORSHIP");
		taxon.setNomenclaturalCode(NomenclaturalCode.Botanical);
		taxon.setTaxonomicStatus(TaxonomicStatus.Accepted);
		taxon.setTaxonRemarks("TAXON_REMARKS");
		taxon.setBibliographicCitation("BIBLIOGRAPHIC_CITATION");
		taxon.setNamePublishedInString("NAME_PUBLISHED_IN_STRING");
		taxon.setNomenclaturalStatus(NomenclaturalStatus.Valid);
	}

	@Test
	public void testGetIdentifier() {
		assertEquals("identifier should equal 'IDENTIFIER'","IDENTIFIER", taxon.getIdentifier());
	}

	@Test
	public void testGetHigherTaxa() {
		assertArrayEquals("higherTaxa should equal higherTaxa",higherTaxa.toArray(), taxon.getHigherTaxa().toArray());
	}
	
	@Test
	public void testGetHigherTaxaWhenNotSet() {
		higherTaxa.add(parentNameUsage);
		higherTaxa.add(taxon);
		taxon.setHigherTaxa(null);
		assertArrayEquals("higherTaxa should equal higherTaxa",higherTaxa.toArray(), taxon.getHigherTaxa().toArray());
	}

	@Test
	public void testGetTaxonId() {
		assertEquals("taxonId should equal 'IDENTIFIER'","IDENTIFIER", taxon.getTaxonId());
	}

	@Test
	public void testGetScientificNameID() {
		assertEquals("scientificNameId should equal 'SCIENTIFIC_NAME_ID'","SCIENTIFIC_NAME_ID", taxon.getScientificNameID());
	}

	@Test
	public void testGetNamePublishedInYear() {
		assertEquals("namePublishedInYear should equal 'NAME_PUBLISHED_IN_YEAR'",new Integer(1), taxon.getNamePublishedInYear());
	}

	@Test
	public void testGetHigherClassification() {
		assertEquals("higherClassification should equal 'HIGHER_CLASSIFICATION'","HIGHER_CLASSIFICATION", taxon.getHigherClassification());
	}

	@Test
	public void testGetKingdom() {
		assertEquals("kingdom should equal 'KINGDOM'","KINGDOM", taxon.getKingdom());
	}

	@Test
	public void testGetPhylum() {
		assertEquals("phylum should equal 'PHYLUM'","PHYLUM", taxon.getPhylum());
	}

	@Test
	public void testGetClazz() {
		assertEquals("clazz should equal 'CLAZZ'","CLAZZ", taxon.getClazz());
	}

	@Test
	public void testGetOrder() {
		assertEquals("order should equal 'ORDER'","ORDER", taxon.getOrder());
	}

	@Test
	public void testGetFamily() {
		assertEquals("family should equal 'FAMILY'","FAMILY", taxon.getFamily());
	}

	@Test
	public void testGetGenus() {
		assertEquals("genus should equal 'GENUS'","GENUS", taxon.getGenus());
	}

	@Test
	public void testGetSubgenus() {
		assertEquals("subgenus should equal 'SUBGENUS'","SUBGENUS", taxon.getSubgenus());
	}

	@Test
	public void testGetSpecificEpithet() {
		assertEquals("specificEpithet should equal 'SPECIFIC_EPITHET'","SPECIFIC_EPITHET", taxon.getSpecificEpithet());
	}

	@Test
	public void testGetInfraspecificEpithet() {
		assertEquals("infraspecificEpithet should equal 'INFRASPECIFIC_EPITHET'","INFRASPECIFIC_EPITHET", taxon.getInfraspecificEpithet());
	}

	@Test
	public void testGetTaxonRank() {
		assertEquals("taxonRank should equal Rank.SPECIES",Rank.SPECIES, taxon.getTaxonRank());
	}

	@Test
	public void testGetVerbatimTaxonRank() {
		assertEquals("verbatimTaxonRank should equal 'VERBATIM_TAXON_RANK'","VERBATIM_TAXON_RANK", taxon.getVerbatimTaxonRank());
	}

	@Test
	public void testGetScientificNameAuthorship() {
		assertEquals("scientificNameAuthorship should equal 'SCIENTIFIC_NAME_AUTHORSHIP'","SCIENTIFIC_NAME_AUTHORSHIP", taxon.getScientificNameAuthorship());
	}

	@Test
	public void testGetNomenclaturalCode() {
		assertEquals("nomenclaturalCode should equal NomenclaturalCode.Botanical",NomenclaturalCode.Botanical, taxon.getNomenclaturalCode());
	}

	@Test
	public void testGetTaxonomicStatus() {
		assertEquals("taxonomicStatus should equal TaxonomicStatus.Accepted",TaxonomicStatus.Accepted, taxon.getTaxonomicStatus());
	}

	@Test
	public void testGetTaxonRemarks() {
		assertEquals("taxonRemarks should equal 'TAXON_REMARKS'","TAXON_REMARKS", taxon.getTaxonRemarks());
	}

	@Test
	public void testGetBibliographicCitation() {
		assertEquals("bibliographicCitation should equal 'BIBLIOGRAPHIC_CITATION'","BIBLIOGRAPHIC_CITATION", taxon.getBibliographicCitation());
	}

	@Test
	public void testGetNamePublishedInString() {
		assertEquals("namePublishedInString should equal 'NAME_PUBLISHED_IN_STRING'","NAME_PUBLISHED_IN_STRING", taxon.getNamePublishedInString());
	}

	@Test
	public void testGetChildren() {
		assertEquals("children should equal children",children, taxon.getChildren());
	}

	@Test
	public void testGetParentNameUsage() {
		assertEquals("parentNameUsage should equal parentNameUsage",parentNameUsage, taxon.getParentNameUsage());
	}

	@Test
	public void testGetSynonyms() {
		assertEquals("synonyms should equal synonyms",synonyms, taxon.getSynonyms());
	}

	@Test
	public void testGetAcceptedNameUsage() {
		assertEquals("acceptedNameUsage should equal acceptedNameUsage",acceptedNameUsage, taxon.getAcceptedNameUsage());
	}

	@Test
	public void testGetOriginalNameUsage() {
		assertEquals("originalNameUsage should equal originalNameUsage",originalNameUsage, taxon.getOriginalNameUsage());
	}

	@Test
	public void testGetDescriptions() {
		assertEquals("descriptions should equal descriptions",descriptions, taxon.getDescriptions());
	}

	@Test
	public void testGetMultimedia() {
		assertEquals("multimedia should equal multimedia",multimedia, taxon.getMultimedia());
	}

	@Test
	public void testGetReferences() {
		assertEquals("references should equal references",references, taxon.getReferences());
	}

	@Test
	public void testGetTypesAndSpecimens() {
		assertEquals("typesAndSpecimens should equal typesAndSpecimens",typesAndSpecimens, taxon.getTypesAndSpecimens());
	}

	@Test
	public void testGetNamePublishedIn() {
		assertEquals("namePublishedIn should equal namePublishedIn",namePublishedIn, taxon.getNamePublishedIn());
	}

	@Test
	public void testGetNameAccordingTo() {
		assertEquals("nameAccordingTo should equal nameAccordingTo",nameAccordingTo, taxon.getNameAccordingTo());
	}

	@Test
	public void testGetDistribution() {
		assertEquals("distribution should equal distribution",distribution, taxon.getDistribution());
	}

	@Test
	public void testGetMeasurementsOrFacts() {
		assertEquals("measurementsOrFacts should equal measurementsOrFacts",measurementsOrFacts, taxon.getMeasurementsOrFacts());
	}

	@Test
	public void testGetScientificName() {
		assertEquals("scientificName should equal 'SCIENTIFIC_NAME'","SCIENTIFIC_NAME", taxon.getScientificName());
	}

	@Test
	public void testGetTerms() {
		assertEquals("terms should equal terms",terms, taxon.getTerms());
	}

	@Test
	public void testGetVernacularNames() {
		assertEquals("vernacularNames should equal vernacularNames",vernacularNames, taxon.getVernacularNames());
	}

	@Test
	public void testGetNomenclaturalStatus() {
		assertEquals("nomenclaturalStatus should equal NomenclaturalStatus.Valid",NomenclaturalStatus.Valid, taxon.getNomenclaturalStatus());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetTaxa() {
		taxon.getTaxa();
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testSetTaxa() {
		taxon.setTaxa(taxa);
	}

}
