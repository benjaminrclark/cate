package org.cateproject.batch.job;

import static org.junit.Assert.*;

import javax.validation.ConstraintViolation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cateproject.domain.Base;
import org.cateproject.domain.Dataset;
import org.cateproject.domain.Description;
import org.cateproject.domain.Distribution;
import org.cateproject.domain.IdentificationKey;
import org.cateproject.domain.Multimedia;
import org.cateproject.domain.MeasurementOrFact;
import org.cateproject.domain.Node;
import org.cateproject.domain.Reference;
import org.cateproject.domain.Taxon;
import org.cateproject.domain.Term;
import org.cateproject.domain.TypeAndSpecimen;
import org.cateproject.domain.VernacularName;
import org.cateproject.repository.jpa.DatasetRepository;
import org.cateproject.repository.jpa.DescriptionRepository;
import org.cateproject.repository.jpa.DistributionRepository;
import org.cateproject.repository.jpa.IdentificationKeyRepository;
import org.cateproject.repository.jpa.MultimediaRepository;
import org.cateproject.repository.jpa.MeasurementOrFactRepository;
import org.cateproject.repository.jpa.NodeRepository;
import org.cateproject.repository.jpa.ReferenceRepository;
import org.cateproject.repository.jpa.TaxonRepository;
import org.cateproject.repository.jpa.TermRepository;
import org.cateproject.repository.jpa.TypeAndSpecimenRepository;
import org.cateproject.repository.jpa.VernacularNameRepository;

import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.MetaDataInstanceFactory;

import org.junit.Before;
import org.junit.Test;
import org.easymock.EasyMock;

public class ResolutionServiceTest {

    class Unknown extends Base {
      public void setIdentifier(String identifier) { }
      public String getIdentifier() { return null; }
    }

    ResolutionService resolutionService;

    DatasetRepository datasetRepository;

    DescriptionRepository descriptionRepository;

    DistributionRepository distributionRepository;
    
    IdentificationKeyRepository identificationKeyRepository;

    MultimediaRepository multimediaRepository;

    MeasurementOrFactRepository measurementOrFactRepository;

    NodeRepository nodeRepository;

    ReferenceRepository referenceRepository;

    TaxonRepository taxonRepository;

    TermRepository termRepository;

    TypeAndSpecimenRepository typeAndSpecimenRepository;

    VernacularNameRepository vernacularNameRepository;

    @Before
    public void setUp() throws Exception {
        resolutionService = new ResolutionService();

        datasetRepository = EasyMock.createMock(DatasetRepository.class);
        descriptionRepository = EasyMock.createMock(DescriptionRepository.class);
        distributionRepository = EasyMock.createMock(DistributionRepository.class);
        identificationKeyRepository = EasyMock.createMock(IdentificationKeyRepository.class);
        multimediaRepository = EasyMock.createMock(MultimediaRepository.class);
        measurementOrFactRepository = EasyMock.createMock(MeasurementOrFactRepository.class);
        nodeRepository = EasyMock.createMock(NodeRepository.class);
        referenceRepository = EasyMock.createMock(ReferenceRepository.class);
        taxonRepository = EasyMock.createMock(TaxonRepository.class);
        termRepository = EasyMock.createMock(TermRepository.class);
        typeAndSpecimenRepository = EasyMock.createMock(TypeAndSpecimenRepository.class);
        vernacularNameRepository = EasyMock.createMock(VernacularNameRepository.class);

        resolutionService.setDatasetRepository(datasetRepository);
        resolutionService.setDescriptionRepository(descriptionRepository);
        resolutionService.setDistributionRepository(distributionRepository);
        resolutionService.setIdentificationKeyRepository(identificationKeyRepository);
        resolutionService.setMultimediaRepository(multimediaRepository);
        resolutionService.setMeasurementOrFactRepository(measurementOrFactRepository);
        resolutionService.setNodeRepository(nodeRepository);
        resolutionService.setReferenceRepository(referenceRepository);
        resolutionService.setTaxonRepository(taxonRepository);
        resolutionService.setTermRepository(termRepository);
        resolutionService.setTypeAndSpecimenRepository(typeAndSpecimenRepository);
        resolutionService.setVernacularNameRepository(vernacularNameRepository);
    }

    /**
     * objectExistsInDatabase
     */
   
