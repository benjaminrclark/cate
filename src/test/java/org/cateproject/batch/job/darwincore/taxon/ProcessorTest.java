package org.cateproject.batch.job.darwincore.taxon;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import org.cateproject.batch.job.ResolutionService;
import org.cateproject.batch.job.darwincore.EntityRelationship;
import org.cateproject.batch.job.darwincore.EntityRelationshipType;
import org.cateproject.domain.Dataset;
import org.cateproject.domain.Reference;
import org.cateproject.domain.Taxon;

public class ProcessorTest {

    Processor processor;

    ResolutionService resolutionService;
      
    Validator validator;

    Taxon taxon;

    @Before
    public void setUp() throws Exception {
        processor = new Processor();
        resolutionService = EasyMock.createMock(ResolutionService.class);
        validator = EasyMock.createMock(Validator.class);
            
        processor.setResolutionService(resolutionService);
        processor.setValidator(validator);
        taxon = new Taxon(); 

    }

    @Test
    public void testProcessWithUnknownTaxon() throws Exception {
        Taxon parentNameUsage = new Taxon();
        parentNameUsage.setIdentifier("parentNameUsage");
        Taxon acceptedNameUsage = new Taxon();
        acceptedNameUsage.setIdentifier("acceptedNameUsage");
        Taxon originalNameUsage = new Taxon();
        originalNameUsage.setIdentifier("originalNameUsage");
        Reference nameAccordingTo = new Reference();
        nameAccordingTo.setIdentifier("nameAccordingTo");
        Reference namePublishedIn = new Reference();
        namePublishedIn.setIdentifier("namePublishedIn");
        taxon.setParentNameUsage(parentNameUsage);
        taxon.setAcceptedNameUsage(acceptedNameUsage);
        taxon.setOriginalNameUsage(originalNameUsage);
        taxon.setNameAccordingTo(nameAccordingTo);
        taxon.setNamePublishedIn(namePublishedIn);

        Set<ConstraintViolation<Taxon>> constraintViolations = new HashSet<ConstraintViolation<Taxon>>();
        EasyMock.expect(validator.validate(EasyMock.eq(taxon))).andReturn(constraintViolations);
        EasyMock.expect(resolutionService.resolve(EasyMock.eq(taxon), EasyMock.eq(Taxon.class))).andReturn(null);
        EasyMock.expect(resolutionService.resolveRelated(EasyMock.eq(parentNameUsage), EasyMock.eq(Taxon.class), EasyMock.eq(constraintViolations))).andReturn(parentNameUsage);
        EasyMock.expect(resolutionService.resolveRelated(EasyMock.eq(acceptedNameUsage), EasyMock.eq(Taxon.class), EasyMock.eq(constraintViolations))).andReturn(acceptedNameUsage);
        EasyMock.expect(resolutionService.resolveRelated(EasyMock.eq(originalNameUsage), EasyMock.eq(Taxon.class), EasyMock.eq(constraintViolations))).andReturn(originalNameUsage);
        EasyMock.expect(resolutionService.resolveRelated(EasyMock.eq(nameAccordingTo), EasyMock.eq(Reference.class), EasyMock.eq(constraintViolations))).andReturn(nameAccordingTo);
        EasyMock.expect(resolutionService.resolveRelated(EasyMock.eq(namePublishedIn), EasyMock.eq(Reference.class), EasyMock.eq(constraintViolations))).andReturn(namePublishedIn);
        resolutionService.register(EasyMock.eq(taxon), EasyMock.eq(Taxon.class), EasyMock.eq(true));
        EasyMock.replay(validator, resolutionService);
        assertEquals("process should return the expected taxon", taxon, processor.process(taxon));
        EasyMock.verify(validator, resolutionService);
    }

    @Test
    public void testProcessWithResolvableTaxon() throws Exception {
        Taxon resolvableTaxon = new Taxon();
        resolvableTaxon.setIdentifier("resolvable");
        Set<ConstraintViolation<Taxon>> constraintViolations = new HashSet<ConstraintViolation<Taxon>>();
        EasyMock.expect(validator.validate(EasyMock.eq(taxon))).andReturn(constraintViolations);
        EasyMock.expect(resolutionService.resolve(EasyMock.eq(taxon), EasyMock.eq(Taxon.class))).andReturn(resolvableTaxon);
        resolutionService.register(EasyMock.eq(resolvableTaxon), EasyMock.eq(Taxon.class));
        EasyMock.replay(validator, resolutionService);
        assertEquals("process should return the expected taxon", resolvableTaxon, processor.process(taxon));
        EasyMock.verify(validator, resolutionService);
    }

