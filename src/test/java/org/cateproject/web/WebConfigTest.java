package org.cateproject.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.cateproject.domain.Dataset;
import org.cateproject.domain.Reference;
import org.cateproject.domain.Taxon;
import org.cateproject.repository.jpa.DatasetRepository;
import org.cateproject.repository.jpa.ReferenceRepository;
import org.cateproject.repository.jpa.TaxonRepository;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.convert.converter.Converter;

public class WebConfigTest {

    private TaxonRepository taxonRepository;

    private ReferenceRepository referenceRepository;

    private DatasetRepository datasetRepository;

    private WebConfig webConfig;

    @Before
    public void setUp() {
        taxonRepository = EasyMock.createMock(TaxonRepository.class);
        referenceRepository = EasyMock.createMock(ReferenceRepository.class);
        datasetRepository = EasyMock.createMock(DatasetRepository.class);
        webConfig = new WebConfig();
        webConfig.setTaxonRepository(taxonRepository);
        webConfig.setReferenceRepository(referenceRepository);
        webConfig.setDatasetRepository(datasetRepository);
    }

    @Test
    public void testLongToReferenceConverter() {
        Reference reference = new Reference();
        EasyMock.expect(referenceRepository.findOne(EasyMock.eq(1L))).andReturn(reference);
        
        EasyMock.replay(taxonRepository, referenceRepository, datasetRepository);

        Converter<Long, Reference> converter = webConfig.getLongToReferenceConverter();
        assertEquals("convert should return a reference", converter.convert(1L), reference); 
        EasyMock.verify(taxonRepository, referenceRepository, datasetRepository);
    }

    @Test
    public void testStringToReferenceConverter() {
        Reference reference = new Reference();
        EasyMock.expect(referenceRepository.findOne(EasyMock.eq(1L))).andReturn(reference);
        
        EasyMock.replay(taxonRepository, referenceRepository, datasetRepository);

        Converter<String, Reference> converter = webConfig.getStringToReferenceConverter();
        assertEquals("convert should return a reference", converter.convert("1"), reference); 
        EasyMock.verify(taxonRepository, referenceRepository, datasetRepository);
    }
    @Test
    public void testStringToReferenceConverterIdNull() {
        EasyMock.replay(taxonRepository, referenceRepository, datasetRepository);

        Converter<String, Reference> converter = webConfig.getStringToReferenceConverter();
        assertNull("convert should not return a reference", converter.convert(null)); 
        EasyMock.verify(taxonRepository, referenceRepository, datasetRepository);
    }

    @Test
    public void testStringToReferenceConverterIdEmpty() {
        EasyMock.replay(taxonRepository, referenceRepository, datasetRepository);

        Converter<String, Reference> converter = webConfig.getStringToReferenceConverter();
        assertNull("convert should not return a reference", converter.convert("")); 
        EasyMock.verify(taxonRepository, referenceRepository, datasetRepository);
    }

    @Test
    public void testLongToTaxonConverter() {
        Taxon taxon = new Taxon();
        EasyMock.expect(taxonRepository.findOne(EasyMock.eq(1L))).andReturn(taxon);
        
        EasyMock.replay(taxonRepository, referenceRepository, datasetRepository);

        Converter<Long, Taxon> converter = webConfig.getLongToTaxonConverter();
        assertEquals("convert should return a taxon", converter.convert(1L), taxon); 
        EasyMock.verify(taxonRepository, referenceRepository, datasetRepository);
    }

    @Test
    public void testStringToTaxonConverter() {
        Taxon taxon = new Taxon();
        EasyMock.expect(taxonRepository.findOne(EasyMock.eq(1L))).andReturn(taxon);
        
        EasyMock.replay(taxonRepository, referenceRepository, datasetRepository);

        Converter<String, Taxon> converter = webConfig.getStringToTaxonConverter();
        assertEquals("convert should return a taxon", converter.convert("1"), taxon); 
        EasyMock.verify(taxonRepository, referenceRepository, datasetRepository);
    }
    @Test
    public void testStringToTaxonConverterIdNull() {
        EasyMock.replay(taxonRepository, referenceRepository, datasetRepository);

        Converter<String, Taxon> converter = webConfig.getStringToTaxonConverter();
        assertNull("convert should not return a taxon", converter.convert(null)); 
        EasyMock.verify(taxonRepository, referenceRepository, datasetRepository);
    }

    @Test
    public void testStringToTaxonConverterIdEmpty() {
        EasyMock.replay(taxonRepository, referenceRepository, datasetRepository);

        Converter<String, Taxon> converter = webConfig.getStringToTaxonConverter();
        assertNull("convert should not return a taxon", converter.convert("")); 
        EasyMock.verify(taxonRepository, referenceRepository, datasetRepository);
    }

    @Test
    public void testLongToDatasetConverter() {
        Dataset dataset = new Dataset();
        EasyMock.expect(datasetRepository.findOne(EasyMock.eq(1L))).andReturn(dataset);
        
        EasyMock.replay(taxonRepository, referenceRepository, datasetRepository);

        Converter<Long, Dataset> converter = webConfig.getLongToDatasetConverter();
        assertEquals("convert should return a dataset", converter.convert(1L), dataset); 
        EasyMock.verify(taxonRepository, referenceRepository, datasetRepository);
    }

    @Test
    public void testStringToDatasetConverter() {
        Dataset dataset = new Dataset();
        EasyMock.expect(datasetRepository.findOne(EasyMock.eq(1L))).andReturn(dataset);
        
        EasyMock.replay(taxonRepository, referenceRepository, datasetRepository);

        Converter<String, Dataset> converter = webConfig.getStringToDatasetConverter();
        assertEquals("convert should return a dataset", converter.convert("1"), dataset); 
        EasyMock.verify(taxonRepository, referenceRepository, datasetRepository);
    }
    @Test
    public void testStringToDatasetConverterIdNull() {
        EasyMock.replay(taxonRepository, referenceRepository, datasetRepository);

        Converter<String, Dataset> converter = webConfig.getStringToDatasetConverter();
        assertNull("convert should not return a dataset", converter.convert(null)); 
        EasyMock.verify(taxonRepository, referenceRepository, datasetRepository);
    }

    @Test
    public void testStringToDatasetConverterIdEmpty() {
        EasyMock.replay(taxonRepository, referenceRepository, datasetRepository);

        Converter<String, Dataset> converter = webConfig.getStringToDatasetConverter();
        assertNull("convert should not return a dataset", converter.convert("")); 
        EasyMock.verify(taxonRepository, referenceRepository, datasetRepository);
    }
}
