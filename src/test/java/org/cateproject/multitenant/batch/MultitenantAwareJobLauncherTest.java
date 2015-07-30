package org.cateproject.multitenant.batch;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.cateproject.multitenant.MultitenantContextHolder;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.TaskRejectedException;

public class MultitenantAwareJobLauncherTest {

    private MultitenantAwareJobLauncher multitenantAwareJobLauncher;

    private TaskExecutor taskExecutor;

    private JobRepository jobRepository;

    private Job job;

    private JobParametersValidator jobParametersValidator;

    private JobExecution jobExecution;
  
    private JobExecution lastJobExecution;

    private ConversionService conversionService;

    @Before
    public void setUp() {
        multitenantAwareJobLauncher = new MultitenantAwareJobLauncher();
        taskExecutor = EasyMock.createMock(TaskExecutor.class);
        jobRepository = EasyMock.createMock(JobRepository.class);
        job = EasyMock.createMock(Job.class);
        jobParametersValidator = EasyMock.createMock(JobParametersValidator.class);
        jobExecution = EasyMock.createMock(JobExecution.class);
        lastJobExecution = EasyMock.createMock(JobExecution.class);
        conversionService = EasyMock.createMock(ConversionService.class);

        multitenantAwareJobLauncher.setTaskExecutor(taskExecutor);
        multitenantAwareJobLauncher.setJobRepository(jobRepository);
        multitenantAwareJobLauncher.setConversionService(conversionService);
    }

    @Test(expected = IllegalStateException.class)
    public void testAfterPropertiesSetWithoutJobRepository() throws Exception {
        multitenantAwareJobLauncher.setJobRepository(null);
        EasyMock.replay(jobRepository,taskExecutor, job, jobParametersValidator, jobExecution, conversionService);        

        multitenantAwareJobLauncher.afterPropertiesSet();
    }

    @Test
    public void testAfterPropertiesSetWithoutTaskExecutor() throws Exception {
        multitenantAwareJobLauncher.setTaskExecutor(null);
         
        EasyMock.replay(jobRepository, taskExecutor, job, jobParametersValidator, jobExecution, lastJobExecution, conversionService);        
        multitenantAwareJobLauncher.afterPropertiesSet();

        EasyMock.verify(jobRepository, taskExecutor, job, jobParametersValidator, jobExecution, lastJobExecution, conversionService);
    }

    @Test
    public void testRunForTheFirstTime() throws Exception{
        Map<String, JobParameter> parameters = new HashMap<String, JobParameter>(); 
        Map<String, Object> tenantProperties = new HashMap<String, Object>();
        tenantProperties.put("PROPERTY_1", "PROPERTY_1_OTHER_VALUE");
        tenantProperties.put("PROPERTY_2", "PROPERTY_2_VALUE");
        parameters.put("tenantId", new JobParameter("TENANT_ID"));
        parameters.put("tenantProperties", new JobParameter("TENANT_PROPERTIES")); 
        Capture<Runnable> runnable = new Capture<Runnable>();
        JobParameters jobParameters = new JobParameters(parameters);
        EasyMock.expect(job.getName()).andReturn("JOB_NAME").anyTimes();
        EasyMock.expect(jobRepository.getLastJobExecution(EasyMock.eq("JOB_NAME"), EasyMock.eq(jobParameters))).andReturn(null);
        EasyMock.expect(job.getJobParametersValidator()).andReturn(jobParametersValidator);
        jobParametersValidator.validate(EasyMock.eq(jobParameters));
        EasyMock.expect(jobRepository.createJobExecution(EasyMock.eq("JOB_NAME"), EasyMock.eq(jobParameters))).andReturn(jobExecution);
        EasyMock.expect(conversionService.convert(EasyMock.eq("TENANT_PROPERTIES"), EasyMock.eq(Map.class))).andReturn(tenantProperties);
        taskExecutor.execute(EasyMock.and(EasyMock.isA(Runnable.class), EasyMock.capture(runnable)));
        EasyMock.expectLastCall().andAnswer(new IAnswer() {
            public Object answer() {
                runnable.getValue().run();
                return null;
            } 
        });
        job.execute(EasyMock.eq(jobExecution));
        EasyMock.expect(jobExecution.getStatus()).andReturn(BatchStatus.COMPLETED);

        EasyMock.replay(jobRepository, taskExecutor, job, jobParametersValidator, jobExecution, lastJobExecution, conversionService);
        MultitenantContextHolder.getContext().putContextProperty("PROPERTY_1", "PROPERTY_1_VALUE"); 
        assertEquals("run should return the job execution", jobExecution, multitenantAwareJobLauncher.run(job, jobParameters));
        assertEquals("MultitenantContext.properties should not be changed by running a batch job", MultitenantContextHolder.getContext().getContextProperty("PROPERTY_1"), "PROPERTY_1_VALUE");
        assertEquals("MultitenantContext.properties should not be changed by running a batch job", MultitenantContextHolder.getContext().getContextProperty("PROPERTY_2"), null);
        EasyMock.verify(jobRepository, taskExecutor, job, jobParametersValidator, jobExecution, lastJobExecution, conversionService);
        
    }

