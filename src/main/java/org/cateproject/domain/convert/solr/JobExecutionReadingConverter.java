package org.cateproject.domain.convert.solr;

import org.apache.solr.common.SolrDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

public class JobExecutionReadingConverter implements Converter<SolrDocument, JobExecution> {

        private static Logger logger = LoggerFactory.getLogger(JobExecutionReadingConverter.class);	

	@Autowired
	private JobExplorer jobExplorer;

        public void setJobExplorer(JobExplorer jobExplorer) {
            this.jobExplorer = jobExplorer;
        }

	@Override
	public JobExecution convert(SolrDocument solrDocument) {
                logger.debug("Reading Document id: {}, class: {}", new Object[]{solrDocument.getFieldValue("base.id_l"), solrDocument.getFieldValue("base.class_s")});
		if(solrDocument.getFieldValue("base.class_s").equals("org.springframework.batch.core.JobExecution")) {
		    Long id = (Long)solrDocument.getFieldValue("base.id_l");
		    return jobExplorer.getJobExecution(id);
		} else {
                    throw new RuntimeException("Could not instantiate bean " + solrDocument.getFieldValue("base.class_s"));
		}
    }

}
