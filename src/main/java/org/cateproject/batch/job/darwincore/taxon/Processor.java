package org.cateproject.batch.job.darwincore.taxon;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.cateproject.domain.Reference;
import org.cateproject.domain.Taxon;
import org.cateproject.batch.job.darwincore.DarwinCoreProcessor;
import org.cateproject.batch.job.darwincore.EntityRelationship;
import org.cateproject.batch.job.darwincore.EntityRelationshipType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author ben
 * 
 */
public class Processor extends DarwinCoreProcessor<Taxon> {

    private Logger logger = LoggerFactory.getLogger(Processor.class);
    
    public Processor() {
        super(Taxon.class);
    }
    
    protected void bindRelationships(Taxon t, Taxon u, Set<ConstraintViolation<Taxon>> constraintViolations) {
        if(t.getParentNameUsage() != null) {
            resolutionService.resolveRelated(t.getParentNameUsage(), Taxon.class, constraintViolations);
            this.entityRelationships.add(new EntityRelationship<Taxon>(u, EntityRelationshipType.parent, t.getParentNameUsage().getTaxonId()));
        }
        if(t.getAcceptedNameUsage() != null) {
            resolutionService.resolveRelated(t.getAcceptedNameUsage(), Taxon.class, constraintViolations);
            this.entityRelationships.add(new EntityRelationship<Taxon>(u, EntityRelationshipType.accepted, t.getAcceptedNameUsage().getTaxonId()));
        }
        if(t.getOriginalNameUsage() != null) {
            resolutionService.resolveRelated(t.getOriginalNameUsage(), Taxon.class, constraintViolations);
            this.entityRelationships.add(new EntityRelationship<Taxon>(u, EntityRelationshipType.original, t.getOriginalNameUsage().getTaxonId()));
        }
        if(t.getNameAccordingTo() != null) {
            resolutionService.resolveRelated(t.getNameAccordingTo(), Reference.class, constraintViolations);
            this.entityRelationships.add(new EntityRelationship<Taxon>(u, EntityRelationshipType.nameAccordingTo, t.getNameAccordingTo().getIdentifier()));
        }
        if(t.getNamePublishedIn() != null) {
            resolutionService.resolveRelated(t.getNamePublishedIn(), Reference.class, constraintViolations);
            this.entityRelationships.add(new EntityRelationship<Taxon>(u, EntityRelationshipType.namePublishedIn, t.getNamePublishedIn().getIdentifier()));
        }
    }
    
    @Override
    public void beforeWrite(List<? extends Taxon> items) {
        logger.info("Before Write");
        super.beforeWrite(items);

        for (EntityRelationship<Taxon> entityRelationship : entityRelationships) {
            
            Set<ConstraintViolation<Taxon>> constraintViolations = new HashSet<ConstraintViolation<Taxon>>();
            Taxon from = entityRelationship.getFrom();
            
            switch(entityRelationship.getType()) {
            case original:
                Taxon originalNameUsage = new Taxon();
                originalNameUsage.setIdentifier(entityRelationship.getToIdentifier());
                originalNameUsage = resolutionService.resolveRelated(originalNameUsage, Taxon.class, constraintViolations);
                from.setOriginalNameUsage(originalNameUsage);
                break;
            case accepted:
                Taxon acceptedNameUsage = new Taxon();
                acceptedNameUsage.setIdentifier(entityRelationship.getToIdentifier());
                acceptedNameUsage = resolutionService.resolveRelated(acceptedNameUsage, Taxon.class, constraintViolations);
                from.setAcceptedNameUsage(acceptedNameUsage);
                break;
            case parent:
                Taxon parentNameUsage = new Taxon();
                parentNameUsage.setIdentifier(entityRelationship.getToIdentifier());
                parentNameUsage = resolutionService.resolveRelated(parentNameUsage, Taxon.class, constraintViolations);
                from.setParentNameUsage(parentNameUsage);
                break;            
            case nameAccordingTo:
                Reference nameAccordingTo = new Reference();
                nameAccordingTo.setIdentifier(entityRelationship.getToIdentifier());
                nameAccordingTo = resolutionService.resolveRelated(nameAccordingTo, Reference.class, constraintViolations);
                from.setNameAccordingTo(nameAccordingTo);
                break;            
            case namePublishedIn:
                Reference namePublishedIn = new Reference();
                namePublishedIn.setIdentifier(entityRelationship.getToIdentifier());
                namePublishedIn = resolutionService.resolveRelated(namePublishedIn, Reference.class, constraintViolations);
                from.setNamePublishedIn(namePublishedIn);
                break;            
            default:
                break;
            }
        }
    }

    /**
     * @param t
     *            a taxon object
     * @throws Exception
     *             if something goes wrong
     * @return Taxon a taxon object
     */
    public Taxon process(Taxon t) throws Exception {
        logger.info("Processing " + t.getTaxonId());
        Set<ConstraintViolation<Taxon>> constraintViolations = new HashSet<ConstraintViolation<Taxon>>();
        
        Taxon persisted = resolutionService.resolve(t, Taxon.class);
        
        if (persisted == null) {
            bindRelationships(t, t, constraintViolations);
            validate(t, constraintViolations);
            resolutionService.register(t, type, true);                        
            logger.info("Adding taxon " + t.getTaxonId());
            return t;
        } else {            
            persisted.setScientificName(t.getScientificName());
            persisted.setScientificNameAuthorship(t.getScientificNameAuthorship());
            persisted.setTaxonRank(t.getTaxonRank());
            persisted.setGenus(t.getGenus());
            persisted.setSubgenus(t.getSubgenus());
            persisted.setSpecificEpithet(t.getSpecificEpithet());
            persisted.setInfraspecificEpithet(t.getInfraspecificEpithet());
            persisted.setTaxonomicStatus(t.getTaxonomicStatus());
            persisted.setNameAccordingTo(t.getNameAccordingTo());
            persisted.setNamePublishedIn(t.getNamePublishedIn());
            persisted.setKingdom(t.getKingdom());
            persisted.setPhylum(t.getPhylum());
            persisted.setClazz(t.getClazz());
            persisted.setOrder(t.getOrder());
            persisted.setFamily(t.getFamily());
            persisted.setNomenclaturalCode(t.getNomenclaturalCode());
            persisted.setNomenclaturalStatus(t.getNomenclaturalStatus());
            persisted.setDataset(t.getDataset());
            persisted.setLine(t.getLine());
            persisted.setLineNumber(t.getLineNumber());
            
            bindRelationships(t,persisted, constraintViolations);
            validate(t, constraintViolations);
            
            resolutionService.register(persisted, type);            
            logger.info("Overwriting taxon " + persisted.getTaxonId());
            return persisted;
        }
    }
}
