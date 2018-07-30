package org.cateproject.batch.sync;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

import java.util.SortedSet;
import java.util.TreeSet;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import org.cateproject.domain.batch.BatchLine;
import org.cateproject.domain.sync.ChangeManifestChange;
import org.cateproject.domain.sync.ChangeManifestChangeType;
import org.cateproject.domain.sync.ChangeManifestUrl;
import org.cateproject.repository.jpa.batch.BatchFileRepository;
import org.cateproject.repository.jpa.batch.BatchLineRepository;
import org.cateproject.repository.jpa.sync.ChangeManifestUrlRepository;

public class ChangeDumpManifestProcessorTest {

    ChangeDumpManifestProcessor processor;
  
    BatchLineRepository batchLineRepository;

    ChangeManifestUrlRepository changeManifestUrlRepository;

    ChangeManifestUrl changeManifestUrl;

    BatchLine batchLine;

    @Before
    public void setUp() throws Exception {
        processor = new ChangeDumpManifestProcessor("identifier");

        batchLineRepository = EasyMock.createMock(BatchLineRepository.class);
        processor.setBatchLineRepository(batchLineRepository);

        changeManifestUrlRepository = EasyMock.createMock(ChangeManifestUrlRepository.class);
        processor.setChangeManifestUrlRepository(changeManifestUrlRepository);
        
        changeManifestUrl = new ChangeManifestUrl();
        changeManifestUrl.setMd(new ChangeManifestChange());
        changeManifestUrl.getMd().setChange(ChangeManifestChangeType.created);

        batchLine = new BatchLine();
        batchLine.setLineNumber(10);
    }

    @Test
    public void testProcessSingleRow() {
        
        changeManifestUrl.getMd().setPath("/path/file.txt#row=10");
        EasyMock.expect(batchLineRepository.findLineByDatasetIdentifierAndFileLocationAndLineNumber(EasyMock.eq("identifier"),
                                                                                                    EasyMock.eq("path/file.txt"), 
                                                                                                    EasyMock.eq(new Integer(10)))).andReturn(batchLine);
        SortedSet<BatchLine> batchLines = new TreeSet<BatchLine>();
        batchLines.add(batchLine);

        EasyMock.expect(changeManifestUrlRepository.save(EasyMock.eq(changeManifestUrl))).andReturn(changeManifestUrl);


        EasyMock.replay(batchLineRepository, changeManifestUrlRepository);
        ChangeManifestUrl result = processor.process(changeManifestUrl);
        
        assertThat("result should contain the expected object", 
         result.getBatchLines(),
         hasItem(batchLine));
        EasyMock.verify(batchLineRepository, changeManifestUrlRepository);

    }

    @Test(expected = RuntimeException.class)
    public void testProcessSingleRowMissingBatchLine() {
        
        changeManifestUrl.getMd().setPath("/path/file.txt#row=10");
        EasyMock.expect(batchLineRepository.findLineByDatasetIdentifierAndFileLocationAndLineNumber(EasyMock.eq("identifier"),
                                                                                                    EasyMock.eq("path/file.txt"), 
                                                                                                    EasyMock.eq(new Integer(10)))).andReturn(null);


        EasyMock.replay(batchLineRepository, changeManifestUrlRepository);
        processor.process(changeManifestUrl);
    }

    @Test
    public void testProcessMultipleRows() {
        
        SortedSet<BatchLine> batchLines = new TreeSet<BatchLine>();
        batchLines.add(batchLine);
        changeManifestUrl.getMd().setPath("/path/file.txt#row=10-11");
        EasyMock.expect(batchLineRepository.findLinesByDatasetIdentifierAndFileLocationAndLineNumbers(EasyMock.eq("identifier"),
                                                                                                      EasyMock.eq("path/file.txt"), 
                                                                                                      EasyMock.eq(new Integer(10)),
                                                                                                      EasyMock.eq(new Integer(11)))).andReturn(batchLines);

        EasyMock.expect(changeManifestUrlRepository.save(EasyMock.eq(changeManifestUrl))).andReturn(changeManifestUrl);


        EasyMock.replay(batchLineRepository, changeManifestUrlRepository);
        ChangeManifestUrl result = processor.process(changeManifestUrl);
        
        assertThat("result should contain the expected object", 
         result.getBatchLines(),
         hasItem(batchLine));
        EasyMock.verify(batchLineRepository, changeManifestUrlRepository);

    }

    @Test(expected = RuntimeException.class)
    public void testProcessMultipleRowsMissingBatchLines() {
        
        SortedSet<BatchLine> batchLines = new TreeSet<BatchLine>();
        changeManifestUrl.getMd().setPath("/path/file.txt#row=10-11");
        EasyMock.expect(batchLineRepository.findLinesByDatasetIdentifierAndFileLocationAndLineNumbers(EasyMock.eq("identifier"),
                                                                                                      EasyMock.eq("path/file.txt"), 
                                                                                                      EasyMock.eq(new Integer(10)),
                                                                                                      EasyMock.eq(new Integer(11)))).andReturn(batchLines);



        EasyMock.replay(batchLineRepository, changeManifestUrlRepository);
        processor.process(changeManifestUrl);
    }

    @Test(expected = RuntimeException.class)
    public void testProcessNoMatches() {
        
        SortedSet<BatchLine> batchLines = new TreeSet<BatchLine>();
        changeManifestUrl.getMd().setPath("/path/file.txt#row=10-11-12");
        EasyMock.replay(batchLineRepository, changeManifestUrlRepository);
        processor.process(changeManifestUrl);
    }

    @Test
    public void testProcessNullChange() {
        changeManifestUrl.setMd(null);
        
        EasyMock.replay(batchLineRepository, changeManifestUrlRepository);
        assertNull("process should return null",processor.process(changeManifestUrl));
        EasyMock.verify(batchLineRepository, changeManifestUrlRepository);
    }
}
