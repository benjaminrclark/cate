package org.cateproject.batch.job;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.cateproject.Application;
import org.cateproject.batch.TestingBatchTaskExecutorConfiguration;
import org.cateproject.file.FileTransferService;
import org.cateproject.repository.jpa.DatasetRepository;
import org.cateproject.repository.jpa.ReferenceRepository;
import org.cateproject.repository.jpa.TaxonRepository;
import org.cateproject.multitenant.MultitenantContextHolder;
import org.junit.After;
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
public class ProcessDarwinCoreArchiveIntegrationTest
{

	@Autowired
	private JobRegistry jobRegistry;

	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private FileTransferService fileTransferService;

	@Autowired
	private ConversionService conversionService;

	@Autowired
	private JobLauncher jobLauncher;

        @Autowired
        private DatasetRepository datasetRepository;

        @Autowired
        private ReferenceRepository referenceRepository;

        @Autowired
        private TaxonRepository taxonRepository;

	private String uri;

	private JobParameters jobParameters;

	@Before
	public void setUp() throws IOException, NoSuchJobException
	{
		MultitenantContextHolder.getContext().clearContextProperties();
		MultitenantContextHolder.getContext().setTenantId(null);
		jobLauncherTestUtils = new JobLauncherTestUtils();
		jobLauncherTestUtils.setJob(jobRegistry.getJob("processDarwinCoreArchive"));
		jobLauncherTestUtils.setJobLauncher(jobLauncher);
		FileSystemResource file = new FileSystemResource("src/test/resources/org/cateproject/batch/job/test.zip");
		uri = "upload://" + UUID.randomUUID().toString() + ".zip";
		fileTransferService.copyFileOut(file.getFile(), uri);
		Map<String, JobParameter> jobParametersMap = new HashMap<String, JobParameter>();
		jobParametersMap.put("input.file", new JobParameter(uri));
		jobParametersMap.put("tenant.id", new JobParameter("localhost"));
		jobParametersMap.put("user.id", new JobParameter("user"));
		jobParametersMap.put("working.dir", new JobParameter(UUID.randomUUID().toString()));
		Map<String, Object> tenantProperties = new HashMap<String, Object>();
		jobParametersMap.put("tenant.properties", new JobParameter(conversionService.convert(tenantProperties, String.class)));
		jobParameters = new JobParameters(jobParametersMap);
	}

	@Test
	public void testProcessDarwinCoreArchive() throws Exception
	{
		JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
		assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
	}

        @After
        public void tearDown() {
    	    taxonRepository.delete(taxonRepository.findAll());
    	    referenceRepository.delete(referenceRepository.findAll());
    	    datasetRepository.delete(datasetRepository.findAll());
        }
}
