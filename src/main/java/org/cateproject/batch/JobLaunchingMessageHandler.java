package org.cateproject.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.integration.launch.JobLaunchRequest;

public class JobLaunchingMessageHandler implements JobLaunchRequestHandler {
    private static Logger logger = LoggerFactory.getLogger(JobLaunchingMessageHandler.class);

    private JobLauncher jobLauncher;

    public JobLaunchingMessageHandler(JobLauncher jobLauncher) {
        this.jobLauncher = jobLauncher;
    }
  
    public void launch(JobLaunchRequest request) {
        try {
            jobLauncher.run(request.getJob(), request.getJobParameters());
        } catch (JobExecutionException jee) {
            logger.error("Exception running job {} with parameters {} : {}", new Object[] {request.getJob(), request.getJobParameters(), jee.getMessage()});
            throw new RuntimeException("Exception handling jobLaunchRequest",jee);
        }
    }
}
