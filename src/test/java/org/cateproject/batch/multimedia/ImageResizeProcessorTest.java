package org.cateproject.batch.multimedia;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.cateproject.domain.Multimedia;
import org.cateproject.domain.constants.DCMIType;
import org.cateproject.domain.constants.MultimediaFileType;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.Operation;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.FileSystemResource;

public class ImageResizeProcessorTest {

    private ImageResizeProcessor imageResizeProcessor;

    private ConvertCmd convertCmd;

    @Before
    public void setUp() {
        imageResizeProcessor = new ImageResizeProcessor();
        convertCmd = EasyMock.createMock(ConvertCmd.class);
        imageResizeProcessor.setConvertCmd(convertCmd);
        imageResizeProcessor.setType(MultimediaFileType.large);
        imageResizeProcessor.setTemporaryFileDirectory(new FileSystemResource(new File(System.getProperty("java.io.tmpdir"))));
    }

    @Test
    public void testConvert() throws Exception {
        Capture<Operation> capture = Capture.newInstance(); 
        Multimedia multimedia = new Multimedia();
        multimedia.setType(DCMIType.StillImage);
        multimedia.setLocalFileName("LOCAL_FILE_NAME.EXTENSION");
        multimedia.setOriginalFile(new File("/FILE.EXTENSION"));

        convertCmd.run(EasyMock.and(EasyMock.isA(Operation.class),EasyMock.capture(capture)));

        EasyMock.replay(convertCmd);
        assertEquals("process should return the multimedia", imageResizeProcessor.process(multimedia), multimedia);
        assertEquals("multimedia should contain a new multimedia file",multimedia.getMultimediaFiles().size(),2);
        assertEquals("process should set the expected command line args",capture.getValue().getCmdArgs().size(),2);
        assertEquals("process should set the expected command line args",capture.getValue().getCmdArgs().get(0),"/FILE.EXTENSION");
        assertTrue("process should set the expected command line args",capture.getValue().getCmdArgs().get(1).startsWith(System.getProperty("java.io.tmpdir")));
        assertTrue("process should set the expected command line args",capture.getValue().getCmdArgs().get(1).endsWith(".EXTENSION"));
        EasyMock.verify(convertCmd);
    } 

    @Test
    public void testConvertWithExtraParameters() throws Exception {
        Capture<Operation> capture = Capture.newInstance(); 
        imageResizeProcessor.setResizeX(1);
        imageResizeProcessor.setResizeY(1);
        imageResizeProcessor.setExtentX(1);
        imageResizeProcessor.setExtentY(1);
        imageResizeProcessor.setResizeChar('Z');
        imageResizeProcessor.setGravity("GRAVITY"); 
        Multimedia multimedia = new Multimedia();
        multimedia.setType(DCMIType.StillImage);
        multimedia.setLocalFileName("LOCAL_FILE_NAME.EXTENSION");
        multimedia.setOriginalFile(new File("/FILE.EXTENSION"));

        convertCmd.run(EasyMock.and(EasyMock.isA(Operation.class),EasyMock.capture(capture)));

        EasyMock.replay(convertCmd);
        assertEquals("process should return the multimedia", imageResizeProcessor.process(multimedia), multimedia);
        assertEquals("multimedia should contain a new multimedia file",multimedia.getMultimediaFiles().size(),2);
        assertEquals("process should set the expected command line args",capture.getValue().getCmdArgs().size(),8);
        assertEquals("process should set the expected command line args",capture.getValue().getCmdArgs().get(0), "/FILE.EXTENSION");
        assertEquals("process should set the expected command line args",capture.getValue().getCmdArgs().get(1), "-resize");
        assertEquals("process should set the expected command line args",capture.getValue().getCmdArgs().get(2), "1x1Z");
        assertEquals("process should set the expected command line args",capture.getValue().getCmdArgs().get(3), "-gravity");
        assertEquals("process should set the expected command line args",capture.getValue().getCmdArgs().get(4), "GRAVITY");
        assertEquals("process should set the expected command line args",capture.getValue().getCmdArgs().get(5), "-extent");
        assertEquals("process should set the expected command line args",capture.getValue().getCmdArgs().get(6), "1x1");
        assertTrue("process should set the expected command line args",capture.getValue().getCmdArgs().get(7).startsWith(System.getProperty("java.io.tmpdir")));
        assertTrue("process should set the expected command line args",capture.getValue().getCmdArgs().get(7).endsWith(".EXTENSION"));
        EasyMock.verify(convertCmd);
    }

