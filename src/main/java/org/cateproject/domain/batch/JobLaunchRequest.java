package org.cateproject.domain.batch;

import java.util.UUID;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.batch.core.launch.NoSuchJobException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.Type;

@Entity
@Configurable
public class JobLaunchRequest extends org.springframework.batch.integration.launch.JobLaunchRequest {

        @Transient
        @Autowired
        private JobLocator jobLocator;

	@Id
	@GeneratedValue(generator = "table-hilo", strategy = GenerationType.TABLE)
	@Column(name = "id")
	private Long id;

	@Version
	@Column(name = "version")
	private Integer version;

        private String job;

	@Type(type = "jobParametersUserType")
        private JobParameters jobParameters;

        private String identifier;

        private Long jobExecutionId;

        public JobLaunchRequest(Job job, JobParameters jobParameters) {
            super(job, jobParameters);
            this.job = job.getName();
            this.jobParameters = jobParameters;
            this.identifier = jobParameters.getString("launch.request.identifier", UUID.randomUUID().toString()); 
        }

        @Override
        public Job getJob() {
            try {   
                return jobLocator.getJob(this.job);
            } catch (NoSuchJobException nsje) {
                throw new RuntimeException("No such job " + job);
            }
        }

        @Override
        public JobParameters getJobParameters() {
            return jobParameters;
        }

        private JobLaunchRequest() {
            super(null, null);
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }

        public void setVersion(Integer version) {
            this.version = version;
        }

        public Integer getVersion() {
            return version;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public String getIdentifier() {
            return identifier;
        }

        public void setJobExecutionId(Long jobExecutionId) {
            this.jobExecutionId = jobExecutionId;
        }

        public Long getJobExecutionId() {
            return jobExecutionId;
        }

        public void setJobLocator(JobLocator jobLocator) {
            this.jobLocator = jobLocator;
        }
}
