package org.cateproject.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.cateproject.domain.constants.DCMIType;
import org.cateproject.domain.constants.MultimediaFileType;
import org.cateproject.multitenant.MultitenantContextHolder;
import org.cateproject.multitenant.event.MultitenantEvent;
import org.cateproject.multitenant.event.MultitenantEventType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.FileSystemResource;

public class DefaultFileTransferServiceImplTest {
    
    private DefaultFileTransferServiceImpl defaultFileTransferService;

    private File uploadDir = null;

    private File staticDir = null;

    private File tenantDir = null;

    private File localDir = null; 

    @Before
    public void setUp() {
        defaultFileTransferService = new DefaultFileTransferServiceImpl();
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        localDir = new File(tempDir, "local");
        uploadDir = new File(tempDir, "upload");
        staticDir = new File(tempDir, "static");
        tenantDir = new File(staticDir, "TENANT");
        uploadDir.mkdir();
        staticDir.mkdir();
        tenantDir.mkdir();
        localDir.mkdir();
        defaultFileTransferService.setUploadFileDirectory(new FileSystemResource(uploadDir));
        defaultFileTransferService.setStaticFileDirectory(new FileSystemResource(staticDir));
        MultitenantContextHolder.getContext().setTenantId("TENANT");
    }

    @After
    public void tearDown() throws IOException {
        FileUtils.deleteDirectory(localDir);
        FileUtils.deleteDirectory(uploadDir);
        FileUtils.deleteDirectory(staticDir);
        MultitenantContextHolder.getContext().setTenantId(null);
    }

    @Test
    public void testExists() throws IOException {
        assertFalse("exists should return false", defaultFileTransferService.exists("RESOURCE"));
    }

    @Test
    public void testCopyDirectoryOut() throws IOException {
        File file = File.createTempFile("PREFIX","SUFFIX");
        defaultFileTransferService.copyDirectoryOut(file, "RESOURCE");
    }

    @Test
    public void testCopyDirectoryIn() throws IOException {
        File file = File.createTempFile("PREFIX","SUFFIX");
        defaultFileTransferService.copyDirectoryIn("RESOURCE", file);
    }
    
    @Test
    public void testDelete() throws IOException {
        File file = File.createTempFile("Delete", "txt", uploadDir);
        assertTrue("delete should return true",defaultFileTransferService.delete("upload://" + file.getName()));
    }

    @Test
    public void testCopyFileIn() throws IOException {
        File from = File.createTempFile("CopyFileIn", "txt", uploadDir);
        FileUtils.touch(from);
        File to = new File(localDir, "CopyFileIn.txt");

        defaultFileTransferService.copyFileIn("upload://" + from.getName(), to);
        assertTrue("copyFileIn should copy a file in", to.exists());
    }

    @Test
    public void testCopyFileInSameFile() throws IOException {
        File from = File.createTempFile("CopyFileInSameFile","txt", uploadDir);
        FileUtils.touch(from);

        defaultFileTransferService.copyFileIn("upload://" + from.getName(), from);
    }

    @Test
    public void testCopyFileOut() throws IOException {
        File from = File.createTempFile("CopyFileOut", "txt", localDir);
        FileUtils.touch(from);
        File to = new File(uploadDir, "CopyFileOut.txt");

        defaultFileTransferService.copyFileOut(from, "upload://" + to.getName());
    }

    @Test
    public void testCopyFileOutSameFile() throws IOException {
        File from = File.createTempFile("CopyFileOutSameFile","txt", uploadDir);
        FileUtils.touch(from);

        defaultFileTransferService.copyFileOut(from, "upload://" + from.getName());
    }

    @Test
    public void testMoveFileIn() throws IOException {
        File from = File.createTempFile("MoveFileIn", "txt", uploadDir);
        File to = new File(localDir, "MoveFileIn.txt");
        FileUtils.touch(from);

        defaultFileTransferService.moveFileIn("upload://" + from.getName(), to);
        assertTrue("moveFileIn should copy a file in", to.exists());
    }

    @Test
    public void testMoveFileInSameFile() throws IOException {
        File from = File.createTempFile("MoveFileInSameFile","txt", uploadDir);
        FileUtils.touch(from);

        defaultFileTransferService.moveFileIn("upload://" + from.getName(), from);
    }

    @Test
    public void testMoveFileOut() throws IOException {
        File from = File.createTempFile("MoveFileOut", "txt", localDir);
        FileUtils.touch(from);
        File to = new File(uploadDir, "MoveFileOut.txt");

        defaultFileTransferService.moveFileOut(from, "upload://" + to.getName());
    }

    @Test
    public void testMoveFileOutSameFile() throws IOException {
        File from = File.createTempFile("MoveFileOutSameFile","txt", uploadDir);
        FileUtils.touch(from);

        defaultFileTransferService.moveFileOut(from, "upload://" + from.getName());
    }

    @Test
    public void testNotify() {
        defaultFileTransferService.notify(new MultitenantEvent());
    }

    @Test
    public void testAfterPropertiesSet() {
        File uploadFileDirectory = new File(localDir,"upload");
        defaultFileTransferService.setUploadFileDirectory(new FileSystemResource(uploadFileDirectory));
        defaultFileTransferService.afterPropertiesSet();
        assertTrue("afterPropertiesSet should create the upload file directory", uploadFileDirectory.exists());
    }

    @Test
    public void testAfterPropertiesSetDirectoryExists() {
        defaultFileTransferService.afterPropertiesSet();
    }

    @Test
    public void testStringToFile() {
        assertEquals("stringToFile should return the file name for static files", new File(tenantDir, "Filename.txt").getAbsolutePath(), defaultFileTransferService.stringToFile("static://Filename.txt").getAbsolutePath());
        assertEquals("stringToFile should return the file name for upload files", new File(uploadDir, "Filename.txt").getAbsolutePath(), defaultFileTransferService.stringToFile("upload://Filename.txt").getAbsolutePath());
    }

    @Test
    public void testHandleDefault() {
        MultitenantEvent multitenantEvent = new MultitenantEvent(); 
        multitenantEvent.setIdentifier("OTHER_TENANT");
        multitenantEvent.setType(MultitenantEventType.OTHER);
        defaultFileTransferService.handle(multitenantEvent);
    }

    @Test
    public void testHandleDelete() {
        MultitenantEvent multitenantEvent = new MultitenantEvent(); 
        multitenantEvent.setIdentifier("OTHER_TENANT");
        multitenantEvent.setType(MultitenantEventType.DELETE);
        defaultFileTransferService.handle(multitenantEvent);
    }

    @Test
    public void testHandleCreate() {
        MultitenantEvent multitenantEvent = new MultitenantEvent(); 
        multitenantEvent.setIdentifier("OTHER_TENANT");
        multitenantEvent.setType(MultitenantEventType.CREATE);
        defaultFileTransferService.handle(multitenantEvent);
        File tenantDirectory = new File(staticDir,"OTHER_TENANT");
        for(DCMIType type : DCMIType.values()) {
            File typeDirectory = new File(tenantDirectory, type.toString());
            for(MultimediaFileType multimediaType : type.getMultimediaFileTypes()) {
                File multimediaTypeDirectory = new File(typeDirectory, multimediaType.toString());
                assertTrue("create should create a new directory", multimediaTypeDirectory.exists());
            }

        }
    }
}
