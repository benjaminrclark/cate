package org.cateproject.domain.convert.solr;

import org.apache.solr.common.SolrInputDocument;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.core.convert.converter.Converter;

public class JobExecutionWritingConverter implements Converter<JobExecution, SolrInputDocument> {
 
    private static Logger logger = LoggerFactory.getLogger(JobExecutionWritingConverter.class);

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Override
    public SolrInputDocument convert(JobExecution jobExecution) {
        logger.debug("Writing JobExecution with id {}", new Object[]{jobExecution.getId()});
        SolrInputDocument solrInputDocument = new SolrInputDocument();
        solrInputDocument.addField("id", "jobexecution_" + jobExecution.getId());
        solrInputDocument.addField("jobexecution.createtime_dt", dateTimeFormatter.print(new DateTime(jobExecution.getCreateTime())));
        solrInputDocument.addField("jobexecution.jobname_t", jobExecution.getJobInstance().getJobName());
        solrInputDocument.addField("jobexecution.jobinstance_l", jobExecution.getJobInstance().getId());
        if(jobExecution.getLastUpdated() != null) {
            solrInputDocument.addField("jobexecution.lastupdated_dt", dateTimeFormatter.print(new DateTime(jobExecution.getLastUpdated())));
        }
        if(jobExecution.getEndTime() != null) {
            solrInputDocument.addField("jobexecution.endtime_dt", dateTimeFormatter.print(new DateTime(jobExecution.getEndTime())));
        }
        if(jobExecution.getExitStatus() != null) {
            solrInputDocument.addField("jobexecution.exitcode_s", jobExecution.getExitStatus().getExitCode());
            solrInputDocument.addField("jobexecution.exitdescription_s", jobExecution.getExitStatus().getExitDescription());
        }

        solrInputDocument.addField("base.id_l", jobExecution.getId());
        solrInputDocument.addField("base.class_s", "org.springframework.batch.core.JobExecution");
        
        StringBuilder summary = new StringBuilder().append(jobExecution.getJobConfigurationName()).append(" ").append(jobExecution.getJobInstance().getJobName()).append(" ").append(jobExecution.getJobParameters().toString()).append(" ").append(jobExecution.getStatus());
        if(jobExecution.getExitStatus() != null) {
            summary.append(" ").append(jobExecution.getExitStatus().getExitCode()).append(" ").append(jobExecution.getExitStatus().getExitDescription());
        }
        
        solrInputDocument.addField("base_solrsummary_t", summary.toString());
	    return solrInputDocument;
	}

}
