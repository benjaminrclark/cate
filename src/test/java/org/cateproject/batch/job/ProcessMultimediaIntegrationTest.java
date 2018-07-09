package org.cateproject.batch.job;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.cateproject.Application;
import org.cateproject.batch.TestingBatchTaskExecutorConfiguration;
import org.cateproject.domain.Multimedia;
import org.cateproject.file.FileTransferService;
import org.cateproject.multitenant.MultitenantContextHolder;
import org.cateproject.repository.jpa.MultimediaRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class, TestingBatchTaskExecutorConfiguration.class})
@ActiveProfiles({"default", "integration-test"})
@IntegrationTest
public class ProcessMultimediaIntegrationTest {

    @Autowired
    private JobRegistry jobRegistry;

    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private MultimediaRepository multimediaRepository;

    @Autowired
    private FileTransferService fileTransferService;

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private JobLauncher jobLauncher;

    private String uri;

    private JobParameters jobParameters;

    @Before
    public void setUp() throws IOException, NoSuchJobException {
        MultitenantContextHolder.getContext().clearContextProperties();
        MultitenantContextHolder.getContext().setTenantId(null);
        jobLauncherTestUtils = new JobLauncherTestUtils();
        jobLauncherTestUtils.setJob(jobRegistry.getJob("processMultimedia"));
        jobLauncherTestUtils.setJobLauncher(jobLauncher);
        Multimedia multimedia = new Multimedia();
        multimedia.setIdentifier("1234");
        multimediaRepository.save(multimedia); 
        FileSystemResource file = new FileSystemResource("src/test/resources/org/cateproject/batch/job/test.jpg");
        uri = "upload://" + UUID.randomUUID().toString() + ".jpg";
        fileTransferService.copyFileOut(file.getFile(),uri);
        Map<String,JobParameter> jobParametersMap = new HashMap<String,JobParameter>();
	jobParametersMap.put("input.file",new JobParameter(uri));
	jobParametersMap.put("query.string", new JobParameter("select m from Multimedia m where m.identifier = :identifier"));
    	jobParametersMap.put("query.parameters_map", new JobParameter("identifier=" + multimedia.getIdentifier()));
        jobParametersMap.put("tenant.id", new JobParameter("localhost"));
        jobParametersMap.put("user.id", new JobParameter("user"));
        Map<String,Object> tenantProperties = new HashMap<String, Object>();
        tenantProperties.put("ProcessingMultimediaFile", Boolean.TRUE);
        jobParametersMap.put("tenant.properties", new JobParameter(conversionService.convert(tenantProperties,String.class)));
        jobParameters = new JobParameters(jobParametersMap);
    }

    @Test
    public void testProcessMultimedia() throws Exception {
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        assertEquals(BatchStatus.COMPLETED,jobExecution.getStatus());
    }
}
