package org.cateproject.web.batch;

public class JobInstanceInfo {
   
    private JobExecutionInfo lastJobExecution;

    private Long jobExecutionCount;

    public void setLastJobExecution(JobExecutionInfo lastJobExecution) {
        this.lastJobExecution = lastJobExecution;
    }

    public JobExecutionInfo getLastJobExecution() {
        return lastJobExecution;
    }

    public void setJobExecutionCount(Long jobExecutionCount) {
        this.jobExecutionCount = jobExecutionCount;
    }

    public Long getJobExecutionCount() {
        return jobExecutionCount;
    }
}
