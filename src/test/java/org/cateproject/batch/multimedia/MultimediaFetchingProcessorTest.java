package org.cateproject.batch.multimedia;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;

import org.cateproject.domain.Multimedia;
import org.cateproject.file.FileTransferService;
import org.cateproject.file.GetResourceClient;
import org.easymock.EasyMock;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.FileSystemResource;

public class MultimediaFetchingProcessorTest {

    private MultimediaFetchingProcessor multimediaFetchingProcessor;

    private FileTransferService fileTransferService;

    private MultimediaFileService multimediaFileService;

    private GetResourceClient getResourceClient;

    @Before
    public void setUp() {
        multimediaFetchingProcessor = new MultimediaFetchingProcessor();
        fileTransferService = EasyMock.createMock(FileTransferService.class);
        multimediaFileService = EasyMock.createMock(MultimediaFileService.class);
        getResourceClient = EasyMock.createMock(GetResourceClient.class);
        multimediaFetchingProcessor.setFileTransferService(fileTransferService);
        multimediaFetchingProcessor.setMultimediaFileService(multimediaFileService);
        multimediaFetchingProcessor.setGetResourceClient(getResourceClient);
        multimediaFetchingProcessor.setTemporaryFileDirectory(new FileSystemResource(new File(System.getProperty("java.io.tmpdir"))));
    }

    @Test
    public void testProcess() throws Exception {
        Multimedia multimediaIn = new Multimedia();
        Multimedia extractedMultimedia = new Multimedia();
        multimediaFetchingProcessor.setUploadedFile("UPLOADED_FILE.EXTENSION");
        fileTransferService.moveFileIn(EasyMock.eq("UPLOADED_FILE.EXTENSION"),EasyMock.isA(File.class));
        EasyMock.expect(multimediaFileService.localFileInfo(EasyMock.isA(File.class))).andReturn(extractedMultimedia);
        EasyMock.expect(multimediaFileService.filesUnchanged(EasyMock.eq(multimediaIn),EasyMock.eq(extractedMultimedia))).andReturn(false);
        multimediaFileService.copyFileInfo(EasyMock.eq(extractedMultimedia),EasyMock.eq(multimediaIn));    
 
        EasyMock.replay(fileTransferService, multimediaFileService, getResourceClient);
        Multimedia multimediaOut = multimediaFetchingProcessor.process(multimediaIn);

        assertTrue("process should set the local file name",multimediaOut.getLocalFileName().endsWith("EXTENSION"));
        assertEquals("process should return the original multimedia",multimediaIn,multimediaOut);
        EasyMock.verify(fileTransferService,multimediaFileService, getResourceClient);
    }

    @Test
    public void testProcessWithExistingLocalFileName() throws Exception {
        Multimedia multimediaIn = new Multimedia();
        multimediaIn.setLocalFileName("LOCAL_FILE_NAME");
        multimediaIn.setIdentifier("IDENTIFIER");
 
        EasyMock.replay(fileTransferService, multimediaFileService, getResourceClient);
        Multimedia multimediaOut = multimediaFetchingProcessor.process(multimediaIn);
        assertEquals("process should not change the local file name", "LOCAL_FILE_NAME", multimediaIn.getLocalFileName());
        assertNull("process should return the null",multimediaOut);
        EasyMock.verify(fileTransferService,multimediaFileService, getResourceClient);
    }

    @Test
    public void testProcessWithRemoteFileAlreadyExists() throws Exception {
        Multimedia multimediaIn = new Multimedia();
        multimediaIn.setIdentifier("http://IDENTIFIER");
        multimediaIn.setFileLastModified(new DateTime(2000,1,1,1,1,DateTimeZone.UTC));

        Multimedia extractedMultimedia = new Multimedia();
        EasyMock.expect(getResourceClient.getResource(EasyMock.eq("http://IDENTIFIER"), (DateTime)EasyMock.isNull(), EasyMock.isA(File.class))).andReturn(new DateTime(2001,1,1,1,1,DateTimeZone.UTC));;
        EasyMock.expect(multimediaFileService.localFileInfo(EasyMock.isA(File.class))).andReturn(extractedMultimedia);
        EasyMock.expect(multimediaFileService.filesUnchanged(EasyMock.eq(multimediaIn),EasyMock.eq(extractedMultimedia))).andReturn(true);
        EasyMock.expect(fileTransferService.exists(EasyMock.isA(String.class))).andReturn(true);
 
        EasyMock.replay(fileTransferService, multimediaFileService, getResourceClient);
        Multimedia multimediaOut = multimediaFetchingProcessor.process(multimediaIn);

        assertNull("process should return null as the file has not changed", multimediaOut);
        EasyMock.verify(fileTransferService,multimediaFileService, getResourceClient);
    }

    @Test
    public void testProcessWithRemoteFileNotExists() throws Exception {
        Multimedia multimediaIn = new Multimedia();
        multimediaIn.setIdentifier("http://IDENTIFIER");
        multimediaIn.setFileLastModified(new DateTime(2000,1,1,1,1,DateTimeZone.UTC));

        Multimedia extractedMultimedia = new Multimedia();
        EasyMock.expect(getResourceClient.getResource(EasyMock.eq("http://IDENTIFIER"), (DateTime)EasyMock.isNull(), EasyMock.isA(File.class))).andReturn(new DateTime(2001,1,1,1,1,DateTimeZone.UTC));;
        EasyMock.expect(multimediaFileService.localFileInfo(EasyMock.isA(File.class))).andReturn(extractedMultimedia);
        EasyMock.expect(multimediaFileService.filesUnchanged(EasyMock.eq(multimediaIn),EasyMock.eq(extractedMultimedia))).andReturn(true);
        EasyMock.expect(fileTransferService.exists(EasyMock.isA(String.class))).andReturn(false);
        multimediaFileService.copyFileInfo(EasyMock.eq(extractedMultimedia),EasyMock.eq(multimediaIn));    

        EasyMock.replay(fileTransferService, multimediaFileService, getResourceClient);
        Multimedia multimediaOut = multimediaFetchingProcessor.process(multimediaIn);

        assertEquals("process should return the original multimedia", multimediaIn, multimediaOut);
        EasyMock.verify(fileTransferService,multimediaFileService, getResourceClient);
    }
}
