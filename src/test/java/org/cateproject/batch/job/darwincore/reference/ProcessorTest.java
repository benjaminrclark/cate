package org.cateproject.batch.job.darwincore.reference;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import org.cateproject.batch.job.darwincore.EntityRelationship;
import org.cateproject.batch.job.darwincore.EntityRelationshipType;
import org.cateproject.batch.job.darwincore.ProcessorException;
import org.cateproject.domain.Dataset;
import org.cateproject.domain.Reference;
import org.cateproject.domain.Taxon;
import org.cateproject.batch.job.ResolutionService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Validator;
import javax.validation.ConstraintViolation;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.test.MetaDataInstanceFactory;

public class ProcessorTest {


    Processor processor;

    ResolutionService resolutionService;

    Validator validator;

    @Before
    public void setUp() throws Exception {
        processor = new Processor();

        resolutionService = EasyMock.createMock(ResolutionService.class);
        processor.setResolutionService(resolutionService);

        validator = EasyMock.createMock(Validator.class);
        processor.setValidator(validator);
    }

    @Test
    public void testProcessUnboundPersisted() throws Exception {
        Reference reference = new Reference();
        reference.setIdentifier("identifier");
        Reference persistedReference = new Reference();
        persistedReference.setIdentifier("persisted identifier");
        Set<ConstraintViolation<Reference>> constraintViolations = new HashSet<ConstraintViolation<Reference>>();

        EasyMock.expect(resolutionService.alreadyContainsBound(EasyMock.eq(reference), EasyMock.eq(Reference.class))).andReturn(false);
        EasyMock.expect(resolutionService.resolve(EasyMock.eq(reference), EasyMock.eq(Reference.class))).andReturn(persistedReference);
        resolutionService.register(EasyMock.eq(persistedReference), EasyMock.eq(Reference.class));
        EasyMock.expect(validator.validate(EasyMock.eq(persistedReference))).andReturn(constraintViolations);


        EasyMock.replay(resolutionService, validator);
        assertEquals("process should return the expected reference", persistedReference, processor.process(reference));
        EasyMock.verify(resolutionService, validator);
    }

    @Test
    public void testProcessUnboundPersistedInvalid() throws Exception {
        Reference reference = new Reference();
        reference.setIdentifier("identifier");
        Reference persistedReference = new Reference();
        persistedReference.setIdentifier("persisted identifier");
        Set<ConstraintViolation<Reference>> constraintViolations = new HashSet<ConstraintViolation<Reference>>();
        ConstraintViolation<Reference> constraintViolation = (ConstraintViolation<Reference>)EasyMock.createMock(ConstraintViolation.class);
        constraintViolations.add(constraintViolation);

        EasyMock.expect(resolutionService.alreadyContainsBound(EasyMock.eq(reference), EasyMock.eq(Reference.class))).andReturn(false);
        EasyMock.expect(resolutionService.resolve(EasyMock.eq(reference), EasyMock.eq(Reference.class))).andReturn(persistedReference);
        EasyMock.expect(validator.validate(EasyMock.eq(persistedReference))).andReturn(constraintViolations);


        EasyMock.replay(resolutionService, validator, constraintViolation);
        ProcessorException thrownProcessorException = null;
        try {
            processor.process(reference);
        } catch (ProcessorException pe) {
            thrownProcessorException = pe;
        }

        assertNotNull("process should throw a ProcessorException", thrownProcessorException);
        assertThat("the thrown processor exception should contain the expected contraint violation", 
         thrownProcessorException.getConstraintViolations(),
         hasItem(constraintViolation));
        EasyMock.verify(resolutionService, validator, constraintViolation);
    }

    @Test
    public void testProcessUnboundUnpersisted() throws Exception {
        Reference reference = new Reference();
        reference.setIdentifier("identifier");
        Set<ConstraintViolation<Reference>> constraintViolations = new HashSet<ConstraintViolation<Reference>>();

        EasyMock.expect(resolutionService.alreadyContainsBound(EasyMock.eq(reference), EasyMock.eq(Reference.class))).andReturn(false);
        EasyMock.expect(resolutionService.resolve(EasyMock.eq(reference), EasyMock.eq(Reference.class))).andReturn(null);
        resolutionService.register(EasyMock.eq(reference), EasyMock.eq(Reference.class), EasyMock.eq(true));
        EasyMock.expect(validator.validate(EasyMock.eq(reference))).andReturn(constraintViolations);


        EasyMock.replay(resolutionService, validator);
        assertEquals("process should return the expected reference", reference, processor.process(reference));
        EasyMock.verify(resolutionService, validator);
    }