    @Test
    public void testBeforeWrite() throws Exception {
        Taxon parentNameUsage = new Taxon();
        parentNameUsage.setIdentifier("parentNameUsage");
        Taxon acceptedNameUsage = new Taxon();
        acceptedNameUsage.setIdentifier("acceptedNameUsage");
        Taxon originalNameUsage = new Taxon();
        originalNameUsage.setIdentifier("originalNameUsage");
        Reference nameAccordingTo = new Reference();
        nameAccordingTo.setIdentifier("nameAccordingTo");
        Reference namePublishedIn = new Reference();
        namePublishedIn.setIdentifier("namePublishedIn");
        Dataset dataset = new Dataset();
        dataset.setIdentifier("dataset");

        Set<ConstraintViolation<Taxon>> constraintViolations = new HashSet<ConstraintViolation<Taxon>>();
        Set<EntityRelationship<Taxon>> entityRelationships = new HashSet<EntityRelationship<Taxon>>();
        EntityRelationship<Taxon> originalEntityRelationship = new EntityRelationship<Taxon>(taxon, EntityRelationshipType.original, "originalNameUsage");
        entityRelationships.add(originalEntityRelationship);
        EntityRelationship<Taxon> acceptedEntityRelationship = new EntityRelationship<Taxon>(taxon, EntityRelationshipType.accepted, "acceptedNameUsage");
        entityRelationships.add(acceptedEntityRelationship);
        EntityRelationship<Taxon> parentEntityRelationship = new EntityRelationship<Taxon>(taxon, EntityRelationshipType.parent, "parentNameUsage");
        entityRelationships.add(parentEntityRelationship);
        EntityRelationship<Taxon> nameAccordingToEntityRelationship = new EntityRelationship<Taxon>(taxon, EntityRelationshipType.nameAccordingTo, "nameAccordingTo");
        entityRelationships.add(nameAccordingToEntityRelationship);
        EntityRelationship<Taxon> namePublishedInEntityRelationship = new EntityRelationship<Taxon>(taxon, EntityRelationshipType.namePublishedIn, "namePublishedIn");
        entityRelationships.add(namePublishedInEntityRelationship);
        EntityRelationship<Taxon> datasetEntityRelationship = new EntityRelationship<Taxon>(taxon, EntityRelationshipType.dataset, "dataset");
        entityRelationships.add(datasetEntityRelationship);
        
        List<Taxon> items = new ArrayList<Taxon>();
        EasyMock.expect(resolutionService.resolveRelated(EasyMock.eq(originalNameUsage), EasyMock.eq(Taxon.class), EasyMock.eq(constraintViolations))).andReturn(originalNameUsage);        
        EasyMock.expect(resolutionService.resolveRelated(EasyMock.eq(parentNameUsage), EasyMock.eq(Taxon.class), EasyMock.eq(constraintViolations))).andReturn(parentNameUsage);        
        EasyMock.expect(resolutionService.resolveRelated(EasyMock.eq(acceptedNameUsage), EasyMock.eq(Taxon.class), EasyMock.eq(constraintViolations))).andReturn(acceptedNameUsage);        
        EasyMock.expect(resolutionService.resolveRelated(EasyMock.eq(namePublishedIn), EasyMock.eq(Reference.class), EasyMock.eq(constraintViolations))).andReturn(namePublishedIn);        
        EasyMock.expect(resolutionService.resolveRelated(EasyMock.eq(nameAccordingTo), EasyMock.eq(Reference.class), EasyMock.eq(constraintViolations))).andReturn(nameAccordingTo);        
        EasyMock.expect(resolutionService.resolveRelated(EasyMock.eq(dataset), EasyMock.eq(Dataset.class), EasyMock.eq(constraintViolations))).andReturn(dataset);        

        EasyMock.replay(validator, resolutionService);
        processor.setEntityRelationships(entityRelationships);
        processor.beforeWrite(items);
        EasyMock.verify(validator, resolutionService);

    }

    @Test
    public void testRemove() throws Exception {
        Taxon taxon1 = new Taxon();
        taxon.setIdentifier("identifier1");
        Taxon taxon2 = new Taxon();
        taxon2.setIdentifier("identifier2");

        Set<EntityRelationship<Taxon>> entityRelationships = new HashSet<EntityRelationship<Taxon>>();
        EntityRelationship<Taxon> originalEntityRelationship = new EntityRelationship<Taxon>(taxon1, EntityRelationshipType.original, "originalNameUsage");
        entityRelationships.add(originalEntityRelationship);
        EntityRelationship<Taxon> acceptedEntityRelationship = new EntityRelationship<Taxon>(taxon2, EntityRelationshipType.accepted, "acceptedNameUsage");
        entityRelationships.add(acceptedEntityRelationship);
        EntityRelationship<Taxon> parentEntityRelationship = new EntityRelationship<Taxon>(taxon1, EntityRelationshipType.parent, "parentNameUsage");
        entityRelationships.add(parentEntityRelationship);
        

        EasyMock.replay(validator, resolutionService);
        processor.setEntityRelationships(entityRelationships);
        processor.remove(taxon2);
        assertThat("processor should contain the expected entity relationships", 
         processor.getEntityRelationships(),
         containsInAnyOrder(originalEntityRelationship,
                            parentEntityRelationship));
         assertThat("processor should not contain the removed entity relationship", 
          processor.getEntityRelationships(),
          not(hasItem(acceptedEntityRelationship)));
        
        EasyMock.verify(validator, resolutionService);

    }

    @Test
    public void testBindRelationships() throws Exception {
        Taxon taxon = new Taxon();
        taxon.setIdentifier("identifier");
        Taxon boundTaxon = new Taxon();
        boundTaxon.setIdentifier("bound identifier");
        Dataset dataset = new Dataset();
        dataset.setIdentifier("dataset");
        taxon.setDataset(dataset);

        Set<ConstraintViolation<Taxon>> constraintViolations = new HashSet<ConstraintViolation<Taxon>>();
        EntityRelationship<Taxon> expectedEntityRelationship = new EntityRelationship<Taxon>(boundTaxon, EntityRelationshipType.dataset, "dataset");
        EasyMock.expect(resolutionService.resolveRelated(EasyMock.eq(dataset), EasyMock.eq(Dataset.class), EasyMock.eq(constraintViolations))).andReturn(dataset); 

        EasyMock.replay(validator, resolutionService);
        processor.bindRelationships(taxon, boundTaxon, constraintViolations);
        assertEquals("processor should contain one entity relationship", 1, processor.getEntityRelationships().size());
        assertThat("processor should contain the expected entity relationship", 
         processor.getEntityRelationships(),
         hasItem(expectedEntityRelationship));
        EasyMock.verify(validator, resolutionService);
    }
}
