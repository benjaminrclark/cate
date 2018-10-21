package org.cateproject.web.form;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


import org.cateproject.file.FileTransferService;
import org.cateproject.domain.batch.JobLaunchRequest;
import org.cateproject.multitenant.MultitenantContextHolder;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile; 

import org.springframework.core.io.FileSystemResource;


public class UploadDtoToJobLaunchRequestConverterTest {

    private UploadDtoToJobLaunchRequestConverter uploadDtoToJobLaunchRequestConverter;

    private FileTransferService fileTransferService;

    private JobLocator jobLocator;

    private ConversionService conversionService;
 
    private UploadDto uploadDto;


    @Before
    public void setUp() {
	MultitenantContextHolder.getContext().clearContextProperties();
	MultitenantContextHolder.getContext().setTenantId("TENANT_ID");
        SecurityContextImpl securityContext = new SecurityContextImpl();
	securityContext.setAuthentication(new TestingAuthenticationToken("USER", "PASSWORD", "ROLE_USER"));
	SecurityContextHolder.setContext(securityContext);

        uploadDtoToJobLaunchRequestConverter = new UploadDtoToJobLaunchRequestConverter();

	FileSystemResource temporaryFileDirectory = new FileSystemResource(System.getProperty("java.io.tmpdir"));
        conversionService = EasyMock.createMock(ConversionService.class);
        fileTransferService = EasyMock.createMock(FileTransferService.class);
        jobLocator = EasyMock.createMock(JobLocator.class);

        uploadDtoToJobLaunchRequestConverter.setConversionService(conversionService);
        uploadDtoToJobLaunchRequestConverter.setJobLocator(jobLocator);
        uploadDtoToJobLaunchRequestConverter.setFileTransferService(fileTransferService);
        uploadDtoToJobLaunchRequestConverter.setTemporaryFileDirectory(temporaryFileDirectory);

        uploadDto = new UploadDto();
        uploadDto.setFiles(new ArrayList<MultipartFile>());
        
    }

