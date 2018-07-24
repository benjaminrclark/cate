package org.cateproject.batch.sync;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Validator;

import org.cateproject.batch.job.ResolutionService;
import org.cateproject.batch.job.darwincore.DarwinCoreFieldSetMapper;
import org.cateproject.batch.job.darwincore.DarwinCoreProcessor;
import org.cateproject.domain.Base;
import org.cateproject.domain.Taxon;
import org.cateproject.domain.batch.BatchDataset;
import org.cateproject.domain.batch.BatchFile;
import org.cateproject.domain.batch.BatchLine;
import org.cateproject.domain.batch.TermFactory;
import org.cateproject.domain.sync.ChangeManifestChange;
import org.cateproject.domain.sync.ChangeManifestChangeType;
import org.cateproject.domain.sync.ChangeManifestUrl;
import org.cateproject.repository.jpa.batch.BatchDatasetRepository;
import org.cateproject.repository.jpa.batch.BatchLineRepository;

import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.GbifTerm;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.test.MetaDataInstanceFactory;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import org.springframework.core.convert.ConversionService;

public class ChangeProcessorTest {

    ChangeProcessor changeProcessor;

    ResolutionService resolutionService;

    ConversionService conversionService;

    Validator validator;

    BatchDatasetRepository batchDatasetRepository;
 
    BatchLineRepository batchLineRepository;

    TermFactory termFactory;

    BatchDataset batchDataset;

    BatchLine batchLine;

    BatchFile batchFile;

    @Before
    public void setUp() throws Exception {
        changeProcessor = new ChangeProcessor("datasetIdentifier");
        
        resolutionService = EasyMock.createMock(ResolutionService.class);

        conversionService = EasyMock.createMock(ConversionService.class);

        validator = EasyMock.createMock(Validator.class);

        batchDatasetRepository = EasyMock.createMock(BatchDatasetRepository.class);

        batchLineRepository = EasyMock.createMock(BatchLineRepository.class);

        termFactory = EasyMock.createMock(TermFactory.class);

        changeProcessor.setResolutionService(resolutionService);
        changeProcessor.setConversionService(conversionService);
        changeProcessor.setValidator(validator);
        changeProcessor.setBatchDatasetRepository(batchDatasetRepository);
        changeProcessor.setBatchLineRepository(batchLineRepository);
        changeProcessor.setTermFactory(termFactory);

        batchDataset = new BatchDataset();
        batchDataset.setIdentifier("datasetIdentifier");
        
        batchFile = new BatchFile();
        
        batchLine = new BatchLine();
    }

