package org.cateproject.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import org.cateproject.multitenant.MultitenantContextHolder;
import org.cateproject.multitenant.event.MultitenantEvent;
import org.cateproject.multitenant.event.MultitenantEventType;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.BucketWebsiteConfiguration;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.SetBucketPolicyRequest;

public class S3FileTransferServiceTest {

    private S3FileTransferService s3FileTransferService;

    private AmazonS3 amazonS3;

    @Before
    public void setUp() {
        s3FileTransferService = new S3FileTransferService();
        amazonS3 = EasyMock.createMock(AmazonS3.class);
        s3FileTransferService.setAmazonS3(amazonS3);
        s3FileTransferService.setRegion("REGION");
        s3FileTransferService.setUploadBucketArn("UPLOAD_BUCKET_ARN");
        MultitenantContextHolder.getContext().setTenantId("TENANT_ID");
    }

    @After
    public void tearDown() {
        MultitenantContextHolder.getContext().setTenantId(null);
    }

    @Test
    public void testStringToFileStatic() throws IOException {
        File file = new File("test.txt");
        ObjectMetadata objectMetadata = new ObjectMetadata();
        EasyMock.expect(amazonS3.getObject(EasyMock.isA(GetObjectRequest.class), EasyMock.eq(file))).andReturn(objectMetadata);

        EasyMock.replay(amazonS3);
        s3FileTransferService.stringToFile("static://FILENAME", file);
        EasyMock.verify(amazonS3);
    }

