package org.cateproject.batch.job;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;

public class ResolutionService implements ChunkListener, StepExecutionListener, ItemWriteListener<Base> {
    
    private static final Logger logger = LoggerFactory.getLogger(ResolutionService.class);
    
    protected Map<Class<? extends Base>,Map<String,Base>> boundObjects = new HashMap<Class<? extends Base>,Map<String,Base>>();
    
    protected Map<Class<? extends Base>,Set<Base>> newObjects = new HashMap<Class<? extends Base>,Set<Base>>();
    
    private StepExecution stepExecution;

    @Autowired
    private DatasetRepository datasetRepository;
    
    @Autowired
    private DistributionRepository distributionRepository;
   
    @Autowired 
    private DescriptionRepository descriptionRepository;
    
    @Autowired
    private MultimediaRepository multimediaRepository;
    
    @Autowired
    private IdentificationKeyRepository identificationKeyRepository;
    
    @Autowired
    private MeasurementOrFactRepository measurementOrFactRepository;
   
    @Autowired 
    private NodeRepository nodeRepository;
   
    @Autowired 
    private ReferenceRepository referenceRepository;
    
    @Autowired
    private TermRepository termRepository;

    @Autowired
    private TaxonRepository taxonRepository; 
    
    @Autowired
    private TypeAndSpecimenRepository typeAndSpecimenRepository;
    
    @Autowired
    private VernacularNameRepository vernacularNameRepository;
    
    public void setDatasetRepository(DatasetRepository datasetRepository) {
        this.datasetRepository = datasetRepository;
    }
    
    public void setDistributionRepository(DistributionRepository distributionRepository) {
        this.distributionRepository = distributionRepository;
    }

    public void setDescriptionRepository(DescriptionRepository descriptionRepository) {
        this.descriptionRepository = descriptionRepository;
    }

    public void setMultimediaRepository(MultimediaRepository multimediaRepository) {
        this.multimediaRepository = multimediaRepository;
    }

    public void setIdentificationKeyRepository(IdentificationKeyRepository identificationKeyRepository) {
        this.identificationKeyRepository = identificationKeyRepository;
    }

    public void setMeasurementOrFactRepository(MeasurementOrFactRepository measurementOrFactRepository) {
        this.measurementOrFactRepository = measurementOrFactRepository;
    }

    public void setNodeRepository(NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }

    public void setReferenceRepository(ReferenceRepository referenceRepository) {
        this.referenceRepository = referenceRepository;
    }

    public void setTermRepository(TermRepository termRepository) {
        this.termRepository = termRepository;
    }

    public void setTaxonRepository(TaxonRepository taxonRepository) {
        this.taxonRepository = taxonRepository;
    }

    public void setTypeAndSpecimenRepository(TypeAndSpecimenRepository typeAndSpecimenRepository) {
        this.typeAndSpecimenRepository = typeAndSpecimenRepository;
    }

    public void setVernacularNameRepository(VernacularNameRepository vernacularNameRepository) {
        this.vernacularNameRepository = vernacularNameRepository;
    }

    public <T extends Base> T getBound(T t, Class<T> clazz) {
        if(!boundObjects.containsKey(clazz)) {
            boundObjects.put(clazz, new HashMap<String,Base>());
        }
        
        return (T)boundObjects.get(clazz).get(t.getIdentifier());
    }
    
    public <T extends Base> boolean alreadyContainsBound(Base base, Class<? extends Base> clazz) {
        if(!boundObjects.containsKey(clazz)) {
            boundObjects.put(clazz, new HashMap<String,Base>());
        }
        return boundObjects.get(clazz).containsKey(base.getIdentifier());
    }
    
    public <T extends Base, U extends Base> Set<T> resolveRelated(Set<T> ts, Class<T> clazz, Set<ConstraintViolation<U>> constraintViolations) {
        Set<T> resolvedTs = new HashSet<T>();
        for(T t : ts) {
            T resolvedT = resolveRelated(t, clazz, constraintViolations);
            if(resolvedT != null) {
                resolvedTs.add(resolvedT);
            }
        }
        return resolvedTs;
    }
    
    public <T extends Base> T resolve(T t, Class<T> clazz) {
        if(!boundObjects.containsKey(clazz)) {
            boundObjects.put(clazz, new HashMap<String,Base>());
        }
        
        if(boundObjects.get(clazz).containsKey(t.getIdentifier())) {
            return (T)boundObjects.get(clazz).get(t.getIdentifier());
        } else {
            T persistedT = (T)lookup(t.getIdentifier(), clazz);
            if(persistedT == null) {
                return null;
            } else {
                boundObjects.get(clazz).put(t.getIdentifier(), persistedT);
                return persistedT;
            }
        }
    }
    