    @Test(expected = JobRestartException.class)
    public void testRunSubsequentlyNotRestartable() throws Exception{
        Map<String, JobParameter> parameters = new HashMap<String, JobParameter>(); 
        Map<String, Object> tenantProperties = new HashMap<String, Object>();
        tenantProperties.put("PROPERTY_1", "PROPERTY_1_OTHER_VALUE");
        tenantProperties.put("PROPERTY_2", "PROPERTY_2_VALUE");
        parameters.put("tenantId", new JobParameter("TENANT_ID"));
        parameters.put("tenantProperties", new JobParameter("TENANT_PROPERTIES")); 
        JobParameters jobParameters = new JobParameters(parameters);
        EasyMock.expect(job.getName()).andReturn("JOB_NAME").anyTimes();
        EasyMock.expect(jobRepository.getLastJobExecution(EasyMock.eq("JOB_NAME"), EasyMock.eq(jobParameters))).andReturn(lastJobExecution);
        EasyMock.expect(job.isRestartable()).andReturn(false);
        EasyMock.replay(jobRepository, taskExecutor, job, jobParametersValidator, jobExecution, lastJobExecution, conversionService);
        MultitenantContextHolder.getContext().putContextProperty("PROPERTY_1", "PROPERTY_1_VALUE"); 
        assertEquals("run should return the job execution", jobExecution, multitenantAwareJobLauncher.run(job, jobParameters));

        assertEquals("MultitenantContext.properties should not be changed by running a batch job", MultitenantContextHolder.getContext().getContextProperty("PROPERTY_1"), "PROPERTY_1_VALUE");
        assertEquals("MultitenantContext.properties should not be changed by running a batch job", MultitenantContextHolder.getContext().getContextProperty("PROPERTY_2"), null);
        EasyMock.verify(jobRepository, taskExecutor, job, jobParametersValidator, jobExecution, lastJobExecution, conversionService);
        
    }

    @Test
    public void testRunSubsequentlyRestartable() throws Exception{
        Map<String, JobParameter> parameters = new HashMap<String, JobParameter>(); 
        Map<String, Object> tenantProperties = new HashMap<String, Object>();
        tenantProperties.put("PROPERTY_1", "PROPERTY_1_OTHER_VALUE");
        tenantProperties.put("PROPERTY_2", "PROPERTY_2_VALUE");
        parameters.put("tenantId", new JobParameter("TENANT_ID"));
        parameters.put("tenantProperties", new JobParameter("TENANT_PROPERTIES")); 
        Capture<Runnable> runnable = new Capture<Runnable>();
        JobParameters jobParameters = new JobParameters(parameters);
        EasyMock.expect(job.getName()).andReturn("JOB_NAME").anyTimes();
        EasyMock.expect(jobRepository.getLastJobExecution(EasyMock.eq("JOB_NAME"), EasyMock.eq(jobParameters))).andReturn(lastJobExecution);
        EasyMock.expect(job.isRestartable()).andReturn(true);
        EasyMock.expect(job.getJobParametersValidator()).andReturn(jobParametersValidator);
        jobParametersValidator.validate(EasyMock.eq(jobParameters));
        EasyMock.expect(jobRepository.createJobExecution(EasyMock.eq("JOB_NAME"), EasyMock.eq(jobParameters))).andReturn(jobExecution);
        EasyMock.expect(conversionService.convert(EasyMock.eq("TENANT_PROPERTIES"), EasyMock.eq(Map.class))).andReturn(tenantProperties);
        taskExecutor.execute(EasyMock.and(EasyMock.isA(Runnable.class), EasyMock.capture(runnable)));
        EasyMock.expectLastCall().andAnswer(new IAnswer() {
            public Object answer() {
                runnable.getValue().run();
                return null;
            } 
        });
        job.execute(EasyMock.eq(jobExecution));
        EasyMock.expect(jobExecution.getStatus()).andReturn(BatchStatus.COMPLETED);

        EasyMock.replay(jobRepository, taskExecutor, job, jobParametersValidator, jobExecution, lastJobExecution, conversionService);
        MultitenantContextHolder.getContext().putContextProperty("PROPERTY_1", "PROPERTY_1_VALUE"); 
        assertEquals("run should return the job execution", jobExecution, multitenantAwareJobLauncher.run(job, jobParameters));
        assertEquals("MultitenantContext.properties should not be changed by running a batch job", MultitenantContextHolder.getContext().getContextProperty("PROPERTY_1"), "PROPERTY_1_VALUE");
        assertEquals("MultitenantContext.properties should not be changed by running a batch job", MultitenantContextHolder.getContext().getContextProperty("PROPERTY_2"), null);
        EasyMock.verify(jobRepository, taskExecutor, job, jobParametersValidator, jobExecution, lastJobExecution, conversionService);
        
    }

