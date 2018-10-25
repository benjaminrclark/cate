package org.cateproject.web.batch;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.JobParameters;

public class JobExecutionInfo {
    
    private JobExecution jobExecution;

    private List<StepExecutionInfo> stepExecutions;

    public JobExecutionInfo(JobExecution jobExecution) {
        this.jobExecution = jobExecution;
    }

    public JobExecutionInfo(JobExecution jobExecution, List<StepExecutionInfo> stepExecutions) {
        this.jobExecution = jobExecution;
        this.stepExecutions = stepExecutions;
    }

    protected JobExecution getJobExecution() {
        return jobExecution;
    }

    public Long getId() {
        return jobExecution.getId();
    }

    public String getJobName() {
        return jobExecution.getJobInstance().getJobName();
    }

    public Long getJobInstance() {
        return jobExecution.getJobInstance().getId();
    }

    public JobParameters getJobParameters() {
        return jobExecution.getJobParameters();
    }

    public DateTime getStartDateTime() {
        return new DateTime(jobExecution.getStartTime());
    }

    public Duration getDuration() {
        if(jobExecution.isRunning()) {
            return new Duration(getStartDateTime(), new DateTime());
        } else {
            return new Duration(getStartDateTime(), new DateTime(jobExecution.getEndTime()));
        }
    }

    public List<StepExecutionInfo> getStepExecutions() {
        return stepExecutions;
    }

    public BatchStatus getStatus() {
        return jobExecution.getStatus();
    }

    public String getExitCode() {
        return jobExecution.getExitStatus().getExitCode();
    }

    public String getExitDescription() {
        return jobExecution.getExitStatus().getExitDescription();
    }

    public Integer getStepExecutionCount() {
        return jobExecution.getStepExecutions().size();
    }
}
