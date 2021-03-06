package org.cateproject.batch.sync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.Validator;

import org.cateproject.batch.job.ResolutionService;
import org.cateproject.batch.job.darwincore.DarwinCoreFieldSetMapper;
import org.cateproject.batch.job.darwincore.DarwinCoreProcessor;
import org.cateproject.domain.Base;
import org.cateproject.domain.batch.BatchDataset;
import org.cateproject.domain.batch.BatchFile;
import org.cateproject.domain.batch.BatchLine;
import org.cateproject.domain.batch.TermFactory;
import org.cateproject.domain.sync.ChangeManifestChangeType;
import org.cateproject.repository.jpa.batch.BatchDatasetRepository;
import org.cateproject.repository.jpa.batch.BatchLineRepository;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.GbifTerm;
import org.gbif.dwc.terms.Term;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;

public class ChangeProcessor implements ItemWriteListener<BatchLine>, ChunkListener, ItemProcessor<BatchLine,BatchLine>, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(ChangeProcessor.class);

    @Autowired
    private BatchDatasetRepository batchDatasetRepository;

    @Autowired
    private BatchLineRepository batchLineRepository;

    @Autowired
    private TermFactory termFactory;
 
    @Autowired
    private ConversionService conversionService;

    @Autowired
    private ResolutionService resolutionService;

    @Autowired
    private Validator validator;

    protected String datasetIdentifier;

    protected BatchDataset batchDataset;

    protected Map<String,BatchFile> batchMetadata = new HashMap<String,BatchFile>();

    protected Map<String,LineMapper<Base>> lineMappers = new HashMap<String,LineMapper<Base>>();

    protected Map<String,DarwinCoreProcessor> itemProcessors = new HashMap<String,DarwinCoreProcessor>();

    protected Map<String,BatchLine> linesInChunk = new HashMap<String,BatchLine>();
   
    public ChangeProcessor(String datasetIdentifier) {
        this.datasetIdentifier = datasetIdentifier;
    }

    public void setResolutionService(ResolutionService resolutionService) {
        this.resolutionService = resolutionService;
    }

    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    public void setBatchLineRepository(BatchLineRepository batchLineRepository) {
        this.batchLineRepository = batchLineRepository;
    }

    public void setBatchDatasetRepository(BatchDatasetRepository batchDatasetRepository) {
        this.batchDatasetRepository = batchDatasetRepository;
    }

    public void setTermFactory(TermFactory termFactory) {
        this.termFactory = termFactory;
    }

    public void afterPropertiesSet() {
       this.batchDataset = batchDatasetRepository.findByIdentifier(datasetIdentifier);

       for(BatchFile batchFile : batchDataset.getFiles()) {
          batchMetadata.put(batchFile.getLocation(), batchFile);
          lineMappers.put(batchFile.getLocation(), buildLineMapper(batchDataset, batchFile));
          itemProcessors.put(batchFile.getLocation(), buildItemProcessor(batchDataset, batchFile));
       }
    }

    public DarwinCoreProcessor buildItemProcessor(BatchDataset batchDataset, BatchFile batchFile) {
        Term term = termFactory.findTerm(batchFile.getRowType());
        DarwinCoreProcessor itemProcessor;
        if (term ==  DwcTerm.Taxon) {
            itemProcessor = new org.cateproject.batch.job.darwincore.taxon.Processor();
        } else if (term == GbifTerm.Reference) {
            itemProcessor = new org.cateproject.batch.job.darwincore.reference.Processor();
        } else {
            throw new RuntimeException(String.format("Could not find an item processor for row type %s", batchFile.getRowType()));
        }
        itemProcessor.setValidator(validator);
        itemProcessor.setResolutionService(resolutionService);
        return itemProcessor;
    }

    public LineTokenizer buildLineTokenizer(BatchDataset batchDataset, BatchFile batchFile) {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(batchFile.getFieldsTerminatedBy());
        if(batchFile.getFieldsEnclosedBy() != null) {
            lineTokenizer.setQuoteCharacter(batchFile.getFieldsEnclosedBy());
        }
        lineTokenizer.setNames(batchFile.getFieldNames(batchLineRepository.findMaxNumberOfColumnsByFile(batchFile.getId())));
        return lineTokenizer;
    }

    public FieldSetMapper<Base> buildFieldSetMapper(BatchDataset batchDataset, BatchFile batchFile) {
        Term term = termFactory.findTerm(batchFile.getRowType());
        DarwinCoreFieldSetMapper fieldSetMapper;
        if (term ==  DwcTerm.Taxon) {
            fieldSetMapper = new org.cateproject.batch.job.darwincore.taxon.FieldSetMapper();
        } else if (term == GbifTerm.Reference) {
            fieldSetMapper = new org.cateproject.batch.job.darwincore.reference.FieldSetMapper(); 
        } else {
            throw new RuntimeException(String.format("Could not find fieldset mapper for row type %s", batchFile.getRowType()));
        }
        fieldSetMapper.setConversionService(conversionService);
        fieldSetMapper.setTermFactory(termFactory);
        fieldSetMapper.setFieldNames(batchFile.getFieldNames(batchLineRepository.findMaxNumberOfColumnsByFile(batchFile.getId())));
        fieldSetMapper.setDefaultValues(batchFile.getDefaultValues());
        return fieldSetMapper;
    }
   
    public LineMapper<Base> buildLineMapper(BatchDataset batchDataset, BatchFile batchFile) {
        DefaultLineMapper<Base> lineMapper = new DefaultLineMapper<Base>();
        lineMapper.setFieldSetMapper(buildFieldSetMapper(batchDataset, batchFile));
        lineMapper.setLineTokenizer(buildLineTokenizer(batchDataset, batchFile));
        lineMapper.afterPropertiesSet();
        return lineMapper;
    } 
 
    public BatchLine process(BatchLine batchLine) throws Exception {
        String file = getFile(batchLine.getChangeManifestUrl().getMd().getPath());
        doRead(batchLine, file);
        return doProcess(batchLine, file);
    }

    public String getFile(String path) {
        Pattern pattern = Pattern.compile("\\/(.*?)#row=(\\d+)");
        Matcher matcher = pattern.matcher(path);
        if(matcher.matches()) {
            return matcher.group(1); 
        } else {
            throw new RuntimeException(String.format("String '%s' does not match expected regex", 
                                                     new Object[] { path })); 
        }
    }

    public void doRead(BatchLine batchLine, String file) throws Exception {
        LineMapper<Base> lineMapper = lineMappers.get(file);
        batchLine.setEntity(lineMapper.mapLine(batchLine.getLine(),batchLine.getLineNumber()));
    }

    public BatchLine doProcess(BatchLine batchLine, String file) throws Exception {
        DarwinCoreProcessor itemProcessor = itemProcessors.get(file); 

        if(linesInChunk.containsKey(batchLine.getChangeManifestUrl().getLoc())) {
            // A line has already been returned for this object so squash the changes as 
            // they will be committed together
            BatchLine existingLine = linesInChunk.get(batchLine.getChangeManifestUrl().getLoc());
            itemProcessor.remove(existingLine.getEntity());
            switch(existingLine.getChangeManifestUrl().getMd().getChange()) {
                case created:
                    switch(batchLine.getChangeManifestUrl().getMd().getChange()) {
                        case updated:
                            // Keep the change as created 
                            existingLine.setEntity(itemProcessor.process(batchLine.getEntity()));
                            break;
                        case deleted:
                            // Skip the change as the create and delete happen in the same chunk
                            existingLine.getChangeManifestUrl().getMd().setChange(ChangeManifestChangeType.skipped);
                            break;
                        // Invalid changes following a create
                        case created:
                        case skipped:
                        default:
                            throw new RuntimeException(String.format("Unexpected change type %s for line %s, object has already been %s by line %s", 
                                new Object[]{ batchLine.getChangeManifestUrl().getMd().getChange(),
                                              batchLine.toString(),
                                              existingLine.getChangeManifestUrl().getMd().getChange(),
                                              existingLine.toString() }));
                    }
                    break;
                case updated:
                    switch(batchLine.getChangeManifestUrl().getMd().getChange()) {
                        case updated:
                            existingLine.setEntity(itemProcessor.process(batchLine.getEntity()));
                            break;
                        case deleted:
                            existingLine.getChangeManifestUrl().getMd().setChange(ChangeManifestChangeType.deleted);
                            break;
                        // Invalid changes following a create
                        case created:
                        case skipped:
                        default:
                            throw new RuntimeException(String.format("Unexpected change type %s for line %s, object has already been %s by line %s", 
                                new Object[]{ batchLine.getChangeManifestUrl().getMd().getChange(),
                                              batchLine.toString(),
                                              existingLine.getChangeManifestUrl().getMd().getChange(),
                                              existingLine.toString() }));
                    }
                    break;
                // Any change following a delete or a skip are invalid
                case deleted:
                case skipped:
                default:
                    throw new RuntimeException(String.format("Unexpected change type %s for line %s, object has already been %s by line %s", 
                            new Object[]{ batchLine.getChangeManifestUrl().getMd().getChange(),
                                          batchLine.toString(),
                                          existingLine.getChangeManifestUrl().getMd().getChange(),
                                          existingLine.toString() }));
            } 
            return null;
        } else {
            batchLine.setEntity(itemProcessor.process(batchLine.getEntity()));
            return batchLine;
        }
    }

    public void afterWrite(List<? extends BatchLine> items) {
        for(String k : itemProcessors.keySet()) {
            itemProcessors.get(k).afterWrite(items);
        }
    }

    @Override
    public void beforeWrite(List<? extends BatchLine> items) {
        logger.info("beforeWrite, calling beforeWrite on itemProcessors");
        Map<Class,List> entities = new HashMap<Class, List>();
        for(BatchLine l : items) {
            Object o = l.getEntity();
            if(!entities.containsKey(o.getClass())) {
                entities.put(o.getClass(), new ArrayList());
            }
            entities.get(o.getClass()).add(o);
        }
        for(DarwinCoreProcessor itemProcessor : itemProcessors.values()) {
            if(entities.containsKey(itemProcessor.getType())) {
                itemProcessor.beforeWrite(entities.get(itemProcessor.getType()));
            }
        }
    }

    public void onWriteError(Exception e, List<? extends BatchLine> items) {
        for(String k : itemProcessors.keySet()) {
            itemProcessors.get(k).onWriteError(e, items);
        }
    }

    public void afterChunk(ChunkContext chunkContext) {
        for(String k : itemProcessors.keySet()) {
            itemProcessors.get(k).afterChunk(chunkContext);
        }
    }

    public void beforeChunk(ChunkContext chunkContext) {
        linesInChunk = new HashMap<String,BatchLine>();
        for(String k : itemProcessors.keySet()) {
            itemProcessors.get(k).beforeChunk(chunkContext);
        }
    }
    
    public void afterChunkError(ChunkContext chunkContext) {
        for(String k : itemProcessors.keySet()) {
            itemProcessors.get(k).afterChunkError(chunkContext);
        }
    }
}
