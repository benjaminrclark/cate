package org.cateproject.batch;

import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(name = "remoteJobLaunchRequests", defaultRequestChannel = "outgoingJobLaunchRequests")
public interface RemoteJobLaunchRequestHandler extends JobLaunchRequestHandler {
    public void launch(JobLaunchRequest request);
}
