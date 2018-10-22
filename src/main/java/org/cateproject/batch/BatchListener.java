
package org.cateproject.batch;

import java.util.List;

import org.cateproject.repository.search.batch.JobExecutionRepository;
import org.cateproject.repository.search.batch.StepExecutionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.annotation.BeforeWrite;
import org.springframework.batch.core.annotation.OnProcessError;
import org.springframework.batch.core.annotation.OnReadError;
import org.springframework.batch.core.annotation.OnWriteError;
import org.springframework.batch.core.listener.ItemListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;

public class BatchListener extends ItemListenerSupport<Object, Object> implements JobExecutionListener, StepExecutionListener {
	
	private static Logger logger = LoggerFactory.getLogger(BatchListener.class);

        @Autowired	
	private JobExecutionRepository jobExecutionRepository;

        @Autowired	
	private StepExecutionRepository stepExecutionRepository;
	
	public void setJobExecutionRepository(JobExecutionRepository jobExecutionRepository) {
		this.jobExecutionRepository = jobExecutionRepository;
	}

	public void setStepExecutionRepository(StepExecutionRepository stepExecutionRepository) {
		this.stepExecutionRepository = stepExecutionRepository;
	}

        @OnProcessError	
	public void onProcessError(Object o, Exception e) {
		logger.error(e.getMessage());
	}
	
	@OnWriteError
        public void onWriteError(Exception e, List<? extends Object> item) {
		logger.error(e.getMessage());
	}
	
	@OnReadError
	public void onReadError(Exception e) {
		logger.error(e.getMessage());
	}
	
	@BeforeWrite
	public void beforeWrite(List<? extends Object> items) {
		logger.debug("beforeWrite " + items);
	}

	@AfterJob
	public void afterJob(JobExecution jobExecution) {
		logger.debug("afterJob " + jobExecution);
		if (!jobExecution.getAllFailureExceptions().isEmpty()) {
			ExitStatus exitStatus = ExitStatus.FAILED;
			for (Throwable e : jobExecution.getAllFailureExceptions()) {
				exitStatus = addErrors(exitStatus, e);			
			}
			jobExecution.setExitStatus(exitStatus);
		}
		jobExecutionRepository.save(jobExecution);
	}

	private ExitStatus addErrors(ExitStatus exitStatus, Throwable e) {
		ExitStatus status = exitStatus.addExitDescription(e.getLocalizedMessage());
		if(e.getCause() == null) {
			return status;
		} else {
		   return addErrors(status, e.getCause());
		}
	}

	@BeforeJob
	public void beforeJob(JobExecution jobExecution) {
		logger.debug("beforeJob " + jobExecution);
		jobExecutionRepository.save(jobExecution);
	}

	@AfterStep
	public ExitStatus afterStep(StepExecution stepExecution) {
		logger.debug("afterStep " + stepExecution);
                stepExecutionRepository.save(stepExecution);
		return null;
	}

	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		logger.debug("beforeStep " + stepExecution);
                stepExecutionRepository.save(stepExecution);
	}
	
	
}