    @Test
    public void testRunThrowRuntimeException() throws Exception{
        Map<String, JobParameter> parameters = new HashMap<String, JobParameter>(); 
        Map<String, Object> tenantProperties = new HashMap<String, Object>();
        tenantProperties.put("PROPERTY_1", "PROPERTY_1_OTHER_VALUE");
        tenantProperties.put("PROPERTY_2", "PROPERTY_2_VALUE");
        parameters.put("tenantId", new JobParameter("TENANT_ID"));
        parameters.put("tenantProperties", new JobParameter("TENANT_PROPERTIES")); 
        Capture<Runnable> runnable = new Capture<Runnable>();
        JobParameters jobParameters = new JobParameters(parameters);
        RuntimeException runtimeException = new RuntimeException("RUNTIME_EXCEPTION");

        EasyMock.expect(job.getName()).andReturn("JOB_NAME").anyTimes();
        EasyMock.expect(jobRepository.getLastJobExecution(EasyMock.eq("JOB_NAME"), EasyMock.eq(jobParameters))).andReturn(null);
        EasyMock.expect(job.getJobParametersValidator()).andReturn(jobParametersValidator);
        jobParametersValidator.validate(EasyMock.eq(jobParameters));
        EasyMock.expect(jobRepository.createJobExecution(EasyMock.eq("JOB_NAME"), EasyMock.eq(jobParameters))).andReturn(jobExecution);
        EasyMock.expect(conversionService.convert(EasyMock.eq("TENANT_PROPERTIES"), EasyMock.eq(Map.class))).andReturn(tenantProperties);
        taskExecutor.execute(EasyMock.and(EasyMock.isA(Runnable.class), EasyMock.capture(runnable)));
        EasyMock.expectLastCall().andAnswer(new IAnswer() {
            public Object answer() {
                runnable.getValue().run();
                return null;
            } 
        });
        job.execute(EasyMock.eq(jobExecution));
        EasyMock.expectLastCall().andThrow(runtimeException);

        EasyMock.replay(jobRepository, taskExecutor, job, jobParametersValidator, jobExecution, lastJobExecution, conversionService);
        MultitenantContextHolder.getContext().putContextProperty("PROPERTY_1", "PROPERTY_1_VALUE");
        try {
            multitenantAwareJobLauncher.run(job, jobParameters);
        } catch (Throwable t) {
            assertEquals("RuntimeException should be rethrown", t, runtimeException);
        }
        assertEquals("MultitenantContext.properties should not be changed by running a batch job", MultitenantContextHolder.getContext().getContextProperty("PROPERTY_1"), "PROPERTY_1_VALUE");
        assertEquals("MultitenantContext.properties should not be changed by running a batch job", MultitenantContextHolder.getContext().getContextProperty("PROPERTY_2"), null);
        EasyMock.verify(jobRepository, taskExecutor, job, jobParametersValidator, jobExecution, lastJobExecution, conversionService);
        
    }
    