    @Test
    public void testConvertWithExtraParametersNoResizeChar() throws Exception {
        Capture<Operation> capture = Capture.newInstance(); 
        imageResizeProcessor.setResizeX(1);
        imageResizeProcessor.setResizeY(1);
        imageResizeProcessor.setExtentX(1);
        imageResizeProcessor.setGravity("GRAVITY"); 
        Multimedia multimedia = new Multimedia();
        multimedia.setType(DCMIType.StillImage);
        multimedia.setLocalFileName("LOCAL_FILE_NAME.EXTENSION");
        multimedia.setOriginalFile(new File("/FILE.EXTENSION"));

        convertCmd.run(EasyMock.and(EasyMock.isA(Operation.class),EasyMock.capture(capture)));

        EasyMock.replay(convertCmd);
        assertEquals("process should return the multimedia", imageResizeProcessor.process(multimedia), multimedia);
        assertEquals("multimedia should contain a new multimedia file",multimedia.getMultimediaFiles().size(),2);
        assertEquals("process should set the expected command line args",capture.getValue().getCmdArgs().size(),6);
        assertEquals("process should set the expected command line args",capture.getValue().getCmdArgs().get(0), "/FILE.EXTENSION");
        assertEquals("process should set the expected command line args",capture.getValue().getCmdArgs().get(1), "-resize");
        assertEquals("process should set the expected command line args",capture.getValue().getCmdArgs().get(2), "1x1");
        assertEquals("process should set the expected command line args",capture.getValue().getCmdArgs().get(3), "-gravity");
        assertEquals("process should set the expected command line args",capture.getValue().getCmdArgs().get(4), "GRAVITY");
        assertTrue("process should set the expected command line args",capture.getValue().getCmdArgs().get(5).startsWith(System.getProperty("java.io.tmpdir")));
        assertTrue("process should set the expected command line args",capture.getValue().getCmdArgs().get(5).endsWith(".EXTENSION"));
        EasyMock.verify(convertCmd);
    }

    @Test
    public void testConvertWithNullType() throws Exception {
        Multimedia multimedia = new Multimedia();
        multimedia.setLocalFileName("LOCAL_FILE_NAME.EXTENSION");
        multimedia.setOriginalFile(new File("/FILE.EXTENSION"));

        EasyMock.replay(convertCmd);
        assertEquals("process should return the multimedia", imageResizeProcessor.process(multimedia), multimedia);
        assertEquals("multimedia should contain the original multimedia file",multimedia.getMultimediaFiles().size(),1);
        EasyMock.verify(convertCmd);
    }

    @Test
    public void testConvertWithVideo() throws Exception {
        Multimedia multimedia = new Multimedia();
        multimedia.setType(DCMIType.MovingImage);
        multimedia.setLocalFileName("LOCAL_FILE_NAME.EXTENSION");
        multimedia.setOriginalFile(new File("/FILE.EXTENSION"));

        EasyMock.replay(convertCmd);
        assertEquals("process should return the multimedia", imageResizeProcessor.process(multimedia), multimedia);
        assertEquals("multimedia should contain the original multimedia file",multimedia.getMultimediaFiles().size(),1);
        EasyMock.verify(convertCmd);
    }

    @Test(expected=IOException.class)
    public void testConvertThrowsIOException() throws Exception {
        imageResizeProcessor.setResizeX(1);
        imageResizeProcessor.setExtentX(1);
        imageResizeProcessor.setGravity("GRAVITY"); 
        Multimedia multimedia = new Multimedia();
        multimedia.setType(DCMIType.StillImage);
        multimedia.setLocalFileName("LOCAL_FILE_NAME.EXTENSION");
        multimedia.setOriginalFile(new File("/FILE.EXTENSION"));

        convertCmd.run(EasyMock.isA(Operation.class));
        EasyMock.expectLastCall().andThrow(new IOException("EXPECTED"));

        EasyMock.replay(convertCmd);
        imageResizeProcessor.process(multimedia);
    }

    @Test(expected=InterruptedException.class)
    public void testConvertThrowsInterruptedException() throws Exception {
        imageResizeProcessor.setResizeX(1);
        imageResizeProcessor.setExtentX(1);
        imageResizeProcessor.setGravity("GRAVITY"); 
        Multimedia multimedia = new Multimedia();
        multimedia.setType(DCMIType.StillImage);
        multimedia.setLocalFileName("LOCAL_FILE_NAME.EXTENSION");
        multimedia.setOriginalFile(new File("/FILE.EXTENSION"));

        convertCmd.run(EasyMock.isA(Operation.class));
        EasyMock.expectLastCall().andThrow(new InterruptedException("EXPECTED"));

        EasyMock.replay(convertCmd);
        imageResizeProcessor.process(multimedia);
    }

    @Test(expected=IM4JavaException.class)
    public void testConvertThrowsIM4JavaException() throws Exception {
        imageResizeProcessor.setResizeX(1);
        imageResizeProcessor.setExtentX(1);
        imageResizeProcessor.setGravity("GRAVITY"); 
        Multimedia multimedia = new Multimedia();
        multimedia.setType(DCMIType.StillImage);
        multimedia.setLocalFileName("LOCAL_FILE_NAME.EXTENSION");
        multimedia.setOriginalFile(new File("/FILE.EXTENSION"));

        convertCmd.run(EasyMock.isA(Operation.class));
        EasyMock.expectLastCall().andThrow(new IM4JavaException("EXPECTED"));

        EasyMock.replay(convertCmd);
        imageResizeProcessor.process(multimedia);
    }
}
