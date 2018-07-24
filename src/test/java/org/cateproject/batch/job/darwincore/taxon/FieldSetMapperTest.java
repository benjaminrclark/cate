package org.cateproject.batch.job.darwincore.taxon;

import static org.junit.Assert.*;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import org.cateproject.domain.Reference;
import org.cateproject.domain.Taxon;
import org.cateproject.domain.batch.TermFactory;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.GbifTerm;
import org.gbif.ecat.voc.NomenclaturalCode;
import org.gbif.ecat.voc.NomenclaturalStatus;
import org.gbif.ecat.voc.TaxonomicStatus;
import org.gbif.ecat.voc.Rank;
import org.springframework.core.convert.ConversionService;

public class FieldSetMapperTest {

    FieldSetMapper fieldSetMapper;

    ConversionService conversionService;

    TermFactory termFactory;

    Taxon taxon;

    @Before
    public void setUp() throws Exception {
        fieldSetMapper = new FieldSetMapper();

        conversionService = EasyMock.createMock(ConversionService.class);
        termFactory = EasyMock.createMock(TermFactory.class);

        fieldSetMapper.setConversionService(conversionService);
        fieldSetMapper.setTermFactory(termFactory);
        taxon = new Taxon();
    }

    @Test
    public void testMapFieldBibliographicCitation() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DcTerm.bibliographicCitation).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(taxon, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", "value", taxon.getBibliographicCitation());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldDate() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DcTerm.date).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(taxon, "field", "value");
        assertNull("fieldSetMapper should map correct value to the correct field", taxon.getBibliographicCitation());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldAcceptedNameUsageIDIdentifier() {
        Taxon expectedTaxon = new Taxon();
        expectedTaxon.setTaxonId("value");

        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.acceptedNameUsageID).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(taxon, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", expectedTaxon, taxon.getAcceptedNameUsage());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldAcceptedNameUsageIDEmpty() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.acceptedNameUsageID).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(taxon, "field", " ");
        assertNull("fieldSetMapper should not map empty strings", taxon.getAcceptedNameUsage());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldAcceptedNameUsageIDNull() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.acceptedNameUsageID).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(taxon, "field", (String)null);
        assertNull("fieldSetMapper should not map null values", taxon.getAcceptedNameUsage());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldClass() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.class_).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(taxon, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", "value", taxon.getClazz());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldFamily() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.family).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(taxon, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", "value", taxon.getFamily());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldGenus() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.genus).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(taxon, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", "value", taxon.getGenus());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldHigherClassification() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.higherClassification).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(taxon, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", "value", taxon.getHigherClassification());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldInfraspecificEpithet() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.infraspecificEpithet).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(taxon, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", "value", taxon.getInfraspecificEpithet());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldKingdom() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.kingdom).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(taxon, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", "value", taxon.getKingdom());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldNameAccordingIDIdentifier() {
        Reference expectedReference = new Reference();
        expectedReference.setIdentifier("value");

        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.nameAccordingToID).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(taxon, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", expectedReference, taxon.getNameAccordingTo());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldNameAccordingToIDEmpty() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.nameAccordingToID).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(taxon, "field", " ");
        assertNull("fieldSetMapper should not map empty strings", taxon.getNameAccordingTo());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldNameAccordingToIDNull() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.nameAccordingToID).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(taxon, "field", (String)null);
        assertNull("fieldSetMapper should not map null values", taxon.getNameAccordingTo());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldNamePublishedIn() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.namePublishedIn).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(taxon, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", "value", taxon.getNamePublishedInString());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldNamePublishedInIDIdentifier() {
        Reference expectedReference = new Reference();
        expectedReference.setIdentifier("value");

        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.namePublishedInID).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(taxon, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", expectedReference, taxon.getNamePublishedIn());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldNamePublishedInIDEmpty() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.namePublishedInID).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(taxon, "field", " ");
        assertNull("fieldSetMapper should not map empty strings", taxon.getNamePublishedIn());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldNamePublishedInIDNull() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.namePublishedInID).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(taxon, "field", (String)null);
        assertNull("fieldSetMapper should not map null values", taxon.getNamePublishedIn());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldNamePublishedInYear() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.namePublishedInYear).anyTimes(); 
        EasyMock.expect(conversionService.convert(EasyMock.eq("value"), EasyMock.eq(Integer.class))).andReturn(new Integer(1));
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(taxon, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", new Integer(1), taxon.getNamePublishedInYear());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldNomenclauralCode() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.nomenclaturalCode).anyTimes(); 
        EasyMock.expect(conversionService.convert(EasyMock.eq("value"), EasyMock.eq(NomenclaturalCode.class))).andReturn(NomenclaturalCode.Botanical);
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(taxon, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", NomenclaturalCode.Botanical, taxon.getNomenclaturalCode());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldNomenclauralStatus() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.nomenclaturalStatus).anyTimes(); 
        EasyMock.expect(conversionService.convert(EasyMock.eq("value"), EasyMock.eq(NomenclaturalStatus.class))).andReturn(NomenclaturalStatus.Legitimate);
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(taxon, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", NomenclaturalStatus.Legitimate, taxon.getNomenclaturalStatus());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldTaxonomicStatus() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.taxonomicStatus).anyTimes(); 
        EasyMock.expect(conversionService.convert(EasyMock.eq("value"), EasyMock.eq(TaxonomicStatus.class))).andReturn(TaxonomicStatus.Accepted);
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(taxon, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", TaxonomicStatus.Accepted, taxon.getTaxonomicStatus());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldTaxonRank() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.taxonRank).anyTimes(); 
        EasyMock.expect(conversionService.convert(EasyMock.eq("value"), EasyMock.eq(Rank.class))).andReturn(Rank.SPECIES);
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(taxon, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", Rank.SPECIES, taxon.getTaxonRank());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldOriginalNameUsageIDIdentifier() {
        Taxon expectedTaxon = new Taxon();
        expectedTaxon.setTaxonId("value");

        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.originalNameUsageID).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(taxon, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", expectedTaxon, taxon.getOriginalNameUsage());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldOriginalNameUsageIDEmpty() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.originalNameUsageID).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(taxon, "field", " ");
        assertNull("fieldSetMapper should not map empty strings", taxon.getOriginalNameUsage());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldOriginalNameUsageIDNull() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.originalNameUsageID).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(taxon, "field", (String)null);
        assertNull("fieldSetMapper should not map null values", taxon.getAcceptedNameUsage());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldParentNameUsageIDIdentifier() {
        Taxon expectedTaxon = new Taxon();
        expectedTaxon.setTaxonId("value");

        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.parentNameUsageID).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(taxon, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", expectedTaxon, taxon.getParentNameUsage());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldParentNameUsageIDEmpty() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.parentNameUsageID).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(taxon, "field", " ");
        assertNull("fieldSetMapper should not map empty strings", taxon.getParentNameUsage());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldParentNameUsageIDNull() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.parentNameUsageID).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(taxon, "field", (String)null);
        assertNull("fieldSetMapper should not map null values", taxon.getAcceptedNameUsage());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldPhylum() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.phylum).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(taxon, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", "value", taxon.getPhylum());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldScientificName() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.scientificName).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(taxon, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", "value", taxon.getScientificName());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldScientificNameAuthorship() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.scientificNameAuthorship).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(taxon, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", "value", taxon.getScientificNameAuthorship());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldScientificNameID() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.scientificNameID).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(taxon, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", "value", taxon.getScientificNameID());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldSpecificEpithet() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.specificEpithet).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(taxon, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", "value", taxon.getSpecificEpithet());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldSubgenus() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.subgenus).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(taxon, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", "value", taxon.getSubgenus());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldTaxonID() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.taxonID).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(taxon, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", "value", taxon.getTaxonId());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldTaxonRemarks() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.taxonRemarks).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(taxon, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", "value", taxon.getTaxonRemarks());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldVerbatimTaxonRank() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.verbatimTaxonRank).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(taxon, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", "value", taxon.getVerbatimTaxonRank());
        EasyMock.verify(conversionService, termFactory);
    } 
}