    @Test
    public void testBuildTaxonProcessor() {
        batchFile.setRowType("http://rs.tdwg.org/dwc/terms/Taxon");
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("http://rs.tdwg.org/dwc/terms/Taxon"))).andReturn(DwcTerm.Taxon);

        EasyMock.replay(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory);
        assertEquals("buildItemProcessor should return a taxon processor", org.cateproject.batch.job.darwincore.taxon.Processor.class, changeProcessor.buildItemProcessor(batchDataset, batchFile).getClass()); 
        EasyMock.verify(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory);
    }

    @Test
    public void testBuildReferenceProcessor() {
        batchFile.setRowType("http://rs.gbif.org/terms/1.0/Reference");
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("http://rs.gbif.org/terms/1.0/Reference"))).andReturn(GbifTerm.Reference);

        EasyMock.replay(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory);
        assertEquals("buildItemProcessor should return a reference processor", org.cateproject.batch.job.darwincore.reference.Processor.class, changeProcessor.buildItemProcessor(batchDataset, batchFile).getClass()); 
        EasyMock.verify(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory);
    }

    @Test(expected = RuntimeException.class)
    public void testBuildUnknownProcessor() {
        batchFile.setRowType("http://purl.org/dc/terms/date");
        EasyMock.expect(termFactory.findTerm(EasyMock.eq("http://purl.org/dc/terms/date"))).andReturn(DcTerm.date);

        EasyMock.replay(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory);
        changeProcessor.buildItemProcessor(batchDataset, batchFile).getClass(); 
    }

    @Test
    public void testBuildLineTokenizerWithoutQuoteChar() {
        batchFile.setFieldsTerminatedBy(",");
        batchFile.setId(1L);
        EasyMock.expect(batchLineRepository.findMaxNumberOfColumnsByFile(EasyMock.eq(1L))).andReturn(1);

        EasyMock.replay(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory);
        assertEquals("buildLineTokenizer should return a lineTokenizer", DelimitedLineTokenizer.class, changeProcessor.buildLineTokenizer(batchDataset, batchFile).getClass()); 
        EasyMock.verify(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory);
    }

    @Test
    public void testBuildLineTokenizerWithQuoteChar() {
        batchFile.setFieldsTerminatedBy(",");
        batchFile.setFieldsEnclosedBy('\"');
        batchFile.setId(1L);
        EasyMock.expect(batchLineRepository.findMaxNumberOfColumnsByFile(EasyMock.eq(1L))).andReturn(1);

        EasyMock.replay(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory);
        assertEquals("buildLineTokenizer should return a lineTokenizer", DelimitedLineTokenizer.class, changeProcessor.buildLineTokenizer(batchDataset, batchFile).getClass()); 
        EasyMock.verify(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory);
    }

    @Test
    public void testBuildTaxonFieldSetMapper() {
        batchFile.setRowType("http://rs.tdwg.org/dwc/terms/Taxon");
        batchFile.setFieldsTerminatedBy(",");
        batchFile.setFieldsEnclosedBy('\"');
        batchFile.setId(1L);

        EasyMock.expect(termFactory.findTerm(EasyMock.eq("http://rs.tdwg.org/dwc/terms/Taxon"))).andReturn(DwcTerm.Taxon);
        EasyMock.expect(batchLineRepository.findMaxNumberOfColumnsByFile(EasyMock.eq(1L))).andReturn(1);

        EasyMock.replay(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory);

        assertEquals("buildFieldSetMapper should return a taxon fieldSetMapper", org.cateproject.batch.job.darwincore.taxon.FieldSetMapper.class, changeProcessor.buildFieldSetMapper(batchDataset, batchFile).getClass()); 
        EasyMock.verify(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory);
    }

    @Test
    public void testBuildReferenceFieldSetMapper() {
        batchFile.setRowType("http://rs.gbif.org/terms/1.0/Reference");
        batchFile.setFieldsTerminatedBy(",");
        batchFile.setFieldsEnclosedBy('\"');
        batchFile.setId(1L);

        EasyMock.expect(termFactory.findTerm(EasyMock.eq("http://rs.gbif.org/terms/1.0/Reference"))).andReturn(GbifTerm.Reference);
        EasyMock.expect(batchLineRepository.findMaxNumberOfColumnsByFile(EasyMock.eq(1L))).andReturn(1);

        EasyMock.replay(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory);

        assertEquals("buildFieldSetMapper should return a reference fieldSetMapper", org.cateproject.batch.job.darwincore.reference.FieldSetMapper.class, changeProcessor.buildFieldSetMapper(batchDataset, batchFile).getClass()); 
        EasyMock.verify(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory);
    }

    @Test(expected = RuntimeException.class)
    public void testBuildUnknownFieldSetMapper() {
        batchFile.setRowType("http://purl.org/dc/terms/date");
        batchFile.setFieldsTerminatedBy(",");
        batchFile.setFieldsEnclosedBy('\"');
        batchFile.setId(1L);

        EasyMock.expect(termFactory.findTerm(EasyMock.eq("http://purl.org/dc/terms/date"))).andReturn(DcTerm.creator);
        EasyMock.replay(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory);

        changeProcessor.buildFieldSetMapper(batchDataset, batchFile); 
    }

    @Test
    public void testBuildLineMapper() {
        batchFile.setRowType("http://rs.tdwg.org/dwc/terms/Taxon");
        batchFile.setFieldsTerminatedBy(",");
        batchFile.setFieldsEnclosedBy('\"');
        batchFile.setId(1L);

        EasyMock.expect(termFactory.findTerm(EasyMock.eq("http://rs.tdwg.org/dwc/terms/Taxon"))).andReturn(DwcTerm.Taxon);
        EasyMock.expect(batchLineRepository.findMaxNumberOfColumnsByFile(EasyMock.eq(1L))).andReturn(1).times(2);

        EasyMock.replay(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory);

        assertEquals("buildFieldSetMapper should return a DefaultLineMapper", DefaultLineMapper.class, changeProcessor.buildLineMapper(batchDataset, batchFile).getClass()); 
        EasyMock.verify(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory);
    }

    @Test
    public void testGetFile() {
        assertEquals("getFile should return the file part of the path", "taxon.txt", changeProcessor.getFile("/taxon.txt#row=11")); 
    }

    @Test(expected = RuntimeException.class)
    public void testGetFileWithInvalidPath() {
        changeProcessor.getFile("taxon.txt#row=11"); 
    }

    @Test
    public void testAfterPropertiesSet() {
        batchFile.setRowType("http://rs.tdwg.org/dwc/terms/Taxon");
        batchFile.setFieldsTerminatedBy(",");
        batchFile.setFieldsEnclosedBy('\"');
        batchFile.setLocation("location");
        batchFile.setId(1L);
        batchDataset.getFiles().add(batchFile);

        EasyMock.expect(termFactory.findTerm(EasyMock.eq("http://rs.tdwg.org/dwc/terms/Taxon"))).andReturn(DwcTerm.Taxon).times(2);
        EasyMock.expect(batchLineRepository.findMaxNumberOfColumnsByFile(EasyMock.eq(1L))).andReturn(1).times(2);
        EasyMock.expect(batchDatasetRepository.findByIdentifier(EasyMock.eq("datasetIdentifier"))).andReturn(batchDataset);

        EasyMock.replay(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory);

        changeProcessor.afterPropertiesSet(); 
        EasyMock.verify(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory);
    }

    @Test
    public void testDoRead() throws Exception {
        Taxon taxon = new Taxon();
        taxon.setIdentifier("taxonIdentifier");
        batchLine.setLine("line");
        batchLine.setLineNumber(1);
        LineMapper<Base> lineMapper = (LineMapper<Base>)EasyMock.createMock(LineMapper.class);
        changeProcessor.lineMappers.put("file", lineMapper);
        EasyMock.expect(lineMapper.mapLine(EasyMock.eq("line"), EasyMock.eq(1))).andReturn(taxon);

        EasyMock.replay(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory, lineMapper);
        changeProcessor.doRead(batchLine, "file");
        assertEquals("doRead should set the taxon in batchLine", taxon, batchLine.getEntity()); 
        EasyMock.verify(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory, lineMapper);
    }

    @Test
    public void testDoProcess() throws Exception {
        Taxon taxon1 = new Taxon();
        taxon1.setIdentifier("taxonIdentifier1");
        Taxon taxon2 = new Taxon();
        taxon2.setIdentifier("taxonIdentifier2");
        batchLine.setLine("line");
        batchLine.setLineNumber(1);
        batchLine.setEntity(taxon1);
        batchLine.setChangeManifestUrl(new ChangeManifestUrl());
        batchLine.getChangeManifestUrl().setMd(new ChangeManifestChange());
        batchLine.getChangeManifestUrl().setLoc("loc");
        DarwinCoreProcessor itemProcessor = (DarwinCoreProcessor)EasyMock.createMock(DarwinCoreProcessor.class);
        changeProcessor.itemProcessors.put("file", itemProcessor);
        EasyMock.expect(itemProcessor.process(EasyMock.eq(taxon1))).andReturn(taxon2);

        EasyMock.replay(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory, itemProcessor);
        assertEquals("doProcess should return the batch line", batchLine, changeProcessor.doProcess(batchLine, "file"));
        assertEquals("doProcess should update the taxon in batchLine", taxon2, batchLine.getEntity()); 
        EasyMock.verify(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory, itemProcessor);
    }

    @Test
    public void testDoProcessExistingCreatedUpdated() throws Exception {
        Taxon taxon1 = new Taxon();
        taxon1.setIdentifier("taxonIdentifier1");
        Taxon taxon2 = new Taxon();
        taxon2.setIdentifier("taxonIdentifier2");
        Taxon taxon3 = new Taxon();
        taxon3.setIdentifier("taxonIdentifier3");
        batchLine.setLine("line");
        batchLine.setLineNumber(1);
        batchLine.setEntity(taxon1);
        batchLine.setChangeManifestUrl(new ChangeManifestUrl());
        batchLine.getChangeManifestUrl().setMd(new ChangeManifestChange());
        batchLine.getChangeManifestUrl().getMd().setChange(ChangeManifestChangeType.updated);
        batchLine.getChangeManifestUrl().setLoc("loc");

        BatchLine existingLine = new BatchLine();
        existingLine.setEntity(taxon3);
        existingLine.setChangeManifestUrl(new ChangeManifestUrl());
        existingLine.getChangeManifestUrl().setMd(new ChangeManifestChange());
        existingLine.getChangeManifestUrl().getMd().setChange(ChangeManifestChangeType.created);
        existingLine.getChangeManifestUrl().setLoc("loc");
        changeProcessor.linesInChunk.put("loc", existingLine);

        DarwinCoreProcessor itemProcessor = (DarwinCoreProcessor)EasyMock.createMock(DarwinCoreProcessor.class);
        changeProcessor.itemProcessors.put("file", itemProcessor);
        itemProcessor.remove(EasyMock.eq(taxon3));
        EasyMock.expect(itemProcessor.process(EasyMock.eq(taxon1))).andReturn(taxon2);

        EasyMock.replay(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory, itemProcessor);
        assertNull("doProcess should return the batch line", changeProcessor.doProcess(batchLine, "file"));
        assertEquals("doProcess should update the taxon in batchLine", taxon2, existingLine.getEntity()); 
        EasyMock.verify(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory, itemProcessor);
    }

    @Test
    public void testDoProcessExistingCreatedDeleted() throws Exception {
        Taxon taxon1 = new Taxon();
        taxon1.setIdentifier("taxonIdentifier1");
        Taxon taxon2 = new Taxon();
        taxon2.setIdentifier("taxonIdentifier2");
        Taxon taxon3 = new Taxon();
        taxon3.setIdentifier("taxonIdentifier3");
        batchLine.setLine("line");
        batchLine.setLineNumber(1);
        batchLine.setEntity(taxon1);
        batchLine.setChangeManifestUrl(new ChangeManifestUrl());
        batchLine.getChangeManifestUrl().setMd(new ChangeManifestChange());
        batchLine.getChangeManifestUrl().getMd().setChange(ChangeManifestChangeType.deleted);
        batchLine.getChangeManifestUrl().setLoc("loc");

        BatchLine existingLine = new BatchLine();
        existingLine.setEntity(taxon3);
        existingLine.setChangeManifestUrl(new ChangeManifestUrl());
        existingLine.getChangeManifestUrl().setMd(new ChangeManifestChange());
        existingLine.getChangeManifestUrl().getMd().setChange(ChangeManifestChangeType.created);
        existingLine.getChangeManifestUrl().setLoc("loc");
        changeProcessor.linesInChunk.put("loc", existingLine);

        DarwinCoreProcessor itemProcessor = (DarwinCoreProcessor)EasyMock.createMock(DarwinCoreProcessor.class);
        changeProcessor.itemProcessors.put("file", itemProcessor);
        itemProcessor.remove(EasyMock.eq(taxon3));

        EasyMock.replay(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory, itemProcessor);
        assertNull("doProcess should return the batch line", changeProcessor.doProcess(batchLine, "file"));
        assertEquals("doProcess should not update the taxon in batchLine", taxon3, existingLine.getEntity()); 
        assertEquals("doProcess should set the change type to 'skipped'", ChangeManifestChangeType.skipped, existingLine.getChangeManifestUrl().getMd().getChange()); 
        EasyMock.verify(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory, itemProcessor);
    }

    @Test(expected = RuntimeException.class)
    public void testDoProcessExistingCreatedCreated() throws Exception {
        Taxon taxon1 = new Taxon();
        taxon1.setIdentifier("taxonIdentifier1");
        Taxon taxon2 = new Taxon();
        taxon2.setIdentifier("taxonIdentifier2");
        Taxon taxon3 = new Taxon();
        taxon3.setIdentifier("taxonIdentifier3");
        batchLine.setLine("line");
        batchLine.setLineNumber(1);
        batchLine.setEntity(taxon1);
        batchLine.setChangeManifestUrl(new ChangeManifestUrl());
        batchLine.getChangeManifestUrl().setMd(new ChangeManifestChange());
        batchLine.getChangeManifestUrl().getMd().setChange(ChangeManifestChangeType.created);
        batchLine.getChangeManifestUrl().setLoc("loc");

        BatchLine existingLine = new BatchLine();
        existingLine.setEntity(taxon3);
        existingLine.setChangeManifestUrl(new ChangeManifestUrl());
        existingLine.getChangeManifestUrl().setMd(new ChangeManifestChange());
        existingLine.getChangeManifestUrl().getMd().setChange(ChangeManifestChangeType.created);
        existingLine.getChangeManifestUrl().setLoc("loc");
        changeProcessor.linesInChunk.put("loc", existingLine);

        DarwinCoreProcessor itemProcessor = (DarwinCoreProcessor)EasyMock.createMock(DarwinCoreProcessor.class);
        changeProcessor.itemProcessors.put("file", itemProcessor);
        itemProcessor.remove(EasyMock.eq(taxon3));

        EasyMock.replay(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory, itemProcessor);
        changeProcessor.doProcess(batchLine, "file");
    }

    @Test
    public void testDoProcessExistingUpdatedUpdated() throws Exception {
        Taxon taxon1 = new Taxon();
        taxon1.setIdentifier("taxonIdentifier1");
        Taxon taxon2 = new Taxon();
        taxon2.setIdentifier("taxonIdentifier2");
        Taxon taxon3 = new Taxon();
        taxon3.setIdentifier("taxonIdentifier3");
        batchLine.setLine("line");
        batchLine.setLineNumber(1);
        batchLine.setEntity(taxon1);
        batchLine.setChangeManifestUrl(new ChangeManifestUrl());
        batchLine.getChangeManifestUrl().setMd(new ChangeManifestChange());
        batchLine.getChangeManifestUrl().getMd().setChange(ChangeManifestChangeType.updated);
        batchLine.getChangeManifestUrl().setLoc("loc");

        BatchLine existingLine = new BatchLine();
        existingLine.setEntity(taxon3);
        existingLine.setChangeManifestUrl(new ChangeManifestUrl());
        existingLine.getChangeManifestUrl().setMd(new ChangeManifestChange());
        existingLine.getChangeManifestUrl().getMd().setChange(ChangeManifestChangeType.updated);
        existingLine.getChangeManifestUrl().setLoc("loc");
        changeProcessor.linesInChunk.put("loc", existingLine);

        DarwinCoreProcessor itemProcessor = (DarwinCoreProcessor)EasyMock.createMock(DarwinCoreProcessor.class);
        changeProcessor.itemProcessors.put("file", itemProcessor);
        itemProcessor.remove(EasyMock.eq(taxon3));
        EasyMock.expect(itemProcessor.process(EasyMock.eq(taxon1))).andReturn(taxon2);

        EasyMock.replay(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory, itemProcessor);
        assertNull("doProcess should return the batch line", changeProcessor.doProcess(batchLine, "file"));
        assertEquals("doProcess should update the taxon in batchLine", taxon2, existingLine.getEntity()); 
        EasyMock.verify(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory, itemProcessor);
    }

    @Test
    public void testDoProcessExistingUpdatedDeleted() throws Exception {
        Taxon taxon1 = new Taxon();
        taxon1.setIdentifier("taxonIdentifier1");
        Taxon taxon2 = new Taxon();
        taxon2.setIdentifier("taxonIdentifier2");
        Taxon taxon3 = new Taxon();
        taxon3.setIdentifier("taxonIdentifier3");
        batchLine.setLine("line");
        batchLine.setLineNumber(1);
        batchLine.setEntity(taxon1);
        batchLine.setChangeManifestUrl(new ChangeManifestUrl());
        batchLine.getChangeManifestUrl().setMd(new ChangeManifestChange());
        batchLine.getChangeManifestUrl().getMd().setChange(ChangeManifestChangeType.deleted);
        batchLine.getChangeManifestUrl().setLoc("loc");

        BatchLine existingLine = new BatchLine();
        existingLine.setEntity(taxon3);
        existingLine.setChangeManifestUrl(new ChangeManifestUrl());
        existingLine.getChangeManifestUrl().setMd(new ChangeManifestChange());
        existingLine.getChangeManifestUrl().getMd().setChange(ChangeManifestChangeType.updated);
        existingLine.getChangeManifestUrl().setLoc("loc");
        changeProcessor.linesInChunk.put("loc", existingLine);

        DarwinCoreProcessor itemProcessor = (DarwinCoreProcessor)EasyMock.createMock(DarwinCoreProcessor.class);
        changeProcessor.itemProcessors.put("file", itemProcessor);
        itemProcessor.remove(EasyMock.eq(taxon3));

        EasyMock.replay(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory, itemProcessor);
        assertNull("doProcess should return the batch line", changeProcessor.doProcess(batchLine, "file"));
        assertEquals("doProcess should not update the taxon in batchLine", taxon3, existingLine.getEntity()); 
        assertEquals("doProcess should set the change type to 'deleted'", ChangeManifestChangeType.deleted, existingLine.getChangeManifestUrl().getMd().getChange()); 
        EasyMock.verify(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory, itemProcessor);
    }


    @Test(expected = RuntimeException.class)
    public void testDoProcessExistingUpdatedCreated() throws Exception {
        Taxon taxon1 = new Taxon();
        taxon1.setIdentifier("taxonIdentifier1");
        Taxon taxon2 = new Taxon();
        taxon2.setIdentifier("taxonIdentifier2");
        Taxon taxon3 = new Taxon();
        taxon3.setIdentifier("taxonIdentifier3");
        batchLine.setLine("line");
        batchLine.setLineNumber(1);
        batchLine.setEntity(taxon1);
        batchLine.setChangeManifestUrl(new ChangeManifestUrl());
        batchLine.getChangeManifestUrl().setMd(new ChangeManifestChange());
        batchLine.getChangeManifestUrl().getMd().setChange(ChangeManifestChangeType.created);
        batchLine.getChangeManifestUrl().setLoc("loc");

        BatchLine existingLine = new BatchLine();
        existingLine.setEntity(taxon3);
        existingLine.setChangeManifestUrl(new ChangeManifestUrl());
        existingLine.getChangeManifestUrl().setMd(new ChangeManifestChange());
        existingLine.getChangeManifestUrl().getMd().setChange(ChangeManifestChangeType.updated);
        existingLine.getChangeManifestUrl().setLoc("loc");
        changeProcessor.linesInChunk.put("loc", existingLine);

        DarwinCoreProcessor itemProcessor = (DarwinCoreProcessor)EasyMock.createMock(DarwinCoreProcessor.class);
        changeProcessor.itemProcessors.put("file", itemProcessor);
        itemProcessor.remove(EasyMock.eq(taxon3));

        EasyMock.replay(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory, itemProcessor);
        changeProcessor.doProcess(batchLine, "file");
    }

    @Test(expected = RuntimeException.class)
    public void testDoProcessExistingDeleted() throws Exception {
        Taxon taxon1 = new Taxon();
        taxon1.setIdentifier("taxonIdentifier1");
        Taxon taxon2 = new Taxon();
        taxon2.setIdentifier("taxonIdentifier2");
        Taxon taxon3 = new Taxon();
        taxon3.setIdentifier("taxonIdentifier3");
        batchLine.setLine("line");
        batchLine.setLineNumber(1);
        batchLine.setEntity(taxon1);
        batchLine.setChangeManifestUrl(new ChangeManifestUrl());
        batchLine.getChangeManifestUrl().setMd(new ChangeManifestChange());
        batchLine.getChangeManifestUrl().setLoc("loc");

        BatchLine existingLine = new BatchLine();
        existingLine.setEntity(taxon3);
        existingLine.setChangeManifestUrl(new ChangeManifestUrl());
        existingLine.getChangeManifestUrl().setMd(new ChangeManifestChange());
        existingLine.getChangeManifestUrl().getMd().setChange(ChangeManifestChangeType.deleted);
        existingLine.getChangeManifestUrl().setLoc("loc");
        changeProcessor.linesInChunk.put("loc", existingLine);

        DarwinCoreProcessor itemProcessor = (DarwinCoreProcessor)EasyMock.createMock(DarwinCoreProcessor.class);
        changeProcessor.itemProcessors.put("file", itemProcessor);
        itemProcessor.remove(EasyMock.eq(taxon3));

        EasyMock.replay(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory, itemProcessor);
        changeProcessor.doProcess(batchLine, "file");
    }

    @Test
    public void testBeforeChunk() {
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();
        StepContext stepContext = new StepContext(stepExecution);
        ChunkContext chunkContext = new ChunkContext(stepContext);    
        DarwinCoreProcessor itemProcessor = (DarwinCoreProcessor)EasyMock.createMock(DarwinCoreProcessor.class);
        changeProcessor.itemProcessors.put("file", itemProcessor);
        itemProcessor.beforeChunk(EasyMock.eq(chunkContext));

        EasyMock.replay(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory, itemProcessor);
        changeProcessor.beforeChunk(chunkContext); 
        EasyMock.verify(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory, itemProcessor);
    }

    @Test
    public void testAfterChunk() {
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();
        StepContext stepContext = new StepContext(stepExecution);
        ChunkContext chunkContext = new ChunkContext(stepContext);    
        DarwinCoreProcessor itemProcessor = (DarwinCoreProcessor)EasyMock.createMock(DarwinCoreProcessor.class);
        changeProcessor.itemProcessors.put("file", itemProcessor);
        itemProcessor.afterChunk(EasyMock.eq(chunkContext));

        EasyMock.replay(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory, itemProcessor);
        changeProcessor.afterChunk(chunkContext); 
        EasyMock.verify(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory, itemProcessor);
    }

    @Test
    public void testAfterChunkError() {
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();
        StepContext stepContext = new StepContext(stepExecution);
        ChunkContext chunkContext = new ChunkContext(stepContext);    
        DarwinCoreProcessor itemProcessor = (DarwinCoreProcessor)EasyMock.createMock(DarwinCoreProcessor.class);
        changeProcessor.itemProcessors.put("file", itemProcessor);
        itemProcessor.afterChunkError(EasyMock.eq(chunkContext));

        EasyMock.replay(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory, itemProcessor);
        changeProcessor.afterChunkError(chunkContext); 
        EasyMock.verify(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory, itemProcessor);
    }

    @Test
    public void testBeforeWrite() {
        Taxon taxon = new Taxon();
        taxon.setIdentifier("identifier");

        BatchLine batchLine = new BatchLine();
        batchLine.setEntity(taxon);

        List<BatchLine> items = new ArrayList<BatchLine>();
        items.add(batchLine);

        List<Taxon> taxa = new ArrayList<Taxon>();
        taxa.add(taxon);
        DarwinCoreProcessor itemProcessor = (DarwinCoreProcessor)EasyMock.createMock(DarwinCoreProcessor.class);
        changeProcessor.itemProcessors.put("file", itemProcessor);
        EasyMock.expect(itemProcessor.getType()).andReturn(Taxon.class).times(2);
        itemProcessor.beforeWrite(EasyMock.eq(taxa));

        EasyMock.replay(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory, itemProcessor);
        changeProcessor.beforeWrite(items); 
        EasyMock.verify(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory, itemProcessor);
    }

    @Test
    public void testAfterWrite() {
        List<BatchLine> items = new ArrayList<BatchLine>();
        DarwinCoreProcessor itemProcessor = (DarwinCoreProcessor)EasyMock.createMock(DarwinCoreProcessor.class);
        changeProcessor.itemProcessors.put("file", itemProcessor);
        itemProcessor.afterWrite(EasyMock.eq(items));

        EasyMock.replay(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory, itemProcessor);
        changeProcessor.afterWrite(items); 
        EasyMock.verify(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory, itemProcessor);
    }

    @Test
    public void testOnWriteError() {
        List<BatchLine> items = new ArrayList<BatchLine>();
        DarwinCoreProcessor itemProcessor = (DarwinCoreProcessor)EasyMock.createMock(DarwinCoreProcessor.class);
        changeProcessor.itemProcessors.put("file", itemProcessor);
        Exception exception = new Exception("exception");
        itemProcessor.onWriteError(EasyMock.eq(exception), EasyMock.eq(items));

        EasyMock.replay(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory, itemProcessor);
        changeProcessor.onWriteError(exception, items); 
        EasyMock.verify(conversionService, resolutionService, batchDatasetRepository, batchLineRepository, termFactory, itemProcessor);
    }
}