    @Test
    public void testProcessBound() throws Exception {
        Reference reference = new Reference();
        reference.setIdentifier("identifier");
        Taxon taxon = new Taxon();
        taxon.setIdentifier("identifier");
        reference.getTaxa().add(taxon);

        Reference boundReference = new Reference();
        boundReference.setIdentifier("bound identifier");
        Set<ConstraintViolation<Reference>> constraintViolations = new HashSet<ConstraintViolation<Reference>>();

        EasyMock.expect(resolutionService.alreadyContainsBound(EasyMock.eq(reference), EasyMock.eq(Reference.class))).andReturn(true);
        EasyMock.expect(resolutionService.getBound(EasyMock.eq(reference), EasyMock.eq(Reference.class))).andReturn(boundReference);
        EasyMock.expect(resolutionService.resolveRelated(EasyMock.eq(taxon), EasyMock.eq(Taxon.class), EasyMock.eq(constraintViolations))).andReturn(taxon);
        EasyMock.expect(validator.validate(EasyMock.eq(boundReference))).andReturn(constraintViolations);


        EasyMock.replay(resolutionService, validator);
        assertNull("process should return null", processor.process(reference));
        assertThat("entityRelationships should contain the expected entity relationship", 
         processor.getEntityRelationships(),
         containsInAnyOrder(new EntityRelationship<Reference>(boundReference, EntityRelationshipType.taxa, "identifier")));
        EasyMock.verify(resolutionService, validator);
    }


    @Test
    public void testBeforeWriteDatasetRelationship() throws Exception {
        Reference reference = new Reference();
        reference.setIdentifier("identifier");
        Dataset dataset = new Dataset();
        dataset.setIdentifier("dataset identifier");
        List<Reference> items = new ArrayList<Reference>();
        items.add(reference);
        Set<ConstraintViolation<Reference>> constraintViolations = new HashSet<ConstraintViolation<Reference>>();
        processor.getEntityRelationships().add(new EntityRelationship<Reference>(reference, EntityRelationshipType.dataset, "dataset identifier"));
        EasyMock.expect(resolutionService.resolveRelated(EasyMock.eq(dataset), EasyMock.eq(Dataset.class), EasyMock.eq(constraintViolations))).andReturn(dataset).times(2);

        EasyMock.replay(resolutionService, validator);
        processor.beforeWrite(items);
        assertEquals("beforeWrite should set the dataset on reference", dataset, reference.getDataset());
        EasyMock.verify(resolutionService, validator);
    }

    @Test
    public void testBeforeWriteTaxonRelationship() throws Exception {
        Reference reference = new Reference();
        reference.setIdentifier("identifier");
        Taxon taxon = new Taxon();
        taxon.setIdentifier("taxon identifier");
        List<Reference> items = new ArrayList<Reference>();
        items.add(reference);
        Set<ConstraintViolation<Reference>> constraintViolations = new HashSet<ConstraintViolation<Reference>>();
        processor.getEntityRelationships().add(new EntityRelationship<Reference>(reference, EntityRelationshipType.taxa, "taxon identifier"));

        EasyMock.replay(resolutionService, validator);
        processor.beforeWrite(items);
        EasyMock.verify(resolutionService, validator);
    }

    @Test
    public void testBeforeChunk() {
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();
        StepContext stepContext = new StepContext(stepExecution);
        ChunkContext chunkContext = new ChunkContext(stepContext);
        Reference reference = new Reference();
        reference.setIdentifier("identifier");
        processor.getEntityRelationships().add(new EntityRelationship<Reference>(reference, EntityRelationshipType.taxa, "taxon identifier"));

        EasyMock.replay(resolutionService, validator);
        processor.beforeChunk(chunkContext);
        assertTrue("entityRelationships should be empty", processor.getEntityRelationships().isEmpty());
        EasyMock.verify(resolutionService, validator);
    }

    @Test
    public void testAfterChunkError() {
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();
        StepContext stepContext = new StepContext(stepExecution);
        ChunkContext chunkContext = new ChunkContext(stepContext);
        Reference reference = new Reference();
        reference.setIdentifier("identifier");
        processor.getEntityRelationships().add(new EntityRelationship<Reference>(reference, EntityRelationshipType.taxa, "taxon identifier"));

        EasyMock.replay(resolutionService, validator);
        processor.afterChunkError(chunkContext);
        assertTrue("entityRelationships should be empty", processor.getEntityRelationships().isEmpty());
        EasyMock.verify(resolutionService, validator);
    }

    @Test
    public void testAfterChunk() {
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();
        StepContext stepContext = new StepContext(stepExecution);
        ChunkContext chunkContext = new ChunkContext(stepContext);

        EasyMock.replay(resolutionService, validator);
        processor.afterChunk(chunkContext);
        EasyMock.verify(resolutionService, validator);
    }

    @Test
    public void testAfterWrite() {
        List<Reference> items = new ArrayList<Reference>();

        EasyMock.replay(resolutionService, validator);
        processor.afterWrite(items);
        EasyMock.verify(resolutionService, validator);
    }

    @Test
    public void testOnWriteError() {
        List<Reference> items = new ArrayList<Reference>();

        EasyMock.replay(resolutionService, validator);
        processor.onWriteError(new Exception("exception"), items);
        EasyMock.verify(resolutionService, validator);
    }

    @Test
    public void testGetType() {
        EasyMock.replay(resolutionService, validator);
        assertEquals("getType should return the expected type", Reference.class, processor.getType());
        EasyMock.verify(resolutionService, validator);
    }
}
