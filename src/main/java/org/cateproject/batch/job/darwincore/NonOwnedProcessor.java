package org.cateproject.batch.job.darwincore;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.validation.ConstraintViolation;

import org.cateproject.domain.Dataset;
import org.cateproject.domain.NonOwnedEntity;
import org.cateproject.domain.Taxon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ben
 *
 */
public abstract class NonOwnedProcessor<T extends NonOwnedEntity> extends DarwinCoreProcessor<T> {
		
    private Logger logger = LoggerFactory.getLogger(NonOwnedProcessor.class);
    
    public NonOwnedProcessor(Class<T> type) {
    	super(type);
    }

    /**
     * @param t an object
     * @throws Exception if something goes wrong
     * @return T an object
     */
    public final T process(final T t)
            throws Exception {
        logger.info("Processing " + t.getIdentifier());
     
        Set<ConstraintViolation<T>> constraintViolations = new HashSet<ConstraintViolation<T>>();
       
        if (!resolutionService.alreadyContainsBound(t, type)) {
            T persisted = resolutionService.resolve(t, type);

            if (persisted == null) {
                validate(t, constraintViolations);
                doPersist(t);
                resolutionService.register(t, type, true);
                bindRelationships(t,t,constraintViolations);
                logger.info("Adding object " + t.getIdentifier());
                return t;
            } else {
                // Assume that this is the first of several times this object
                // appears in the result set, and we'll use this version to
                // overwrite the existing object
                	
                persisted.setAccessRights(t.getAccessRights());
                persisted.setCreated(t.getCreated());
                persisted.setLicense(t.getLicense());
                persisted.setModified(t.getModified());
                persisted.setRights(t.getRights());
                persisted.setRightsHolder(t.getRightsHolder());
                doUpdate(persisted, t);
                    
                validate(persisted, constraintViolations);                    
                    
                resolutionService.register(persisted, type);
                bindRelationships(t,persisted,constraintViolations);
                logger.info("Overwriting object " + t.getIdentifier());
                return persisted;
            }
        } else {
            T bound = resolutionService.getBound(t, type);
            // We've already seen this object within this chunk and we'll
            // update it with this taxon but that's it, assuming that it
            // isn't a more up to date version
            bound.setAccessRights(t.getAccessRights());
            bound.setCreated(t.getCreated());
            bound.setLicense(t.getLicense());
            bound.setModified(t.getModified());
            bound.setRights(t.getRights());
            bound.setRightsHolder(t.getRightsHolder());
            doUpdate(bound, t);
                    
            validate(bound, constraintViolations);                    
                    
            bindRelationships(t,bound,constraintViolations);
            // We've already returned this object once
            logger.info("Skipping object " + t.getIdentifier());
            return null;
        }
    }

    protected void bindRelationships(T t, T u, Set<ConstraintViolation<T>> constraintViolations) {
        super.bindRelationships(t,u, constraintViolations);
        if(!t.getTaxa().isEmpty()) {
            resolutionService.resolveRelated(t.getTaxa().get(0), Taxon.class, constraintViolations);        	
            this.entityRelationships.add(new EntityRelationship<T>(u, EntityRelationshipType.taxa, t.getTaxa().get(0).getIdentifier()));
        } else {
            this.entityRelationships.add(new EntityRelationship<T>(u, EntityRelationshipType.taxa, null));
        }
    }

    @Override
    public void beforeWrite(List<? extends T> items) {
        super.beforeWrite(items);

        for(T t : items) {

        }

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

    protected abstract void doUpdate(T persisted, T t);

    protected abstract void doPersist(T t);
}
