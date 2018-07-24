package org.cateproject.batch.job.darwincore.reference;

import static org.junit.Assert.*;

import java.util.Locale;

import org.cateproject.domain.Reference;
import org.cateproject.domain.constants.ReferenceType;
import org.cateproject.domain.batch.TermFactory;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.GbifTerm;
import org.springframework.core.convert.ConversionService;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

public class FieldSetMapperTest {

    FieldSetMapper fieldSetMapper;

    ConversionService conversionService;

    TermFactory termFactory;

    Reference reference;

    @Before
    public void setUp() throws Exception {
        fieldSetMapper = new FieldSetMapper();

        conversionService = EasyMock.createMock(ConversionService.class);
        termFactory = EasyMock.createMock(TermFactory.class);

        fieldSetMapper.setConversionService(conversionService);
        fieldSetMapper.setTermFactory(termFactory);
        reference = new Reference();
    }

    @Test
    public void testMapFieldBibliographicCitation() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DcTerm.bibliographicCitation).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(reference, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", "value", reference.getBibliographicCitation());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldDate() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DcTerm.date).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(reference, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", "value", reference.getDate());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldDescription() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DcTerm.description).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(reference, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", "value", reference.getDescription());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldIdentifier() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DcTerm.identifier).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(reference, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", "value", reference.getIdentifier());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldLocale() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DcTerm.language).anyTimes(); 
        EasyMock.expect(conversionService.convert(EasyMock.eq("value"), EasyMock.eq(Locale.class))).andReturn(Locale.ENGLISH);
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(reference, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", Locale.ENGLISH, reference.getLanguage());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldSource() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DcTerm.source).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(reference, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", "value", reference.getSource());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldSubject() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DcTerm.subject).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(reference, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", "value", reference.getSubject());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldTitle() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DcTerm.title).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(reference, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", "value", reference.getTitle());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldReferenceType() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DcTerm.type).anyTimes(); 
        EasyMock.expect(conversionService.convert(EasyMock.eq("value"), EasyMock.eq(ReferenceType.class))).andReturn(ReferenceType.publication);
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(reference, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", ReferenceType.publication, reference.getType());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapOtherDcTerm() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DcTerm.contributor).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(reference, "field", "value");
        assertNull("fieldSetMapper should map correct value to the correct field", reference.getType());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldTaxonRemarks() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.taxonRemarks).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(reference, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", "value", reference.getTaxonRemarks());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapOtherDwcTerm() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.taxonID).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(reference, "field", "value");
        assertNull("fieldSetMapper should map correct value to the correct field", reference.getTaxonRemarks());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapOtherTerm() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(GbifTerm.elevation).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(reference, "field", "value");
        assertNull("fieldSetMapper should map correct value to the correct field", reference.getTaxonRemarks());
        EasyMock.verify(conversionService, termFactory);
    } 
}
