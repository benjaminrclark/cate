package org.cateproject.batch.multimedia;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.cateproject.domain.Multimedia;
import org.cateproject.domain.constants.MultimediaFileType;
import org.cateproject.domain.util.MultimediaFile;
import org.cateproject.file.FileTransferService;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class MultimediaFileWriterTest {

    private MultimediaFileWriter multimediaFileWriter;

    private FileTransferService fileTransferService;

    @Before
    public void setUp() {
        multimediaFileWriter = new MultimediaFileWriter();
        fileTransferService = EasyMock.createMock(FileTransferService.class);
        multimediaFileWriter.setFileTransferService(fileTransferService);
        TransactionSynchronizationManager.initSynchronization();
    }

    @After
    public void tearDown() {
        TransactionSynchronizationManager.clearSynchronization();
    }

    @Test
    public void testWrite() throws Exception {
        Multimedia multimedia = new Multimedia();
        File file = new File("test.jpg");
        MultimediaFile multimediaFile = new MultimediaFile(multimedia, file, MultimediaFileType.original);
        multimedia.getMultimediaFiles().add(multimediaFile);
        List<Multimedia> multimedias = new ArrayList<Multimedia>();
        multimedias.add(multimedia);

        EasyMock.expect(fileTransferService.moveFileOut(EasyMock.eq(file),EasyMock.isA(String.class))).andReturn("RETURN_VALUE");

        EasyMock.replay(fileTransferService);
        multimediaFileWriter.write(multimedias);
        for(TransactionSynchronization transactionSynchronization : TransactionSynchronizationManager.getSynchronizations()) {
            transactionSynchronization.afterCommit();
        }
        EasyMock.verify(fileTransferService);
    }

    @Test(expected = RuntimeException.class)
    public void testWriteThrowsIOException() throws Exception {
        Multimedia multimedia = new Multimedia();
        File file = new File("test.jpg");
        MultimediaFile multimediaFile = new MultimediaFile(multimedia, file, MultimediaFileType.original);
        multimedia.getMultimediaFiles().add(multimediaFile);
        List<Multimedia> multimedias = new ArrayList<Multimedia>();
        multimedias.add(multimedia);

        EasyMock.expect(fileTransferService.moveFileOut(EasyMock.eq(file),EasyMock.isA(String.class))).andThrow(new IOException("EXPECTED"));

        EasyMock.replay(fileTransferService);
        multimediaFileWriter.write(multimedias);
        for(TransactionSynchronization transactionSynchronization : TransactionSynchronizationManager.getSynchronizations()) {
            transactionSynchronization.afterCommit();
        }
    }
}
