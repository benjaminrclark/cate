package org.cateproject.batch.job.darwincore;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

public class HaveRemoteDatasetDecider implements JobExecutionDecider {
  
  private String resourceName;

  public HaveRemoteDatasetDecider(String resourceName) {
      this.resourceName = resourceName;
  }

  public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
      String resource = jobExecution.getExecutionContext().getString(resourceName);
      if(resource.startsWith("upload://") || resource.startsWith("static://")) {
          return new FlowExecutionStatus("Local Dataset");
      } else {
          return new FlowExecutionStatus("Remote Dataset");
      }
  }
}