    public <T extends Base, U extends Base> T resolveRelated(T t, Class<T> clazz, Set<ConstraintViolation<U>> constraintViolations) {
        if(t == null) {
            return null;
        }
        if(!boundObjects.containsKey(clazz)) {
            boundObjects.put(clazz, new HashMap<String,Base>());
        }
        
        if(boundObjects.get(clazz).containsKey(t.getIdentifier())) {
            logger.info("Found " + clazz + " with identifier " + t.getIdentifier() + " from cache returning " + clazz + " with id " + boundObjects.get(clazz).get(t.getIdentifier()).getId());
            return (T)boundObjects.get(clazz).get(t.getIdentifier());
        } else {
            T persistedT = (T)lookup(t.getIdentifier(), clazz);
            if(persistedT == null) {
                logger.info("Didn't find " + clazz + " with identifier " + t.getIdentifier() + " from database returning new " + clazz);
                try {
                    T newT = (T)clazz.newInstance();
                    newT.setIdentifier(t.getIdentifier());
                    boundObjects.get(clazz).put(t.getIdentifier(), newT);
                    if(!newObjects.containsKey(clazz)) {
                        newObjects.put(clazz, new HashSet<Base>());
                    }
                    newObjects.get(clazz).add(newT);
                    return newT;
                } catch (ReflectiveOperationException re) {
                    throw new RuntimeException ("Unable to create instance of " + clazz, re );
                }
                
            } else {
                logger.info("Found " + clazz + " with identifier " + t.getIdentifier() + " from database returning " + clazz + " with id " + persistedT.getId());
                boundObjects.get(clazz).put(t.getIdentifier(), persistedT);
                return persistedT;
            }
        }
    }
    
    @Override
    public void beforeWrite(List<? extends Base> bases) {
        logger.info("beforeWrite, persisting new objects");
        for(Class clazz : newObjects.keySet()) {
            for(Base base : newObjects.get(clazz)) {
                logger.info("\tpersisting " + base.getIdentifier());
                save(base,clazz);
            }
        }
    }

    @Override
    public void afterWrite(List<? extends Base> bases) {
        
    }

    @Override
    public void onWriteError(Exception e, List<? extends Base> arg1) {
        
    }
    
    protected <T extends Base> T save(T t, Class<T> clazz) {
        if(clazz.equals(Taxon.class)) {
            return (T) taxonRepository.save((Taxon)t);
        } else if(clazz.equals(Dataset.class)) {
            return (T) datasetRepository.save((Dataset) t);
        } else if(clazz.equals(IdentificationKey.class)) {
            return (T) identificationKeyRepository.save((IdentificationKey)t);
        } else if(clazz.equals(Multimedia.class)) {
            return (T) multimediaRepository.save((Multimedia)t);
        } else if(clazz.equals(Node.class)) {
            return (T) nodeRepository.save((Node)t);
        } else if(clazz.equals(Reference.class)) {
            return (T) referenceRepository.save((Reference)t);
        } else if(clazz.equals(Term.class)) {
            return (T) termRepository.save((Term)t);
        } else if(clazz.equals(TypeAndSpecimen.class)) {
            return (T) typeAndSpecimenRepository.save((TypeAndSpecimen)t);
        } else if(clazz.equals(Distribution.class)) {
            return (T) distributionRepository.save((Distribution)t);
        } else if(clazz.equals(Description.class)) {
            return (T) descriptionRepository.save((Description)t);
        } else if(clazz.equals(MeasurementOrFact.class)) {
            return (T) measurementOrFactRepository.save((MeasurementOrFact)t);
        } else if(clazz.equals(VernacularName.class)) {
            return (T) vernacularNameRepository.save((VernacularName)t);
        } else {
            throw new IllegalArgumentException(clazz.getSimpleName() + " is not saveable");
        }
    }
    
