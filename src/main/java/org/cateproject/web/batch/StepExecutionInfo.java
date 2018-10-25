package org.cateproject.web.batch;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.data.domain.Page;

public class StepExecutionInfo {
    
    private StepExecution stepExecution;

    private String message;

    public StepExecutionInfo(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    protected StepExecution getStepExecution() {
        return stepExecution;
    }

    public Long getId() {
        return stepExecution.getId();
    }

    public String getStepName() {
        return stepExecution.getStepName();
    }

    public DateTime getStartDateTime() {
        return new DateTime(stepExecution.getStartTime());
    }

    public Duration getDuration() {
        if(stepExecution.getEndTime() == null) {
            return new Duration(getStartDateTime(), new DateTime());
        } else {
            return new Duration(getStartDateTime(), new DateTime(stepExecution.getEndTime()));
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BatchStatus getStatus() {
        return stepExecution.getStatus();
    }

    public String getExitCode() {
        return stepExecution.getExitStatus().getExitCode();
    }

    public String getExitDescription() {
        return stepExecution.getExitStatus().getExitDescription();
    }

    public Integer getReadCount() {
        return stepExecution.getReadCount();
    }

    public Integer getWriteCount() {
        return stepExecution.getWriteCount();
    }

    public Integer getCommitCount() {
        return stepExecution.getCommitCount();
    }

    public Integer getRollbackCount() {
        return stepExecution.getRollbackCount();
    }
}