    @Test
    public void testRunThrowError() throws Exception{
        Map<String, JobParameter> parameters = new HashMap<String, JobParameter>(); 
        Map<String, Object> tenantProperties = new HashMap<String, Object>();
        tenantProperties.put("PROPERTY_1", "PROPERTY_1_OTHER_VALUE");
        tenantProperties.put("PROPERTY_2", "PROPERTY_2_VALUE");
        parameters.put("tenantId", new JobParameter("TENANT_ID"));
        parameters.put("tenantProperties", new JobParameter("TENANT_PROPERTIES")); 
        Capture<Runnable> runnable = new Capture<Runnable>();
        JobParameters jobParameters = new JobParameters(parameters);
        Error error = new Error("ERROR");

        EasyMock.expect(job.getName()).andReturn("JOB_NAME").anyTimes();
        EasyMock.expect(jobRepository.getLastJobExecution(EasyMock.eq("JOB_NAME"), EasyMock.eq(jobParameters))).andReturn(null);
        EasyMock.expect(job.getJobParametersValidator()).andReturn(jobParametersValidator);
        jobParametersValidator.validate(EasyMock.eq(jobParameters));
        EasyMock.expect(jobRepository.createJobExecution(EasyMock.eq("JOB_NAME"), EasyMock.eq(jobParameters))).andReturn(jobExecution);
        EasyMock.expect(conversionService.convert(EasyMock.eq("TENANT_PROPERTIES"), EasyMock.eq(Map.class))).andReturn(tenantProperties);
        taskExecutor.execute(EasyMock.and(EasyMock.isA(Runnable.class), EasyMock.capture(runnable)));
        EasyMock.expectLastCall().andAnswer(new IAnswer() {
            public Object answer() {
                runnable.getValue().run();
                return null;
            } 
        });
        job.execute(EasyMock.eq(jobExecution));
        EasyMock.expectLastCall().andThrow(error);

        EasyMock.replay(jobRepository, taskExecutor, job, jobParametersValidator, jobExecution, lastJobExecution, conversionService);
        MultitenantContextHolder.getContext().putContextProperty("PROPERTY_1", "PROPERTY_1_VALUE");
        try {
            multitenantAwareJobLauncher.run(job, jobParameters);
        } catch (Throwable t) {
            assertEquals("Error should be rethrown", t, error);
        }
        assertEquals("MultitenantContext.properties should not be changed by running a batch job", MultitenantContextHolder.getContext().getContextProperty("PROPERTY_1"), "PROPERTY_1_VALUE");
        assertEquals("MultitenantContext.properties should not be changed by running a batch job", MultitenantContextHolder.getContext().getContextProperty("PROPERTY_2"), null);
        EasyMock.verify(jobRepository, taskExecutor, job, jobParametersValidator, jobExecution, lastJobExecution, conversionService);
        
    }

    @Test
    public void testRunRejectTask() throws Exception{
        Map<String, JobParameter> parameters = new HashMap<String, JobParameter>(); 
        Map<String, Object> tenantProperties = new HashMap<String, Object>();
        tenantProperties.put("PROPERTY_1", "PROPERTY_1_OTHER_VALUE");
        tenantProperties.put("PROPERTY_2", "PROPERTY_2_VALUE");
        parameters.put("tenantId", new JobParameter("TENANT_ID"));
        parameters.put("tenantProperties", new JobParameter("TENANT_PROPERTIES")); 
        Capture<Runnable> runnable = new Capture<Runnable>();
        JobParameters jobParameters = new JobParameters(parameters);
        TaskRejectedException taskRejectedException = new TaskRejectedException("TASK_REJECTED_EXCEPTION");

        EasyMock.expect(job.getName()).andReturn("JOB_NAME").anyTimes();
        EasyMock.expect(jobRepository.getLastJobExecution(EasyMock.eq("JOB_NAME"), EasyMock.eq(jobParameters))).andReturn(null);
        EasyMock.expect(job.getJobParametersValidator()).andReturn(jobParametersValidator);
        jobParametersValidator.validate(EasyMock.eq(jobParameters));
        EasyMock.expect(jobRepository.createJobExecution(EasyMock.eq("JOB_NAME"), EasyMock.eq(jobParameters))).andReturn(jobExecution);
        EasyMock.expect(conversionService.convert(EasyMock.eq("TENANT_PROPERTIES"), EasyMock.eq(Map.class))).andReturn(tenantProperties);
        taskExecutor.execute(EasyMock.and(EasyMock.isA(Runnable.class), EasyMock.capture(runnable)));
        EasyMock.expectLastCall().andThrow(taskRejectedException);
        jobExecution.upgradeStatus(EasyMock.eq(BatchStatus.FAILED));
        EasyMock.expect(jobExecution.getExitStatus()).andReturn(ExitStatus.UNKNOWN);
        jobExecution.setExitStatus(EasyMock.isA(ExitStatus.class));
        jobRepository.update(EasyMock.eq(jobExecution));

        EasyMock.replay(jobRepository, taskExecutor, job, jobParametersValidator, jobExecution, lastJobExecution, conversionService);
        MultitenantContextHolder.getContext().putContextProperty("PROPERTY_1", "PROPERTY_1_VALUE");
        assertEquals("run should return jobExecution", multitenantAwareJobLauncher.run(job, jobParameters), jobExecution);
        assertEquals("MultitenantContext.properties should not be changed by running a batch job", MultitenantContextHolder.getContext().getContextProperty("PROPERTY_1"), "PROPERTY_1_VALUE");
        assertEquals("MultitenantContext.properties should not be changed by running a batch job", MultitenantContextHolder.getContext().getContextProperty("PROPERTY_2"), null);
        EasyMock.verify(jobRepository, taskExecutor, job, jobParametersValidator, jobExecution, lastJobExecution, conversionService);
        
    }


