package org.cateproject.batch.multimedia;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;

import org.cateproject.domain.Multimedia;
import org.cateproject.domain.constants.DCMIType;
import org.cateproject.file.GetResourceClient;
import org.cateproject.multitenant.MultitenantContextHolder;
import org.easymock.EasyMock;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class MultimediaFileServiceTest {

    private MultimediaFileService multimediaFileService;

    private GetResourceClient getResourceClient;

    private FFprobe ffprobe;

    @Before
    public void setUp() {
        multimediaFileService = new MultimediaFileService();
        getResourceClient = EasyMock.createMock(GetResourceClient.class);
        ffprobe = EasyMock.createMock(FFprobe.class);
        multimediaFileService.setGetResourceClient(getResourceClient);
        multimediaFileService.setTemporaryFileDirectory(new FileSystemResource(new File(System.getProperty("java.io.tmpdir"))));
        multimediaFileService.setFFprobe(ffprobe);
    }

    @Test
    public void testLocalFileInfoImage() {
        Multimedia multimedia = multimediaFileService.localFileInfo(new File("src/test/resources/org/cateproject/batch/job/test.jpg"));
        assertEquals("localFileInfo should set the correct format", "image/jpeg", multimedia.getFormat()); 
        assertEquals("localFileInfo should set the correct size", new Long(20270), multimedia.getSize()); 
        assertEquals("localFileInfo should set the correct type", DCMIType.StillImage, multimedia.getType()); 
        assertEquals("localFileInfo should set the correct width", new Integer(250), multimedia.getWidth()); 
        assertEquals("localFileInfo should set the correct height", new Integer(250), multimedia.getHeight()); 
        assertNotNull("localFileInfo should set the  hash", multimedia.getHash()); 
        assertNotNull("localFileInfo should set the local file name", multimedia.getLocalFileName()); 
        assertNull("localFileInfo should not set the fileLastModified", multimedia.getFileLastModified());
    }

    @Test
    public void testLocalFileInfoVideo() throws IOException {
        FFmpegStream stream = new FFmpegStream();
        stream.width = 1920;
        stream.height = 1080;
        stream.duration = 2.238978;
        stream.codec_name = "h264"; 
        List<FFmpegStream> streams = new ArrayList<FFmpegStream>();
        streams.add(stream);
        FFmpegProbeResult ffmpegProbeResult = new FFmpegProbeResult();
        ffmpegProbeResult.streams = streams;
        File file = new File("src/test/resources/org/cateproject/batch/job/test.mp4");
        EasyMock.expect(ffprobe.probe(EasyMock.eq(file.getAbsolutePath()))).andReturn(ffmpegProbeResult);

        EasyMock.replay(ffprobe);
        Multimedia multimedia = multimediaFileService.localFileInfo(file);
        assertEquals("localFileInfo should set the correct format", "video/mp4", multimedia.getFormat()); 
        assertEquals("localFileInfo should set the correct size", new Long(5754322), multimedia.getSize()); 
        assertEquals("localFileInfo should set the correct type", DCMIType.MovingImage, multimedia.getType()); 
        assertEquals("localFileInfo should set the correct width", new Integer(1920), multimedia.getWidth()); 
        assertEquals("localFileInfo should set the correct height", new Integer(1080), multimedia.getHeight()); 
        assertEquals("localFileInfo should set the correct duration", new Double(2.238978), multimedia.getDuration()); 
        assertNotNull("localFileInfo should set the  hash", multimedia.getHash()); 
        assertNotNull("localFileInfo should set the local file name", multimedia.getLocalFileName()); 
        assertNull("localFileInfo should not set the fileLastModified", multimedia.getFileLastModified());
        EasyMock.verify(ffprobe);
    }

    @Test
    public void testLocalFileInfoAudio() throws IOException {

        FFmpegStream stream = new FFmpegStream();
        stream.duration = 3.422041;
        stream.codec_name = "mp3"; 
        List<FFmpegStream> streams = new ArrayList<FFmpegStream>();
        streams.add(stream);
        FFmpegProbeResult ffmpegProbeResult = new FFmpegProbeResult();
        ffmpegProbeResult.streams = streams;
        File file = new File("src/test/resources/org/cateproject/batch/job/test.mp3");
        EasyMock.expect(ffprobe.probe(EasyMock.eq(file.getAbsolutePath()))).andReturn(ffmpegProbeResult);

        EasyMock.replay(ffprobe);
        Multimedia multimedia = multimediaFileService.localFileInfo(file);
        assertEquals("localFileInfo should set the correct format", "audio/mpeg", multimedia.getFormat()); 
        assertEquals("localFileInfo should set the correct size", new Long(69101), multimedia.getSize()); 
        assertEquals("localFileInfo should set the correct type", DCMIType.Sound, multimedia.getType()); 
        assertEquals("localFileInfo should set the correct duration", new Double(3.422041), multimedia.getDuration()); 
        assertNotNull("localFileInfo should set the  hash", multimedia.getHash()); 
        assertNotNull("localFileInfo should set the local file name", multimedia.getLocalFileName()); 
        assertNull("localFileInfo should not set the fileLastModified", multimedia.getFileLastModified());
        EasyMock.verify(ffprobe);
    }
    @Test
    public void testLocalFileInfoFileNotFound() {
        multimediaFileService.localFileInfo(new File("/does/not/exist.jpg"));
    }

    @Test
    public void testLocalFileInfoImageReadException() {
        multimediaFileService.localFileInfo(new File("src/test/resources/org/cateproject/batch/job/corrupt.jpg"));
    }
    
    @Test
    public void testRemoteFileInfoWithNonHttpIdentifier() {
        EasyMock.replay(getResourceClient);
        Multimedia multimedia = multimediaFileService.remoteFileInfo("file:/a/file.jpg");
        EasyMock.verify(getResourceClient);
        assertNull("remoteFileInfo should not set the format", multimedia.getFormat()); 
        assertNull("remoteFileInfo should not set the size", multimedia.getSize()); 
        assertNull("remoteFileInfo should not set the type", multimedia.getType()); 
        assertNull("remoteFileInfo should not set the width", multimedia.getWidth()); 
        assertNull("remoteFileInfo should not set the height", multimedia.getHeight()); 
        assertNull("remoteFileInfo should not set the  hash", multimedia.getHash()); 
        assertNull("remoteFileInfo should not set the local file name", multimedia.getLocalFileName()); 
        assertNull("remoteFileInfo should not set the fileLastModified", multimedia.getFileLastModified());
    }

    @Test
    public void testRemoteFileInfoWithHttpIdentifier() {
        EasyMock.expect(getResourceClient.getLastModified(EasyMock.eq("http://a/file.jpg"))).andReturn(new DateTime(2000,1,1,1,1,DateTimeZone.UTC));
        EasyMock.replay(getResourceClient);
        Multimedia multimedia = multimediaFileService.remoteFileInfo("http://a/file.jpg");
        EasyMock.verify(getResourceClient);
        assertNull("remoteFileInfo should not set the format", multimedia.getFormat()); 
        assertNull("remoteFileInfo should not set the size", multimedia.getSize()); 
        assertNull("remoteFileInfo should not set the type", multimedia.getType()); 
        assertNull("remoteFileInfo should not set the width", multimedia.getWidth()); 
        assertNull("remoteFileInfo should not set the height", multimedia.getHeight()); 
        assertNull("remoteFileInfo should not set the  hash", multimedia.getHash()); 
        assertNull("remoteFileInfo should not set the local file name", multimedia.getLocalFileName()); 
        assertEquals("remoteFileInfo should set the fileLastModified",new DateTime(2000,1,1,1,1,DateTimeZone.UTC), multimedia.getFileLastModified());
    }

    @Test
    public void testsFilesUnchanged() {
        Multimedia m1 = new Multimedia();

        Multimedia m2 = new Multimedia();

        Multimedia m3 = new Multimedia();
        m3.setFileLastModified(new DateTime(2000,1,1,1,1, DateTimeZone.UTC)); 

        Multimedia m4 = new Multimedia();
        m4.setFileLastModified(new DateTime(2000,1,1,1,1, DateTimeZone.forID("GMT"))); 

        Multimedia m5 = new Multimedia();
        m5.setHash("");

        Multimedia m6 = new Multimedia();
        m6.setHash("HASH");

        Multimedia m7 = new Multimedia();
        m7.setHash("HASH");
        m7.setSize(1L);

        Multimedia m8 = new Multimedia();
        m8.setHash("HASH");
        m8.setSize(2L);
        m8.setWidth(1);

        Multimedia m9 = new Multimedia();
        m9.setHash("HASH");
        m9.setSize(2L);
        m9.setWidth(2);

        Multimedia m10 = new Multimedia();
        m10.setHash("HASH");
        m10.setSize(2L);
        m10.setWidth(2);
        m10.setHeight(1);

        Multimedia m11 = new Multimedia();
        m11.setHash("HASH");
        m11.setSize(2L);
        m11.setWidth(2);
        m11.setHeight(2);
        m11.setDuration(1D);

        Multimedia m12 = new Multimedia();
        m12.setHash("HASH");
        m12.setSize(2L);
        m12.setWidth(2);
        m12.setHeight(2);
        m12.setDuration(2D);
        
        Multimedia m13 = new Multimedia();
        m13.setHash("HASI");
        m13.setSize(2L);
        m13.setWidth(2);
        m13.setHeight(2);
        m13.setDuration(2D);

        assertFalse("Empty multimedia are changed", multimediaFileService.filesUnchanged(m1, m2));
        assertFalse("Empty multimedia without file last modified dates are changed", multimediaFileService.filesUnchanged(m1, m3));
        assertFalse("Empty multimedia without file last modified dates are changed", multimediaFileService.filesUnchanged(m3, m1));
        assertFalse("Empty multimedia with changed file last modified dates are changed", multimediaFileService.filesUnchanged(m3, m4));
        assertFalse("Multimedia with different hashes are changed", multimediaFileService.filesUnchanged(m1, m5));
        assertFalse("Multimedia with one empty hashe are changed", multimediaFileService.filesUnchanged(m5, m1));
        assertFalse("Multimedia with two empty hashes are changed", multimediaFileService.filesUnchanged(m5, m5));
        assertFalse("Multimedia with one hash and an empty hash are changed", multimediaFileService.filesUnchanged(m5, m6));
        assertFalse("Multimedia with one hash and an empty hash are changed", multimediaFileService.filesUnchanged(m6, m5));
        assertFalse("Multimedia with different sizes are changed", multimediaFileService.filesUnchanged(m6, m7));
        assertFalse("Multimedia with different widths are changed", multimediaFileService.filesUnchanged(m7, m8));
        assertFalse("Multimedia with different widths are changed", multimediaFileService.filesUnchanged(m8, m9));
        assertFalse("Multimedia with different heights are changed", multimediaFileService.filesUnchanged(m9, m10));
        assertFalse("Multimedia with different heights are changed", multimediaFileService.filesUnchanged(m10, m11));
        assertFalse("Multimedia with different durations are changed", multimediaFileService.filesUnchanged(m11, m12));
        assertTrue("Multimedia with identical hashes are unchanged", multimediaFileService.filesUnchanged(m11, m11));
    }

    @Test
    public void testCopyFileInfo() {
        Multimedia m1 = new Multimedia();
        Multimedia m2 = new Multimedia();
        m1.setSize(1L);
        m1.setWidth(1);
        m1.setHeight(1);
        m1.setDuration(1D);
        m1.setFileLastModified(new DateTime(2000,1,1,1,1,DateTimeZone.UTC));
        m1.setHash("HASH");

        multimediaFileService.copyFileInfo(m1, m2);

        assertEquals("copyFileInfo should copy the file size", m1.getSize(), m2.getSize());
        assertEquals("copyFileInfo should copy the file width", m1.getWidth(), m2.getWidth());
        assertEquals("copyFileInfo should copy the file height", m1.getHeight(), m2.getHeight());
        assertEquals("copyFileInfo should copy the file duration", m1.getDuration(), m2.getDuration());
        assertEquals("copyFileInfo should copy the file file last modified", m1.getFileLastModified(), m2.getFileLastModified());
        assertEquals("copyFileInfo should copy the file hash", m1.getHash(), m2.getHash());
    }

    @Test
    public void testIsAudioCodec() {
        assertTrue("mp3 is an audio codec", multimediaFileService.isAudioCodec("mp3"));
        assertFalse("h264 is not an audio codec", multimediaFileService.isAudioCodec("h264"));
    }

    @Test
    public void testIsVideoCodec() {
        assertTrue("h264 is a video codec", multimediaFileService.isVideoCodec("h264"));
        assertFalse("mp3 is not an video codec", multimediaFileService.isVideoCodec("mp3"));
    }

    @Test
    public void testHandleFile() {
        Multimedia multimedia = new Multimedia();
        MockMultipartFile multipartFile = new MockMultipartFile("multipartFile","test.jpg","image/jpeg", new byte[] {(byte)0xe0});
        multimedia.setMultipartFile(multipartFile);
        multimediaFileService.handleFile(multimedia); 
        assertNotNull("Handle File should set the original file", multimedia.getOriginalFile());
    }

    @Test
    public void testHandleFileEmptyMultipartFile() {
        Multimedia multimedia = new Multimedia();
        MockMultipartFile multipartFile = new MockMultipartFile("multipartFile","test.jpg","image/jpeg", new byte[] {});
        multimedia.setMultipartFile(multipartFile);
        multimediaFileService.handleFile(multimedia); 
        assertNull("Handle File should not set the original file", multimedia.getOriginalFile());
    }

    @Test(expected = RuntimeException.class)
    public void testHandleFileMultipartFileThrowsIOException() throws IOException {
        Multimedia multimedia = new Multimedia();
        MultipartFile multipartFile = EasyMock.createMock(MultipartFile.class);
        EasyMock.expect(multipartFile.isEmpty()).andReturn(false);
        EasyMock.expect(multipartFile.getOriginalFilename()).andReturn("test.jpg").anyTimes();
        multipartFile.transferTo(EasyMock.isA(File.class));
        EasyMock.expectLastCall().andThrow(new IOException());
        multimedia.setMultipartFile(multipartFile);

        EasyMock.replay(multipartFile);
        multimediaFileService.handleFile(multimedia); 
    }

    @Test
    public void testHandleFileInDwCArchiveExists() {
        FileSystemResource dwcArchiveDir = new FileSystemResource(new File("src/test/resources/org/cateproject/batch/job/"));
        Multimedia multimedia = new Multimedia();
        multimedia.setIdentifier("test.jpg");
        MultitenantContextHolder.getContext().putContextProperty("DwcArchiveDir",dwcArchiveDir.getFile().getAbsolutePath());
        multimediaFileService.handleFile(multimedia); 
        assertNotNull("Handle File should set the original file", multimedia.getOriginalFile());
    }

    @Test
    public void testHandleFileInDwCArchiveDoesNotExist() {
        FileSystemResource dwcArchiveDir = new FileSystemResource(new File("src/test/resources/org/cateproject/batch/job/"));
        Multimedia multimedia = new Multimedia();
        multimedia.setIdentifier("doesnotexist.jpg");
        MultitenantContextHolder.getContext().putContextProperty("DwcArchiveDir",dwcArchiveDir.getFile().getAbsolutePath());
        multimediaFileService.handleFile(multimedia); 
        assertNull("Handle File should not set the original file", multimedia.getOriginalFile());
    }

    @Test
    public void testHandleFileHttpIdentifier() {
        Multimedia multimedia = new Multimedia();
        multimedia.setIdentifier("http://example.com/test.jpg");
        multimediaFileService.handleFile(multimedia); 
        assertNull("Handle File should not set the original file", multimedia.getOriginalFile());
    }

    @Test
    public void testHandleFileNoHttpIdentifier() {
        Multimedia multimedia = new Multimedia();
        multimedia.setIdentifier("ftp://example.com/test.jpg");
        multimediaFileService.handleFile(multimedia); 
        assertNull("Handle File should not set the original file", multimedia.getOriginalFile());
    }
}