    protected <T extends Base> boolean objectExistsInDatabase(String identifier, Class<T> clazz) {
        boolean exists = false;
        if(clazz.equals(Taxon.class)) {
            exists = taxonRepository.existsByTaxonId(identifier);
        } else if(clazz.equals(Dataset.class)) {
            exists = datasetRepository.existsByIdentifier(identifier);
        } else if(clazz.equals(IdentificationKey.class)) {
            exists = identificationKeyRepository.existsByIdentifier(identifier);
        } else if(clazz.equals(Multimedia.class)) {
            exists = multimediaRepository.existsByIdentifier(identifier);
        } else if(clazz.equals(Node.class)) {
            exists = nodeRepository.existsByIdentifier(identifier);
        } else if(clazz.equals(Reference.class)) {
            exists = referenceRepository.existsByIdentifier(identifier);
        } else if(clazz.equals(Term.class)) {
            exists = termRepository.existsByIdentifier(identifier);
        } else if(clazz.equals(TypeAndSpecimen.class)) {
            exists = typeAndSpecimenRepository.existsByOccurrenceId(identifier);
        } else if(clazz.equals(Distribution.class)) {
            exists = distributionRepository.existsByIdentifier(identifier);
        } else if(clazz.equals(Description.class)) {
            exists = descriptionRepository.existsByIdentifier(identifier);
        } else if(clazz.equals(MeasurementOrFact.class)) {
            exists = measurementOrFactRepository.existsByIdentifier(identifier);
        } else if(clazz.equals(VernacularName.class)) {
            exists = vernacularNameRepository.existsByIdentifier(identifier);
        } else {
            throw new IllegalArgumentException(clazz.getSimpleName() + " is not resolvable");
        }
        
        return exists;
    }
    
    protected <T extends Base> T lookup(String identifier, Class<T> clazz) {
        if(clazz.equals(Taxon.class)) {
            return (T)taxonRepository.findByTaxonId(identifier);
        } else if(clazz.equals(Dataset.class)) {
            return (T)datasetRepository.findByIdentifier(identifier);
        } else if(clazz.equals(IdentificationKey.class)) {
            return (T)identificationKeyRepository.findByIdentifier(identifier);
        } else if(clazz.equals(Multimedia.class)) {
            return (T)multimediaRepository.findByIdentifier(identifier);
        } else if(clazz.equals(Node.class)) {
            return (T)nodeRepository.findByIdentifier(identifier);
        } else if(clazz.equals(Reference.class)) {
            return (T)referenceRepository.findByIdentifier(identifier);
        } else if(clazz.equals(Term.class)) {
            return (T)termRepository.findByIdentifier(identifier);
        } else if(clazz.equals(TypeAndSpecimen.class)) {
            return (T)typeAndSpecimenRepository.findByOccurrenceId(identifier);
        } else if(clazz.equals(Distribution.class)) {
            return (T)distributionRepository.findByIdentifier(identifier);
        } else if(clazz.equals(Description.class)) {
            return (T)descriptionRepository.findByIdentifier(identifier);
        } else if(clazz.equals(MeasurementOrFact.class)) {
            return (T)measurementOrFactRepository.findByIdentifier(identifier);
        } else if(clazz.equals(VernacularName.class)) {
            return (T)vernacularNameRepository.findByIdentifier(identifier);
        } else {
            throw new IllegalArgumentException(clazz.getSimpleName() + " is not resolvable");
        }
    }

    @Override
    public void afterChunk(ChunkContext chunkContext) {
        logger.debug("afterChunk - clearing context");
        for(Class clazz : newObjects.keySet()) {
            newObjects.get(clazz).clear();
        }
        for(Class clazz : boundObjects.keySet()) {
            boundObjects.get(clazz).clear();
        }

    }

    @Override
    public void beforeChunk(ChunkContext chunkContext) {
        
    }
    
    public void afterChunkError(ChunkContext chunkContext) {
        logger.debug("afterChunkError - clearing context");
        for(Class clazz : boundObjects.keySet()) {
            boundObjects.get(clazz).clear();
        }
        for(Class clazz : newObjects.keySet()) {
            newObjects.get(clazz).clear();
        }
    }
    
    public <T extends Base> void register(Base base, Class<? extends Base> clazz, Boolean isNew) {
        if(isNew) {
            if(!newObjects.containsKey(clazz)) {
                newObjects.put(clazz, new HashSet<Base>());
            }
            
            newObjects.get(clazz).add(base);
        }
        register(base,clazz);
        
    }

    public <T extends Base> void register(Base base, Class<? extends Base> clazz) {
        if(!boundObjects.containsKey(clazz)) {
            boundObjects.put(clazz, new HashMap<String,Base>());
        }
        
        boundObjects.get(clazz).put(base.getIdentifier(), base);
        
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;        
    }

}