    @Test
    public void testObjectExistsInDatabaseTaxon() {
        EasyMock.expect(taxonRepository.existsByTaxonId(EasyMock.eq("identifier"))).andReturn(true);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertTrue("objectExistsInDatabase should return true", resolutionService.objectExistsInDatabase("identifier", Taxon.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testObjectExistsInDatabaseDataset() {
        EasyMock.expect(datasetRepository.existsByIdentifier(EasyMock.eq("identifier"))).andReturn(true);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertTrue("objectExistsInDatabase should return true", resolutionService.objectExistsInDatabase("identifier", Dataset.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testObjectExistsInDatabaseIdentificationKey() {
        EasyMock.expect(identificationKeyRepository.existsByIdentifier(EasyMock.eq("identifier"))).andReturn(true);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertTrue("objectExistsInDatabase should return true", resolutionService.objectExistsInDatabase("identifier", IdentificationKey.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testObjectExistsInDatabaseMultimedia() {
        EasyMock.expect(multimediaRepository.existsByIdentifier(EasyMock.eq("identifier"))).andReturn(true);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertTrue("objectExistsInDatabase should return true", resolutionService.objectExistsInDatabase("identifier", Multimedia.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testObjectExistsInDatabaseNode() {
        EasyMock.expect(nodeRepository.existsByIdentifier(EasyMock.eq("identifier"))).andReturn(true);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertTrue("objectExistsInDatabase should return true", resolutionService.objectExistsInDatabase("identifier", Node.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testObjectExistsInDatabaseReference() {
        EasyMock.expect(referenceRepository.existsByIdentifier(EasyMock.eq("identifier"))).andReturn(true);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertTrue("objectExistsInDatabase should return true", resolutionService.objectExistsInDatabase("identifier", Reference.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testObjectExistsInDatabaseTerm() {
        EasyMock.expect(termRepository.existsByIdentifier(EasyMock.eq("identifier"))).andReturn(true);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertTrue("objectExistsInDatabase should return true", resolutionService.objectExistsInDatabase("identifier", Term.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testObjectExistsInDatabaseTypeAndSpecimen() {
        EasyMock.expect(typeAndSpecimenRepository.existsByOccurrenceId(EasyMock.eq("identifier"))).andReturn(true);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertTrue("objectExistsInDatabase should return true", resolutionService.objectExistsInDatabase("identifier", TypeAndSpecimen.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testObjectExistsInDatabaseDistribution() {
        EasyMock.expect(distributionRepository.existsByIdentifier(EasyMock.eq("identifier"))).andReturn(true);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertTrue("objectExistsInDatabase should return true", resolutionService.objectExistsInDatabase("identifier", Distribution.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testObjectExistsInDatabaseDescription() {
        EasyMock.expect(descriptionRepository.existsByIdentifier(EasyMock.eq("identifier"))).andReturn(true);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertTrue("objectExistsInDatabase should return true", resolutionService.objectExistsInDatabase("identifier", Description.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testObjectExistsInDatabaseMeasurementOrFact() {
        EasyMock.expect(measurementOrFactRepository.existsByIdentifier(EasyMock.eq("identifier"))).andReturn(true);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertTrue("objectExistsInDatabase should return true", resolutionService.objectExistsInDatabase("identifier", MeasurementOrFact.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testObjectExistsInDatabaseVernacularName() {
        EasyMock.expect(vernacularNameRepository.existsByIdentifier(EasyMock.eq("identifier"))).andReturn(true);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertTrue("objectExistsInDatabase should return true", resolutionService.objectExistsInDatabase("identifier", VernacularName.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectExistsInDatabaseUnresolvable() {
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        resolutionService.objectExistsInDatabase("identifier", Unknown.class);
    }
 
    /**
     * lookup
     */   

    @Test
    public void testLookupTaxon() {
        Taxon taxon = new Taxon();
        EasyMock.expect(taxonRepository.findByTaxonId(EasyMock.eq("identifier"))).andReturn(taxon);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertEquals("lookup should return the expected object", taxon, resolutionService.lookup("identifier", Taxon.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testLookupDataset() {
        Dataset dataset = new Dataset();
        EasyMock.expect(datasetRepository.findByIdentifier(EasyMock.eq("identifier"))).andReturn(dataset);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertEquals("lookup should return the expected object", dataset, resolutionService.lookup("identifier", Dataset.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testLookupIdentificationKey() {
        IdentificationKey identificationKey = new IdentificationKey();
        EasyMock.expect(identificationKeyRepository.findByIdentifier(EasyMock.eq("identifier"))).andReturn(identificationKey);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertEquals("lookup should return the expected object", identificationKey, resolutionService.lookup("identifier", IdentificationKey.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testLookupMultimedia() {
        Multimedia multimedia = new Multimedia();
        EasyMock.expect(multimediaRepository.findByIdentifier(EasyMock.eq("identifier"))).andReturn(multimedia);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertEquals("lookup should return the expected object", multimedia, resolutionService.lookup("identifier", Multimedia.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testLookupNode() {
        Node node = new Node();
        EasyMock.expect(nodeRepository.findByIdentifier(EasyMock.eq("identifier"))).andReturn(node);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertEquals("lookup should return the expected object", node, resolutionService.lookup("identifier", Node.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testLookupReference() {
        Reference reference = new Reference();
        EasyMock.expect(referenceRepository.findByIdentifier(EasyMock.eq("identifier"))).andReturn(reference);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertEquals("lookup should return the expected object", reference, resolutionService.lookup("identifier", Reference.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testLookupTerm() {
        Term term = new Term();
        EasyMock.expect(termRepository.findByIdentifier(EasyMock.eq("identifier"))).andReturn(term);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertEquals("lookup should return the expected object", term, resolutionService.lookup("identifier", Term.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testLookupTypeAndSpecimen() {
        TypeAndSpecimen typeAndSpecimen = new TypeAndSpecimen();
        EasyMock.expect(typeAndSpecimenRepository.findByOccurrenceId(EasyMock.eq("identifier"))).andReturn(typeAndSpecimen);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertEquals("lookup should return the expected object", typeAndSpecimen, resolutionService.lookup("identifier", TypeAndSpecimen.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testLookupDistribution() {
        Distribution distribution = new Distribution();
        EasyMock.expect(distributionRepository.findByIdentifier(EasyMock.eq("identifier"))).andReturn(distribution);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertEquals("lookup should return the expected object", distribution, resolutionService.lookup("identifier", Distribution.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testLookupDescription() {
        Description description = new Description();
        EasyMock.expect(descriptionRepository.findByIdentifier(EasyMock.eq("identifier"))).andReturn(description);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertEquals("lookup should return the expected object", description, resolutionService.lookup("identifier", Description.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testLookupMeasurementOrFact() {
        MeasurementOrFact measurementOrFact = new MeasurementOrFact();
        EasyMock.expect(measurementOrFactRepository.findByIdentifier(EasyMock.eq("identifier"))).andReturn(measurementOrFact);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertEquals("lookup should return the expected object", measurementOrFact, resolutionService.lookup("identifier", MeasurementOrFact.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testLookupVernacularName() {
        VernacularName vernacularName = new VernacularName();
        EasyMock.expect(vernacularNameRepository.findByIdentifier(EasyMock.eq("identifier"))).andReturn(vernacularName);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertEquals("lookup should return the expected object", vernacularName, resolutionService.lookup("identifier", VernacularName.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupUnresolvable() {
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        resolutionService.lookup("identifier", Unknown.class);
    }

    /**
     * save 
     */   

    @Test
    public void testSaveTaxon() {
        Taxon taxon = new Taxon();
        EasyMock.expect(taxonRepository.save(EasyMock.eq(taxon))).andReturn(taxon);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertEquals("save should return the expected object", taxon, resolutionService.save(taxon, Taxon.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testSaveDataset() {
        Dataset dataset = new Dataset();
        EasyMock.expect(datasetRepository.save(EasyMock.eq(dataset))).andReturn(dataset);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertEquals("save should return the expected object", dataset, resolutionService.save(dataset, Dataset.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testSaveIdentificationKey() {
        IdentificationKey identificationKey = new IdentificationKey();
        EasyMock.expect(identificationKeyRepository.save(EasyMock.eq(identificationKey))).andReturn(identificationKey);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertEquals("save should return the expected object", identificationKey, resolutionService.save(identificationKey, IdentificationKey.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testSaveMultimedia() {
        Multimedia multimedia = new Multimedia();
        EasyMock.expect(multimediaRepository.save(EasyMock.eq(multimedia))).andReturn(multimedia);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertEquals("save should return the expected object", multimedia, resolutionService.save(multimedia, Multimedia.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testSaveNode() {
        Node node = new Node();
        EasyMock.expect(nodeRepository.save(EasyMock.eq(node))).andReturn(node);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertEquals("save should return the expected object", node, resolutionService.save(node, Node.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testSaveReference() {
        Reference reference = new Reference();
        EasyMock.expect(referenceRepository.save(EasyMock.eq(reference))).andReturn(reference);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertEquals("save should return the expected object", reference, resolutionService.save(reference, Reference.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testSaveTerm() {
        Term term = new Term();
        EasyMock.expect(termRepository.save(EasyMock.eq(term))).andReturn(term);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertEquals("save should return the expected object", term, resolutionService.save(term, Term.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testSaveTypeAndSpecimen() {
        TypeAndSpecimen typeAndSpecimen = new TypeAndSpecimen();
        EasyMock.expect(typeAndSpecimenRepository.save(EasyMock.eq(typeAndSpecimen))).andReturn(typeAndSpecimen);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertEquals("save should return the expected object", typeAndSpecimen, resolutionService.save(typeAndSpecimen, TypeAndSpecimen.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testSaveDistribution() {
        Distribution distribution = new Distribution();
        EasyMock.expect(distributionRepository.save(EasyMock.eq(distribution))).andReturn(distribution);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertEquals("save should return the expected object", distribution, resolutionService.save(distribution, Distribution.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testSaveDescription() {
        Description description = new Description();
        EasyMock.expect(descriptionRepository.save(EasyMock.eq(description))).andReturn(description);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertEquals("save should return the expected object", description, resolutionService.save(description, Description.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testSaveMeasurementOrFact() {
        MeasurementOrFact measurementOrFact = new MeasurementOrFact();
        EasyMock.expect(measurementOrFactRepository.save(EasyMock.eq(measurementOrFact))).andReturn(measurementOrFact);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertEquals("save should return the expected object", measurementOrFact, resolutionService.save(measurementOrFact, MeasurementOrFact.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testSaveVernacularName() {
        VernacularName vernacularName = new VernacularName();
        EasyMock.expect(vernacularNameRepository.save(EasyMock.eq(vernacularName))).andReturn(vernacularName);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertEquals("save should return the expected object", vernacularName, resolutionService.save(vernacularName, VernacularName.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveUnresolvable() {
        Unknown unknown = new Unknown();
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        resolutionService.save(unknown, Unknown.class);
    }

    /**
     * resolveRelated
     */  

    @Test
    public void testResolveRelatedNull() {
        Set<ConstraintViolation<Taxon>> constraintViolations = new HashSet<ConstraintViolation<Taxon>>();
        
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertNull("resolveRelated should return null",resolutionService.resolveRelated((Taxon)null, Taxon.class, constraintViolations));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testResolveRelatedBound() {
        Taxon taxon = new Taxon();
        taxon.setTaxonId("identifier");
        resolutionService.boundObjects.put(Taxon.class, new HashMap<String,Base>());
        resolutionService.boundObjects.get(Taxon.class).put("identifier", taxon);
        Set<ConstraintViolation<Taxon>> constraintViolations = new HashSet<ConstraintViolation<Taxon>>();
        
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertEquals("resolveRelated should return the expected object", taxon, resolutionService.resolveRelated(taxon, Taxon.class, constraintViolations));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testResolveRelatedNotInDb() {
        Taxon taxon = new Taxon();
        taxon.setTaxonId("identifier");
        Set<ConstraintViolation<Taxon>> constraintViolations = new HashSet<ConstraintViolation<Taxon>>();
        EasyMock.expect(taxonRepository.findByTaxonId(EasyMock.eq("identifier"))).andReturn(null); 
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertEquals("resolveRelated should return the expected object", taxon, resolutionService.resolveRelated(taxon, Taxon.class, constraintViolations));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testResolveRelatedNotInDbNewObjectInitialized() {
        Taxon taxon = new Taxon();
        taxon.setTaxonId("identifier");
        Set<ConstraintViolation<Taxon>> constraintViolations = new HashSet<ConstraintViolation<Taxon>>();
        EasyMock.expect(taxonRepository.findByTaxonId(EasyMock.eq("identifier"))).andReturn(null); 
        resolutionService.newObjects.put(Taxon.class, new HashSet<Base>());
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertEquals("resolveRelated should return the expected object", taxon, resolutionService.resolveRelated(taxon, Taxon.class, constraintViolations));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testResolveRelatedInDb() {
        Taxon taxon = new Taxon();
        taxon.setTaxonId("identifier");

        Taxon  persistedTaxon =  new Taxon();
        persistedTaxon.setTaxonId("persisted identifier");
        Set<ConstraintViolation<Taxon>> constraintViolations = new HashSet<ConstraintViolation<Taxon>>();
        EasyMock.expect(taxonRepository.findByTaxonId(EasyMock.eq("identifier"))).andReturn(persistedTaxon); 
        resolutionService.newObjects.put(Taxon.class, new HashSet<Base>());
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertEquals("resolveRelated should return the expected object", persistedTaxon, resolutionService.resolveRelated(taxon, Taxon.class, constraintViolations));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testResolveInDb() {
        Taxon taxon = new Taxon();
        taxon.setTaxonId("identifier");

        EasyMock.expect(taxonRepository.findByTaxonId(EasyMock.eq("identifier"))).andReturn(taxon); 
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertEquals("resolve should return the expected object", taxon, resolutionService.resolve(taxon, Taxon.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testResolveNotInDb() {
        Taxon taxon = new Taxon();
        taxon.setTaxonId("identifier");

        EasyMock.expect(taxonRepository.findByTaxonId(EasyMock.eq("identifier"))).andReturn(null); 
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertNull("resolve should return null", resolutionService.resolve(taxon, Taxon.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testResolveBound() {
        Taxon taxon = new Taxon();
        taxon.setTaxonId("identifier");
        resolutionService.boundObjects.put(Taxon.class, new HashMap<String,Base>());
        resolutionService.boundObjects.get(Taxon.class).put("identifier", taxon);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertEquals("resolve should return the expected object", taxon, resolutionService.resolve(taxon, Taxon.class));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    /**
     * beforeWrite  
     */

    @Test
    public void testBeforeWrite() {
        Taxon taxon = new Taxon();
        taxon.setTaxonId("identifier");
        resolutionService.newObjects.put(Taxon.class, new HashSet<Base>());
        resolutionService.newObjects.get(Taxon.class).add(taxon);
        EasyMock.expect(taxonRepository.save(EasyMock.eq(taxon))).andReturn(taxon);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        resolutionService.beforeWrite(null);
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    /**
     * afterChunk  
     */

    @Test
    public void testAfterChunk() {
        Taxon taxon = new Taxon();
        taxon.setTaxonId("identifier");
        resolutionService.newObjects.put(Taxon.class, new HashSet<Base>());
        resolutionService.newObjects.get(Taxon.class).add(taxon);
        resolutionService.boundObjects.put(Taxon.class, new HashMap<String,Base>());
        resolutionService.boundObjects.get(Taxon.class).put("identifier", taxon);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        resolutionService.afterChunk(null);
        assertTrue("afterChunk should clear the cached newObjects", resolutionService.newObjects.get(Taxon.class).isEmpty());
        assertTrue("afterChunk should clear the cached boundObjects", resolutionService.boundObjects.get(Taxon.class).isEmpty());
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    /**
     * afterChunkError 
     */ 

    @Test
    public void testAfterChunkError() {
        Taxon taxon = new Taxon();
        taxon.setTaxonId("identifier");
        resolutionService.newObjects.put(Taxon.class, new HashSet<Base>());
        resolutionService.newObjects.get(Taxon.class).add(taxon);
        resolutionService.boundObjects.put(Taxon.class, new HashMap<String,Base>());
        resolutionService.boundObjects.get(Taxon.class).put("identifier", taxon);
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        resolutionService.afterChunkError(null);
        assertTrue("afterChunkError should clear the cached newObjects", resolutionService.newObjects.get(Taxon.class).isEmpty());
        assertTrue("afterChunkError should clear the cached boundObjects", resolutionService.boundObjects.get(Taxon.class).isEmpty());
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    /**
     * resolveRelated
     */

    @Test
    public void testResolveRelatedSetBound() {
        Taxon taxon = new Taxon();
        taxon.setTaxonId("identifier");
        Set<Taxon> related = new HashSet<Taxon>();
        related.add(taxon);
        resolutionService.boundObjects.put(Taxon.class, new HashMap<String,Base>());
        resolutionService.boundObjects.get(Taxon.class).put("identifier", taxon);
        Set<ConstraintViolation<Taxon>> constraintViolations = new HashSet<ConstraintViolation<Taxon>>();
        
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertEquals("resolveRelated should return the expected objects", related, resolutionService.resolveRelated(related, Taxon.class, constraintViolations));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testResolveRelatedSetNull() {
        Set<Taxon> related = new HashSet<Taxon>();
        related.add((Taxon) null);
        Set<ConstraintViolation<Taxon>> constraintViolations = new HashSet<ConstraintViolation<Taxon>>();
        
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertTrue("resolveRelated should return an empty set",resolutionService.resolveRelated(related, Taxon.class, constraintViolations).isEmpty());

        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    /**
     * register
     */ 

    @Test
    public void testRegister() {
        Taxon taxon = new Taxon();
        taxon.setTaxonId("identifier");

        Description description = new Description();
        description.setIdentifier("identifier");
        resolutionService.boundObjects.put(Description.class, new HashMap<String,Base>());
        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        resolutionService.register(taxon, Taxon.class);
        assertEquals("register should cache the expected object", taxon, resolutionService.boundObjects.get(Taxon.class).get("identifier"));
        resolutionService.register(description, Description.class);
        assertEquals("register should cache the expected object", description, resolutionService.boundObjects.get(Description.class).get("identifier"));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testRegisterNewObject() {
        Taxon taxon = new Taxon();
        taxon.setTaxonId("identifier");

        Taxon taxon2 = new Taxon();
        taxon2.setTaxonId("identifier 2");
        resolutionService.newObjects.put(Taxon.class, new HashSet<Base>());

        Description description = new Description();
        description.setIdentifier("identifier");

        
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        resolutionService.register(taxon, Taxon.class, false);
        assertTrue("register should not cache the expected object", resolutionService.newObjects.get(Taxon.class).isEmpty());
        assertEquals("register should cache the expected object", taxon, resolutionService.boundObjects.get(Taxon.class).get("identifier"));

        resolutionService.register(taxon2, Taxon.class, true);
        assertTrue("register should cache the expected object", resolutionService.newObjects.get(Taxon.class).contains(taxon2));
        assertEquals("register should cache the expected object", taxon2, resolutionService.boundObjects.get(Taxon.class).get("identifier 2"));

        resolutionService.register(description, Description.class, true);
        assertTrue("register should return the expected object", resolutionService.newObjects.get(Description.class).contains(description));
        assertEquals("register should return the expected object", description, resolutionService.boundObjects.get(Description.class).get("identifier"));
        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testGetBound() {
        Taxon taxon = new Taxon();
        taxon.setTaxonId("identifier");

        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertNull("getBound should return null", resolutionService.getBound(taxon, Taxon.class));
        resolutionService.boundObjects.get(Taxon.class).put("identifier", taxon);
        assertEquals("getBound should return the expected object", taxon, resolutionService.getBound(taxon, Taxon.class));

        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testAlreadyContainsBound() {
        Taxon taxon = new Taxon();
        taxon.setTaxonId("identifier");

        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertFalse("getBound should return null", resolutionService.alreadyContainsBound(taxon, Taxon.class));
        resolutionService.boundObjects.get(Taxon.class).put("identifier", taxon);
        assertTrue("getBound should return the expected object", resolutionService.alreadyContainsBound(taxon, Taxon.class));

        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testBeforeStep() {
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        resolutionService.beforeStep(stepExecution);

        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testAfterStep() {
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        assertNull("afterStep should return null",resolutionService.afterStep(stepExecution));

        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testBeforeChunk() {
        ChunkContext chunkContext = new ChunkContext(null);
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        resolutionService.beforeChunk(chunkContext);

        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testAfterWrite() {
        List<Taxon> bases = new ArrayList<Taxon>();
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        resolutionService.afterWrite(bases);

        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }

    @Test
    public void testOnWriteError() {
        List<Taxon> bases = new ArrayList<Taxon>();
        Exception exception = new Exception();
        EasyMock.replay(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
        resolutionService.onWriteError(exception, bases);

        EasyMock.verify(datasetRepository,
                        descriptionRepository,
                        distributionRepository,
                        identificationKeyRepository,
                        multimediaRepository,
                        measurementOrFactRepository,
                        nodeRepository,
                        referenceRepository,
                        taxonRepository,
                        termRepository,
                        typeAndSpecimenRepository,
                        vernacularNameRepository);
    }
}
