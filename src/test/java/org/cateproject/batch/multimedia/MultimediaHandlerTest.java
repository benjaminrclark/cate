package org.cateproject.batch.multimedia;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.cateproject.batch.JobLaunchRequestHandler;
import org.cateproject.domain.Multimedia;
import org.cateproject.file.FileTransferService;
import org.cateproject.multitenant.MultitenantContextHolder;
import org.cateproject.repository.jpa.batch.JobLaunchRequestRepository;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class MultimediaHandlerTest {

    private MultimediaHandler multimediaHandler;

    private FileTransferService fileTransferService;

    private MultimediaFileService multimediaFileService;

    private Job job;

    private JobRegistry jobRegistry;

    private JobLaunchRequestHandler jobLaunchRequestHandler;

    private JobLaunchRequestRepository jobLaunchRequestRepository;

    private ConversionService conversionService;

    @Before
    public void setUp() {
        multimediaHandler = new MultimediaHandler();
        fileTransferService = EasyMock.createMock(FileTransferService.class);
        multimediaHandler.setFileTransferService(fileTransferService);
        multimediaFileService = EasyMock.createMock(MultimediaFileService.class);
        jobRegistry = EasyMock.createMock(JobRegistry.class);
        multimediaHandler.setMultimediaFileService(multimediaFileService);
        job = new SimpleJob("JOB_NAME");
        multimediaHandler.setJobRegistry(jobRegistry);
        jobLaunchRequestHandler = EasyMock.createMock(JobLaunchRequestHandler.class);
        multimediaHandler.setJobLaunchRequestHandler(jobLaunchRequestHandler);
        conversionService = EasyMock.createMock(ConversionService.class);
        jobLaunchRequestRepository = EasyMock.createMock(JobLaunchRequestRepository.class);
        multimediaHandler.setConversionService(conversionService);
        multimediaHandler.setJobLaunchRequestRepository(jobLaunchRequestRepository);

        MultitenantContextHolder.getContext().clearContextProperties();
        MultitenantContextHolder.getContext().setTenantId("TENANT_ID");
        TestingAuthenticationToken authentication = new TestingAuthenticationToken("USER_ID", null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @After
    public void tearDown() {
        SecurityContextHolder.clearContext();
        MultitenantContextHolder.getContext().setTenantId(null);
        MultitenantContextHolder.getContext().clearContextProperties();
    }

    @Test
    public void testProcess() {
        Multimedia multimedia = new Multimedia();

        EasyMock.replay(fileTransferService, multimediaFileService, jobLaunchRequestHandler, conversionService, jobRegistry);
        multimediaHandler.process(multimedia);
        EasyMock.verify(fileTransferService, multimediaFileService, jobLaunchRequestHandler, conversionService, jobRegistry);
    }

    @Test
    public void testProcessWithOriginalFile() throws Exception {
        Capture<JobLaunchRequest> jobLaunchRequestCapture = EasyMock.newCapture();
        Map<String, Object> expectedTenantPropertiesMap = new HashMap<String, Object>();
        expectedTenantPropertiesMap.put("ProcessingMultimediaFile", Boolean.TRUE);
        
        File originalFile = File.createTempFile("TMP","FILE");
        Multimedia multimedia = new Multimedia();
        multimedia.setIdentifier("IDENTIFIER");
        multimedia.setOriginalFile(originalFile);
        EasyMock.expect(fileTransferService.moveFileOut(EasyMock.eq(originalFile), EasyMock.isA(String.class))).andReturn("RETURN_VALUE");
        EasyMock.expect(conversionService.convert(EasyMock.eq(expectedTenantPropertiesMap), EasyMock.eq(String.class))).andReturn("TENANT_PROPERTIES");
        EasyMock.expect(jobRegistry.getJob(EasyMock.eq("processMultimedia"))).andReturn(job);
        EasyMock.expect(jobLaunchRequestRepository.save(EasyMock.isA(org.cateproject.domain.batch.JobLaunchRequest.class))).andReturn(null);
        jobLaunchRequestHandler.launch(EasyMock.and(EasyMock.capture(jobLaunchRequestCapture),EasyMock.isA(JobLaunchRequest.class)));

        EasyMock.replay(fileTransferService, multimediaFileService, jobLaunchRequestHandler, conversionService, jobRegistry, jobLaunchRequestRepository);
        multimediaHandler.process(multimedia);
        EasyMock.verify(fileTransferService, multimediaFileService, jobLaunchRequestHandler, conversionService, jobRegistry, jobLaunchRequestRepository);
        JobLaunchRequest jobLaunchRequest = jobLaunchRequestCapture.getValue();
        assertTrue("process should set the 'input.file' job property",((String)jobLaunchRequest.getJobParameters().getParameters().get("input.file").getValue()).startsWith("upload://"));
        assertEquals("process should set the 'query.string' job property",((String)jobLaunchRequest.getJobParameters().getParameters().get("query.string").getValue()),"select m from Multimedia m where m.identifier = :identifier");
        assertEquals("process should set the 'query.parameters_map' job property",((String)jobLaunchRequest.getJobParameters().getParameters().get("query.parameters_map").getValue()),"identifier=IDENTIFIER");
        assertEquals("process should set the 'tenant.id' job property",((String)jobLaunchRequest.getJobParameters().getParameters().get("tenant.id").getValue()),"TENANT_ID");
        assertEquals("process should set the 'user.id' job property",((String)jobLaunchRequest.getJobParameters().getParameters().get("user.id").getValue()),"USER_ID");
        assertEquals("process should set the 'tenant.properties' job property",((String)jobLaunchRequest.getJobParameters().getParameters().get("tenant.properties").getValue()),"TENANT_PROPERTIES");
    }

    @Test
    public void testProcessWithIdentifier() throws Exception {
        Capture<JobLaunchRequest> jobLaunchRequestCapture = EasyMock.newCapture();
        Map<String, Object> expectedTenantPropertiesMap = new HashMap<String, Object>();
        expectedTenantPropertiesMap.put("ProcessingMultimediaFile", Boolean.TRUE);
        
        Multimedia multimedia = new Multimedia();
        multimedia.setIdentifier("http://IDENTIFIER");
        EasyMock.expect(conversionService.convert(EasyMock.eq(expectedTenantPropertiesMap), EasyMock.eq(String.class))).andReturn("TENANT_PROPERTIES");
        EasyMock.expect(jobRegistry.getJob(EasyMock.eq("processMultimedia"))).andReturn(job);
        EasyMock.expect(jobLaunchRequestRepository.save(EasyMock.isA(org.cateproject.domain.batch.JobLaunchRequest.class))).andReturn(null);
        jobLaunchRequestHandler.launch(EasyMock.and(EasyMock.capture(jobLaunchRequestCapture),EasyMock.isA(JobLaunchRequest.class)));

        EasyMock.replay(fileTransferService, multimediaFileService, jobLaunchRequestHandler, conversionService, jobRegistry, jobLaunchRequestRepository);
        multimediaHandler.process(multimedia);
        EasyMock.verify(fileTransferService, multimediaFileService, jobLaunchRequestHandler, conversionService, jobRegistry, jobLaunchRequestRepository);
        JobLaunchRequest jobLaunchRequest = jobLaunchRequestCapture.getValue();
        assertNull("process should not set the 'input.file' job property",jobLaunchRequest.getJobParameters().getParameters().get("input.file"));
        assertNotNull("process should set the 'random.string' job property",jobLaunchRequest.getJobParameters().getParameters().get("random.string"));
        assertEquals("process should set the 'query.string' job property",((String)jobLaunchRequest.getJobParameters().getParameters().get("query.string").getValue()),"select m from Multimedia m where m.identifier = :identifier");
        assertEquals("process should set the 'query.parameters_map' job property",((String)jobLaunchRequest.getJobParameters().getParameters().get("query.parameters_map").getValue()),"identifier=http://IDENTIFIER");
        assertEquals("process should set the 'tenant.id' job property",((String)jobLaunchRequest.getJobParameters().getParameters().get("tenant.id").getValue()),"TENANT_ID");
        assertEquals("process should set the 'user.id' job property",((String)jobLaunchRequest.getJobParameters().getParameters().get("user.id").getValue()),"USER_ID");
        assertEquals("process should set the 'tenant.properties' job property",((String)jobLaunchRequest.getJobParameters().getParameters().get("tenant.properties").getValue()),"TENANT_PROPERTIES");
    }

    @Test(expected = RuntimeException.class)
    public void testProcessThrowsIOException() throws Exception {
        
        File originalFile = File.createTempFile("TMP","FILE");
        Multimedia multimedia = new Multimedia();
        multimedia.setIdentifier("IDENTIFIER");
        multimedia.setOriginalFile(originalFile);
        EasyMock.expect(fileTransferService.moveFileOut(EasyMock.eq(originalFile), EasyMock.isA(String.class))).andThrow(new IOException("EXPECTED"));

        EasyMock.replay(fileTransferService, multimediaFileService, jobLaunchRequestHandler, conversionService, jobRegistry);
        multimediaHandler.process(multimedia);
    }

    @Test
    public void testPrePersist() {
        Multimedia multimedia = new Multimedia();

        multimediaFileService.handleFile(EasyMock.eq(multimedia));
        EasyMock.replay(fileTransferService, multimediaFileService, jobLaunchRequestHandler, conversionService, jobRegistry);
        multimediaHandler.prePersist(multimedia);
        EasyMock.verify(fileTransferService, multimediaFileService, jobLaunchRequestHandler, conversionService, jobRegistry);
    }

    @Test
    public void testPrePersistProcessingMultimediaFile() {
        Multimedia multimedia = new Multimedia();
        MultitenantContextHolder.getContext().putContextProperty("ProcessingMultimediaFile", Boolean.TRUE);
        EasyMock.replay(fileTransferService, multimediaFileService, jobLaunchRequestHandler, conversionService, jobRegistry);
        multimediaHandler.prePersist(multimedia);
        EasyMock.verify(fileTransferService, multimediaFileService, jobLaunchRequestHandler, conversionService, jobRegistry);
    }

    @Test
    public void testPostPersist() {
        Multimedia multimedia = new Multimedia();

        EasyMock.replay(fileTransferService, multimediaFileService, jobLaunchRequestHandler, conversionService, jobRegistry);
        multimediaHandler.postPersist(multimedia);
        EasyMock.verify(fileTransferService, multimediaFileService, jobLaunchRequestHandler, conversionService, jobRegistry);
    }

    @Test
    public void testPostPersistProcessingMultimediaFile() {
        Multimedia multimedia = new Multimedia();
        multimedia.setIdentifier("http://IDENTIFIER");
        MultitenantContextHolder.getContext().putContextProperty("ProcessingMultimediaFile", Boolean.TRUE);
        EasyMock.replay(fileTransferService, multimediaFileService, jobLaunchRequestHandler, conversionService, jobRegistry);

        multimediaHandler.postPersist(multimedia);
        EasyMock.verify(fileTransferService, multimediaFileService, jobLaunchRequestHandler, conversionService, jobRegistry);
    }

    @Test
    public void testPostUpdateProcessingMultimediaFile() {
        Multimedia multimedia = new Multimedia();
        multimedia.setIdentifier("http://IDENTIFIER");
        MultitenantContextHolder.getContext().putContextProperty("ProcessingMultimediaFile", Boolean.TRUE);
        EasyMock.replay(fileTransferService, multimediaFileService, jobLaunchRequestHandler, conversionService, jobRegistry);

        multimediaHandler.postUpdate(multimedia);
        EasyMock.verify(fileTransferService, multimediaFileService, jobLaunchRequestHandler, conversionService, jobRegistry);
    }

    @Test
    public void testPostUpdateLocalFile() throws IOException {
        Multimedia extractedMultimedia = new Multimedia();
        Multimedia multimedia = new Multimedia();
        multimedia.setIdentifier("IDENTIFIER");
        File originalFile = File.createTempFile("TMP","FILE");
        multimedia.setOriginalFile(originalFile);

        EasyMock.expect(multimediaFileService.localFileInfo(EasyMock.eq(originalFile))).andReturn(extractedMultimedia);
        EasyMock.expect(multimediaFileService.filesUnchanged(EasyMock.eq(multimedia),EasyMock.eq(extractedMultimedia))).andReturn(true);

        EasyMock.replay(fileTransferService, multimediaFileService, jobLaunchRequestHandler, conversionService, jobRegistry);

        multimediaHandler.postUpdate(multimedia);
        EasyMock.verify(fileTransferService, multimediaFileService, jobLaunchRequestHandler, conversionService, jobRegistry);
    }

    @Test
    public void testPostUpdateWithIdentifier() throws Exception {
        Multimedia extractedMultimedia = new Multimedia();
        Map<String, Object> expectedTenantPropertiesMap = new HashMap<String, Object>();
        expectedTenantPropertiesMap.put("ProcessingMultimediaFile", Boolean.TRUE);
        
        Multimedia multimedia = new Multimedia();
        multimedia.setIdentifier("http://IDENTIFIER");

        EasyMock.expect(multimediaFileService.remoteFileInfo(EasyMock.eq("http://IDENTIFIER"))).andReturn(extractedMultimedia);
        EasyMock.expect(multimediaFileService.filesUnchanged(EasyMock.eq(multimedia),EasyMock.eq(extractedMultimedia))).andReturn(false);
        EasyMock.expect(conversionService.convert(EasyMock.eq(expectedTenantPropertiesMap), EasyMock.eq(String.class))).andReturn("TENANT_PROPERTIES");
        EasyMock.expect(jobRegistry.getJob(EasyMock.eq("processMultimedia"))).andReturn(job);
        EasyMock.expect(jobLaunchRequestRepository.save(EasyMock.isA(org.cateproject.domain.batch.JobLaunchRequest.class))).andReturn(null);
        jobLaunchRequestHandler.launch(EasyMock.isA(JobLaunchRequest.class));

        EasyMock.replay(fileTransferService, multimediaFileService, jobLaunchRequestHandler, conversionService, jobRegistry, jobLaunchRequestRepository);
        multimediaHandler.postUpdate(multimedia);
        EasyMock.verify(fileTransferService, multimediaFileService, jobLaunchRequestHandler, conversionService, jobRegistry, jobLaunchRequestRepository);
    }

    @Test
    public void testPostRemove() {
        Multimedia multimedia = new Multimedia();

        EasyMock.replay(fileTransferService, multimediaFileService, jobLaunchRequestHandler, conversionService, jobRegistry);
        multimediaHandler.postRemove(multimedia);
        EasyMock.verify(fileTransferService, multimediaFileService, jobLaunchRequestHandler, conversionService, jobRegistry);
    }
}
