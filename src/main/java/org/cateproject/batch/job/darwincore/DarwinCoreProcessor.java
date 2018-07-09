package org.cateproject.batch.job.darwincore;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.cateproject.batch.job.ResolutionService;
import org.cateproject.domain.Base;
import org.cateproject.domain.Dataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ben
 * @param <T> the type of object validated
 */
public abstract class DarwinCoreProcessor<T extends Base> implements
        ItemProcessor<T,T>, ChunkListener, ItemWriteListener<T> {
    
    private Logger logger = LoggerFactory.getLogger(DarwinCoreProcessor.class);
   
    protected Set<EntityRelationship<T>> entityRelationships = new HashSet<EntityRelationship<T>>();

    @Autowired 
    protected ResolutionService resolutionService;
   
    @Autowired 
    private Validator validator;
    
    protected Class<T> type;
    
    public DarwinCoreProcessor(Class<T> type) {
        this.type = type;
    }
    
    protected void validate(T t , Set<ConstraintViolation<T>> relationViolations) {
        Set<ConstraintViolation<T>> violations = validator.validate(t);
        violations.addAll(relationViolations);
        if(!violations.isEmpty()) {
            ProcessorException pe = new ProcessorException(); 
            pe.setConstraintViolations(violations);
            throw pe;            
        }
    }
    
    public void setResolutionService(ResolutionService resolutionService) {
        this.resolutionService = resolutionService;
    }
    
    public void setValidator(Validator validator) {
        this.validator = validator;
    }
   
    public abstract T process(T t) throws Exception;

    public void remove(T t) throws Exception {
        Set<EntityRelationship<T>> relationshipsToRemove = new HashSet<EntityRelationship<T>>();

        for(EntityRelationship<T> entityRelationship : entityRelationships) {
            if(entityRelationship.getFrom().getIdentifier() == t.getIdentifier()) {
                relationshipsToRemove.add(entityRelationship);
            }
        }

        entityRelationships.removeAll(relationshipsToRemove);
    }
    
    protected void bindRelationships(T t, T u, Set<ConstraintViolation<T>> constraintViolations) {
        if(t.getDataset() != null) {
            resolutionService.resolveRelated(t.getDataset(), Dataset.class, constraintViolations);
            this.entityRelationships.add(new EntityRelationship<T>(u, EntityRelationshipType.dataset, t.getDataset().getIdentifier()));
        }
    }

    @Override
    public void afterWrite(List<? extends T> items) {
        
    }

    @Override
    public void beforeWrite(List<? extends T> items) {
        for (EntityRelationship<T> entityRelationship : entityRelationships) {
            
            Set<ConstraintViolation<T>> constraintViolations = new HashSet<ConstraintViolation<T>>();
            T from = entityRelationship.getFrom();
            
            switch(entityRelationship.getType()) {
            case dataset:
                Dataset dataset = new Dataset();
                dataset.setIdentifier(entityRelationship.getToIdentifier());
                dataset = resolutionService.resolveRelated(dataset, Dataset.class, constraintViolations);
                from.setDataset(dataset);
                break;
            default:
                break;
            }
        }
    }

    @Override
    public void onWriteError(Exception e, List<? extends T> items) {

    }

    @Override
    public void afterChunk(ChunkContext chunkContext) {

    }

    @Override
    public void beforeChunk(ChunkContext chunkContext) {
        logger.debug("beforeChunk");
        entityRelationships = new HashSet<EntityRelationship<T>>();
    }
    
    @Override
    public void afterChunkError(ChunkContext chunkContext) {
        logger.debug("afterChunkError");
        entityRelationships = new HashSet<EntityRelationship<T>>();
    }

}
