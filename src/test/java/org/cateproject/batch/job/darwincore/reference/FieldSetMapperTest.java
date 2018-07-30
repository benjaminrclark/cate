package org.cateproject.batch.job.darwincore.reference;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Locale;

import org.cateproject.domain.Dataset;
import org.cateproject.domain.Reference;
import org.cateproject.domain.constants.ReferenceType;
import org.cateproject.domain.batch.TermFactory;
import org.cateproject.batch.job.darwincore.FieldSetMapperException;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.GbifTerm;
import org.joda.time.DateTime;
import org.springframework.batch.item.file.transform.DefaultFieldSet;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.validation.BindException;

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
    public void testMapFieldReferenceAccessRights() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DcTerm.accessRights).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(reference, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", "value", reference.getAccessRights());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldCreated() {
        DateTime dateTime = new DateTime();
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DcTerm.created).anyTimes(); 
        EasyMock.expect(conversionService.convert(EasyMock.eq("value"), EasyMock.eq(DateTime.class))).andReturn(dateTime);
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(reference, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", dateTime, reference.getCreated());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldReferenceLicense() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DcTerm.license).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(reference, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", "value", reference.getLicense());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldModified() {
        DateTime dateTime = new DateTime();
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DcTerm.modified).anyTimes(); 
        EasyMock.expect(conversionService.convert(EasyMock.eq("value"), EasyMock.eq(DateTime.class))).andReturn(dateTime);
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(reference, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", dateTime, reference.getModified());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldReferenceRightsHolder() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DcTerm.rightsHolder).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(reference, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", "value", reference.getRightsHolder());
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
    public void testMapFieldDataset() {
        Dataset dataset = new Dataset();
        dataset.setIdentifier("value");
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.datasetID).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(reference, "field", "value");
        assertEquals("fieldSetMapper should map correct value to the correct field", dataset, reference.getDataset());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldDatasetNull() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.datasetID).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(reference, "field", null);
        assertNull("fieldSetMapper should map correct value to the correct field", reference.getDataset());
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldDatasetEmpty() {
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DwcTerm.datasetID).anyTimes(); 
        EasyMock.replay(conversionService, termFactory);
        fieldSetMapper.mapField(reference, "field", " ");
        assertNull("fieldSetMapper should map correct value to the correct field", reference.getDataset());
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

    @Test
    public void testMapFieldSet() throws Exception {
        Reference reference = new Reference();
        reference.setLanguage(Locale.ENGLISH);
        FieldSet fieldSet = new DefaultFieldSet(new String[] {"value"});
        fieldSetMapper.setFieldNames(new String[] {"field"});
        fieldSetMapper.setDefaultValues(new HashMap<String,String>());

        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DcTerm.language).anyTimes(); 
        EasyMock.expect(conversionService.convert(EasyMock.eq("value"), EasyMock.eq(Locale.class))).andReturn(Locale.ENGLISH); 
        EasyMock.replay(conversionService, termFactory);
        assertEquals("mapFieldSet should return the expected entity", reference,fieldSetMapper.mapFieldSet(fieldSet));
        EasyMock.verify(conversionService, termFactory);
    } 

    @Test
    public void testMapFieldSetConversionFailed() throws Exception {
        FieldSet fieldSet = new DefaultFieldSet(new String[] {"value"});
        fieldSetMapper.setFieldNames(new String[] {"field"});
        fieldSetMapper.setDefaultValues(new HashMap<String,String>());
        ConversionFailedException conversionFailedException = 
            new ConversionFailedException(TypeDescriptor.valueOf(String.class),
                                          TypeDescriptor.valueOf(Locale.class),
                                          "value",
                                          new Exception("exception"));

        EasyMock.expect(termFactory.findTerm(EasyMock.eq("field"))).andReturn(DcTerm.language).anyTimes(); 
        EasyMock.expect(conversionService.convert(EasyMock.eq("value"), EasyMock.eq(Locale.class))).andThrow(conversionFailedException); 
        EasyMock.replay(conversionService, termFactory);
        FieldSetMapperException thrownFieldSetMapperException = null;
        try {
            fieldSetMapper.mapFieldSet(fieldSet);
        } catch(FieldSetMapperException fsme) {
            thrownFieldSetMapperException = fsme;
        }
        assertNotNull("mapFieldSet should throw a FieldSetMapperException", thrownFieldSetMapperException);
        assertEquals("the FieldSetMapperException should contain the expected exception", thrownFieldSetMapperException.getConversionExceptions().get("field"), conversionFailedException);
    } 
}