    @Test
    public void testRunRejectTaskFailed() throws Exception{
        Map<String, JobParameter> parameters = new HashMap<String, JobParameter>(); 
        Map<String, Object> tenantProperties = new HashMap<String, Object>();
        tenantProperties.put("PROPERTY_1", "PROPERTY_1_OTHER_VALUE");
        tenantProperties.put("PROPERTY_2", "PROPERTY_2_VALUE");
        parameters.put("tenantId", new JobParameter("TENANT_ID"));
        parameters.put("tenantProperties", new JobParameter("TENANT_PROPERTIES")); 
        Capture<Runnable> runnable = new Capture<Runnable>();
        JobParameters jobParameters = new JobParameters(parameters);
        TaskRejectedException taskRejectedException = new TaskRejectedException("TASK_REJECTED_EXCEPTION");

        EasyMock.expect(job.getName()).andReturn("JOB_NAME").anyTimes();
        EasyMock.expect(jobRepository.getLastJobExecution(EasyMock.eq("JOB_NAME"), EasyMock.eq(jobParameters))).andReturn(null);
        EasyMock.expect(job.getJobParametersValidator()).andReturn(jobParametersValidator);
        jobParametersValidator.validate(EasyMock.eq(jobParameters));
        EasyMock.expect(jobRepository.createJobExecution(EasyMock.eq("JOB_NAME"), EasyMock.eq(jobParameters))).andReturn(jobExecution);
        EasyMock.expect(conversionService.convert(EasyMock.eq("TENANT_PROPERTIES"), EasyMock.eq(Map.class))).andReturn(tenantProperties);
        taskExecutor.execute(EasyMock.and(EasyMock.isA(Runnable.class), EasyMock.capture(runnable)));
        EasyMock.expectLastCall().andThrow(taskRejectedException);
        jobExecution.upgradeStatus(EasyMock.eq(BatchStatus.FAILED));
        EasyMock.expect(jobExecution.getExitStatus()).andReturn(ExitStatus.FAILED);
        jobRepository.update(EasyMock.eq(jobExecution));

        EasyMock.replay(jobRepository, taskExecutor, job, jobParametersValidator, jobExecution, lastJobExecution, conversionService);
        MultitenantContextHolder.getContext().putContextProperty("PROPERTY_1", "PROPERTY_1_VALUE");
        assertEquals("run should return jobExecution", multitenantAwareJobLauncher.run(job, jobParameters), jobExecution);
        assertEquals("MultitenantContext.properties should not be changed by running a batch job", MultitenantContextHolder.getContext().getContextProperty("PROPERTY_1"), "PROPERTY_1_VALUE");
        assertEquals("MultitenantContext.properties should not be changed by running a batch job", MultitenantContextHolder.getContext().getContextProperty("PROPERTY_2"), null);
        EasyMock.verify(jobRepository, taskExecutor, job, jobParametersValidator, jobExecution, lastJobExecution, conversionService);
        
    }
}