    @After
    public void tearDown() {
	MultitenantContextHolder.getContext().clearContextProperties();
	MultitenantContextHolder.getContext().setTenantId(null);
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testConvert() throws Exception {
        
	FileSystemResource file = new FileSystemResource("src/test/resources/org/cateproject/batch/job/test.zip");
        uploadDto.getFiles().add(new MockMultipartFile("test.zip", "test.zip", "application/zip", file.getInputStream()));
        EasyMock.expect(jobLocator.getJob("processDarwinCoreArchive")).andReturn(new SimpleJob("processDarwinCoreArchive"));
        EasyMock.expect(fileTransferService.copyFileOut(EasyMock.isA(File.class), EasyMock.isA(String.class))).andReturn("STRING");
        EasyMock.expect(conversionService.convert(EasyMock.eq(new HashMap<String, Object>()), EasyMock.eq(String.class))).andReturn("TENANT_PROPERTIES");

        EasyMock.replay(conversionService, fileTransferService, jobLocator);
        JobLaunchRequest jobLaunchRequest = uploadDtoToJobLaunchRequestConverter.convert(uploadDto);
        EasyMock.verify(conversionService, fileTransferService, jobLocator);
        assertTrue("jobParameters should contain a parameter 'input.file'", jobLaunchRequest.getJobParameters().getParameters().containsKey("input.file"));
        assertTrue("jobParameters should contain a parameter 'working.dir'", jobLaunchRequest.getJobParameters().getParameters().containsKey("working.dir"));
        assertEquals("jobParameters should contain a parameter 'tenant.id' with value 'TENANT_ID'", "TENANT_ID", jobLaunchRequest.getJobParameters().getString("tenant.id"));
        assertEquals("jobParameters should contain a parameter 'tenant.properties' with value 'TENANT_PROPERTIES'", "TENANT_PROPERTIES", jobLaunchRequest.getJobParameters().getString("tenant.properties"));
        assertEquals("jobParameters should contain a parameter 'user.id' with value 'USER'", "USER", jobLaunchRequest.getJobParameters().getString("user.id"));
        assertEquals("jobParameters should contain a parameter 'launch.request.identifier' which is the same as its identifier", jobLaunchRequest.getIdentifier(), jobLaunchRequest.getJobParameters().getString("launch.request.identifier"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertUnsupportedFormats() throws Exception {
        
	FileSystemResource file1 = new FileSystemResource("src/test/resources/org/cateproject/web/form/unknown.x1");
	FileSystemResource file2 = new FileSystemResource("src/test/resources/org/cateproject/web/form/unknown.x2");
        uploadDto.getFiles().add(new MockMultipartFile("unknown.x1", "unknown.x1", "application/unknown", file1.getInputStream()));
        uploadDto.getFiles().add(new MockMultipartFile("unknown.x2", "unknown.x2", "application/unknown", file1.getInputStream()));

        EasyMock.replay(conversionService, fileTransferService, jobLocator);
        uploadDtoToJobLaunchRequestConverter.convert(uploadDto);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertIOException() throws Exception {
        
	FileSystemResource file = new FileSystemResource("src/test/resources/org/cateproject/batch/job/test.zip");
        uploadDto.getFiles().add(new MockMultipartFile("test.zip", "test.zip", "application/zip", file.getInputStream()));
        EasyMock.expect(jobLocator.getJob("processDarwinCoreArchive")).andReturn(new SimpleJob("processDarwinCoreArchive"));
        EasyMock.expect(fileTransferService.copyFileOut(EasyMock.isA(File.class), EasyMock.isA(String.class))).andThrow(new IOException("Exception"));

        EasyMock.replay(conversionService, fileTransferService, jobLocator);
        uploadDtoToJobLaunchRequestConverter.convert(uploadDto);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertNoSuchJobException() throws Exception {
        
	FileSystemResource file = new FileSystemResource("src/test/resources/org/cateproject/batch/job/test.zip");
        uploadDto.getFiles().add(new MockMultipartFile("test.zip", "test.zip", "application/zip", file.getInputStream()));
        EasyMock.expect(jobLocator.getJob("processDarwinCoreArchive")).andThrow(new NoSuchJobException("Exception"));

        EasyMock.replay(conversionService, fileTransferService, jobLocator);
        uploadDtoToJobLaunchRequestConverter.convert(uploadDto);
    }

    @Test
    public void testConvertWithMultipleFiles() throws Exception {
        
	FileSystemResource file = new FileSystemResource("src/test/resources/org/cateproject/batch/job/test.zip");
	FileSystemResource file1 = new FileSystemResource("src/test/resources/org/cateproject/web/form/unknown.x1");
        uploadDto.getFiles().add(new MockMultipartFile("test.zip", "test.zip", "application/zip", file.getInputStream()));
        uploadDto.getFiles().add(new MockMultipartFile("unknown.x1", "unknown.x1", "application/unknown", file1.getInputStream()));
        EasyMock.expect(jobLocator.getJob("processDarwinCoreArchive")).andReturn(new SimpleJob("processDarwinCoreArchive"));
        EasyMock.expect(fileTransferService.copyFileOut(EasyMock.isA(File.class), EasyMock.isA(String.class))).andReturn("STRING");
        EasyMock.expect(conversionService.convert(EasyMock.eq(new HashMap<String, Object>()), EasyMock.eq(String.class))).andReturn("TENANT_PROPERTIES");

        EasyMock.replay(conversionService, fileTransferService, jobLocator);
        JobLaunchRequest jobLaunchRequest = uploadDtoToJobLaunchRequestConverter.convert(uploadDto);
        EasyMock.verify(conversionService, fileTransferService, jobLocator);
        assertTrue("jobParameters should contain a parameter 'input.file'", jobLaunchRequest.getJobParameters().getParameters().containsKey("input.file"));
        assertTrue("jobParameters should contain a parameter 'working.dir'", jobLaunchRequest.getJobParameters().getParameters().containsKey("working.dir"));
        assertEquals("jobParameters should contain a parameter 'tenant.id' with value 'TENANT_ID'", "TENANT_ID", jobLaunchRequest.getJobParameters().getString("tenant.id"));
        assertEquals("jobParameters should contain a parameter 'tenant.properties' with value 'TENANT_PROPERTIES'", "TENANT_PROPERTIES", jobLaunchRequest.getJobParameters().getString("tenant.properties"));
        assertEquals("jobParameters should contain a parameter 'user.id' with value 'USER'", "USER", jobLaunchRequest.getJobParameters().getString("user.id"));
        assertEquals("jobParameters should contain a parameter 'launch.request.identifier' which is the same as its identifier", jobLaunchRequest.getIdentifier(), jobLaunchRequest.getJobParameters().getString("launch.request.identifier"));
    }
    
}
