package org.cateproject.batch;

import org.springframework.batch.integration.launch.JobLaunchRequest;

public interface JobLaunchRequestHandler {
    public void launch(JobLaunchRequest request); 
}