    @Test
    public void testStringToFileUpload() throws IOException {
        File file = new File("test.txt");
        ObjectMetadata objectMetadata = new ObjectMetadata();
        EasyMock.expect(amazonS3.getObject(EasyMock.isA(GetObjectRequest.class), EasyMock.eq(file))).andReturn(objectMetadata);

        EasyMock.replay(amazonS3);
        s3FileTransferService.stringToFile("upload://FILENAME", file);
        EasyMock.verify(amazonS3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStringToFileBadURI() throws IOException {
        File file = new File("test.txt");

        EasyMock.replay(amazonS3);
        s3FileTransferService.stringToFile("other://FILENAME", file);
    }

    @Test
    public void testfileToStringStatic() throws IOException {
        File file = new File("test.txt");
        PutObjectResult putObjectResult = new PutObjectResult();
        putObjectResult.setETag("ETAG");
        EasyMock.expect(amazonS3.putObject(EasyMock.isA(PutObjectRequest.class))).andReturn(putObjectResult);

        EasyMock.replay(amazonS3);
        assertEquals("fileToString should return the ETag","ETAG", s3FileTransferService.fileToString(file,"static://FILENAME"));
        EasyMock.verify(amazonS3);
    }

    @Test
    public void testfileToStringUpload() throws IOException {
        File file = new File("test.txt");
        PutObjectResult putObjectResult = new PutObjectResult();
        putObjectResult.setETag("ETAG");
        EasyMock.expect(amazonS3.putObject(EasyMock.isA(PutObjectRequest.class))).andReturn(putObjectResult);

        EasyMock.replay(amazonS3);
        assertEquals("fileToString should return the ETag","ETAG", s3FileTransferService.fileToString(file,"upload://FILENAME"));
        EasyMock.verify(amazonS3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFileToStringBadURI() throws IOException {
        File file = new File("test.txt");

        EasyMock.replay(amazonS3);
        s3FileTransferService.fileToString(file, "other://FILENAME");
    }

    @Test
    public void testDeleteStatic() throws IOException {
        amazonS3.deleteObject(EasyMock.isA(DeleteObjectRequest.class));

        EasyMock.replay(amazonS3);
        assertTrue("delete should return true", s3FileTransferService.delete("static://FILENAME"));
        EasyMock.verify(amazonS3);
    }

    @Test
    public void testDeleteUpload() throws IOException {
        amazonS3.deleteObject(EasyMock.isA(DeleteObjectRequest.class));

        EasyMock.replay(amazonS3);
        assertTrue("delete should return true",s3FileTransferService.delete("upload://FILENAME"));
        EasyMock.verify(amazonS3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteBadURI() throws IOException {
        EasyMock.replay(amazonS3);
        s3FileTransferService.delete("other://FILENAME");
    }

    @Test
    public void testCopyDirectoryIn() throws IOException {
        File file = new File("test.txt");

        EasyMock.replay(amazonS3);
        s3FileTransferService.copyDirectoryIn("upload://FILENAME",file);
        EasyMock.verify(amazonS3);
    }

    @Test
    public void testCopyDirectoryOut() throws IOException {
        File file = new File("test.txt");

        EasyMock.replay(amazonS3);
        s3FileTransferService.copyDirectoryOut(file, "upload://FILENAME");
        EasyMock.verify(amazonS3);
    }

    @Test
    public void testExists() throws IOException {
        EasyMock.replay(amazonS3);
        assertFalse("exists should return false", s3FileTransferService.exists("upload://FILENAME"));
        EasyMock.verify(amazonS3);
    }

    @Test
    public void testNotify() throws IOException {
        MultitenantEvent multitenantEvent = new MultitenantEvent();        

        EasyMock.replay(amazonS3);
        s3FileTransferService.notify(multitenantEvent);
        EasyMock.verify(amazonS3);
    }

    @Test
    public void testCopyFileIn() throws IOException {
        File file = File.createTempFile("test",".txt");
        ObjectMetadata objectMetadata = new ObjectMetadata();
        EasyMock.expect(amazonS3.getObject(EasyMock.isA(GetObjectRequest.class), EasyMock.eq(file))).andReturn(objectMetadata);

        EasyMock.replay(amazonS3);
        s3FileTransferService.copyFileIn("static://FILENAME", file);
        EasyMock.verify(amazonS3);
    }


    @Test
    public void testMoveFileIn() throws IOException {
        File file = File.createTempFile("test",".txt");
        ObjectMetadata objectMetadata = new ObjectMetadata();
        EasyMock.expect(amazonS3.getObject(EasyMock.isA(GetObjectRequest.class), EasyMock.eq(file))).andReturn(objectMetadata);
        amazonS3.deleteObject(EasyMock.isA(DeleteObjectRequest.class));

        EasyMock.replay(amazonS3);
        s3FileTransferService.moveFileIn("static://FILENAME", file);
        EasyMock.verify(amazonS3);
    }

    @Test
    public void testCopyFileOut() throws IOException {
        File file = new File("test.txt");
        PutObjectResult putObjectResult = new PutObjectResult();
        putObjectResult.setETag("ETAG");
        EasyMock.expect(amazonS3.putObject(EasyMock.isA(PutObjectRequest.class))).andReturn(putObjectResult);

        EasyMock.replay(amazonS3);
        assertEquals("copyFileOut should return the ETag","ETAG", s3FileTransferService.copyFileOut(file,"static://FILENAME"));
        EasyMock.verify(amazonS3);
    }


    @Test
    public void testMoveFileOut() throws IOException {
        File file = new File("test.txt");
        PutObjectResult putObjectResult = new PutObjectResult();
        putObjectResult.setETag("ETAG");
        EasyMock.expect(amazonS3.putObject(EasyMock.isA(PutObjectRequest.class))).andReturn(putObjectResult);

        EasyMock.replay(amazonS3);
        assertEquals("moveFileOut should return the ETag","ETAG", s3FileTransferService.moveFileOut(file,"static://FILENAME"));
        assertFalse("moveFileOut should delete the file", file.exists());
        EasyMock.verify(amazonS3);
    }

    @Test
    public void testGetObjectMetadata() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("abcdefg".getBytes());
        ObjectMetadata objectMetadata = s3FileTransferService.getObjectMetadata(byteArrayInputStream);
        assertEquals("getObjectMetadata should set the correct content type", "text/html",objectMetadata.getContentType());
        assertEquals("getObjectMetadata should set the correct content encoding", "UTF-8",objectMetadata.getContentEncoding());
        assertEquals("getObjectMetadata should set the correct content length","abcdefg".getBytes().length ,objectMetadata.getContentLength());
        assertEquals("getObjectMetadata should set the correct content MD5", "esZsDxSN6VGbi9JkMSxNZA==",objectMetadata.getContentMD5());
    }

    @Test
    public void testHandleCreate() {
        MultitenantEvent multitenantEvent = new MultitenantEvent();
        multitenantEvent.setIdentifier("TENANT_ID");
        multitenantEvent.setType(MultitenantEventType.CREATE);
        Bucket bucket = new Bucket();
        EasyMock.expect(amazonS3.createBucket(EasyMock.isA(CreateBucketRequest.class))).andReturn(bucket);
        PutObjectResult putObjectResult = new PutObjectResult();
        EasyMock.expect(amazonS3.putObject(EasyMock.isA(PutObjectRequest.class))).andReturn(putObjectResult).anyTimes();
        amazonS3.setBucketWebsiteConfiguration(EasyMock.eq("static-TENANT_ID"),EasyMock.isA(BucketWebsiteConfiguration.class));
        amazonS3.setBucketPolicy(EasyMock.isA(SetBucketPolicyRequest.class));       

        EasyMock.replay(amazonS3);
        s3FileTransferService.handle(multitenantEvent);
        EasyMock.verify(amazonS3); 
    }

    @Test
    public void testHandleDelete() {
        MultitenantEvent multitenantEvent = new MultitenantEvent();
        multitenantEvent.setIdentifier("TENANT_ID");
        multitenantEvent.setType(MultitenantEventType.DELETE);

        EasyMock.replay(amazonS3);
        s3FileTransferService.handle(multitenantEvent);
        EasyMock.verify(amazonS3); 
    }

    @Test
    public void testHandleDefault() {
        MultitenantEvent multitenantEvent = new MultitenantEvent();
        multitenantEvent.setIdentifier("TENANT_ID");
        multitenantEvent.setType(MultitenantEventType.OTHER);

        EasyMock.replay(amazonS3);
        s3FileTransferService.handle(multitenantEvent);
        EasyMock.verify(amazonS3); 
    }
}
